package repast.simphony.gis.visualization.engine;

import java.util.List;
import java.util.Set;

import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.gis.engine.GeographyProjectionRegistryData;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.editedStyle.DefaultEditedEdgeStyleData2D;
import repast.simphony.visualization.editedStyle.DefaultEditedStyleData2D;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.engine.DisplayCreatorFactory;
import repast.simphony.visualization.engine.DisplayValidator;
import repast.simphony.visualization.engine.ProjectionDescriptorFactory;
import repast.simphony.visualization.engine.VisualizationRegistryData;
import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
import repast.simphony.visualization.gis3D.style.DefaultNetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.DefaultSurfaceShapeStyle;
import repast.simphony.visualization.gis3D.style.EditedMarkStyle;
import repast.simphony.visualization.gis3D.style.EditedNetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.EditedSurfaceShapeStyle;
import repast.simphony.visualization.gis3D.style.NetworkStyleGIS;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.gui.DisplayDescriptorFactory;
import repast.simphony.visualization.gui.styleBuilder.IconFactory2D;

public class GIS3DVisualizationRegistryData implements VisualizationRegistryData { 

	public static final String TYPE = "GIS 3D";
	
	public GIS3DVisualizationRegistryData(){
		
	}
	
	@Override
	public String getVisualizationType() {
		return TYPE;
	}

	@Override
	public DisplayCreatorFactory getDisplayCreatorFactory() {
		return new DisplayCreatorFactory3DGIS();
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
		
		return  new Class<?>[] {DefaultNetworkStyleGIS.class};
	}

	@Override
	public Class<?> getEdgeStyleInterface() {
		return NetworkStyleGIS.class;
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
		return new GISProjectionDescriptorFactory();
	}

	@Override
	public String getRequiredLibraryClassName() {
		return "com.jogamp.opengl.glu.GLU";
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

	// The GIS 3D display just re-uses the 2D edited style to support surface shapes
	@Override
	public Class<? extends EditedStyleData> getDefaultEditedStyleDataClass() {
		return DefaultEditedStyleData2D.class;
	}

	// TODO class ? extends
	@Override
	public Class<?> getEditedStyleClass(EditedStyleData editedStyledata) {
		
		String shape = editedStyledata.getShapeWkt();
		
		if (EditedSurfaceShapeStyle.LINE.equalsIgnoreCase(shape) || 
				EditedSurfaceShapeStyle.POLYGON.equalsIgnoreCase(shape))
		
			return EditedSurfaceShapeStyle.class;
			
		return EditedMarkStyle.class;
	}

	@Override
	public Set<String> getAllowedShapes() {
		
		// Allowed shapes in the GIS display are either the point marker shapes...
		Set<String> allowedShapes = IconFactory2D.Shape_List;
		
		// ...or line or polygon shapes.
		allowedShapes.add(EditedSurfaceShapeStyle.LINE);
		allowedShapes.add(EditedSurfaceShapeStyle.POLYGON);
		
		return allowedShapes;
	}

	@Override
	public Class<?> getEditedEdgeStyleClass() {
		return EditedNetworkStyleGIS.class;
	}

	@Override
	public Class<? extends EditedEdgeStyleData> getDefaultEditedEdgeStyleDataClass() {
		return DefaultEditedEdgeStyleData2D.class;
	}

}