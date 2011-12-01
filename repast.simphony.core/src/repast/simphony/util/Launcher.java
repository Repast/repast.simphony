package repast.simphony.util;

import java.io.File;
import java.io.IOException;

public class Launcher {
	public static void launch(String className, String classpath) {
		String javaExec = getJavaExecutable();
		
//		if (javaExec == null) {
//			// TODO: handle this error
//		}
		
		if (classpath == null) {
			classpath = System.getProperty("java.class.path");
		}
		
		try {
			Runtime.getRuntime().exec(javaExec + " -cp \"" + classpath + "\" " + className);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getJavaExecutable() {
		String javaHome = System.getProperty("java.home");

		File file = new File(javaHome + SystemConstants.DIR_SEPARATOR + "bin"
				+ SystemConstants.DIR_SEPARATOR + "java");
		
		if (file.exists()) {
			return file.getAbsolutePath();
		} 
		
		file = new File(javaHome + SystemConstants.DIR_SEPARATOR + "bin"
				+ SystemConstants.DIR_SEPARATOR + "java.exe");
		
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		
		return null;
	}
}
