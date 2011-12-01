/*
 * Created by JFormDesigner on Tue May 30 08:53:13 CDT 2006
 */

package repast.simphony.gis.tools;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.RendererUtilities;
import org.mc4j.console.swing.LogarithmicJSlider;
import org.mc4j.console.swing.LogarithmicJSlider.LogSliderUI;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import repast.simphony.gis.display.PiccoloMapPanel;
import simphony.util.messages.MessageCenter;
import simphony.util.messages.MessageEvent;
import simphony.util.messages.MessageEventListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * @author User #1
 */
public class ZoomSlider extends JPanel {
  private PiccoloMapPanel panel;

  MapContext context;

  private boolean externalChange;
  private boolean internalChange = false;

  public ZoomSlider(PiccoloMapPanel panel, MapContext context) {
    this.context = context;
    this.panel = panel;
    initComponents();
    try {
      int scaleDenominator = (int) RendererUtilities.calculateScale(
              context.getAreaOfInterest(), panel.getCanvas().getWidth(),
              panel.getCanvas().getHeight(), 90);
      logarithmicJSlider1.setValue(scaleDenominator);
    } catch (Exception e) {
      e.printStackTrace();
    }
    MessageEventListener listener = new MessageEventListener() {

      public void messageReceived(MessageEvent arg0) {
        if (arg0.getMessage() instanceof ScaleDenominatorChanged) {
          externalChange = true;
          if (!internalChange) {
            ScaleDenominatorChanged changed = (ScaleDenominatorChanged) arg0
                    .getMessage();
            textField1.setText("1:"
                    + (int) changed.getScaleDenomintor());
            logarithmicJSlider1.setValue((int) changed
                    .getScaleDenomintor());
          }
          externalChange = false;
        }

      }

    };
    MessageCenter.addMessageListener(listener);
  }

  private void zoomChanged(ChangeEvent e) {
    if (externalChange) {
      externalChange = false;
      return;
    }
    int zoomValue = logarithmicJSlider1.getValue();
    textField1.setText("1:" + zoomValue);
    if (logarithmicJSlider1.getValueIsAdjusting())
      return;
    try {
      Envelope newEnv = ScaleUtil.calcEnvelope(context
              .getAreaOfInterest(), context
              .getCoordinateReferenceSystem(), zoomValue, panel
              .getCanvas().getWidth(), panel.getCanvas().getHeight());
      context.setAreaOfInterest(newEnv);
      panel.getCanvas().zoomToAreaOfInterest();
    } catch (FactoryException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (TransformException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }

  private void zoomOut(MouseEvent e) {
    LogSliderUI ui = (LogSliderUI) logarithmicJSlider1.getUI();
    int value = logarithmicJSlider1.getValue();
    int position = ui.xPositionForValue(value);
    position--;
    value = ui.valueForXPosition(position);
    internalChange = true;
    logarithmicJSlider1.setValue(value);
    internalChange = false;
  }

  private void zoomIn(MouseEvent e) {
    LogSliderUI ui = (LogSliderUI) logarithmicJSlider1.getUI();
    int value = logarithmicJSlider1.getValue();
    int position = ui.xPositionForValue(value);
    position++;
    value = ui.valueForXPosition(position);
    internalChange = true;
    logarithmicJSlider1.setValue(value);
    internalChange = false;
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    button1 = new JLabel();
    URL imageFile = getClass().getResource("Minus.png");
    Image image = Toolkit.getDefaultToolkit().getImage(imageFile);
    button1.setIcon(new ImageIcon(image));

    logarithmicJSlider1 = new LogarithmicJSlider();
    logarithmicJSlider1.setInverted(true);
    button2 = new JLabel();
    imageFile = getClass().getResource("Plus.png");
    image = Toolkit.getDefaultToolkit().getImage(imageFile);
    button2.setIcon(new ImageIcon(image));

    textField1 = new JTextField();

    // ======== this ========
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        zoomOut(e);
      }
    });
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    // ---- button1 ----
    button1.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        zoomOut(e);
      }
    });
    add(button1);

    // ---- logarithmicJSlider1 ----
    logarithmicJSlider1.setMaximum(10000000);
    logarithmicJSlider1.setMinimum(100);
    logarithmicJSlider1.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        zoomChanged(e);
      }
    });
    add(logarithmicJSlider1);

    // ---- button2 ----
    button2.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        zoomIn(e);
      }
    });
    add(button2);

    // ---- textField1 ----
    textField1.setColumns(10);
    textField1.setEditable(false);
    add(textField1);
    // //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY
  // //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel button1;

  private LogarithmicJSlider logarithmicJSlider1;

  private JLabel button2;

  private JTextField textField1;
  // JFormDesigner - End of variables declaration //GEN-END:variables

}
