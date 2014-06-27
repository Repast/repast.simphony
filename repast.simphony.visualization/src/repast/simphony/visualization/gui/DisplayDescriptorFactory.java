package repast.simphony.visualization.gui;

import repast.simphony.visualization.engine.DisplayDescriptor;

public interface DisplayDescriptorFactory {

	public DisplayDescriptor createDescriptor(String name);
	
}
