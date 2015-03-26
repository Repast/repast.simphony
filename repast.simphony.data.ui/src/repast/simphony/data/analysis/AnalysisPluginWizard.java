package repast.simphony.data.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;
import repast.simphony.data2.gui.FileSinkChooserStep;
import repast.simphony.ui.plugin.editor.PluginWizard;
import repast.simphony.util.Settings;
import simphony.util.messages.MessageCenter;

/**
 * A wizard for executing a third party analysis tool on a file outputter's
 * output.
 * 
 * @author Eric Tatara
 * @author Jerry Vos
 */
public abstract class AnalysisPluginWizard {
  protected static final MessageCenter LOG = MessageCenter
      .getMessageCenter(AnalysisPluginWizard.class);

  private StaticModel wizardModel;
  private DataSetRegistry loggingRegistry;

  private BrowseForHomeStep homeStep;

  private boolean skipFirstStep;

  private String defaultLocation;
  private String installHome;
  private String name;
  private String licenseFileName;

  protected FileSinkChooserStep fileStep;

  public AnalysisPluginWizard() {

  }

  public AnalysisPluginWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
      boolean browseForRHome, String name, String installHome, String defaultLocation,
      String licenseFileName) {

    this.loggingRegistry = loggingRegistry;
    this.wizardModel = new StaticModel();
    this.defaultLocation = defaultLocation;
    this.installHome = installHome;
    this.name = name;
    this.licenseFileName = licenseFileName;

    setupWizard(showCopyright, browseForRHome);
  }

  public void init(DataSetRegistry loggingRegistry, boolean showCopyright, boolean browseForRHome,
      String name, String installHome, String defaultLocation, String licenseFileName) {

    this.loggingRegistry = loggingRegistry;
    this.wizardModel = new StaticModel();
    this.defaultLocation = defaultLocation;
    this.installHome = installHome;
    this.name = name;
    this.licenseFileName = licenseFileName;

    setupWizard(showCopyright, browseForRHome);
  }

  private void setupWizard(boolean showCopyright, boolean browseForHome) {
    if (showCopyright) {
      addCopyRightStep();
    }
    addBrowseForHomeStep();

    addSelectOutputterStep();
    if (!browseForHome && !showCopyright) {
      // advance past the browse for home step
      skipFirstStep = true;
    }
  }

  private void addCopyRightStep() {
    wizardModel.add(new CopyRightStep(name, this.getClass().getResourceAsStream(licenseFileName)));
  }

  private void addBrowseForHomeStep() {
    homeStep = new BrowseForHomeStep(name, installHome, defaultLocation);
    wizardModel.add(homeStep);
  }

  private void addSelectOutputterStep() {
    fileStep = new FileSinkChooserStep(getTabularFileSinks(), true,
        "Select the outputter(s) to pass to " + name,
        "<HTML>Please select which file sinks' data you would " + "like to send to " + name
            + ".<BR>");
    wizardModel.add(fileStep);
  }

  private List<FileDataSink> getTabularFileSinks() {
    ArrayList<FileDataSink> fileSinks = new ArrayList<FileDataSink>();

    for (FileDataSink fs : loggingRegistry.fileSinks()) {
        if (fs.getFormat() == FormatType.TABULAR) fileSinks.add(fs);
    }
    return fileSinks;
  }

  protected String prepFileNameFor(String fileName) {
    // return fileName.replace('\\', '/');

    String pass1 = fileName.replace('\\', File.separatorChar);
    String pass2 = pass1.replace('/', File.separatorChar);

    return pass2;

  }

  /**
   * Gets the home directory of the application.
   * 
   * @return String path of the application directory
   */
  public String getInstallHome() {
    String home;
    if (homeStep == null) {
      home = homeStep.getDefaultLocation();
    } else {
      home = homeStep.getInstallHome();
    }
    // if (!home.endsWith(SystemConstants.DIR_SEPARATOR)) {
    // return home.concat(SystemConstants.DIR_SEPARATOR);
    // } else {
    // return home;
    // }
    return home;
  }

  /**
   * Shows the wizard in a modal dialog.
   * 
   * @return if the wizard was was completed (true) or canceled (false).
   */
  public boolean showDialogModal() {
    Wizard wizard = new PluginWizard(wizardModel);
    wizard.setOverviewVisible(false);
    wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);

    if (skipFirstStep) {
      // advance past the browse for home step
      wizardModel.nextStep();
    }

    wizard.showInDialog(name, null, true);

    if (!wizard.wasCanceled()) {
      Settings.put(name + ".home", homeStep.getInstallHome());
    }
    return !wizard.wasCanceled();
  }

  public void setHome(String settingsRHome) {
    homeStep.homeDirField.setText(settingsRHome);
  }

  /**
   * Subclasses must specify the execution command including executable location
   *  and arguments required to run the external plugin.
   *  
   * @return the execution command path and arguments.
   */
  public abstract String[] getExecutionCommand();
  
  public Map<String, String> getEnvVars() {
    return new HashMap<String, String>();
  }

  /**
   * Message for when the plugin executable fails.
   * 
   * @return Message for when the plugin executable fails.
   */
  public abstract String getCannotRunMessage();
}