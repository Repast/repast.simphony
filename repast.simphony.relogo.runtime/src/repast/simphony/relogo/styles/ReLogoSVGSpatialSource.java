/**
 * 
 */
package repast.simphony.relogo.styles;

import java.io.File;
import java.io.IOException;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import repast.simphony.visualizationOGL2D.SVGSpatialSource;

/**
 * Registers spatials from an SVG spatial source. If the svg contains keyword
 * metadata of "simple" then, this will attempt to create geometry and color
 * info from the svg, otherwise it will render the svg as an image. 
 * 
 * @author Nick Collier
 */
public class ReLogoSVGSpatialSource extends SVGSpatialSource implements ReLogoSpatialSource{

  private static final String SIMPLE = "simple";
  private static final String ROTATE = "rotate";
  // space at end is intentional
  private static final String OFFSET = "offset ";

  private boolean rotate;
  private float offset;

  public ReLogoSVGSpatialSource(String id, String path) throws IOException {
    super(id, path);
//    parseProps(path);
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#doRotate()
   */
  public boolean doRotate() {
    return rotate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#getOffset()
   */
  public float getOffset() {
    return offset;
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualizationOGL2D.SpatialSource#isSimple()
   */
  public boolean isSimple() {
    return simple;
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
    else if (text.equals(ROTATE))
      rotate = true;
    else if (text.startsWith(OFFSET)) {
      try {
        offset = Float.parseFloat(text.substring(OFFSET.length()));
      } catch (NumberFormatException ex) {
        throw new IOException("Invalid offset");
      }
    }
  }
}
