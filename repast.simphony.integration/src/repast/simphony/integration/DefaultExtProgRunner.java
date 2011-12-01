/*CopyrightHere*/
package repast.simphony.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import simphony.util.messages.MessageCenter;

public class DefaultExtProgRunner implements ExternalProgramRunner {
	private MessageCenter msgCenter = MessageCenter.getMessageCenter(DefaultExtProgRunner.class);

	private static final long serialVersionUID = 7673974590328212428L;

	private LegacyExecutor executor;

	private ArrayList<IntegrationSource> integrationSources;

	public DefaultExtProgRunner() {
		this.integrationSources = new ArrayList<IntegrationSource>();
		this.executor = new DefaultLegacyExecutor("", null, null);
	}

	public void write() throws Exception {
		for (IntegrationSource source : integrationSources) {
			source.write();
		}
	}

	public void execute() {
		try {
			executor.execute();
		} catch (IOException e) {
			msgCenter.warn("Error executing external program. Continuing.", e);
		}
	}

	public void read() throws Exception {
		for (IntegrationSource source : integrationSources) {
			source.read();
		}
	}

	public void addSource(IntegrationSource source) {
		integrationSources.add(source);
	}

	public boolean removeSource(IntegrationSource source) {
		return integrationSources.remove(source);
	}

	public List<IntegrationSource> getSources() {
		return integrationSources;
	}

	/**
	 * Performs a setWriteObject on all the integration sources.
	 */
	public void setWrittenObject(Queryable queryable) {
		for (IntegrationSource source : integrationSources) {
			source.setWrittenObject(queryable);
		}

	}

	public LegacyExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(LegacyExecutor executor) {
		this.executor = executor;
	}

}
