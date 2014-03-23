package repast.simphony.visualization.engine;

import java.util.List;

import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.gui.DisplayDescriptorFactory;


/**
 * Visualization registry data stores information about the available visualization
 *   types (displays) and their associated capabilities, such as converters, loaders, and
 *   GUI panels.  User-defined visualizations should implement registry
 *   data to provide these basic capabilities to the runtime.
 * 
 * @author Eric Tatara
 *
 * @param <T> 
 */
public interface VisualizationRegistryData {

	// TODO Projections: store the projections this viz can use.
	
	/**
	 * The visualization type Name, e.g. "2D", "3D", etc
	 * 
	 * @return the projection type name.
	 */
	public String getVisualizationType();
	
	/**
	 * Provide a DisplayCreator that will create the display.
	 * 
	 * @return the projection builder factory.
	 */
	public DisplayCreatorFactory getDisplayCreatorFactory();
	
	/**
	 * The default style classes that are available in the display wizard.
	 * 
	 * @return the style classes.
	 */
	public Class<?>[] getDefaultStyles();
	
	/**
	 * The default edge style classes that are available in the display wizard.
	 * 
	 * @return the edge style classes.
	 */
	public Class<?>[] getDefaultEdgeStyles();
	
	/**
	 * The style interface associated with the display.
	 * 
	 * @return the style interface.
	 */
	public Class<?> getStyleInterface();
	
	/**
	 * The edge style interface associated with the display.
	 * 
	 * @return the edge style interface.
	 */
	public Class<?> getEdgeStyleInterface();
	
	/**
	 * Gets display wizard steps (style, etc) specific to this display type.
	 * 
	 * @return
	 */
	public List<Pair<WizardStep, Condition>> getDisplayWizardSteps();
	
	/**
	 * Get a factory for creating display descriptors for this display type.
	 * 
	 * @return
	 */
	public DisplayDescriptorFactory getDescriptorFactory();
}