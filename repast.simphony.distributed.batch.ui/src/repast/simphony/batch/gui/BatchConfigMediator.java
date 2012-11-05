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

import repast.simphony.batch.BatchConstants;
import repast.simphony.batch.parameter.ParametersToInput;
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
  
  private PresentationModel<BatchRunConfigBean> pModel = new PresentationModel<BatchRunConfigBean>(model);

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
        setDirty(true);
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
    status.setText(tabs.getTitleAt(tabs.getSelectedIndex()));
  }
  
  private void setDirty(boolean dirty) {
    this.dirty = dirty;
    // TODO query the panels to see if we can run
    
  }
  
  public void onExit() {
    Logger logger = Logger.getLogger("repast.simphony.batch");
    logger.addAppender(stdout);
  }
  
  public JComponent getStatusBar() {
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
    bpPanel = new BatchParamPanel(pModel);
    tabs.addTab("Batch Parameters", bpPanel);
    hostsPanel = new HostsPanel();
    tabs.addTab("Hosts", hostsPanel);
    tabs.addTab("Console", console);
    hostsPanel.init(model);
    
    if (model.getModelDirectory().length() > 0) {
      updateFromModel();
      dirty = false;
    }
    return tabs;
  }

  private void commitAll() {
    pModel.triggerCommit();
    hostsPanel.commit(model);
  }
  
  private void askSave() {
    int ret = JOptionPane.showConfirmDialog(console.getParent().getParent(), 
        "Do you want to save the changes you have made to the batch run configuration?", "Save Changes?",
        JOptionPane.YES_NO_OPTION);
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
        configFile = chooser.getSelectedFile();
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
      writer.write("model.archive = "
          + convertPath(new File(model.getOutputDirectory(), "complete_model.jar").getCanonicalPath()) + "\n");
      writer.write("batch.params.file = scenario.rs/batch_params.xml\n");
      writer.write("ssh.key_dir = " + convertPath(new File(model.getKeyDirectory()).getCanonicalPath()) + "\n");
      // stored in minutes, but config in seconds
      writer.write("poll.frequency = " + model.getPollFrequency() * 60 + "\n");
      writer.write("model.output = " + convertPath(new File(model.getOutputDirectory()).getCanonicalPath())
          + "\n\n");
      writer.write("vm.arguments = " + model.getVMArguments() + "\n");
      hostsPanel.writeHosts(writer);
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
  }

  private Project createAntProject() throws IOException, ParserConfigurationException, SAXException {
	URL url = BatchConstants.class.getResource("/scripts/build.xml");
    Project project = new Project();
    project.setUserProperty("ant.file",  new File(URLDecoder.decode(url.getFile(), "UTF-8")).getCanonicalPath());
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
    
    //project.setProperty("zip.file", new File(output, "complete_model.zip").getCanonicalPath());
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

    return project;
  }

  public void createArchive() {
    commitAll();
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
        SSHSessionFactory.getInstance().setUserInfo(new GUIUserInfo(SwingUtilities.getWindowAncestor(console)));
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
