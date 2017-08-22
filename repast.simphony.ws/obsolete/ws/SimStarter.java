package repast.simphony.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.websocket.Session;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioLoadException;

public class SimStarter {

  public Thread run(String scenario, BlockingQueue<SimCommand> cmdQueue,
      BlockingQueue<String> statusQueue, Session session) {
    
    Thread t = null;
    try {
      Properties props = new Properties();
      File in = new File(new File(scenario).getParentFile(), "MessageCenter.log4j.properties");
      try {
        props.load(new FileInputStream(in));
      } catch (IOException e) {
        throw new ScenarioLoadException(e);
      } 
      PropertyConfigurator.configure(props);
      
      String parameters = scenario + "/parameters.xml";
      ParametersParser parser = new ParametersParser(new File(parameters));
      Parameters params = parser.getParameters();

      WSRunner runner = new WSRunner(new File(scenario), cmdQueue, statusQueue, params, session);

      t = new Thread(runner);
    } catch (IOException | ParserConfigurationException | SAXException | ScenarioLoadException ex) {
      ex.printStackTrace();
    }

    return t;
  }

}
