package repast.simphony.userpanel.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.scenario.Scenario;
import repast.simphony.util.ClassFinder;
import repast.simphony.util.ClassPathEntry;

/**
 * Utility class to find all subclasses of superClass on the classpath entries specified in a scenario's model data.
 * @author jozik
 *
 */
public class ImplementingClassesFinder {
	
	private Scenario scenario;
	private Class superClass;
	
	public ImplementingClassesFinder(Scenario scenario, Class superClass){
		this.scenario = scenario;
		this.superClass = superClass;
	}
	
	public List<Class<?>> findClasses(){
		ClassFinder cf = new ClassFinder();
		Iterable<ClassPathEntry> cpEntries = scenario.getModelData().classpathEntries();
	    for (ClassPathEntry cpe : cpEntries){
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
	    
		List<Class<?>> clazzes = new ArrayList();
		for (Class c : allClazzes){
			if (superClass.isAssignableFrom(c)){
				clazzes.add(c);
			}
		}
		return clazzes;
	}

}
