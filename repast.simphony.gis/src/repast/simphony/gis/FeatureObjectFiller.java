package repast.simphony.gis;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;

public class FeatureObjectFiller {

	Map<AttributeType, Field> fieldMap;

	Class<?> clazz;

	public FeatureObjectFiller(Class<?> clazz) {
		fieldMap = new HashMap<AttributeType, Field>();
		this.clazz = clazz;
	}

	public void addAttribute(AttributeType type) {
		Field candidate = null;
		for (Field f : clazz.getDeclaredFields()) {
			if (f.getName().equalsIgnoreCase(type.getName())) {
				candidate = f;
				break;
			}
		}
		if (candidate == null) {
			return;
		}
		if (areEquivelent(candidate.getType(), type.getType())) {
			candidate.setAccessible(true);
			fieldMap.put(type, candidate);
		}
	}

	public void fillObject(Feature feature, Object o) throws Exception {
		for (Entry<AttributeType, Field> entry : fieldMap.entrySet()) {
			try {
				if (Number.class.isAssignableFrom(entry.getKey().getType())) {
					setNumber(o, entry.getValue(), (Number) feature
							.getAttribute(entry.getKey().getName()));
					continue;
				}
				if (Boolean.class.equals(entry.getKey().getType())) {
					entry.getValue().setBoolean(
							o,
							((Boolean) feature.getAttribute(entry.getKey()
									.getName())).booleanValue());
				}
				entry.getValue().set(o,
						feature.getAttribute(entry.getKey().getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setNumber(Object o, Field f, Number n) throws Exception {
		if (f.getType().equals(Integer.TYPE)) {
			f.setInt(o, n.intValue());
		} else if (f.getType().equals(Byte.TYPE)) {
			f.setByte(o, n.byteValue());
		} else if (f.getType().equals(Float.TYPE)) {
			f.setFloat(o, n.floatValue());
		} else if (f.getType().equals(Short.TYPE)) {
			f.setShort(o, n.shortValue());
		} else if (f.getType().equals(Long.TYPE)) {
			f.setLong(o, n.longValue());
		} else if (f.getType().equals(Double.TYPE)) {
			f.setDouble(o, n.doubleValue());
		}
	}

	private boolean areEquivelent(Class class1, Class class2) {
		if (class1.equals(class2))
			return true;
		if (Number.class.isAssignableFrom(class2)
				&& class1.equals(Long.TYPE)
				|| (Number.class.isAssignableFrom(class1) && class2
						.equals(Long.TYPE))) {
			return true;
		}
		if (Number.class.isAssignableFrom(class2)
				&& class1.equals(Integer.TYPE)
				|| (Number.class.isAssignableFrom(class1) && class2
						.equals(Integer.TYPE))) {
			return true;
		}
		if (Number.class.isAssignableFrom(class2)
				&& class1.equals(Short.TYPE)
				|| (Number.class.isAssignableFrom(class1) && class2
						.equals(Short.TYPE))) {
			return true;
		}
		if (Number.class.isAssignableFrom(class2)
				&& class1.equals(Double.TYPE)
				|| (Number.class.isAssignableFrom(class1) && class2
						.equals(Double.TYPE))) {
			return true;
		}
		if (Number.class.isAssignableFrom(class2)
				&& class1.equals(Float.TYPE)
				|| (Number.class.isAssignableFrom(class1) && class2
						.equals(Float.TYPE))) {
			return true;
		}
		if (Number.class.isAssignableFrom(class2)
				&& class1.equals(Byte.TYPE)
				|| (Number.class.isAssignableFrom(class1) && class2
						.equals(Byte.TYPE))) {
			return true;
		}
		if (class1.equals(Boolean.class) && class2.equals(Boolean.TYPE)
				|| class2.equals(Boolean.class) && class1.equals(Boolean.TYPE)) {
			return true;
		}
		return false;
	}
}
