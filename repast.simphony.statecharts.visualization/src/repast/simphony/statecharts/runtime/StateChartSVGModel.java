package repast.simphony.statecharts.runtime;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

public class StateChartSVGModel {

	private SVGDocument svgDoc;
	private static final String SVG = "svg";
	private static final String FILL_COLOR = "green";
	private static final String FILL_OPACITY = "0.5";
	
	private Map<String,Element> activeElementsMap = new LinkedHashMap<String,Element>();
	private Set<String> activeUUIDs = new LinkedHashSet<String>();
	
	public void setActiveUUIDs(Set<String> activeUUIDs) {
		this.activeUUIDs = activeUUIDs;
	}

	public StateChartSVGModel(SVGDocument svgDoc) {
		SVGDocument svgDocLocal = deepCopySVGDocument(svgDoc);
		initializeModel(svgDocLocal);
	}

	private SVGDocument deepCopySVGDocument(SVGDocument svgDoc) {
		DOMImplementation impl = svgDoc.getImplementation();
		String namespaceURI = svgDoc.getDocumentElement().getNamespaceURI();
		SVGDocument svgDocLocal = (SVGDocument)impl.createDocument(namespaceURI, SVG, null);
		Node localElementNode = svgDocLocal.importNode(svgDoc.getDocumentElement(), true);
		svgDocLocal.replaceChild(localElementNode, svgDocLocal.getDocumentElement());
		return svgDocLocal;
	}
	
	private void initializeModel(SVGDocument svgDoc){
		this.svgDoc = svgDoc;

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
	/*final String namespace = svgDoc.getDocumentElement().getNamespaceURI();
		
		NamespaceContext nc = new NamespaceContext() {			
			
			@Override
			public Iterator getPrefixes(String namespaceURI) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPrefix(String namespaceURI) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNamespaceURI(String prefix) {
				if("x".equals(prefix)) {
          return namespace;
      }
				return null;
			}
		};
		xpath.setNamespaceContext(nc);*/
		try {
			XPathExpression expr = xpath.compile("//*[@uuid]"); // any element with uuid attribute			
			Object result = expr.evaluate(svgDoc, XPathConstants.NODESET);
			NodeList nodes = (NodeList)result;
			for (int i = 0; i < nodes.getLength(); i++) {
			    Element uuidActiveElement = (Element)svgDoc.importNode(nodes.item(i),true);
			    uuidActiveElement.setAttribute("fill", FILL_COLOR);
			    uuidActiveElement.setAttribute("opacity", FILL_OPACITY);
			    activeElementsMap.put(uuidActiveElement.getAttribute("uuid"), uuidActiveElement);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException{
		URL resource = StateChartSVGModel.class.getResource("MyStateChart4.svg");
		String parser = XMLResourceDescriptor.getXMLParserClassName();
	    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
	    
	    SVGDocument doc = (SVGDocument)f.createDocument(resource.toURI().toString());
		
		new StateChartSVGModel(doc);
	}
	
	public SVGDocument getCurrentSVGDocument(){
		SVGDocument svgDocLocal = deepCopySVGDocument(svgDoc);
//		SVGUtils.printDocument(svgDocLocal, System.out);
		Element svgDocLocalElement = svgDocLocal.getDocumentElement();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		for (String uuid : activeUUIDs){
			try {
				Object result = xpath.evaluate("//*[@uuid=\""+uuid+"\"]",svgDocLocalElement,XPathConstants.NODE);
				if (result != null && result instanceof Element){
					Element resultElement = (Element) result;
					Node parentNode = resultElement.getParentNode();
					parentNode.replaceChild(svgDocLocal.importNode(activeElementsMap.get(uuid), true), resultElement);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			} // any element with uuid attribute
		}
		return svgDocLocal;
	}
	
}
