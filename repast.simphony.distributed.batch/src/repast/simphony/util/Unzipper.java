/**
 * 
 */
package repast.simphony.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Unzips a named file into current directory.
 * 
 * @author Nick Collier
 */
public class Unzipper {

  public void run(String zipFile) throws IOException {
    ZipFile zip = null;
    try {
      zip = new ZipFile(zipFile);
      for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
        ZipEntry entry = iter.nextElement();
        File file = new File(".", entry.getName());
        if (entry.isDirectory()) {
          file.mkdirs();
        } else {
          ReadableByteChannel source = Channels.newChannel(zip.getInputStream(entry));
          if (!file.exists())
            file.createNewFile();
          FileChannel dstChannel = new FileOutputStream(file).getChannel();
          dstChannel.transferFrom(source, 0, entry.getSize());
          dstChannel.close();
          source.close();
        }
      }

    } finally {
      if (zip != null)
        try {
          zip.close();
        } catch (IOException ex) {
          throw ex;
        }
    }

  }
  
  public static void main(String[] args) throws Exception {
      new Unzipper().run(args[0]);
  }
}
