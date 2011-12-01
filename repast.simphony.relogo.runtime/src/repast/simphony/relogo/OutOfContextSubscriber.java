package repast.simphony.relogo;

public interface OutOfContextSubscriber<T extends OutOfContextSubject> {
	public void update(T o);
}
