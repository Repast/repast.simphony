/*CopyrightHere*/
package repast.simphony.integration;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;

import simphony.util.messages.MessageCenter;

/**
 * A queryable that works on Beans using JXPath. This uses the
 * {@link repast.simphony.integration.JXPathUtils} class to perform the querying, so more
 * information can be found there.
 * 
 * @see repast.simphony.integration.JXPathUtils
 * @see FileDef
 * 
 * @author Jerry Vos
 */
public class BeanQueryer implements Queryable {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(BeanQueryer.class);

	private JXPathContext rootContext;

	/**
	 * Creates the queryer with the specified object as the root. Note this gets wrapped in a
	 * FileDef object, so getRoot() returns a FileDef object that returns the passed in rootObj in
	 * it's FileDef method.
	 * 
	 * @param rootObj
	 *            the root object
	 */
	public BeanQueryer(Object rootObj) {
		this.rootContext = JXPathUtils.createRootContext(rootObj, true);
	}

	/**
	 * Returns the value of <code>JXPathUtils.getXPathNode(rootContext, null, path)}.
	 * 
	 * @see JXPathUtils#getXPathNode(JXPathContext, JXPathContext, String)

	 * @param path
	 *            the XPath string to evaluate
	 * 
	 * @return either an Object or null
	 */
	public Object selectNode(String path) {
		return JXPathUtils.getXPathNode(rootContext, null, path);
	}

	/**
	 * This will create a JXPathContext for the curContext if the curContext Object is not a
	 * JXPathContext and will pass that created context in to
	 * {@link JXPathUtils#getXPathNode(JXPathContext, JXPathContext, String)}.
	 * 
	 * @param curContext
	 *            the current context for evaluation
	 * @param path
	 *            the XPath string to evaluate
	 * @return an object or null.
	 */
	public Object selectNode(Object curContext, String path) {
		if (curContext == rootContext.getContextBean()) {
			curContext = ((FileDef) curContext).getFileDef();
		}
		if (!(curContext instanceof JXPathContext)) {
			msgCenter.info("Building a context for (" + curContext + ")");
			return JXPathUtils.getXPathNode(rootContext, JXPathUtils
					.createContext(curContext, true), path);
		}
		return JXPathUtils.getXPathNode(rootContext, (JXPathContext) curContext, path);
	}

	/**
	 * Returns a collection of nodes according to
	 * {@link JXPathUtils#getXPathNodes(JXPathContext, JXPathContext, String)}.
	 * 
	 * @param path
	 *            the XPath string to evaluate
	 * 
	 * @return a non-null collection of nodes
	 */
	public List<?> selectNodes(String path) {
		return JXPathUtils.getXPathNodes(rootContext, null, path);
	}

	/**
	 * Returns a collection of nodes according to
	 * {@link JXPathUtils#getXPathNodes(JXPathContext, JXPathContext, String)}. If curContext is
	 * not a JXPathContext then this will create one for it and pass it in as the current context to
	 * the JXPathUtils method.
	 * 
	 * @param curContext
	 *            the current context for evaluation
	 * @param path
	 *            the XPath string to evaluate
	 * 
	 * @return a non-null collection of nodes
	 */
	public List<?> selectNodes(Object curContext, String path) {
		if (!(curContext instanceof JXPathContext)) {
			msgCenter.info("Building a context for (" + curContext + ")");
			return JXPathUtils.getXPathNodes(rootContext, JXPathUtils.createContext(curContext,
					true), path);
		}
		return JXPathUtils.getXPathNodes(rootContext, (JXPathContext) curContext, path);
	}

	/**
	 * Returns the root object for this queryer. This will not be the object passed into the
	 * constructor, but will be a FileDef object wrapping that object.
	 * 
	 * @see FileDef
	 * 
	 * @return the root object of the root context
	 */
	public Object getRoot() {
		return rootContext.getContextBean();
	}

	/**
	 * Just returns the passed in object since JXPath doesn't have a concept of nodes.
	 * 
	 * @param o
	 *            the object to get the value of
	 * 
	 * @return the passed in object
	 */
	public Object getValue(Object o) {
		return o;
	}

}
