package repast.simphony.relogo.ide.handlers;

import org.eclipse.jdt.core.IType;

/**
 * Class to hold plural information on turtle and link classes.
 * 
 * @author jozik
 * 
 */
class PluralInformation {
	boolean hasPluralAnnotation = false;
	String plural;
	String singular;

	public PluralInformation(IType type) {
		this.singular = type.getElementName();
		this.plural = type.getElementName() + "s";
	}
}