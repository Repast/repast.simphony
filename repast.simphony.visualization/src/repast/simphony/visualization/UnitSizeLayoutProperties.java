package repast.simphony.visualization;

import java.util.Arrays;

/**
 * Properties for a layout that needs a "unit size".
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class UnitSizeLayoutProperties implements VisualizationProperties {


	public static final String UNIT_SIZE = "cellSize";
	public static final String UNIT_SIZE_LAYOUT_PROPERTIES_ID = UnitSizeLayoutProperties.class.getName();

	private static final String[] keys = {UnitSizeLayoutProperties.UNIT_SIZE,
					UnitSizeLayoutProperties.UNIT_SIZE_LAYOUT_PROPERTIES_ID};

	private float unitSize = .06f;

	public Object getProperty(String id) {
		if (id.equals(UnitSizeLayoutProperties.UNIT_SIZE))
			return getUnitSize();
		if (id.equals(UnitSizeLayoutProperties.UNIT_SIZE_LAYOUT_PROPERTIES_ID))
			return true;
		return null;
	}

	public float getUnitSize() {
		return unitSize;
	}

	public void setUnitSize(float unitSize) {
		this.unitSize = unitSize;
	}

	/**
	 * Gets an iterable over all the valid keys for these properties.
	 *
	 * @return an iterable over all the keys for these properties.
	 */
	public Iterable<String> getKeys() {
		return Arrays.asList(UnitSizeLayoutProperties.keys);
	}
}
