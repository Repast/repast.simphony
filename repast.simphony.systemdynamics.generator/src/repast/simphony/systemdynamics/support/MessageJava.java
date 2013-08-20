/**
 * 
 */
package repast.simphony.systemdynamics.support;

/**
 * @author bragen
 *
 */
public class MessageJava implements Message {
    
    public MessageJava() {
	
    }
    
    public void println(String msg) {
	System.out.println(msg);
    }

}
