package repast.simphony.relogo.ide.handlers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GeneratedFilesInformationHolder {

	Set<String> instrumentingPackageNames = new LinkedHashSet<String>();
	
	
	public void addInstrumentingPackageName(String instrumentingPackageName){
		instrumentingPackageNames.add(instrumentingPackageName);
	}
	
	// TODO:
	public void addReLogoLibTurtleInformation(String instrumentingPackageName, String libraryTurtleExtended, String reLogoLibTurtleName){
		
	}
	// TODO:
	public void addReLogoLibPatchInformation(){
		
	}
	// TODO:
	public void addReLogoLibLinkInformation(){
		
	}
	// TODO:
	public void addReLogoLibObserverInformation(){
		
	}

	static private final String[] standardReLogoClasses = {"ReLogoTurtle", "ReLogoPatch", "ReLogoLink", "ReLogoObserver"};
	
	
	public List<String> getAllReLogoSrcGenClasses(){
		List<String> allClasses = new ArrayList<String>();
		for (String instrumentingPackageName : instrumentingPackageNames){
			for (String className : standardReLogoClasses){
				StringBuilder sb = new StringBuilder();
				sb.append(instrumentingPackageName);
				sb.append(".");
				sb.append(className);
				allClasses.add(sb.toString());
			}
		}
		// TODO: get information about lib extending classes
		
		return allClasses;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("CleaningInformationHolder contents:");
		for(String c : getAllReLogoSrcGenClasses()){
			sb.append("\n");
			sb.append(c);
		}
		return sb.toString();
	}
}
