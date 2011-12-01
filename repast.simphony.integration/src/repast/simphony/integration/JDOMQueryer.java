/*CopyrightHere*/
package repast.simphony.integration;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import simphony.util.messages.MessageCenter;

/**
 * A queryable that works with JDOM. The querying functionality of this class is based on the
 * {@link repast.simphony.integration.JDOMXPathUtils} class, and most methods delegate in some way to
 * that class.
 * 
 * @author Jerry Vos
 */
public class JDOMQueryer implements Queryable {
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(JDOMQueryer.class);

	private Document document;

	/**
	 * Constructs this queryer working on the specified document.
	 * 
	 * @param document
	 *            the document to run queries against
	 */
	public JDOMQueryer(Document document) {
		super();
		this.document = document;
	}

	/**
	 * Selects the node using the given xpath string. This uses the root element of the document as
	 * the current context. A search of "/" will return the root document.
	 * 
	 * @see #getRoot()
	 * @see #selectNode(Object, String)
	 * 
	 * @return the object (generally an Element) selected with the given path
	 */
	public Object selectNode(String path) {
		return selectNode(document.getRootElement(), path);
	}

	/**
	 * Selects the node using the given xpath string. Using the given the object as the context to
	 * search from. This context is used in relative queries, such as "element/sub", as the current
	 * location in the DOM tree.<br/>
	 * 
	 * The curContext currently must be an element. A document (for instance the root document) is
	 * not an acceptable value. the current context. A search of "/" will return the root document.
	 * 
	 * @see #getRoot()
	 * 
	 * @return the object (generally an Element) selected with the given path
	 */
	public Object selectNode(Object curContext, String path) {
		// TODO: "/" is a Document, this should handle curContext being a Document
		// However, getRoot returns the root element, not the document, so the behavior could go
		// either way
		if (!(curContext instanceof Element)) {
			msgCenter
					.warn("Received a context that was not an element. The context must be an element.  Returning null.");
			return null;
		}
		try {
			return JDOMXPathUtils.evalXPath(path, (Element) curContext);
		} catch (JDOMException e) {
			// XPath attempt failed, return null
		}
		return null;
	}

	/**
	 * Retrieves a list of nodes as specified in the select node methods.
	 * 
	 * @return a list of nodes
	 */
	public List<?> selectNodes(String path) {
		return selectNodes(document.getRootElement(), path);
	}

	/**
	 * Retrieves a list of nodes as specified in the select node methods.
	 * 
	 * @return a list of nodes
	 */
	public List<?> selectNodes(Object curContext, String path) {
		if (!(curContext instanceof Element)) {
			msgCenter
					.warn("Received a context that was not an element. The context must be an element.  Returning null.");
			return null;
		}
		try {
			return JDOMXPathUtils.evalXPaths(path, (Element) curContext);
		} catch (JDOMException e) {
			// XPath attempt failed, return null
		}
		return null;
	}

	/**
	 * The root element of the document specified in the construction.
	 * 
	 * @return the document's root element
	 */
	public Object getRoot() {
		return document.getRootElement();
	}

	/**
	 * @see JDOMXPathUtils#getValue(Object)
	 */
	public Object getValue(Object o) {
		return JDOMXPathUtils.getValue(o);
	}

}
