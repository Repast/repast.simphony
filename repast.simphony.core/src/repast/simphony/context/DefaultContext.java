package repast.simphony.context;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DefaultContext<T> extends SmallDefaultContext<T> {

  // NOTE!!! this field is accessed directly via reflection
  // in the DefaultContextConverter which writes a Context
  // to XML. If this is changed then that should be changed as well!!!!
  private Set<T> contents = new HashSet<T>();

  public DefaultContext() {

  }

  public DefaultContext(Object name) {
    this(name, name);
  }

  public DefaultContext(Object name, Object typeID) {
    setId(name);
    setTypeID(typeID);
  }

  /*
    * (non-Javadoc)
    *
    * @see repast.simphony.context.AbstractContext#addInternal(T)
    */
  @Override
  protected boolean addInternal(T o) {
    if (contents.contains(o)) return false;
    boolean retVal = super.addInternal(o);
    if (retVal) {
      // if there's an issue adding to contents
      // then also remove it from super
      retVal = contents.add(o);
      if (!retVal) super.remove(o);
    }

    return retVal;
  }

  /*
    * (non-Javadoc)
    *
    * @see repast.simphony.context.AbstractContext#iterator()
    */
  @Override
  protected Iterator<T> iteratorInternal() {
    return contents.iterator();
  }

  /*
    * (non-Javadoc)
    *
    * @see repast.simphony.context.AbstractContext#removeInternal(java.lang.Object)
    */
  @Override
  protected boolean removeInternal(Object o) {
    return super.removeInternal(o) && contents.remove(o);
  }

  @Override
  protected boolean containsInternal(Object o) {
    return contents.contains(o);
  }

  /*
    * (non-Javadoc)
    *
    * @see java.util.AbstractCollection#size()
    */
  @Override
  protected int sizeInternal() {
    return contents.size();
  }
}
