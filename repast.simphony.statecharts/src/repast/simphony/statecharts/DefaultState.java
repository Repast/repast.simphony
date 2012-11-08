package repast.simphony.statecharts;

import java.util.concurrent.Callable;

import simphony.util.messages.MessageCenter;

public class DefaultState implements State {

	private String id;
	private Callable<Void> onEnter = new Callable<Void>(){
		@Override
		public Void call() throws Exception {
			return null;
		}
	};
	private Callable<Void> onExit = new Callable<Void>(){
		@Override
		public Void call() throws Exception {
			return null;
		}
	};
	
	public DefaultState(String id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return getId();
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void onEnter() {
		try {
			onEnter.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onEnter in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onExit() {
		try {
			onExit.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onExit in state: " + id, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void registerOnEnter(Callable<Void> onEnter) {
		this.onEnter = onEnter;
	}

	@Override
	public void registerOnExit(Callable<Void> onExit) {
		this.onExit = onExit;
	}

}
