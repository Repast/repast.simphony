/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import repast.simphony.ui.probe.ValueLayerProbeObject2D;
import repast.simphony.valueLayer.ValueLayer;
import saf.v3d.grid.GridMesh;
import saf.v3d.grid.VGridShape;
import saf.v3d.scene.VLayer;
import simphony.util.messages.MessageCenter;

/**
 * DisplayLayer for displaying value layers.
 * 
 * @author Nick Collier
 */
public class ValueLayerDisplayLayer {
  
  private static MessageCenter msg = MessageCenter.getMessageCenter(ValueLayerDisplayLayer.class);
  public static final String VALUE_LAYER_KEY = "ValueLayerDisplayLayer.ValueLayerKey";
  
  private ValueLayer valueLayer;
  private ValueLayerStyleOGL style;
  private VLayer layer;
  private int xDim, yDim;
  private int[] origin;
  private VGridShape shape;
  
  public ValueLayerDisplayLayer(ValueLayer valueLayer, ValueLayerStyleOGL style, VLayer layer) {
    this.layer = layer;
    resetLayer(valueLayer, style);
  }
  
  public void resetLayer(ValueLayer valueLayer, ValueLayerStyleOGL style) {
    if (shape != null) {
      layer.removeAllChildren();
    }
    
    this.valueLayer = valueLayer;
    this.style = style;
    this.style.init(this.valueLayer);
    
    int[] dims = valueLayer.getDimensions().toIntArray(null);
    origin = valueLayer.getDimensions().originToIntArray(null);
    if (dims.length > 3 || (dims.length == 3 && dims[2] != 0)) {
            // todo better error reporting
            msg.error("Error while creating display for value error", 
                new IllegalArgumentException("Value Layers with more than 2 dimensions are unsupported"));
            return;
    }
    
    xDim = dims[0];
    yDim = dims[1];
    
    // mesh is in rows x cols so ydim is first
    GridMesh grid = new GridMesh(yDim, xDim, style.getCellSize(), new ColorMapStyleAdapter(style, origin));
    shape = new VGridShape(grid, AbstractDisplayLayerOGL2D.MODEL_OBJECT_KEY);
    shape.putProperty(VALUE_LAYER_KEY, this);
    // offset half cell size so that object can appear in center of value layer cell
 // Commented this out to fix value layer display offset.
//    shape.translate(-style.getCellSize() / 2f, -style.getCellSize() / 2f, 0); 
    layer.addChild(shape);
  }
  
  public void update() {
    shape.update();
  }

  /**
   * Get the currently probed value layer.
   * 
   * @return the currently probed value layer.
   */
  Object getProbedObject() {
    int[] location = (int[])shape.getProperty(AbstractDisplayLayerOGL2D.MODEL_OBJECT_KEY);
    
    // origin is more like a translate for 0,0 so 
    // account for that
    if (location.length == 1) {
      location[0] -= origin[0];
    } else {
      location[0] -= origin[0];
      location[1] -= origin[1];
    }
    
    double[] loc = new double[location.length];
    for (int i = 0; i < location.length; i++) {
      loc[i] = location[i];
    }
    
    return new ValueLayerProbeObject2D(loc, valueLayer);
  }
}
