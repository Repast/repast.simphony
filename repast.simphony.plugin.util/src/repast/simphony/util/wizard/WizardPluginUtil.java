/*CopyrightHere*/
package repast.simphony.util.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

import simphony.util.messages.MessageCenter;

/**
 * Utilities for working with plugin-able wizards. This helps in loading settings for a wizard whose
 * options are loaded from plugin descriptions (plugin.xml's).
 * 
 * @see repast.simphony.util.wizard.WizardOption
 * 
 * @author Jerry Vos
 */
public class WizardPluginUtil {
	public static final String CLASS_KEY = "class";

	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(WizardPluginUtil.class);

	/**
	 * Retrieves {@link WizardOption}s of the specified type from the plugin manager. The options
	 * are read in from the specified plugin and extension point. The extension point is assumed to
	 * contain a class field ({@link #CLASS_KEY}) that specifies the class of the wizard option.<p/>
	 * 
	 * If the register option is true, this will register the loaded options into the
	 * {@link DynamicWizard} with the extensionPointId as the wizardId.
	 * 
	 * @see DynamicWizard#registerWizardOption(String, WizardOption)
	 * 
	 * @param manager
	 *            the plugin manager that the extension points will be retrieved from
	 * @param baseOptionClass
	 *            the option class (cannot be null as it is used for casting)
	 * @param extensionPointPluginId
	 *            the id of the plugin that the extension point is declared in
	 * @param extensionPointId
	 *            the id of the extension point that the wizard options will be registered to
	 * @param register
	 *            whether or not to register the loaded plugins to the {@link DynamicWizard}
	 * @return all the options that could be instantiated from the specified point
	 */
	@SuppressWarnings("unchecked")
	public static <T extends WizardOption> List<T> loadWizardOptions(PluginManager manager,
			Class<T> baseOptionClass, String extensionPointPluginId, String extensionPointId, boolean register) {
		ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint(extensionPointPluginId,
				extensionPointId);

		ArrayList<T> options = new ArrayList<T>();

		for (Extension ext : (Collection<Extension>) extPoint.getConnectedExtensions()) {
			// workaround for bug in JPF
			if (!ext.getExtendedPluginId().equals(extensionPointPluginId)) {
				continue;
			}
			String pluginID = ext.getDeclaringPluginDescriptor().getId();
			String className = null;
			try {
				manager.activatePlugin(pluginID);
				className = ext.getParameter(CLASS_KEY).valueAsString();

				PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext
						.getDeclaringPluginDescriptor());
				Class clazz = Class.forName(className, true, pluginClassLoader);

				T option = baseOptionClass.cast(clazz.newInstance());

				option.init(manager);

				options.add(option);
				
				if (register) {
					DynamicWizard.registerWizardOption(extensionPointPluginId + extensionPointId, option);
				}
				
			} catch (ClassCastException e) {
				msgCenter.warn("Class '" + className + "' in '" + pluginID + "' must implement '"
						+ baseOptionClass + "'.", e);
			} catch (ClassNotFoundException e) {
				msgCenter.warn("Unable to create class '" + className + "' in " + extensionPointId
						+ " in plugin '" + pluginID + "'", e);
			} catch (NoClassDefFoundError e) {
				msgCenter.warn("Unable to create class '" + className + "' in " + extensionPointId
						+ " in plugin '" + pluginID + "'", e);
			} catch (IllegalAccessException e) {
				msgCenter.warn("Unable to create class '" + className + "' in " + extensionPointId
						+ " in plugin '" + pluginID + "'", e);
			} catch (InstantiationException e) {
				msgCenter.warn("Unable to create class '" + className + "' in " + extensionPointId
						+ " in plugin '" + pluginID + "'", e);
			} catch (PluginLifecycleException e) {
				msgCenter.warn("Error activating plugin '" + pluginID + "' in " + extensionPointId
						+ "'.", e);
			}
		}
		return options;
	}
}
