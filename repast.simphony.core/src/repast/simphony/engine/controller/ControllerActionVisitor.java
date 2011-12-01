package repast.simphony.engine.controller;

import repast.simphony.engine.environment.ControllerAction;


/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:11:55 $
 */
public interface ControllerActionVisitor {

	void visit(ControllerAction action);


	void visitContextSchedulableControllerAction(DefaultContextSchedulableControllerAction action);

	void visitSchedulableAction(DefaultSchedulableAction action);
}
