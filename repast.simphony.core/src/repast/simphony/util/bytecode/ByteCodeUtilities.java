package repast.simphony.util.bytecode;

import javassist.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nick Collier
 *         Date: Aug 4, 2008 3:55:22 PM
 */
public class ByteCodeUtilities {

  static {
    ClassPool.getDefault().appendClassPath(new LoaderClassPath(ByteCodeUtilities.class.getClassLoader()));
  }

  private int counter = 0;

  // data source cache so we can resuse them
  private Map<String, DataSource> sourceCache = new HashMap<String, DataSource>();
  // map of template for creating objects from primitive types.
  private Map<Class, String> templateMap = new HashMap<Class, String>();

  private static ByteCodeUtilities instance = new ByteCodeUtilities();

  private ByteCodeUtilities() {
    templateMap.put(int.class, "new Integer($body$)");
    templateMap.put(double.class, "new Double($body$)");
    templateMap.put(float.class, "new Float($body$)");
    templateMap.put(long.class, "new Long($body$)");
    templateMap.put(short.class, "new Short($body$)");
    templateMap.put(byte.class, "new Byte($body$)");
    templateMap.put(boolean.class, "Boolean.valueOf($body$)");
  }

  /**
   * Gets the singleton instance of ByteCodeUtilities.
   *
   * @return the singleton instance of ByteCodeUtilities.
   */
  public static ByteCodeUtilities getInstance() {
    return instance;
  }

  /**
   * Creates a DataSource that will call the specified method.
   *
   * @param method the method to adapt as a DataSource.
   * @return the created date source.
   * @throws NotFoundException        if the DataSource interface is not found
   * @throws IllegalArgumentException if the specified method cannot be adapted as a
   *                                  DataSource.
   * @throws CannotCompileException   if the byte code cannot be compiled.
   * @throws IllegalAccessException   if there is an error while creating the DataSource
   * @throws InstantiationException   if there is an error while creating the DataSource
   */
  public DataSource createMethodCall(Method method) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
    Class retType = method.getReturnType();
    Class objType = method.getDeclaringClass();

    if (retType.equals(void.class) || method.getParameterTypes().length != 0) {
      throw new IllegalArgumentException("Method '" + method.getName() + "' cannot be wrapped in " +
              "a DataSource");
    }

    String methodId = objType.getName() + "." + method.getName();
    DataSource source = sourceCache.get(methodId);
    if (source == null) {


      ClassPool pool = ClassPool.getDefault();
      CtClass clazz = pool.makeClass("repast.simphony.util.bytecode.__DataSource" + counter++);
      clazz.addInterface(pool.get("repast.simphony.util.bytecode.DataSource"));
      StringBuilder methodStr = new StringBuilder("public Object getData(Object obj) {return ");

      String template = templateMap.get(retType);
      if (template == null) template = "$body$";

      StringBuilder bodyStr = new StringBuilder("((");
      bodyStr.append(objType.getName());
      bodyStr.append(")obj).");
      bodyStr.append(method.getName());
      bodyStr.append("()");
      methodStr.append(template.replace("$body$", bodyStr));
      methodStr.append(";}");

      //System.out.println("methodStr = " + methodStr);

      CtMethod ctMethod = CtMethod.make(methodStr.toString(), clazz);
      clazz.addMethod(ctMethod);
      ClassLoader loader = this.getClass().getClassLoader();
      source = (DataSource) clazz.toClass(loader, this.getClass().getProtectionDomain()).newInstance();
      sourceCache.put(methodId, source);
    }

    return source;
  }
}

/*

return new Integer($body$);


 */
