package repast.simphony.visualization.visualization3D;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class AppearanceFactory {

	// todo rename -- blech
	public enum PolygonDraw {
		FILL, LINE, POINT
  }

	private static Color3f tmpColor = new Color3f();

	public static Appearance setColoredAppearance(Appearance appearance, Color color) {
		if (appearance == null) {
			appearance = AppearanceFactory.createAppearance();
		}

		ColoringAttributes ca = new ColoringAttributes();
		ca.setCapability(ColoringAttributes.ALLOW_COLOR_READ);
		ca.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
		ca.setColor(new Color3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f));
		appearance.setColoringAttributes(ca);

		return appearance;
	}

	// 0 = opaque, 1 = transparent
	// mode is one TransparancyAttributes.NICEST etc.
	public static Appearance setTransparentAppearance(Appearance appearance, int mode, float transVal) {
		if (appearance == null) {
			appearance = AppearanceFactory.createAppearance();
		}

		TransparencyAttributes trans = new TransparencyAttributes(mode, transVal);
		appearance.setTransparencyAttributes(trans);

		return appearance;
	}

	public static Appearance createAppearance() {
		Appearance appearance = new Appearance();
		appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		appearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
		appearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
		appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
		appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);

		appearance.setPolygonAttributes(new PolygonAttributes());
		appearance.getPolygonAttributes().setCapability(PolygonAttributes.ALLOW_MODE_READ);
		appearance.getPolygonAttributes().setCapability(PolygonAttributes.ALLOW_MODE_WRITE);
		return appearance;
	}


	// returns true if the material's diffuse color is the
	// specified color.
	private static boolean materialColorEquals(Material mat, Color3f color) {
		// assumes the material has been set by this class, so we only need to
		// check for diffuse color difference.
		if (mat == null) return false;
		mat.getDiffuseColor(tmpColor);
		return color.equals(tmpColor);
	}

	/**
	 * Sets the material of the specified to appearance to the specified color.
	 * This will create a new Appearance if the specified appearance is null.
	 *
	 * @param appearance
	 * @param color the color to set the diffuse color of the material to.
	 * @return an appearance whose material is the specified color. If the specified
	 * appearance is not null, then it will be return having had its material's color
	 * set appropriately.
	 */
	public static Appearance setMaterialAppearance(Appearance appearance, Color color) {
		if (appearance == null) {
			appearance = AppearanceFactory.createAppearance();
		}

		Color3f colorf = new Color3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
		Material material = appearance.getMaterial();
		if (!materialColorEquals(material, colorf)) {
			if (material == null) {
				material = new Material();
				material.setCapability(Material.ALLOW_COMPONENT_READ);
				material.setCapability(Material.ALLOW_COMPONENT_WRITE);
				appearance.setMaterial(material);
			}
			material.setDiffuseColor(colorf);

		}
		return appearance;
	}

	public static Appearance setPolygonAppearance(Appearance appearance, PolygonDraw draw) {
		if (appearance == null) {
			appearance = AppearanceFactory.createAppearance();
		}

		PolygonAttributes pa = appearance.getPolygonAttributes();
		// todo change so that the enums produce the attribute int
		if (draw == PolygonDraw.FILL) pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		else if (draw == PolygonDraw.LINE) pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		else pa.setPolygonMode(PolygonAttributes.POLYGON_POINT);
		//appearance.setPolygonAttributes(pa);

		return appearance;
	}
}
