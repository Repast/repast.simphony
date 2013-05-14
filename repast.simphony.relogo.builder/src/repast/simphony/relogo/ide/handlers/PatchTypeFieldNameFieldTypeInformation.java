package repast.simphony.relogo.ide.handlers;

import org.eclipse.jdt.core.IType;

/**
 * Class to hold the patch type, field name and field type.
 * 
 * @author jozik
 * 
 */
class PatchTypeFieldNameFieldTypeInformation {
	String patchType;
	String fieldName;
	String fieldType;
	String patchGetter;
	String patchSetter;
	

	public PatchTypeFieldNameFieldTypeInformation(String patchType, String fieldName, String fieldType, String patchGetter, String patchSetter) {
		this.patchType = patchType;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.patchGetter = patchGetter;
		this.patchSetter = patchSetter;
	}
	
}