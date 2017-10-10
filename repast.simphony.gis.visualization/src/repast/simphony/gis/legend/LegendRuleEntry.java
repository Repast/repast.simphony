package repast.simphony.gis.legend;

import org.geotools.styling.Rule;

import javax.swing.*;
import java.awt.*;

/**
 * This legend element represents a rule in a style. This will hold the rule as
 * well as an iconic representation of that rule. This will not have children.
 * Setting the dataVisible flag on this element will have no effect.
 *
 * @author $Author: howe $
 * @date $Date: 2007/04/18 19:26:11 $
 * @version $Revision: 1.5 $
 * 
 * @deprecated 2D piccolo based code is being removed
 */
public class LegendRuleEntry extends LegendEntry {

	private static final long serialVersionUID = 4753785110800410180L;

	private Icon icon = null;

	private Rule rule = null;

	/**
	 * Creates a new instance of LegendStyleElementNodeInfo
	 *
	 * @param name
	 *            The title of the Rule.
	 * @param icon
	 *            The icon for this Rule.
	 * @param rule
	 *            The rule for this LegendEntry.
	 *
	 */
	public LegendRuleEntry(String name, Icon icon, Rule rule) {
		super(rule);
		setName(name);
		setIcon(icon);
		setRule(rule);
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Rule getRule() {
		return this.rule;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return this.icon;
	}

	public Color getBackground(boolean selected) {
		return null;
	}
}

