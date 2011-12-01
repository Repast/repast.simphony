/*CopyrightHere*/
package repast.simphony.integration;

import java.util.List;

/**
 * An interface representing an object that is queryable, particularly using XPath expressions. This
 * need not be a DOM, but could be a bean using JXPath or some such thing.<p/>
 * 
 * This interfaces uses the term Node to refer to an object returned by an XPath expression. Value
 * represents the value that a node contains or the value of a node. These need not be different
 * things for instance, on a DOM a Node could be an Element of the DOM tree, its value could be it's
 * CDATA or some other content. On a Bean the node could just be the value returned by the query,
 * and its value would be the same object.
 * 
 * @see repast.simphony.integration.JDOMQueryer
 * @see repast.simphony.integration.BeanQueryer
 * 
 * @author Jerry Vos
 */
public interface Queryable {

	/**
	 * Selects a node from using the current location (or root if no such thing exists) using the
	 * specified path. If multiple nodes are returned with the query this will be the first one.
	 * 
	 * @param path
	 *            the XPath query
	 * @return the selected node (can be null if the query failed)
	 */
	Object selectNode(String path);

	/**
	 * Selects a node using the specified context as the current location. If multiple nodes are
	 * returned with the query this will be the first one.
	 * 
	 * @see #selectNode(String)
	 * 
	 * @param curContext
	 *            the context of the query
	 * @param path
	 *            the query
	 * @return the value of the query
	 */
	Object selectNode(Object curContext, String path);

	/**
	 * Selects multiple nodes using the query.
	 * 
	 * @param path
	 *            the query
	 * @return the result
	 */
	List<?> selectNodes(String path);

	/**
	 * Selects multiple nodes using the query based at the specified context.
	 * 
	 * @param path
	 *            the query
	 * @return the result
	 */
	List<?> selectNodes(Object curContext, String path);

	/**
	 * Extracts a value from a Node and returns it. What this means is implementation dependent.
	 * 
	 * @param node
	 *            the node to extract a value from
	 * @return
	 */
	Object getValue(Object node);

	/**
	 * Retrieves the root object.
	 * 
	 * @return the root
	 */
	Object getRoot();
}