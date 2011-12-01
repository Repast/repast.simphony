package repast.simphony.runtime;

import java.io.File;
import java.net.URL;

/**
 * Main class for launching Repast Simphony application
 * 
 * @author tatara
 * 
 */
public class RepastMain {

	public static void main(String[] args) {

		String[] pathInfo = new String[2];

		if (args.length > 0) {
			// the scenario path
			pathInfo[0] = args[0];
		} else {
			// otherwise use default
			pathInfo[0] = "";
		}

		if (args.length > 1) {
			
			// if the runtime path is provided
			pathInfo[1] = args[1];
			
		} else {
			
			// otherwise use default
			URL runtimeSource = repast.simphony.runtime.RepastMain.class
					.getProtectionDomain().getCodeSource().getLocation();
			
			String path = runtimeSource.getFile().replaceAll("%20", " ");
			
			pathInfo[1] = new File(path).getParent()
					+ File.separator;
			
		}
		saf.core.runtime.Boot.main(pathInfo);
	}
}