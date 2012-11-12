/**
 * 
 */
package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class ConsolePanel extends JPanel implements BatchRunPanel {
  
  private class ConsoleOutputStream extends OutputStream {
    
    private boolean error;
    
    public ConsoleOutputStream(boolean error) {
      this.error = error;
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      update(new String(b, off, len), error);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException {
      write(b, 0, b.length);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
      update(String.valueOf((char)b), error);
    }
  };
  
  private JTextPane pane = new JTextPane();
  private ConsoleOutputStream stdOut = new ConsoleOutputStream(false);
  private ConsoleOutputStream errOut = new ConsoleOutputStream(true);
  
  
  public ConsolePanel() {
    super(new BorderLayout());
    add(new JScrollPane(pane), BorderLayout.CENTER);
    pane.setFont(Font.decode(Font.MONOSPACED).deriveFont(12f));
    
    Style style = pane.addStyle("error", null);
    StyleConstants.setForeground(style, Color.RED);
    
    style = pane.addStyle("std", null);
    StyleConstants.setForeground(style, Color.BLACK);
    addListeners();
  }
  
  private void addListeners() {
    pane.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent evt) {
        if (evt.isPopupTrigger()) showMenu(evt);
      }

      @Override
      public void mouseReleased(MouseEvent evt) {
        if (evt.isPopupTrigger()) showMenu(evt);
      }
      
    });
  }
  
  private void showMenu(MouseEvent evt) {
    JPopupMenu menu = new JPopupMenu();
    menu.add(new AbstractAction("Clear") {
      @Override
      public void actionPerformed(ActionEvent evt1) {
        pane.setText("");
      }
    });
    menu.show(pane, evt.getX(), evt.getY());
  }
  
  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#validateInput()
   */
  @Override
  public ValidationResult validateInput() {
    return ValidationResult.SUCCESS;
  }

  public void update(final String val, final boolean error) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Style style = error ? pane.getStyle("error") : pane.getStyle("std");
        StyledDocument doc = pane.getStyledDocument();
        try {
          doc.insertString(doc.getLength(), val, style);
        } catch (BadLocationException ex) {
          ex.printStackTrace();
        }
        pane.setCaretPosition(doc.getLength() - 1);
      }
    });
  }
  
  public OutputStream getStdOutputStream() {
    return stdOut;
  }
  
  public OutputStream getErrorOutputStream() {
    return errOut;
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#init(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public void init(BatchRunConfigBean model) {
  }

  /* (non-Javadoc)
   * @see repast.simphony.batch.gui.BatchRunPanel#commit(repast.simphony.batch.gui.BatchRunModel)
   */
  @Override
  public CommitResult commit(BatchRunConfigBean model) {
    return CommitResult.SUCCESS;    
  }
}
