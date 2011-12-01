/*CopyrightHere*/
package repast.simphony.integration;

import java.security.InvalidParameterException;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;

import simphony.util.messages.MessageCenter;

/**
 * Utility methods for working with JXPath.
 * 
 * @author Jerry Vos
 */
public class JXPathUtils {
	/**
	 * The correct representation of the root FileDef tag (/fileDef) according to what JXPath requires.
	 */
	public static final String XPATH_FILE_DEF_CORRECT = DataFileElements.FILE_DEF.getTag()
			.replaceFirst("F", "f");

	/**
	 * The normal (default) FileDef string.
	 */
	public static final String XPATH_FILE_DEF_WRONG = DataFileElements.FILE_DEF.getTag();

	
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(JXPathUtils.class);

	
	/**
	 * Creates a context with the specified object as its root and the specified leniency.
	 * 
	 * @param rootObj
	 *            the object at the root of the context
	 * @param lenient
	 *            whether the context is lenient about xpath errrors or not
	 * @return a newly created context
	 */
	public static JXPathContext createContext(Object rootObj, boolean lenient) {
		JXPathContext context = JXPathContext.newContext(rootObj);
		context.setLenient(lenient);
		return context;
	}

	/**
	 * Creates a context for the root of a data file. This wraps the specified object in a FileDef
	 * object so that xpath strings like "/FileDef/..." work correctly.
	 * 
	 * @param obj
	 *            the object to return from "/FileDef" xpath strings
	 * @param lenient
	 *            whether to be lenient about xpath errors
	 * @return the newly created context
	 */
	public static JXPathContext createRootContext(Object obj, boolean lenient) {
		return createContext(new FileDef(obj), lenient);
	}

	/**
	 * If the path begins with a '/' then the root context is returned, otherwise curContext is.
	 * Hopefully in the future this method will also handle higher nested levels of contexts and
	 * relative paths in contexts, but as of now it does not.
	 * 
	 * @param rootContext
	 *            the context to be returned for finding from root
	 * @param curContext
	 *            the context to be returned for normal (non-rooted) checking
	 * @param path
	 *            the xpath expression
	 * @return the context as specified
	 */
	public static JXPathContext getContext(JXPathContext rootContext, JXPathContext curContext,
			String path) {
		if (!path.startsWith("/")) {
			if (curContext == null) {
				return rootContext;
			} else {
				return curContext;
			}
		} else if (rootContext != null) {
			return rootContext;
		} else {
			return curContext;
		}
	}

	/**
	 * When doing xpath against beans we need to reference the root with /fileDef, but to be
	 * consistent with the DOM code we convert FilDef to fileDef.
	 * 
	 * @param path
	 *            the path to possibly convert
	 * 
	 * @return the result of replacing {@link #XPATH_FILE_DEF_WRONG} with
	 *         {@link #XPATH_FILE_DEF_CORRECT}
	 */
	public static String fixPath(String path) {
		return path.replace(XPATH_FILE_DEF_WRONG, XPATH_FILE_DEF_CORRECT);
	}

	/**
	 * Retrieves a value from a context (which depends on the path) based on the xpath expression
	 * and returns it.
	 * 
	 * @see #getContext(JXPathContext, JXPathContext, String)
	 * 
	 * @param rootContext
	 *            the root context
	 * @param curContext
	 *            the current context
	 * @param path
	 *            the xpath expression
	 * @return the result of applying the xpath expression to the context returned by
	 *         {@link #getContext(JXPathContext, JXPathContext, String)}
	 */
	public static Object getXPathNode(JXPathContext rootContext, JXPathContext curContext,
			String path) {
		if (rootContext == null && curContext == null) {
			InvalidParameterException ex = new InvalidParameterException(
					"Must pass in a non-null context to evaluate, rootContext and curContext cannot both be null");
			msgCenter.error("rootContext and curContext cannot both be null", ex);
			throw ex;
		}

		JXPathContext contextToUse = getContext(rootContext, curContext, path);

		// replace /FileDef with /fileDef because otherwise jxpath doesn't work correctly
		path = fixPath(path);

		return contextToUse.getValue(path);
	}

	/**
	 * Retrieves a set of nodes from a context (which depends on the path) based on the xpath expression
	 * and returns it.
	 * 
	 * @see #getContext(JXPathContext, JXPathContext, String)
	 * 
	 * @param rootContext
	 *            the root context
	 * @param curContext
	 *            the current context
	 * @param path
	 *            the xpath expression
	 * @return the result of applying the xpath expression to the context returned by
	 *         {@link #getContext(JXPathContext, JXPathContext, String)}
	 */
	public static List<?> getXPathNodes(JXPathContext rootContext, JXPathContext curContext,
			String path) {
		if (rootContext == null && curContext == null) {
			InvalidParameterException ex = new InvalidParameterException(
					"Must pass in a non-null context to evaluate, rootContext and curContext cannot both be null");
			msgCenter.error("rootContext and curContext cannot both be null", ex);
			throw ex;
		}

		JXPathContext contextToUse = getContext(rootContext, curContext, path);

		// replace /FileDef with /fileDef because otherwise jxpath doesn't work correctly
		path = fixPath(path);

		return contextToUse.selectNodes(path);
	}
}
