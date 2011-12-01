/*CopyrightHere*/
package repast.simphony.integration;

import java.util.List;

import repast.simphony.engine.schedule.IAction;

public interface ExternalProgramRunner extends IAction, Reader, Writer {
	// void setProgramConfig(...)
	void addSource(IntegrationSource source);
		
	boolean removeSource(IntegrationSource source);
	
	List<IntegrationSource> getSources();
	
	LegacyExecutor getExecutor();
	
	void setExecutor(LegacyExecutor executor);
}
