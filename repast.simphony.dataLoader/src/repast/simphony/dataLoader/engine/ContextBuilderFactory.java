package repast.simphony.dataLoader.engine;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.ProjectionType;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.ShapefileLoader;
import simphony.util.messages.MessageCenter;

/**
 * Factory class for creating context builders
 * that create and add projections to Contexts and
 * create and add agents to contexts.
 *
 * @author Nick Collier
 */
public class ContextBuilderFactory {

  private static final String AGENT_COUNT_ID = "initialCount";
  private static final String SHP_LOAD_ID = "shpFile";

  private Map<ProjectionType, ProjectionBuilderFactory> builderMap =
          new HashMap<ProjectionType, ProjectionBuilderFactory>();

  private static ContextBuilderFactory instance = new ContextBuilderFactory();

  /**
   * Gets the singleton instance of the ContextBuilderFactory.
   *
   * @return the singleton instance of the ContextBuilderFactory.
   */
  public static ContextBuilderFactory getInstance() {
    return instance;
  }

  private ContextBuilderFactory() {
    registerProjectionBuilder(ProjectionType.GRID, new GridProjectionBuilder());
    registerProjectionBuilder(ProjectionType.NETWORK, new NetworkProjectionBuilder());
    registerProjectionBuilder(ProjectionType.CONTINUOUS_SPACE, new SpaceProjectionBuilder());
    registerProjectionBuilder(ProjectionType.GEOGRAPHY, new GeographyProjectionBuilder());
  }

  /**
   * Registers the projection builder for use with the specified class.
   *
   * @param clazz   the class of the Projection to register the builder for
   * @param builder the builder to use to build the specified class of projections.
   */
  public void registerProjectionBuilder(ProjectionType type, ProjectionBuilderFactory builder) {
    builderMap.put(type, builder);
  }

  /**
   * Creates a context builder that will create the specified projection.
   *
   * @param proj the projection to build
   * @return a context builder that will create the specified projection.
   */
  public ContextBuilder createBuilder(ProjectionData proj) {
    ProjectionBuilderFactory builder = builderMap.get(proj.getType());
    return builder.getBuilder(proj);
  }

  /*
  public List<ContextBuilder> createBuilder(SAgent agent) {
    List<ContextBuilder> builders = new ArrayList<ContextBuilder>();
    ContextBuilder agentBuilder = null;
    ShapefileAgentBuilder shpBuilder = null;
    for (SAttribute attribute : agent.getAttributes()) {
      if (attribute.getID().equals(AGENT_COUNT_ID) && attribute.getSType().equals(SAttributeType.INTEGER_LITERAL) &&
              attribute.getDefaultValue().trim().length() > 0) {
        // score contains a valid initial count so create a context builder.
        SImplementation imp = agent.getImplementation();
        agentBuilder = new AgentContextBuilder(imp.getDerivedPackage() + "." + imp.getClassName(), agent.getID() + attribute.getID());
      }

      if (attribute.getSType().equals(SAttributeType.STRING_LITERAL) && attribute.getID().equals(SHP_LOAD_ID) &&
                attribute.getDefaultValue() != null && attribute.getDefaultValue().endsWith(".shp")) {
        SImplementation imp = agent.getImplementation();
        shpBuilder = new ShapefileAgentBuilder(imp.getDerivedPackage() + "." + imp.getClassName(), agent.getID() + attribute.getID());
      }
    }

    if (agentBuilder != null) builders.add(agentBuilder);
    if (shpBuilder != null) builders.add(shpBuilder);
    // size is two then both in there and
    // shp builder should not create the agents but just
    // load them.
    if (builders.size() == 2) shpBuilder.useContext = true;
    return builders;
  }
  */

  private static class ShapefileAgentBuilder implements ContextBuilder {

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

  private static class AgentContextBuilder implements ContextBuilder {

    private MessageCenter msg = MessageCenter.getMessageCenter(AgentContextBuilder.class);
    private String clazzName;
    private String parameterID;

    private AgentContextBuilder(String clazzName, String parameterID) {
      this.clazzName = clazzName;
      this.parameterID = parameterID;
    }

    /**
     * Builds and returns a context. Building a context consists of filling it with
     * agents, adding projects and so forth. The returned context does not necessarily
     * have to be the passed in context.
     *
     * @param context
     * @return the built context.
     */
    public Context build(Context context) {
      Parameters p = RunEnvironment.getInstance().getParameters();
      if (!p.getSchema().contains(parameterID)) {
        msg.error("Initial agent count parameter '" + parameterID + "' is missing", new RuntimeException());
        return context;
      }

      int count = ((Integer) p.getValue(parameterID)).intValue();
      try {
        Class clazz = Class.forName(clazzName);
        for (int i = 0; i < count; i++) {
          Object obj = clazz.newInstance();
          context.add(obj);
        }
      } catch (ClassNotFoundException e) {
        msg.error("Agent creation error: unable to find agent class '" + clazzName + "'", e);
      } catch (IllegalAccessException e) {
        msg.error("Agent creation error", e);
      } catch (InstantiationException e) {
        msg.error("Agent creation error: agent class missing no-arg constructor?", e);
      }

      return context;
    }

  }
}
