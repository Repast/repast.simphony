/**
 * 
 */
package repast.simphony.ui.widget;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.ContextJungNetwork;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.ui.RSApplication;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.MatrixFile;
import edu.uci.ics.jung.io.PajekNetWriter;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class NetworkExportDialog extends JDialog {

  private static final String GRAPHML = "GraphML";
  private static final String PAJEK_NET = "Pajek NET";
  private static final String TEXT = "Text Matrix";

  private static final String[] FORMATS = { GRAPHML, PAJEK_NET, TEXT };

  private List<Network<?>> networks = new ArrayList<>();
  private JComboBox<Network<?>> cmbNet = new JComboBox<>();
  private JComboBox<String> cmbFmt = new JComboBox<>(FORMATS);

  public NetworkExportDialog(JFrame owner) {
    super(owner, "Export Network Projections", true);
    setLayout(new BorderLayout());
    initComponents();
  }

  private void initComponents() {
    cmbNet.setRenderer(new DefaultListCellRenderer() {

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        if (value != null)
          value = ((Network<?>) value).getName();
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });

    // JPanel top = new JPanel();
    // add(top, BorderLayout.CENTER);
    FormLayout layout = new FormLayout("right:pref, 3dlu, default:grow", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.border(Borders.DIALOG);

    formBuilder.append("Network:", cmbNet);
    formBuilder.append("Format:", cmbFmt);
    add(formBuilder.build(), BorderLayout.CENTER);

    JButton save = new JButton("Save");
    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        save();
      }
    });
    JButton finished = new JButton("Finished");
    finished.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        finished();
      }
    });

    ButtonBarBuilder builder = new ButtonBarBuilder();
    builder.border(Borders.DIALOG);
    builder.addGlue();
    builder.addButton(save, finished);
    add(builder.build(), BorderLayout.SOUTH);
  }

  private void finished() {
    this.dispose();
  }

  @SuppressWarnings("unchecked")
  private void save() {
    File f = RSApplication.getRSApplicationInstance().getCurrentScenario().getScenarioDirectory().getParentFile();
    JFileChooser chooser = new JFileChooser(f);		
    chooser.showSaveDialog(this);	
	  f = chooser.getSelectedFile();
    
    if (f != null) {
      Graph<Object, RepastEdge<Object>> graph = 
          ((ContextJungNetwork<Object>)(Network<Object>)cmbNet.getSelectedItem()).getGraph();
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
        String format = cmbFmt.getSelectedItem().toString();
        if (format.equals(GRAPHML)) {
          GraphMLWriter<Object, RepastEdge<Object>> graphWriter = new GraphMLWriter<Object, RepastEdge<Object>>();
          graphWriter.save(graph, writer);
        } else if (format.equals(PAJEK_NET)) {
          PajekNetWriter<Object, RepastEdge<Object>> graphWriter = new PajekNetWriter<Object, RepastEdge<Object>>();
          graphWriter.save(graph, writer);
        } else if (format.equals(TEXT)) {
          MatrixFile<Object, RepastEdge<Object>> mf = new MatrixFile<>(null, null, null, null); 
          mf.save(graph, f.getAbsolutePath());
        }
      } catch (IOException ex) {
        MessageCenter.getMessageCenter(getClass()).error("Error while exporting network", ex);
      }
    }
  }

  private void findNets(Context<? extends Object> context) {
    for (Network<?> network : context.getProjections(Network.class)) {
      networks.add(network);
    }

    for (Context<? extends Object> child : context.getSubContexts()) {
      findNets(child);
    }
  }

  /**
   * Initializes the dialog with any networks contained in the specified Context
   * including all sub-contexts.
   * 
   * @param context
   */
  public void init(Context<? extends Object> context) {
    findNets(context);
    Collections.sort(networks, new Comparator<Network<?>>() {
      @Override
      public int compare(Network<?> o1, Network<?> o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    DefaultComboBoxModel<Network<?>> model = new DefaultComboBoxModel<Network<?>>();
    for (Network<?> net : networks) {
      model.addElement(net);
    }
    cmbNet.setModel(model);

  }
}
