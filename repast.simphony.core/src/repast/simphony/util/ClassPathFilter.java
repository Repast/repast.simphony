/**
 * 
 */
package repast.simphony.util;

import java.io.File;
import java.util.regex.Pattern;

import repast.simphony.filter.RegExStringFilter;

/**
 * Filter that takes a fully qualified class or package name expression
 * and filters found classes on whether they match the expression. The
 * expression can contain the "*" wildcard. 
 * 
 * @author Nick Collier
 */
public class ClassPathFilter extends RegExStringFilter {
  
  /**
   * @param filterExpression
   */
  public ClassPathFilter(String filterExpression) {
    super();
    filterExpression = filterExpression.replace(".", "\\.");
    // match right to the end of the input
    filterExpression += "\\.class";
    // replace * wildcard with the proper regex version
    filterExpression = filterExpression.replace("*", "\\w*");
    pattern = Pattern.compile(filterExpression);
  }

 /**
  * Evaluates class paths (e.g. foo\bar\Baz.class) against
  * an filter.
  * 
  * @return true if the class path matches the filter
  * otherwise false.
  */
  @Override
  public boolean evaluate(String pathExp) {
    // pathExp might be from a jar which may always have "/"
    // rather than "\" on windows
    pathExp = pathExp.replace("/", ".");
    pathExp = pathExp.replace(File.separator, ".");
    if (!pathExp.endsWith(".class")) pathExp += ".class";
    return super.evaluate(pathExp);
  }
}
