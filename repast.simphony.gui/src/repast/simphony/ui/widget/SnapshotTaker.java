package repast.simphony.ui.widget;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import repast.simphony.ui.Imageable;
import repast.simphony.ui.ImageableJComponentAdapter;
import simphony.util.messages.MessageCenter;

/**
 * Takes a snapshot of a JComponent.
 *
 * @author Nick Collier
 */
public class SnapshotTaker {

  static MessageCenter msg = MessageCenter.getMessageCenter(SnapshotTaker.class);

  public final static String JPEG = "jpeg";
  public final static String JPG = "jpg";
  public final static String TIFF = "tiff";
  public final static String TIF = "tif";
  public final static String PNG = "png";
  public final static String BMP = "bmp";

  private static class ImageFileFilter extends FileFilter {

    private String description;
    private Set<String> imageExt = new HashSet<String>();
    private String ext;

    public ImageFileFilter(String description, String... exts) {
      this.description = description;
      ext = exts[0];
      for (String str : exts) {
        imageExt.add(str);
      }
    }

    public String getDescription() {
      return description;
    }

    public boolean accept(File f) {
      if (f.isDirectory()) return true;
      String ext = findExtension(f);
      return ext != null && imageExt.contains(ext);
    }

    public String getExtension() {
      return ext;
    }

    private String findExtension(File f) {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 && i < s.length() - 1) {
        ext = s.substring(i + 1).toLowerCase();
      }
      return ext;
    }
  }


  private Imageable imageable;
  private String currentExt = "png";

  public SnapshotTaker(Imageable imageable) {
    this.imageable = imageable;
  }

  public SnapshotTaker(JComponent comp) {
    if (comp instanceof Imageable) imageable = (Imageable) comp;
    else {
      imageable = new ImageableJComponentAdapter(comp);
    }
  }

  /**
   * Takes a snapshot of the Imageable associated
   * with this Snapshot taker. This will display a file dialog.
   *
   * @param parent the component to center the file dialog over
   * @throws IOException if there is an error taking the snapshot
   */
  public void takeSnapshot(JComponent parent) throws IOException {
    JFileChooser chooser = new JFileChooser();
    chooser.addChoosableFileFilter(new ImageFileFilter("BMP Image (*.bmp)", BMP));
    chooser.addChoosableFileFilter(new ImageFileFilter("JPEG Image (*.jpg, *.jpeg)", JPG, JPEG));
    chooser.addChoosableFileFilter(new ImageFileFilter("TIFF Image (*.tif, *.tiff)", TIF, TIFF));
    ImageFileFilter pngFilter = new ImageFileFilter("PNG Image (*.png)", PNG);
    chooser.addChoosableFileFilter(pngFilter);
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.setFileFilter(pngFilter);

    chooser.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
          currentExt = ((ImageFileFilter) evt.getNewValue()).getExtension();
        }
      }
    });

    int res = chooser.showSaveDialog(parent);
		if (res == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
      if (!file.getName().endsWith("." + currentExt)) {
        file = new File(file.getParentFile(), file.getName() + "." + currentExt);
      }
      save(file, currentExt);
		}
  }

  /**
   * Saves the snapshot to the specified file and in the
   * specified format.
   *
   * @param file the file to save the image to
   * @param imageFormat the image format. One of "bmp", "jpg", "tif", or "png".
   * @throws IOException if there is an error saving the snapshot
   */
  public void save(File file, String imageFormat) throws IOException {
    try {
      BufferedImage img = imageable.getImage();
      ImageIO.write(img, imageFormat, file);
    } catch (IOException ex) {
      throw ex;
    } catch (Exception ex) {
      IOException io = new IOException(ex.getMessage());
      io.initCause(ex);
      throw io;
    }
  }

  /**
   * Creates an Action that will take a snapshot fo the specified
   * JComponent when triggered.
   *
   * @param comp the component to take a snapshot of.
   *
   * @return an Action that will take a snapshot fo the specified
   * JComponent when triggered.
   */
  public static Action createSnapshotAction(JComponent comp) {
    SnapshotTaker taker = new SnapshotTaker(comp);
    return new SnapAction(taker, comp);
  }

  private static class SnapAction extends AbstractAction {

    SnapshotTaker taker;
    JComponent parent;

    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public SnapAction(SnapshotTaker taker, JComponent parent) {
      super("Take Snapshot");
      this.taker = taker;
      this.parent = parent;
    }

    public void actionPerformed(ActionEvent e) {
      try {
        taker.takeSnapshot(parent);
      } catch (IOException ex) {
        msg.error("Error while taking snapshot", ex);
      }
    }
  }
}
