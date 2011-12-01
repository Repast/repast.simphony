package repast.simphony.ant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class PluginDescriptor {
	private String pluginId;

	private List<String> dependencies = new LinkedList<String>();

	private List<File> libraries = new LinkedList<File>();

	private List<File> exportedLibraries = new LinkedList<File>();

	private File basedir;

	private File buildFile;

	public File getBasedir() {
		return basedir;
	}

	public void setBasedir(File basedir) {
		this.basedir = basedir;
	}

	public File getBuildFile() {
		return buildFile;
	}

	public void setBuildFile(File buildFile) {
		this.buildFile = buildFile;
	}

	public File getBaseDir() {
		return basedir;
	}

	public void setBaseDir(File basedir) {
		this.basedir = basedir;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public void addDependency(String dependency) {
		dependencies.add(dependency);
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String toString() {
		return pluginId;
	}

	public void addExportedLibrary(File library) {
		exportedLibraries.add(library);
	}

	public List<File> getExportedLibraries() {
		return exportedLibraries;
	}

	public void addLibrary(File library) {
		libraries.add(library);
	}

	public List<File> getLibraries() {
		return libraries;
	}
}
