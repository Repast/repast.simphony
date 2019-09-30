package repast.simphony.ws;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

public class ScenarioFileParser extends DefaultHandler2 {

    private static final String MODEL_INITIALIZER_XML = "model.initializer";
    private static final String MODEL_PLUGIN_XML = "model.plugin_jpf";

    public static class ScenarioElement {
        public String name, context, file;
    }

    List<ScenarioElement> elements;

    public List<ScenarioElement> parseScenario(Path scenarioPath)
            throws ParserConfigurationException, SAXException, IOException {
        elements = new ArrayList<>();
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(scenarioPath.toFile(), this);
        return elements;

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!(qName.equals(MODEL_INITIALIZER_XML) || qName.equals(MODEL_PLUGIN_XML))) {
            ScenarioElement element = new ScenarioElement();
            element.name = qName;
            element.file = attributes.getValue("file");
            element.context = attributes.getValue("context");
            elements.add(element);
        }
    }

}
