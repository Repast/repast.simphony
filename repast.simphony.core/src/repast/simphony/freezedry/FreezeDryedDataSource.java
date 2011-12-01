package repast.simphony.freezedry;


public interface FreezeDryedDataSource {

  public void reset();

  public void write(FreezeDryedObject object) throws FreezeDryingException;

	public Iterable<FreezeDryedObject> read(Class clazz)
			throws FreezeDryingException;

	public FreezeDryedObject read(Class clazz, String key)
			throws FreezeDryingException;

  public void close() throws FreezeDryingException;
}
