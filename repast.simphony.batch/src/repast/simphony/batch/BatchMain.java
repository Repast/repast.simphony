package repast.simphony.batch;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import repast.simphony.parameter.RunResultProducer;
import repast.simphony.parameter.SweeperProducer;
import repast.simphony.parameter.bsf.ScriptRunner;
import repast.simphony.parameter.groovy.GroovyRunner;
import repast.simphony.parameter.optimizer.AdvancementChooser;
import repast.simphony.parameter.optimizer.OptPropertiesParser;
import repast.simphony.parameter.xml.OptimizedXMLSweeperProducer;
import repast.simphony.parameter.xml.XMLSweeperProducer;
import simphony.util.messages.MessageCenter;

/**
 * Main class used to kick off a batch run. This expects a command line arg
 * that is either the path to a repast.simphony simphony scenario file or the fully qualified name
 * of a class that implements BatchScenarioCreator.
 * 
 * !!!NOTE THAT THIS IS DEPRECATED FOR NON-DISTRIBUTED BATCH RUNS. PLEASE USE
 * RepastBatchMain INSTEAD!!!
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchMain {

	static MessageCenter msgCenter = MessageCenter.getMessageCenter(BatchMain.class);
	private Options options;
	private static Map<String, byte[]> htJarContents=new HashMap<String,byte[]>();

	public BatchMain() {
		options = new Options();
		Option help = new Option("help", "print this message");
		
		Option paramfile = OptionBuilder.withArgName("file")
		.hasArg()
		.withDescription("use given parameter sweep file")
		.create("params");

		Option opt = OptionBuilder.withArgName("file")
		.hasOptionalArg()
		.withDescription("use optimizable parameter sweeper with given optimizing properties file")
		.create("opt");
		
		Option mode = OptionBuilder
		.withDescription("specifies if the batch mode is interactive")
		.create("interactive");
		
		options.addOption(help);
		options.addOption(paramfile);
		options.addOption(opt);
		options.addOption(mode);
	}

	@Deprecated
	public void run(String[] args) throws Exception {
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help") || args.length < 1) {
				showHelp();
				System.exit(0);
			}

			BatchRunner runner;

			
				if (line.hasOption("interactive"))
					runner = new BatchRunner(true);
				else
					runner = new BatchRunner(false);

			SweeperProducer producer = null;
			// parse all the options
			if (line.hasOption("opt")) {
				// if opt get all the info from there
				String file = line.getOptionValue("opt");
				OptPropertiesParser optParser = new OptPropertiesParser(new File(file));
				RunResultProducer rrProducer = optParser.getRunResultProducer();
				AdvancementChooser chooser = optParser.getAdvancementChooser();
				if (optParser.getParametersFile() != null) {
					String paramsFile = optParser.getParametersFile();
					if (chooser == null)
						producer = new OptimizedXMLSweeperProducer(rrProducer, new File(paramsFile).toURL());
					else
						producer = new OptimizedXMLSweeperProducer(rrProducer, chooser, new File(paramsFile).toURL());
				} else {
					String scriptFile = optParser.getBSFScript();
					if (chooser == null) {
						producer = new ScriptRunner(rrProducer, new File(scriptFile));
					} else {
						producer = new ScriptRunner(rrProducer, chooser, new File(scriptFile));
					}
				}

			} else {
				String params = line.getOptionValue("params");
				if (params != null) {
					if (params.endsWith("xml")) {
						producer = new XMLSweeperProducer(new File(params).toURL());
					} else if (params.endsWith("bsh")) {
						producer = new ScriptRunner(new File(params));
					} else if (params.endsWith("groovy")) {
						producer = new GroovyRunner(new File(params));
					}
				}
			}

			runner.setSweeperProducer(producer);

			// parse the target
			String targetArg = args[args.length - 1];
			File file = new File(targetArg);
			if (file.exists()) {
				runner.run(file);
			} else {
				Class c = Class.forName(targetArg);
				BatchScenarioCreator creator = (BatchScenarioCreator) c.newInstance();
				runner.run(creator);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			showHelp();
			msgCenter.fatal("Fatal error starting repast.simphony simphony.", ex);
			throw ex;
		}
	} 
	
	public void runDistributed(String[] args) throws Exception {
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				showHelp();
				System.exit(0);
			}
			// parse the target
			String targetArg = args[2];
			BatchRunner runner;
			File file = new File(targetArg);
			
			
			if (line.hasOption("interactive"))
				runner = new BatchRunner(true,Integer.parseInt(args[6]));
			else
				runner = new BatchRunner(false,Integer.parseInt(args[6]));
		
			
			SweeperProducer producer = null;
			// parse all the options
			if (line.hasOption("opt")) {
				// if opt get all the info from there
				String f = line.getOptionValue("opt");
				OptPropertiesParser optParser = new OptPropertiesParser(new File(f));
				RunResultProducer rrProducer = optParser.getRunResultProducer();
				AdvancementChooser chooser = optParser.getAdvancementChooser();
				if (optParser.getParametersFile() != null) {
					String paramsFile = optParser.getParametersFile();
					if (chooser == null) {
						producer = new OptimizedXMLSweeperProducer(rrProducer, new File(paramsFile).toURL());
					}
					else {
						producer = new OptimizedXMLSweeperProducer(rrProducer, chooser, new File(paramsFile).toURL());
					}
				} else {
					String scriptFile = optParser.getBSFScript();
					if (chooser == null) {
						producer = new ScriptRunner(rrProducer, new File(scriptFile));
					} else {
						producer = new ScriptRunner(rrProducer, chooser, new File(scriptFile));
					}
				}

			} else {
				String params = line.getOptionValue("params");
				if (params != null) {
					if (params.endsWith(".xml")) {
						String read = args[7];
						if(file.exists()) 
							producer = new XMLSweeperProducer(new File(args[1]).toURL());
						else
							producer = new XMLSweeperProducer(new File(args[5]).toURL());
						((XMLSweeperProducer)producer).parser.inputFile(read);
					} else if (params.endsWith("bsh")) {
						producer = new ScriptRunner(new File(params));
					} else if (params.endsWith("groovy")) {
						producer = new GroovyRunner(new File(params));
					}
				}
			}

			runner.setSweeperProducer(producer);

		//	JarFile jarFile=null;
		//	String name=args[4];
			
			
			if (file.exists()) {
				runner.run(file);
			} 
			else if(!file.exists()) {
	//			processJar(name);
				while(!new File(args[3]).exists()){
					synchronized(this) {
						this.wait(100);
					}
				} 
				runner.run(new File(args[3]));
			}
			else {
				Class c = Class.forName(targetArg);
				BatchScenarioCreator creator = (BatchScenarioCreator) c.newInstance();
				runner.run(creator);
			}
		} catch (Exception ex) {
			
			ex.printStackTrace();
			showHelp();
			msgCenter.fatal("Fatal error starting repast.simphony simphony.", ex);
			throw ex;
		}
	}
	
	public void processJar(String name){
		 String arg ="jar xf "+name; 
		 try {
			Runtime.getRuntime().exec(arg);
		} catch (IOException e) {
			msgCenter.fatal("Bad jar file, could not extract jar file",e);
		}
	}
	
	private void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		String header = "Where target is the path to a scenario file or the fully qualified name of a class" +
		" that implements repast.simphony.batch.BatchScenarioCreator.\n";
		formatter.printHelp(BatchMain.class.getName() + " [options] target", header, options, "");
	}

	public static void main(String[] args) throws Exception {
		if(args.length<6)
			new BatchMain().run(args);
		else
			new BatchMain().runDistributed(args);
	}
}

