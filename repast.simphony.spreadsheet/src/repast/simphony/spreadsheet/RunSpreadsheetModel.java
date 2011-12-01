package repast.simphony.spreadsheet;

import org.apache.commons.lang.SystemUtils;

import repast.simphony.data.analysis.AnalysisPluginRunner;

public class RunSpreadsheetModel extends AnalysisPluginRunner  {
	
	private static String path;
	
	static {
	    if (SystemUtils.IS_OS_MAC)
	    	path = "/Applications/Microsoft Office 2008/Microsoft Excel.app";
	    else if (SystemUtils.IS_OS_WINDOWS)
	    	path = "C:\\Program Files\\Microsoft Office\\OFFICE11\\Excel.exe";
	    else
	    	path = "/usr/bin/oocalc";
	  }
	
	public RunSpreadsheetModel(){
		super("Spreadsheet",path,
				"license.txt",new SpreadSheetWizard());
		
	}
}
