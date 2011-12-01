package repast.simphony.dataLoader.ui.wizard;

import com.thoughtworks.xstream.converters.Converter;
import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.engine.XMLContextBuilder;
import repast.simphony.dataLoader.engine.XMLDataLoaderAction;
import repast.simphony.scenario.Scenario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 */
public class XMLContextActionBuilder implements ContextActionBuilder {

  private File xmlFile;
  private List<Converter> converters = new ArrayList<Converter>();

  public DataLoaderControllerAction getAction(Scenario scenario, Object parentID) {
    return new XMLDataLoaderAction(new XMLContextBuilder(xmlFile, converters), scenario);
  }

  void setXMLFile(File file) {
    xmlFile = file;
  }

  File getXMLFile() {
    return xmlFile;
  }

  public void addConverter(Converter converter) {
    if (!converters.contains(converter)) converters.add(converter);
  }

  public void clearConverters() {
    converters.clear();
  }

  public Iterable<Converter> converters() {
    return converters;
  }
}