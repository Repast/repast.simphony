package repast.simphony.integration;

import org.apache.commons.jxpath.JXPathContext;

public class BeanXPathTest {
	public static class SubChild {
		public String getSubChildProperty() {
			return "subChildValue";
		}
	}
	public static class Child {
		SubChild subChild = new SubChild();
		
		public SubChild getSubChild() {
			return subChild;
		}
		
		public String getChildProperty() {
			return "childValue";
		}
	}
	public static class Root {
		Child child = new Child();
		
		public Child getChild() {
			return child;
		}
		
		public String getRootProperty() {
			return "rootPropertyValue";
		}
	}
	
	public static void test1() {
		Root root = new Root();
		JXPathContext rootContext = JXPathContext.newContext(root);
		rootContext.setLenient(true);
		JXPathContext childContext = JXPathContext.newContext(rootContext, root.child);
		childContext.setLenient(true);
		JXPathContext subChildContext = JXPathContext.newContext(childContext, root.child.subChild);
		subChildContext.setLenient(true);
		

		System.out.println("**********************");
		System.out.println("Nested classes and contexts Test");
		System.out.println("subChildContext:");
		test(rootContext, subChildContext, "subChildProperty");
		test(rootContext, subChildContext, "./subChildProperty");
		test(rootContext, subChildContext, ".");
		test(rootContext, subChildContext, "../.");
		test(rootContext, subChildContext, "//subChildProperty");
		test(rootContext, subChildContext, "/child/subChild/subChildProperty");
		test(rootContext, subChildContext, "/child/subChild/@subChildProperty");
		test(rootContext, subChildContext, "/");
		test(rootContext, subChildContext, "//rootProperty");
		test(rootContext, subChildContext, "/rootProperty");
		test(rootContext, subChildContext, "/RootProperty");
		test(rootContext, subChildContext, "../subChild/subChildProperty");
		
		System.out.println("rootContext:");
		test(rootContext, subChildContext, "/rootProperty");
		test(rootContext, rootContext, "child/subChild/subChildProperty");
		test(rootContext, rootContext, "//subChildProperty");
	}
	
	public static void testDemoData() {
		DemoData data = new DemoData();
		data.title = "titleValue";
		data.date = "dateValue";

		data.spectrometerType = 1;

		// data.spectralData;

		data.xAxisType = "xAxisTypeValue";
		data.numXCols = 2;
		// data.xData;

		data.numCalibrationRuns = 3;
		JXPathContext context = JXPathContext.newContext(data);
		context.setLenient(true);
		
		System.out.println("**********************");
		System.out.println("DemoData Test");
		test(context, context, "/");
		test(context, context, "/demoData");
		test(context, context, "/title");
		test(context, context, "title");
		test(context, context, "numXCols");
		test(context, context, "demoData/numXCols");
		test(context, context, "/numXCols");
		test(context, context, "//numXCols");
		test(context, context, "../numXCols");
	}
	
	private static void test(JXPathContext rootContext, JXPathContext context, String path) {
		System.out.println("\t" + path + ":\t (" + JXPathUtils.getXPathNode(rootContext, context, path) + ")");
	}
	
	public static void testDemoDataFileDef() {
		DemoData data = new DemoData();
		
		FileDef boundRoot = new FileDef(data);
		
		data.title = "titleValue";
		data.date = "dateValue";

		data.spectrometerType = 1;

		// data.spectralData;

		data.xAxisType = "xAxisTypeValue";
		data.numXCols = 2;
		// data.xData;

		data.numCalibrationRuns = 3;
		JXPathContext rootContext = JXPathContext.newContext(boundRoot);
		rootContext.setLenient(true);
		JXPathContext context = JXPathContext.newContext(data);
		context.setLenient(true);
		
		System.out.println("**********************");
		System.out.println("Bound DemoData Test");
		test(rootContext, context, "/");
		test(rootContext, context, "/fileDef");
		test(rootContext, context, "fileDef");
		test(rootContext, context, "/fileDef");
		test(rootContext, context, "fileDef");
		test(rootContext, context, "/DemoData");
		test(rootContext, context, "DemoData");
		test(rootContext, context, "title");
		test(rootContext, context, "numXCols");
		test(rootContext, context, "DemoData/numXCols");
		test(rootContext, context, "/fileDef/numXCols");
		test(rootContext, context, "/numXCols");
		test(rootContext, context, "//numXCols");
		test(rootContext, context, "../numXCols");
		test(rootContext, context, "/fileDef/fileDef/title");
		test(rootContext, context, "numXCols");
		test(rootContext, context, "fileDef/numXCols");
		test(rootContext, context, "fileDef/fileDef/title");
	}
	
	public static void main(String[] args) {
		test1();
		testDemoData();
		testDemoDataFileDef();
	}
}
