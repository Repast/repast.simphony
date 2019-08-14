package org.eclipse.gmf.tooling.runtime.edit.policies.labels;

import org.eclipse.gef.EditPolicy;

public interface IRefreshableFeedbackEditPolicy extends EditPolicy {
	/**
	 * Notifies the editpolicy that it is time to refresh feedback due to external changes 
	 */
	public void refreshFeedback();

}
