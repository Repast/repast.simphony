package repast.simphony.ui.sweep;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import repast.simphony.parameter.Parameters;

public class ParameterSweepParameterPanel extends JPanel {

	private Parameters params;
	private HashMap<String, String> displayInternalNameMap = new HashMap<String, String>();
	private List<ParameterSweepInput> entries = new ArrayList<ParameterSweepInput>();
	
	public ParameterSweepParameterPanel(Parameters params) {
		this.params = params;
		initComponents();
	}
	
	private void initComponents() {
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		JPanel parameterPanel = this;
		parameterPanel.setLayout(new GridBagLayout());
		


		parameterPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		JPanel parameters = new JPanel();
		parameters.setLayout(new GridBagLayout());
		
		JScrollPane jsp = new JScrollPane(parameters);
		jsp.setPreferredSize(new Dimension(700, 300));
		jsp.setMinimumSize(new Dimension(700, 200));
		jsp.setMaximumSize(new Dimension(700, 500));
		
		c.weighty = 1;
		parameterPanel.add(jsp, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		int row = 0;

		List<ParameterSweepInput> paramPanels = new ArrayList<ParameterSweepInput>();
		List<String> names = new ArrayList<String>();
		List<String> parameterNames = new ArrayList<String>();
		
		
		for (String name : params.getSchema().parameterNames()) {
			names.add(params.getDisplayName(name));
			parameterNames.add(name);
			displayInternalNameMap.put(params.getDisplayName(name), name);
		}
		Collections.sort(names);
		for (String name : names) {
			
			JLabel parameterName = new JLabel(name);
	        parameterName.setHorizontalAlignment(JLabel.RIGHT);
	        
	        ParameterSweepInput aPSI = new ParameterSweepInput(displayInternalNameMap.get(name),params);
	        aPSI.setConstantValue(params.getValueAsString(displayInternalNameMap.get(name)));
	        
	        entries.add(aPSI);

	        c.gridy = row++;
			c.gridx = 0;
			parameters.add(aPSI.getParameterName(),c);
			c.gridx = 1;
			parameters.add(aPSI.getParameterTypeComboBox(),c);
			c.gridx = 2;
			parameters.add(aPSI.getParameterSweepPanel(),c);
			paramPanels.add(aPSI);
			
			 
		}
	}

	public List<ParameterSweepInput> getEntries() {
		return entries;
	}
}
