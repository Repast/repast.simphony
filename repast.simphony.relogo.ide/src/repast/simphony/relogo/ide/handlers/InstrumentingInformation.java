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
	List<TypeSingularPluralInformation> turtleSingularPlurals = new ArrayList<TypeSingularPluralInformation>();
	List<TypeSingularPluralInformation> dirLinkSingularPlurals = new ArrayList<TypeSingularPluralInformation>();
	List<TypeSingularPluralInformation> undirLinkSingularPlurals = new ArrayList<TypeSingularPluralInformation>();
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
	
	public List<TypeSingularPluralInformation> getTurtleSingularPlurals() {
		return turtleSingularPlurals;
	}
	
	public void addToTurtleSingularPlurals(TypeSingularPluralInformation turtleSingularPlural) {
		turtleSingularPlurals.add(turtleSingularPlural);
	}
	
	public List<TypeSingularPluralInformation> getDirLinkSingularPlurals() {
		return dirLinkSingularPlurals;
	}
	
	public void addToDirLinkSingularPlurals(TypeSingularPluralInformation dirLinkSingularPlural) {
		dirLinkSingularPlurals.add(dirLinkSingularPlural);
	}
	
	public List<TypeSingularPluralInformation> getUndirLinkSingularPlurals() {
		return undirLinkSingularPlurals;
	}
	
	public void addToUndirLinkSingularPlurals(TypeSingularPluralInformation undirLinkSingularPlural) {
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
