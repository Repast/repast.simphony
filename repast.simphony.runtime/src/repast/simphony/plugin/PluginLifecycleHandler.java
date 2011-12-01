package repast.simphony.plugin;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.registry.PluginAttribute;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.standard.StandardPluginLifecycleHandler;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class PluginLifecycleHandler extends StandardPluginLifecycleHandler {

	public static final String MODEL_PLUGIN = "model_plugin";

	private ExtendablePluginClassLoader loader;

	/**
	 * Creates standard implementation of plug-in class loader.
	 *
	 * @see org.java.plugin.standard.PluginLifecycleHandler#createPluginClassLoader(
	 *      org.java.plugin.registry.PluginDescriptor)
	 */
	@Override
	protected PluginClassLoader createPluginClassLoader(final PluginDescriptor descr) {
		// load model plugins in their own class loader, so we can
		// unload them when the model is unloaded
		PluginAttribute attribute = descr.getAttribute(MODEL_PLUGIN);
		if (attribute != null && Boolean.parseBoolean(attribute.getValue())) {
			return new ExtendablePluginClassLoader(getPluginManager(), descr, getClass().getClassLoader());
		}

		if (loader == null) {
			loader = new ExtendablePluginClassLoader(getPluginManager(), descr, getClass().getClassLoader());
		} else {
			loader.addDescriptor(descr);
		}
		return loader;
	}
}
