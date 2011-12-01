package repast.simphony.ant;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.Path;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

public class BuildPluginClasspath extends Task {
	private String reference;

	private String pluginId;

	private String bootstrapLibDir;

	public String getBootstrapLibDir() {
		return bootstrapLibDir;
	}

	public void setBootstrapLibDir(String bootstrapLibDir) {
		this.bootstrapLibDir = bootstrapLibDir;
	}

	public void execute() throws BuildException {
		Map<String, PluginDescriptor> descriptorMap = (Map<String, PluginDescriptor>) getProject()
				.getReference("pluginMap");
		try {
			SAXParserFactory fac = SAXParserFactory.newInstance();
			fac.setValidating(false);
			SAXParser parser = fac.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setEntityResolver(new DummyEntityResolver());
			InputStream inputStream = new File(getProject().getBaseDir(),
					"plugin.xml").toURL().openStream();
			reader.setContentHandler(new NameExtractor());
			reader.parse(new InputSource(inputStream));
			inputStream.close();
			PluginDescriptor descriptor = descriptorMap.get(pluginId);
			log("Setting classpath for: " + pluginId);
			Path path = new Path(getProject());
			File bootstrapDir = new File(bootstrapLibDir);
			for (File bootLib : bootstrapDir.listFiles(new FilenameFilter() {

				public boolean accept(File arg0, String arg1) {
					if (arg1.endsWith("jar") || arg1.endsWith("zip"))
						return true;
					return false;
				}

			})) {
				FileList fl = new FileList();
				fl.setDir(bootLib.getParentFile());
				FileList.FileName fileName = new FileList.FileName();
				fileName.setName(bootLib.getName());
				fl.addConfiguredFile(fileName);
				path.addFilelist(fl);
			}
			{
				File bootBin = new File(bootstrapDir.getParentFile(), "bin");
				FileList fl = new FileList();
				fl.setDir(bootBin.getParentFile());
				FileList.FileName fileName = new FileList.FileName();
				fileName.setName(bootBin.getName());
				fl.addConfiguredFile(fileName);
				path.addFilelist(fl);
			}
			for (File lib : descriptor.getLibraries()) {
				FileList fl = new FileList();
				fl.setDir(lib.getParentFile());
				FileList.FileName fileName = new FileList.FileName();
				fileName.setName(lib.getName());
				fl.addConfiguredFile(fileName);
				path.addFilelist(fl);
			}
			for (String dep : descriptor.getDependencies()) {
				PluginDescriptor dependency = descriptorMap.get(dep);
				if (dependency == null) {
					continue;
				}
				for (File lib : dependency.getExportedLibraries()) {
					FileList fl = new FileList();
					fl.setDir(lib.getParentFile());
					FileList.FileName fileName = new FileList.FileName();
					fileName.setName(lib.getName());
					fl.addConfiguredFile(fileName);
					path.addFilelist(fl);
				}
			}

			getProject().addReference(reference, path);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	class NameExtractor extends DefaultHandler2 {

		@Override
		public void startElement(java.lang.String uri,
				java.lang.String localName, java.lang.String qName,
				Attributes attributes) throws SAXException {
			if (qName.equals("plugin")) {
				pluginId = attributes.getValue("id");
			}
		}
	}

}
