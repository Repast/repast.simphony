package repast.simphony.gis.visualization.engine;

import java.util.List;

import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.gis.engine.GeographyProjectionRegistryData;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.DisplayCreatorFactory;
import repast.simphony.visualization.engine.DisplayValidator;
import repast.simphony.visualization.engine.ProjectionDescriptorFactory;
import repast.simphony.visualization.engine.VisualizationRegistryData;
import repast.simphony.visualization.gui.DisplayDescriptorFactory;

/**
 * 
 * @author Eric Tatara
 *
 * @deprecated Use 3D GIS display instead
 */
public class GISVisualizationRegistryData implements VisualizationRegistryData {

	public static final String TYPE = "GIS";
	
	public GISVisualizationRegistryData() {

	}
	
	@Override
	public String getVisualizationType() {
		return TYPE;
	}

	@Override
	public DisplayCreatorFactory getDisplayCreatorFactory() {
		return new DisplayCreatorFactoryGIS();
	}

	@Override
	public Class<?>[] getDefaultStyles() {
		return null;  // 2D GIS uses SLD not style classes
	}

	@Override
	public Class<?> getStyleInterface() {
		return null;  // 2D GIS uses SLD not style classes
	}

	@Override
	public Class<?>[] getDefaultEdgeStyles() {
		return null;  // 2D GIS uses SLD not style classes
	}

	@Override
	public Class<?> getEdgeStyleInterface() {
		return null;  // 2D GIS uses SLD not style classes
	}

	@Override
	public List<Pair<WizardStep, Condition>> getDisplayWizardSteps() {
	  return GISDisplayWizardStepCreator.getDisplayWizardSteps();
	}
	
	@Override
	public DisplayDescriptorFactory getDescriptorFactory() {
		return new GISDisplayDescriptorFactory();
	}

	@Override
	public ProjectionDescriptorFactory getProjectionDescriptorFactory() {
		return new GISProjectionDescriptorFactory();
	}

	@Override
	public String getRequiredLibraryClassName() {
		return "javax.media.opengl.glu.GLU";
	}

	@Override
	public boolean handlesProjectionType(String projectionType) {
		if (GeographyProjectionRegistryData.NAME.equals(projectionType)){
			return true;
		}
		return false;
	}

	@Override
	public DisplayValidator getDisplayValidator() {
		return new GISDisplayValidator();
	}
}