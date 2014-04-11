package repast.simphony.ui.probe;

import java.beans.PropertyDescriptor;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import repast.simphony.parameter.StringConverter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueModel;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class StringProbedProperty extends DefaultProbedPropertyUICreator {

  private List<Object> vals;
  private StringConverter<Object> converter = null;

  public StringProbedProperty(PropertyDescriptor desc) {
    super(desc);
    if (desc instanceof MethodPropertyDescriptor) {
      converter = (StringConverter<Object>)((MethodPropertyDescriptor) desc).getStringConverter();
    }
  }

  public StringProbedProperty(PropertyDescriptor desc, List<Object> vals) {
    this(desc);
    this.vals = vals;
  }

  public JComponent getComponent(PresentationModel<Object> model) {
    ValueModel vModel = model.getModel(name, getterName, setterName);
    if (type == Type.READ) {
      if (converter == null) {
        return BasicComponentFactory.createLabel(vModel);
      } else {
        return BasicComponentFactory.createLabel(vModel, new StringConverterFormat(converter));
      }
    } else if (vals == null) {
      if (converter == null) {
        return BasicComponentFactory.createTextField(vModel, true);
      } else {
        return BasicComponentFactory.createFormattedTextField(vModel, new StringConverterFormat(converter));
      }
    } else
      return new JComboBox<Object>(new ComboBoxAdapter<Object>(vals, vModel));
  }
}
