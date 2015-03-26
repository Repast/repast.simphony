package repast.simphony.ui.plugin.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Generic options dialog. Once the dialog is created content can be added to it
 * with <code>addContent</code>
 * 
 * @author Nick Collier
 */
public class OptionsDialog extends JPanel implements ChangeListener {
  private static final long serialVersionUID = 8778748428311162266L;
  
  // The standard screen dimensions of the dialogs.  Note these are a bit larger
  //  than the dimensions for the PluginWizardStep to account for the tabs.
	public static int DIALOG_WIDTH = 650;
	public static int DIALOG_HEIGHT = 450;
  
  private static MessageCenter msgCenter = MessageCenter.getMessageCenter(OptionsDialog.class);

  private static final String CONTENT = "content";

  private HashSet<OptionsDialogContent> dirtyContent;

  protected JDialog dialog;
  private OptionsDialogContent content;
  private boolean canceled = false;
  private JPanel contentPanel = new JPanel(new BorderLayout());
  private JTabbedPane contentTabs = new JTabbedPane();
  private int currentTab = 0;

  public OptionsDialog() {
    super(new BorderLayout());

    this.dirtyContent = new HashSet<OptionsDialogContent>();

    //bar.setPreferredSize(new Dimension(130, 400));
    setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    //add(bar, BorderLayout.WEST);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
    contentPanel.add(contentTabs, BorderLayout.CENTER);
    add(contentPanel, BorderLayout.CENTER);
    contentTabs.setTabPlacement(JTabbedPane.LEFT);
    createButtons();
    
    List<OptionsDialogContent> list = new ArrayList<OptionsDialogContent>();
    contentTabs.putClientProperty(CONTENT, list);
  }

  /**
   * Adds the specified content as an option in this dialog.
   * 
   * @param name
   *          the name of the content to display
   * @param icon
   *          the icon
   * @param content
   *          the content
   */
  @SuppressWarnings("unchecked")
  public void addContent(String name, Icon icon, OptionsDialogContent content) {
    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    p.add(content.getPanel(), BorderLayout.CENTER);
    contentTabs.addTab(name, p);
    contentTabs.addChangeListener(this);
    List<OptionsDialogContent> list = (List<OptionsDialogContent>) contentTabs.getClientProperty(CONTENT);
    list.add(content);
  }

  /**
   * @return true if the dialog was canceled, otherwise false.
   */
  public boolean isCanceled() {
    return canceled;
  }

  private void createButtons() {
    JButton okBtn = new JButton("OK");
    JButton applyBtn = new JButton("Apply All");
    JButton cancelBtn = new JButton("Cancel");
    okBtn.setMnemonic('o');
    okBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ok();
      }
    });

    applyBtn.setMnemonic('a');
    applyBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        apply();
      }
    });

    cancelBtn.setMnemonic('c');
    cancelBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        cancel();
      }
    });

    JPanel panel = new JPanel();
    BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
    panel.setLayout(layout);
    panel.add(Box.createHorizontalGlue());
    panel.add(okBtn);
    panel.add(Box.createRigidArea(new Dimension(6, 0)));
    panel.add(applyBtn);
    panel.add(Box.createRigidArea(new Dimension(6, 0)));
    panel.add(cancelBtn);

    FormLayout dlgLayout = new FormLayout("fill:p:grow", "pref, 3dlu, pref");

    PanelBuilder builder = new PanelBuilder(dlgLayout);
    builder.setBorder(BorderFactory.createEmptyBorder(8, 0, 6, 0));
    CellConstraints cc = new CellConstraints();
    builder.addSeparator("", cc.xy(1, 1));
    builder.add(panel, cc.xy(1, 3));

    add(builder.getPanel(), BorderLayout.SOUTH);
  }

  protected void ok() {
    try {
      for (OptionsDialogContent content : dirtyContent) {
        content.ok();
      }
      dirtyContent.clear();
      content.ok();
    } catch (InvalidStateException e) {
      msgCenter.error("Error ok'ing content in Options Dialog", e);
    }
    dialog.dispose();
  }

  protected void apply() {
    try {
      for (OptionsDialogContent content : dirtyContent) {
        content.apply();
      }
      dirtyContent.clear();
      content.apply();
    } catch (InvalidStateException e) {
      msgCenter.error("Error applying content in Options Dialog", e);
    }
  }

  protected void cancel() {
    for (OptionsDialogContent content : dirtyContent) {
      content.cancel();
    }
    dirtyContent.clear();
    content.cancel();
    dialog.dispose();
    canceled = true;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void stateChanged(ChangeEvent evt) {
    int selectedTab = contentTabs.getSelectedIndex();
    if (selectedTab != currentTab) {
      if (content != null) {
        dirtyContent.add(content);
      }

      List<OptionsDialogContent> list = (List<OptionsDialogContent>) contentTabs.getClientProperty(CONTENT);
      content = list.get(selectedTab);
      currentTab = selectedTab;
      if (!dirtyContent.contains(content)) {
        // prevent the changes from being discarded
        content.selected();
      }
    }
  }

  /**
   * Invoked when one of the option buttons is clicked.
   * 
   * @param e
   */
  public void actionPerformed(ActionEvent e) {
    JComponent comp = (JComponent) e.getSource();
    if (content != null) {
      dirtyContent.add(content);
    }

    content = (OptionsDialogContent) comp.getClientProperty(CONTENT);
    if (!dirtyContent.contains(content)) {
      // prevent the changes from being discarded
      content.selected();
    }
    contentPanel.removeAll();
    contentPanel.add(content.getPanel(), BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  /**
   * Initializes the dialog for display. This sets the layout, packs() it and so
   * forth.
   * 
   * @param parent
   */
  protected void displayInit(Component parent) {
    dialog.setLayout(new BorderLayout());
    dialog.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
    dialog.add(this, BorderLayout.CENTER);
    dialog.pack();
    dialog.setLocationRelativeTo(parent);
    contentTabs.setSelectedIndex(0);
   
    // Initialize the first OptionsDialogContent so it displays properly 
    List<OptionsDialogContent> list = (List<OptionsDialogContent>) contentTabs.getClientProperty(CONTENT);
    list.get(0).selected();
    content = list.get(0);
  }

  public void showDialog(JFrame parent, String title) {
    if (dialog == null) {
      dialog = new JDialog(parent, title, true);
      displayInit(parent);
    }
    dialog.setVisible(true);
  }

  public void showDialog(JDialog parent, String title) {
    if (dialog == null) {
      dialog = new JDialog(parent, title, true);
      displayInit(parent);
    }
    dialog.setVisible(true);
  }
}
