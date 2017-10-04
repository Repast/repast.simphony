package repast.simphony.gis.visualization.engine;

import java.util.List;

import javax.swing.JOptionPane;

import repast.simphony.gis.engine.GeographyProjectionRegistryData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.DisplayValidator;

/**
 * Validates GIS displays against the provided list of projection types.
 * 
 * @author Eric Tatara
 *
 */
public class GISDisplayValidator implements DisplayValidator {

	@Override
	public boolean validateDisplay(List<String> selectedProjectionTypes) {

		int geoCount = 0;
		int geoCoverageCount = 0;
		int netCount = 0;
		int otherCount = 0;

		for (String projType : selectedProjectionTypes){

			if (projType.equals(GeographyProjectionRegistryData.NAME)){
				geoCount++;
			}
			else if (projType.equals(GeographyProjectionRegistryData.COVERAGE_NAME)){
				geoCoverageCount++;
			}
			else if (projType.equals(ProjectionData.NETWORK_TYPE)){
				netCount++;
			}			
			else{
				otherCount++;
			}
		}

		if (geoCount != 1) {
			JOptionPane.showMessageDialog(null, "A GIS display must contain a single Geography.",
					"Display Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		else if (geoCount <= 1 && (otherCount > 0) ) {
			JOptionPane.showMessageDialog(null,
					"GIS displays may only contain Geography projections.", "Display Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}