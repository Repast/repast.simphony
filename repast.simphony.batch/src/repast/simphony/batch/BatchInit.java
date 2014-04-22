/**
 * 
 */
package repast.simphony.batch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.cli.CommandLine;

import repast.simphony.parameter.RunResultProducer;
import repast.simphony.parameter.SweeperProducer;
import repast.simphony.parameter.bsf.ScriptRunner;
import repast.simphony.parameter.groovy.GroovyRunner;
import repast.simphony.parameter.optimizer.AdvancementChooser;
import repast.simphony.parameter.optimizer.OptPropertiesParser;
import repast.simphony.parameter.xml.OptimizedXMLSweeperProducer;
import repast.simphony.parameter.xml.XMLSweeperProducer;

/**
 * Initializes the batch code and starts the BatchRunner.
 * 
 * @author Nick Collier
 */
public class BatchInit {

  public void run(CommandLine line, String[] args) throws Exception {
	  
    BatchRunner runner;
    if (line.hasOption("interactive"))
      runner = new BatchRunner(true);
    else
      runner = new BatchRunner(false);
    
//    boolean mjbFix = true;
    
//    if (mjbFix) {
    	// moved code and call load scenario
    	// parse the target
    	String targetArg = args[args.length - 1];
    	File file = new File(targetArg);
    	if (file.exists()) {
    		runner.loadScenario(file);
    	} else {
    		Class<?> c = Class.forName(targetArg);
    		BatchScenarioCreator creator = (BatchScenarioCreator) c.newInstance();
    		runner.createScenario(creator);
    	}
//    }

    SweeperProducer producer = null;
    // parse all the options
    if (line.hasOption("opt")) {
      producer = runOpt(line);

    } else {
      String params = line.getOptionValue("params");
      if (params != null) {
        if (params.endsWith("xml")) {
          producer = new XMLSweeperProducer(new File(params).toURI().toURL());
        } else if (params.endsWith("bsh")) {
          producer = new ScriptRunner(new File(params));
        } else if (params.endsWith("groovy")) {
          producer = new GroovyRunner(new File(params));
        }
      }
    }

    runner.setSweeperProducer(producer);
    
//    if (mjbFix) {
    	runner.run();
//    }
    
//    if (!mjbFix) {
//    	// parse the target
//    	String targetArg = args[args.length - 1];
//    	File file = new File(targetArg);
//    	if (file.exists()) {
//    		runner.run(file);
//    	} else {
//    		Class<?> c = Class.forName(targetArg);
//    		BatchScenarioCreator creator = (BatchScenarioCreator) c.newInstance();
//    		runner.run(creator);
//    	}
//    }
  }

  private SweeperProducer runOpt(CommandLine line) throws IOException, ClassNotFoundException,
      IllegalAccessException, InstantiationException, MalformedURLException {
    SweeperProducer producer;
    // if opt get all the info from there
    String file = line.getOptionValue("opt");
    OptPropertiesParser optParser = new OptPropertiesParser(new File(file));
    RunResultProducer rrProducer = optParser.getRunResultProducer();
    AdvancementChooser chooser = optParser.getAdvancementChooser();
    if (optParser.getParametersFile() != null) {
      String paramsFile = optParser.getParametersFile();
      if (chooser == null)
        producer = new OptimizedXMLSweeperProducer(rrProducer, new File(paramsFile).toURI().toURL());
      else
        producer = new OptimizedXMLSweeperProducer(rrProducer, chooser, new File(paramsFile)
            .toURI().toURL());
    } else {
      String scriptFile = optParser.getBSFScript();
      if (chooser == null) {
        producer = new ScriptRunner(rrProducer, new File(scriptFile));
      } else {
        producer = new ScriptRunner(rrProducer, chooser, new File(scriptFile));
      }
    }
    return producer;
  }

}
