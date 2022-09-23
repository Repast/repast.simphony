package repast.simphony.gis.styleEditor;

import java.awt.Color;

import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;

/**
 * @author Nick Collier
 * 
 */
public class Palette {
	private Color[] colors;
	private String description = "";

	public Palette(Palette pal) {
		colors = new Color[pal.colors.length];
		System.arraycopy(pal.colors, 0, colors, 0, pal.colors.length);
		this.description = pal.description;
	}

	public Palette(Color[] colors, String description) {
		this.colors = colors;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColor(int index, Color color) {
		colors[index] = color;
	}

	public int getColorCount() {
		return colors.length;
	}

	public Color getColor(int index) {
		return colors[index];
	}

	public static Palette getDefaultPalette() {
		ColorBrewer brewer = new ColorBrewer();
		brewer.loadPalettes();
		BrewerPalette palette = brewer.getPalettes(ColorBrewer.QUALITATIVE)[0];
		return new Palette(palette.getColors(8), palette.getDescription());
	}
}
