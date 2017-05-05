package repast.simphony.gis.engine;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ServiceLoader;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.IIOServiceProvider;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;

import com.sun.media.jai.imageioimpl.ImageReadWriteSpi;

import repast.simphony.engine.environment.RunEnvironment;
import simphony.util.messages.MessageCenter;

/**
 * JAIInitializer provides Java Advanced Imaging initialization functions to support
 * the Geography projection.
 * 
 * @author Eric Tatara
 *
 */
public class JAIInitializer {

	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(JAIInitializer.class);

	/**
	 * Initialize Java Advanced Imaging library performs init functions for JAI that
	 * are needed because Repast uses a custom classloader that prevents JAI from
	 * initializing itself automatically.
	 */
	public void initJAI() {
		// Disable JAI MediaLib native libraries since we don't provide them 
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
		
		// First add the JAI-ImageIO OperationRegistrySPI instance to the JAI OperationRegistry
		OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
		
		if (registry == null) {
			msgCenter.error("Cannot initialize Java Advanced Imaging.", new Exception());
			return;
		} 
		else {
			try {
				new ImageReadWriteSpi().updateRegistry(registry);
			} catch (IllegalArgumentException e) {
				msgCenter.warn("Cannot initialize JAI ImageReadWriteSpi", e);
			}

			// Next look for JAI registry files on classpath
			// Note: META-INF/javax.media.jai.registryFile.jai in jai_core.jar and 
			//       META-INF/registryFile.jaiext in jt-*.jar should be loaded automatically
			
			Enumeration<URL> resources = null;;
			try {
				resources = getClass().getClassLoader().getResources("META-INF/registryFile.jai");
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (resources.hasMoreElements()) {
				URL url = null;
				try {
					url = resources.nextElement();
					msgCenter.debug("Register JAI resource: " + url.toString());
					registry.updateFromStream(url.openStream());

				} catch (IOException e) {
					msgCenter.warn("Cannot initialize JAI  resource " + url.toString(), e);
				}
			}
		}

		// Find ImageIO SPI entries on the classpath and register them
		IIORegistry ioRegistry = IIORegistry.getDefaultInstance();
		ClassLoader loader = this.getClass().getClassLoader();

		Iterator<Class<?>> categories = ioRegistry.getCategories();
		while (categories.hasNext()) {
			Class<?> clazz = categories.next();
			msgCenter.debug("Loading IO Registry category: " + clazz.getName());
			Iterator<IIOServiceProvider> riter = ServiceLoader.load((Class<IIOServiceProvider>) clazz, loader).iterator();

			while (riter.hasNext()) {
				IIOServiceProvider provider = riter.next();
				msgCenter.debug("\t Registering provider " + provider.getClass().getName());
				ioRegistry.registerServiceProvider(provider);
			}
		}
	}	
}