/**
 * 
 */
package repast.simphony.visualizationOGL2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import saf.v3d.ShapeFactory2D;

/**
 * Registers spatials from an SVG spatial source. If the svg contains keyword
 * metadata of "simple" then, this will attempt to create geometry and color
 * info from the svg, otherwise it will render the svg as an image.
 * 
 * @author Nick Collier
 */
public class SVGSpatialSource implements SpatialSource {

  private static final String SIMPLE = "simple";
  private String id, path;
  protected boolean simple = false;

  public SVGSpatialSource(String id, String path) throws IOException {
    this.id = id;
    this.path = path;
    parseProps(path);
  }

  protected void parseProps(String path) throws IOException {
    String parser = XMLResourceDescriptor.getXMLParserClassName();
    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
    String uri = new File(path).toURI().toString();
    SVGOMDocument doc = (SVGOMDocument) f.createDocument(uri);

    NodeList nodes = doc.getElementsByTagName("dc:subject");
    if (nodes.getLength() > 0) {
      nodes = ((Element) nodes.item(0)).getElementsByTagName("rdf:li");
      for (int i = 0; i < nodes.getLength(); ++i) {
        if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
          Element item = (Element) nodes.item(i);
          parseProperty(item.getTextContent().trim());
        }
      }
    }
  }

  private void parseProperty(String text) throws IOException {
    if (text.equals(SIMPLE))
      simple = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#getName()
   */
  public String getID() {
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#registerSource()
   */
  public void registerSource(ShapeFactory2D shapeFactory, Map<String, String> props)
      throws IOException {
    if (simple) {
      registerShape(shapeFactory, props);
    } else {
      registerImage(shapeFactory, props);
    }
  }

  private void registerShape(ShapeFactory2D shapeFactory, Map<String, String> props)
      throws IOException {
    SVGToNamedShape shaper = new SVGToNamedShape(path, id, props);
    shaper.createShape(shapeFactory);
  }

  private void registerImage(ShapeFactory2D shapeFactory, Map<String, String> props)
      throws IOException {
    try {
      int width = -1;
      int height = -1;

      if (props.containsKey(KEY_BSQUARE_SIZE)) {
        int size = Integer.parseInt(props.get(KEY_BSQUARE_SIZE));
        // create a tmp image in order to get width and height
        BufferedImage img = SVGToBufferedImage.createImage(path, -1, -1);
        float imgWidth = img.getWidth();
        float imgHeight = img.getHeight();
        img = null;
        
        if (imgWidth > imgHeight) {
          width = size;
          float scale = size / imgWidth;
          height = (int)(imgHeight * scale);
        } else {
          height = size;
          float scale = size / imgHeight;
          width = (int)(imgWidth * scale);
        }

      } else {
        if (props.containsKey(KEY_WIDTH)) {
          width = Integer.parseInt(props.get(KEY_WIDTH));
        }

        if (props.containsKey(KEY_HEIGHT)) {
          width = Integer.parseInt(props.get(KEY_HEIGHT));
        }
      }

      BufferedImage img = SVGToBufferedImage.createImage(path, width, height);

      float scale = 1;
      if (props.containsKey(KEY_SCALE)) {
        scale = Float.parseFloat(props.get(KEY_SCALE));
      }

      shapeFactory.registerImage(id, img, scale);

    } catch (TranscoderException ex) {
      throw new IOException(ex);
    } catch (NumberFormatException ex) {
      throw new IOException("Invalid bbox size, width, height or scale in properties", ex);
    }
  }

}
