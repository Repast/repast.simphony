package repast.simphony.weka;

import org.apache.commons.lang3.SystemUtils;
import repast.simphony.data.analysis.AnalysisPluginRunner;

public class RunWekaModel extends AnalysisPluginRunner  {

  private static String path;

  static {
    if (SystemUtils.IS_OS_MAC) 
    	path = "/Applications/weka-3-6/";
    else if (SystemUtils.IS_OS_WINDOWS)
    	path = "C:\\Program Files\\Weka-3-6";
    else
    	path = "/usr/local/bin/Weka-3-6/";
  }
  
  public RunWekaModel(){
		super("Weka", path,"license.txt",new WekaWizard());
		
	}
}
