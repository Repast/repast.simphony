/**
 * 
 */
package repast.simphony.relogo.styles;

import edu.umd.cs.piccolo.PNode;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import repast.simphony.relogo.ide.image.NLImage 
import repast.simphony.relogo.ide.image.NLImagePrimitive 
import com.thoughtworks.xstream.XStream;

public class TurtleShapeReader {

	List<NLImage> turtleShapeList

	public TurtleShapeReader(File file){
		XStream xstream = new XStream()
		xstream.setClassLoader(NLImage.getClassLoader())
		String xml = file.text
		turtleShapeList = (List)xstream.fromXML(xml)
	}

	public Map<String, TurtleShape> getReLogoTurtleShapeMap(){
		Map tsm = new HashMap<String,PNode>()
        for (NLImage nli in turtleShapeList) {
        	String shapeName = nli.getName()
        	List componentList = []
        	for (NLImagePrimitive imp in nli.getPrimitives()){
        		componentList << new TurtleShapeComponent(imp.reLogoShape(new Rectangle(new Dimension(300,300))),imp.isChangingColor(),imp.getColor())
        	}
        	TurtleShape ts = new TurtleShape(componentList,nli.isRotates())
        	tsm.put(shapeName, ts)
        }
		return tsm
	}
}