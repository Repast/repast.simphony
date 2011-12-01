package repast.simphony.integration;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

public class XPathTester {
	public static void main(String[] args) {
		Document doc = new Document(new Element("fileDef"));
		Element element = new Element("title");
		element.setContent(new DataContent("foo"));
		element.setAttribute("blah", "foo");
		doc.getRootElement().addContent(element);
		
		Element subElement = new Element("subElement");
		subElement.addContent(new DataContent("subElementValue"));
		element.addContent(subElement);
		
		try {
			System.out.println("****************");
			System.out.println("Select . from root");
			XPath xpath = XPath.newInstance(".");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement()) + ":");
			
			System.out.println("****************");
			System.out.println("Select fileDef from root");
			xpath = XPath.newInstance("fileDef");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement()) + ":");
			
			System.out.println("****************");
			System.out.println("Select fileDef/title from root");
			xpath = XPath.newInstance("fileDef/title");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement()) + ":");
			
			System.out.println("****************");
			System.out.println("Select title from root");
			xpath = XPath.newInstance("title");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement()) + ":");
			
			System.out.println("****************");
			System.out.println("Select ./title from root");
			xpath = XPath.newInstance("./title");
			System.out.println(":" + xpath.selectSingleNode(element) + ":");
			
			System.out.println("****************");
			System.out.println("Select /fileDef/title from root");
			xpath = XPath.newInstance("/fileDef/title");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement()) + ":");
			
			System.out.println("****************");
			System.out.println("Select title from root children(0)");
			xpath = XPath.newInstance("title");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement().getChildren().get(0)) + ":");
			
			System.out.println("****************");
			System.out.println("Select //title from root children(0)");
			xpath = XPath.newInstance("//title");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement().getChildren().get(0)) + ":");
			
			System.out.println("****************");
			System.out.println("Select title/subElement from root");
			xpath = XPath.newInstance("title/subElement");
			System.out.println(":" + xpath.selectSingleNode(doc.getRootElement()) + ":");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		doc.getRootElement().addContent()
	}
}
