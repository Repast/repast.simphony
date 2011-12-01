package repast.simphony.ui;

import java.util.Iterator;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

import repast.simphony.ui.plugin.TickCountFormatter;
import saf.core.runtime.PluginDefinitionException;

/**
 * Processes Repast Simphony related gui extension points.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class UIExtPointLoader {

	private RSGui gui;
	private boolean tickFormatterLoaded = false;

	/**
	 * Creates a new UIExtPointLoader that will work with the specified gui.
	 *
	 * @param gui
	 */
	public UIExtPointLoader(RSGui gui) {
		this.gui = gui;
	}

	/**
	 * @return true if the last calls to processExtPoints loaded a TickCountFormatter, otherwise
	 * false.
	 */
	public boolean isTickFormatterLoaded() {
		return tickFormatterLoaded;
	}

	/**
	 * Process rs gui related extension points using the specified plugin manager.
	 *
	 * @param manager the manager to use
	 *
	 * @throws PluginLifecycleException if there is an error in plugin processing
	 * @throws PluginDefinitionException if there is an error in plugin processing
	 */
	public void processExtPoints(PluginManager manager) throws PluginLifecycleException, PluginDefinitionException {
		tickFormatterLoaded = processTickLabel(manager);
	}

	// returns true if we have registered a new tick count formatter.
	private boolean processTickLabel(PluginManager manager) throws PluginLifecycleException, PluginDefinitionException {
		ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint("repast.simphony.gui", "tick.label.formatter");
		for (Iterator iter = extPoint.getConnectedExtensions().iterator(); iter.hasNext();) {
			Extension ext = (Extension) iter.next();
			String pluginID = ext.getDeclaringPluginDescriptor().getId();
			manager.activatePlugin(pluginID);
			String className = ext.getParameter("class").valueAsString();
			PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext.getDeclaringPluginDescriptor());
			try {
				TickCountFormatter formatter = null;
				if (className.equalsIgnoreCase("none")) {
					formatter = new TickCountFormatter() {
						String empty = "";
						public String format(double tick) {
							return empty;
						}

						public String getInitialValue() {
							return empty;
						}
					};
				} else {
					Class clazz = Class.forName(className, true, pluginClassLoader);
					formatter = (TickCountFormatter) clazz.newInstance();
				}
				gui.setTickCountFormatter(formatter);

			} catch (ClassCastException e) {
				throw new PluginDefinitionException("tick.label.formatter class '" + className + "'in '" + pluginID +
								"' must implement TickCountCreator", e);
			} catch (ClassNotFoundException e) {
				throw new PluginDefinitionException("Unable to create class '" + className +
								"' in tick.label.formatter in plugin '" + pluginID + "'", e);
			} catch (NoClassDefFoundError e) {
				throw new PluginDefinitionException("Unable to create class '" + className +
								"' in tick.label.formatter in plugin '" + pluginID + "'", e);
			} catch (IllegalAccessException e) {
				throw new PluginDefinitionException("Unable to create class '" + className +
								"' in tick.label.formatter in plugin '" + pluginID + "'", e);
			} catch (InstantiationException e) {
				throw new PluginDefinitionException("Unable to create class '" + className +
								"' in tick.label.formatter in plugin '" + pluginID + "'", e);
			}
		}
		return extPoint.getConnectedExtensions().size() > 0;
	}
}
