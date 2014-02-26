package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import repast.simphony.batch.gui.Host.Type;
import repast.simphony.batch.ssh.Configuration;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class HostsPanel extends JPanel implements BatchRunPanel {

  private JTextField userFld = new JTextField(10), hostsFld = new JTextField(10), 
      sshKeyFileFld = new JTextField("id_rsa", 10);
  private JPanel propsPanel;
  private JLabel userLbl, hostLbl, sshKeyFileLbl, typeLbl, instancesLbl;
  private JSplitPane splitPane;
  private JButton addBtn, deleteBtn, copyBtn;
  private JList hostList;
  private BufferedValueModel hostsModel;
  private JComboBox typeBox;
  private JSpinner instancesSpn;

  private Host selectedHost = null;

  /**
   * Create the panel.
   * @param adapter 
   */
  public HostsPanel(PresentationModel<BatchRunConfigBean> pModel) {
    setLayout(new BorderLayout(0, 0));
    addComponents();
    addListeners();
    hostsModel = pModel.getBufferedModel("hosts");
  }
  
  private JButton createButton(String icon, String tooltip) {
    JButton btn = new JButton(IconLoader.loadIcon(icon));
    btn.setToolTipText(tooltip);
    btn.setBorderPainted(false);
    btn.setPreferredSize(new Dimension(22, 18));
    btn.setMaximumSize(btn.getPreferredSize());
    btn.setMinimumSize(btn.getPreferredSize());
    return btn;
  }

  private void addComponents() {

    FormLayout layout = new FormLayout("pref:grow", "center:pref, 3dlu, fill:pref:grow");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.addSeparator("Hosts");
    formBuilder.nextLine(2);

    splitPane = new JSplitPane();
    splitPane.setResizeWeight(0.2);
    formBuilder.add(splitPane);
    add(formBuilder.getPanel(), BorderLayout.CENTER);

    JPanel panel = new JPanel();
    splitPane.setLeftComponent(panel);
    panel.setLayout(new BorderLayout(0, 0));

    JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(false);
    panel.add(toolBar, BorderLayout.NORTH);

    addBtn = createButton("new_con.gif", "Add a Host");
    toolBar.add(addBtn);
    
    copyBtn = createButton("copy_edit_co.gif", "Copy Selected Host");
    toolBar.add(copyBtn);
    copyBtn.setEnabled(false);

    deleteBtn = createButton("rem_co.gif", "Delete Selected Host(s)");
    toolBar.add(deleteBtn);
    deleteBtn.setEnabled(false);

    //toolBar.addSeparator();
    //loadBtn = createButton("prj_obj.gif", "Load hosts from exiting configuration file");
    //toolBar.add(loadBtn);

    JScrollPane scrollPane = new JScrollPane();
    panel.add(scrollPane, BorderLayout.CENTER);

    hostList = new JList();
    hostList.setCellRenderer(new ItemRenderer());
    scrollPane.setViewportView(hostList);
    
    layout = new FormLayout("5dlu, left:pref, 3dlu, pref:grow", "");
    formBuilder = new DefaultFormBuilder(layout);
    formBuilder.setDefaultDialogBorder();
    formBuilder.appendSeparator("Host Properties");
    formBuilder.nextLine();
    formBuilder.setLeadingColumnOffset(1);
    typeBox = new JComboBox(new Object[] { Host.Type.LOCAL, Host.Type.REMOTE });
    typeLbl = formBuilder.append("Type:", typeBox);
    
    formBuilder.nextLine();
    userLbl = formBuilder.append("User:", userFld);
    formBuilder.nextLine();
    hostLbl = formBuilder.append("Host (Name or Address):", hostsFld);
    formBuilder.nextLine();
    sshKeyFileLbl = formBuilder.append("SSH Key File:", sshKeyFileFld);
    formBuilder.nextLine();
    instancesSpn = new JSpinner();
    instancesSpn.setModel(new SpinnerNumberModel(1, 1, 1000000, 1));
    instancesLbl = formBuilder.append("Instances:", instancesSpn);
    
    propsPanel = formBuilder.getPanel();
    splitPane.setRightComponent(propsPanel);
    
    setPropsEnabled(false);
  }
  
  private void notifyHostsModel() {
    List<Host> hosts = new ArrayList<Host>();
    DefaultListModel listModel = (DefaultListModel) hostList.getModel();
    for (int i = 0; i < listModel.getSize(); i++) {
      hosts.add((Host) listModel.elementAt(i));
    }
    hostsModel.setValue(hosts);
  }

  private void addListeners() {
    hostList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
          deleteBtn.setEnabled(hostList.getSelectedIndex() != -1);
          copyBtn.setEnabled(hostList.getSelectedIndex() != -1);
          updateHost();
          enableFields(hostList.getSelectedIndex() != -1);
        }
      }
    });

    typeBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (typeBox.getSelectedIndex() != -1)
          enableForType((Type) typeBox.getSelectedItem());
      }
    });

    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        addHost();
        notifyHostsModel();
      }
    });

    deleteBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        deleteHost();
        notifyHostsModel();
      }
    });

    copyBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        copyHost();
        notifyHostsModel();
      }
    });
  }

  private void deleteHost() {
    Object[] objs = hostList.getSelectedValues();
    int retVal = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete the selected host(s)?", "Delete Host(s)?",
        JOptionPane.YES_NO_OPTION);
    if (retVal == JOptionPane.YES_OPTION) {
      DefaultListModel model = (DefaultListModel) hostList.getModel();
      int index = 0;
      for (Object obj : objs) {
        index = model.indexOf(obj);
        model.removeElementAt(index);
      }
      
      if (model.size() > 0) {
        if (index == 0)
          hostList.setSelectedIndex(0);
        else
          hostList.setSelectedIndex(index - 1);
      } else {
        hostList.setSelectedIndex(-1);
      }
    }
  }

  private void addHost() {
    commitCurrent();
    Host host = new Host(Type.REMOTE);
    host.setInstances(2);
    host.setUser("user");
    host.setHost("remotehost");
    DefaultListModel model = (DefaultListModel) hostList.getModel();
    model.addElement(host);
    hostList.setSelectedIndex(model.getSize() - 1);
  }

  private void copyHost() {
    commitCurrent();
    Host source = (Host) hostList.getSelectedValue();
    Host target = new Host(source);
    DefaultListModel model = (DefaultListModel) hostList.getModel();
    model.addElement(target);
    hostList.setSelectedIndex(model.getSize() - 1);
  }
  
  

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#validateInput()
   */
  @Override
  public ValidationResult validateInput() {
    if (hostList.getModel().getSize() == 0) return new ValidationResult("Hosts list is empty.");
    return ValidationResult.SUCCESS;
  }

  private void commitCurrent() {
    if (selectedHost != null) {
      selectedHost.setType((Type) typeBox.getSelectedItem());
      selectedHost.setUser(userFld.getText().trim());
      selectedHost.setHost(hostsFld.getText().trim());
      selectedHost.setInstances((Integer) instancesSpn.getValue());
      selectedHost.setSSHKeyFile(sshKeyFileFld.getText().trim());
      notifyHostsModel();
    }
  }

  private void updateHost() {
    commitCurrent();

    selectedHost = (Host) hostList.getSelectedValue();
    if (selectedHost != null) {
      userFld.setText(selectedHost.getUser());
      hostsFld.setText(selectedHost.getHost());
      sshKeyFileFld.setText(selectedHost.getSSHKeyFile());
      typeBox.setSelectedItem(selectedHost.getType());
      instancesSpn.setValue(new Integer(selectedHost.getInstances()));
      enableForType(selectedHost.getType());
    } else {
      userFld.setText("");
      hostsFld.setText("");
      sshKeyFileFld.setText("");
      typeBox.setSelectedIndex(-1);
      instancesSpn.setValue(new Integer(1));
    }
  }
  
  private void enableFields(boolean enabled) {
    userFld.setEnabled(enabled);
    hostsFld.setEnabled(enabled);
    sshKeyFileFld.setEnabled(enabled);
    instancesSpn.setEnabled(enabled);
    typeBox.setEnabled(enabled);
    
    userLbl.setEnabled(enabled);
    hostLbl.setEnabled(enabled);
    sshKeyFileLbl.setEnabled(enabled);
    typeLbl.setEnabled(enabled);
    instancesLbl.setEnabled(enabled);
  }

  private void enableForType(Host.Type type) {
    setPropsEnabled(true);
    if (type == Host.Type.LOCAL) {
      userFld.setText(System.getProperty("user.name"));
      hostsFld.setText("localhost");
      userFld.setEnabled(false);
      hostsFld.setEnabled(false);
      userLbl.setEnabled(false);
      hostLbl.setEnabled(false);
      sshKeyFileLbl.setEnabled(false);
      sshKeyFileFld.setEnabled(false);
      sshKeyFileFld.setText("");
    } else {
      sshKeyFileFld.setText("id_rsa");
    }
  }

  private void setPropsEnabled(boolean enabled) {
    for (int i = 0; i < propsPanel.getComponentCount(); i++) {
      propsPanel.getComponent(i).setEnabled(enabled);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.
   * BatchRunModel)
   */
  @Override
  public void init(BatchRunConfigBean model) {
    splitPane.setDividerLocation(175);
    List<Host> hosts = model.getHosts();
    DefaultListModel listModel = new DefaultListModel();
    for (Host host : hosts) {
      listModel.addElement(host);
    }
    hostList.setModel(listModel);
    if (listModel.size() > 0)
      hostList.setSelectedIndex(0);

  }

  public void writeHosts(Writer writer) throws IOException {
    DefaultListModel listModel = (DefaultListModel) hostList.getModel();
    for (int i = 0; i < listModel.getSize(); i++) {
      Host host = (Host) listModel.elementAt(i);
      if (host.getType() == Type.LOCAL) {
        writer.write(Configuration.LOCAL_PREFIX + "." + i + "." + Configuration.SESSION_INSTANCES + 
            " = " + host.getInstances() + "\n");

        writer.write(Configuration.LOCAL_PREFIX + "." + i + ".working_directory = "
            + System.getProperty("java.io.tmpdir", "model_run").replace("\\", "/") + "\n");

      } else {
        String prefix = Configuration.REMOTE_PREFIX + "." + i + ".";
        writer.write(prefix + Configuration.SESSION_USER + " = " + host.getUser() + "\n");
        writer.write(prefix + Configuration.SESSION_HOST + " = " + host.getHost() + "\n");
        writer.write(prefix + Configuration.SESSION_INSTANCES + " = " + host.getInstances() + "\n");
        writer.write(prefix + Configuration.SESSION_KEY_FILE + " = " + host.getSSHKeyFile().replace("\\", "/") + "\n");
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.batch.gui.BatchRunPanel#commit(repast.simphony.batch.gui
   * .BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunConfigBean model) {
    commitCurrent();
    List<Host> hosts = new ArrayList<Host>();
    DefaultListModel listModel = (DefaultListModel) hostList.getModel();
    for (int i = 0; i < listModel.getSize(); i++) {
      hosts.add((Host) listModel.elementAt(i));
    }

    model.setHosts(hosts);
    return CommitResult.SUCCESS;
  }

  static class ItemRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
        boolean arg4) {
      if (arg1 != null) {
        Host host = (Host) arg1;
        arg1 = host.getUser() + "@" + host.getHost();
      }
      return super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
    }

  }
}
