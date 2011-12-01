package repast.simphony.ui.sweep;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import repast.simphony.parameter.MutableParameters;

public class ParameterSweepDialog extends JDialog {
	
	private JPanel buttonBar;
	private JButton cancelButton;
	private JButton okButton;

	private MutableParameters params;
	
	public ParameterSweepDialog(Frame owner) {
	    super(owner);
	    initComponents();
	    setLocationRelativeTo(getOwner());
//	    System.out.println("Owner Location "+getOwner().getLocation().toString());
	    Point parent = getOwner().getLocation();
	    setLocation((int) parent.getX()+50, (int) parent.getY()+50);
	  }

	  public ParameterSweepDialog(Dialog owner) {
	    super(owner);
	    initComponents();
	    setLocationRelativeTo(getOwner());
//	    System.out.println("Owner Location "+getOwner().getLocation().toString());
	    Point parent = getOwner().getLocation();
	    setLocation((int) parent.getX()+50, (int) parent.getY()+50);
	  }
	  
	  private void addListeners() {
		    okButton.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    		  ParameterSweepDialog.this.dispose();
		      }
		    });

		    cancelButton.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  ParameterSweepDialog.this.dispose();
		      }
		    });
		  }
	  
	  private void initComponents() {
		  setTitle("Parameter Sweep");
		  setModal(true);
		  
		  Container contentPane = getContentPane();
//		  contentPane.setLayout(new BorderLayout());
		  

	
	  }
	  
	  public void init(MutableParameters params) {
		    
		    this.params = params;
		    
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
		    
		    Container contentPane = getContentPane();
		    contentPane.setLayout(new GridBagLayout());
//			 contentPane.add(new JScrollPane(getInputPanel()),BorderLayout.CENTER);
		    
		    c.gridx=0;
		    c.gridy=0;
		    c.weighty=1.0;
		    
		    contentPane.add(getInputPanel(), c);
			
		    c.gridy=1;
		    c.weighty=0.0;
			 contentPane.add(getButtonBar(),c);
			 addListeners();
	  }
	  
	  private JPanel getButtonBar() {
		  if (buttonBar == null) {
			  buttonBar = new JPanel();
			 
			    okButton = new JButton();
			    cancelButton = new JButton();
		        buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
		        buttonBar.setLayout(new GridBagLayout());
		        ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{80, 80, 80};
		        ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0};

		        //---- okButton ----
		        okButton.setText("Done");
		        buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(0, 0, 0, 0), 0, 0));

//		        //---- cancelButton ----
//		        cancelButton.setText("Cancel");
//		        buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
//		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//		                new Insets(0, 0, 0, 0), 0, 0));

		  }
		  return buttonBar;
	  }
	  
	  private JPanel getInputPanel() {
		  return new ParameterSweepPanel(params, this);
	  }

}
