package repast.simphony.systemdynamics.support;

public class MutableInteger {
	
	private int value;
	
	public MutableInteger() {
		value = 0;
	}
	
	public MutableInteger(int n) {
		value = n;
	}
	
	public MutableInteger(MutableInteger mi) {
		value = mi.value;
	}
	
	public int value() {
		return value;
	}
	
	public int add(int n) {
		value += n;
		return value;
	}
	
	public void setValue(int n) {
		value = n;
	}
	
	public int valueAndInc() {
	    int retVal = value;
	    value++;
	    return retVal;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

}
