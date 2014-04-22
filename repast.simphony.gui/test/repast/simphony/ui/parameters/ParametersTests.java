package repast.simphony.ui.parameters;

import static org.junit.Assert.assertEquals;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.ui.probe.ZeroNumberFormat;

import com.jgoodies.binding.adapter.BasicComponentFactory;

public class ParametersTests {
 
  private Parameters params;

  @Before
  public void setup() {
    ParametersCreator creator = new ParametersCreator();
    creator.addParameter("age", "Age", int.class, 3, false);
    creator.addParameter("name", "Name", String.class, "Nick", false);
    creator.addParameter("bool", "A Bool", boolean.class, true, false);
    creator.addParameter("size", "Size", Size.class, Size.SMALL, false);
    creator.addConvertor("size", new SizeConverter());
    
    params = creator.createParameters();   
  }

  public void run() {
    setup();
    ParameterValueModel model = new ParameterValueModel("age", params);
    JFormattedTextField fld = BasicComponentFactory.createIntegerField(model, new ZeroNumberFormat(
        NumberFormat.getIntegerInstance()));
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosed(WindowEvent e) {
        System.out.println(params.getValue("age"));
      }
      
    });
  
    frame.add(fld);
    frame.setSize(400, 80);
    frame.setVisible(true);
  }
  
  @Test
  public void testToParameter() {
    DefaultParameterBinder binder = new DefaultParameterBinder("age", "Age", int.class);
    JComponent comp = binder.getComponent(params);
    ((JTextComponent)comp).setText("10.2derafdf21");
    binder.toParameter();
    assertEquals(Integer.valueOf(10), params.getInteger("age"));
    
    binder = new DefaultParameterBinder("name", "Name", String.class);
    JTextField fld = (JTextField) binder.getComponent(params);
    fld.setText("Bill");
    binder.toParameter();
    assertEquals("Bill", params.getString("name"));
    
  }
  
  @Test
  public void testConverter() {
    assertEquals("SMALL", params.getValueAsString("size"));
    params.setValue("size", "BIG");
    assertEquals(Size.BIG, params.getValue("size"));
  }

  @Test
  public void testValueModel() {
    ParameterValueModel model = new ParameterValueModel("age", params);
    JFormattedTextField fld = BasicComponentFactory.createIntegerField(model, new ZeroNumberFormat(
        NumberFormat.getIntegerInstance()));
    assertEquals("3", fld.getText());

    model.setValue(10);
    assertEquals("10", fld.getText());
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new ParametersTests().run();
      }
    });
  }
}
