/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xpand2.output.FileHandle;
import org.eclipse.xpand2.output.PostProcessor;

/**
 * Runs the CodeFormatter on a directory of source files.
 * 
 * @author Nick Collier
 */
public class CodeBeautifier implements PostProcessor {

  private CodeFormatter codeFormatter;

  @Override
  public void beforeWriteAndClose(FileHandle file) {
    if (file.getAbsolutePath() != null && (file.getAbsolutePath().endsWith("java") || file.getAbsolutePath().endsWith(".groovy"))) {
      IDocument doc = new Document(file.getBuffer().toString());
      TextEdit edit = getCodeFormatter().format(CodeFormatter.K_COMPILATION_UNIT, doc.get(), 0,
          doc.get().length(), 0, null);

      // check if text formatted successfully
      if (edit != null) {
        try {
          edit.apply(doc);
          file.setBuffer(new StringBuffer(doc.get()));
          
        } catch (MalformedTreeException e) {
          
          // log.warn("Error during code formatting. Illegal code edit tree (" +
          // e.getMessage() + ").");
        } catch (BadLocationException e) {
          // log.warn("Error during code formatting. Bad location (" +
          // e.getMessage() + ").");
        }
      } 
    }
  }

  private CodeFormatter getCodeFormatter() {
    if (codeFormatter == null) {
      @SuppressWarnings("unchecked")
      Map<String, String> options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();

      options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
      options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
      options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);

      // instantiate the default code formatter with the given options
      codeFormatter = ToolFactory.createCodeFormatter(options);
    }
    return codeFormatter;
  }

  @Override
  public void afterClose(FileHandle impl) {
  }
}
