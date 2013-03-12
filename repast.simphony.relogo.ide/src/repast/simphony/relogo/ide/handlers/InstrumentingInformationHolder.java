package repast.simphony.relogo.ide.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.util.collections.Pair;

/**
 * Class to hold instrumenting information.
 * 
 * @author jozik
 * 
 */
public class InstrumentingInformationHolder {

	Map<String, InstrumentingInformation> instrumentingPackageMap = new HashMap<String, InstrumentingInformation>();

	protected InstrumentingInformation getNonNullInstrumentingInformation(
			String instrumentingPackageName) {
		InstrumentingInformation ii = instrumentingPackageMap.get(instrumentingPackageName);
		if (ii == null) {
			ii = new InstrumentingInformation();
			instrumentingPackageMap.put(instrumentingPackageName, ii);
		}
		return ii;
	}

	public void putTurtlePluralInformation(TypeSingularPluralInformation pi, String instrumentingPackageName) {
		InstrumentingInformation ii = getNonNullInstrumentingInformation(instrumentingPackageName);
		ii.addToTurtleSingularPlurals(pi);
	}

	public void putDirLinkPluralInformation(TypeSingularPluralInformation pi, String instrumentingPackageName) {
		InstrumentingInformation ii = getNonNullInstrumentingInformation(instrumentingPackageName);
		ii.addToDirLinkSingularPlurals(pi);
	}

	public void putUndirLinkPluralInformation(TypeSingularPluralInformation pi, String instrumentingPackageName) {
		InstrumentingInformation ii = getNonNullInstrumentingInformation(instrumentingPackageName);
		ii.addToUndirLinkSingularPlurals(pi);
	}

	public void putPatchFieldTypes(List<Pair<String,String>> patchFieldTypes, String instrumentingPackageName) {
		InstrumentingInformation ii = getNonNullInstrumentingInformation(instrumentingPackageName);
		ii.addToPatchFieldTypes(patchFieldTypes);
	}
	
	public void putGlobalsInfo(List<String> globalFieldNames, String instrumentingPackageName) {
		InstrumentingInformation ii = getNonNullInstrumentingInformation(instrumentingPackageName);
		ii.addToListOfGlobalFieldNames(globalFieldNames);
	}

	public InstrumentingInformation getInstrumentingInformationFor(String instrumentingPackageName) {
		return instrumentingPackageMap.get(instrumentingPackageName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
			sb.append("###### Outputting IIH Info: ######");
			sb.append('\n');
			for (String ipn : instrumentingPackageMap.keySet()) {
				sb.append("### For package: " + ipn);
				sb.append('\n');
				InstrumentingInformation ii = instrumentingPackageMap.get(ipn);
				sb.append("# Turtles info:");
				sb.append('\n');
				for (TypeSingularPluralInformation pi : ii.getTurtleSingularPlurals()) {
					sb.append("FullyQualifiedName: " + pi.fullyQualifiedName + ", Singular: " + pi.singular + ", Plural: " + pi.plural);
					sb.append('\n');
				}
				sb.append("# DirLink info:");
				sb.append('\n');
				for (TypeSingularPluralInformation pi : ii.getDirLinkSingularPlurals()) {
					sb.append("FullyQualifiedName: " + pi.fullyQualifiedName + ", Singular: " + pi.singular + ", Plural: " + pi.plural);
					sb.append('\n');
				}
				sb.append("# UndirLink info:");
				sb.append('\n');
				for (TypeSingularPluralInformation pi : ii.getUndirLinkSingularPlurals()) {
					sb.append("FullyQualifiedName: " + pi.fullyQualifiedName + ", Singular: " + pi.singular + ", Plural: " + pi.plural);
					sb.append('\n');
				}
				sb.append("# Patch fields info:");
				sb.append('\n');
				for (Pair<String, String> pair : ii.getPatchFieldTypes()) {
					sb.append("FieldName: " + pair.getFirst() + ", FieldType: " + pair.getSecond());
					sb.append('\n');
				}
				sb.append("# Global fields info:");
				sb.append('\n');
				for (String global : ii.getListOfGlobalFieldNames()) {
					sb.append("FieldName: " + global);
					sb.append('\n');
				}
			}
		return sb.toString();
	}
	
	

}
