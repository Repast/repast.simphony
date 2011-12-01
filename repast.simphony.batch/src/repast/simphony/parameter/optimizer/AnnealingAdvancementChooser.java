/*CopyrightHere*/
package repast.simphony.parameter.optimizer;

import repast.simphony.parameter.ParameterSetter;
import repast.simphony.random.RandomHelper;

public class AnnealingAdvancementChooser extends HillClimber {
    public static final double DEFAULT_INITIAL_TEMP = 1000;

    protected CoolingSchedule coolingSchedule;

    protected double maxTemp;

    protected boolean jumped;

    protected AdvanceType preJumpAdvance;

    protected double preJumpRunResult;

    private boolean reverted;


    public AnnealingAdvancementChooser() {
        this(new DefaultExponentialCoolingSchedule(), DEFAULT_INITIAL_TEMP);
    }

    public AnnealingAdvancementChooser(CoolingSchedule schedule, double initialTemp) {
        this.coolingSchedule = schedule;
        this.maxTemp = initialTemp;

        this.coolingSchedule.init(initialTemp);

        this.preJumpAdvance = AdvanceType.FORWARD;
        this.jumped = false;
    }

    @Override
    public AdvanceType chooseAdvancement(ParameterSetter init, AdvanceType lastType, double runResult) {
        AdvanceType advance;

        if (!chooseRandom()) {
            if (jumped && reverted) {
                advance = super.chooseAdvancement(init, preJumpAdvance, preJumpRunResult);
                if (advance != AdvanceType.SWITCH) {
                    jumped = false;
                }
            } else {
                advance = super.chooseAdvancement(init, lastType, runResult);
                jumped = false;
            }
        } else {
//			if (lastType != AdvanceType.RANDOM) {
            preJumpRunResult = runResult;
            preJumpAdvance = lastType;
//			}
            jumped = true;
            advance = AdvanceType.RANDOM;
        }

        return advance;
    }

    @Override
    public boolean shouldRevert(double runResult) {
//		System.out.println(runResult);
        if (jumped) {
            if (runResult <= preJumpRunResult) {
                reverted = true;
            } else {
                // we jumped to a random location and it was better than the old one, so we
                // want to now do a local check which is initiated based on the lastSetter
                lastSetter = null;
                reverted = false;
            }
            return reverted;
        }
        // we don't care what revert is when we didn't jump
        return super.shouldRevert(runResult);
    }

    protected boolean chooseRandom() {
        if (getRandom() < coolingSchedule.cool()) {
            return true;
        } else {
            return false;
        }
    }

    protected double getRandom() {
        return RandomHelper.getUniform().nextDoubleFromTo(0, maxTemp);
    }
}
