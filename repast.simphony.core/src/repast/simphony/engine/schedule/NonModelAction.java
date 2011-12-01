/*CopyrightHere*/
package repast.simphony.engine.schedule;

import java.lang.annotation.*;


/**
 * This is an annotation for {@link repast.simphony.engine.schedule.IAction}s that marks them as not being
 * related to the model's execution, but being related to the back-end of the simulation. Things
 * like logging and display updating and so forth should be marked with this identifier so that they
 * are not accounted for when determining if the model can finish up or not.<p/>
 * 
 * @author Jerry Vos
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target( { ElementType.TYPE })
public @interface NonModelAction {

}
