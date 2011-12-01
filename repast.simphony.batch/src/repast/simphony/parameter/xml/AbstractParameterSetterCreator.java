package repast.simphony.parameter.xml;

import org.xml.sax.Attributes;
import repast.simphony.parameter.ParameterFormatException;

import java.util.Map;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractParameterSetterCreator implements ParameterSetterCreator{

  protected interface ValueGetter {
    String getValue(String name);
  }

  protected class AttributesValueGetter implements ValueGetter {

    Attributes attributes;

    public AttributesValueGetter(Attributes attributes) {
      this.attributes = attributes;
    }

    public String getValue(String name) {
      return attributes.getValue(name);
    }
  }

  protected class MapValueGetter implements ValueGetter {

      Map<String, String> attributes;

      public MapValueGetter(Map<String, String> attributes) {
        this.attributes = attributes;
      }

      public String getValue(String name) {
        return attributes.get(name);
      }
    }


  protected ValueGetter attributes;
	protected String name;

	/**
	 * Initializes this ParameterSetterCreator with the specified attributes.
	 * Any following calls to addParameter or createSetter will use this
	 * attributes.
	 *
	 * @param attributes
	 */
	public void init(Attributes attributes) throws ParameterFormatException {
		this.attributes = new AttributesValueGetter(attributes);
		this.name = this.attributes.getValue("name");
	}

  /**
	 * Initializes this ParameterSetterCreator with the specified attributes.
	 * Any following calls to addParameter or createSetter will use this
	 * attributes.
	 *
	 * @param attributes
	 */
	public void init(Map<String, String> attributes) throws ParameterFormatException {
		this.attributes = new MapValueGetter(attributes);
		this.name = this.attributes.getValue("name");
	}
}
