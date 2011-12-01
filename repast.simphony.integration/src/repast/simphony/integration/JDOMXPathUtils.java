/*CopyrightHere*/
package repast.simphony.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * Utility classes for working with XPath expressions on JDOM.
 * 
 * @author Jerry Vos
 */
public class JDOMXPathUtils {

	/**
	 * Retrieves the value of an object. The return value is:
	 * <table>
	 * <tr><td><u>o property</u></td><td><u>Value</u></td></tr>
	 * <tr><td>o is null</td><td>null</td></tr>
	 * <tr><td>o is not an {@link Element}</td><td>o.toString()</td></tr>
	 * <tr><td>o is an {@link Element} and has some content</td><td>the first content attribute</td></tr>
	 * <tr><td>o is an {@link Element} and has no content</td><td>null</td></tr>
	 * </table>
	 * 
	 * @param o
	 *            the object to get a value from
	 * @return the object's value as specified
	 */
	public static String getValue(Object o) {
		if (o == null) {
			return null;
		}
	
		if (!(o instanceof Element)) {
			return o.toString();
		}
	
		Element element = (Element) o;
		if (element.getContentSize() != 0) {
			return element.getContent(0).getValue();
		} else {
			return null;
		}
	}

	/**
	 * Returns the nodes selected in the given context with the given XPath string.
	 * 
	 * @see XPath#selectNodes(java.lang.Object)
	 * 
	 * @param xpathExpression
	 *            the XPath expression
	 * @param nodeContext
	 *            the element to apply the XPath query to
	 * @return the results of {@link XPath#selectNodes(java.lang.Object)}
	 * @throws JDOMException
	 *             if there was an error querying
	 */
	public static List<?> evalXPaths(String xpathExpression, Element nodeContext)
			throws JDOMException {
	
		// TODO: cache these xpath objects
		XPath xpath;
		xpath = XPath.newInstance(xpathExpression);
		return xpath.selectNodes(nodeContext);
	}

	/**
	 * Returns the first result from {@link evalXPaths} or null if no such element
	 * exists.
	 * 
	 * @see evalXPaths
	 * 
	 * @param xpathExpression
	 *            the XPath expression to use
	 * @param nodeContext
	 *            the element to apply the XPath query to
	 * @return the first result from {@link evalXPaths} or null
	 * @throws JDOMException
	 *             if there was an error querying
	 */
	public static Object evalXPath(String xpathExpression, Element nodeContext)
			throws JDOMException {
	
		Iterable<?> results = evalXPaths(xpathExpression, nodeContext);
	
		if (results == null) {
			return null;
		}
	
		Iterator<?> iter = results.iterator();
		if (iter.hasNext()) {
			return iter.next();
		}
		return null;
	}

	/**
	 * Returns a list of Strings which are the result of calling {@link getValue} on the
	 * results of {@link evalXPaths}.
	 * 
	 * @param xpathExpression
	 *            the xpath expression
	 * @param nodeContext
	 *            the context to search under
	 * @return a list of strings
	 * @throws JDOMException
	 *             if there was an error using the XPath expression
	 */
	public static List<String> evalXPathStrings(String xpathExpression, Element nodeContext)
			throws JDOMException {
	
		List<?> xpathResult = evalXPaths(xpathExpression, nodeContext);
		if (xpathResult == null) {
			return null;
		}
		ArrayList<String> vals = new ArrayList<String>(xpathResult.size());
		for (Object obj : xpathResult) {
			vals.add(getValue(obj));
		}
		return vals;
	}

	/**
	 * Same as <code>getValue(evalXPath(xpathExpression, nodeContext));</code>
	 * 
	 * @see getValue
	 * @see evalXPath
	 */
	public static String evalXPathString(String xpathExpression, Element nodeContext)
			throws JDOMException {
	
		Object obj = evalXPath(xpathExpression, nodeContext);
	
		return getValue(obj);
	}

}
