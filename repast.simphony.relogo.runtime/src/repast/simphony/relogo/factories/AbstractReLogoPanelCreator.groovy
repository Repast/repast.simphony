/**
 * 
 */
package repast.simphony.relogo.factories

import javax.swing.JPanelimport groovy.swing.SwingBuilder
import java.awt.BorderLayout
import java.awt.event.ItemEventimport javax.swing.BoxLayout
import repast.simphony.essentials.RepastEssentials
import repast.simphony.engine.schedule.IScheduleimport repast.simphony.engine.schedule.ScheduleParametersimport repast.simphony.engine.schedule.IActionimport repast.simphony.engine.environment.RunEnvironment
import repast.simphony.relogo.*
import java.awt.FlowLayout
import java.awt.GridLayoutimport javax.swing.JScrollPaneimport javax.swing.ScrollPaneConstantsimport java.awt.Color
import repast.simphony.userpanel.ui.UserPanelCreator;/**
 * @author jozik
 *
 */

public abstract class AbstractReLogoPanelCreator implements UserPanelCreator {
	
	public JPanel createPanel(){
		SwingBuilder swing = new SwingBuilder()
		JPanel innerPanel
		JPanel basePanel = swing.panel(constraints: BorderLayout.WEST){
			flowLayout(alignment: FlowLayout.LEADING)
			innerPanel = panel(){
				boxLayout(axis:BoxLayout.Y_AXIS)
			}
		}
		addComponents(innerPanel)
		return basePanel
	}
	
	public abstract void addComponents(JPanel parent);
		
}
