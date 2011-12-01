package repast.simphony.ui.widget;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.media.Buffer;
import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.format.RGBFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.util.ImageToBuffer;

import simphony.util.messages.MessageCenter;

/**
 * Makes a quicktime movie from a buffered images.
 *
 * @author Nick Collier
 */
public class MovieMaker implements ControllerListener, DataSinkListener {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(MovieMaker.class);

  private Processor p;
  private BufferDataSource source;

  private Object waitSync = new Object();
  private Object waitFileSync = new Object();
  private boolean fileDone = false;
  boolean stateTransitionOK = true;
  private MediaLocator outML;
  private DataSink sink;
  private String movieType;
  private int frameRate;
  private boolean init = false;

  public MovieMaker(int frameRate, File file, String movieType) {
    this.movieType = movieType;
    this.frameRate = frameRate;
    String url = "file://" + file.getAbsolutePath();
    outML = new MediaLocator(url);
  }

  private boolean init(RGBFormat format) {
    source = new BufferDataSource(format);
    try {
      p = Manager.createProcessor(source);
    } catch (Exception ex) {
      msg.error("Failed to create processor for movie", ex);
      return false;
    }

    p.addControllerListener(this);

    // put the processor into configured state so can set some options
    // on the processor
    p.configure();
    if (!waitForState(p, Processor.Configured)) {
      System.err.println("Failed to configure the processor");
      return false;
    }

    // set the output content descriptor to the movie type
    p.setContentDescriptor(new ContentDescriptor(movieType));

    TrackControl tcs[] = p.getTrackControls();
    Format f[] = tcs[0].getSupportedFormats();

    if (f == null || f.length <= 0) {
      msg.warn("The mux does not support the input format: " +
              tcs[0].getFormat());
      return false;
    }

    tcs[0].setFormat(f[0]);

    // realize the processor
    p.realize();
    if (!waitForState(p, Processor.Realized)) {
      msg.warn("Failed to Realize processor");
      return false;
    }

    boolean result = createDataSink();

    if (!result) {
      return false;
    }

    sink.addDataSinkListener(this);

    try {
      p.start();
      sink.start();
    } catch (IOException ex) {
      msg.error("Movie error", ex);
      return false;
    }

    msg.info("Movie capture has started", "STATUS");
    return true;
  }

  /**
   * Adds an image to a movie as a frame with a default sleep value of 40 milliseconds
   *
   * @param image the image to add as a frame
   * @throws IOException if there is an error while adding the frame
   */
  public void addImageAsFrame(BufferedImage image) throws IOException {
    addImageAsFrame(image, 40);
  }

  /**
   * Adds an image to a movie as a frame. This method pause the main
   * simulation thread for the specified amount of time. This pause
   * is necessary to allow the images to be written to disk in a background
   * thread. Without the pauses the images are still written to disk, but
   * are added much faster than they can be written, resulting in increased
   * memory use and eventual OutOfMemoryErrors. This method is called by
   * DisplaySurface and shouldn't be called by a user in the course of
   * writing a model.
   *
   * @param image      the image to add as a frame
   * @param sleepCount the amount to pause for writing images to disk
   * @throws IOException if there is an error while adding the frame
   */
  public void addImageAsFrame(BufferedImage image, int sleepCount) throws IOException {
    Buffer b = ImageToBuffer.createBuffer(image, frameRate);

    if (!init) {
      boolean result = init((RGBFormat) b.getFormat());
      if (!result) {
        System.err.println("Failed to setup movie capture");
      }
      init = true;
    }
    source.addBuffer(b);
    try {
      Thread.sleep(sleepCount);
    } catch (InterruptedException ex) {
    }
    System.gc();
  }


  private boolean waitForState(Processor p, int state) {
    synchronized (waitSync) {
      try {
        while (p.getState() < state && stateTransitionOK) {
          waitSync.wait();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return stateTransitionOK;
  }

  public void cleanUp() {
    source.cleanUp();
    waitForFileDone();
    try {
      sink.close();
    } catch (Exception ex) {
    }


    p.removeControllerListener(this);
    msg.info("Movie capture has finished", "STATUS");
  }

  private void waitForFileDone() {
    synchronized (waitFileSync) {
      try {
        while (!fileDone) {
          waitFileSync.wait();
        }
      } catch (Exception ex) {
        msg.error("Error exporting to movie", ex);
        sink.close();
      }
    }
  }


  private boolean createDataSink() {
    DataSource ds = p.getDataOutput();
    try {
      sink = Manager.createDataSink(ds, outML);
      sink.open();
    } catch (Exception ex) {
      msg.error("Error exporting to movie", ex);
      return false;
    }

    return true;
  }

  // controller listener
  public void controllerUpdate(ControllerEvent evt) {
    if (evt instanceof ConfigureCompleteEvent ||
            evt instanceof RealizeCompleteEvent ||
            evt instanceof PrefetchCompleteEvent) {
      synchronized (waitSync) {
        stateTransitionOK = true;
        waitSync.notifyAll();
      }
    } else if (evt instanceof ResourceUnavailableEvent) {
      synchronized (waitSync) {
        stateTransitionOK = false;
        waitSync.notifyAll();
      }
    } else if (evt instanceof EndOfMediaEvent) {
      System.out.println("End of Media Event");
      evt.getSourceController().stop();
      evt.getSourceController().close();
    }

  }

  // datasink listener
  public void dataSinkUpdate(DataSinkEvent evt) {
    if (evt instanceof EndOfStreamEvent) {
      synchronized (waitFileSync) {
        fileDone = true;
        waitFileSync.notifyAll();
      }
    } else if (evt instanceof DataSinkErrorEvent) {
      synchronized (waitFileSync) {
        fileDone = true;
        waitFileSync.notifyAll();
      }
    }
  }


}
