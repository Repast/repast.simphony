/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.xml.sax.SAXException;

import repast.simphony.batch.parameter.ParametersToInput;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.SessionsDriver;

import com.jgoodies.binding.PresentationModel;

/**
 * Mediates the interactions between the various batch run configuration
 * components.
 * 
 * @author Nick Collier
 */
public class BatchConfigMediator {

  private static Logger logger = Logger.getLogger(BatchConfigMediator.class);

  // the current state of the model
  private BatchRunConfigBean model = new BatchRunConfigBean();

  private ConsolePanel console = new ConsolePanel();
  private HostsPanel hostsPanel;
  private ModelPanel modelPanel;
  private BatchParamPanel bpPanel;
  private JTabbedPane tabs = new JTabbedPane();
  private JLabel status = new JLabel();

  private PresentationModel<BatchRunConfigBean> pModel = new PresentationModel<BatchRunConfigBean>(
      model);

  private File configFile = null;
  private boolean dirty = false;
  private Appender stdout;

  public BatchConfigMediator(File modelDirectory) {
    this();
    model.setModelDirectory(modelDirectory.getAbsolutePath());
  }

  public BatchConfigMediator() {
    // append logging output to the console
    Logger logger = Logger.getLogger("repast.simphony.batch");
    stdout = logger.getAppender("stdout");
    Layout layout = stdout.getLayout();
    logger.removeAppender(stdout);
    Appender tap = new TextAreaAppender(console, layout);
    tap.setName(TextAreaAppender.class.getName());
    logger.addAppender(tap);

    pModel.addPropertyChangeListener("buffering", new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent arg0) {
        setDirty(arg0.getNewValue().equals(Boolean.TRUE));
      }
    });

    tabs.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent evt) {
        tabChanged();
      }
    });

    status.setFont(status.getFont().deriveFont(12f));
    status.setForeground(Color.RED);
    status.setText(" ");
  }

  private void tabChanged() {
    // status.setText(tabs.getTitleAt(tabs.getSelectedIndex()));
  }

  private void setDirty(boolean dirty) {
    this.dirty = dirty;
    
  }

  public void onExit() {
    Logger logger = Logger.getLogger("repast.simphony.batch");
    logger.addAppender(stdout);
  }

  public void updateStatusBar(Color color, String msg) {
    status.setForeground(color);
    status.setText(msg);
  }

  JComponent getStatusBar() {
    return status;
  }

  /**
   * Adds the tab components to the tabbed pane.
   * 
   * @param tabs
   */
  public JTabbedPane createTabs() {
    modelPanel = new ModelPanel(pModel);
    tabs.addTab("Model", modelPanel);
    bpPanel = new BatchParamPanel(this, pModel);
    tabs.addTab("Batch Parameters", bpPanel);
    hostsPanel = new HostsPanel(pModel);
    tabs.addTab("Hosts", hostsPanel);
    tabs.addTab("Console", console);
    hostsPanel.init(model);

    if (model.getModelDirectory().length() > 0) {
      updateFromModel();
      // this should set dirty back to false
      commitAll();
      dirty = false;
    }
    return tabs;
  }

  private ValidationResult validateAll() {
    ValidationResult result = bpPanel.validateInput();
    if (!result.equals(ValidationResult.SUCCESS))
      return result;
    result = modelPanel.validateInput();
    if (!result.equals(ValidationResult.SUCCESS))
      return result;
    result = hostsPanel.validateInput();
    return result;
  }

  private void commitAll() {
    pModel.triggerCommit();
    hostsPanel.commit(model);
    modelPanel.commit(model);
  }

  private void askSave() {
    int ret = JOptionPane.showConfirmDialog(console.getParent().getParent(),
        "Do you want to save the changes you have made to the batch run configuration?",
        "Save Changes?", JOptionPane.YES_NO_OPTION);
    if (ret == JOptionPane.YES_OPTION) {
      saveConfig();
    }
  }

  public void newConfig() {
    if (dirty) {
      askSave();
    }
    model = new BatchRunConfigBean();
    pModel.setBean(model);
    pModel.triggerFlush();
    hostsPanel.init(model);
  }

  public void openConfig() {
    if (dirty) {
      askSave();
    }

    File file = configFile;
    if (file == null) {
      file = new File(model.getModelDirectory());
      if (!file.exists()) {
        file = new File(System.getProperty("user.home"), "batch_configuration.properties");
      } else {
        File batchDir = new File(file, "batch");
        if (batchDir.exists()) {
          file = new File(batchDir, "batch_configuration.properties");
        } else {
          file = new File(file, "batch_configuration.properties");
        }
      }
    }

    JFileChooser chooser = new JFileChooser(file);
    chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setDialogTitle("Open Configuration");

    int retVal = chooser.showOpenDialog(console.getParent().getParent());
    if (retVal == JFileChooser.APPROVE_OPTION)
      try {
        model.load(chooser.getSelectedFile());
        hostsPanel.init(model);
        modelPanel.init(model);
        configFile = chooser.getSelectedFile();
        pModel.triggerFlush();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
  }

  public void saveConfig() {
    commitAll();
    if (configFile == null) {
      saveConfigAs();
    } else {
      try {
        model.save(configFile);
        setDirty(false);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void saveConfigAs(File file) {
    JFileChooser chooser = new JFileChooser();
    chooser.setSelectedFile(file);
    chooser.setDialogType(JFileChooser.SAVE_DIALOG);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int retVal = chooser.showSaveDialog(console.getParent().getParent());
    if (retVal == JFileChooser.APPROVE_OPTION)
      try {
        model.save(chooser.getSelectedFile());
        setDirty(false);
        configFile = chooser.getSelectedFile();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

  }

  public void saveConfigAs() {
    commitAll();
    if (configFile == null) {
      File file = new File(model.getModelDirectory());
      if (!file.exists()) {
        saveConfigAs(new File(System.getProperty("user.home"), "batch_configuration.properties"));
      } else {
        File batchDir = new File(file, "batch");
        if (batchDir.exists()) {
          saveConfigAs(new File(batchDir, "batch_configuration.properties"));
        } else {
          saveConfigAs(new File(file, "batch_configuration.properties"));
        }
      }
    } else {
      saveConfigAs(configFile);
    }

  }

  public void updateFromModel() {
    File modelDir = new File(modelPanel.getModelDirectory());
    modelPanel.update(modelDir);
    File scenario = new File(modelPanel.getScenarioDirectory());
    if (scenario.exists())
      bpPanel.update(scenario);
  }

  private void writeConfigFile(File configFile) throws IOException {
    // create the config.properties file
    BufferedWriter writer = null;
    try {
      configFile.getParentFile().mkdirs();
      writer = new BufferedWriter(new FileWriter(configFile));
      writer.write(Configuration.MA_KEY + " = "
      + convertPath(new File(model.getOutputDirectory(), "complete_model.jar")
              .getCanonicalPath()) + "\n");
      writer.write(Configuration.BATCH_PARAMS_KEY + " = scenario.rs/batch_params.xml\n");
      writer.write(Configuration.SSH_DIR_KEY + " = "
          + convertPath(new File(model.getKeyDirectory()).getCanonicalPath()) + "\n");
      // stored in minutes, but config in seconds
      writer.write(Configuration.POLL_INTERVAL_KEY + " = " + model.getPollFrequency() * 60 + "\n");
      writer.write(Configuration.OUT_DIR_KEY + " = "
          + convertPath(new File(model.getOutputDirectory()).getCanonicalPath()) + "\n\n");
      writer.write(Configuration.VM_ARGS_KEY + " = " + model.getVMArguments() + "\n");
      hostsPanel.writeHosts(writer);
      
      int i = 1;
      for (OutputPattern pattern : model.getOutputPatterns()) {
        writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.PATH + " = " + pattern.getPath());
        writer.write("\n");
        writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.PATTERN + " = " + pattern.getPattern());
        writer.write("\n");
        writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.CONCATENATE + " = " +
            String.valueOf(pattern.isConcatenate()));
        writer.write("\n");
       writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.HEADER + " = " +
            String.valueOf(pattern.isHeader()));
       writer.write("\n");
       i++;
      }
        
      logger.info("Writing batch run config file to: " + configFile.getAbsolutePath());
    } finally {
      if (writer != null)
        writer.close();
    }
  }

  private String convertPath(String path) {
    return path.replace("\\", "/");
  }

  public void run() {
    commitAll();
    ValidationResult result = validateAll();
    if (result.equals(ValidationResult.SUCCESS)) {
      tabs.setSelectedComponent(console);
      try {
        File configFile = new File(model.getOutputDirectory(), "config.props");
        writeConfigFile(configFile);
        System.setErr(new PrintStream(console.getErrorOutputStream(), true));
        System.setOut(new PrintStream(console.getStdOutputStream(), true));

        Project p = createAntProject();
        AntSessionRunner runner = new AntSessionRunner(p, configFile.getCanonicalPath());
        runner.execute();

      } catch (IOException ex) {
        ex.printStackTrace();
        ex.printStackTrace(new PrintStream(console.getErrorOutputStream(), true));
      } catch (ParserConfigurationException ex) {
        ex.printStackTrace(new PrintStream(console.getErrorOutputStream(), true));
      } catch (SAXException ex) {
        ex.printStackTrace(new PrintStream(console.getErrorOutputStream(), true));
      }
    } else {
      JOptionPane.showMessageDialog(tabs, result.getMessage());
    }
  }

  private Project createAntProject() throws IOException, ParserConfigurationException, SAXException {

    Project project = new Project();
    @SuppressWarnings("restriction")
    URL url = groovy.lang.GroovyObject.class.getProtectionDomain().getCodeSource().getLocation();
    project.setProperty("groovy.home", URLDecoder.decode(url.getFile(), "UTF-8"));
   
    url = SessionsDriver.class.getResource("/scripts/build.xml");
    String antFile = new File(URLDecoder.decode(url.getFile(), "UTF-8")).getCanonicalPath();
    int index = antFile.indexOf("repast.simphony.distributed.batch_");
    if (index != -1) {
      int start = index + "repast.simphony.distributed.batch_".length();
      int end = antFile.indexOf(File.separator, start);
      String version = antFile.substring(start, end);
      project.setProperty("plugins.version", version);
      project.setUserProperty("plugins.version", version);
    } else {
      project.setProperty("plugins.version", "");
      project.setUserProperty("plugins.version", "");
    }
    
    project.setUserProperty("ant.file", antFile);
    project.setProperty("model.dir", new File(model.getModelDirectory()).getCanonicalPath());
    project.setProperty("model.scenario.dir",
        new File(model.getScenarioDirectory()).getCanonicalPath());
    project.setProperty("working.dir",
        new File(System.getProperty("java.io.tmpdir"), "working").getCanonicalPath());
    File batchParamFile = new File(model.getBatchParameterFile());
    File unrolledParamFile = new File(System.getProperty("java.io.tmpdir"), "unrolledParamFile.txt");
    File batchMapFile = new File(System.getProperty("java.io.tmpdir"), "batchMapFile.txt");
    ParametersToInput pti = new ParametersToInput(batchParamFile);
    logger.info(String.format("Unrolling batch parameter file:\n\t%s to\n\t%s",
        batchParamFile.getPath(), unrolledParamFile.getPath()));
    pti.formatForInput(unrolledParamFile, batchMapFile);
    project.setProperty("unrolled.param.file", unrolledParamFile.getCanonicalPath());
    project.setProperty("batch.param.file", batchParamFile.getCanonicalPath());

    File output = new File(model.getOutputDirectory());
    if (!output.exists())
      output.mkdirs();

    // project.setProperty("zip.file", new File(output,
    // "complete_model.zip").getCanonicalPath());
    project.setProperty("jar.file", new File(output, "complete_model.jar").getCanonicalPath());

    project.init();
    ProjectHelper helper = ProjectHelper.getProjectHelper();
    project.addReference("ant.projectHelper", helper);
    helper.parse(project, new File(URLDecoder.decode(url.getFile(), "UTF-8")));

    DefaultLogger consoleLogger = new DefaultLogger();
    consoleLogger.setErrorPrintStream(new PrintStream(console.getErrorOutputStream(), true));
    consoleLogger.setOutputPrintStream(new PrintStream(console.getStdOutputStream(), true));
    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
    project.addBuildListener(consoleLogger);
    
    System.out.println("plugins version: " + project.getProperty("plugins.version"));

    return project;
  }

  public void createArchive() {
    commitAll();
    ValidationResult result = validateAll();
    if (result.equals(ValidationResult.SUCCESS)) {
      tabs.setSelectedComponent(console);
      try {
        File configFile = new File(model.getOutputDirectory(), "config.props");
        writeConfigFile(configFile);
        Project p = createAntProject();
        new AntSessionRunner(p, null).execute();
      } catch (IOException ex) {
        ex.printStackTrace(new PrintStream(console.getErrorOutputStream(), true));
      } catch (ParserConfigurationException ex) {
        ex.printStackTrace(new PrintStream(console.getErrorOutputStream(), true));
      } catch (SAXException ex) {
        ex.printStackTrace(new PrintStream(console.getErrorOutputStream(), true));
      }
    } else {
      JOptionPane.showMessageDialog(tabs, result.getMessage());
    }
  }

  class AntSessionRunner extends SwingWorker<Void, Object> {

    private Project project;
    private String configFile;

    public AntSessionRunner(Project project, String configFile) {
      this.project = project;
      this.configFile = configFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    protected Void doInBackground() throws Exception {
      project.executeTarget(project.getDefaultTarget());
      if (configFile != null) {
        SessionsDriver driver = new SessionsDriver(configFile);
        SSHSessionFactory.getInstance().setUserInfo(
            new GUIUserInfo(SwingUtilities.getWindowAncestor(console)));
        driver.run();
      }
      return null;
    }

    protected void done() {
      try {
        get();
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      } catch (ExecutionException ex) {
        ex.printStackTrace();
      }
    }
  }
}
