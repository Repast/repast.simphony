package repast.simphony.dataLoader.ui.wizard.builder;

import javax.swing.Icon;

public class AgentLayerCheckNode extends CheckNode {
	Icon icon;
	public AgentLayerCheckNode(String title, AgentLayer layer,
			boolean isSelected) {
		super(title, layer, isSelected);
		icon = new SquareIcon(layer.getNodeColor());
		
	}

	@Override
	public Icon getIcon() {
		return icon;
	}

}
