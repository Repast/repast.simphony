package repast.simphony.visualization.visualization3D;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import repast.simphony.visualization.Layout;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * @author Nick Collier
 * 
 */
public class VisualItem3D<T> {

	protected BranchGroup branchGroup;
	protected BranchGroup oldUserBranch, newUserBranch;
	protected TaggedBranchGroup taggedBranchGroup;
	protected Color3f color = new Color3f();
	protected Point3f location = new Point3f();
	protected float[] scale;
	protected TransformGroup translateGroup, rotationGroup, scaleGroup;
	protected Transformer transformer = new Transformer();
	protected T visualizedObject;
	protected boolean moved = true;
	protected Map<Shape3D, TaggedAppearance> shapes = new HashMap<Shape3D, TaggedAppearance>();
	// stores the changed appearance of a shape.
	protected Map<Shape3D, TaggedAppearance> changedMap = new HashMap<Shape3D, TaggedAppearance>();
	protected Label label;

	public VisualItem3D(TaggedBranchGroup tGroup, T obj, Label label) {
		this.label = label;
		this.taggedBranchGroup = tGroup;
		visualizedObject = obj;

		translateGroup = createTransformGroup();
		rotationGroup = createTransformGroup();
		scaleGroup = createTransformGroup();
		translateGroup.addChild(rotationGroup);
		rotationGroup.addChild(scaleGroup);

		BranchGroup intermediateGroup = new BranchGroup();
		intermediateGroup.setCapability(BranchGroup.ALLOW_DETACH);
		intermediateGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		intermediateGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		intermediateGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		intermediateGroup.addChild(taggedBranchGroup.getBranchGroup());
		scaleGroup.addChild(intermediateGroup);

		branchGroup = new BranchGroup();
		branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		branchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		branchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		branchGroup.addChild(translateGroup);
		oldUserBranch = taggedBranchGroup.getBranchGroup();
		oldUserBranch.setCapability(BranchGroup.ALLOW_BOUNDS_READ);
		initShape(taggedBranchGroup.getBranchGroup());
	}

	private TransformGroup createTransformGroup() {
		TransformGroup transGroup = new TransformGroup();
		//translateGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		transGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroup.setCapability(TransformGroup.ALLOW_BOUNDS_READ);
		return transGroup;
	}

	public Iterator<Shape3D> shapes() {
		return shapes.keySet().iterator();
	}

	public TaggedAppearance getShapeAppearance(Shape3D shape) {
		return shapes.get(shape);
	}

	public void setShapeAppearance(Shape3D shape, TaggedAppearance appearance) {
		if (appearance != null) {
		changedMap.put(shape, appearance);
		shapes.put(shape, appearance);
	}
	}

	public TaggedBranchGroup getTaggedBranchGroup() {
		return taggedBranchGroup;
	}

	public void setTaggedBranchGroup(TaggedBranchGroup tGroup) {
		initShape(tGroup.getBranchGroup());
		oldUserBranch = taggedBranchGroup.getBranchGroup();
		newUserBranch = tGroup.getBranchGroup();
		newUserBranch.setCapability(BranchGroup.ALLOW_DETACH);
		newUserBranch.setCapability(BranchGroup.ALLOW_BOUNDS_READ);

		this.taggedBranchGroup = tGroup;
	}

	private void initShape(BranchGroup group) {
		// try to find the shape under the group and set the appearance
		// and user data
		shapes.clear();
		findShape(group);
	}

	private void findShape(Group group) {
		for (int i = 0, n = group.numChildren(); i < n; i++) {
			Node node = group.getChild(i);
			if (node instanceof Shape3D) {
				shapes.put((Shape3D) node, null);
			} else if (node instanceof Group) {
				findShape((Group) node);
			}
		}
	}

	public Point3f getLocation() {
		return location;
	}

	public void setLocation(Point3f location) {
		moved = !this.location.equals(location);
		if (moved) {
			this.location = location;
			transformer.setTranslation(this.location);
		}
	}

	public void setLocation(float[] location) {
		moved = !(location[0] == this.location.x && location[1] == this.location.y && location[2] == this.location.z);
		if (moved) {
			this.location.set(location);
			transformer.setTranslation(this.location);
		}
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setLocation() {
		transformer.setTranslation(this.location);
	}

	public void setScale(float x, float y, float z) {
		transformer.setScale(x, y, z);
	}
	
	 public float[] getScale() {
	    return scale;
	  }

	public void addTo(BranchGroup group) {
		branchGroup.compile();
		group.addChild(branchGroup);
	}

	public void applyTransform() {

		for (Map.Entry<Shape3D, TaggedAppearance> entry : changedMap.entrySet()) {
			if (entry.getValue().getAppearance() != null) {
			entry.getKey().setAppearance(entry.getValue().getAppearance());
		}
		}

		BranchGroup group = (BranchGroup) scaleGroup.getChild(0);
		if (newUserBranch != null) {
			newUserBranch.compile();
			group.removeChild(oldUserBranch);
			group.addChild(newUserBranch);
			oldUserBranch = newUserBranch;
			newUserBranch = null;
		}

		BranchGroup labelGroup = label.getBranchGroup(location, oldUserBranch);
		if (labelGroup != null) {
			labelGroup.compile();
			branchGroup.addChild(labelGroup);
		}

		changedMap.clear();
		// set moved to false here, because in the absence of
		// another layout update move will remain whatever it was
		// so we need to reset it.
		moved = false;
		transformer.apply(translateGroup, rotationGroup, scaleGroup);
	}

	public void removeFrom(BranchGroup parent) {
		parent.removeChild(branchGroup);
	}

	public T getVisualizedObject() {
		return visualizedObject;
	}

	public void setRotation(AxisAngle4f rotation) {
		transformer.setRotation(rotation);
	}

	public void setLabel(String label, Style3D.LabelPosition position) {
		this.label.setLabel(label);
		this.label.setPosition(position);
	}

	public String getLabel() {
		return this.label.getLabel();
	}

	public Style3D.LabelPosition getLabelPosition() {
		return this.label.getPosition();
	}

	public void setLabelColor(Color color) {
		if (color != null) label.setColor(color);
	}

	public Color getLabelColor() {
		return label.getColor();
	}

	public void setLabelFont(Font font) {
		if (font != null) label.setFont(font);
	}

	public Font getLabelFont() {
		return label.getFont();
	}

	public void setLabelOffset(float offset) {
		label.setOffset(offset);
	}

	public void updateTaggedBranchGroup(Style3D style) {
		// set a new branch if necessary
		BranchGroup oldBg = taggedBranchGroup.getBranchGroup();
		TaggedBranchGroup newTaggedBranchGroup = style.getBranchGroup(visualizedObject, taggedBranchGroup);
		if (newTaggedBranchGroup != null) {
			if (oldBg == null || !newTaggedBranchGroup.getBranchGroup().equals(oldBg) || !newTaggedBranchGroup.equals(taggedBranchGroup))
			{
				setTaggedBranchGroup(newTaggedBranchGroup);
			}
		}
	}

	public void updateAppearance(Style3D style) {
		for (Map.Entry<Shape3D, TaggedAppearance> entry : shapes.entrySet()) {
			Shape3D shape = entry.getKey();
			TaggedAppearance oldAppearance = entry.getValue();
			TaggedAppearance appearance = style.getAppearance(visualizedObject, oldAppearance, shape.getUserData());
			if (appearance != null && !appearance.equals(oldAppearance)) {
				setShapeAppearance(shape, appearance);
			}
		}
	}

	public void updateLabel(Style3D style) {
		String label = style.getLabel(visualizedObject, getLabel());
		
		// If the label is an empty string, set to NULL for faster rendering.
		if (label != null && label.length() == 0)
			label = null;
		
		setLabel(label, style.getLabelPosition(visualizedObject, getLabelPosition()));
		
		if (label != null && label.length() > 0) {
			setLabelColor(style.getLabelColor(visualizedObject, getLabelColor()));
			setLabelFont(style.getLabelFont(visualizedObject, getLabelFont()));
			setLabelOffset(style.getLabelOffset(visualizedObject));
		}
	}

	public void updateScale(Style3D style) {
	  scale = style.getScale(visualizedObject);
		if (scale != null) setScale(scale[0], scale[1], scale[2]);
	}
	
	public void updateRotation(Style3D style) {
		float[] point = style.getRotation(visualizedObject);
		if (point != null) setRotation(new AxisAngle4f(point));
	}

	// implemented by subclass
	public void updateLocation(Layout layout) {
	}

	// implemented by subclass
	public void updateLocation(Point3f source, float sourceRadius, Point3f target, float targetRadius) {
	}
}
