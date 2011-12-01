package repast.simphony.dataLoader.engine;

import repast.simphony.scenario.data.ContextData;

/**
 * DataLoaderAction that uses a ScoreContextBuilder to build the context.
 *
 * @author Nick Collier
 */
public class ContextXMLDataLoaderAction extends DataLoaderControllerAction<ContextXMLBuilder> {

  /**
   * Creates this ScoreDataLoader Action using the specified SContext.
   *
   * @param root the root SContext of the score file
   */
  public ContextXMLDataLoaderAction(ContextData root) {
    super("XML Context Builder", new ContextXMLBuilder(root), root);
  }
}
