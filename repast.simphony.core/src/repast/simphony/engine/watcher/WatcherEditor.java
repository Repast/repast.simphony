package repast.simphony.engine.watcher;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

/**
 * Edits the code in a watched class.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:17:19 $
 */
public class WatcherEditor extends ExprEditor {

  private String fieldName;
  private boolean addID = true;

  public WatcherEditor(String fieldName) {
    this.fieldName = fieldName;
  }

  private boolean hasField(CtField[] ctFields, String fieldName) {
    for (CtField field : ctFields) {
      if (field.getName().equals(fieldName)) return true;
    }
    
    return false;
  }

  public void edit(FieldAccess arg) throws CannotCompileException {
    if (arg.isWriter() && arg.getFieldName().equals(fieldName)
        && (!(arg.where() instanceof CtConstructor))) {
      String idName = "__id__" + arg.getFieldName();
      if (addID) {
        try {
          CtClass ctClass = arg.getField().getDeclaringClass();
          // need to check if declaring class already has the field
          // because this method can be called for multiple child classes
          // and so we would be duplicating the field in the parent declaring class
          if (!hasField(ctClass.getDeclaredFields(), idName)) {
            StringBuffer buf = new StringBuffer("protected static String ");
            buf.append(idName);
            buf.append(" = \"");
            buf.append(ctClass.getName());
            buf.append(".");
            buf.append(arg.getFieldName());
            buf.append("\";");
            CtField field = CtField.make(buf.toString(), ctClass);
            ctClass.addField(field);
          }

        } catch (NotFoundException e) {
          e.printStackTrace();
        }

        addID = false;
      }
      StringBuilder code = new StringBuilder();
      code.append("$proceed($$);");
      code.append("repast.simphony.engine.watcher.WatcherTrigger.getInstance().triggered(");
      code.append(idName);
      code.append(", $0, $1);");
      arg.replace(code.toString());
    }
  }
}
