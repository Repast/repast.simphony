package repast.simphony.visualizationOGL2D;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.SVGCircleElementBridge;
import org.apache.batik.bridge.SVGEllipseElementBridge;
import org.apache.batik.bridge.SVGGElementBridge;
import org.apache.batik.bridge.SVGLineElementBridge;
import org.apache.batik.bridge.SVGPathElementBridge;
import org.apache.batik.bridge.SVGPolygonElementBridge;
import org.apache.batik.bridge.SVGRectElementBridge;
import org.apache.batik.bridge.SVGShapeElementBridge;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.gvt.CompositeShapePainter;
import org.apache.batik.gvt.FillShapePainter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.apache.batik.gvt.StrokeShapePainter;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGSVGElement;

import saf.v3d.NamedShapeCreator;
import saf.v3d.ShapeFactory2D;

public class SVGToNamedShape {

  private static final String COLOR_FIXED = "fixedColor";

  private String path, name;
  private Map<String, String> props;

  static class ShapeData {
    boolean isLine;
    Color color;

    ShapeData(Color color, boolean isLine) {
      this.color = color;
      this.isLine = isLine;
    }
  }

  public SVGToNamedShape(String path, String name) {
    this(path, name, null);
  }

  public SVGToNamedShape(String path, String name, Map<String, String> props) {
    this.path = path;
    this.name = name;
    this.props = props;
  }

  private SVGOMDocument createDocument() throws IOException {
    String parser = XMLResourceDescriptor.getXMLParserClassName();
    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
    String uri = new File(path).toURI().toString();
    return (SVGOMDocument) f.createDocument(uri);
  }

  private ShapeData getShapeData(ShapePainter shapePainter) {
    if (shapePainter instanceof CompositeShapePainter) {
      CompositeShapePainter painter = (CompositeShapePainter) shapePainter;
      for (int j = 0; j < painter.getShapePainterCount(); j++) {
        ShapeData data = getShapeData(painter.getShapePainter(j));
        if (data != null)
          return data;
      }
    } else if (shapePainter instanceof FillShapePainter) {
      Paint paint = ((FillShapePainter) shapePainter).getPaint();
      // has a fill paint with a color, so is not a line
      if (paint != null)
        return new ShapeData((Color) paint, false);
    } else if (shapePainter instanceof StrokeShapePainter) {
      // has a stroke paint with a color, so is a line
      Paint paint = ((StrokeShapePainter) shapePainter).getPaint();
      return new ShapeData((Color) paint, true);
    }

    return null;
  }

  public void createShape(ShapeFactory2D shapeFactory) throws IOException {

    SVGOMDocument doc = createDocument();
    GVTBuilder builder = new GVTBuilder();
    BridgeContext ctx = createContext(doc);
    // this is necessary to generate the style info for some reason.
    builder.build(ctx, doc);

    NamedShapeCreator creator = shapeFactory.createNamedShape(name);

    SVGSVGElement root = doc.getRootElement();
    List<GeneralPath> shapes = new ArrayList<GeneralPath>();
    Rectangle2D.Float rect = new Rectangle2D.Float();
    processNode(root, creator, ctx, shapes, rect);
    if (props != null)
      transformShapes(shapes, rect);

    creator.registerShape();
  }

  private void transformShapes(List<GeneralPath> shapes, Rectangle2D bounds) {
    float imgWidth = (float) bounds.getWidth();
    float imgHeight = (float) bounds.getHeight();

    float scaleX = 1f;
    float scaleY = 1f;
    
    if (props.containsKey(SpatialSource.KEY_BSQUARE_SIZE)) {
      int size = Integer.parseInt(props.get(SpatialSource.KEY_BSQUARE_SIZE));
      
      if (imgWidth > imgHeight) {
        scaleX = size / imgWidth;
        float height = imgHeight * scaleX;
        scaleY = height / imgHeight;
      } else {
        scaleY = size / imgHeight;
        float width = imgWidth * scaleY;
        scaleX = width / imgWidth;
      }

    } else {
      float width = imgWidth;
      float height = imgHeight;
      
      if (props.containsKey(SpatialSource.KEY_WIDTH)) {
        width = Integer.parseInt(props.get(SpatialSource.KEY_WIDTH));
      }

      if (props.containsKey(SpatialSource.KEY_HEIGHT)) {
        height = Integer.parseInt(props.get(SpatialSource.KEY_HEIGHT));
      }
      
      if (width != imgWidth) {
        scaleX = width / imgWidth;
        if (height != imgHeight) {
          scaleY = height /imgHeight;
        } else {
          scaleY = scaleX;
          height = (int) (imgHeight * scaleY);
        }
      } else {
        if (height != imgHeight) {
          scaleX = scaleY = height / (float) imgHeight;
          width = (int) (imgWidth * scaleX);
        }
      }
    }

    float scale = 1;
    if (props.containsKey(SpatialSource.KEY_SCALE)) {
      scale = Float.parseFloat(props.get(SpatialSource.KEY_SCALE));
    }

    scaleX *= scale;
    scaleY *= scale;

    for (GeneralPath shape : shapes) {
      // -scaleY = scaleY * -1 which is necessary to flip Y axis
      shape.transform(AffineTransform.getScaleInstance(scaleX, -scaleY));
    }
  }

  private SVGShapeElementBridge lookupBridge(Element node) {
    // not sure if we need a new one of these each time
    // but will do it anyway.
    String name = node.getLocalName();
    if (name.equals("path"))
      return new SVGPathElementBridge();

    else if (name.equals("rect"))
      return new SVGRectElementBridge();

    else if (name.equals("circle"))
      return new SVGCircleElementBridge();

    else if (name.equals("ellipse"))
      return new SVGEllipseElementBridge();

    else if (name.equals("polygon"))
      return new SVGPolygonElementBridge();

    else if (name.equals("line"))
      return new SVGLineElementBridge();

    return null;

  }

  private boolean isColorFixed(Element node) {
    NodeList nodes = node.getElementsByTagName("desc");
    if (nodes.getLength() > 0) {
      return ((Element) nodes.item(0)).getTextContent().equals(COLOR_FIXED);
    }

    nodes = node.getElementsByTagName("svg:desc");
    if (nodes.getLength() > 0) {
      return ((Element) nodes.item(0)).getTextContent().equals(COLOR_FIXED);
    }

    return false;
  }

  private void accumulateTransforms(Node node, List<AffineTransform> trans, BridgeContext ctx) {
    if (node != null) {
      if (node.getLocalName().equals("g")) {
        SVGGElementBridge bridge = new SVGGElementBridge();
        GraphicsNode gNode = bridge.createGraphicsNode(ctx, (Element) node);
        trans.add(gNode.getTransform());
        accumulateTransforms(node.getParentNode(), trans, ctx);
      }
    }
  }

  private void processNode(Element node, NamedShapeCreator creator, BridgeContext ctx,
      List<GeneralPath> shapes, Rectangle2D rect) {
    SVGShapeElementBridge bridge = lookupBridge(node);
    if (bridge != null) {
      ShapeNode shapeNode = (ShapeNode) bridge.createGraphicsNode(ctx, node);
      bridge.buildGraphicsNode(ctx, node, shapeNode);
      GeneralPath shape = new GeneralPath(shapeNode.getShape());
      List<AffineTransform> transforms = new LinkedList<AffineTransform>();
      transforms.add(shapeNode.getTransform());
      accumulateTransforms(node.getParentNode(), transforms, ctx);
      for (AffineTransform trans : transforms) {
        shape.transform(trans);
      }
      ShapeData data = getShapeData(shapeNode.getShapePainter());
      if (data != null) {
        boolean canUpdateColor = !(isColorFixed(node));
        if (data.isLine)
          creator.addLine(shape, data.color, canUpdateColor);
        else
          creator.addShape(shape, data.color, canUpdateColor);
      }

      shapes.add(shape);
      if (rect.getWidth() == 0 && rect.getHeight() == 0) {
        rect.setRect(shape.getBounds());
        // System.out.printf("shape node bounds: %s%n", shapeNode.getBounds());
        // System.out.printf("shape bounds: %s%n", shape.getBounds());
      } else {
        // System.out.printf("shape node bounds: %s%n", shapeNode.getBounds());
        // System.out.printf("shape bounds: %s%n", shape.getBounds());
        Rectangle2D.union(rect, shape.getBounds(), rect);
      }
      // System.out.printf("rect bounds: %s%n%n", rect);
    }

    for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode
        .getNextSibling()) {
      if (childNode.getNodeType() == Node.ELEMENT_NODE)
        processNode((Element) childNode, creator, ctx, shapes, rect);
    }
  }

  private BridgeContext createContext(SVGOMDocument doc) {
    if (doc.isSVG12())
      return new SVG12BridgeContext(new UserAgentAdapter());
    else
      return new BridgeContext(new UserAgentAdapter());
  }

}
