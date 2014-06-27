package repast.simphony.visualization.visualization2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.piccolo2d.PLayer;
import org.piccolo2d.nodes.PImage;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.visualization2D.style.ValueLayerStyle;

/**
 * @author Eric Tatara
 *
 *  @deprecated replaced by ogl 2D
 *
 * 
 */
public class ValueDisplayLayer2D extends PLayer{

	private ValueLayerStyle style;
	private PImage node;
	private int xDim, yDim;
	private int xOffset, yOffset;

	private int[] data;

	public void addDataLayer(ValueLayer layer){
		int[] dims = layer.getDimensions().toIntArray(null);
		int[] origin = layer.getDimensions().originToIntArray(null);
		if (dims.length > 3 || (dims.length == 3 && dims[2] != 0)) {
			// todo better error reporting
			throw new IllegalArgumentException("Data Layers with more than 2 " +
					"dimensions are unsupported");
		}
		xDim = Math.max(dims[0], xDim);
		yDim = Math.max(dims[1], yDim);
		xOffset = origin[0];
		yOffset = origin[1];
	}

	public ValueDisplayLayer2D(ValueLayerStyle style, RepastCanvas2D canvas){
		super.setPickable(true);
		this.style = style;
		canvas.addLayer(this);
	}

	public void setStyle(ValueLayerStyle style){
		this.style = style;
	}

	public void init(ValueLayer layer){
		addDataLayer(layer);

		float cellSize = style.getCellSize();

		BufferedImage bimage = new BufferedImage(xDim, yDim, 
				BufferedImage.TYPE_INT_ARGB);

		// create an white rectangle as the default image
		Graphics2D g2d = bimage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle2D.Float(0, 0, xDim*cellSize, yDim*cellSize));
		g2d.dispose();

		node = new PImage(bimage);
		node.setBounds(new Rectangle2D.Float(0, 0, xDim*cellSize, yDim*cellSize));
		node.translate(-cellSize / 2, -cellSize / 2);

		data = ((DataBufferInt)(bimage.getRaster().getDataBuffer())).getData();

		this.addChild(node);
	}

	public void applyUpdates(){
		int i = 0;
		Color color;
		for (int y=-yOffset; y<yDim-yOffset; y++){
			for (int x=-xOffset; x<xDim-xOffset; x++){  			 	
				color = (Color)style.getPaint(x,y);

				if (color != null)
					data[i] = color.getRGB();
				else{
					int a = 255                 << 24; // alpha
					int r = style.getRed(x,y)   << 16; // red
					int b = style.getGreen(x,y) << 8;  // green
					int g = style.getBlue(x,y);        // blue

					data[i] = a + r + g + b;
				}
				i++;
			}
		}  		
		repaint();
	}

	public PImage getNode() {
		return node;
	}

	public ValueLayerStyle getStyle() {
		return style;
	}
	
	public int[] convertPixelToLoc(Point2D point){
	  int[] loc = new int[2];
	  float cellSize = style.getCellSize();
	  
	  double offSet = cellSize / 2 ;
	  
	  loc[0] = ((int)Math.floor(((point.getX() + offSet) / cellSize))) - xOffset;
	  loc[1] = ((int)Math.floor(((point.getY() + offSet) / cellSize))) - yOffset;
	  	  
	  return loc;
	}
}