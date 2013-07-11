package repast.simphony.relogo.factories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import repast.simphony.scenario.ScenarioConstants;
import repast.simphony.scenario.ScenarioUtils;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.scenario.data.UserPathFileReader;
import repast.simphony.util.ClassFinder;
import repast.simphony.util.ClassPathEntry;

/**
 * Utility class to find all subclasses of superClass on the classpath entries
 * specified in a scenario's model data. SIM-489: The method reads the scenario
 * file each time it is called. Can implement local caching of UserPathData if
 * necessary.
 * 
 * @author jozik
 * 
 */
public class ReLogoImplementingClassesFinder {

	// If local caching is needed
	// private static UserPathData upd;

	public static <E> Collection<Class<? extends E>> find(Class<? extends E> E) {
		File modelFile = new File(ScenarioUtils.getScenarioDir(),
				ScenarioConstants.USER_PATH_FILE_NAME);
		List<Class<? extends E>> clazzes = new ArrayList();
		try {
			if (modelFile.exists()) {
				File absoluteModelFile = modelFile.getAbsoluteFile();
				UserPathData data = new UserPathFileReader().read(absoluteModelFile);
				// If local caching is needed
				/*
				 * UserPathData data = upd; if (data == null){ data = new
				 * UserPathFileReader().read(modelFile); upd = data; }
				 */
				// Just picks up the agent entries
				Iterable<ClassPathEntry> cpEntries = data.agentEntries();
				ClassFinder cf = new ClassFinder();

				for (ClassPathEntry cpe : cpEntries) {
					cf.addEntry(cpe);
				}
				List<Class<?>> allClazzes = new ArrayList();
				try {
					allClazzes = cf.findClasses();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				for (Class c : allClazzes) {
					if (E.isAssignableFrom(c)) {
						if (c.getPackage() != null) {
							String[] packageName = c.getPackage().getName()
									.split("[.]");
							Boolean found = false;
							for (String s : packageName) {
								if (s.equals("relogo")) {
									found = true;
								}
							}
							if (found) {
								clazzes.add(c);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return clazzes;
	}

}
