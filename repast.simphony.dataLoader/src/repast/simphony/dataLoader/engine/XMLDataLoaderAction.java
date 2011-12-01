package repast.simphony.dataLoader.engine;

import com.thoughtworks.xstream.converters.Converter;
import repast.simphony.scenario.Scenario;

import java.util.List;

/**
 * Action for creating contexts from a serialized xml file.
 *
 * @author Nick Collier
 */
public class XMLDataLoaderAction extends DataLoaderControllerAction<XMLContextBuilder> {


  public XMLDataLoaderAction(XMLContextBuilder loader, Scenario scenario) {
    super("XML data loader", loader, scenario.getContext());
  }

  public XMLDataLoaderAction(String fileName, Scenario scenario) {
    super("XML data loader", new XMLContextBuilder(fileName), scenario.getContext());
  }

  public XMLDataLoaderAction(String fileName, List<Converter> converters, Scenario scenario) {
    super("XML data loader", new XMLContextBuilder(fileName, converters), scenario.getContext());
  }
}