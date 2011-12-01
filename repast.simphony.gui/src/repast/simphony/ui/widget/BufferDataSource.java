package repast.simphony.ui.widget;

import javax.media.Buffer;
import javax.media.MediaLocator;
import javax.media.Time;
import javax.media.format.RGBFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;

/**
 * A DataSource for turning JMF Buffers into movies. See the Java Media
 * Framework documentation for details.
 *
 * @author Nick Collier
 */

public class BufferDataSource extends PullBufferDataSource {

  private BufferSourceStream streams[];

  public BufferDataSource(RGBFormat format) {
    streams = new BufferSourceStream[1];
    streams[0] = new BufferSourceStream(format);
  }

  public void addBuffer(Buffer buf) {
    streams[0].addBuffer(buf);
  }

  public void cleanUp() {
    streams[0].waitForDone();
  }

  public void setLocator(MediaLocator source) {

  }

  public MediaLocator getLocator() {
    return null;
  }

  public String getContentType() {
    return ContentDescriptor.RAW;
  }

  public void connect() {}

  public void disconnect() {}

  public void start() {}

  public void stop() {}

  public PullBufferStream[] getStreams() {
    return streams;
  }

  public Time getDuration() {
    return DURATION_UNKNOWN;
  }

  public Object[] getControls() {
    return new Object[0];
  }

  public Object getControl(String type) {
    return null;
  }
}





