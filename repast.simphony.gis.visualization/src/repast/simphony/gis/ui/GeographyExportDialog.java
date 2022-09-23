/*
 * Created by JFormDesigner on Tue Oct 30 17:44:38 EDT 2007
 */

package repast.simphony.gis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.ShapefileWriter;
import simphony.util.messages.MessageCenter;

/**
 * @author User #2
 */
public class GeographyExportDialog extends JDialog {

  private java.util.List<Geography> geogs = new ArrayList<Geography>();
  private static MessageCenter msg = MessageCenter.getMessageCenter(GeographyExportDialog.class);

  public GeographyExportDialog(Frame owner) {
    super(owner);
    initComponents();
    saveBtn.setEnabled(false);
    initListeners();
  }

  public GeographyExportDialog(Dialog owner) {
    super(owner);
    initComponents();
    saveBtn.setEnabled(false);
    initListeners();
  }

  private void initListeners() {
    layerTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getPath();
        if (path == null) saveBtn.setEnabled(false);
        else {
          GeogNode node = (GeogNode) path.getLastPathComponent();
          saveBtn.setEnabled(node.isLayer());
        }
      }
    });

    finishedBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GeographyExportDialog.this.dispose();
      }
    });

    saveBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {
          public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".shp");
          }

          public String getDescription() {
            return "Shapefile (*.shp)";
          }
        });

        if (chooser.showSaveDialog(GeographyExportDialog.this) == JFileChooser.APPROVE_OPTION) {
          File shp = chooser.getSelectedFile();
          if (!shp.getName().endsWith(".shp")) {
            shp = new File(shp.getParentFile(), shp.getName() + ".shp");
          }
          save(shp);
        }
      }
    });
  }

  private void save(File shpFile) {
    TreePath path = layerTree.getSelectionPath();
    if (path != null) {
      GeogNode node = (GeogNode) path.getLastPathComponent();
      String layerName = node.getUserObject().toString();
      GeogNode geog = (GeogNode) node.getParent();
      ShapefileWriter writer = new ShapefileWriter((Geography) geog.getUserObject());
      try {
        writer.write(layerName, shpFile.toURI().toURL());
      } catch (MalformedURLException e) {
       msg.error("Error while writing shapefile", e);
      }
    }
  }

  public void init(Context rootContext) {
    gatherGeographies(rootContext);
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    DefaultTreeModel model = new DefaultTreeModel(root);
    Collections.sort(geogs, new Comparator<Geography>() {
      public int compare(Geography o1, Geography o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    for (Geography geog : geogs) {
      GeogNode node = new GeogNode(geog, geog.getName(), false);
      root.add(node);


      java.util.List<GeogNode> nodes = new ArrayList<GeogNode>();
      for (String name : (Iterable<String>) geog.getLayerNames()) {
        String shortName = name.substring(0, name.lastIndexOf("."));
        shortName = shortName.substring(shortName.lastIndexOf(".") + 1, shortName.length());
        GeogNode layerNode = new GeogNode(name, shortName, true);
        nodes.add(layerNode);
      }

      Collections.sort(nodes);

      for (GeogNode layerNode : nodes) {
        node.add(layerNode);
      }
    }

    layerTree.setModel(model);
    layerTree.setRootVisible(false);
    expand();
  }

  public void expand() {
    TreeNode root = (TreeNode) layerTree.getModel().getRoot();
    // Traverse tree from root
    expandAll(layerTree, new TreePath(root), true);
  }

  private void expandAll(JTree tree, TreePath parent, boolean expand) {
    // Traverse children
    TreeNode node = (TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration e = node.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        expandAll(tree, path, expand);
      }
    }

    // Expansion or collapse must be done bottom-up
    if (expand) {
      tree.expandPath(parent);
    } else {
      tree.collapsePath(parent);
    }
  }

  private void gatherGeographies(Context context) {
    for (Geography geog : (Iterable<Geography>) context.getProjections(Geography.class)) {
      geogs.add(geog);
    }

    for (Context child : (Iterable<Context>) context.getSubContexts()) {
      gatherGeographies(child);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    dialogPane = new JPanel();
    contentPanel = new JPanel();
    panel1 = new JPanel();
    title1 = compFactory.createTitle("<html>Select the layer to export to a shapefile and<br>\nclick Save to export. Click finished when done.</html>");
    scrollPane1 = new JScrollPane();
    layerTree = new JTree();
    buttonBar = new JPanel();
    saveBtn = new JButton();
    finishedBtn = new JButton();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
    	dialogPane.setBorder(Borders.DIALOG);
    	dialogPane.setLayout(new BorderLayout());

    	//======== contentPanel ========
    	{
    		contentPanel.setLayout(new FormLayout(
    			ColumnSpec.decodeSpecs("default:grow"),
    			new RowSpec[] {
    				FormSpecs.DEFAULT_ROWSPEC,
    				FormSpecs.LINE_GAP_ROWSPEC,
    				new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
    			}));

    		//======== panel1 ========
    		{
    			panel1.setBackground(Color.white);
    			panel1.setBorder(new CompoundBorder(
    				LineBorder.createBlackLineBorder(),
    				new EmptyBorder(5, 5, 5, 5)));
    			panel1.setLayout(new BorderLayout());

    			//---- title1 ----
    			title1.setBackground(Color.white);
    			title1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
    			panel1.add(title1, BorderLayout.CENTER);
    		}
    		contentPanel.add(panel1, cc.xy(1, 1));

    		//======== scrollPane1 ========
    		{

    			//---- layerTree ----
    			layerTree.setVisibleRowCount(10);
    			scrollPane1.setViewportView(layerTree);
    		}
    		contentPanel.add(scrollPane1, cc.xy(1, 3));
    	}
    	dialogPane.add(contentPanel, BorderLayout.CENTER);

    	//======== buttonBar ========
    	{
    		buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
    		buttonBar.setLayout(new FormLayout(
    			new ColumnSpec[] {
    				FormSpecs.GLUE_COLSPEC,
    				FormSpecs.BUTTON_COLSPEC,
    				FormSpecs.RELATED_GAP_COLSPEC,
    				FormSpecs.BUTTON_COLSPEC
    			},
    			RowSpec.decodeSpecs("pref")));

    		//---- saveBtn ----
    		saveBtn.setText("Save");
    		saveBtn.setToolTipText("Click to save layer");
    		buttonBar.add(saveBtn, cc.xy(2, 1));

    		//---- finishedBtn ----
    		finishedBtn.setText("Finished");
    		buttonBar.add(finishedBtn, cc.xy(4, 1));
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
  private JPanel panel1;
  private JLabel title1;
  private JScrollPane scrollPane1;
  private JTree layerTree;
  private JPanel buttonBar;
  private JButton saveBtn;
  private JButton finishedBtn;
  // JFormDesigner - End of variables declaration  //GEN-END:variables


  private static class GeogNode extends DefaultMutableTreeNode implements Comparable<GeogNode> {

    private boolean isLayer = false;
    private String name;

    public GeogNode(Object userObject, String name, boolean layer) {
      super(userObject);
      this.name = name;
      isLayer = layer;
    }

    public boolean isLayer() {
      return isLayer;
    }

    public String toString() {
      return name;
    }

    public int compareTo(GeogNode node) {
      return name.compareTo(node.name);
    }
  }
}
