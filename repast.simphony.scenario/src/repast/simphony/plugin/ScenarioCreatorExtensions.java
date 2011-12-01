package repast.simphony.plugin;

import java.util.Iterator;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

import repast.simphony.scenario.ScenarioCreator;
import repast.simphony.scenario.ScenarioCreatorExtension;

/**
 * Loads scenario.creator.plugin extensions.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ScenarioCreatorExtensions {

	private static final String SCENARIO_CREATOR_EXTENSION = "scenario.creator.extension";


	public void loadExtensions(PluginManager manager) throws ClassNotFoundException,
					IllegalAccessException, InstantiationException
	{
		ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint("repast.simphony.scenario",
						SCENARIO_CREATOR_EXTENSION);

		for (Iterator iter = extPoint.getConnectedExtensions().iterator(); iter.hasNext();) {
			Extension ext = (Extension) iter.next();
			String className = ext.getParameter("className").valueAsString();
			PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext.getDeclaringPluginDescriptor());

			Class clazz = Class.forName(className, true, pluginClassLoader);
			ScenarioCreatorExtension extension = (ScenarioCreatorExtension) clazz.newInstance();
			ScenarioCreator.registerExtension(extension);
		}
	}
}
