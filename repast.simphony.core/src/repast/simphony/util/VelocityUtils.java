/*CopyrightHere*/
package repast.simphony.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import simphony.util.messages.MessageCenter;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * A class providing utility functions for working with template strings through
 * Velocity.<p/> For more information on velocity, including a listing of how
 * to define variables in it, visit <a
 * href="http://jakarta.apache.org/velocity/">http://jakarta.apache.org/velocity/</a>.
 * 
 * @see org.apache.velocity.VelocityContext
 * @see org.apache.velocity.app.Velocity
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public class VelocityUtils {
	private static final MessageCenter l4jLogger = MessageCenter
			.getMessageCenter(VelocityUtils.class);

	/**
	 * Parses a string containing variables who's values will be filled in based
	 * on the VelocityContext passed in. <p/> If there is an error parsing the
	 * string, the valueOnError is returned.<p/>
	 * 
	 * An example string would be "Var1's value is: ${Var1}"
	 * 
	 * @param context
	 *            object containing the values to replace the variables with
	 * @param toParse
	 *            the string containing the variables and arbitrary text
	 * @param valueOnError
	 *            the string to return if there is an error parsing the toParse
	 *            string
	 * @return either a string based on the variable toParse string or the
	 *         valueOnError if there was an error parsing the string
	 */
	public static String evaluate(VelocityContext context, String toParse,
			String valueOnError) {
		StringWriter writer = new StringWriter();

		try {
			Velocity.evaluate(context, writer, toParse, toParse);
			return writer.toString();
		} catch (Exception e) {
			l4jLogger.warn(
					"VelocityUtils.evaluate: Error evaluating variable based string("
							+ toParse + "), returning(" + valueOnError + ").",
					e);

			return valueOnError;
		}
	}

	/**
	 * Retrieves the names of the variables contained in the toParse string.<p/>
	 * The code found in this method was harvested from Velocity's parsing code.
	 * 
	 * @param toParse
	 *            a string containing variables
	 * @return an ArrayList of the variables contained in the string
	 */
	public static ArrayList<String> getTemplateVarNames(String toParse) {
		SimpleNode nodeTree = null;
		ArrayList<String> varNames = new ArrayList<String>();

		try {
			nodeTree = RuntimeSingleton.parse(new BufferedReader(
					new StringReader(toParse)), toParse);
		} catch (Exception e) {
			l4jLogger.error(
					"VelocityUtils.templateVarNames: Error finding variable names in string("
							+ toParse + ")", e);
			return null;
		}

		if (nodeTree != null) {
			InternalContextAdapterImpl ica = new InternalContextAdapterImpl(
					new VelocityContext());

			ica.pushCurrentTemplateName(toParse);

			try {
				nodeTree.init(ica, RuntimeSingleton.getRuntimeServices());

				for (int i = 0; i < nodeTree.jjtGetNumChildren(); i++) {
					Node node = nodeTree.jjtGetChild(i);

					if (node instanceof ASTReference) {
						varNames.add(((ASTReference) node).getRootString());
					}
				}
			} catch (Exception e) {
				l4jLogger.error(
						"VelocityUtils.templateVarNames: Error finding variable names in string("
								+ toParse + ")", e);
				return null;
			}
		}

		return varNames;
	}

	/**
	 * Initializes velocity
	 */
	static {
		try {
			Velocity.init();
		} catch (Exception e) {
			l4jLogger
					.error(
							"VelocityUtils.static: Error initializing the velocity parsing library.",
							e);
		}
	}
}
