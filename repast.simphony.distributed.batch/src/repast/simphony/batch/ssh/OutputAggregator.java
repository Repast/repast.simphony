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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import repast.simphony.batch.BatchConstants;

/**
 * Aggregates the separate instance batch param map and output files.
 * 
 * @author Nick Collier
 */
public class OutputAggregator {

  private List<File> findOutput(String baseName, List<File> files) {
    List<File> chosen = new ArrayList<File>();
    int index = baseName.indexOf(".");
    if (index != -1) {
      baseName = baseName.substring(0, index);
    }

    for (File file : files) {
      if (file.getName().startsWith(baseName)) {
        chosen.add(file);
      }
    }

    return chosen;
  }

  private String makePFName(String fname) {
    int index = fname.lastIndexOf(".");
    String pfname = null;
    if (index == -1) {
      pfname = fname + "." + BatchConstants.PARAM_MAP_SUFFIX;
    } else {
      pfname = fname.substring(0, index) + "." + BatchConstants.PARAM_MAP_SUFFIX
          + fname.substring(index, fname.length());
    }
    return pfname;
  }
  
  public String getNonParamMapFile(List<File> files) {
    for (File file : files) {
      if (!file.getName().contains(BatchConstants.PARAM_MAP_SUFFIX)) return file.getName();
    }
    return null;
  }

  public void run(List<String> baseOutputNames, List<File> outputFiles, String aggOutputDirectory)
      throws IOException {
    
    // sort by length so we compare against the longest baseName first
    // this will avoid matching smaller names that are subsets of longer ones
    // e.g. ModelOutput and ModelOutput2
    Collections.sort(baseOutputNames, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.length() < s2.length() ? 1 : s1.length() == s2.length() ? 0 : -1;
      }
    });
    
    for (String baseName : baseOutputNames) {
      List<File> output = findOutput(baseName, outputFiles);
      outputFiles.removeAll(output);
      
      if (output.isEmpty())
        throw new IOException("Output files are missing");
      
      String fname = getNonParamMapFile(output);
      if (fname == null)
        throw new IOException("Error while aggregating output");
      String pfname = makePFName(fname);

      BufferedWriter fout = null;
      BufferedWriter pfout = null;

      try {
        fout = new BufferedWriter(new FileWriter(aggOutputDirectory + "/" + fname));
        pfout = new BufferedWriter(new FileWriter(aggOutputDirectory + "/" + pfname));
        boolean fskip = false, pfskip = false;
        for (File file : output) {
          if (file.getName().contains(BatchConstants.PARAM_MAP_SUFFIX)) {
            process(pfout, file, pfskip);
            pfskip = true;
          } else {
            process(fout, file, fskip);
            fskip = true;
          }
        }

      } finally {
        if (fout != null)
          fout.close();
        if (pfout != null)
          pfout.close();
      }
    }
  }

  private void process(BufferedWriter out, File file, boolean skip) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line = null;
      if (skip)
        reader.readLine();
      while ((line = reader.readLine()) != null) {
        out.write(line);
        out.write("\n");
      }
    } finally {
      if (reader != null)
        reader.close();
    }
  }
}
