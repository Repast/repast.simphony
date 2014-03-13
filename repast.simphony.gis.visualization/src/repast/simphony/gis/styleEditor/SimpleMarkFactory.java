package repast.simphony.gis.styleEditor;

import java.awt.Shape;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.style.WellKnownMarkFactory;
import org.opengis.filter.expression.Expression;

import simphony.util.messages.MessageCenter;

/**
 * MarkFactory that provides Styled Layer Descriptor (SLD) compatible shapes
 *   based on the "well known name" element of the SLD schema.
 * 
 * @author Eric Tatara
 *
 */
public class SimpleMarkFactory extends WellKnownMarkFactory {

	private static WellKnownMarkFactory markFac = new WellKnownMarkFactory();
	private static MessageCenter msg = MessageCenter.getMessageCenter(SimpleMarkFactory.class);
	
	private static String[] WKT_List = new String[]{
			"circle",
			"cross",
			"star",
			"square",
			"triangle",
			"X",
			"arrow"};
	
  /**
   * Provide a list of all well-know text strings for mark shapes.
   * @return the list of available WKT mark names.
   */
	public static String[] getWKT_List() {
		return WKT_List;
	}

	/**
	 * Creates a Shape using the well-known text (WKT) for the mark based on the 
	 * SLD 1.0 specification
	 * 
	 * @param markName the well-known mark name ("circle", "square", etc.) 
	 * @return the shape
	 */
	public Shape getMark(String markName){
		Expression markExpression = CommonFactoryFinder.getFilterFactory().literal(markName);
		Shape markShape = null;
		try {
			markShape = markFac.getShape(null,markExpression,null);
		} catch (Exception e) {
			msg.error("Error creating preview", e);
		}
		return markShape;
	}
}