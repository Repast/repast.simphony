/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.visualization.LayoutUpdater;
import saf.v3d.AppearanceFactory;
import saf.v3d.scene.Label;
import saf.v3d.scene.TextureLayer;
import saf.v3d.scene.TextureRenderable;
import saf.v3d.scene.VLabelLayer;
import saf.v3d.scene.VLayer;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;

/**
 * OGL2D display layer that compbines a style with VSpatial objects.
 * 
 * @author Nick Collier
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StyledDisplayLayerOGL2D extends AbstractDisplayLayerOGL2D<StyleOGL2D> {

  private static class LabelEntry {
    public Font font;
    public Label label;
  }

  private TextureLayer textureLayer = new TextureLayer();

  private List<SpatialWrapper> textureToRemove = new ArrayList<SpatialWrapper>();
  private List<SpatialWrapper> textureToAdd = new ArrayList<SpatialWrapper>();
  private List<SpatialWrapper> layerToRemove = new ArrayList<SpatialWrapper>();
  private List<SpatialWrapper> layerToAdd = new ArrayList<SpatialWrapper>();

  public StyledDisplayLayerOGL2D(StyleOGL2D<?> style, VLayer layer) {
    super(style, layer);
    layer.getParent().addChild(textureLayer);
  }

  private VLabelLayer getLabelLayer(Font font, boolean antialias) {
    VLabelLayer labelLayer = labelLayers.get(font);
    if (labelLayer == null) {
      labelLayer = new VLabelLayer(font, antialias);
      labelLayers.put(font, labelLayer);
      layer.getParent().addChild(labelLayer);
    }
    return labelLayer;
  }

  private void applyStyle(Object obj, SpatialWrapper wrapper) {
    Color paint = style.getColor(obj);
    VSpatial item = wrapper.getChild();
    if (paint != null)
      item.setAppearance(AppearanceFactory.createColorAppearance(paint));
    item.scale(style.getScale(obj));
    item.rotate2D(style.getRotation(obj));
    int borderSize = style.getBorderSize(obj);
    // TODO borders on compound figures
    if (borderSize > 0 && item instanceof VShape) {
      ((VShape) item).setBorderStrokeSize(borderSize);
      ((VShape) item).setBorderColor(style.getBorderColor(obj));
    }

    LabelEntry entry = (LabelEntry) wrapper.getProperty(LABEL_KEY);
    String label = style.getLabel(obj);
    if (label != null && !label.equals("")) {
      Font font = style.getLabelFont(obj);
      if (font != null) {
        if (entry.label == null) {
          // create new label
          entry.label = new Label(label, wrapper);
          entry.font = font;
          VLabelLayer labelLayer = getLabelLayer(font, style.getLabelAntialiased());
          labelLayer.addLabel(entry.label);
        }

        entry.label.setColor(style.getLabelColor(obj));
        entry.label.setPosition(style.getLabelPosition(obj));
        entry.label.setText(label);
        entry.label.setOffset(style.getLabelXOffset(obj), style.getLabelYOffset(obj));

        if (!entry.font.equals(font)) {
          // font was updated so need to move it to a new label layer
          getLabelLayer(entry.font, true).removeLabel(entry.label);
          VLabelLayer labelLayer = getLabelLayer(font, style.getLabelAntialiased());
          labelLayer.addLabel(entry.label);
          entry.label.fontChanged();
          entry.font = font;

        }
      }
    } else if (entry.label != null) {
      // label is empty but entry.label exists
      labelLayers.get(entry.font).removeLabel(entry.label);
    }
  }

  /**
   * Updates existing VSpatials. The style may return a new VSpatial instance in
   * which case this returns that new instance and updates the objMap
   * appropriately. Otherwise, this returns null meaning the same VSpatial
   * instance was returned from the style.
   * 
   * @param obj
   * @param wrapper
   * @return A new VSpatial instance in which case this returns that new
   *         instance and updates the objMap appropriately. Otherwise, this
   *         returns null meaning the same VSpatial instance was returned from
   *         the style.
   */
  private void updateSpatial(Object obj, SpatialWrapper wrapper) {
    VSpatial spatial = wrapper.getChild();
    VSpatial newSpatial = style.getVSpatial(obj, spatial);
    // != comparison is intentional here
    if (newSpatial != null && newSpatial != spatial) {
      // System.out.printf("obj: %s, STR: %s, NSTR: %s%n", obj, (spatial
      // instanceof TextureRenderable), (newSpatial instanceof
      // TextureRenderable));
      newSpatial.translate(spatial.getLocalTranslation());
      newSpatial.scale(spatial.getLocalScale());
      if (spatial instanceof TextureRenderable) {
        textureToRemove.add(wrapper);
        if (newSpatial instanceof TextureRenderable) {
          textureToAdd.add(wrapper);
        } else {
          layerToAdd.add(wrapper);
        }
        wrapper.setNextChild(newSpatial);
      } else {
        if (newSpatial instanceof TextureRenderable) {
          layerToRemove.add(wrapper);
          textureToAdd.add(wrapper);
        }
        wrapper.setChild(newSpatial);
      }
    }
  }

  private SpatialWrapper createSpatial(Object obj) {
    SpatialWrapper wrapper = new SpatialWrapper(style.getVSpatial(obj, null));
    objMap.put(obj, wrapper);
    wrapper.putProperty(MODEL_OBJECT_KEY, obj);
    return wrapper;
  }

  /**
   * Removes the objects from the display that have been tagged for removal.
   */
  private void processRemoved() {
    for (Object obj : toBeRemoved) {
      SpatialWrapper item = (SpatialWrapper) objMap.remove(obj);
      if (item != null) {
        if (item.getChild() instanceof TextureRenderable)
          textureLayer.removeChild(item);
        else
          layer.removeChild(item);
        LabelEntry entry = (LabelEntry) item.getProperty(LABEL_KEY);
        if (entry.label != null) {
          getLabelLayer(entry.font, style.getLabelAntialiased()).removeLabel(entry.label);
        }
      }

    }
    toBeRemoved.clear();
  }

  private void updateExistingSpatials(LayoutUpdater updater) {
    // style, update the location of the existing nodes
    for (VSpatial item : layer.children()) {
      updateSpatial((SpatialWrapper) item, updater);
    }

    // style, update the location of the existing nodes
    for (VSpatial item : textureLayer.children()) {
      updateSpatial((SpatialWrapper) item, updater);
    }

    // order is important here -- remove must come before add
    for (SpatialWrapper item : textureToRemove) {
      textureLayer.removeChild(item);
      // removes from the textures with the old child data
      // then swaps to the new one
      item.updateChild();
    }

    for (VSpatial item : layerToRemove) {
      layer.removeChild(item);
    }

    for (VSpatial item : textureToAdd) {
      textureLayer.addChild(item);
    }

    for (VSpatial item : layerToAdd) {
      layer.addChild(item);
    }

    textureToAdd.clear();
    textureToRemove.clear();
    layerToAdd.clear();
    layerToRemove.clear();
  }

  private void updateSpatial(SpatialWrapper wrapper, LayoutUpdater updater) {
    Object obj = wrapper.getProperty(MODEL_OBJECT_KEY);
    updateSpatial(obj, wrapper);
    applyStyle(obj, wrapper);
    if (updater.getUpdateItemsLocation()) {
      float[] location = updater.getLayout().getLocation(obj);
      wrapper.getChild().translate(location[0], location[1], 0);
    }

  }

  /**
   * Adds the objects to the display that are tagged as added.
   */
  private void processAdded(LayoutUpdater updater) {
    // create VSpatials for the objs to add,
    // style, update the location of them.
    for (Object obj : toBeAdded) {
      SpatialWrapper wrapper = createSpatial(obj);
      wrapper.putProperty(LABEL_KEY, new LabelEntry());
      applyStyle(obj, wrapper);
      VSpatial item = wrapper.getChild();
      float[] location = updater.getLayout().getLocation(obj);
      item.translate(location[0], location[1], 0);
      if (item instanceof TextureRenderable)
        textureLayer.addChild(wrapper);
      else
        layer.addChild(wrapper);
    }
    toBeAdded.clear();
  }

  /**
   * Updates the displayed nodes by applying styles etc. The display is not
   * updated to reflect these changes.
   */
  public void update(LayoutUpdater updater) {
    // remove what needs to be removed
    processRemoved();
    updateExistingSpatials(updater);
    processAdded(updater);
  }
}
