/**
 * 
 */
package repast.simphony.conversion;

import java.io.File;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Nick Collier
 */
public class ScoreToCM {
  
  public void run(File scoreFile) throws TransformerException {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    InputStream in = getClass().getResourceAsStream("ScoreToContext.xsl");
    Transformer transformer = tFactory.newTransformer(new StreamSource(in));
    File out = new File(scoreFile.getParentFile(), "context.xml");
    transformer.transform(new StreamSource(scoreFile), new StreamResult(out));
  }
}
