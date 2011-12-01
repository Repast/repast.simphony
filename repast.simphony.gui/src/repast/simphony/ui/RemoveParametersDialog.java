package repast.simphony.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.List;

import repast.simphony.parameter.MutableParameters;

public class RemoveParametersDialog extends ParameterSelectionDialog<MutableParameters> {

	private final static String TITLE_TEXT = "<html><b>Remove Parameters</b><br>\n\tPlease select the parameters to be removed.</html>";
	private boolean parameterRemoved = false;
	
	public RemoveParametersDialog(Frame owner) {
		super(owner,TITLE_TEXT);
	}

	public RemoveParametersDialog(Dialog owner) {
		super(owner,TITLE_TEXT);
	}

	@Override
	public void init(MutableParameters params) {
		parameterRemoved = false;
		init(params,false,false);
	}

	@Override
	protected void doOKaction(List<String> selectedParamNames) {
		parameterRemoved = false;
		for (String paramName : selectedParamNames) {
			if (params.removeParameter(paramName)) {
				//if any are successful
				parameterRemoved = true;
			}
		}
		
	}
	
	public boolean parameterRemoved() {
		return parameterRemoved;
	}

}
