package repast.simphony.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.java.plugin.JpfException;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.DefaultPluginsCollector;
import org.java.plugin.registry.IntegrityCheckReport;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.util.ExtendedProperties;

import saf.core.runtime.PluginDefinitionException;

/**
 * Loads / unloads model plugins.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ModelPluginLoader {

	private StringBuilder sBuilder = new StringBuilder();
	private PluginManager manager;
	private List<String> registeredPlugins = new ArrayList<String>();

	/**
	 * Creates a ModelPluginLoader that uses the specified PluginManager.
	 *
	 * @param manager
	 */
	public ModelPluginLoader(PluginManager manager) {
		this.manager = manager;
	}

	/**
	 * Gets the PluginManger.
	 *
	 * @return the PluginManger.
	 */
	public PluginManager getManager() {
		return manager;
	}

	/**
	 * Adds a plugin search path.
	 */
	public void addPath(File path) {
		if ((sBuilder.length() > 0) && (path.length() > 0)) {
			sBuilder.append(",");
		}
		sBuilder.append(path.getParentFile().getAbsolutePath());
	}

	public String currentPath() {
		return sBuilder.toString();
	}
	
	/**
	 * Removes any currently registered plugins.
	 */
	public void removePlugins() {
		for (String id : registeredPlugins) {
			manager.deactivatePlugin(id);
		}

		String[] ids = new String[registeredPlugins.size()];
		registeredPlugins.toArray(ids);
		manager.getRegistry().unregister(ids);
		registeredPlugins.clear();
		sBuilder = new StringBuilder();
	}

	/**
	 * Publishes any plugins found in the added paths.
	 *
	 * @throws PluginDefinitionException if any errors were encountered while publishing the plugins.
	 */
	public void publishPlugins() throws PluginDefinitionException {
		DefaultPluginsCollector collector = new DefaultPluginsCollector();
		ExtendedProperties eprops = new ExtendedProperties();
		eprops.put("org.java.plugin.boot.pluginsRepositories", sBuilder.toString());
		try {
			collector.configure(eprops);
			Collection locations = collector.collectPluginLocations();
			PluginManager.PluginLocation[] pluginLocations = (PluginManager.PluginLocation[])
							locations.toArray(new PluginManager.PluginLocation[locations.size()]);
			Map map = manager.publishPlugins(pluginLocations);
			// Check plug-in's integrity
			IntegrityCheckReport report = manager.getRegistry().checkIntegrity(manager.getPathResolver(), true);
			if (report.countErrors() != 0) {
				throw new PluginDefinitionException(parsePluginError(report));
			}
			for (PluginDescriptor descriptor : (Iterable<PluginDescriptor>) map.values()) {
				registeredPlugins.add(descriptor.getId());
			}
		} catch (JpfException e) {
			throw new PluginDefinitionException(e.getMessage(), e);
		} catch (PluginDefinitionException e) {
			throw e;
		} catch (Exception e) {
			throw new PluginDefinitionException(e.getMessage(), e);
		}
	}

	private String parsePluginError(IntegrityCheckReport report) {
		StringBuilder builder = new StringBuilder();
		for (Iterator it = report.getItems().iterator(); it.hasNext();) {
			IntegrityCheckReport.ReportItem item = (IntegrityCheckReport.ReportItem) it.next();
			if (item.getSeverity() == IntegrityCheckReport.ReportItem.SEVERITY_ERROR) {
				builder.append(item.getMessage());
				builder.append("\n");
			}
		}
		return builder.toString();
	}
}
