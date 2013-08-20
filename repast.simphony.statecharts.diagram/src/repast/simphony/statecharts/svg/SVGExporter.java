package repast.simphony.statecharts.svg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.OffscreenEditPartFactory;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator;
import org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramImageGenerator;
import org.eclipse.gmf.runtime.diagram.ui.util.DiagramEditorUtil;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;

public class SVGExporter {

	public void run(IPath path, final IPath srcPath, final IProgressMonitor monitor) {

		ResourceSet resourceSet = new ResourceSetImpl();
		GMFEditingDomainFactory.getInstance().createEditingDomain(resourceSet);
		Resource resource = resourceSet.createResource(org.eclipse.emf.common.util.URI
				.createPlatformResourceURI(path.toString(), true));
		try {
			resource.load(new FileInputStream(path.toFile()), new HashMap<Object, Object>());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		final OutPathInfo outPathInfo = new OutPathInfo();
		final Diagram diagram = readNotation(resource, monitor, outPathInfo);

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				DiagramEditPart dep = OffscreenEditPartFactory.getInstance().createDiagramEditPart(diagram,
						new Shell());

				IPath outPath = getOutPath(srcPath, outPathInfo);
				try {
					if (monitor == null) {
						new ExportToSVGUtil().copyToImage(dep, outPath, new NullProgressMonitor());
					} else {
						new ExportToSVGUtil().copyToImage(dep, outPath, monitor);
					}
				} catch (CoreException e) {
					StatechartDiagramEditorPlugin.getInstance().
					logError("Error while exporting SVG", e);
				}
			}

		});

	}

	private IPath getOutPath(IPath srcPath, OutPathInfo outPathInfo) {
		IPath result = srcPath;
		String pkg = outPathInfo.pkg;
		if (pkg != null) {
			for (String segment : pkg.split("\\.")) {
				result = result.append(segment);
			}
		}
		String className = outPathInfo.className;
		result = result.append(className + ".svg");
		return result;
	}

	/**
	 * Utility class to retrieve package and class name information.
	 * 
	 * @author jozik
	 * 
	 */
	private static class OutPathInfo {
		String pkg, className;
	}

	private Diagram readNotation(Resource resource, IProgressMonitor monitor, OutPathInfo names) {

		ResourceSet rset = resource.getResourceSet();
		TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain(rset);
		editingDomain.setID("repast.simphony.statecharts.diagram.EditingDomain");
		StateMachine statemachine = null;
		Diagram diagram = null;

		for (EObject obj : resource.getContents()) {

			if (obj.eClass().equals(StatechartPackage.Literals.STATE_MACHINE)) {
				statemachine = (StateMachine) obj;
				continue;
			}

			if (obj.eClass().equals(NotationPackage.Literals.DIAGRAM)) {
				diagram = (Diagram) obj;
			}
		}

		names.className = statemachine.getClassName();
		names.pkg = statemachine.getPackage();

		return diagram;
	}

	public static Image getDiagramImage(Diagram diagram, PreferencesHint preferencesHint) {
		Image image = null;
		DiagramEditor openedDiagramEditor = DiagramEditorUtil.findOpenedDiagramEditorForID(ViewUtil
				.getIdStr(diagram));

		if (openedDiagramEditor != null) {
			image = getDiagramImage(openedDiagramEditor.getDiagramEditPart());
		} else {
			DiagramEditPart diagramEditPart = OffscreenEditPartFactory.getInstance()
					.createDiagramEditPart(diagram, new Shell(), preferencesHint);
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
		ImageDescriptor descriptor = gen.createSWTImageDescriptorForParts(editParts, rectangle);
		return descriptor.createImage();
	}

}
