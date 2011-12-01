/*CopyrightHere*/
package repast.simphony.integration;



public class ReadWriteTester {
	public static void main(String[] args) {
		DataFileReader parser = new DataFileReader(new BeanBuilder(DemoData.class));

		parser.setDescriptorFileName("test/testFiles/asciiDemoData.txt-def.xml");
		parser.setFileToParseName("test/testFiles/asciiDemoData.txt");
		// parser.descriptorFileName = "testData.txt-def.xml";
		// parser.fileToParseName = "testData.txt";
		try {
			parser.read();

			System.out.println("done reading");
//			JDOMTreeModel.showInFrame(((Document) parser.getParseResult()).getRootElement(), true);
			
//			CollectionUtils.breadthFirstMap(new Executor<Element>() {
//				public void execute(Element toExecuteOn) {
//					System.out.println(toExecuteOn + toExecuteOn.getValue());
//				}
//			}, new DOMTraverser(), ((Document) parser.getParseResult()).getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DataFileWriter writer = new DataFileWriter(new BeanQueryer(parser
				.getParseResult()));
		writer.setDestFileName("asciiDemoDataOutput.txt");
		writer.setDescriptorFileName("test/testFiles/asciiDemoData.txt-def.xml");
		try {
			writer.write();

			System.out.println("done writing");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
