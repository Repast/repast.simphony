/*CopyrightHere*/
package repast.simphony.parameter;

public abstract class SimpleParameterSetter implements ParameterSetter {
    public void reset(Parameters params) {
        // do nothing
    }

    public boolean canContinue() {
        return true;
    }

    public boolean atEnd() {
        return true;
    }

}
