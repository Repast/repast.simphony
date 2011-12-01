package repast.simphony.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class PluginList extends Task {
	private List buildFiles = new ArrayList(); // List (FileSet)

	private String reference;

	private boolean haltOnError = true;

	private boolean reverse = false;

	private String root = "*";

	private boolean excludeRoot = false;

	public void addFileset(FileSet buildFiles) {
		this.buildFiles.add(buildFiles);
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getRoot() {
		return root;
	}

	private Map<String, PluginDescriptor> descriptorMap = new HashMap<String, PluginDescriptor>();

	public void execute() throws BuildException {
		this.log("Setting Build Order");
		if (reference == null) {
			throw new BuildException(
					"reference should be provided in ivy build list");
		}
		if (buildFiles.isEmpty()) {
			throw new BuildException(
					"at least one nested fileset should be provided in ivy build list");
		}
		Path path = new Path(getProject());
		for (ListIterator iter = this.buildFiles.listIterator(); iter.hasNext();) {
			FileSet fs = (FileSet) iter.next();
			DirectoryScanner ds = fs.getDirectoryScanner(getProject());
			String[] builds = ds.getIncludedFiles();
			for (String build : builds) {
				File buildFile = new File(ds.getBasedir(), build);
				File pluginFile = new File(buildFile.getParent(), "plugin.xml");
				if (pluginFile.exists()) {
					try {
						SAXParserFactory fac = SAXParserFactory.newInstance();
						fac.setValidating(false);
						SAXParser parser = fac.newSAXParser();
						XMLReader reader = parser.getXMLReader();
						reader.setEntityResolver(new DummyEntityResolver());
						InputStream inputStream = pluginFile.toURL()
								.openStream();
						PluginDescriptor descriptor = new PluginDescriptor();
						descriptor.setBaseDir(buildFile.getParentFile());
						descriptor.setBuildFile(buildFile);
						PluginHandler pluginHandler = new PluginHandler(
								descriptor);
						reader.setContentHandler(pluginHandler);
						reader.parse(new InputSource(inputStream));
						inputStream.close();
						descriptorMap.put(descriptor.getPluginId(), descriptor);
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					}
				} else {
					continue;
				}
			}
		}
		Iterator<PluginDescriptor> iterator = descriptorMap.values().iterator();
		while (iterator.hasNext()) {
			sortPlugins(iterator.next(), new Stack<PluginDescriptor>());
		}
		for (PluginDescriptor descriptor : sorted) {
			log("Adding build for: " + descriptor.getPluginId());
			File buildFile = descriptor.getBuildFile();
			FileList fl = new FileList();
			fl.setDir(buildFile.getParentFile());
			FileList.FileName fileName = new FileList.FileName();
			fileName.setName(buildFile.getName());
			fl.addConfiguredFile(fileName);
			path.addFilelist(fl);
		}
		getProject().addReference(reference, path);
		getProject().addReference("pluginMap", descriptorMap);
	}

	private List<PluginDescriptor> sorted = new LinkedList<PluginDescriptor>();

	private void sortPlugins(PluginDescriptor current,
			Stack<PluginDescriptor> callStack) {
		if (sorted.contains(current)) {
			return;
		}
		if (callStack.contains(current)) {
			callStack.add(current);
			System.out.println("Circular Dependency");
			return;
		}
		for (String id : current.getDependencies()) {
			callStack.push(current);
			if (descriptorMap.get(id) != null) {
				sortPlugins(descriptorMap.get(id), callStack);
			}
			callStack.pop();
		}
		sorted.add(current);
	}

	public void setExcludeRoot(boolean root) {
		this.excludeRoot = root;
	}

	public boolean isExcludeRoot() {
		return excludeRoot;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReference() {
		return reference;
	}

	public List getBuildFiles() {
		return buildFiles;
	}

	public void setBuildFiles(List buildFiles) {
		this.buildFiles = buildFiles;
	}

	public boolean isHaltOnError() {
		return haltOnError;
	}

	public void setHaltOnError(boolean haltOnError) {
		this.haltOnError = haltOnError;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
