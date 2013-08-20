package repast.simphony.relogo.ide.image;

import groovy.swing.SwingBuilder

import java.awt.Color
import java.awt.FlowLayout

import javax.swing.DefaultComboBoxModel
import javax.swing.JFrame

import repast.simphony.relogo.image.NLImage

import com.thoughtworks.xstream.XStream

/**
 * @author jozik
 *
 */

public class ImageTesterG {
	
	public static String netLogoFile = "/Applications/NetLogo 4.0.4/models/Sample Models/Biology/Rabbits Grass Weeds.nlogo"
	
	ImagePanel customSurface = new ImagePanel()
	List<NLImage> imageList = []
	int currentImageIndex = 0
	DefaultComboBoxModel imageSelectionModel = new DefaultComboBoxModel();
	
	void scanNLFile(rdr){
		if (scan(rdr)){
			imageList.each{
				imageSelectionModel.addElement(it.getName());
			}
			imageSelectionModel.setSelectedItem(imageSelectionModel.getElementAt(currentImageIndex));
			customSurface.setImage(imageList.get(currentImageIndex));
		}
	}
	
	void buildSwing(){
		SwingBuilder swing = new SwingBuilder()
		def comboBoxAction = {e -> 
			currentImageIndex = imageSelectionModel.getIndexOf(imageSelectionModel.getSelectedItem());
			imageSelectionModel.setSelectedItem(imageSelectionModel.getElementAt(currentImageIndex));
			customSurface.setImage(imageList.get(currentImageIndex));
		}
		
		def buttonAction = {e ->
			currentImageIndex++
			if (currentImageIndex >= imageList.size()) {
				currentImageIndex = 0;
			}
			imageSelectionModel.setSelectedItem(imageSelectionModel.getElementAt(currentImageIndex));
			customSurface.setImage(imageList.get(currentImageIndex));
		}
		customSurface.setBackground(new java.awt.Color(51, 51, 51));
		customSurface.setForeground(new java.awt.Color(255, 255, 51));
		customSurface.setPreferredSize(new java.awt.Dimension(300, 300));
		customSurface.setLayout(new java.awt.BorderLayout());
		customSurface.setBorder(swing.lineBorder(color: Color.red))
		def mainFrame = swing.frame(title: "Image Displayer", defaultCloseOperation: JFrame.EXIT_ON_CLOSE, layout: new FlowLayout()){
			widget(customSurface)
			button(text: 'next', actionPerformed: buttonAction)
			comboBox(model: imageSelectionModel, actionPerformed: comboBoxAction)
		}
		mainFrame.pack()
		mainFrame.show()
	}
	
	public static void main(def args) {
		ImageTesterG im = new ImageTesterG()
		FileReader rdr = new FileReader(netLogoFile);
		im.scanNLFile(rdr)
//		im.serializeImages()
		im.buildSwing()
	}

	public void serializeImages(){
		XStream xstream = new XStream()
		String xml = xstream.toXML(imageList)
//		StringBuffer xml = new StringBuffer()
		
//		for (image in imageList){
//			xml << xstream.toXML(image)
//		}
		File file = new File("turtleShapes.xml")
		file.write(xml)
		String body = new File("turtleShapes.xml").text
		List inputList = (List)xstream.fromXML(body)
		println "the new inputList is ${inputList.size()} long"
		
	}
	
	
	
	
	
	
	public boolean scan(Reader r) {
		BufferedReader rdr = new BufferedReader(r);
		int sectionIndex = 0;
		int linesInSection = 0;
		StringBuffer sectionBuffer = new StringBuffer();
		try {
			ImageSectionParser turtleParser = null;
			for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
				if (line.equals('@#$#@#$#@')) {
					// code_section windows_section doc_section image_section ver_section pre_section ser_section bsp_section?
					switch (sectionIndex) {
						case 0: // code section
							break;
						case 1: // control section
							break;
						case 2: // documentation section
							break;
						case 3: // turtle vector graphics section
							turtleParser = new ImageSectionParser(sectionBuffer);
							println "in case 3"
							if (turtleParser.errorCount() == 0) {
								this.imageList = turtleParser.getModel();
								println "setting imageList to ${imageList}"
							}
							break;
						case 4: // NetLogo version number section
							break;
						case 5: // preview section
							break;
						case 6: // System Dynamics section
							break;
						case 7: // Behavior Space section (XML-ish)
							break;
						case 8: // HubNet section (similar to controls section)
							break;
						case 9: // ??? section
							break;
						default: // any other sections
							break;
					}
					sectionIndex++;
					linesInSection = 0;
					sectionBuffer.setLength(0);
				} else {
					sectionBuffer.append(line);
					sectionBuffer.append('\n');
					linesInSection++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
}