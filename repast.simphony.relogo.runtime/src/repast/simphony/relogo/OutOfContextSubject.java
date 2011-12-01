package repast.simphony.relogo;

public interface OutOfContextSubject<T extends OutOfContextSubscriber> {
	public void registerSubscriber(T s);
	public void removeSubscriber(T s);
	public void notifySubscribers();
}
