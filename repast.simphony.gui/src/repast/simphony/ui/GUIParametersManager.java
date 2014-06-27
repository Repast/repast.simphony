package repast.simphony.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.ui.parameters.ParametersUI;

/**
 * Manages gui parameters such that parameters can be changed in the gui and
 * then reset back to some set of default parameters.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GUIParametersManager implements ParameterSetter {

  private Parameters params;
  private ParametersUI pui;

  public GUIParametersManager(Parameters params, ParametersUI pui) {
    this.pui = pui;
    this.params = params;
  }

  /**
   * Returns false.
   * 
   * @return false
   */
  public boolean atEnd() {
    return false;
  }

  /**
   * Sets the parameters to the next set of values. In this case, that will be
   * the values from the GUI.
   * 
   * @param params
   */
  public void next(Parameters params) {
    pui.commitParameters();
  }

  /**
   * Resets the parameters to the default values passed in on the constructor.
   * 
   * @param params
   */
  public void reset(Parameters params) {
    pui.resetParameters();
    for (String name : params.getSchema().parameterNames()) {
      ParameterSchema schema = params.getSchema().getDetails(name);
      Object defVal = schema.getDefaultValue();
      if (name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME) && defVal.equals(Parameters.NULL)) {
        int val = (int) System.currentTimeMillis();
        // per JIRA 76 - "Use positive default random seeds"
        if (val < 0) val = Math.abs(val);
        params.setValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, val);
      } else {
        if (!defVal.equals(Parameters.NULL)) {
          params.setValue(name, defVal);
        }
      }
    }
  }

  /**
   * Resets the parameters managed by this GUIParametersManager back to their
   * default values. The default values are whatever values the Parameters
   * object held when it was passed in the constructor.
   */
  public void reset() {
    reset(params);
  }

  /**
   * Gets the parameters object managed by this GUIParametersManager.
   * 
   * @return the parameters object managed by this GUIParametersManager.
   */
  public Parameters getParameters() {
    return params;
  }

  /**
   * Saves the parameters in the specified file.
   * 
   * @param paramFile
   * @throws IOException if there is an error saving the parameters to a file.
   */
  public void saveParameters(File paramFile) throws IOException {
    String xml = pui.toXML();
    Files.write(paramFile.toPath(), xml.getBytes(), StandardOpenOption.CREATE);
  }
}
