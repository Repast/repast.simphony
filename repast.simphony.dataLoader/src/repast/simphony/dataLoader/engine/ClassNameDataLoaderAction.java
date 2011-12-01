package repast.simphony.dataLoader.engine;

import repast.simphony.scenario.Scenario;

/**
 * Action for creating contexts using an object created via reflection from a
 * class name.
 * 
 * @author Nick Collier
 */
public class ClassNameDataLoaderAction extends DataLoaderControllerAction<ClassNameContextBuilder> {

  public ClassNameDataLoaderAction(ClassNameContextBuilder loader) {
    super("Class Name data loader", loader, null);
  }

  public ClassNameDataLoaderAction(ClassNameContextBuilder loader, Scenario scenario) {
    super("Class Name data loader", loader, scenario.getContext());
  }

  public ClassNameDataLoaderAction(String className, Scenario scenario) 
      throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    super("Class Name data loader", new ClassNameContextBuilder(className), scenario.getContext());
  }
}
