package repast.simphony.visualization.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.visualization3D.style.ValueLayerStyle3D;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * This implementation of class utilities is different from the one in
 * repast.simphony.util.ClassUtilities only in that here the exceptions are not
 * reported. Occasionally, we might come across a class on the classpath with
 * dependencies that can't be resolved, and this will catch those errors but not
 * report them. Since this method is only used to find style classes for the GUI
 * wizard, this wont be a problem.
 * 
 * @author Eric Tatara
 * 
 * TODO Projections: Only need a single get_() method that also provides the style class.
 */
public class ClassFinder {

  /**
   * Gets a list of Class<?> names that are assignable from a given class
   * 
   * @param context
   * @param styleClass
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
	public static List<String> getFoundClasses(ContextData context, Class<?> requiredClass) {
		java.util.List<Class<?>> classes = ClassFinder.getClasses(context);
		List<String> names = new ArrayList<>();
		for (Class<?> clazz : classes)
			if (requiredClass.isAssignableFrom(clazz))
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
}
