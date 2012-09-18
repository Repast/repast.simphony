/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
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
import repast.simphony.batch.ssh.SessionsDriver;

/**
 * @author Nick Collier
 */
public class MainPanel {

  private static Logger logger = Logger.getLogger(MainPanel.class);

  private JPanel main;
  private JTabbedPane tabs = new JTabbedPane();
  private int currentTab = 0;

  private BatchRunModel model = new BatchRunModel();

  private ConsolePanel console = new ConsolePanel();
  private HostsPanel hostsPanel = new HostsPanel();

  public MainPanel() {
    main = new JPanel(new BorderLayout());
    createToolBar();
    createTabs();
    addListeners();

    Logger logger = Logger.getLogger("repast.simphony.batch");
    Appender appender = logger.getAppender("stdout");
    Layout layout = appender.getLayout();
    logger.removeAppender(appender);
    logger.addAppender(new TextAreaAppender(console, layout));

    init();
  }

  private void initModel() {
    model.setModelDirectory("../repast.simphony.distributed.batch/test_data/JZombies");
    model
        .setScenarioDirectory("../repast.simphony.distributed.batch/test_data/JZombies/JZombies.rs");
    model.setOutputDirectory("../repast.simphony.distributed.batch/test_out");
    model
        .setBatchParameterFile("../repast.simphony.distributed.batch/test_data/JZombies/batch/batch_params.xml");
    model.setKeyDirectory(System.getProperty("user.home") + "/.ssh");
    List<Host> hosts = new ArrayList<Host>();
    Host host = new Host(Host.Type.LOCAL);
    host.setInstances(2);
    hosts.add(host);

    host = new Host(Host.Type.REMOTE);
    host.setUser("sshtesting");
    host.setHost("128.135.250.205");
    host.setInstances(4);
    hosts.add(host);

    model.setHosts(hosts);
  }

  private void init() {
    initModel();

    for (int i = 0; i < tabs.getTabCount(); i++) {
      BatchRunPanel panel = (BatchRunPanel) tabs.getComponentAt(i);
      panel.init(model);
    }
  }

  private void addListeners() {
    tabs.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent arg0) {
        update();
        currentTab = tabs.getSelectedIndex();
      }
    });
  }

  private void update() {
    BatchRunPanel panel = (BatchRunPanel) tabs.getComponentAt(currentTab);
    panel.commit(model);
  }

  public JPanel getPanel() {
    return main;
  }

  private void createTabs() {
    main.add(tabs, BorderLayout.CENTER);

    tabs.addTab("Model", new ModelPanel());
    tabs.addTab("Batch Parameters", new BatchParamPanel());
    tabs.addTab("Hosts", hostsPanel);
    tabs.addTab("Console", console);
  }

  private JButton createButton(String icon, String tooltip) {
    JButton btn = new JButton(IconLoader.loadIcon(icon));
    btn.setToolTipText(tooltip);
    btn.setBorderPainted(false);
    btn.setPreferredSize(new Dimension(22, 18));
    btn.setMaximumSize(btn.getPreferredSize());
    btn.setMinimumSize(btn.getPreferredSize());
    return btn;
  }

  private void createToolBar() {
    JToolBar bar = new JToolBar();

    JButton newBtn = createButton("newprj_wiz.gif", "New Batch Configuration");
    newBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {

      }
    });
    bar.add(newBtn);
    bar.addSeparator();

    JButton openBtn = createButton("prj_obj.gif", "Open Batch Configuration");
    openBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("open");
      }
    });
    bar.add(openBtn);

    JButton saveBtn = createButton("save_edit.gif", "Save Batch Configuration");
    saveBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("save");
      }
    });
    bar.add(saveBtn);

    JButton saveAsBtn = createButton("saveas_edit.gif", "Save Batch Configuration As");
    saveAsBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("saveAs");
      }
    });
    bar.add(saveAsBtn);

    bar.addSeparator();
    JButton generateBtn = createButton("refresh_tab.gif", "Create Model Archive for Batch Runs");
    generateBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        createArchive();
      }
    });
    bar.add(generateBtn);

    JButton runBtn = createButton("lrun_obj.gif", "Execute Batch Runs");
    runBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        run();
      }
    });
    bar.add(runBtn);

    main.add(bar, BorderLayout.NORTH);
  }

  private void writeConfigFile(File configFile) throws IOException {
    // create the config.properties file
    BufferedWriter writer = null;
    try {
      if (!configFile.getParentFile().exists())
        configFile.mkdirs();

      writer = new BufferedWriter(new FileWriter(configFile));
      writer.write("model.archive = "
          + new File(model.getOutputDirectory(), "complete_model.zip").getCanonicalPath() + "\n");
      writer.write("batch.params.file = scenario.rs/batch_params.xml\n");
      // TODO get these two from GUI
      writer.write("ssh.key_dir = " + new File(model.getKeyDirectory()).getCanonicalPath() + "\n");
      // stored in minutes
      writer.write("poll.frequency = " + model.getPollFrequency() * 60 + "\n");
      writer.write("model.output = " + new File(model.getOutputDirectory()).getCanonicalPath()
          + "\n\n");
      hostsPanel.writeHosts(writer);
      logger.info("Writing batch run config file to: " + configFile.getAbsolutePath());
    } finally {
      writer.close();
    }

  }

  private void run() {
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
    project.setUserProperty("ant.file", url.getFile());
    project.setProperty("model.dir", new File(model.getModelDirectory()).getCanonicalPath());
    project.setProperty("model.scenario.dir",
        new File(model.getScenarioDirectory()).getCanonicalPath());
    project.setProperty("working.dir",
        new File(System.getProperty("java.io.tmpdir"), "working").getCanonicalPath());
    File batchParamFile = new File(model.getBatchParameterFile());
    File unrolledParamFile = new File(System.getProperty("java.io.tmpdir"), "unrolledParamFile.txt");
    File batchMapFile = new File(System.getProperty("java.io.tmpdir"), "batchMapFile.txt");
    ParametersToInput pti = new ParametersToInput(batchParamFile);
    pti.formatForInput(unrolledParamFile, batchMapFile);
    project.setProperty("unrolled.param.file", unrolledParamFile.getCanonicalPath());
    project.setProperty("batch.param.file", batchParamFile.getCanonicalPath());

    File output = new File(model.getOutputDirectory());
    if (!output.exists())
      output.mkdirs();
    project.setProperty("zip.file", new File(output, "complete_model.zip").getCanonicalPath());

    project.init();
    ProjectHelper helper = ProjectHelper.getProjectHelper();
    project.addReference("ant.projectHelper", helper);
    helper.parse(project, new File(url.getFile()));

    DefaultLogger consoleLogger = new DefaultLogger();
    consoleLogger.setErrorPrintStream(new PrintStream(console.getErrorOutputStream(), true));
    consoleLogger.setOutputPrintStream(new PrintStream(console.getStdOutputStream(), true));
    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
    project.addBuildListener(consoleLogger);

    return project;
  }

  private void createArchive() {
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
