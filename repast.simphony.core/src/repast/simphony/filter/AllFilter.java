package repast.simphony.filter;

/**
 * A Filter that always returns true.
 * 
 * @author Nick Collier
 * @param <T>
 *          the type to filer on
 */
public class AllFilter<T> implements Filter<T> {
  public static final Filter INSTANCE = new AllFilter();

  public boolean evaluate(T object) {
    return true;
  }

}
