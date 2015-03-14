package repast.simphony.engine.watcher;

/**
 * @author Nick Collier
 */
public class Counter {

  private int countA, countB;

  public void incrementA() {
    countA++;
  }

  public void reset() {
    countA = 0;
    countB = 0;
  }

  public int getCountA() {
    return countA;
  }

  public void incrementB() {
    countB++;
  }

  public int getCountB() {
    return countB;
  }

}
