package repast.simphony.annotate;

import java.lang.reflect.Field;

import simphony.util.messages.MessageCenter;

public class FieldPropertyManipulator implements PropertyManipulator {
	MessageCenter messageCenter = MessageCenter.getMessageCenter(this
			.getClass());

	private Field field;

	public FieldPropertyManipulator(Field f) {
		field = f;
		field.setAccessible(true);
	}

	public void setProperty(Object target, Object value) {
		try {
			field.set(target, value);
		} catch (Exception e) {
			messageCenter.error("Unable to set property in object",
					new RuntimeException(e));
		}
	}

	public Object getProperty(Object target) {
		// TODO Auto-generated method stub
		return null;
	}

}
