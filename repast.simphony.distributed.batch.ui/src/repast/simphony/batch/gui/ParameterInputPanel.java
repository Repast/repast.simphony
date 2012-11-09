/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.Parameters;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ParameterInputPanel extends JPanel {

  private JComboBox typeBox;
  private JPanel inputPanels = new JPanel(new CardLayout());
  private String pName, displayName;
  private Class<?> pType;

  public ParameterInputPanel(String pName, Parameters params, ParameterData pData) {
    super(new BorderLayout());
    this.pName = pName;
    this.displayName = params.getDisplayName(pName);
    setName(pName);
    this.pType = params.getSchema().getDetails(pName).getType();

    fillTypeBox(pName, params);
    createInputPanels(params);

    FormLayout layout = new FormLayout("5dlu, left:pref, 3dlu, fill:default:grow", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.append(new JLabel(params.getDisplayName(pName) + ":"), 4);
    formBuilder.nextLine();
    formBuilder.setLeadingColumnOffset(1);
    formBuilder.append(typeBox, inputPanels);

    add(formBuilder.getPanel(), BorderLayout.CENTER);

    typeBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent evt) {
        CardLayout layout = (CardLayout) inputPanels.getLayout();
        layout.show(inputPanels, ((ParameterType) evt.getItem()).toString());
      }
    });

    update(pData, params);
  }

  public void update(ParameterData pData, Parameters params) {
    if (pData == null && pName.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
      typeBox.setSelectedItem(ParameterType.RANDOM);
    } else if (pData != null) {
      typeBox.setSelectedItem(pData.getType());
      getCurrentPanel().set(pData);
    } else if (pData == null) {
      typeBox.setSelectedItem(ParameterType.CONSTANT);
      if (params != null && params.getSchema().getDetails(pName) != null) {
        ParameterSchema schema = params.getSchema().getDetails(pName);
        if (schema.getDefaultValue() != null)
          ((ConstantInputPanel) getCurrentPanel()).setDefaultValue(schema.getDefaultValue());
      }
    }
  }

  public ParameterType getType() {
    return (ParameterType) typeBox.getSelectedItem();
  }

  private InputPanel getCurrentPanel() {
    for (Component comp : inputPanels.getComponents()) {
      if (comp.isVisible())
        return (InputPanel) comp;
    }
    return null;
  }

  public void writeXML(XMLStreamWriter writer) throws XMLStreamException {
    getCurrentPanel().write(writer, pName, pType);
  }

  private void fillTypeBox(String pName, Parameters params) {
    if (pName.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
      typeBox = new JComboBox(new ParameterType[] { ParameterType.CONSTANT, ParameterType.NUMBER,
          ParameterType.LIST, ParameterType.RANDOM });
    } else {
      Class<?> pType = params.getSchema().getDetails(pName).getType();
      if (isNumber(pType)) {
        typeBox = new JComboBox(new ParameterType[] { ParameterType.CONSTANT, ParameterType.NUMBER,
            ParameterType.LIST });
      } else {
        typeBox = new JComboBox(new ParameterType[] { ParameterType.CONSTANT, ParameterType.LIST });
      }
    }
  }

  private void createInputPanels(Parameters params) {
    for (int i = 0; i < typeBox.getModel().getSize(); i++) {
      Object type = typeBox.getModel().getElementAt(i);
      if (type == ParameterType.LIST) {
        ListInputPanel inputPanel = new ListInputPanel(displayName);
        inputPanels.add(inputPanel, type.toString());
      } else if (type == ParameterType.CONSTANT) {
        ConstantInputPanel inputPanel = new ConstantInputPanel(displayName, pType);
        inputPanels.add(inputPanel, type.toString());
      } else if (type == ParameterType.NUMBER) {
        NumberInputPanel inputPanel = new NumberInputPanel(displayName, pType);
        inputPanels.add(inputPanel, type.toString());
      } else if (type == ParameterType.RANDOM) {
        RandomInputPanel inputPanel = new RandomInputPanel();
        inputPanels.add(inputPanel, type.toString());
      }
    }
  }

  private boolean isNumber(Class<?> clazz) {
    return Number.class.isAssignableFrom(clazz) || clazz.equals(int.class)
        || clazz.equals(long.class) || clazz.equals(float.class) || clazz.equals(double.class)
        || clazz.equals(byte.class) || clazz.equals(short.class);
  }

  /**
   * Validates the input in the result panels.
   */
  public ValidationResult validateInput() {
    return getCurrentPanel().validateInput();
  }

  private interface InputPanel {
    void write(XMLStreamWriter writer, String pName, Class<?> pType) throws XMLStreamException;

    void set(ParameterData data);

    ValidationResult validateInput();
  }

  private static abstract class AbstractInputPanel extends JPanel {

    protected String pName;

    public AbstractInputPanel(String pName) {
      super(new BorderLayout());
      this.pName = pName;
    }

  }

  private static class NumberInputPanel extends AbstractInputPanel implements InputPanel {

    protected JTextField fromFld = new JTextField(5);
    protected JTextField toFld = new JTextField(5);
    protected JTextField stepFld = new JTextField(5);

    public NumberInputPanel(String pName, Class<?> numberType) {
      super(pName);

      if (numberType.equals(Double.class) || numberType.equals(double.class)) {
        fromFld.setDocument(new DoubleDocument());
        toFld.setDocument(new DoubleDocument());
        stepFld.setDocument(new DoubleDocument());
      } else if (numberType.equals(Float.class) || numberType.equals(float.class)) {
        fromFld.setDocument(new FloatDocument());
        toFld.setDocument(new FloatDocument());
        stepFld.setDocument(new FloatDocument());
      } else if (numberType.equals(Long.class) || numberType.equals(long.class)) {
        fromFld.setDocument(new LongDocument());
        toFld.setDocument(new LongDocument());
        stepFld.setDocument(new LongDocument());
      } else {
        fromFld.setDocument(new IntegerDocument());
        toFld.setDocument(new IntegerDocument());
        stepFld.setDocument(new IntegerDocument());
      }

      FormLayout layout = new FormLayout(
          "left:pref, 3dlu, pref:grow, 3dlu, left:pref, 3dlu, pref:grow, "
              + "3dlu, left:pref, 3dlu, pref:grow", "");
      DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
      formBuilder.append("From:", fromFld);
      formBuilder.append("To:", toFld);
      formBuilder.append("Step:", stepFld);

      add(formBuilder.getPanel(), BorderLayout.CENTER);
    }

    @Override
    public ValidationResult validateInput() {
      if (fromFld.getText().trim().isEmpty())
        return new ValidationResult("Parameter " + pName + " is missing a \"from\" value");
      if (toFld.getText().trim().isEmpty())
        return new ValidationResult("Parameter " + pName + " is missing a \"to\" value");
      if (stepFld.getText().trim().isEmpty())
        return new ValidationResult("Parameter " + pName + " is missing a \"step\" value");
      
      double from = Double.valueOf(fromFld.getText().trim());
      double to = Double.valueOf(toFld.getText().trim());
      double step = Double.valueOf(stepFld.getText().trim());
      
      if (step == 0) return new ValidationResult("Parameter " + pName + ": \"step\" value must be greater than 0");
      if (step > 0 && to <= from) return new ValidationResult("Parameter " + pName + ": \"to\" value must be greater than \"from\" value");
      if (step < 0 && to >= from) return new ValidationResult("Parameter " + pName + ": \"from\" value must be greater than \"to\" value");
      

      return ValidationResult.SUCCESS;
    }

    @Override
    public void write(XMLStreamWriter writer, String pName, Class<?> pType)
        throws XMLStreamException {
      writer.writeStartElement("parameter");
      writer.writeAttribute("name", pName);
      writer.writeAttribute("type", "number");
      writer.writeAttribute("number_type", pType.getName());
      writer.writeAttribute("start", fromFld.getText().trim());
      writer.writeAttribute("end", toFld.getText().trim());
      writer.writeAttribute("step", stepFld.getText().trim());
    }

    @Override
    public void set(ParameterData data) {
      fromFld.setText(data.getAttribute(ParameterAttribute.START));
      toFld.setText(data.getAttribute(ParameterAttribute.END));
      stepFld.setText(data.getAttribute(ParameterAttribute.STEP));
    }
  }

  private static class ConstantInputPanel extends AbstractInputPanel implements InputPanel {

    private JComponent fld;

    public ConstantInputPanel(String pName, Class<?> valueType) {
      super(pName);
      if (valueType.equals(boolean.class) || valueType.equals(Boolean.class)) {
        fld = new JCheckBox();
      } else {
        fld = new JTextField("0", 15);
        if (valueType.equals(Double.class) || valueType.equals(double.class)) {
          ((JTextField) fld).setDocument(new DoubleDocument());

        } else if (valueType.equals(Float.class) || valueType.equals(float.class)) {
          ((JTextField) fld).setDocument(new FloatDocument());

        } else if (valueType.equals(Long.class) || valueType.equals(long.class)) {
          ((JTextField) fld).setDocument(new LongDocument());

        } else if (valueType.equals(Integer.class) || valueType.equals(int.class)) {
          ((JTextField) fld).setDocument(new IntegerDocument());
        }
      }

      FormLayout layout = new FormLayout("left:pref, 3dlu, fill:default:grow", "");
      DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
      formBuilder.append("Value:", fld);
      add(formBuilder.getPanel(), BorderLayout.CENTER);
    }

    @Override
    public ValidationResult validateInput() {
      if (!(fld instanceof JCheckBox)) {
        if (((JTextField) fld).getText().trim().isEmpty())
          return new ValidationResult("Parameter " + pName + " is missing a value");
      }
      return ValidationResult.SUCCESS;
    }

    void setDefaultValue(Object obj) {
      if (obj instanceof Boolean) {
        ((JCheckBox) fld).setSelected((Boolean) obj);
      } else {
        ((JTextField) fld).setText(obj.toString());
      }
    }

    @Override
    public void write(XMLStreamWriter writer, String pName, Class<?> pType)
        throws XMLStreamException {
      writer.writeStartElement("parameter");
      writer.writeAttribute("name", pName);
      writer.writeAttribute("type", "constant");
      writer.writeAttribute("constant_type", pType.getName());
      if (fld instanceof JTextField)
        writer.writeAttribute("value", ((JTextField) fld).getText().trim());
      else {
        writer.writeAttribute("value", String.valueOf(((JCheckBox) fld).isSelected()));
      }
      writer.writeEndElement();
    }

    @Override
    public void set(ParameterData data) {
      if (fld instanceof JTextField)
        ((JTextField) fld).setText(data.getAttribute(ParameterAttribute.VALUE));
      else {
        ((JCheckBox) fld).setSelected(Boolean.parseBoolean(data
            .getAttribute(ParameterAttribute.VALUE)));
      }
    }
  }

  private static class ListInputPanel extends AbstractInputPanel implements InputPanel {

    protected JTextField fld = new JTextField(15);

    public ListInputPanel(String pName) {
      super(pName);
      FormLayout layout = new FormLayout("left:pref, 3dlu, fill:default:grow", "");
      DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
      formBuilder.append("Values:", fld);
      add(formBuilder.getPanel(), BorderLayout.CENTER);
    }

    @Override
    public void write(XMLStreamWriter writer, String pName, Class<?> pType)
        throws XMLStreamException {
      writer.writeStartElement("parameter");
      writer.writeAttribute("name", pName);
      writer.writeAttribute("type", "list");
      writer.writeAttribute("value_type", pType.getName());
      writer.writeAttribute("values", fld.getText().trim());
    }

    @Override
    public ValidationResult validateInput() {
      // validate the list format somehow
      if (fld.getText().trim().isEmpty())
        return new ValidationResult("Parameter " + pName + " is missing a value");
      return ValidationResult.SUCCESS;
    }

    @Override
    public void set(ParameterData data) {
      fld.setText(data.getAttribute(ParameterAttribute.VALUES));
    }
  }

  private static class RandomInputPanel extends JPanel implements InputPanel {

    @Override
    public void write(XMLStreamWriter writer, String pName, Class<?> pType)
        throws XMLStreamException {
    }

    @Override
    public void set(ParameterData data) {
    }

    @Override
    public ValidationResult validateInput() {
      return ValidationResult.SUCCESS;
    }

  }
}
