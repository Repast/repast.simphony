package repast.simphony.ant;

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

public class PluginHandler extends DefaultHandler2 {

	PluginDescriptor descriptor;

	File currentLibrary;

	public PluginHandler(PluginDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public void startElement(java.lang.String uri, java.lang.String localName,
			java.lang.String qName, Attributes attributes) throws SAXException {
		if (qName.equals("import")) {
			descriptor.addDependency(attributes.getValue("plugin-id"));
		} else if (qName.equals("plugin")) {
			descriptor.setPluginId(attributes.getValue("id"));
		} else if (qName.equals("library")) {
			currentLibrary = new File(descriptor.getBaseDir(), attributes
					.getValue("path"));
			descriptor.addLibrary(currentLibrary);

		} else if (qName.equals("export")) {
			if (currentLibrary != null) {
				descriptor.addExportedLibrary(currentLibrary);
			}
		}
	}

	public PluginDescriptor getDescriptor() {
		return descriptor;
	}

}
