package org.eclipse.gmf.tooling.runtime.structure;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;

public abstract class DiagramStructure {

	public abstract int getVisualID(View view);

	public abstract String getModelID(View view);

	public abstract int getNodeVisualID(View containerView, EObject domainElement);

	public abstract boolean checkNodeVisualID(View containerView, EObject domainElement, int candidate);

	public abstract boolean isCompartmentVisualID(int visualID);

	public abstract boolean isSemanticLeafVisualID(int visualID);

}
