package repast.simphony.ant;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DummyEntityResolver implements EntityResolver {

	public InputSource resolveEntity(String arg0, String arg1)
			throws SAXException, IOException {
		InputSource source = new InputSource(DummyEntityResolver.class
				.getResourceAsStream("plugin_0_4.dtd"));
		return source;
	}

}
