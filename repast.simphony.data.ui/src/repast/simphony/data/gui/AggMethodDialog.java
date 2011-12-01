/*
 * Created by JFormDesigner on Tue Jul 31 11:21:31 EDT 2007
 */

package repast.simphony.data.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import repast.simphony.data.logging.gather.MethodMapping;
import repast.simphony.data.logging.gather.aggregate.AbstractStatsAggregateMapping;
import repast.simphony.data.logging.gather.aggregate.CountMapping;
import repast.simphony.data.logging.gather.aggregate.GeometricMeanMapping;
import repast.simphony.data.logging.gather.aggregate.KurtosisMapping;
import repast.simphony.data.logging.gather.aggregate.MaxMapping;
import repast.simphony.data.logging.gather.aggregate.MeanMapping;
import repast.simphony.data.logging.gather.aggregate.MinMapping;
import repast.simphony.data.logging.gather.aggregate.SkewnessMapping;
import repast.simphony.data.logging.gather.aggregate.StandardDeviationMapping;
import repast.simphony.data.logging.gather.aggregate.SumMapping;
import repast.simphony.data.logging.gather.aggregate.SumsqMapping;
import repast.simphony.data.logging.gather.aggregate.VarianceMapping;
import repast.simphony.util.SimpleFactory;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author User #2
 */
public class AggMethodDialog extends JDialog {

  private Map<String, SimpleFactory<AbstractStatsAggregateMapping>> aggregatorTable;

  private static final Set<Class> pTypes = new HashSet<Class>();
  private Method method;
  private AbstractStatsAggregateMapping statsMapping;


  static {
    pTypes.add(int.class);
    pTypes.add(double.class);
    pTypes.add(float.class);
    pTypes.add(long.class);
    pTypes.add(byte.class);
    pTypes.add(short.class);
  }

  public AggMethodDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public AggMethodDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

  public void init(Class agentClass) {

    DefaultComboBoxModel methodsModel = new DefaultComboBoxModel();

    Method[] methods = agentClass.getMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0 && (pTypes.contains(method.getReturnType()) ||
            Number.class.isAssignableFrom(method.getReturnType()))) {
        methodsModel.addElement(method);
			}
    }

    methodBox.setModel(methodsModel);
    methodBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value instanceof Method) {
          return super.getListCellRendererComponent(list, ((Method)value).getName(), index,
							isSelected, cellHasFocus);
				}
				return super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
			}
		});
    
    funcBox.setModel(new DefaultComboBoxModel(new Vector<String>(getAggregators())));

    methodBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        okButton.setEnabled(methodBox.getSelectedIndex() != -1 && funcBox.getSelectedIndex() != -1);
      }
    });

    funcBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        okButton.setEnabled(methodBox.getSelectedIndex() != -1 && funcBox.getSelectedIndex() != -1);
      }
    });

    okButton.addActionListener(new ActionListener() {


      public void actionPerformed(ActionEvent evt) {
        method = (Method) methodBox.getSelectedItem();
        statsMapping = aggregatorTable.get(funcBox.getSelectedItem().toString()).create();
        dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        dispose();
      }
    });
  }

  public AggregateMethodRepresentation getRepresentation() {
    if (statsMapping != null) {
      return new AggregateMethodRepresentation(statsMapping, method.getName());
    }

    return null;
  }


  protected Set<String> getAggregators() {
		if (aggregatorTable == null) {
			aggregatorTable = new HashMap<String, SimpleFactory<AbstractStatsAggregateMapping>>();
			aggregatorTable.put(CountMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new CountMapping();
				}
			});
			aggregatorTable.put(GeometricMeanMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new GeometricMeanMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(KurtosisMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new KurtosisMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(MaxMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new MaxMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(MeanMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new MeanMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(MinMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new MinMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(SkewnessMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new SkewnessMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(StandardDeviationMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new StandardDeviationMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(SumMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new SumMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(SumsqMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new SumsqMapping(new MethodMapping(method));
				}
			});
			aggregatorTable.put(VarianceMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new VarianceMapping(new MethodMapping(method));
				}
			});
		}

		return aggregatorTable.keySet();
	}



  private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		separator1 = compFactory.createSeparator("Select Method and Aggregate Function");
		label1 = new JLabel();
		methodBox = new JComboBox();
		label2 = new JLabel();
		funcBox = new JComboBox();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));
				contentPanel.add(separator1, cc.xywh(1, 1, 3, 1));

				//---- label1 ----
				label1.setText("Method:");
				contentPanel.add(label1, cc.xy(1, 3));
				contentPanel.add(methodBox, cc.xy(3, 3));

				//---- label2 ----
				label2.setText("Function:");
				contentPanel.add(label2, cc.xy(1, 5));
				contentPanel.add(funcBox, cc.xy(3, 5));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JComponent separator1;
	private JLabel label1;
	private JComboBox methodBox;
	private JLabel label2;
	private JComboBox funcBox;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
