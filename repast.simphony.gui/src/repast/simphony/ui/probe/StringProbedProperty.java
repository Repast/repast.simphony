package repast.simphony.ui.probe;

import java.beans.PropertyDescriptor;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class StringProbedProperty extends DefaultProbedPropertyUICreator {

  private List vals;

  public StringProbedProperty(PropertyDescriptor desc) {
		super(desc);
	}

  public StringProbedProperty(PropertyDescriptor desc, List vals) {
		this(desc);
    this.vals = vals;

  }

  public JComponent getComponent(PresentationModel<Object> model) {
		ValueModel vModel = model.getModel(name);
		if (type == Type.READ) return BasicComponentFactory.createLabel(vModel);
		else if (vals == null) return BasicComponentFactory.createTextField(vModel, true);
    else return new JComboBox(new ComboBoxAdapter(vals, vModel));
  }

}
