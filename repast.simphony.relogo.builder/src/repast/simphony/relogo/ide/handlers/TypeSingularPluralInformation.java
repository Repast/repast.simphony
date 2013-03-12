package repast.simphony.relogo.ide.handlers;

import org.eclipse.jdt.core.IType;

/**
 * Class to hold plural information on turtle and link classes.
 * 
 * @author jozik
 * 
 */
class TypeSingularPluralInformation {
	String fullyQualifiedName;
	String singular;
	String plural;
	

	public TypeSingularPluralInformation(IType type) {
		this.fullyQualifiedName = type.getFullyQualifiedName();
		this.singular = type.getElementName();
		this.plural = type.getElementName() + "s";
	}
}