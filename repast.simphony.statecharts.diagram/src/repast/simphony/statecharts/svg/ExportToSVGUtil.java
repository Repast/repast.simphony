package repast.simphony.statecharts.svg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;

public class ExportToSVGUtil {

    /**
     * Copies the diagram to an image file in the specified format.
     * 
     * @param diagramEP
     *            the diagram editpart
     * @param destination
     *            the destination file, including path and file name
     * @param format
     *            the image format to create.
     * @param monitor
     *            progress monitor.
     * @return The diagram generator used to copy the image.
     * @exception CoreException
     *                if this method fails
     */
    public SVGGenerator copyToImage(DiagramEditPart diagramEP,
            IPath destination, IProgressMonitor monitor)
        throws CoreException {
    	SVGGenerator sg = new SVGGenerator(diagramEP);
        List editParts = diagramEP.getPrimaryEditParts();
        copyToImage(sg, editParts, sg.calculateImageRectangle(editParts), destination,  monitor);
        monitor.worked(1);
        return sg;
    }
    
    /**
	 * Generates image of editparts with on a given image rectangle and creates
	 * the specified image file containing this image. The image rectangle may
	 * be the limitation for the editparts displayed on the image
	 * 
	 * @param gen
	 *            diagram generator
	 * @param editParts
	 *            editparts to be present on the image
	 * @param imageRect
	 *            clipping rectangle for the image
	 * @param destination
	 *            image file path
	 * @param format
	 *            image file format
	 * @param monitor
	 *            progress monitor
	 * @throws CoreException
	 */
    protected void copyToImage(SVGGenerator gen, List editParts,
			org.eclipse.swt.graphics.Rectangle imageRect, IPath destination,
			IProgressMonitor monitor)
			throws CoreException {
		
			gen.renderPartsToGraphics(editParts, imageRect);
			monitor.worked(1);
			saveToFile(destination, gen, monitor);
	}

    
    /**
	 * Saves an SVG files.
	 * 
	 * @param destination
	 *            the destination file, including path and file name
	 * @param generator
	 *            the svg generator for a diagram, used to write
	 * @param format
	 *            currently supports SVG
	 * @param monitor
	 *            the progress monitor
	 * @exception CoreException
	 *                if this method fails
	 */
    protected void saveToFile(IPath destination,
            SVGGenerator generator, IProgressMonitor monitor)
        throws CoreException {

		IStatus fileModificationStatus = createFile(destination);
		if (!fileModificationStatus.isOK()) {
			// can't write to the file
			return;
		}
		monitor.worked(1);

		try {
			FileOutputStream os = new FileOutputStream(destination.toOSString());
			monitor.worked(1);
			saveToOutputStream(os, generator, monitor);
			os.close();
			monitor.worked(1);
			refreshLocal(destination);
		} catch (IOException ex) {
			Log.error(DiagramUIRenderPlugin.getInstance(), IStatus.ERROR, ex
					.getMessage(), ex);
			IStatus status = new Status(IStatus.ERROR,
					"exportToFile", IStatus.OK, //$NON-NLS-1$
					ex.getMessage(), null);
			throw new CoreException(status);
		}
	}
        
    private void saveToOutputStream(OutputStream stream, SVGGenerator generator, IProgressMonitor monitor) throws CoreException {
    	generator.stream(stream);
		monitor.worked(1);
    }

    /**
     * create a file in the workspace if the destination is in a project in the
     * workspace.
     * 
     * @param destination
     *            the destination file.
     * @return the status from validating the file for editing
     * @exception CoreException
     *                if this method fails
     */
    private IStatus createFile(IPath destination)
        throws CoreException {
        IFile file = ResourcesPlugin.getWorkspace().getRoot()
            .getFileForLocation(destination);
        if (file != null && !file.exists()) {
            File osFile = new File(destination.toOSString());
            if (osFile.exists()) {
                file.refreshLocal(IResource.DEPTH_ZERO, null);
            } else {
                ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
                    IResource.DEPTH_INFINITE, null);
                InputStream input = new ByteArrayInputStream(new byte[0]);
                file.create(input, false, null);
            }
        }

        if (file != null) {
        	return FileModificationValidator.approveFileModification(new IFile[] {file});
        }
        return Status.OK_STATUS;
    }
    
    /**
     * refresh the file in the workspace if the destination is in a project in
     * the workspace.
     * 
     * @param destination
     *            the destination file.
     * @exception CoreException
     *                if this method fails
     */
    private void refreshLocal(IPath destination)
        throws CoreException {
        IFile file = ResourcesPlugin.getWorkspace().getRoot()
            .getFileForLocation(destination);
        if (file != null) {
            file.refreshLocal(IResource.DEPTH_ZERO, null);
        }
    }

}
