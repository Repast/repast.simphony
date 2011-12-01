package repast.simphony.space.graph;

import javassist.*;


/**
 * Produces EdgeCreator that create Edges of a named type.
 *
 * @author Nick Collier
 */
public class EdgeCreatorFactory {

  private static int counter = 1;

  static {
    ClassPool.getDefault().appendClassPath(new LoaderClassPath(EdgeCreator.class.getClassLoader()));
  }


  public EdgeCreator createEdgeCreator(String edgeClassName) throws NotFoundException, CannotCompileException,
          IllegalAccessException, InstantiationException {
    ClassPool pool = ClassPool.getDefault();
    CtClass inter = pool.get(EdgeCreator.class.getName());
    CtClass clazz = pool.makeClass("SynEdgeCreator" + counter++);
    clazz.setInterfaces(new CtClass[]{inter});

    

    StringBuilder buf = new StringBuilder("public ");
    buf.append(RepastEdge.class.getName());
    buf.append(" createEdge(Object source, Object target, boolean isDirected, double weight) { return new ");
    buf.append(edgeClassName);
    buf.append("($1, $2, $3, $4);}");

    CtMethod method = CtNewMethod.make(buf.toString(), clazz);
    clazz.addMethod(method);

    buf = new StringBuilder("public java.lang.Class getEdgeType() { return ");
    buf.append(edgeClassName);
    buf.append(".class; }");
    method = CtNewMethod.make(buf.toString(), clazz);
    clazz.addMethod(method);

    Class c = clazz.toClass(this.getClass().getClassLoader());
    return (EdgeCreator) c.newInstance();
  }
}
