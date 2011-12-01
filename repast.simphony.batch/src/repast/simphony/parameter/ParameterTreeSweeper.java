/*CopyrightHere*/
package repast.simphony.parameter;


import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections15.Predicate;

import repast.simphony.engine.graph.EngineGraphUtilities;
import repast.simphony.engine.graph.Executor;
import repast.simphony.engine.graph.NaryTreeTraverser;
import repast.simphony.util.collections.NaryTree;
import repast.simphony.util.collections.TreeVisitor;
import simphony.util.messages.MessageCenter;

/**
 * The default implementation of a parameter sweeper. This will just next through all its
 * {@link repast.simphony.parameter.ParameterSetter}s always performing the nexts in the same way. <p/>
 * <p/>
 * For a given parameter tree this sweeper will: <br/> First, execute a next on all of the elements
 * in the tree generating a base set of parameters that will be executed. <br/> Second, execute the
 * leaves of the tree until they are finished. After one finishes it will next the parent of that
 * item, and then execute its subleaves again. In other words, the lowest levels are executed until
 * they are finished, as they finish their parents are then called, followed by calling the lower
 * levels again. Finish propagates upwards until everything has finished.
 *
 * @author Jerry Vos
 */
public class ParameterTreeSweeper implements ParameterSweeper {

	private static MessageCenter LOG = MessageCenter.getMessageCenter(ParameterTreeSweeper.class);

	class DummyParamSetter implements ParameterSetter {

		public void reset(Parameters params) {
		}

		public boolean atEnd() {
			return true;
		}

		public void next(Parameters params) {
		}

		public String toString() {
			return "[root]";
		}
	}

	protected ParameterSetter rootSetter;
	protected RunParameterSetter runSetter;

	protected ParameterSetter lastAddedSetter;

	protected boolean firstTime;

	protected NaryTree<ParameterSetter> paramTree;

	protected NaryTreeTraverser<ParameterSetter> traverser;
	

	/**
	 * Default constructor.
	 */
	public ParameterTreeSweeper() {
		rootSetter = new DummyParamSetter();
		runSetter = new RunParameterSetter(1);
		this.paramTree = new NaryTree<ParameterSetter>(rootSetter);
		paramTree.addNode(rootSetter, runSetter);
		this.traverser = new NaryTreeTraverser<ParameterSetter>(paramTree);
		this.firstTime = true;
		lastAddedSetter = runSetter;
	}

	/**
	 * Returns true if all the {@link ParameterSetter}s have finished.
	 *
	 * @return (not) all {@link ParameterSetter}s have finished
	 */
	public boolean atEnd() {
		return childrenFinished(rootSetter);
	}

	/**
	 * Executes the {@link ParameterSetter}s as specified in this class's description.
	 *
	 * @param params the parameters object to load settings into
	 */
	public void next(Parameters params) {
		if (firstTime) {
			firstParameters(params);
			firstTime = false;
		} else {
			nextParameters(params);
		}
	}

	/**
	 * Resets all the parameter initializers
	 */
	public void reset(Parameters params) {
		firstTime = true;

		rootSetter.reset(null);
		resetChildren(rootSetter, params);
	}

	protected Parameters nextParameters(ParameterSetter init, Parameters params) {
		if (paramTree.getChildren(init).size() == 0 || childrenFinished(init)) {
			// either this is a leaf or its children are finished
			nextSelf(init, params);
		} else {
			for (ParameterSetter child : paramTree.getChildren(init)) {
				nextParameters(child, params);
			}
		}

		return params;
	}

	protected void nextSelf(final ParameterSetter init, Parameters params) {
		boolean doReset = !init.atEnd();

		init.next(params);

		if (doReset) {
			// we only want to reset the child if the next of the current object did something
			resetChildren(init, params);
		}

//        for (ParameterInitializer child : paramTree.getChildren(init)) {
//            nextParameters(child, params);
//        }
	}

	protected void resetChildren(final ParameterSetter init, final Parameters params) {
		EngineGraphUtilities.breadthFirstMap(new Executor<ParameterSetter>() {
			public void execute(ParameterSetter toExecuteOn) {
				if (toExecuteOn != init) {
					toExecuteOn.reset(params);
				}
			}
		}, traverser, init);
	}

	protected boolean childrenFinished(final ParameterSetter parent) {
		// if this returns an object then it's found one that isn't finished, so the children
		// aren't finished
		return EngineGraphUtilities.breadthFirstSearch(new Predicate<ParameterSetter>() {
			private static final long serialVersionUID = 5701489867964980769L;

			public boolean evaluate(ParameterSetter init) {
				if (init != parent && !init.atEnd()) {
					return true;
				}
				return false;
			}

		},
						traverser,
						parent) == null;
	}

	protected Parameters nextParameters(Parameters params) {
		return nextParameters(rootSetter, params);
	}

	protected Parameters firstParameters(final Parameters params) {
		// this is the simple case, just have to execute all the parameters once to get the initial
		// values
		paramTree.preOrderTraversal(new TreeVisitor<ParameterSetter>() {
			public void visit(ParameterSetter node) {
				if (node != rootSetter) {
					node.reset(params);
				}
			}
		});

		return params;
	}

	/**
	 * Adds the given {@link ParameterSetter} to specified parent initialzier.
	 *
	 * @param parent the parent setter the new setter is a child of
	 * @param setter a parameter setter that will be executed
	 * @throws IllegalArgumentException if either the parent or the setter are null.
	 */
	public void add(ParameterSetter parent, ParameterSetter setter) {
		if (parent == null || setter == null) {
			IllegalArgumentException ex = new IllegalArgumentException("param cannot be null");
      LOG.error("Cannot pass in a null parameter name to a sweeper's add method.", ex);
			throw ex;
		}
		paramTree.addNode(parent, setter);
	}

	/**
	 * Adds the given {@link ParameterSetter} to be executed as a direct descendent
	 * of previously added node. The last added setter will be the "innermost" setter
	 * executed during a sweep.
	 * <p/>
	 * All convenience methods use this method.
	 * <p/>
	 * (This supports the typical usage where we simply intend
	 * to create an n-D cube space to sweep through.
	 *
	 * @param setter a parameter setter that will be executed
	 */
	public void add(ParameterSetter setter) {
		paramTree.addNode(lastAddedSetter, setter);
		lastAddedSetter = setter;
	}

	/**
	 * Adds the specified parameter setter as a child of the root.
	 *
	 * @param setter the setter to add
	 */
	public void addToRoot(ParameterSetter setter) {
		add(getRootParameterSetter(), setter);
	}

	/**
	 * Removes the given {@link ParameterSetter} from those to be executed
	 *
	 * @param setter a parameter setter to remove
	 */
	public void remove(ParameterSetter setter) {
		paramTree.removeNode(setter);
	}

	/**
	 * Adds a sweep dimension for the specifed integer parameter -- that is, for every run currently defined for this sweeper,
	 * runs will now be made for each step from base to max.
	 * (Conveience method.)
	 *
	 * @param param parameter name
	 * @param base  starting value
	 * @param max   ending value
	 * @param step  added to current value to get next
	 */
	public void addIntRange(String param, int base, int max, int step) {
		ParameterSetter init = new IntSteppedSetter(param, base, max, step);
		add(init);
	}

	/**
	 * Adds a sweep dimension for the specifed double parameter -- that is, for every run currently defined for this sweeper,
	 * runs will now be made for each step from base to max.
	 * (Conveience method.)
	 *
	 * @param param parameter name
	 * @param base  starting value
	 * @param max   ending value
	 * @param step  added to current value to get next
	 */
	public void addDoubleRange(String param, double base, double max, double step) {
		ParameterSetter init = new DoubleSteppedSetter(param, base, max, step);
		add(init);
	}

	/**
	 * Sets the number of time that the sweeper should execute a run for each unique parameter combination.
	 * <i>Note: this method only manages the default run initialzer that will be placed just below root.
	 * If a {@link RunParameterSetter} is also added elsewhere, that intializer will also create new runs.
	 * Also, if new runs are added directly to root (e.g., not using conveinience methods) those runs may not be subject to
	 * this count.
	 *
	 * @param count number of runs to execute for each combination of paramaters
	 */
	public void setRunCount(int count) {
		RunParameterSetter tmp = new RunParameterSetter(count);
		paramTree.replaceNode(runSetter, tmp);
		runSetter = tmp;
	}

	public int getRunCount() {
		return runSetter.getTotalRuns();
	}

	/**
	 * Retrieves the root {@link ParameterSetter}, that all other setters are a child of.
	 *
	 * @return the root {@link ParameterSetter}
	 */
	public ParameterSetter getRootParameterSetter() {
		// we return the run setter because we want all other
		// setters to be added as children of the runSetter.
		return runSetter;
	}

	/**
	 * Retrieves the {@link ParameterSetter}s that are direct children of the specified
	 * setter.
	 *
	 * @param parentSetter the setter whose children to fetch
	 * @return the direct descendants of the specified setter
	 */
	public Collection<ParameterSetter> getChildren(ParameterSetter parentSetter) {
		return paramTree.getChildren(parentSetter);
	}

	public String toStringChildren(ParameterSetter setter) {
		String res = setter + " {";
		Collection<ParameterSetter> children = paramTree.getChildren(setter);
		for (Iterator<ParameterSetter> iterator = children.iterator(); iterator.hasNext();) {
			ParameterSetter childInit = iterator.next();
			res += toStringChildren(childInit);
			if (iterator.hasNext()) {
				res += ",";
			}
		}
		return res + "}";
	}

	public String toString() {
		return toStringChildren(rootSetter);
	}
}
