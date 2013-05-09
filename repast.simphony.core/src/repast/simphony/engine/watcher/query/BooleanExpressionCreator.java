package repast.simphony.engine.watcher.query;

import javassist.*;
import org.apache.commons.lang3.StringUtils;
import repast.simphony.util.collections.Pair;
import simphony.util.messages.MessageCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Dynamicaly creates a BooleanExpression.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class BooleanExpressionCreator {

  private static MessageCenter msg = MessageCenter.getMessageCenter(BooleanExpressionCreator.class);

  private static Map<Class, String[]> pObjMap = new HashMap<Class, String[]>();
  private static int counter = 1;

  private static Map<Pair<Object, String>, IBooleanExpression> cache = new HashMap<Pair<Object, String>, IBooleanExpression>();

  static {
    pObjMap.put(Integer.class, new String[]{"int", "((Integer)", ").intValue()"});
    pObjMap.put(Double.class, new String[]{"double", "((Double)", ").doubleValue()"});
    pObjMap.put(Float.class, new String[]{"float", "((Float)", ").floatValue()"});
    pObjMap.put(Long.class, new String[]{"long", "((Long)", ").longValue()"});
    pObjMap.put(Byte.class, new String[]{"byte", "((Byte)", ").byteValue()"});
    pObjMap.put(Short.class, new String[]{"short", "((Short)", ").shortValue()"});
    pObjMap.put(Boolean.class, new String[]{"boolean", "((Boolean)", ").booleanValue()"});

    ClassPool.getDefault().appendClassPath(new LoaderClassPath(BooleanExpressionCreator.class.getClassLoader()));
  }

  public IBooleanExpression create(Object id, String expression, Class watcher, Class watchee, Class field) throws NotFoundException, CannotCompileException,
          IllegalAccessException, InstantiationException {
    Pair<Object, String> key = new Pair<Object, String>(id, expression);
    IBooleanExpression exp = cache.get(key);
    if (exp == null) {
      exp = doCreate(id, expression, watcher, watchee, field);
      cache.put(key, exp);
    }

    return exp;

  }


  private IBooleanExpression doCreate(Object id, String expression, Class watcher, Class watchee, Class field) throws NotFoundException, CannotCompileException,
          IllegalAccessException, InstantiationException {
    ClassPool pool = ClassPool.getDefault();
    CtClass inter = pool.get("repast.simphony.engine.watcher.query.IBooleanExpression");
    CtClass ser = pool.get("java.io.Serializable");
    CtClass clazz = pool.makeClass("Synthetic$BooleanExpression" + counter++);
    clazz.setInterfaces(new CtClass[]{inter, ser});

    StringBuffer buffer = new StringBuffer("public boolean execute(Object watcher, Object watchee, Object field) {");
    buffer.append(createCast("$1", "watcher", watcher));
    buffer.append(createCast("$2", "watchee", watchee));
    buffer.append(createCast("$3", "field", field));
    if (expression.indexOf("$context") != -1) {
      buffer.append("repast.simphony.context.Context context = repast.simphony.util.ContextUtils.getContext(watcher);\n");
    }
    buffer.append("return ");
    buffer.append(replaceNames(expression));
    buffer.append(";\n");
    buffer.append("}");

    msg.debug(buffer.toString());

    CtMethod method = CtNewMethod.make(buffer.toString(), clazz);
    clazz.addMethod(method);

    buffer = new StringBuffer("public String getExpression() { return \"");
    buffer.append(StringUtils.replace(expression, "\"", "\\\""));
    buffer.append("\";}");
    method = CtNewMethod.make(buffer.toString(), clazz);
    clazz.addMethod(method);
    Class c = clazz.toClass(this.getClass().getClassLoader());
    return (IBooleanExpression) c.newInstance();
  }

  private String replaceNames(String expression) {
    expression = StringUtils.replace(expression, "$watcher", "watcher");
    expression = StringUtils.replace(expression, "$watchee", "watchee");
    expression = StringUtils.replace(expression, "$field", "field");
    expression = StringUtils.replace(expression, "$context", "context");
    return expression;
  }

  private String createCast(String jaIndex, String varName, Class clazz) {
    StringBuffer buf = new StringBuffer();
    buf.append(getClassName(clazz));
    buf.append(" ");
    buf.append(varName);
    buf.append(" = ");
    buf.append(getClassCast(clazz, jaIndex));
    buf.append(";\n");
    return buf.toString();
  }

  private String getClassCast(Class clazz, String jaIndex) {
    String[] vals = pObjMap.get(clazz);
    StringBuffer buf = new StringBuffer();
    if (vals == null) {
      buf.append("(");
      if (clazz.isArray()) {
        buf.append(clazz.getComponentType().getName() + "[]");
      } else {
        buf.append(clazz.getName());
      }
      buf.append(")");
      buf.append(jaIndex);
    } else {
      buf.append(vals[1]);
      buf.append(jaIndex);
      buf.append(vals[2]);
    }
    return buf.toString();
  }

  private String getClassName(Class clazz) {
    if (clazz.isArray()) {
      return clazz.getComponentType().getName() + "[]";
    }

    String[] vals = pObjMap.get(clazz);
    if (vals == null) {
      return clazz.getName();
    }
    return vals[0];
  }
}
