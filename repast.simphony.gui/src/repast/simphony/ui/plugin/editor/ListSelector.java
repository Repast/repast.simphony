package repast.simphony.ui.plugin.editor;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GUI widget that allows the user to select items from a list into a new list. Layout is two lists
 * with buttons in between for moving items back and forth between the lists.
 *
 * @author Nick Collier
 */
public class ListSelector<T> extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final MessageCenter LOG = MessageCenter.getMessageCenter(ListSelector.class);

  private static boolean iconsLoaded = false;
  private static Icon upIcon;// = new ImageIcon(getClass().getClassLoader().getResource("up.png"));
  private static Icon downIcon;// = new ImageIcon(getClass().getClassLoader().getResource("down.png"));
  private static Icon addIcon;// = new ImageIcon(getClass().getClassLoader().getResource("forward.png"));
  private static Icon removeIcon;// = new ImageIcon(getClass().getClassLoader().getResource("back.png"));

  private JList source;
  private JList target;
  private JButton addBtn, upBtn, removeBtn, downBtn;
  private java.util.List<ActionListener> listeners = new ArrayList<ActionListener>();

  public ListSelector() {
    this(null, new ArrayList<T>(0), new ArrayList<T>(0), true);
  }

  /**
   * Creates  list selector from the specified lists.
   *
   * @param sourceList contains the items to put in the source left-hand side list
   * @param targetList contains the items to put in the target right-hand side list.
   * @param orderButtons boolean for using order buttons in the target list.
   */
  public ListSelector(List<T> sourceList, List<T> targetList, boolean orderButtons) {
    this(null, sourceList, targetList, orderButtons);
  }

  public void setLists(List<T> sourceList, List<T> targetList) {

    DefaultListModel model = new DefaultListModel();
    for (Object item : sourceList) {
      model.addElement(item);
    }
    source.setModel(model);
    model = new DefaultListModel();
    for (Object item : targetList) {
      model.addElement(item);
    }

    target.setModel(model);
    source.setPrototypeCellValue("xxxxxxxxxxx");
    target.setPrototypeCellValue("xxxxxxxxxxx");
    source.setVisibleRowCount(7);
    target.setVisibleRowCount(7);
  }
  
  public DefaultListModel getTargetListModel() {
    return (DefaultListModel) target.getModel();
  }
  
  public DefaultListModel getSourceListModel() {
    return (DefaultListModel) source.getModel();
  }

  private void createLists(List<T> sourceList, List<T> targetList) {
    DefaultListModel model = new DefaultListModel();
    for (Object item : sourceList) {
      model.addElement(item);
    }
    source = new JList(model);
    model = new DefaultListModel();
    for (Object item : targetList) {
      model.addElement(item);
    }
    target = new JList(model);
    source.setPrototypeCellValue("xxxxxxxxxxx");
    target.setPrototypeCellValue("xxxxxxxxxxx");
    source.setVisibleRowCount(7);
    target.setVisibleRowCount(7);
  }

  /**
   * Creates  list selector from the specified lists.
   *
   * @param title      a title to display above the lists
   * @param sourceList contains the items to put in the source left-hand side list
   * @param targetList contains the items to put in the target right-hand side list.
   * @param orderButtons boolean for using order buttons in the target list.
   */
  public ListSelector(String title, List<T> sourceList, List<T> targetList, boolean orderButtons) {
    initButtonImages();

    CellConstraints cc = new CellConstraints();
    String widthInfo = (title == null ? "pref, 3dlu, pref, 3dlu, pref:grow"
            : "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref:grow, 3dlu, pref, pref:grow");
    FormLayout layout = new FormLayout("pref:grow, 3dlu, pref, 3dlu, pref:grow, 3dlu, pref",
            widthInfo);
    PanelBuilder builder = new PanelBuilder(layout, this);
    //builder.setDefaultDialogBorder();
    if (title != null) {
      builder.addSeparator(title, cc.xyw(1, 1, 7));
    }
    createLists(sourceList, targetList);

    int listBase = (title == null ? 1 : 3);

    JScrollPane pane = new JScrollPane(source);
    int height = pane.getPreferredSize().height + 10;
    pane.setPreferredSize(new Dimension(100, height));
    builder.add(pane, cc.xywh(1, listBase, 1, 5));

    pane = new JScrollPane(target);
    pane.setPreferredSize(new Dimension(100, height));
    builder.add(pane, cc.xywh(5, listBase, 1, 5));

    createButtons();
    int buttonBase = (title == null ? 1 : 5);

    builder.add(addBtn, cc.xy(3, buttonBase));
    builder.add(removeBtn, cc.xy(3, buttonBase + 2));
    
    if (orderButtons){
    	builder.add(upBtn, cc.xy(7, buttonBase));
    	downBtn.setEnabled(false);
    	builder.add(downBtn, cc.xy(7, buttonBase + 2));
    }
    addListeners();
  }

  private void initButtonImages() {
    if (iconsLoaded) {
      return;
    }
    try {
      iconsLoaded = true;
      upIcon = new ImageIcon(getClass().getClassLoader().getResource("up.png"));
      downIcon = new ImageIcon(getClass().getClassLoader().getResource("down.png"));
      addIcon = new ImageIcon(getClass().getClassLoader().getResource("forward.png"));
      removeIcon = new ImageIcon(getClass().getClassLoader().getResource("back.png"));
    } catch (RuntimeException ex) {
      upIcon = null;
      downIcon = null;
      addIcon = null;
      removeIcon = null;
      LOG.warn("Error loading button icons, continuing with empty images.", ex);
    }
  }

  public void addActionListeners(ActionListener listener) {
    listeners.add(listener);
  }

  private void createButtons() {
    Dimension d = new Dimension(24, 24);
    addBtn = new JButton();
    if (addIcon != null) {
      addBtn.setIcon(addIcon);
    } else {
      addBtn.setText(">");
    }
    addBtn.setMaximumSize(d);
    addBtn.setMinimumSize(d);
    addBtn.setPreferredSize(d);
    removeBtn = new JButton(removeIcon);
    if (removeIcon != null) {
      removeBtn.setIcon(removeIcon);
    } else {
      removeBtn.setText("<");
    }
    removeBtn.setEnabled(false);
    removeBtn.setMaximumSize(d);
    removeBtn.setMinimumSize(d);
    removeBtn.setPreferredSize(d);
    upBtn = new JButton();
    if (upIcon != null) {
      upBtn.setIcon(upIcon);
    } else {
      upBtn.setText("^");
    }
    upBtn.setEnabled(false);
    upBtn.setMaximumSize(d);
    upBtn.setMinimumSize(d);
    upBtn.setPreferredSize(d);
    downBtn = new JButton();
    if (downIcon != null) {
      downBtn.setIcon(downIcon);
    } else {
      downBtn.setText("v");
    }
    downBtn.setEnabled(false);
    downBtn.setMaximumSize(d);
    downBtn.setMinimumSize(d);
    downBtn.setPreferredSize(d);
  }

  private void addListeners() {
    source.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        addBtn.setEnabled(source.getSelectedValues().length > 0);
        target.setSelectedIndex(-1);
      }
    });

    target.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        removeBtn.setEnabled(target.getSelectedValues().length > 0);
        upBtn.setEnabled(target.getSelectedIndex() > 0);
        downBtn.setEnabled(target.getSelectedIndex() > -1 &&
                target.getSelectedIndex() < target.getModel().getSize() - 1);
        source.setSelectedIndex(-1);
      }
    });

    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Object[] objs = source.getSelectedValues();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        DefaultListModel sModel = (DefaultListModel) source.getModel();
        for (Object obj : objs) {
          sModel.removeElement(obj);
          tModel.addElement(obj);
        }
        if (sModel.size() > 0) source.setSelectedIndex(0);
        for (ActionListener listener : listeners) {
          listener.actionPerformed(evt);
        }
      }
    });

    removeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Object[] objs = target.getSelectedValues();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        DefaultListModel sModel = (DefaultListModel) source.getModel();
        for (Object obj : objs) {
          tModel.removeElement(obj);
          sModel.addElement(obj);
        }
        if (tModel.size() > 0) target.setSelectedIndex(0);
        for (ActionListener listener : listeners) {
          listener.actionPerformed(evt);
        }
      }
    });

    upBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int index = target.getSelectedIndex();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        Object obj = tModel.remove(index);
        tModel.add(index - 1, obj);
        target.setSelectedIndex(index - 1);
      }
    });

    downBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        int index = target.getSelectedIndex();
        DefaultListModel tModel = (DefaultListModel) target.getModel();
        Object obj = tModel.remove(index);
        tModel.add(index + 1, obj);
        target.setSelectedIndex(index + 1);
      }
    });
    boolean sourceSize = source.getModel().getSize() > 0;
    if (sourceSize) {
      source.setSelectedIndex(0);
    }
    addBtn.setEnabled(sourceSize);
  }

  /**
   * Gets the list of items the user has selected. These items will be whatever is in the
   * right hand target list when this method is called.
   *
   * @return the list of items the user has selected.
   */
  public List<T> getSelectedItems() {
    List<T> list = new ArrayList<T>();
    DefaultListModel model = (DefaultListModel) target.getModel();
    for (int i = 0; i < model.size(); i++) {
      list.add((T) model.get(i));
    }
    return list;
  }

  /**
   * Gets the panel containing the widget.
   *
   * @return the panel containing the widget.
   */
  public JPanel getPanel() {
    return this;
  }

  public static void main(String[] args) {
    String[] str = new String[]{"Nick", "Caitrin", "Cormac", "Nicola", "Happiness"};
    ListSelector<String> sel = new ListSelector<String>();
    sel.setLists(Arrays.asList(str), new ArrayList<String>());
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(sel.getPanel());
    frame.pack();
    frame.setVisible(true);
	}
}
