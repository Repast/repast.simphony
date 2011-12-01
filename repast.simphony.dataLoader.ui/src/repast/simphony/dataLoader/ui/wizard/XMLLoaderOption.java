/*CopyrightHere*/
/**
 *
 */
package repast.simphony.dataLoader.ui.wizard;

import org.java.plugin.PluginManager;
import org.pietschy.wizard.models.SimplePath;
import repast.simphony.dataLoader.ContextBuilder;

public class XMLLoaderOption implements DataLoaderWizardOption {
  public String getDescription() {
    return "Choose a freezedryed xml file";
  }

  public String getTitle() {
    return "Freeze Dried Simulation - XML Format";
  }

  public SimplePath getWizardPath() {
    return new SimplePath(new XMLFileChooserStep());
  }

  public ContextActionBuilder createBuilder(ContextBuilder baseLoader) {
    return new XMLContextActionBuilder();
  }

  public void reset() {

  }

  public void init(PluginManager manager) {

  }
}