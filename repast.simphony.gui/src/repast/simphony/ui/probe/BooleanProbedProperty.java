package repast.simphony.ui.probe;

import java.beans.PropertyDescriptor;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;

/**
 * ProbedProperty for boolean properties.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BooleanProbedProperty extends DefaultProbedPropertyUICreator {

	public BooleanProbedProperty(PropertyDescriptor desc) {
		super(desc);
	}

	public JComponent getComponent(PresentationModel<Object> model) {
		ValueModel vModel = model.getModel(name,getterName,setterName);
		JCheckBox box = BasicComponentFactory.createCheckBox(vModel, "");
		if (type == Type.READ) box.setEnabled(false);
		return box;
	}

}
