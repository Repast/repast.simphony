package repast.simphony.statecharts;

public interface State {
	public String getId();
	public void setId(String id);
	public void onEnter();
	public void onExit();
}
