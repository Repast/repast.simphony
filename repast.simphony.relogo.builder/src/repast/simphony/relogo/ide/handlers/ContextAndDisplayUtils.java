package repast.simphony.relogo.ide.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class ContextAndDisplayUtils {

	private static String getStringFromInputStream(InputStream is) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, "UTF-8");
		return writer.toString();
	}

	private static String getStringContentsFromIFile(IFile file) throws CoreException, IOException {
		InputStream is = file.getContents();
		String contents = null;
		try{
			contents = getStringFromInputStream(is);
		}
		finally{
			is.close();
		}
		return contents;
	}

}
