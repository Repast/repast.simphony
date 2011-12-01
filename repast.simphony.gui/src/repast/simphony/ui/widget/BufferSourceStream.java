package repast.simphony.ui.widget;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A DataSourceStream for turning JMF Buffers into movies.
 *
 * @author Nick Collier
 */

public class BufferSourceStream implements PullBufferStream {

  private List<Buffer> buffers = new ArrayList<Buffer>();

  private RGBFormat vFormat;
  private boolean notDone = true;
  private boolean finished = false;

  public BufferSourceStream(RGBFormat aFormat) {
    vFormat = aFormat;
  }

  public void addBuffer(Buffer buf) {
    synchronized(buffers) {
      buffers.add(buf);
      buffers.notify();
    }
  }

  public void waitForDone() {

    synchronized (buffers) {
      notDone = false;
      while (buffers.size() != 0) {
        try {
          buffers.wait();
        } catch (InterruptedException ex) {}
      }
      buffers.notify();
    }
    finished = true;
  }

  public boolean willReadBlock() {
    return buffers.size() == 0;
  }

  public void read(Buffer buf) {
    synchronized (buffers) {
      while (buffers.size() == 0 && notDone) {
        try {
          buffers.wait();
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
      }

      if (buffers.size() > 0) {
        Buffer newBuf = (Buffer)buffers.get(0);
        int[] newData = (int[])newBuf.getData();
        buf.setData(newData);
        buf.setLength(newBuf.getLength());
        buf.setOffset(0);
        buf.setFormat(vFormat);
        buf.setFlags(Buffer.FLAG_KEY_FRAME | Buffer.FLAG_NO_DROP);
        buffers.remove(0);
        //System.out.println("Removing buffer: size = " + buffers.size());
      } else {
        buf.setEOM(true);
        buf.setOffset(0);
        buf.setLength(0);
        synchronized (buffers) {
          buffers.notify();
        }
      }
    }
  }

  public Format getFormat() {
    return vFormat;
  }

  public ContentDescriptor getContentDescriptor() {
    return new ContentDescriptor(ContentDescriptor.RAW);
  }

  public long getContentLength() {
    return 0;
  }

  public boolean endOfStream() {
    return finished;
  }

  public Object[] getControls() {
    return new Object[0];
  }

  public Object getControl(String type) {
    return null;
  }
}

