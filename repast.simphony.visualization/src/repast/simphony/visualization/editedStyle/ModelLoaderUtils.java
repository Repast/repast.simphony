package repast.simphony.visualization.editedStyle;

import java.io.File;
import java.io.FileNotFoundException;

// import com.glyphein.j3d.loaders.milkshape.MS3DLoader;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.Loader;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.lw3d.Lw3dLoader;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;

/**
 * Utils class for finding a org.jogamp.java3d.loaders.Loader based on the
 *  file type.
 * 
 * @author Eric Tatara
 *
 */
public class ModelLoaderUtils {

	public static Loader getLoaderForFile(String filename){

		if (filename.toLowerCase().endsWith("ms3d"))
			// return new MS3DLoader(MS3DLoader.LOAD_ALL);
			throw new UnsupportedOperationException("MS3D files are no longer supported");
		
		else if (filename.toLowerCase().endsWith("lws") || 
				filename.toLowerCase().endsWith("lwo"))
			return new Lw3dLoader(Lw3dLoader.LOAD_ALL);
		
		else if (filename.toLowerCase().endsWith("obj"))
			return new ObjectFile(ObjectFile.LOAD_ALL);
		
		else return null;
	}

	/**
	 * Load the scene from a provided model file name.
	 * 
	 * @param filename the mode file name
	 * @return the scene
	 */
	public static Scene loadSceneFromModel(File file){
		Scene scene = null;
    Loader loader = null;
		
		try {
			loader = getLoaderForFile(file.getName());
			
//			if (file.getParent().length() > 0) // figure out the base path
//			  loader.setBasePath(file.getParent() + java.io.File.separator);
			
			return loader.load(file.getAbsolutePath());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IncorrectFormatException e) {
			e.printStackTrace();
		} catch (ParsingErrorException e) {
			e.printStackTrace();
		}
		return null;
	}
}