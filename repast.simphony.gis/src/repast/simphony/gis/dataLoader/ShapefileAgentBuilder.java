package repast.simphony.gis.dataLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.apache.poi.ss.formula.functions.T;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.ShapefileLoader;
import simphony.util.messages.MessageCenter;

/**
 * TODO Geotools: re-work this into the ContextBuilderFactory 
 *        context.xml dataloader mechanism
 * 
 * @author eric
 *
 */
public class ShapefileAgentBuilder implements ContextBuilder<T> {

	private MessageCenter msg = MessageCenter.getMessageCenter(ShapefileAgentBuilder.class);

  private String clazzName;
  private String parameterID;
  private boolean useContext = false;

  private ShapefileAgentBuilder(String clazzName, String parameterID) {
    this.clazzName = clazzName;
    this.parameterID = parameterID;
  }

  public Context build(Context context) {
    Parameters p = RunEnvironment.getInstance().getParameters();
    if (!p.getSchema().contains(parameterID)) {
      String info = "Shapefile parameter '" + parameterID + "' is missing";
      msg.error(info, new RuntimeException(info));
      return context;
    }

    File shpFile = new File(p.getValue(parameterID).toString());
    if (!shpFile.exists()) {
      String emsg = "Unable to load agents from shapefile: '" + shpFile.getName() + "'does not exist";
      msg.error(emsg, new IllegalArgumentException("Missing shapefile"));
      return context;
    }
   
    Geography geog = null;
    Iterator iter = context.getProjections(Geography.class).iterator();
    if (iter.hasNext()) geog = (Geography) iter.next();

    //class loaded should not be an interface or abstract class
    //empty constructors should be present in class loaded
    try {
      Class clazz = Class.forName(clazzName);
      ShapefileLoader loader = new ShapefileLoader(clazz, shpFile.toURI().toURL(), geog,
              context);
      if (useContext) {
        for (Object obj : context) {
          if (!loader.hasNext()) break;
          loader.next(obj);
        }

      } else {
        while (loader.hasNext()) {
          loader.next();
        }
      }

    } catch (ClassNotFoundException e) {
      msg.error("Agent creation error: unable to find agent class '" + clazzName + "'", e);
    } catch (MalformedURLException e) {
      msg.error("Shapefile loading error", e);
    }
    return context;
  }
	
}
