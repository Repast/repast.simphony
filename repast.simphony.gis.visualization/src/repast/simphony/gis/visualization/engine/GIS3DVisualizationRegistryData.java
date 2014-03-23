package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.engine.AbstractVisualizationRegistryData;
import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;
import repast.simphony.visualization.gis3D.style.DefaultSurfaceShapeStyle;
import repast.simphony.visualization.gis3D.style.StyleGIS;
import repast.simphony.visualization.gui.DisplayDescriptorFactory;
import repast.simphony.visualization.gui.DisplayWizardModel;
import repast.simphony.visualization.gui.GISStyleStep;

public class GIS3DVisualizationRegistryData extends AbstractVisualizationRegistryData {

	public static final String TYPE = "GIS3D";
	
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
}