package repast.simphony.engine.watcher;

import org.apache.log4j.BasicConfigurator;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class WatcherTestRunner {

  public static void main(String[] args) {
    BasicConfigurator.configure();
    WatcheeInstrumentor inst = new WatcheeInstrumentor();

  
  
    inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "dVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "lVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "byVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "bVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "sVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "cVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "fVal");
		inst.addFieldToWatch("repast.simphony.engine.watcher.Generator", "counter");

		inst.addFieldToWatch("repast.simphony.engine.watcher.Watchee", "counter");
		

    inst.instrument("./test_bin");
    WatcherTrigger.initInstance(inst);

    junit.textui.TestRunner.main(args);
  }
}
  