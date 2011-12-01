package repast.simphony.dataLoader.ui.wizard.builder;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.annotate.PropertyAnnot;
import repast.simphony.ui.plugin.editor.UISaver;
import saf.core.ui.util.DoubleDocument;
import saf.core.ui.util.IntegerDocument;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

public class AgentDescriptorFillerPanel extends UISaver {

	ContextDescriptor contextDescriptor;

	ContextCreatorCanvas canvas;

	Point2D position;

	AgentDescriptor desc;

	Map<Field, JComponent> propertyMap;

	DefaultFormBuilder builder;

	String title;

	class FieldWrapper {
		Field f;

		String displayName;
	}

	public AgentDescriptorFillerPanel(Point2D position,
			ContextDescriptor contextDescriptor, ContextCreatorCanvas canvas,
			AgentDescriptor descriptor) {
		this.desc = descriptor;
		this.contextDescriptor = contextDescriptor;
		this.canvas = canvas;
		this.position = position;
		if (desc.getAgentClass().isAnnotationPresent(AgentAnnot.class)) {
			AgentAnnot anno = (AgentAnnot) desc.getAgentClass()
					.getAnnotation(AgentAnnot.class);
			title = anno.displayName();
		} else {
			title = desc.getAgentClass().getSimpleName();
		}
		propertyMap = new HashMap<Field, JComponent>();
		Class<?> agentClass = descriptor.getAgentClass();
		FormLayout layout = FormFactory.createColumnLayout(1, 1,
				new ColumnSpec("right:p"));

		builder = new DefaultFormBuilder(layout, this);
		builder.setDefaultDialogBorder();
		builder.setLeadingColumnOffset(1);

		builder.appendSeparator(title);
		for (Field field : agentClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(PropertyAnnot.class)) {
				String displayName = field.getAnnotation(PropertyAnnot.class)
						.displayName();
				JComponent component = null;
				if (field.getType().equals(Integer.class)
						|| field.getType().equals(Integer.TYPE)) {
					JTextField tf = new JTextField();
					tf.setDocument(new IntegerDocument());
					if (desc.getProperties().get(field.getName()) != null) {
						tf.setText(desc.getProperties().get(field.getName()).toString());
					} else {
						tf.setText("0");
					}
					tf.setColumns(10);
					component = tf;
				} else if (field.getType().equals(Double.class)
						|| field.getType().equals(Double.TYPE)) {
					JTextField tf = new JTextField();
					tf.setDocument(new DoubleDocument());
					if (desc.getProperties().get(field.getName()) != null) {
						tf.setText(desc.getProperties().get(field.getName()).toString());
					} else {
						tf.setText("0.0");
					}
					tf.setColumns(10);
					component = tf;

				} else if (field.getType().equals(Boolean.class)
						|| field.getType().equals(Boolean.TYPE)) {
					JCheckBox cb = new JCheckBox();
					if (desc.getProperties().get(field.getName()) != null) {
						Object val = desc.getProperties().get(field.getName());
						if (val != null && val instanceof Boolean) {
							cb.setSelected((Boolean) val);
						}
					}
					component = cb;
				} else {
					JTextField tf = new JTextField();
					if (desc.getProperties().get(field.getName()) != null) {
						tf.setText(desc.getProperties().get(field.getName()).toString());
					}
					tf.setText("");
					tf.setColumns(10);
					component = tf;

				}
				propertyMap.put(field, component);
				builder.append(displayName, component);
				builder.nextLine();
			}
		}
	}

	@Override
	public String getDialogTitle() {
		return "Set Object Properties";
	}

	public Object getValue(Class type, String toString) {
		if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
			return Integer.parseInt(toString);
		} else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
			return Boolean.parseBoolean(toString);
		} else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
			return Short.parseShort(toString);
		} else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
			return Long.parseLong(toString);
		} else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
			return Float.parseFloat(toString);
		} else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
			return Double.parseDouble(toString);
		} else if (type.equals(String.class)
				|| String.class.isAssignableFrom(type)) {
			return toString;
		} else if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
			return Byte.parseByte(toString);
		} else if (type.equals(Character.TYPE) || type.equals(Character.class)) {
			return Character.toChars(Integer.parseInt(toString));
		}
		return null;
	}

	@Override
	public boolean save() {
		for (Entry<Field, JComponent> entry : propertyMap.entrySet()) {
			if (entry.getValue() instanceof JCheckBox) {
				boolean value = ((JCheckBox) entry.getValue()).isSelected();
				desc.addProperty(entry.getKey().getName(), value);
			} else {
				JTextField tf = (JTextField) entry.getValue();
				desc.addProperty(entry.getKey().getName(), getValue(entry
						.getKey().getType(), tf.getText()));
			}
		}
		if (contextDescriptor != null) {
			int index = contextDescriptor.addAgentDescriptor(desc);
			canvas.addAgentDescriptor(index, desc, position);
			return true;
		}
		
		return true;
	}

	@Override
	public boolean cancel() {
		return true;
	}

}
