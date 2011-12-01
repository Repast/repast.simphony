/*CopyrightHere*/
package repast.simphony.integration;

import simphony.util.messages.MessageCenter;


/**
 * Handler for different types of data.  These are the values that are acceptable for the data
 * element's (in a data file descriptor) type field.
 *  
 * @author Jerry Vos
 */
public enum ArrayDataTypeHandler {
	INT(DataTypeHandler.INT.getTypeName()) {
		@Override
		public ArrayHandler getHandler(int rows, int cols) {
			return new IntHandler(rows, cols, null);
		}
		@Override
		public ArrayHandler getHandler(int rows, int cols, Object data) {
			return new IntHandler(rows, cols, data);
		}

	},
	STRING(DataTypeHandler.STRING.getTypeName()) {
		@Override
		public ArrayHandler getHandler(int rows, int cols) {
			return new StringHandler(rows, cols, null);
		}
		@Override
		public ArrayHandler getHandler(int rows, int cols, Object data) {
			return new StringHandler(rows, cols, data);
		}

	},
	DOUBLE(DataTypeHandler.DOUBLE.getTypeName()) {
		@Override
		public ArrayHandler getHandler(int rows, int cols) {
			return new DoubleHandler(rows, cols, null);
		}
		@Override
		public ArrayHandler getHandler(int rows, int cols, Object data) {
			return new DoubleHandler(rows, cols, data);
		}
	};
	
	public interface ArrayHandler {
		void setValue(int row, int col, String value);
		
		Object getValue();
		
		String getValueString(int row, int col);
		
		int getRowCount();
	}
	
	class IntHandler implements ArrayHandler {
		int[] array1d;
		
		int[][] array2d;
		
		public IntHandler(int rows, int cols, Object data) {
			if (cols == 1) {
				if (data == null) {
					array1d = new int[rows];
				} else {
					array1d = (int[]) data;
				}
			} else {
				if (data == null) {
					array2d = new int[rows][cols];
				} else {
					array2d = (int[][]) data;					
				}
			}
		}
		
		public void setValue(int row, int col, String value) {
			if (array1d != null) {
				if (col != 0) {
					msgCenter.warn("Warning: in a 1-dimensional handler received a 2-dimensional " +
							"index (" + row + "," + col + "). Ignoring the column.");
				}
				
				array1d[row] = Integer.parseInt(value);
			} else {
				array2d[row][col] = Integer.parseInt(value);
			}
		}

		public Object getValue() {
			if (array1d != null) {
				return array1d;
			} else {
				return array2d;
			}
		}

		public String getValueString(int row, int col) {
			if (array1d != null) {
				if (col != 0) {
					msgCenter.warn("Warning: in a 1-dimensional handler received a 2-dimensional " +
							"index (" + row + "," + col + "). Ignoring the column.");
				}
				
				return Integer.toString(array1d[row]);
			} else {
				return Integer.toString(array2d[row][col]);
			}
		}

		public int getRowCount() {
			if (array1d != null) {
				return array1d.length;
			} else {
				return array2d.length;
			}
		}
	}
	class StringHandler implements ArrayHandler  {
		String[] array1d;
		
		String[][] array2d;
		
		public StringHandler(int rows, int cols, Object data) {
			if (cols == 1) {
				if (data == null) {
					array1d = new String[rows];
				} else {
					array1d = (String[]) data;					
				}
			} else {
				if (data == null) {
					array2d = new String[rows][cols];
				} else {
					array2d = (String[][]) data;					
				}
			}
		}
		
		public void setValue(int row, int col, String value) {
			if (array1d != null) {
				if (col != 0) {
					msgCenter.warn("Warning: in a 1-dimensional handler received a 2-dimensional " +
							"index (" + row + "," + col + "). Ignoring the column.");
				}
				
				array1d[row] = value;
			} else {
				array2d[row][col] = value;
			}
		}

		public Object getValue() {
			if (array1d != null) {
				return array1d;
			} else {
				return array2d;
			}
		}

		public String getValueString(int row, int col) {
			if (array1d != null) {
				if (col != 0) {
					msgCenter.warn("Warning: in a 1-dimensional handler received a 2-dimensional " +
							"index (" + row + "," + col + "). Ignoring the column.");
				}
				
				return array1d[row];
			} else {
				return array2d[row][col];
			}
		}

		public int getRowCount() {
			if (array1d != null) {
				return array1d.length;
			} else {
				return array2d.length;
			}
		}
	}
	class DoubleHandler implements ArrayHandler  {
		double[] array1d;
		
		double[][] array2d;
		
		public DoubleHandler(int rows, int cols, Object data) {
			if (cols == 1) {
				if (data == null) {
					array1d = new double[rows];
				} else {
					array1d = (double[]) data;					
				}
			} else {
				if (data == null) {
					array2d = new double[rows][cols];
				} else {
					array2d = (double[][]) data;					
				}
			}
		}
		
		public void setValue(int row, int col, String value) {
			if (array1d != null) {
				if (col != 0) {
					msgCenter.warn("Warning: in a 1-dimensional handler received a 2-dimensional " +
							"index (" + row + "," + col + "). Ignoring the column.");
				}
				
				array1d[row] = Double.parseDouble(value);
			} else {
				array2d[row][col] = Double.parseDouble(value);
			}
		}

		public Object getValue() {
			if (array1d != null) {
				return array1d;
			} else {
				return array2d;
			}
		}

		public String getValueString(int row, int col) {
			if (array1d != null) {
				if (col != 0) {
					msgCenter.warn("Warning: in a 1-dimensional handler received a 2-dimensional " +
							"index (" + row + "," + col + "). Ignoring the column.");
				}
				
				return Double.toString(array1d[row]);
			} else {
				return Double.toString(array2d[row][col]);
			}
		}

		public int getRowCount() {
			if (array1d != null) {
				return array1d.length;
			} else {
				return array2d.length;
			}
		}
	}
	
	private static MessageCenter msgCenter = MessageCenter
			.getMessageCenter(ArrayDataTypeHandler.class);
	
	private String typeName;

	ArrayDataTypeHandler(String description) {
		this.typeName = description;
	}

	public static ArrayDataTypeHandler getDataType(String typeName) {
		for (ArrayDataTypeHandler handler : values()) {
			if (handler.typeName.equalsIgnoreCase(typeName)) {
				return handler;
			}
		}
		return null;
	}

	public abstract ArrayHandler getHandler(int rows, int cols);
	
	public abstract ArrayHandler getHandler(int rows, int cols, Object array);
}