package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.statecharts.TriggerTests.MyMessageEventListener;
import simphony.util.messages.MessageCenter;
import simphony.util.messages.MessageEvent;
import simphony.util.messages.MessageEventListener;

public class MessageCheckerTests {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void unconditionalMessageCheckerTest() {
		UnconditionalMessageChecker umc = new UnconditionalMessageChecker();
		assertEquals(true,umc.isValidMessage("hello"));
		assertEquals(false,umc.isValidMessage(null));
		int msg = 6;
		assertEquals(true,umc.isValidMessage(msg));
	}
	
	// Test classes
	private static interface A {
	}
	private static class B implements A {
	}
	private static class C extends B {
	}
	
	@Test
	public void unconditionalByMessageCheckerTest() {
		UnconditionalByClassMessageChecker ubcmc = new UnconditionalByClassMessageChecker(String.class);
		assertEquals(true,ubcmc.isValidMessage("hello"));
		assertEquals(false,ubcmc.isValidMessage(1));
		assertEquals(false,ubcmc.isValidMessage(new Object()));
		assertEquals(false,ubcmc.isValidMessage(null));
		
		UnconditionalByClassMessageChecker ubcmc2 = new UnconditionalByClassMessageChecker(A.class);
		assertEquals(true,ubcmc2.isValidMessage(new A(){}));
		assertEquals(true,ubcmc2.isValidMessage(new B()));
		assertEquals(true,ubcmc2.isValidMessage(new C()));
		
		UnconditionalByClassMessageChecker ubcmc3 = new UnconditionalByClassMessageChecker(C.class);
		assertEquals(false,ubcmc3.isValidMessage(new B()));
	}
	
	// Test class
	private static class D {
		private int d;
		public D(int d){
			this.d = d;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + d;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			D other = (D) obj;
			if (d != other.d)
				return false;
			return true;
		}
	}
	
	@Test
	public void messageEqualsMessageCheckerTest() {
		MessageEqualsMessageChecker<String> mc1 = new MessageEqualsMessageChecker<String>("hello");
		assertEquals(true,mc1.isValidMessage("hello"));
		assertEquals(false,mc1.isValidMessage("hell"));
		assertEquals(false,mc1.isValidMessage(1));
		assertEquals(false,mc1.isValidMessage(new Object()));
		assertEquals(false,mc1.isValidMessage(new B()));
		
		C c = new C();
		MessageEqualsMessageChecker<C> mc2 = new MessageEqualsMessageChecker<C>(c);
		assertEquals(true,mc2.isValidMessage(c));
		assertEquals(false,mc2.isValidMessage(new C()));
		
		D d = new D(3);
		MessageEqualsMessageChecker<D> mc3 = new MessageEqualsMessageChecker<D>(d);
		assertEquals(true,mc3.isValidMessage(new D(3)));
		assertEquals(false,mc3.isValidMessage(new D(2)));
	}
	
	@Test
	public void messageConditionMessageCheckerTest() {
		MessageConditionMessageChecker mc1 = new MessageConditionMessageChecker(new MessageCondition(){

			@Override
			public boolean isMessageConditionTrue(Object message)
					throws Exception {
				if (message instanceof D){
					D d = (D)message;
					return d.d == 3 ? true : false;
				}
				return false;
			}
			
		});
		assertEquals(true,mc1.isValidMessage(new D(3)));
		assertEquals(false,mc1.isValidMessage(new D(4)));
		assertEquals(false,mc1.isValidMessage("hell"));
		
		MessageConditionMessageChecker mc2 = new MessageConditionMessageChecker(new MessageCondition(){

			@Override
			public boolean isMessageConditionTrue(Object message)
					throws Exception {
				throw new Exception();
			}
			
			@Override
			public String toString() {
				return "Exception throwing MessageCondition";
			}
			
		});
		
		class MyMessageEventListener implements MessageEventListener{

			public boolean messageReceived = false;
			public String message;
			
			@Override
			public void messageReceived(MessageEvent me) {
				messageReceived = true;
				message = (String) me.getMessage();
			}
			
		}
		
		MyMessageEventListener mel = new MyMessageEventListener();
		MessageCenter.addMessageListener(mel);
		assertEquals(false, mel.messageReceived);
		assertEquals(false, mc2.isValidMessage("hello"));
		assertEquals(true, mel.messageReceived); 
		assertEquals("Error encountered when calling message condition in: MessageConditionMessageChecker with Exception throwing MessageCondition",mel.message);
		
	}

}
