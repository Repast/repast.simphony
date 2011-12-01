/**
 * 
 */
package repast.simphony.conversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Eclipse object action that converts the selected model.score to a context.xml
 * and user_path.xml.
 * 
 * @author Nick Collier
 */
public class ScenarioConverterAction implements IObjectActionDelegate {

  private ISelection selection;

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
   */
  @Override
  public void run(IAction action) {
    IFolder ifile = (IFolder) ((IStructuredSelection) selection).getFirstElement();
    File file = new File(ifile.getLocation().toOSString());
    File scoreFile = new File(file, "model.score");

    try {
      ScoreToCM transformer = new ScoreToCM();
      transformer.run(scoreFile);
      
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file, "user_path.xml")));
      
      ScoreToUserPath trans = new ScoreToUserPath();
      trans.run(scoreFile, writer);
      writer.close();
      
      processScenarioXML(file);
      ifile.refreshLocal(IResource.DEPTH_ONE, null);

    } catch (TransformerException ex) {
      ex.printStackTrace();

    } catch (IOException e) {
      
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  private void processScenarioXML(File scenarioDir) throws IOException, XMLStreamException {
    File scenarioFile = new File(scenarioDir, "scenario.xml");
    copyFile(scenarioFile);
    File tmpFile = new File(scenarioDir, "tmp.xml");
    BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));
    ScenarioXMLConverter converter = new ScenarioXMLConverter();
    converter.run(scenarioFile, writer);
    writer.flush();
    writer.close();
    
    scenarioFile.delete();
    tmpFile.renameTo(scenarioFile);
    tmpFile.delete();
  }
  
  private void copyFile(File source) throws IOException {
    File backup = new File(source.getCanonicalPath() + ".backup");
    if (!backup.exists()) {
      FileChannel srcChannel = new FileInputStream(source).getChannel();
      // Create channel on the destination
      backup.createNewFile();
      FileChannel dstChannel = new FileOutputStream(backup).getChannel();

      // Copy file contents from source to destination
      dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

      // Close the channels
      srcChannel.close();
      dstChannel.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
   * .IAction, org.eclipse.jface.viewers.ISelection)
   */
  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    this.selection = selection;

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action
   * .IAction, org.eclipse.ui.IWorkbenchPart)
   */
  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
  }

}
