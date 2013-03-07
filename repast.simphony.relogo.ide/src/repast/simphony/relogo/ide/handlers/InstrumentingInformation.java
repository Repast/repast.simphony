package repast.simphony.relogo.ide.handlers;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.util.collections.Pair;

/**
 * Class to hold instrumenting information.
 * @author jozik
 *
 */
public class InstrumentingInformation {

	List<String> listOfGlobalFieldNames = new ArrayList<String>();
	List<Pair<String,String>> turtleSingularPlurals = new ArrayList<Pair<String,String>>();
	List<Pair<String,String>> dirLinkSingularPlurals = new ArrayList<Pair<String,String>>();
	List<Pair<String,String>> undirLinkSingularPlurals = new ArrayList<Pair<String,String>>();
	List<Pair<String,String>> patchFieldTypes = new ArrayList<Pair<String,String>>();
	
	public List<String> getListOfGlobalFieldNames() {
		return listOfGlobalFieldNames;
	}
	
	public void addToListOfGlobalFieldNames(String global) {
		listOfGlobalFieldNames.add(global);
	}
	
	public void addToListOfGlobalFieldNames(List<String> globalFieldNames) {
		this.listOfGlobalFieldNames.addAll(globalFieldNames);
	}
	
	public List<Pair<String, String>> getTurtleSingularPlurals() {
		return turtleSingularPlurals;
	}
	
	public void addToTurtleSingularPlurals(Pair<String, String> turtleSingularPlural) {
		turtleSingularPlurals.add(turtleSingularPlural);
	}
	
	public List<Pair<String, String>> getDirLinkSingularPlurals() {
		return dirLinkSingularPlurals;
	}
	
	public void addToDirLinkSingularPlurals(Pair<String, String> dirLinkSingularPlural) {
		dirLinkSingularPlurals.add(dirLinkSingularPlural);
	}
	
	public List<Pair<String, String>> getUndirLinkSingularPlurals() {
		return undirLinkSingularPlurals;
	}
	
	public void addToUndirLinkSingularPlurals(Pair<String, String> undirLinkSingularPlural) {
		undirLinkSingularPlurals.add(undirLinkSingularPlural);
	}
	
	public List<Pair<String, String>> getPatchFieldTypes() {
		return patchFieldTypes;
	}
	
	public void addToPatchFieldTypes(Pair<String, String> patchFieldType) {
		patchFieldTypes.add(patchFieldType);
	}
	
	public void addToPatchFieldTypes(List<Pair<String, String>> patchFieldTypes) {
		this.patchFieldTypes.addAll(patchFieldTypes);
	}

}
