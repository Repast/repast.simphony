package repast.simphony.ant;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

public class PluginLibHandler extends DefaultHandler2 {
	@Override
	public void startElement(java.lang.String uri, java.lang.String localName,
			java.lang.String qName, Attributes attributes) throws SAXException {
		if (qName.equals("import")) {

		} else if (qName.equals("plugin")) {

		}
	}
}
