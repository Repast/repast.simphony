package repast.simphony.scenario;

public class TestDescriptor extends AbstractDescriptor {

	public TestDescriptor(String name) {
		super(name);
	}
	
	private int foo;

  
	public int getFoo() {
		return foo;
	}

	public void setFoo(int foo) {
		this.foo = foo;
	}
  
	/*
	 * Transient field that should be initialized on deserialization.
	 */
	public ScenarioChangedSupport getScs() {
		return scs;
	}
  

}
