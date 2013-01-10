package repast.simphony.statecharts.svg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramSVGGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.util.CopyToImageUtil;
import org.eclipse.gmf.runtime.diagram.ui.util.DiagramEditorUtil;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public class NotationReader {

	public void readNotation(IPath path) {
		ResourceSet resourceSet = new ResourceSetImpl();
		GMFEditingDomainFactory.getInstance().createEditingDomain(resourceSet);
		Resource resource = resourceSet
				.createResource(org.eclipse.emf.common.util.URI
						.createPlatformResourceURI(path.toString(), true));
		try {
			resource.load(new FileInputStream(path.toFile()),
					new HashMap<Object, Object>());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		readNotation(resource);
	}

	public void readNotation(Resource resource) {

		ResourceSet rset = resource.getResourceSet(); // this is null
		TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain(rset);
		editingDomain
				.setID("repast.simphony.statecharts.diagram.EditingDomain");
		Diagram diagram = null;

		for (EObject obj : resource.getContents()) {

			if (obj.eClass().equals(NotationPackage.Literals.DIAGRAM)) {
				diagram = (Diagram) obj;
				break;
			}
		}

		DiagramEditPart dep = OffscreenEditPartFactory.getInstance()
				.createDiagramEditPart(diagram, new Shell());
		try {
			new ExportToSVGUtil().copyToImage(dep, new Path(
					"/Users/jozik/Desktop/temp/customhellotest.svg"),
					new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Image getDiagramImage(Diagram diagram,
			PreferencesHint preferencesHint) {
		Image image = null;
		DiagramEditor openedDiagramEditor = DiagramEditorUtil
				.findOpenedDiagramEditorForID(ViewUtil.getIdStr(diagram));

		if (openedDiagramEditor != null) {
			image = getDiagramImage(openedDiagramEditor.getDiagramEditPart());
		} else {
			DiagramEditPart diagramEditPart = OffscreenEditPartFactory
					.getInstance().createDiagramEditPart(diagram, new Shell(),
							preferencesHint);
			Assert.isNotNull(diagramEditPart);
			image = getDiagramImage(diagramEditPart);
			diagramEditPart.deactivate();
		}

		return image;
	}

	public static Image getDiagramImage(DiagramEditPart diagramEP) {
		DiagramGenerator gen = new DiagramImageGenerator(diagramEP);
		List<?> editParts = diagramEP.getPrimaryEditParts();
		Rectangle rectangle = gen.calculateImageRectangle(editParts);
		ImageDescriptor descriptor = gen.createSWTImageDescriptorForParts(
				editParts, rectangle);
		return descriptor.createImage();
	}

}
