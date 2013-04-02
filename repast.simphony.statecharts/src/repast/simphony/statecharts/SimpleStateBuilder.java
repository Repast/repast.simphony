package repast.simphony.statecharts;

public class SimpleStateBuilder<T> extends AbstractStateBuilder<T>{
	
	public SimpleStateBuilder(String id) {
		super(id);
	}

	public SimpleState<T> build(){
		SimpleState<T> result = new SimpleState<T>(id);
		setAbstractProperties(result);
		return result;
	}
	
}
