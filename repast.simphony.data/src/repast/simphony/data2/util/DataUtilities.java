/**
 * 
 */
package repast.simphony.data2.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.data2.DataSource;
import repast.simphony.data2.engine.CustomDataSourceDefinition;
import repast.simphony.data2.engine.DataSetComponentControllerAction;
import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.data2.engine.MethodDataSourceDefinition;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;

/**
 * Static util methods for working with DataSetDescriptors etc.
 * 
 * @author Nick Collier
 */
public class DataUtilities {

  /**
   * Gets a list of the data set descriptors defined for the specified context.
   * 
   * @param reg
   * @param contextId
   * @return
   */
  public static List<DataSetDescriptor> getDataSetDescriptors(ControllerRegistry reg,
      Object contextId) {
    List<DataSetDescriptor> descriptors = new ArrayList<DataSetDescriptor>();
    ControllerAction parent = reg.findAction(contextId, ControllerActionConstants.DATA_SET_ROOT);
    for (ControllerAction action : reg.getActionTree(contextId).getChildren(parent)) {
      if (action instanceof DataSetComponentControllerAction) {
        descriptors.add(((DataSetComponentControllerAction) action).getDescriptor());
      }
    }

    return descriptors;
  }

  /**
   * Gets whether or not the specified data sources produces numeric data.
   * 
   * @param def
   * 
   * @return true if the data source produces numeric data, otherwise false.
   * 
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static boolean isNumeric(CustomDataSourceDefinition def) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException {

    String cname = def.getDataSourceClassName();
    Class<?> clazz = Class.forName(cname);
    DataSource source = (DataSource) clazz.newInstance();
    Class<?> dataType = source.getDataType();
    if (dataType.isPrimitive() && !dataType.equals(void.class) && !dataType.equals(boolean.class))
      return true;
    return Number.class.isAssignableFrom(source.getDataType());
  }
  
  public static boolean isNumeric(Class<?> dataType) {
    if (dataType.isPrimitive() && !dataType.equals(void.class) && !dataType.equals(boolean.class)) return true;
    return Number.class.isAssignableFrom(dataType);
  }

  /**
   * Gets whether or not the specified data sources produces numeric data.
   * 
   * @param def
   * 
   * @return true if the data source produces numeric data, otherwise false.
   * 
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public static boolean isNumeric(MethodDataSourceDefinition def) throws ClassNotFoundException {
    String cname = def.getObjTargetClass();
    Class<?> clazz = Class.forName(cname);
    for (Method method : clazz.getMethods()) {
      if (method.getParameterTypes().length == 0 && method.getName().equals(def.getMethodName())) {
        Class<?> ret = method.getReturnType();
        if (ret.isPrimitive() && !ret.equals(void.class) && !ret.equals(boolean.class))
          return true;
        return Number.class.isAssignableFrom(ret);
      }
    }
    return false;

  }
  
  /**
   * Rename any existing file with the specified filename. This will append
   * an incremented number to the filename until it won't clobber any existing
   * files.
   * 
   * @param filename the filename to rename if it exists
   */
  public static void renameFileIfExists(String filename) {
    File originalFile = new File(filename);
    if (!originalFile.exists()) {
      return;
    }

    File movedFileName = new File(originalFile.getAbsolutePath());
    
    long i = 0;
    while (movedFileName.exists()) {
      int index = filename.lastIndexOf(".");
      if (index != -1) {
        movedFileName = new File(filename.substring(0, index) + "." + i + filename.substring(index, filename.length()));
      } else {
        filename = filename + "." + i;
      }
      i++;
    }

    originalFile.renameTo(movedFileName);
  }


}
