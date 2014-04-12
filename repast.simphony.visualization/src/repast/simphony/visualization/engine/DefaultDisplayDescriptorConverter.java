package repast.simphony.visualization.engine;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.scenario.data.ProjectionData;

/**
 * Convert the Repast Simphony ver 2.1 DefaultDisplayDescriptor
 * 
 * @author Eric Tatara
 *
 */
public class DefaultDisplayDescriptorConverter{
	
	private static final String VER_2_1_TWO_D   = "TWO_D";
	private static final String VER_2_1_THREE_D = "THREE_D";
	private static final String VER_2_1_GIS     = "GIS";
	private static final String VER_2_1_GIS3D   = "GIS3D";
	
	private static final String VER_2_1_GRID   					= "GRID";
	private static final String VER_2_1_CONTNUOUS_SPACE = "CONTINUOUS_SPACE";
	private static final String VER_2_1_NETWORK     		= "NETWORK";
	private static final String VER_2_1_VALUE_LAYER   	= "VALUE_LAYER";
	private static final String VER_2_1_GEOGRAPHY   		= "GEOGRAPHY";
	
	private Map<String,String> displayTypeConversionMap;
	private Map<String,String> projectionTypeConversionMap;
	
	public DefaultDisplayDescriptorConverter(){
		displayTypeConversionMap = new HashMap<String,String>();
		projectionTypeConversionMap = new HashMap<String,String>();
		
		displayTypeConversionMap.put(VER_2_1_TWO_D, DisplayType.TWO_D);
		displayTypeConversionMap.put(VER_2_1_THREE_D, DisplayType.THREE_D);
		displayTypeConversionMap.put(VER_2_1_GIS, "GIS");
		displayTypeConversionMap.put(VER_2_1_GIS3D, "GIS 3D");
		
		projectionTypeConversionMap.put(VER_2_1_GRID, ProjectionData.GRID_TYPE);
		projectionTypeConversionMap.put(VER_2_1_CONTNUOUS_SPACE, ProjectionData.CONTINUOUS_SPACE_TYPE);
		projectionTypeConversionMap.put(VER_2_1_NETWORK, ProjectionData.NETWORK_TYPE);
		projectionTypeConversionMap.put(VER_2_1_VALUE_LAYER, ProjectionData.VALUE_LAYER_TYPE);
		projectionTypeConversionMap.put(VER_2_1_GEOGRAPHY, ProjectionData.GEOGRAPHY_TYPE);
		
	}
	
	public DisplayDescriptor convertDesriptor(DefaultDisplayDescriptor descriptor) {

		// Convert the display type string
		String displayType = displayTypeConversionMap.get(descriptor.getDisplayType());
		String displayName = descriptor.getName();
		
		// Need to first update the type in the old descriptor because it needs
		//  to be current for DisplayDescriptor.set(descriptor)
		descriptor.setDisplayType(displayType);
		
		if (DisplayType.TWO_D.equals(displayType) || DisplayType.THREE_D.equals(displayType)){
			CartesianDisplayDescriptor newDesc = new CartesianDisplayDescriptor(displayName);
			newDesc.set(descriptor);
			
			// Convert the projection data type string
			for (ProjectionData pd : newDesc.getProjections()){
				pd.setType(projectionTypeConversionMap.get(pd.getType()));
			}

			// Additional Cartesian descriptor steps
			for (String vlName : descriptor.getValueLayerNames()) {
				newDesc.addValueLayerName(vlName);
	    }

			newDesc.setValueLayerStyleName(descriptor.getValueLayerStyleName());

	    if (descriptor.getValueLayerEditedStyleName() != null)
	    	newDesc.setValueLayerEditedStyleName(descriptor.getValueLayerEditedStyleName());
			 
	    return newDesc;
		}
		else {
			for (VisualizationRegistryData data : VisualizationRegistry.getRegistryData()){
				DisplayDescriptor newDesc =	data.getDescriptorFactory().createDescriptor(displayName);
				newDesc.set(descriptor);
				
			// Additional Cartesian descriptor steps
				for (ProjectionData pd : newDesc.getProjections()){
					pd.setType(projectionTypeConversionMap.get(pd.getType()));
				}

				return newDesc;
			}
		}
		
		return null;
	}
}