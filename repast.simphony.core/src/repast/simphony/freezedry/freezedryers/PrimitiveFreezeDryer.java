/*CopyrightHere*/
package repast.simphony.freezedry.freezedryers;

import repast.simphony.freezedry.FieldUtilities;
import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;

public class PrimitiveFreezeDryer implements FreezeDryer {

	static enum PrimitiveConverter {
		DOUBLE(Double.class, Double.TYPE) {
			@Override
			public Object convert(String val) {
				return Double.valueOf(val);
			}
		},
		FLOAT(Float.class, Float.TYPE) {
			@Override
			public Object convert(String val) {
				return Integer.valueOf(val);
			}
		},
		INTEGER(Integer.class, Integer.TYPE) {
			@Override
			public Object convert(String val) {
				return Integer.valueOf(val);
			}
		},
		SHORT(Short.class, Short.TYPE) {
			@Override
			public Object convert(String val) {
				return Short.valueOf(val);
			}
		},
		BYTE(Byte.class, Byte.TYPE) {
			@Override
			public Object convert(String val) {
				return Byte.valueOf(val);
			}
		},
		LONG(Long.class, Long.TYPE) {
			@Override
			public Object convert(String val) {
				return Long.valueOf(val);
			}
		},
		BOOLEAN(Boolean.class, Boolean.TYPE) {
			@Override
			public Object convert(String val) {
				return Boolean.valueOf(val);
			}
		},
		STRING(String.class, null) {
			@Override
			public Object convert(String val) {
				return val;
			}
		},
		CHARACTER(Character.class, Character.TYPE) {
			@Override
			public Object convert(String val) {
				if (val.length() != 1) {
					// TODO: warn
					return null;
				}
				return val.charAt(0);
			}
		};

		String className;

		String typeName;

		PrimitiveConverter(Class<?> clazz, Class<?> type) {
			this.className = clazz.getName();
			if (type == null) {
				typeName = null;
			} else {
				this.typeName = type.getName();
			}
		}

		public abstract Object convert(String val);

		public static PrimitiveConverter getConverter(String name) {
			for (PrimitiveConverter converter : values()) {
				if (converter.className.equals(name)
						|| (converter.typeName != null && converter.typeName.equals(name))) {
					return converter;
				}
			}
			return null;
		}
	}

	public static final String TYPE_COL = "TYPE";
	public static final String VAL_COL = "VAL";

	public FreezeDryedObject freezeDry(String id, Object o) throws FreezeDryingException {
		if (!FieldUtilities.INSTANCE.isPrimitive(o.getClass().getName())) {
			throw new FreezeDryingException("Object '" + o + "'is not a primitive");
		}

		FreezeDryedObject obj = new FreezeDryedObject(id, o.getClass());
    String strVal = o.toString();
    if (o instanceof Boolean) strVal = strVal.toUpperCase(); // for SQL standard compatibility
    obj.put(VAL_COL, strVal);
		
		return obj;
	}

	public Object rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		PrimitiveConverter converter = PrimitiveConverter.getConverter(fdo.getType().getName());
		if (converter != null) {
			return converter.convert(fdo.get(VAL_COL).toString());
		}
		return null;
	}
	
	public boolean handles(Class clazz) {
		return PrimitiveConverter.getConverter(clazz.getName()) != null;
//		return clazz.isPrimitive();
	}
}
