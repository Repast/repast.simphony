package repast.simphony.gis.visualization.engine;

import java.util.List;

import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.gis.engine.GeographyProjectionRegistryData;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.AbstractVisualizationRegistryData;
import repast.simphony.visualization.engine.ProjectionDescriptorFactory;
import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
import repast.simphony.visualization.gis3D.style.DefaultSurfaceShapeStyle;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.gui.DisplayDescriptorFactory;

public class GIS3DVisualizationRegistryData extends AbstractVisualizationRegistryData {

	public static final String TYPE = "GIS 3D";
	
	public GIS3DVisualizationRegistryData() {
		super(TYPE, new DisplayCreatorFactory3DGIS());
	}

	@Override
	public Class<?>[] getDefaultStyles() {
		return  new Class<?>[] { DefaultMarkStyle.class, DefaultSurfaceShapeStyle.class };
	}

	@Override
	public Class<?> getStyleInterface() {
	  return StyleGIS.class;
	}

	@Override
	public Class<?>[] getDefaultEdgeStyles() {
		
		// TODO GIS: implement an edge style.
		return null;
	}

	@Override
	public Class<?> getEdgeStyleInterface() {
		
		// TODO GIS: implement an edge style interface.
		return null;
	}

	@Override
	public List<Pair<WizardStep, Condition>> getDisplayWizardSteps() {
		return GIS3DDisplayWizardStepCreator.getDisplayWizardSteps();
	}

	@Override
	public DisplayDescriptorFactory getDescriptorFactory() {
		return new GISDisplayDescriptorFactory();
	}

	@Override
	public ProjectionDescriptorFactory getProjectionDescriptorFactory() {
		return new GISProjectionDescritorFactory();
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
}