/*CopyrightHere*/
package repast.simphony.data.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.pietschy.wizard.InvalidStateException;

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
import repast.simphony.util.wizard.ModelAwarePanelStep;
import repast.simphony.util.SimpleFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * @author Jerry Vos
 */
public class AggregationStep extends ModelAwarePanelStep<DataMappingWizardModel> {
	private static final long serialVersionUID = 6030604154470752051L;
	
	public AggregationStep() {
		super("Aggregation Function", "Choose the aggregation function to apply to the data.");
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label1 = new JLabel();
		aggregationBox = new JComboBox();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
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

		//---- label1 ----
		label1.setText("Aggregation Function");
		add(label1, cc.xy(1, 1));
		add(aggregationBox, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	
		aggregationBox.setModel(new DefaultComboBoxModel(new Vector<String>(getAggregators())));
		addCompleteListener(aggregationBox);
	}
	
	@Override
	protected void updateComplete() {
		setComplete(aggregationBox.getSelectedIndex() >= 0);
	}
	
	protected Map<String, SimpleFactory<AbstractStatsAggregateMapping>> aggregatorTable;
	
	
	
	@SuppressWarnings("unchecked")
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
					return new GeometricMeanMapping(null);
				}
			});
			aggregatorTable.put(KurtosisMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new KurtosisMapping(null);
				}
			});
			aggregatorTable.put(MaxMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new MaxMapping(null);
				}
			});
			aggregatorTable.put(MeanMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new MeanMapping(null);
				}
			});
			aggregatorTable.put(MinMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new MinMapping(null);
				}
			});
			aggregatorTable.put(SkewnessMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new SkewnessMapping(null);
				}
			});
			aggregatorTable.put(StandardDeviationMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new StandardDeviationMapping(null);
				}
			});
			aggregatorTable.put(SumMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new SumMapping(null);
				}
			});
			aggregatorTable.put(SumsqMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new SumsqMapping(null);
				}
			});
			aggregatorTable.put(VarianceMapping.TITLE, new SimpleFactory<AbstractStatsAggregateMapping>() {
				public AbstractStatsAggregateMapping create() {
					return new VarianceMapping(null);
				}
			});
		}
		
		return aggregatorTable.keySet();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label1;
	private JComboBox aggregationBox;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	@Override
	public void prepare() {
		super.prepare();
		
		if (model.getAggregator() != null && model.getAggregator() instanceof AbstractStatsAggregateMapping) {
			aggregationBox.setSelectedItem(((AbstractStatsAggregateMapping) model
					.getAggregator()).getStatisticType());
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		model.setAggregator(aggregatorTable.get(aggregationBox.getSelectedItem()).create());
	}
}
