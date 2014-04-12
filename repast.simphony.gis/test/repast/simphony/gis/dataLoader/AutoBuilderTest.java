package repast.simphony.gis.dataLoader;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.dataLoader.engine.ContextXMLBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.gis.engine.GeographyProjectionController;
import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterType;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.AttributeFactory;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ContextFileReader;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.gis.Geography;

/**
 * @author Nick Collier
 */
public class AutoBuilderTest {

  private ContextData sContext;
  private Context context;
  private Parameters params;

  @Before
  public void init() throws IOException, ParameterFormatException, XMLStreamException {
  	new GeographyProjectionController();
  	
  	// assumes working dir is repast.simphony.dataLoader
    sContext = new ContextFileReader().read(new File("./test/repast/simphony/gis/dataLoader/context.xml"),
        new Classpath());
    context = new DefaultContext();

    ParametersCreator creator = new ParametersCreator();
    for (ProjectionData proj : sContext.projections()) {
      for (Attribute attribute : proj.attributes()) {
        ParameterType type = AttributeFactory.toParameterType(attribute);
        creator.addParameter(proj.getId() + attribute.getId(), attribute.getDisplayName(), type.getJavaClass(), 
            type.getValue(attribute.getValue()), false);
      }
    }
    params = creator.createParameters();
    RunEnvironment.init(null, null, params, false);
  }


  @Test
  public void geogCreation() {
    ContextXMLBuilder builder = new ContextXMLBuilder(sContext);
    builder.build(context);

    Geography geog = (Geography) context.getProjection("Geography");
    assertTrue(geog != null);
  }
}