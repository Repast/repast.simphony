package repast.simphony.context.space.graph;

import simphony.util.messages.MessageCenter;

/**
 * Abstract base implementation of NetworkGenerator.
 * 
 * @author Nick Collier
 */
public abstract class AbstractGenerator<T> implements NetworkGenerator<T> {

  protected static final MessageCenter msg = MessageCenter.getMessageCenter(AbstractGenerator.class);

}
