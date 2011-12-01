package repast.simphony.engine.watcher;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class Watchee {

  private int counter = 0;

  public void run() {
    counter++;
  }

  public int getCounter() {
    return counter;
  }
}
