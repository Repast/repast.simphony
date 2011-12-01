/*CopyrightHere*/
package repast.simphony.util;

import simphony.settings.SettingsIO;
import simphony.settings.SettingsRegistry;
import simphony.util.messages.MessageCenter;

import java.io.IOException;

public class Settings {
	public static final String SETTINGS_FILE_NAME = "Repast.settings";
	public static final String SETTINGS_REGISTRY_ID = "Repast.registry";

	private static final MessageCenter LOG = MessageCenter.getMessageCenter(Settings.class);
	
	private static SettingsRegistry settingsRegistry;
	
	public static SettingsRegistry getRegistry() {
		if (settingsRegistry == null) {
			try {
				settingsRegistry = SettingsRegistry.getRegistry(SETTINGS_REGISTRY_ID, SETTINGS_FILE_NAME);
			} catch (RuntimeException ex) {
				settingsRegistry = new SettingsRegistry(SETTINGS_REGISTRY_ID);
				LOG.warn("Error while loading Repast settings, will continue with default settings", ex);
			}
		}
		
		return settingsRegistry;
	}

	public static void storeSettings() throws IOException {
		SettingsIO.storeSettings(settingsRegistry, SETTINGS_FILE_NAME);
	}
	
	public static Object get(String key) {
		return getRegistry().get(key);
	}
	
	public static void put(String key, Object value) {
		getRegistry().put(key, value);
	}
}
