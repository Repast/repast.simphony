package repast.simphony.engine.watcher;


/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class PerfTest {
  
  int iterations = 20000000;

  public void run() {
    Generator gen = new Generator();
    for (int i = 0; i < iterations; i++) {
      gen.run();
    }

    long start = System.currentTimeMillis();
    for (int i = 0; i < iterations; i++) {
      gen.run();
    }
    long duration = System.currentTimeMillis() - start;
    System.out.println("Duration = " + duration + " ms");
  }

  public static void main(String[] args) {
    PerfTest test = new PerfTest();
    test.run();
  }
}



