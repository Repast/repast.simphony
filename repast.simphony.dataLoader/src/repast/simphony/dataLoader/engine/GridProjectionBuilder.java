package repast.simphony.dataLoader.engine;

import static repast.simphony.dataLoader.engine.AutoBuilderConstants.BORDER_RULE_ID;
import static repast.simphony.dataLoader.engine.AutoBuilderConstants.BOUNCY_RULE;
import static repast.simphony.dataLoader.engine.AutoBuilderConstants.MULTI_ID;
import static repast.simphony.dataLoader.engine.AutoBuilderConstants.PERIODIC_RULE;
import static repast.simphony.dataLoader.engine.AutoBuilderConstants.STICKY_RULE;
import static repast.simphony.dataLoader.engine.AutoBuilderConstants.STRICT_RULE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.grid.BouncyBorders;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPointTranslator;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StickyBorders;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;
import simphony.util.messages.MessageCenter;

/**
 * Builds a grid projection based on SGrid data.
 * 
 * @author Nick Collier
 */
public class GridProjectionBuilder implements ProjectionBuilderFactory {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(GridProjectionBuilder.class);

  private static class GridBuilder implements ContextBuilder {

    private String name, borderRule;
    private List<String> dimNames = new ArrayList<String>();
    private ProjectionData grid;
    private boolean multi = false;
    
    GridBuilder(ProjectionData proj) {
      name = proj.getId();
      this.grid = proj;
      init();
    }

    private void init() {
      for (Attribute attrib : grid.attributes()) {
        if (attrib.getType().equals(int.class)) {
          dimNames.add(grid.getId() + attrib.getId());
        }
        
        if (attrib.getId().equalsIgnoreCase(BORDER_RULE_ID)) {
          borderRule = attrib.getValue();
        }
        
        if (attrib.getId().equalsIgnoreCase(MULTI_ID)) {
          multi = Boolean.parseBoolean(attrib.getValue());
        }
      }

      if (dimNames.isEmpty()) {
        String msg = "Unable to build Grid '" + grid.getId() + "': too few dimensions specified";
        GridProjectionBuilder.msg.error(msg, new IllegalArgumentException("Too few dimensions specified"));
      }
      
      if (borderRule == null) {
        String msg = "Unable to build Grid '" + grid.getId() + "': invalid border rule";
        GridProjectionBuilder.msg.error(msg, new IllegalArgumentException("Invalid border rule"));
        
      }
    }

    public Context build(Context context) {
      GridFactory factory = GridFactoryFinder.createGridFactory(new HashMap<String, Object>());
      int[] dims = new int[dimNames.size()];
      Parameters p = RunEnvironment.getInstance().getParameters();
      for (int i = 0; i < dimNames.size(); i++) {
        dims[i] = ((Integer) p.getValue(dimNames.get(i))).intValue();
      }
      GridBuilderParameters params = new GridBuilderParameters(translatorFor(borderRule),
              new RandomGridAdder(), multi, dims);
      factory.createGrid(name, context, params);
      return context;
    }

    private GridPointTranslator translatorFor(String rule) {
      if (rule.equalsIgnoreCase(BOUNCY_RULE)) {
        return new BouncyBorders();
      } else if (rule.equalsIgnoreCase(STICKY_RULE)) {
        return new StickyBorders();
      } else if (rule.equalsIgnoreCase(STRICT_RULE)) {
        return new StrictBorders();
      } else if (rule.equalsIgnoreCase(PERIODIC_RULE)) {
        return new WrapAroundBorders();
      } else {
        return null;
      }
    }
  }

  /**
   * Gets a ContextBuilder to build the specified
   * Projection.
   *
   * @param proj the type of Projection to build
   * @return a ContextBuilder to build the specified
   *         Projection.
   */
  public ContextBuilder getBuilder(ProjectionData proj) {
    return new GridBuilder(proj);
  }

}
