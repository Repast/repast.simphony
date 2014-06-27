/**
 * 
 */
package repast.simphony.ui.parameters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.StringConverter;

/**
 * ParameterBinder for parameters constrained to a list of values.
 * 
 * @author Nick Collier
 */
public class ListParameterBinder extends AbstractParameterBinder {

  private JComboBox<String> box;
  private StringConverter<Object> converter;

  public ListParameterBinder(String name, String displayName) {
    super(name, displayName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.CompCreator#create(repast.simphony.parameter.Parameters)
   */
  @SuppressWarnings("unchecked")
  @Override
  public JComponent getComponent(Parameters params) {
    this.params = params;
    if (box == null) {
      final ParameterValueModel vModel = new ParameterValueModel(getName(), params);
      Object val = params.getValue(name);
      converter = params.getSchema().getDetails(name).getConverter();
      List<Object> objs = new ArrayList<>(params.getSchema().getDetails(name).getConstrainingList());

      DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
      for (Object obj : objs) {
        model.addElement(converter.toString(obj));
      }

      box = new JComboBox<String>(model);
      box.setSelectedItem(val);
      box.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          vModel.setValue(box.getSelectedItem());
        }
      });
    }
    
    return box;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#reset(repast.simphony.parameter.Parameters)
   */
  @Override
  public void resetToDefault() {
    Object defaultValue = params.getSchema().getDetails(getName()).getDefaultValue();
    @SuppressWarnings("unchecked")
    StringConverter<Object> conv = params.getSchema().getDetails(name).getConverter();
    box.setSelectedItem(conv.toString(defaultValue));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#update(repast.simphony.parameter.Parameters
   * )
   */
  @Override
  public void toParameter() {
    params.setValue(name, box.getSelectedItem());
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * projz.parameter.ParameterBinder#toXML(repast.simphony.parameter.Parameters)
   */
  @SuppressWarnings("unchecked")
  @Override
  public String toXML() {
    StringBuilder buf = new StringBuilder();
    StringConverter<Object> conv = params.getSchema().getDetails(name).getConverter();
    for (Object obj : params.getSchema().getDetails(name).getConstrainingList()) {
      if (buf.length() > 0)
        buf.append(" ");
      String val = conv.toString(obj);
      if (val.contains(" ")) {
        buf.append("'");
        buf.append(val);
        buf.append("'");
      } else {
        buf.append(val);
      }
    }
    return toXML(params, box.getSelectedItem().toString(),
        String.format("values = \"%s\"", buf.toString()));
  }
}
