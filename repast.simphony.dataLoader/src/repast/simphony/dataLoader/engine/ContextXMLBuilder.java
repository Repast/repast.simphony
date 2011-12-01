package repast.simphony.dataLoader.engine;

import java.util.ArrayList;
import java.util.List;

import org.jscience.physics.amount.Amount;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ProjectionData;
import simphony.util.messages.MessageCenter;

/**
 * Builds a context based only the info in an SContext.
 *
 * @author Nick Collier
 */
public class ContextXMLBuilder implements ContextBuilder {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(ContextXMLBuilder.class);

  private List<ContextBuilder> builders = new ArrayList<ContextBuilder>();


  private Object id;
  private Amount tickUnits;

  public ContextXMLBuilder(ContextData context) {
    for (Attribute attribute : context.attributes()) {
      if (attribute.getId().equals(AutoBuilderConstants.TIME_UNITS_ID) && attribute.getType().equals(String.class) &&
              attribute.getValue().trim().length() > 0) {
        try {
          tickUnits = Amount.valueOf(attribute.getValue());
        } catch (Exception ex) {
          msg.warn("Error setting time units: unit values must be parsable by Amount.valueOf()", ex);
        }
      }
    }

    id = context.getId();
    ContextBuilderFactory fac = ContextBuilderFactory.getInstance();

    for (ProjectionData proj : context.projections()) {
      ContextBuilder builder = fac.createBuilder(proj);
      builders.add(builder);
    }

    /*
    for (SAgent agent : score.getAgents()) {
      // don't do contexts as agents for now
      if (!(agent instanceof SContext)) {
        for (ContextBuilder builder : fac.createBuilder(agent)) {
          builders.add(builder);
        }
      }
    }
    */
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
    // this assumes that the context is not an agent,
    // that is that we don't have multiple contexts of this type.

    if (tickUnits != null) {
      RunEnvironment.getInstance().getCurrentSchedule().setTimeUnits(tickUnits);
    }
    
    context.setTypeID(id);
    context.setId(id);

    for (ContextBuilder builder : builders) {
      context = builder.build(context);
    }
    return context;
  }


}
