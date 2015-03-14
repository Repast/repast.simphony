package repast.simphony.engine.watcher;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:26:02 $
 */
public class Generator {
  
  private int counter;
  private double dVal;
  private long lVal;
  private boolean bVal;
  private float fVal;
  private short sVal;
  private byte byVal;
  private char cVal;
  

  public int getCounter() {
    return counter;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }
  
  public void run() {
    counter++;
    dVal++;
    lVal++;
    bVal = true;
    fVal++;
    sVal++;
    byVal++;
    cVal = 'a';
  }

	public void setBVal() {
		bVal = true;
	}

	public void incrCounter() {
		counter++;
	}

  public boolean isbVal() {
    return bVal;
  }

  public byte getByVal() {
    return byVal;
  }

  public char getcVal() {
    return cVal;
  }

  public double getdVal() {
    return dVal;
  }

  public float getfVal() {
    return fVal;
  }

  public long getlVal() {
    return lVal;
  }

  public short getsVal() {
    return sVal;
  }

}
