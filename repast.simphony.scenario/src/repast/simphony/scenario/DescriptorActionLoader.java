/*CopyrightHere*/
package repast.simphony.scenario;

import java.io.File;

import repast.simphony.engine.schedule.Descriptor;

public abstract class DescriptorActionLoader<T extends Descriptor> extends ObjectActionLoader<T> {
	public DescriptorActionLoader(File file, Object contextID, Class<T> descriptorClass,
			String actionRoot) {
		super(file, contextID, descriptorClass, actionRoot);
	}

}