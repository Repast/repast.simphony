package repast.simphony.space.physics;

import repast.simphony.engine.schedule.IAction;

/**
 * 
 * @author Eric Tatara
 *
 */
public class PhysicsScheduleAction implements IAction {

	private PhysicsSpace space;
	
	public PhysicsScheduleAction(PhysicsSpace space){
	  this.space = space;	
	}
	
	public void execute() {
		space.step();
	}
}
