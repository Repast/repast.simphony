/**
 * 
 */
package repast.simphony.ui.parameters;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;

/**
 * @author Nick Collier
 */
public class ParametersUITest {
  
  private  interface Setter {
    
    void set(JComponent comp);
  }
  
  private static class TextSetter implements Setter {
    
    private String val;
    
    public TextSetter(String val) {
      this.val = val;
    }

    @Override
    public void set(JComponent comp) {
      comp.requestFocus();
      ((JTextComponent)comp).setText(val);
    }
  }
  
private static class ComboSetter implements Setter {
    
    private int index;
    
    public ComboSetter(int index) {
      this.index = index;
    }

    @Override
    public void set(JComponent comp) {
      comp.requestFocus();
      ((JComboBox<?>)comp).setSelectedIndex(index);
    }
  }

private static class BoolSetter implements Setter {
  
  private boolean val;
  
  public BoolSetter(boolean val) {
    this.val = val;
  }

  @Override
  public void set(JComponent comp) {
    comp.requestFocus();
    ((JCheckBox)comp).setSelected(val);
  }
}

  private Parameters params;
  private JPanel panel;
  private Map<String, Setter> setterMap = new HashMap<>();
  private Map<String, Object> expectedMap = new HashMap<>();
  
  public ParametersUITest() {
    setterMap.put("int", new TextSetter("5AMLES"));
    setterMap.put("float", new TextSetter("5.52f"));
    setterMap.put("long", new TextSetter("25234"));
    setterMap.put("double", new TextSetter("12.23434"));
    setterMap.put("c_double", new ComboSetter(3));
    setterMap.put("string", new ComboSetter(3));
    setterMap.put("A_string", new TextSetter("foo"));
    setterMap.put("bool", new BoolSetter(false));
    setterMap.put("size", new TextSetter("SMALL"));
    
    expectedMap.put("int", Integer.valueOf(5));
    expectedMap.put("float", Float.valueOf(5.52f));
    expectedMap.put("long", Long.valueOf(25234L));
    expectedMap.put("double", Double.valueOf(12.23434));
    expectedMap.put("c_double", Double.valueOf(2.1));
    expectedMap.put("string", "Bob");
    expectedMap.put("A_string", "foo");
    expectedMap.put("bool", Boolean.FALSE);
    expectedMap.put("size", Size.SMALL);
  }

  private JButton getButton() {
    JButton runBtn = new JButton("Run");
    runBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        updateComponents();
      }
    });

    return runBtn;
  }
  
  private void doTest() {
    for (String name : expectedMap.keySet()) {
      Object exp = expectedMap.get(name);
      Object found = params.getValue(name);
      if (!exp.equals(found)) {
        System.out.printf("%s != %s%n", exp, found);
      }
    }
  }

  private void updateComponents() {

    try {
      JScrollPane container = (JScrollPane) panel.getComponent(0);
      //System.out.println(container);
      JPanel panel = (JPanel) container.getViewport().getView();
      for (int i = 0; i < panel.getComponentCount(); ++i) {
        JComponent comp = (JComponent) panel.getComponent(i);
        Object name = comp.getClientProperty("name");
        if (name != null) {
          Setter setter = setterMap.get(name);
          if (setter != null) setter.set(comp);
         
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  

  public void run() {
    try {
      ParametersParser parser = new ParametersParser(new File("./test_data/test_params.xml"));
      params = parser.getParameters();
      ParametersUI pui = new ParametersUI(params);
      panel = pui.createPanel(new File("./test_data/test_params.xml"));

      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {

        @Override
        public void windowClosed(WindowEvent e) {
          doTest();
        }
      });

      frame.setLayout(new BorderLayout());
      frame.add(panel, BorderLayout.CENTER);
      frame.add(getButton(), BorderLayout.NORTH);
      frame.pack();
      frame.setVisible(true);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new ParametersUITest().run();
      }
    });
  }

}
