package repast.simphony.relogo.styles

import groovy.io.FileType 
import javax.imageio.ImageIO 

class StyleUtility {
	public static Map<String,String> getSVGFileNamesAndPaths(File shapesDirectory){
		Map<String,String> svgFileNamesAndPaths = [:]
		shapesDirectory.eachFileRecurse(FileType.FILES){ File file ->
			if (file.getName().endsWith(".svg")){
				int index = file.getName().lastIndexOf(".")
				if (index > 0){
					svgFileNamesAndPaths.put(file.getName().substring(0, index), file.getAbsolutePath())
				}
			}
		}
		return svgFileNamesAndPaths
	}
	
	/**
	 * Returns a map of file names and absolute paths of ImageIO readable files.
	 * @param shapesDirectory
	 * @return
	 */
	public static Map<String,String> getImageFileNamesAndPaths(File shapesDirectory){
		Map<String,String> imageFileNamesAndPaths = [:]
		List<String> readableSuffixes = Arrays.asList(ImageIO.getReaderFileSuffixes())
		
		shapesDirectory.eachFileRecurse(FileType.FILES){ File file ->
			if (readableSuffixes.contains(getExtension(file))){
				int index = file.getName().lastIndexOf(".")
				if (index > 0){
					imageFileNamesAndPaths.put(file.getName().substring(0, index), file.getAbsolutePath())
				}
			}
		}
		return imageFileNamesAndPaths
	}
	
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
 
		if (f.isDirectory())
			ext = null;
		else if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}
	
	// getReaderFileSuffixes
}
