package repast.simphony.engine.watcher.query;

import repast.simphony.context.Context;

/**
 * Interface for AST* classes that create IBooleanExpressions. 
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface BooleanExpressionBuilder {

	public IBooleanExpression buildExpression(Context context);
}
