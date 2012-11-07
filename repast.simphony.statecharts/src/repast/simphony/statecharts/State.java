package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public interface State {
	public String getId();
	public void onEnter();
	public void onExit();
	public void registerOnEnter(Callable<Void> onEnter);
	public void registerOnExit(Callable<Void> onExit);
}
