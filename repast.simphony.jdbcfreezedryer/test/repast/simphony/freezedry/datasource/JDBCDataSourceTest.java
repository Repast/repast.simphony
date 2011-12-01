/*CopyrightHere*/
package repast.simphony.freezedry.datasource;

import junit.framework.TestCase;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryingException;
import simphony.util.messages.Log4jMessageListener;

import java.util.Arrays;

public class JDBCDataSourceTest extends TestCase {

	public static class Foo {
		private String val = "FooVal";

		private double valDouble = 1.0;

		public void setVal(String val) {
			this.val = val;
		}

		public String getVal() {
			return val;
		}

		public double getValDouble() {
			return valDouble;
		}

		public void setValDouble(double valDouble) {
			this.valDouble = valDouble;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Foo) {
				Foo foo = (Foo) obj;
				return foo.val.equals(val) && foo.valDouble == valDouble;
			}
			return false;
		}
	}

	public static class Bar extends Foo {
		private Integer barVal = 1234;

		double[] doubles = new double[] { 0.0, 1.1, 2.2 };

		public Integer getBarVal() {
			return barVal;
		}

		public void setBarVal(Integer barVal) {
			this.barVal = barVal;
		}

		public double[] getDoubles() {
			return doubles;
		}

		public void setDoubles(double[] doubles) {
			this.doubles = doubles;
		}

		@Override
		public boolean equals(Object arg0) {
			if (arg0 instanceof Bar) {
				Bar bar = (Bar) arg0;
				return super.equals(arg0) && barVal.equals(bar.barVal)
					&& Arrays.equals(doubles, bar.doubles);
			}
			return false;
		}
		
		@Override
		public String toString() {
			return "barVal: " + barVal + ", doubles:" + Arrays.toString(doubles);
		}
	}

	public static class FooBar {
		Bar bar = new Bar();

		Foo foo = new Foo();

		public Bar getBar() {
			return bar;
		}

		public void setBar(Bar bar) {
			this.bar = bar;
		}

		public Foo getFoo() {
			return foo;
		}

		public void setFoo(Foo foo) {
			this.foo = foo;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FooBar) {
				FooBar fooBar = (FooBar) obj;
				return fooBar.bar.equals(bar) && fooBar.foo.equals(foo);
			}
			return false;
		}
	}
	

	public void testAll() throws FreezeDryingException {
		Log4jMessageListener.loadDefaultSettings();
		FreezeDryedRegistry reg = new FreezeDryedRegistry();
		Foo foo = new Foo();
		Bar bar = new Bar();
		FooBar fooBar = new FooBar();

		// start up an hsqldb and use it
		JDBCDataSource source = new JDBCDataSource("jdbc:hsqldb:mem:aname", "org.hsqldb.jdbcDriver", "sa", "");
//		JDBCDataSource source = new JDBCDataSource("jdbc:mysql://localhost/test", "com.mysql.jdbc.Driver", "root", "password");
		
		reg.setDataSource(source);
		
		
		String idFoo = reg.freezeDry(foo);
		String idBar = reg.freezeDry(bar);
		String idFooBar = reg.freezeDry(fooBar);
		

		Foo fooRehydrated = reg.getObject(idFoo, Foo.class);
		Bar barRehydrated = reg.getObject(idBar, Bar.class);
		FooBar fooBarRehydrated = reg.getObject(idFooBar, FooBar.class);

		assertEquals(foo, fooRehydrated);

		assertEquals(bar, barRehydrated);

		assertEquals(fooBar, fooBarRehydrated);
	}
}
