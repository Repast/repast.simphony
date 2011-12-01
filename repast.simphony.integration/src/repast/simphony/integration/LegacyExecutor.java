/*CopyrightHere*/
package repast.simphony.integration;

import java.io.IOException;

public interface LegacyExecutor {
	int execute() throws IOException;
	
	void spawn() throws IOException;
}
