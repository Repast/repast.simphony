/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {

  private BatchConfigMediator mediator;

  public MainPanel(File modelDirectory) {
    super(new BorderLayout());
    mediator = new BatchConfigMediator(modelDirectory);
    init();
  }

  private void init() {
    createToolBar();
    add(mediator.createTabs(), BorderLayout.CENTER);
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 5));
    panel.add(mediator.getStatusBar(), BorderLayout.CENTER);
    add(panel, BorderLayout.SOUTH);

  }

  public MainPanel() {
    super(new BorderLayout());
    mediator = new BatchConfigMediator();
    init();
  }
  
  public void onExit() {
    mediator.onExit();
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

  private void createToolBar() {
    JToolBar bar = new JToolBar();
    bar.setFloatable(false);

    JButton newBtn = createButton("newprj_wiz.gif", "New Batch Configuration");
    newBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.newConfig();
      }
    });
    bar.add(newBtn);
    bar.addSeparator();

    JButton openBtn = createButton("prj_obj.gif", "Open Batch Configuration");
    openBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.openConfig();
      }
    });
    bar.add(openBtn);

    JButton saveBtn = createButton("save_edit.gif", "Save Batch Configuration");
    saveBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.saveConfig();
      }
    });
    bar.add(saveBtn);

    JButton saveAsBtn = createButton("saveas_edit.gif", "Save Batch Configuration As");
    saveAsBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.saveConfigAs();
      }
    });
    bar.add(saveAsBtn);

    bar.addSeparator();
    JButton updateBtn = createButton("reset.gif", "Update All Input from Model Project");
    updateBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.updateFromModel();
      }
    });
    bar.add(updateBtn);

    bar.addSeparator();
    JButton generateBtn = createButton("refresh_tab.gif", "Create Model Archive for Batch Runs");
    generateBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.createArchive();
      }
    });
    bar.add(generateBtn);

    JButton runBtn = createButton("lrun_obj.gif", "Execute Batch Runs");
    runBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        mediator.run();
      }
    });
    bar.add(runBtn);

    add(bar, BorderLayout.NORTH);
  }

}
