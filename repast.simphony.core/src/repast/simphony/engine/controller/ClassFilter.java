/**
 * 
 */
package repast.simphony.engine.controller;

import java.util.HashSet;
import java.util.Set;

/**
 * Takes a Class and returns true or false depending on whether or not the Class
 * passes the filter.
 * 
 * @author Nick Collier
 */
public abstract class ClassFilter {

  private static final ClassFilter ALWAYS_PASS = new AlwaysPassFilter();

  /**
   * Applies this filter to the specified class.
   * 
   * @param clazz
   * @return true if the class passes the filter otherwise false.
   */
  public abstract boolean apply(Class<?> clazz);
  
  /**
   * Gets a ClassFilter that filters based on whether or not the
   * class passed to the filter is equal to one of the specified classes.
   * If the classes is empty or null, an always pass filter will be returned.
   * 
   * @param classes
   * @return
   */
  public static ClassFilter getNotEqualsFilter(Class<?>...classes) {
    if (classes == null || classes.length == 0) return ALWAYS_PASS;
    if (classes.length == 1) return new ClassFilter1(classes[0]);
    if (classes.length == 2) return new ClassFilter2(classes[0], classes[1]);
    if (classes.length == 3) return new ClassFilter3(classes[0], classes[1], classes[2]);
    if (classes.length == 4) return new ClassFilter4(classes[0], classes[1], classes[2], classes[3]);
    Set<Class<?>> set = new HashSet<Class<?>>();
    for (Class<?> clazz : classes) {
      set.add(clazz);
    }
    return new ClassFilterN(set);
  }

  private static class ClassFilter1 extends ClassFilter {

    private Class<?> c1;

    public ClassFilter1(Class<?> c1) {
      this.c1 = c1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.engine.controller.ClassFilter#apply(java.lang.Class)
     */
    @Override
    public boolean apply(Class<?> clazz) {
      return !c1.equals(clazz);
    }

  }

  private static class ClassFilter2 extends ClassFilter {

    private Class<?> c1, c2;

    public ClassFilter2(Class<?> c1, Class<?> c2) {
      this.c1 = c1;
      this.c2 = c2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.engine.controller.ClassFilter#apply(java.lang.Class)
     */
    @Override
    public boolean apply(Class<?> clazz) {
      return !c1.equals(clazz) && !c2.equals(clazz);
    }
  }

  private static class ClassFilter3 extends ClassFilter {

    private Class<?> c1, c2, c3;

    public ClassFilter3(Class<?> c1, Class<?> c2, Class<?> c3) {
      this.c1 = c1;
      this.c2 = c2;
      this.c3 = c3;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.engine.controller.ClassFilter#apply(java.lang.Class)
     */
    @Override
    public boolean apply(Class<?> clazz) {
      return !c1.equals(clazz) && !c2.equals(clazz) && !c3.equals(clazz);
    }
  }

  private static class ClassFilter4 extends ClassFilter {

    private Class<?> c1, c2, c3, c4;

    public ClassFilter4(Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
      this.c1 = c1;
      this.c2 = c2;
      this.c3 = c3;
      this.c4 = c4;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.engine.controller.ClassFilter#apply(java.lang.Class)
     */
    @Override
    public boolean apply(Class<?> clazz) {
      return !c1.equals(clazz) && !c2.equals(clazz) && !c3.equals(clazz) && !c4.equals(clazz);
    }
  }

  private static class AlwaysPassFilter extends ClassFilter {

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.engine.controller.ClassFilter#apply(java.lang.Class)
     */
    @Override
    public final boolean apply(Class<?> clazz) {
      return true;
    }

  }
  
  private static class ClassFilterN extends ClassFilter {
    
    private Set<Class<?>> set;
    
    public ClassFilterN(Set<Class<?>> set) {
      this.set = set;
    }
    

    /* (non-Javadoc)
     * @see repast.simphony.engine.controller.ClassFilter#apply(java.lang.Class)
     */
    @Override
    public boolean apply(Class<?> clazz) {
      return !set.contains(clazz);
    }
    
  }
}
