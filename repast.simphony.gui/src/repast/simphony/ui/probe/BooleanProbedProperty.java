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
public class BooleanProbedProperty extends AbstractProbedProperty {

	public BooleanProbedProperty(PropertyDescriptor desc) {
		super(desc);
	}

	public JComponent getComponent(PresentationModel model, boolean buffered) {
		ValueModel vModel = buffered ? model.getBufferedModel(name) : model.getModel(name);
		JCheckBox box = BasicComponentFactory.createCheckBox(vModel, "");
		if (type == Type.READ) box.setEnabled(false);
		return box;
	}

}
