package repast.simphony.visualization.engine;

import java.util.List;
import java.util.Set;

import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleData;
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
	
	/**
	 * Get a factory for creation projection discritprs used by displays.
	 * 
	 * @return
	 */
	public ProjectionDescriptorFactory getProjectionDescriptorFactory();
	
	/**
	 * Return an optional (can be null or emptystring) name of a class that is 
	 *   required for this visualization type to work propely.  This is useful if
	 *   the visualization is dependent on a third party library like JOGL. The
	 *   DisplayControllerAction will check if this required class name is on the
	 *   classpath and will not try to create the display if it is missing.
	 * 
	 * @return
	 */
	public String getRequiredLibraryClassName();
	
	/**
	 * Returns true if this visualization is able to handle the projection type.
	 * 
	 * @param projectionType
	 * @return
	 */
	public boolean handlesProjectionType(String projectionType);
	
	/**
	 * Get a DisplayValidator instance associated with this display type.
	 * 
	 * @return
	 */
	public DisplayValidator getDisplayValidator();
	
	public Class<? extends EditedStyleData> getDefaultEditedStyleDataClass();
	
	public Class<? extends EditedEdgeStyleData> getDefaultEditedEdgeStyleDataClass();
	
	public Class<?> getEditedStyleClass(EditedStyleData editedStyleData);
	
	public Class<?> getEditedEdgeStyleClass();
	
	public Set<String> getAllowedShapes();
}