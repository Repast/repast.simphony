/*CopyrightHere*/
package repast.simphony.parameter;


/**
 * A {@link repast.simphony.parameter.ParameterSetter} that signifies a set of nested parameters being
 * executed multiple times. This will run its nested initializers the number of times specified in
 * its totalRuns argument.
 * 
 * @author Jerry Vos
 */
public class RunParameterSetter implements ParameterSetter {
    protected int runNumber;

    protected int totalRuns;

    /**
     * The number of times this and its nested parameters should be run.
     *
     * @param totalRuns
     *            number of times to run through the parameters
     */
    public RunParameterSetter(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    /**
     * Resets the run count to 0.
     */
    public void reset(Parameters params) {
        this.runNumber = 1;
    }

    /**
     * True if we've run totalRuns times.
     *
     * @return if we've ran enough times
     */
    public boolean atEnd() {
        return runNumber >= totalRuns;
    }

    /**
     * Increments the run number and calls the super's next method.
     *
     * @param params
     *            unused but passed to the super
     */
    public void next(Parameters params) {
        runNumber++;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public String toString() {
        return "[runs " + totalRuns + "]";
    }
}
