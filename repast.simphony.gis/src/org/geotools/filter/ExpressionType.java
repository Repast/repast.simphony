package org.geotools.filter;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public interface ExpressionType {
	short LITERAL_DOUBLE = 101;
  short LITERAL_INTEGER = 102;
  short LITERAL_STRING = 103;
  short LITERAL_GEOMETRY = 104;
  short MATH_ADD = 105;
  short MATH_SUBTRACT = 106;
  short MATH_MULTIPLY = 107;
  short MATH_DIVIDE = 108;
  short ATTRIBUTE_DOUBLE = 109;
  short ATTRIBUTE_INTEGER = 110;
  short ATTRIBUTE_STRING = 111;
  short ATTRIBUTE_GEOMETRY = 112;
  short ATTRIBUTE_UNDECLARED = 100;
  short ATTRIBUTE = 113;
  short FUNCTION = 114;

	short LITERAL_FLOAT = 115;
	short LITERAL_LONG = 116;
}
