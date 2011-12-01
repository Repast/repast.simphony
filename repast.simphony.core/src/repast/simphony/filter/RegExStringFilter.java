/**
 * 
 */
package repast.simphony.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String filter that matches using regular expressions.
 * 
 * @author Nick Collier
 */
public class RegExStringFilter implements Filter<String> {
  
  protected Pattern pattern;
  
  protected RegExStringFilter() {}
  
  /**
   * Creates a {@link RegExStringFilter} that will evaluate to true
   * if the passed in String matches the specified regular expression.
   * 
   * @param regEx the regular expression to match
   */
  public RegExStringFilter(String regEx) {
    pattern = Pattern.compile(regEx);
  }

  /* (non-Javadoc)
   * @see repast.simphony.filter.Filter#evaluate(java.lang.Object)
   */
  public boolean evaluate(String object) {
    Matcher matcher = pattern.matcher(object);
    return matcher.find();
  }
  
  /**
   * Gets the regular expression pattern for this RegExFilter.
   * 
   * @return the regular expression pattern for this RegExFilter.
   */
  public String getPattern() {
    return pattern.pattern();
  }
}
