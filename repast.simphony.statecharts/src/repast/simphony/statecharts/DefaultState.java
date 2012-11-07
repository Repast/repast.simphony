package repast.simphony.statecharts;

import java.util.concurrent.Callable;

import simphony.util.messages.MessageCenter;

public class DefaultState implements State {

	private String id;
	private Callable<Void> onEnter, onExit;
	
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
		if(onEnter == null) return;
		try {
			onEnter.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onEnter in state: " + id, e);
		}
	}

	@Override
	public void onExit() {
		if(onExit == null) return;
		try {
			onExit.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onExit in state: " + id, e);
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
