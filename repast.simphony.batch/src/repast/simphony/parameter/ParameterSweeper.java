/*CopyrightHere*/

package repast.simphony.parameter;

import java.util.Collection;

/**
 * An interface representing an object that will perform a sweep of a parameter space represented by
 * a series of {@link repast.simphony.parameter.ParameterSetter}s.
 * 
 * @author Jerry Vos
 */
public interface ParameterSweeper extends ParameterSetter {

    /**
     * Adds the given {@link repast.simphony.parameter.ParameterSetter} to be executed.
     *
     * @param parent
     *            the parent setter the new setter is a child of
     * @param setter
     *            a parameter setter that will be executed
     */
    void add(ParameterSetter parent, ParameterSetter setter);

    /**
     * Removes the given {@link ParameterSetter} from those to be executed
     *
     * @param setter
     *            a parameter setter to remove
     */
    void remove(ParameterSetter setter);

    /**
     * Retrieves the root {@link ParameterSetter}, that all other setters are a child of.
     *
     * @return the root {@link ParameterSetter}
     */
    ParameterSetter getRootParameterSetter();

    /**
     * Retrieves the {@link ParameterSetter}s that are direct children of the specified
     * setter.
     *
     * @param parentSetter
     *            the setter whose children to fetch
     *
     * @return the direct descendants of the specified setter
     */
    Collection<ParameterSetter> getChildren(ParameterSetter parentSetter);
}