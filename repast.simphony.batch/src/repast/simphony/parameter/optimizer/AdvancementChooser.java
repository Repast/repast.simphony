/*CopyrightHere*/
package repast.simphony.parameter.optimizer;

import repast.simphony.parameter.ParameterSetter;

/**
 * This interface represents objects that will tell the
 * {@link repast.simphony.parameter.optimizer.OptimizedParameterSweeper} which way it should traverse the
 * parameter space.
 * 
 * @author Jerry Vos
 */
public interface AdvancementChooser {
    /**
     * Chooses which action the parameter sweeper should apply to the given setter.
     *
     * @param setter
     *            the setter that will be executed
     * @param lastType
     *            the last executed advancement (might not be what this {@link AdvancementChooser}
     *            last said
     * @param runResult
     *            the result of the last run (probably produced by a {@link repast.simphony.parameter.RunResultProducer}
     * @return which action the parameter sweeper should next apply
     */
    AdvanceType chooseAdvancement(ParameterSetter setter, AdvanceType lastType,
                                  double runResult);

    /**
     * If the sweeper should revert to the parameters used before the last ones. For instance, if we
     * executed 2 runs, first at (0, 0) and second at (1, 0) and the value of those runs were 100
     * and 50 respectively, it would most likely tell it to revert and go back to the (0, 0)
     * parameters (because the value was "better").
     *
     * @param runResult
     *            the result of the previous run
     * @return if the sweeper should revert or not
     */
    boolean shouldRevert(double runResult);
}