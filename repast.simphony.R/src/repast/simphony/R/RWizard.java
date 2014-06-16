package repast.simphony.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import repast.simphony.data.analysis.AnalysisPluginWizard;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;
import repast.simphony.data2.Formatter;
import repast.simphony.util.SystemConstants;

/**
 * A wizard for executing R on a file outputter's output.
 * 
 * @author Eric Tatara
 * @author Jerry Vos
 */

public class RWizard extends AnalysisPluginWizard {

	private static final String R_HOME = "./RHome/";

	private Map<String, String> envVars = new HashMap<String, String>();

	public RWizard() {

	}

	public RWizard(DataSetRegistry loggingRegistry, boolean showCopyright,
			boolean browseForRHome, String name, String installHome,
			String defaultLocation, String licenseFileName) {

		super(loggingRegistry, showCopyright, browseForRHome, name,
				installHome, defaultLocation, licenseFileName);
	}

	// this must be called after getExecutionCommand
	public Map<String, String> getEnvVars() {
		return envVars;
	}

	public String[] getExecutionCommand() {

		List<String> commands = new ArrayList<String>();

		// NOTE: the LOGFILE and DELIMMTER args are processed by our .Rprofile.

		if (SystemUtils.IS_OS_WINDOWS) {
			//commands.add("R_PROFILE_USER=" + prepFileNameForR(getRHome()));
			//commands.add("&");
			
			envVars.put("R_PROFILE_USER", prepFileNameForR(getRHome()) + ".Rprofile");
			//commands.add("SET R_PROFILE_USER=" + prepFileNameForR(getRHome()));
			
			//builder.append("& ");
			List<FileDataSink> outputters = fileStep.getChosenOutputters();

			// For each outputter, define the log file and delimitter
			for (int i = 0; i < outputters.size(); i++) {
				Path out = outputters.get(i).getFile().toPath().normalize().toAbsolutePath();
				Path cwd = FileSystems.getDefault().getPath(".").normalize().toAbsolutePath();
				//System.out.println(out);
				//System.out.println(cwd);
				String sout = out.toString();
				if (out.startsWith(cwd)) {
					sout = sout.substring(cwd.toString().length() + 1);
				}
				
				envVars.put("LOG_FILE" + i, sout.replace("\\", "/"));
				Formatter formatter = outputters.get(i).getFormatter();
				if (outputters.get(i).getFormat() != FormatType.TABULAR) {
					LOG.warn("When invoking R, an outputter without a delimited formatter "
							+ "was found. R can only be invoked on output files with using a delimiter.");
					break;
				}
				String delimiter = formatter.getDelimiter();
				envVars.put("DELIMITER" + i, delimiter);
			}
			commands.add(getInstallHome());
		} else if (SystemUtils.IS_OS_MAC) {
			List<FileDataSink> outputters = fileStep.getChosenOutputters();
			List<String> files = new ArrayList<String>();
			List<String> delims = new ArrayList<String>();
			String cwd = new File(".").getAbsolutePath();
			for (int i = 0; i < outputters.size(); i++) {
				Formatter formatter = outputters.get(i).getFormatter();
				if (outputters.get(i).getFormat() != FormatType.TABULAR) {
					LOG.warn("When invoking R, an outputter without a delimited formatter "
							+ "was found. R can only be invoked on output files with using a delimiter.");
					break;
				}
				delims.add(formatter.getDelimiter());
				String fileName = outputters.get(i).getFile().getAbsolutePath();
				File f = new File(fileName);
				if (fileName.startsWith("/")) {
					files.add(fileName);
				} else {
					if (f.exists()) {
						files.add("./" + fileName);
					} else {
						// assume its relative to the current working directory
						files.add(cwd + "/" + fileName);
					}
				}
			}

			for (int i = 0; i < files.size(); i++) {
				envVars.put("LOG_FILE" + i, files.get(i));
				envVars.put("DELIMITER" + i, delims.get(i));
			}

			// this should ceate the .RProfile file
			getRHome();
			envVars.put("R_PROFILE_USER", cwd + "/RHome/.RProfile");
			commands.add("open");
			commands.add("-a");
			commands.add("RStudio.app");
			commands.add(cwd);

		} else {
			// TODO linux command
		}

		return commands.toArray(new String[commands.size()]);
	}

	protected String prepFileNameForR(String fileName) {
		return fileName.replace('\\', '/');
	}

	private String getRHome() {
		File rHomeDir = new File(R_HOME);

		// create the RHome directory in the runtime dir if not present
		if (!rHomeDir.exists()) {
			rHomeDir.mkdir();
		}

		// copy .Rprofile to the user's R Home
		// check if it exists already as /RHome/.Rprofile
		if (rHomeDir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.equals(".Rprofile")) {
					return true;
				}
				return false;
			}
		}).length == 0) {

			// if /RHome/.Rprofile doesnt exist make a new one from Rprofile.txt
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								RWizard.class
										.getResourceAsStream("Rprofile.txt")));

				FileWriter writer = new FileWriter(R_HOME + ".Rprofile");

				String line = reader.readLine();
				while (line != null) {
					writer.write(line + SystemConstants.LINE_SEPARATOR);

					line = reader.readLine();
				}
				reader.close();
				writer.close();
			} catch (IOException e) {
				LOG.error(
						"RWizard.getRHome: Error, preparing the R home "
								+ "directory.  The execution of R may not occur properly.",
						e);
			}
		}

		return R_HOME;
	}

	@Override
	public String getCannotRunMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}