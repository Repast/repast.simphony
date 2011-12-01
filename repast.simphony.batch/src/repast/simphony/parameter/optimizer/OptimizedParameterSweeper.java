/*CopyrightHere*/
package repast.simphony.parameter.optimizer;

import java.util.LinkedList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.controller.NullAbstractControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.graph.EngineGraphUtilities;
import repast.simphony.engine.graph.Executor;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParameterTreeSweeper;
import repast.simphony.parameter.RunResultProducer;

/**
 * A {@link repast.simphony.parameter.ParameterSweeper} that attempts to traverse the space in an
 * optimized manner. This does not necessarily traverse the entire parameter space, but will be lead
 * through it's traversal by a traversal algorithm (a
 * {@link repast.simphony.parameter.optimizer.AdvancementChooser}) which relies on a
 * {@link repast.simphony.parameter.RunResultProducer} to produce a single value for a run. This value
 * is the value that is optimized through the search.
 * 
 * @author Jerry Vos
 */
public class OptimizedParameterSweeper extends ParameterTreeSweeper {
	protected RunResultProducer resultProducer;

	protected AdvancementChooser advancementChooser;

	protected boolean firstStepRandom = false;

	protected int index = 0;

	protected ParameterSetter[] flatTree;

	protected double runValue;

	protected AdvanceType lastAdvancement;

	protected boolean finished;

	protected ParameterSetter lastSetter;

	protected double previousRunValue;
	
	protected int lastIndex;

	protected boolean resetIndex;

	/**
	 * This just goes FORWARD through the parameter space.  This will traverse the space along
	 * its edges until it reaches the farthest corner from the initial coordinates. 
	 */
	public static class DefaultAdvanceChooser implements AdvancementChooser {
		public AdvanceType advanceParameter(double runResult) {
			return AdvanceType.FORWARD;
		}

		public boolean shouldRevert(double runResult) {
			return false;
		}

		public AdvanceType chooseAdvancement(ParameterSetter init, AdvanceType lastType,
				double runResult) {
			return AdvanceType.FORWARD;
		}
	};

	public OptimizedParameterSweeper(ControllerRegistry registry, Object masterContextId) {
		this(registry, masterContextId, new DefaultAdvanceChooser());
	}

	public OptimizedParameterSweeper(ControllerRegistry registry, Object masterContextId,
			DefaultAdvanceChooser chooser) {
		this.advancementChooser = chooser;

		resetValues();

		addResultProducerAction(registry, masterContextId);
	}

	protected void addResultProducerAction(ControllerRegistry registry, Object masterContextId) {
		registry.addAction(masterContextId, null, new NullAbstractControllerAction() {

			@Override
			public void runCleanup(RunState runState, Context context) {
				previousRunValue = runValue;
				runValue = resultProducer.getRunValue(runState);
			}
		});
	}

	protected void flattenTree() {
		final List<ParameterSetter> flatTree = new LinkedList<ParameterSetter>();

		EngineGraphUtilities.breadthFirstMap(new Executor<ParameterSetter>() {
			public void execute(ParameterSetter toExecuteOn) {
				flatTree.add(toExecuteOn);
			}
		}, traverser, rootSetter);

		this.flatTree = flatTree.toArray(new ParameterSetter[flatTree.size()]);
		index = this.flatTree.length - 1;
	}

	@Override
	public boolean atEnd() {
		return finished;
	}

	@Override
	protected Parameters nextParameters(final Parameters params) {
		if (flatTree == null) {
			flattenTree();
		}
		OptimizableParameterSetter advancedInit = null;

		if (lastSetter instanceof OptimizableParameterSetter) {
			advancedInit = (OptimizableParameterSetter) lastSetter;
		}
//		System.out.println("a");
		if (advancementChooser.shouldRevert(runValue)) {
			// need to revert if we can
			if (lastAdvancement == AdvanceType.RANDOM) {
				EngineGraphUtilities.breadthFirstMap(new Executor<ParameterSetter>() {
					public void execute(ParameterSetter toExecuteOn) {
						if (toExecuteOn instanceof OptimizableParameterSetter) {
							OptimizableParameterSetter advancedInit = (OptimizableParameterSetter) toExecuteOn;
							advancedInit.revert(params);
						}
					}
				}, traverser, rootSetter);
			} else {
				if (advancedInit != null) {
					advancedInit.revert(params);
				}

			}
			// we reverted, so we don't want to go through the whole tree again
			if (resetIndex) {
				index = lastIndex;
			}

			runValue = previousRunValue;
		}
		resetIndex = false;

		boolean usedSetter = false;
		while (!usedSetter) {
			if (index < 1) {
				finished = true;
				break;
			}
			lastSetter = flatTree[index];
			lastIndex = index;
			advancedInit = null;

			if (lastSetter instanceof OptimizableParameterSetter) {
				advancedInit = (OptimizableParameterSetter) lastSetter;
			}

			AdvanceType newAdvancement = advancementChooser.chooseAdvancement(lastSetter,
					lastAdvancement, runValue);
			lastAdvancement = newAdvancement;

			switch (newAdvancement) {
			case SWITCH:
//				System.out.println("switched to " + flatTree[index - 1]);
				index--;
				break;
			case FORWARD:
//				System.out.println("forward " + flatTree[index]);
				if (lastSetter.atEnd()) {
					index--;
				} else {
					lastSetter.next(params);
					updateChildren(lastSetter, params);

					usedSetter = true;
				}
				break;
			case BACKWARD:
//				System.out.println("backward " + flatTree[index]);
				if (advancedInit == null || advancedInit.atBeginning()) {
					index--;
				} else {
					advancedInit.previous(params);

					updateChildren(lastSetter, params);
					usedSetter = true;
				}
				break;
			case RANDOM:
				// TODO: from the way this is implemented it may be possible for
				// nothing to actually get executed (if it can't find an
				// OptimizableParameterSetter
				executeRandom(rootSetter, false, params);
				// TODO: what should this be set to?
				resetIndex();
				usedSetter = true;
				break;
			}
		}

		return params;
	}

	private void updateChildren(ParameterSetter init, Parameters params) {
		if (paramTree.getChildren(init).size() > 0) {
			resetIndex();
		}
	}

	private void resetIndex() {
		resetIndex = true;
		index = flatTree.length - 1;
	}

	private Parameters executeRandom(ParameterSetter setter, final boolean executeAll, final Parameters params) {
		// this could be overridden or replaced to just advance the given setter
		EngineGraphUtilities.breadthFirstMap(new Executor<ParameterSetter>() {
			public void execute(ParameterSetter toExecuteOn) {
				if (toExecuteOn instanceof OptimizableParameterSetter) {
					OptimizableParameterSetter advancedSetter = (OptimizableParameterSetter) toExecuteOn;
					advancedSetter.random(params);
				} else if (executeAll) {
					toExecuteOn.next(params);
				}
			}
		}, traverser, setter);

		return params;
	}

	@Override
	protected Parameters firstParameters(Parameters params) {
		if (!firstStepRandom) {
			// this starts us at the origin (potentially)
			return super.firstParameters(params);
		} else {
			return executeRandom(rootSetter, true, params);
		}
	}

	protected void resetValues() {
		this.runValue = Double.NEGATIVE_INFINITY;
		this.lastAdvancement = null;
		// this.previousValues = new DefaultParameters();
		// this.previousValues.setValidating(false);
		this.finished = false;
		this.flatTree = null;
	}

	@Override
	public void reset(Parameters params) {
		resetValues();
		super.reset(params);
	}

	public AdvancementChooser getAdvancementChooser() {
		return advancementChooser;
	}

	public void setAdvancementChooser(AdvancementChooser chooser) {
		this.advancementChooser = chooser;
	}

	public boolean isFirstStepRandom() {
		return firstStepRandom;
	}

	public void setFirstStepRandom(boolean firstStepRandom) {
		this.firstStepRandom = firstStepRandom;
	}

	public RunResultProducer getResultProducer() {
		return resultProducer;
	}

	public void setResultProducer(RunResultProducer resultProducer) {
		this.resultProducer = resultProducer;
	}
}
