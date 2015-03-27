package repast.simphony.chart2.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
import repast.simphony.ui.plugin.editor.PluginWizardStep;


/**
 * Step for configuring non-aggregate data to be displayed by a chart.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class AggregateChartStep extends PluginWizardStep implements TableModelListener {

  private TimeSeriesWizardModel model;
  private JTable table;
  private SeriesConfigTableModel tableModel;

  public AggregateChartStep() {
    super("Chart Data", "Please configure the data to be displayed by the chart.");
  }
  
  @Override
	protected JPanel getContentPanel(){
  	JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    table = new JTable();
    table.setPreferredScrollableViewportSize(new Dimension(450, 100));
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    table.setRowHeight(table.getRowHeight() + 4);
    table.setDefaultRenderer(Color.class, new ColorRenderer());
    ColorEditor colorEditor = new ColorEditor();
    table.setDefaultEditor(Color.class, colorEditor);
    
    ((DefaultCellEditor)table.getDefaultEditor(String.class)).setClickCountToStart(2);

    JScrollPane scrollPane = new JScrollPane(table);
   
    panel.add(scrollPane);
    return panel;
  }

  public void init(WizardModel wizardModel) {
    this.model = (TimeSeriesWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    table.getModel().removeTableModelListener(this);
    tableModel = new SeriesConfigTableModel(descriptor, model.getDataSet());
    tableModel.addTableModelListener(this);
    table.setModel(tableModel);
    TableColumn col = table.getColumnModel().getColumn(0);
    Dimension dim = new JCheckBox().getPreferredSize();
    col.setMaxWidth((int)dim.getWidth() + 4);
    col.setMinWidth((int)dim.getWidth() + 4);
    
    
    setComplete(tableModel.anySeriesIncluded());
  }

  /* (non-Javadoc)
   * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
   */
  @Override
  public void tableChanged(TableModelEvent evt) {
    setComplete(tableModel.anySeriesIncluded());
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    tableModel.apply(descriptor);
  }
}
