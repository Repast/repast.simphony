package repast.simphony.iReport;

import org.apache.commons.lang3.SystemUtils;
import repast.simphony.data.analysis.AnalysisPluginRunner;

/**
 * A wizard for executing iReport on a file outputter's output.
 * 
 * @author Eric Tatara
 * 
 */

public class RunIReportModel extends AnalysisPluginRunner  {

  private static String path;

  static {
    if (SystemUtils.IS_OS_MAC) 
    	path = "/Applications/iReport.app/Contents/Resources/ireport/bin/ireport";
    else if (SystemUtils.IS_OS_WINDOWS)
    	path = "C:\\Program Files\\JasperSoft\\iReport-3.0.0\\bin\\startup.bat";
    else
    	path = "/usr/local/bin/iReport-3.0.0/bin/startup.sh";
  }

  public RunIReportModel(){
		super("iReport", path, "license.txt",new IReportWizard());
	}
}
