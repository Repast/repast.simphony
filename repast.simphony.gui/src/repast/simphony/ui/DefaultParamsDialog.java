/*
 * Created by JFormDesigner on Fri Aug 10 11:37:21 EDT 2007
 */

package repast.simphony.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.List;

import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.Schema;

/**
 * @author User #2
 */
public class DefaultParamsDialog extends ParameterSelectionDialog<Parameters> {
	private final static String TITLE_TEXT = "<html><b>Set Default Parameter Values</b><br>\n\tPlease select the parameters whose default values<br>should be set to the currently displayed value.</html>";
  public DefaultParamsDialog(Frame owner) {
    super(owner,TITLE_TEXT);
  }

  public DefaultParamsDialog(Dialog owner) {
    super(owner,TITLE_TEXT);
  }

  @Override
  public void init(Parameters params) {
	  init(params,true,true);
  }

  @Override
  protected void doOKaction(List<String> selectedParamNames) {
	  Schema schema = params.getSchema();
      for (String name : selectedParamNames) {
        ParameterSchema details = schema.getDetails(name);
        details.setDefaultValue(params.getValue(name));
      }
	  
  }
}
