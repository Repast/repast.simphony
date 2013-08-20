package repast.simphony.systemdynamics.support;

public class MutableBoolean {

private boolean value;
	
	public MutableBoolean() {
		value = true;
	}
	
	public MutableBoolean(boolean n) {
		value = n;
	}
	
	public MutableBoolean(MutableBoolean mi) {
		value = mi.value;
	}
	

	
	public void setValue(boolean n) {
		value = n;
	}
	
	public boolean value() {
		return value;
	}
	

}
