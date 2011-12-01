package repast.simphony.weka;

import org.apache.commons.lang.SystemUtils;
import repast.simphony.data.analysis.AnalysisPluginRunner;

public class RunWekaModel extends AnalysisPluginRunner  {

  private static String path;

  static {
    if (SystemUtils.IS_OS_MAC) 
    	path = "/Applications/Weka-3-4/";
    else if (SystemUtils.IS_OS_WINDOWS)
    	path = "C:\\Program Files\\Weka-3-4\\";
    else
    	path = "/usr/local/bin/Weka-3-4/";
  }
  
  public RunWekaModel(){
		super("Weka", path,"license.txt",new WekaWizard());
		
	}
}
