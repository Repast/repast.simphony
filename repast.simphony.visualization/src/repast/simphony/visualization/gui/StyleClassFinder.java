package repast.simphony.visualization.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.visualization3D.style.EdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * This implementation of class utilities is different from the one in
 * repast.simphony.util.ClassUtilities only in that here the exceptions are not
 * reported. Occasionally, we might come across a class on the classpath with
 * dependencies that can't be resolved, and this will catch those errors but not
 * report them. Since this method is only used to find style classes for the GUI
 * wizard, this wont be a problem.
 * 
 * @author tatara
 * 
 * TODO Projections: Only need a single get_() method that also provides the style class.
 */
public class StyleClassFinder {

  /**
   * Gets a list of Class<?> names that are assignable from Style3D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailable3DStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    for (Class<?> clazz : classes)
      if (Style3D.class.isAssignableFrom(clazz))
	names.add(clazz.getName());

    return names;
  }

  private static List<Class<?>> getClasses(ContextData context) {
    List<Class<?>> classes = new ArrayList<Class<?>>();

    try {
      classes = context.getClasspath().getClasses();
    } catch (IOException e) {

    } catch (ClassNotFoundException e) {
    }
    return classes;
  }

  /**
   * Gets a list of Class<?> names that are assignable from Style2D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailable2DStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    for (Class<?> clazz : classes)
      if (StyleOGL2D.class.isAssignableFrom(clazz)) names.add(clazz.getName());

    return names;
  }

  /**
   * Gets a list of Class<?> names that are assignable from StyleGIS3D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailableGIS3DStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    // TODO WWJ - need to find all StyleGIS subclasses?
    for (Class<?> clazz : classes)
      if (StyleGIS.class.isAssignableFrom(clazz))
	names.add(clazz.getName());

    return names;
  }

  /**
   * Gets a list of Class<?> names that are assignable from EdgeStyleGIS3D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  // TODO WWJ - networks
//  public static List<String> getAvailable3DGISEdgeStyles(ContextData context) {
//    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
//    List<String> names = new ArrayList<String>();
//    for (Class<?> clazz : classes)
//      if (EdgeStyleGIS3D.class.isAssignableFrom(clazz))
//	names.add(clazz.getName());
//
//    return names;
//  }

  /**
   * Gets a list of Class<?> names that are assignable from EdgeStyle3D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailable3DEdgeStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    for (Class<?> clazz : classes)
      if (EdgeStyle3D.class.isAssignableFrom(clazz))
	names.add(clazz.getName());

    return names;
  }

  /**
   * Gets a list of Class<?> names that are assignable from EdgeStyle2D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailable2DEdgeStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    for (Class<?> clazz : classes)
      if (EdgeStyleOGL2D.class.isAssignableFrom(clazz))
	names.add(clazz.getName());

    return names;
  }

  /**
   * Gets a list of Class<?> names that are assignable from ValueLayerStyle3D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailable3DValueLayerStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    for (Class<?> clazz : classes)
      if (ValueLayerStyle3D.class.isAssignableFrom(clazz))
	names.add(clazz.getName());

    return names;
  }

  /**
   * Gets a list of Class<?> names that are assignable from ValueLayerStyle3D
   * 
   * @param context
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<String> getAvailable2DValueLayerStyles(ContextData context) {
    java.util.List<Class<?>> classes = StyleClassFinder.getClasses(context);
    List<String> names = new ArrayList<String>();
    for (Class<?> clazz : classes)
      if (ValueLayerStyleOGL.class.isAssignableFrom(clazz))
	names.add(clazz.getName());

    return names;
  }
}
