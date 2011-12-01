/**
 * 
 */
package repast.simphony.batch.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import repast.simphony.parameter.Parameters;

/**
 * Converts a parameter tree into parameter file xml format.
 * 
 * @author Nick Collier
 */
public class ParameterFileWriter {
  
  private Parameters params;
  private static  Set<Class<?>> NUM_CLASSES = new HashSet<Class<?>>();
  
  static {
    NUM_CLASSES.add(int.class);
    NUM_CLASSES.add(double.class);
    NUM_CLASSES.add(long.class);
    NUM_CLASSES.add(float.class);
    NUM_CLASSES.add(Integer.class);
    NUM_CLASSES.add(Double.class);
    NUM_CLASSES.add(Long.class);
    NUM_CLASSES.add(Float.class);
  }
  
  public ParameterFileWriter(Parameters params) {
    this.params = params;
  }
  
  public void write(LabelNode constants, LabelNode parameters) {
    StringWriter swriter = new StringWriter();
    PrintWriter writer = new PrintWriter(swriter);
    writeConstants(constants, writer);
    writeParameters(parameters, writer);
    writer.flush();
    System.out.println(swriter.toString());
  }
  
  private void writeConstants(LabelNode node, PrintWriter writer) {
    for (int i = 0; i < node.getChildCount(); i++) {
      BatchParameterNode child = (BatchParameterNode) node.getChildAt(i);
      BatchParameterBean bean = (BatchParameterBean) child.getUserObject();
      writer.printf("<parameter type=\"constant\" name=\"%s\" constant_type=\"%s\" value=\"%s\" />%n",
          bean.getName(), getType(bean), getValue(bean));
      
    }
  }
  
  private void writeParameters(LabelNode node, PrintWriter writer) {
    for (int i = 0; i < node.getChildCount(); i++) {
      writeParameter((BatchParameterNode)node.getChildAt(i), writer);
    }
  }
  
  private void writeParameter(BatchParameterNode node, PrintWriter writer) {
    BatchParameterBean bean = (BatchParameterBean) node.getUserObject();
    writer.printf("<parameter name=\"%s\"", bean.getName());
    if (node.getChildCount() == 0) writer.println("/>");
    else {
      writer.println(">");
      for (int i = 0; i < node.getChildCount(); i++) {
        writeParameter((BatchParameterNode)node.getChildAt(i), writer);
      }
      writer.println("</parameter>");
    }
  }
  
  private String getType(BatchParameterBean bean) {
    Class<?> clazz = params.getSchema().getDetails(bean.getName()).getType();
    if (NUM_CLASSES.contains(clazz)) return "number";
    if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) return "boolean";
    if (clazz.equals(String.class)) return "string";
    
    throw new IllegalArgumentException("Invalid parameter type");
  }
  
  private String getValue(BatchParameterBean bean) {
    String val = bean.getValue();
    Class<?> clazz = params.getSchema().getDetails(bean.getName()).getType();
    if (clazz.equals(Long.class) || clazz.equals(long.class)) return val + "L";
    if (clazz.equals(Float.class) || clazz.equals(float.class)) return val + "f";
    return val;
  }
}
