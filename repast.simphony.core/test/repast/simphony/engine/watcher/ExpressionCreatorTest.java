package repast.simphony.engine.watcher;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import repast.simphony.engine.watcher.query.BooleanExpressionCreator;
import repast.simphony.engine.watcher.query.IBooleanExpression;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class ExpressionCreatorTest extends TestCase {

  static {
    BasicConfigurator.configure();
  }

  public void testInteger() {
    // tests when field is an Integer for cast to int
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("IntegerTest", "$field > 3", Object.class,
          Object.class, Integer.class);
      boolean result = exp.execute(new Object(), new Object(), new Integer(6));
      assertTrue(result);
    } catch (NotFoundException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    } catch (CannotCompileException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    } catch (IllegalAccessException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    } catch (InstantiationException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
  }

  public void testDouble() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("DoubleTest", "$field < 3.53", Object.class,
          Object.class, Double.class);
      boolean result = exp.execute(new Object(), new Object(), new Double(3.5));
      assertTrue(result);
    } catch (NotFoundException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    } catch (CannotCompileException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    } catch (IllegalAccessException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    } catch (InstantiationException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
  }

  public void testFloat() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("FloatTest", "$field < 3.53", Object.class,
          Object.class, Float.class);
      boolean result = exp.execute(new Object(), new Object(), new Float(3.5));
      assertTrue(result);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testLong() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("LongTest", "$field < 3.53", Object.class,
          Object.class, Long.class);
      boolean result = exp.execute(new Object(), new Object(), new Long(-22));
      assertTrue(result);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testShort() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("ShortTest", "$field < 3.53", Object.class,
          Object.class, Short.class);
      boolean result = exp.execute(new Object(), new Object(), new Short("2"));
      assertTrue(result);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testByte() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("ByteTest", "$field < 3.53", Object.class,
          Object.class, Byte.class);
      boolean result = exp.execute(new Object(), new Object(), new Byte("-22"));
      assertTrue(result);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testBoolean() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      IBooleanExpression exp = creator.create("BooleanTest", "$field", Object.class, Object.class,
          Boolean.class);
      boolean result = exp.execute(new Object(), new Object(), Boolean.TRUE);
      assertTrue(result);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testEquals() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      Integer zero = new Integer(0);
      Integer twenty = new Integer(20);
      IBooleanExpression exp = creator.create("MethodCallTest", "$watcher == $watchee",
          Integer.class, Integer.class, Integer.class);
      boolean result = exp.execute(zero, twenty, new Integer(-1));
      assertFalse(result);
    } catch (Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
  }

  public void testMethodCalls() {
    try {
      BooleanExpressionCreator creator = new BooleanExpressionCreator();
      List list = new ArrayList();
      list.add(new Integer(3));
      List list2 = new ArrayList();
      list2.add(new Double(13234324));
      IBooleanExpression exp = creator.create("MethodCallTest",
          "((Double)$watcher.get(0)).intValue() > $watchee.size() && $field > -100", List.class,
          List.class, Long.class);
      boolean result = exp.execute(list2, list, new Long(-22));
      assertTrue(result);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void testAnnotations() throws IOException, ClassNotFoundException {
    BufferedInputStream stream = new BufferedInputStream(new FileInputStream(
        "../repast.simphony.demos/bin/repast/simphony/demo/simple/SimpleHappyAgent.class"));
    ClassPool pool = ClassPool.getDefault();
    CtClass ctClass = pool.makeClass(stream);
    stream.close();
    CtMethod[] methods = ctClass.getDeclaredMethods();
    for (CtMethod method : methods) {
      MethodInfo info = method.getMethodInfo();
      AnnotationsAttribute attr = (AnnotationsAttribute) info
          .getAttribute(AnnotationsAttribute.visibleTag);
      if (attr != null) {
        Annotation an = attr.getAnnotation("repast.simphony.engine.watcher.Watch");
        if (an != null) {
          String s = ((StringMemberValue) an.getMemberValue("watcheeClassName")).getValue();
          System.out.println("@Watch(name=" + s + ")");
        }
      }
    }
  }

  public static junit.framework.Test suite() {
    return new TestSuite(repast.simphony.engine.watcher.ExpressionCreatorTest.class);
  }
}
