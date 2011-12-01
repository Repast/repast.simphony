package repast.simphony.integration;


public class DataFileReaderTester {

	public static void main(String[] args) {
		DataFileReader parser = new DataFileReader(new BeanBuilder(DemoData.class));

		parser.setDescriptorFileName("test/testFiles/asciiDemoData.txt-def.xml");
		parser.setFileToParseName("test/testFiles/asciiDemoData.txt");
		// parser.descriptorFileName = "testData.txt-def.xml";
		// parser.fileToParseName = "testData.txt";
		try {
			parser.read();

			System.out.println("done");
//			JDOMTreeModel.showInFrame(((Document) parser.getParseResult()).getRootElement(), true);
			
//			CollectionUtils.breadthFirstMap(new Executor<Element>() {
//				public void execute(Element toExecuteOn) {
//					System.out.println(toExecuteOn + toExecuteOn.getValue());
//				}
//			}, new DOMTraverser(), ((Document) parser.getParseResult()).getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// parser
	}
}
