package repast.simphony.dataLoader.engine;

import java.io.Serializable;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

/**
 * @author Nick Collier
 */
public class ClassNameContextBuilder implements ContextBuilder {

	private ContextBuilder delegate;

	public ClassNameContextBuilder(ContextBuilder delegate) {
		this.delegate = delegate;
	}

	public ClassNameContextBuilder(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		Class clazz = Class.forName(className);
		delegate = (ContextBuilder)clazz.newInstance();
	}

	public ContextBuilder getDataLoader() {
		return delegate;
	}

	

	/**
	 * Builds and returns a context. Building a context consists of filling it with
	 * agents, adding projects and so forth. When this is called for the master context
	 * the system will pass in a created context based on information given in the
	 * model.score file. When called for subcontexts, each subcontext that was added
	 * when the master context was built will be passed in.
	 *
	 * @param context
	 * @return the built context.
	 */
	public Context build(Context context) {
		return delegate.build(context);
	}

	public ContextBuilder getDelegateDataLoader() {
		return delegate;
	}

	public String getClassName() {
		return delegate.getClass().getName();
	}
}
