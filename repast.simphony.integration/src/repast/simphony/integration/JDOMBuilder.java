/*CopyrightHere*/
package repast.simphony.integration;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

/**
 * This is an {@link repast.simphony.integration.OutputBuilder} that will output to a JDOM Document.
 * 
 * @author Jerry Vos
 */
public class JDOMBuilder implements OutputBuilder<Element, Document> {

	private Document document;

	private Element root;

	private Element curElement;

	private JDOMQueryer queryer;

	public JDOMBuilder() {

	}

	/**
	 * Creates an element and inserts it under it's parent. This parent is either the current object
	 * or it is found by the name parameter if it is an xpath expression. After this call the
	 * created element is set the current element.
	 * 
	 * @param name
	 *            the name of the element to be created. If it is an xpath string just the last part
	 *            of the string represents the name, and the path represents the patth to the new
	 *            element's parent.
	 */
	public Element createAndGoInto(String name) {
		Element parentElement = (Element) DataFileUtils.findExplicitTargetParent(this, name);
		name = DataFileUtils.getName(name);

		Element element = new Element(name);

		if (parentElement == null) {
			parentElement = curElement;
		}

		parentElement.addContent(element);
		curElement = element;
		return element;
	}

	/**
	 * Goes up a level in the DOM tree.
	 */
	public void goUp() {
		curElement = curElement.getParentElement();
	}

	/**
	 * Goes to the root of the DOM tree.
	 */
	public void goRoot() {
		curElement = root;
	}

	/**
	 * Writes a value under the parent element. The value is written by creating a new element and
	 * adding it to the parent element. The parent element is found in the same way as in
	 * {@link #createAndGoInto(String)}. After this call the current element is the same as it was
	 * before the call.
	 * 
	 * @return the created element that was added to its parent
	 */
	public Element writeValue(String name, Object value) {
		Element parentElement = (Element) DataFileUtils.findExplicitTargetParent(this, name);
		name = DataFileUtils.getName(name);

		if (parentElement == null) {
			parentElement = curElement;
		}

		Element element = new Element(name);
		element.addContent(new DataContent(value));
		parentElement.addContent(element);
		return element;
	}

	/**
	 * Returns the {@link Document} that holds the Element tree of values.
	 * 
	 * @return the written document
	 */
	public Document getWrittenObject() {
		return document;
	}

	/**
	 * Removes the current element from its parent and sets the current element to be that parent.
	 */
	public void detach() {
		Element parent = curElement.getParentElement();
		curElement.detach();
		curElement = parent;
	}

	/**
	 * Detaches the given elements from their parent elements.
	 * 
	 * @param objsToDetach
	 *            a group of elements to detach
	 */
	public void detach(Iterable<Element> objsToDetach) {
		for (Element element : objsToDetach) {
			element.detach();
		}
	}

	/**
	 * Creates the document to return and it's root element
	 */
	public void initialize() {
		this.document = new Document(new Element(DataFileElements.FILE_DEF.getTag()));
		this.root = document.getRootElement();
		this.queryer = new JDOMQueryer(document);
		goRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repast.simphony.integration.OutputWriter#getValue(java.lang.String)
	 */
	public Object selectNode(String path) {
		return selectNode(curElement, path);
	}

	public Object selectNode(Object curContext, String path) {
		return queryer.selectNode(curContext, path);
	}

	public List<?> selectNodes(String path) {
		return selectNodes(curElement, path);
	}

	public List<?> selectNodes(Object curContext, String path) {
		return queryer.selectNodes(curContext, path);
	}

	public Object getRoot() {
		return queryer.getRoot();
	}

	public Object getValue(Object o) {
		return queryer.getValue(o);
	}

}
