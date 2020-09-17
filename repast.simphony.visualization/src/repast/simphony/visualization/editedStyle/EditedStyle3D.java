package repast.simphony.visualization.editedStyle;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Iterator;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.TextureAttributes;

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.Loader;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;

/**
 * 
 * @author Eric Tatara
 *
 */
public class EditedStyle3D implements Style3D<Object> {
	EditedStyleData<Object> innerStyle;
	Texture texture;
	BranchGroup modelGroup;

	public EditedStyle3D (String userStyleFile) {
		innerStyle = EditedStyleUtils.getStyle(userStyleFile);
		if (innerStyle == null)
			innerStyle = new DefaultEditedStyleData3D<Object>();

		String textureFile = innerStyle.getTextureFile3D();
		texture = null;
		if (textureFile != null){
			texture = new TextureLoader(textureFile, "RGB", new Container()).getTexture();
			texture.setBoundaryModeS(Texture.WRAP);
			texture.setBoundaryModeT(Texture.WRAP);
		}

		String modelFile = innerStyle.getModelFile3D();
		if (modelFile != null){
			Loader loader = ModelLoaderUtils.getLoaderForFile(modelFile);

			if (loader != null){
				modelGroup = new BranchGroup();
				try {
					Scene scene = loader.load(innerStyle.getModelFile3D());

					modelGroup = scene.getSceneGroup();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IncorrectFormatException e) {
					e.printStackTrace();
				} catch (ParsingErrorException e) {
					e.printStackTrace();
				}
			}
		}


	}

	public TaggedBranchGroup getBranchGroup(Object o, TaggedBranchGroup taggedGroup) {

		Shape3D shape = null;
		String wkt = innerStyle.getShapeWkt();
		float size = EditedStyleUtils.getSize(innerStyle, o);
		int primFlags  = Primitive.GENERATE_NORMALS;
		Appearance ap = null;

		if (taggedGroup == null || taggedGroup.getTag() == null){ 
			taggedGroup = new TaggedBranchGroup("DEFAULT");

			if (texture != null){
				TextureAttributes texAttr = new TextureAttributes();
				texAttr.setTextureMode(TextureAttributes.MODULATE);
				ap = new Appearance();
				ap.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
				ap.setCapability(Appearance.ALLOW_TEXTURE_READ);
				ap.setTexture(texture);
				ap.setTextureAttributes(texAttr);

				primFlags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

//				if (inward)
//				primflags = Primitive.GENERATE_NORMALS_INWARD + Primitive.GENERATE_TEXTURE_COORDS;
			}

			if (modelGroup != null){
				BranchGroup aGroup = new BranchGroup();
				// for (Enumeration e=modelGroup.getAllChildren(); e.hasMoreElements();){
				for (Iterator<Node> iter = modelGroup.getAllChildren(); iter.hasNext(); ) {
					Node node = (Node) iter.next();
					node.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
					aGroup.addChild(node.cloneNode(true));
				}
				taggedGroup.getBranchGroup().addChild(aGroup);

				return taggedGroup;
			}

			if ("sphere".equals(wkt))
				shape = ShapeFactory.createSphere(1.0f, 15, "DEFAULT", primFlags, ap);

			else if ("cube".equals(wkt))
				shape = ShapeFactory.createBox(1.0f, 1.0f, 1.0f, "DEFAULT", primFlags, ap);

			else if ("cylinder".equals(wkt))
				shape = ShapeFactory.createCylinder(1.0f, 2.0f, "DEFAULT", primFlags, ap);

			else if ("cone".equals(wkt))
				shape = ShapeFactory.createCone(1.0f, 2.0f, "DEFAULT", primFlags, ap);


			taggedGroup.getBranchGroup().addChild(shape);

			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(Object o) {
		return null;
	}

	public String getLabel(Object o, String currentLabel) {
		return EditedStyleUtils.getLabel(innerStyle, o);
	}

	public Color getLabelColor(Object t, Color currentColor) {
		float colorRGB[] = innerStyle.getLabelColor();

		return new Color(colorRGB[0],colorRGB[1],colorRGB[2]);
	}

	public Font getLabelFont(Object t, Font currentFont) {
		return new Font(innerStyle.getLabelFontFamily(), 
				innerStyle.getLabelFontType(),
				innerStyle.getLabelFontSize());
	}

	public LabelPosition getLabelPosition(Object o, Style3D.LabelPosition curentPosition) {

		// right
		if ("right".equals(innerStyle.getLabelPosition())){
			return Style3D.LabelPosition.WEST;
		}
		// left
		else if ("left".equals(innerStyle.getLabelPosition())){
			return Style3D.LabelPosition.EAST;
		}
		// top
		else if ("bottom".equals(innerStyle.getLabelPosition())){
			return Style3D.LabelPosition.SOUTH;
		}
		else
			return Style3D.LabelPosition.NORTH;
	}

	public float getLabelOffset(Object t) {
		return innerStyle.getLabelOffset();
	}

	public TaggedAppearance getAppearance(Object t, TaggedAppearance taggedAppearance, Object shapeID) {

		if (texture != null)
			return taggedAppearance;  // no appearance change with texture maps

		if (taggedAppearance == null || taggedAppearance.getTag() == null) 
			taggedAppearance = new TaggedAppearance("DEFAULT");

		Color color = EditedStyleUtils.getColor(innerStyle, t);
		
		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), color);
		return taggedAppearance;

	}

	public float[] getScale(Object o) {

		float size = EditedStyleUtils.getSize(innerStyle, o);

		return new float [] {size, size, size};
	
//	  return null;
	}
}
