/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard.builder;

public class RelationshipDescriptor {

	private double strength = 1.0;
	
	private Object source;
	
	private Object target;

	public RelationshipDescriptor(AgentDescriptor source, AgentDescriptor target, double strength) {
		this.source = source;
		this.target = target;
		this.strength = strength;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
}
