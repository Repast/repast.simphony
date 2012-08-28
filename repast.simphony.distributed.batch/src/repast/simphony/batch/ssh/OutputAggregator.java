/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import repast.simphony.batch.BatchConstants;

/**
 * Aggregates the separate instance batch param map and output files. 
 * 
 * @author Nick Collier
 */
public class OutputAggregator {
  
  private String findOutputName(List<File> files) {
    for (File file : files) {
      if (!file.getName().contains(BatchConstants.PARAM_MAP_SUFFIX)) {
        return file.getName();
      }
    }
    
    return null;
  }
  
  private String makePFName(String fname) {
    int index =  fname.lastIndexOf(".");
    String pfname = null;
    if (index == -1) {
      pfname = fname + "." + BatchConstants.PARAM_MAP_SUFFIX;
    } else {
      pfname = fname.substring(0, index) + "." + BatchConstants.PARAM_MAP_SUFFIX + fname.substring(index, fname.length());
    }
    return pfname;
    
  }
  
  public void run(List<File> outputFiles, String aggOutputDirectory) throws IOException {
    String fname = findOutputName(outputFiles);
    if (fname == null) throw new IOException("Output files are missing");
    String pfname = makePFName(fname);
    
    BufferedWriter fout = null;
    BufferedWriter pfout = null;
    
    try {
      fout = new BufferedWriter(new FileWriter(aggOutputDirectory + "/" + fname));
      pfout = new BufferedWriter(new FileWriter(aggOutputDirectory + "/" + pfname));
      boolean fskip = false, pfskip = false;
      for (File file : outputFiles) {
        if (file.getName().contains(BatchConstants.PARAM_MAP_SUFFIX)) {
          process(pfout, file, pfskip);
          pfskip = true;
        } else {
          process(fout, file, fskip);
          fskip = true;
        }
      }
      
    } finally {
      if (fout != null) fout.close();
      if (pfout != null) pfout.close();
    }
  }

  private void process(BufferedWriter out, File file, boolean skip) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line = null;
      if (skip) reader.readLine();
      while ((line = reader.readLine()) != null) {
        out.write(line);
        out.write("\n");
      }
    } finally {
      if (reader != null) reader.close();
    }
    
   
  }

}
