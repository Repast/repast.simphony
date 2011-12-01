package repast.simphony.visualization.editor;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ClassUtilities;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class FloatingList {

  private class CellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
      String title = probeForTitle(value);
      if (title == null) {
        title = value.getClass().getName();
        title = title.substring(title.lastIndexOf("."), title.length());
      }

      return super.getListCellRendererComponent(list, String.format("%-30s", title), index, isSelected, cellHasFocus);
    }

    private String probeForTitle(Object obj) {
      Method[] methods = ClassUtilities.findMethods(obj.getClass(), ProbeID.class);
      if (methods.length > 0) {
        Method method = methods[0];
        if (!method.getReturnType().equals(void.class) && method.getParameterTypes().length == 0) {
          try {
            return method.invoke(obj).toString();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        }
      }

      if (obj instanceof RepastEdge) {
        RepastEdge edge = (RepastEdge) obj;
        String source = probeForTitle(edge.getSource());
        if (source != null) {
          String target = probeForTitle(edge.getTarget());
          if (target != null) return edge.isDirected() ? source + " -> " + target : source + " <-> " + target;
        }
      }

      return null;

    }
  }

  private JList list;
  private JPopupMenu popup;
  private List<ActionListener> listeners = new ArrayList<ActionListener>();

  public FloatingList(Object... items) {
    list = new JList(items);
    list.setCellRenderer(new CellRenderer());
    list.setFont(list.getFont().deriveFont(10f));
    list.setVisibleRowCount(2);
    list.setSelectedIndex(0);
    list.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          popup.setVisible(false);
        }
      }
    });

    list.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          popup.setVisible(false);
        }
      }
    });
  }

  public void addActionListener(ActionListener listener) {
    listeners.add(listener);
  }

  public Object[] getSelectedItems() {
    return list.getSelectedValues();
  }

  public void show(JComponent comp, int x, int y) {
    popup = new JPopupMenu();
    popup.add(list);
    popup.addPopupMenuListener(new PopupMenuListener() {
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
      }

      public void popupMenuCanceled(PopupMenuEvent e) {
      }

      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        fireItemSelected();
      }
    });

    popup.show(comp, x, y);
    list.requestFocus();
  }

  private void fireItemSelected() {
    for (ActionListener listener : listeners) {
      listener.actionPerformed(new ActionEvent(list, 0, "itemSelected"));
    }
  }

  /*
  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final JPanel p = new JPanel();
    p.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          final FloatingList list = new FloatingList("Foo", "Bar", "Beh");
          list.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              System.out.println("list.getSelectedItem() = " + list.getSelectedItem());
            }
          });
          list.show(p, e.getX(), e.getY());
        }
      }
    });



    f.add(p);
    f.setSize(100, 100);
    f.setVisible(true);
  }
  */
}

