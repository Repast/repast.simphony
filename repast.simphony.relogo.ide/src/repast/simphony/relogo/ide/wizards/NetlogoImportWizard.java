package repast.simphony.relogo.ide.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.codehaus.groovy.eclipse.dsl.RefreshDSLDJob;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import repast.simphony.eclipse.RSProjectConfigurator;
import repast.simphony.eclipse.RepastSimphonyPlugin;
import repast.simphony.eclipse.util.Utilities;
import repast.simphony.relogo.ide.ReLogoFilter;
import repast.simphony.relogo.ide.code.Attribute;
import repast.simphony.relogo.ide.code.ProcedureDefinition;
import repast.simphony.relogo.ide.code.ProcedureInvocation;
import repast.simphony.relogo.ide.code.RelogoClass;
import repast.simphony.relogo.ide.handlers.ReLogoBuilder;
import repast.simphony.relogo.ide.handlers.ReLogoBuilderTests;
import repast.simphony.relogo.ide.intf.NLChooser;
import repast.simphony.relogo.ide.intf.NLControl;
import repast.simphony.relogo.ide.intf.NLGraphicsWindow;
import repast.simphony.relogo.ide.intf.NLInputBox;
import repast.simphony.relogo.ide.intf.NLMonitor;
import repast.simphony.relogo.ide.intf.NLSlider;
import repast.simphony.relogo.ide.intf.NLSwitch;
import repast.simphony.relogo.image.NLImage;

import com.thoughtworks.xstream.XStream;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "nlogo". If
 * a sample multi-page editor (also available as a template) is registered for
 * the same extension, it will be able to open it.
 */

public class NetlogoImportWizard extends NewElementWizard implements IImportWizard {

	static Logger logger = Logger.getLogger(NetlogoImportWizard.class);

	private static final String PAGE_NAME = "NewRelogoProjectWizardPage"; //$NON-NLS-1$
	private static final String SRC_GEN = "src-gen";
	private static final String SRC = "src";

	protected NetlogoWizardPageOne pageOne;
	protected NetlogoWizardPageTwo pageTwo;
	protected IConfigurationElement fConfigElement;

	private IFolder shapesFolder;

	/**
	 * Constructor for NetlogoImportWizard.
	 */
	public NetlogoImportWizard() {
		super();
		setNeedsProgressMonitor(true);
		BasicConfigurator.configure();
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		/*
		 * A convenient way to list nature descriptors, since the workspace is
		 * typically not available unless in a plugin of some kind.
		 * IProjectNatureDescriptor[] descriptors =
		 * ResourcesPlugin.getWorkspace().getNatureDescriptors(); for (int i=0;
		 * i<descriptors.length; i++) {
		 * System.out.println("*** Nature descriptor "+i);
		 * System.out.println("\tLabel:\t"+descriptors[i].getLabel());
		 * System.out.println("\tID:\t"+descriptors[i].getNatureId());
		 * System.out.println("\tSet IDs:\t"+descriptors[i].getNatureSetIds());
		 * System .out.println("\tReqd:\t"+descriptors[i].getRequiredNatureIds());
		 * System.out.println("\tLink?:\t"+descriptors[i].isLinkingAllowed()); }
		 */
		pageOne = new NetlogoWizardPageOne(false);
		addPage(pageOne);
		pageTwo = new NetlogoWizardPageTwo(pageOne);
		addPage(pageTwo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse
	 * .core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		pageTwo.performFinish(monitor); // use the full progress monitor
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		// final String containerName = page.getContainerName();
		final String projectName = pageOne.getProjectName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					boolean res = doFinish(projectName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	/*
	 * @see Wizard#performFinish
	 */
	public boolean superPerformFinish(IProgressMonitor monitor) {
		try {
			finishPage(monitor);
		} catch (CoreException e) {
			return false;
		} catch (InterruptedException e) {
			throw new OperationCanceledException(e.getMessage());
		}
		return true;
	}

	protected void selectAndReveal(IResource newResource) {
		// For reasons as yet unidentified, calling this is causing
		// Eclipse to throw an error dialog with no message.
		// So I'm removing it for now.
		// BasicNewResourceWizard.selectAndReveal(newResource,
		// getWorkbench().getActiveWorkbenchWindow());
	}

	public static IPersistentPreferenceStore preferenceStore(final IProject project) {
		return new ScopedPreferenceStore(new ProjectScope(project),
				"org.codehaus.groovy.eclipse.preferences");
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private boolean doFinish(String projectName, IProgressMonitor monitor) throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.subTask("Creating " + projectName);
		// create a sample file
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(projectName));
		boolean res = superPerformFinish(subMonitor.newChild(40)); // super.performFinish();
		if (res) {
			final IJavaElement newElement = getCreatedElement();

			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
			// here I think is where we should create the special project
			// contents
			IProject relogoProject = pageTwo.getJavaProject().getProject();

			// String groovyOutputPath =
			// relogoProject.getProject().getLocation().toString() + "/bin";
			// preferenceStore(relogoProject).setValue(
			// "groovy.compiler.output.path", groovyOutputPath );;
			try {
				createStandardRelogoDirectories(relogoProject, subMonitor.newChild(50));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// RepastSimphonyPlugin.getInstance().addRepastSimphonyNature(relogoProject,monitor,
			// false,true);
			// selectAndReveal(relogoProject);

			final IJavaProject javaProjectRef = pageTwo.getJavaProject();

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					// IWorkbenchPart activePart = getActivePart();
					// if (activePart instanceof IPackagesViewPart) {
					// (new ShowInPackageViewAction(activePart.getSite()))
					// .run(newElement);
					// }
					IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					if (workbenchWindow != null) {
						IViewPart view = workbenchWindow.getActivePage().findView(JavaUI.ID_PACKAGES);
						if (view != null && view instanceof IPackagesViewPart) {
							IPackagesViewPart pv = (IPackagesViewPart) view;
							ViewerFilter[] filters = pv.getTreeViewer().getFilters();
							boolean filtered = false;
							for (ViewerFilter vf : filters) {
								if (vf instanceof ReLogoFilter) {
									filtered = true;
								}
							}
							// If ReLogoFilter is active, expand to default level, otherwise
							// leave closed.
							if (filtered) {
								pv.getTreeViewer().expandToLevel(javaProjectRef, 3);
								if (shapesFolder != null) {
									pv.getTreeViewer().collapseToLevel(shapesFolder, AbstractTreeViewer.ALL_LEVELS);
								}
							}
						}
					}
				}
			});
			
		// Ensure that dslds are all available
      final RefreshDSLDJob job = new RefreshDSLDJob(relogoProject);
      job.run(new NullProgressMonitor());
		}
		subMonitor.worked(10);
		return res;
	}

	private IWorkbenchPart getActivePart() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWindow = wb.getActiveWorkbenchWindow();
		if (activeWindow != null) {
			IWorkbenchPage activePage = activeWindow.getActivePage();
			if (activePage != null) {
				return activePage.getActivePart();
			}
		}
		return null;
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "relogo", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// could potentially select a Netlogo file and use that as
		// the default starting point for this wizard.
		// Not currently planning to do that.
		// this.selection = selection;
	}

	/*
	 * Stores the configuration element for the wizard. The config element will be
	 * used in <code>performFinish</code> to set the result perspective.
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		fConfigElement = cfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IWizard#performCancel()
	 */
	public boolean performCancel() {
		pageTwo.performCancel();
		return super.performCancel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.internal.ui.wizards.NewElementWizard#getCreatedElement()
	 */
	public IJavaElement getCreatedElement() {
		return pageTwo.getJavaProject();
	}

	private IPath checkAndGetSourcePath(String srcPathName, IProject project,
			IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
		// Create src-gen directory
		IPath srcPath = javaProject.getPath().append(srcPathName + "/");
		// project relative
		IFolder srcGenfolder = project.getFolder(srcPathName);

		if (!srcGenfolder.exists()) {
			// creates within the project
			srcGenfolder.create(true, true, monitor);
			IClasspathEntry[] entries = javaProject.getRawClasspath();
			boolean found = false;
			for (IClasspathEntry entry : entries) {
				if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE && entry.getPath().equals(srcPath)) {
					found = true;
					break;
				}
			}

			if (!found) {
				IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
				System.arraycopy(entries, 0, newEntries, 0, entries.length);
				IClasspathEntry srcEntry = JavaCore.newSourceEntry(srcPath, null);
				newEntries[entries.length] = srcEntry;
				javaProject.setRawClasspath(newEntries, null);
			}

		}

		return srcPath;
	}

	/**
	 * Creates the standard set of Relogo directories with default contents.
	 * Returns the source folder into which all of the generated model files will
	 * be written.
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 * @throws UnsupportedEncodingException
	 */
	protected IFolder createStandardRelogoDirectories(IProject project, IProgressMonitor monitor)
			throws CoreException, UnsupportedEncodingException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, "Creating ReLogo Directories",100);
		// IFolder projectFolder = project.getFolder(".");
		String projectName = project.getDescription().getName();
		String packageName = pageOne.getPackageName();
		IJavaProject javaProject = pageTwo.getJavaProject();

		IPath srcPath = checkAndGetSourcePath(SRC, project, javaProject, subMonitor.newChild(1));

		IPath srcGenPath = checkAndGetSourcePath(SRC_GEN, project, javaProject, subMonitor.newChild(1));

		if (srcPath != null) {
			IFolder srcFolder = project.getFolder(SRC);

			String scenarioDirectory = projectName + ".rs";
			String mainDataSourcePluginDirectory = RepastSimphonyPlugin.getInstance()
					.getPluginInstallationDirectory();
			// Code from
			// repast.simphony.agents.designer.ui.wizards.NewProjectCreationWizard's
			// finishPage method
			String[][] variableMap = { { "%MODEL_NAME%", projectName },
					{ "%PROJECT_NAME%", projectName }, { "%SCENARIO_DIRECTORY%", scenarioDirectory },
					{ "%PACKAGE%", packageName },
					{ "%REPAST_SIMPHONY_INSTALL_BUILDER_PLUGIN_DIRECTORY%", mainDataSourcePluginDirectory } };
			IFolder newFolder = null;
			newFolder = createFolderResource(project, "batch");

			Utilities.copyFileFromPluginInstallation("batch/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("batch/batch_params.xml", newFolder,
					"batch_params.xml", variableMap, subMonitor.newChild(1));

			newFolder = createFolderResource(project, "docs");
			Utilities.copyFileFromPluginInstallation("docs/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("docs/index.html", newFolder, "index.html",
					variableMap, subMonitor.newChild(1));

			// for distributed batch (see SIM-459)
			newFolder = createFolderResource(project, "transferFiles");

			// for distributed batch (see SIM-459)
			newFolder = createFolderResource(project, "output");

			newFolder = createFolderResource(project, "freezedried_data");
			Utilities.copyFileFromPluginInstallation("freezedried_data/ReadMe.txt", newFolder,
					"ReadMe.txt", variableMap, subMonitor.newChild(1));

			newFolder = createFolderResource(project, "icons");

			Utilities.copyFileFromPluginInstallation("icons/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("icons/model.bmp", newFolder, "model.bmp",
					variableMap, subMonitor.newChild(1));

			newFolder = createFolderResource(project, "integration");
			Utilities.copyFileFromPluginInstallation("integration/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));

			newFolder = createFolderResource(project, "launchers");
			Utilities.copyFileFromPluginInstallation("launchers/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));

			new RSProjectConfigurator().createReLogoLaunchConfigurations(javaProject, newFolder,
					scenarioDirectory);
			// loadStringTemplates();
			// exportLauncherTemplates(newFolder, projectName);

			newFolder = createFolderResource(project, "lib");
			Utilities.copyFileFromPluginInstallation("lib/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));

			newFolder = createFolderResource(project, "data");
			Utilities.copyFileFromPluginInstallation("data/ReadMe.txt", newFolder, "ReadMe.txt",
					variableMap, subMonitor.newChild(1));

			newFolder = createFolderResource(project, "installer");
			Utilities.copyFileFromPluginInstallation("installer/installation_components.xml", newFolder,
					"installation_components.xml", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("installer/shortcuts.xml", newFolder,
					"shortcuts.xml", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("installer/Unix_shortcuts.xml", newFolder,
					"Unix_shortcuts.xml", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("installer/splash_screen.png", newFolder,
					"splash_screen.png", subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("installer/start_model.bat", newFolder,
					"start_model.bat", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("installer/start_model.command", newFolder,
					"start_model.command", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("installer/installation_coordinator.xml", newFolder,
					"installation_coordinator.xml", variableMap, subMonitor.newChild(1));
			
			newFolder = createFolderResource(project, "repast-licenses");
			Utilities.copyFileFromPluginInstallation("repast-licenses/apache-license.txt", newFolder,
					"apache-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/asm-license.txt", newFolder,
					"asm-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/axion-license.txt", newFolder,
					"axion-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/binding-license.txt", newFolder,
					"binding-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/common-public-license.txt",
					newFolder, "common-public-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/concurrent-license.pdf", newFolder,
					"concurrent-license.pdf", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/CPL.txt", newFolder, "CPL.txt",
					variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/forms-license.txt", newFolder,
					"forms-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/geotools-license.txt", newFolder,
					"geotools-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/groovy-license.txt", newFolder,
					"groovy-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/hsqldb-license.txt", newFolder,
					"hsqldb-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation(
					"repast-licenses/jakarta-commons-collections-license.txt", newFolder,
					"jakarta-commons-collections-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jaxen-license.txt", newFolder,
					"jaxen-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jh-license.txt", newFolder,
					"jh-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jide-oss-license.txt", newFolder,
					"jide-oss-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jmatlink-license.txt", newFolder,
					"jmatlink-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jmf-license.txt", newFolder,
					"jmf-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jmock-license.txt", newFolder,
					"jmock-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jscience-license.txt", newFolder,
					"jscience-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jsp-servlet-api-license.txt",
					newFolder, "jsp-servlet-api-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jts-license.txt", newFolder,
					"jts-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/jung-license.txt", newFolder,
					"jung-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/lgpl-2.1.txt", newFolder,
					"lgpl-2.1.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/LICENSE-jgoodies.txt", newFolder,
					"LICENSE-jgoodies.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/log4j-license.txt", newFolder,
					"log4j-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation(
					"repast-licenses/mitre-relogo-import-wizard-license.txt", newFolder,
					"mitre-relogo-import-wizard-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/MPL-license.txt", newFolder,
					"MPL-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/msql-connector-license.txt",
					newFolder, "msql-connector-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/mx4j-license.txt", newFolder,
					"mx4j-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/openforecast-license.txt",
					newFolder, "openforecast-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/piccolo-license.txt", newFolder,
					"piccolo-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/proactive-license.txt", newFolder,
					"proactive-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/repast-license.txt", newFolder,
					"repast-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/saxpath-license.txt", newFolder,
					"saxpath-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/swingx-license.txt", newFolder,
					"swingx-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/table-layout-license.txt",
					newFolder, "table-layout-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/violinstrings-license.txt",
					newFolder, "violinstrings-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/wizard-license.txt", newFolder,
					"wizard-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/xpp3-license.txt", newFolder,
					"xpp3-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/xstream-license.txt", newFolder,
					"xstream-license.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/license_apache.txt", newFolder,
					"license_apache.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/license_apache11.txt", newFolder,
					"license_apache11.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/license_flow4j.txt", newFolder,
					"license_flow4j.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/license_flow4J-eclipse.txt",
					newFolder, "license_flow4J-eclipse.txt", variableMap, subMonitor.newChild(1));
			Utilities.copyFileFromPluginInstallation("repast-licenses/license_xstream.txt", newFolder,
					"license_xstream.txt", variableMap, subMonitor.newChild(1));
			
			newFolder = srcFolder.getFolder("../license.txt");
			Utilities.copyFileFromPluginInstallation("license.txt", newFolder, "", variableMap, subMonitor.newChild(1));

			newFolder = srcFolder.getFolder("../MessageCenter.log4j.properties");
			Utilities.copyFileFromPluginInstallation("MessageCenter.log4j.properties", newFolder, "",
					variableMap, subMonitor.newChild(1));

			newFolder = srcFolder.getFolder("../model_description.txt");
			Utilities.copyFileFromPluginInstallation("model_description.txt", newFolder, "", variableMap,
					subMonitor.newChild(1));

			// Utilities.copyFileFromPluginInstallation("license.txt", projectFolder,
			// "",
			// variableMap, monitor);
			//
			// Utilities.copyFileFromPluginInstallation("model_description.txt",
			// projectFolder, "", variableMap, monitor);

			// create turtleShapes.xml in project root
			shapesFolder = createFolderResource(project, "shapes");
			List<NLImage> imageList = pageOne.getImportShapes();

			// imageList = pageOne.getNetlogoSimulation().getNLImages();
			// imageList = pageOne.getImportShapes();

			if (!imageList.isEmpty()) {
				XStream xstream = new XStream();
				String xml = xstream.toXML(imageList);
				createFileResource(shapesFolder, "turtleShapes.xml",
						new ByteArrayInputStream(xml.getBytes("UTF-8")));
			}

			// All svg default shapes
			File templateShapesFolder = new File(RepastSimphonyPlugin.getInstance()
					.getPluginInstallationDirectory()
					+ RepastSimphonyPlugin.getInstance().getPluginDirectoryName() + "/setupfiles/shapes/svg");
			List<String> shapeFiles = WizardUtilities.getFileNamesInDirectory(templateShapesFolder);
			for (String shapeFileName : shapeFiles) {
				Utilities.copyFileFromPluginInstallation("shapes/svg/" + shapeFileName, shapesFolder,
						shapeFileName, null);
			}

			// create XML files defining model and UI
			IFolder rsFolder = createFolderResource(project, projectName + ".rs");
			Utilities.copyFileFromPluginInstallation("package.rs/default_frame_layout.xml", rsFolder,
					"default_frame_layout.xml", variableMap, subMonitor.newChild(1));
			exportReLogoParameters(rsFolder, projectName);
			exportReLogoModel(rsFolder, projectName);
			exportReLogoDataLoader(rsFolder, packageName);
			exportReLogoUserPanel(rsFolder, packageName);
			exportReLogoScenario(rsFolder, projectName);
			// exportRelogoScore(folder, projectName);
			IFolder styleFolder = createFolderResource(rsFolder, "styles");
			// exportReLogoStyles(styleFolder);
			// exportRelogoScenario(rsFolder, styleFolder, projectName);
			newFolder = srcFolder;
			// String lowerCaseProjectName = packageName;//TODO: project name as
			// package name here
			String[] packageNames = packageName.split("\\.");
			for (String subpackage : packageNames) {
				newFolder = createFolderResource(newFolder, subpackage);
			}
			IFolder srcGenNewFolder = project.getFolder(SRC_GEN);
			for (String subpackage : packageNames) {
				srcGenNewFolder = createFolderResource(srcGenNewFolder, subpackage);
			}

			try {
				exportRelogoTemplates(newFolder, srcGenNewFolder, rsFolder, styleFolder);
				subMonitor.setWorkRemaining(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return newFolder;
		} else {
			return null;
		}
	}

	protected byte[] hexStringToByteArray(String str) {
		byte[] result = new byte[str.length() / 2];
		for (int idx = 0; idx < result.length; idx++) {
			int upper = "0123456789ABCDEF".indexOf(str.charAt(idx * 2)) << 4;
			int lower = "0123456789ABCDEF".indexOf(str.charAt(idx * 2 + 1));
			result[idx] = (byte) ((upper & 0xf0) | (lower & 0xf));
		}
		return result;
	}

	/**
	 * Code snippet from Eclipse wiki showing how to create resources for a
	 * project, a folder, and a file.
	 */
	private IProject createProjectResource(String name) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(name);

		// at this point, no resources have been created
		if (!project.exists()) {
			try {
				project.create(null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!project.isOpen()) {
			try {
				project.open(null);

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return project;
	}

	/**
	 * Code snippet from Eclipse wiki showing how to create resources for a
	 * project, a folder, and a file.
	 */
	protected static IFolder createFolderResource(IProject project, String name) {
		IFolder folder = project.getFolder(name);
		// at this point, no resources have been created
		if (!folder.exists()) {
			try {
				folder.create(IResource.NONE, true, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return folder;
	}

	/**
	 * Code snippet from Eclipse wiki showing how to create resources for a
	 * project, a folder, and a file.
	 */
	private IFolder createFolderResource(IFolder parentFolder, String name) {
		IFolder folder = parentFolder.getFolder(name);
		// at this point, no resources have been created
		if (!folder.exists()) {
			try {
				folder.create(IResource.NONE, true, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return folder;
	}

	/**
	 * Create a new file in the specified folder, containing the specified
	 * contents. Forces a write of the contents to the file system.
	 */
	private void createFileResource(IFolder folder, String name, InputStream contents) {
		IFile file = folder.getFile(name);
		// at this point, no resources have been created
		if (!file.exists()) {
			try {
				file.create(contents, IResource.FORCE, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	static private String[] RELOGO_STYLE_TEMPLATES = new String[] { "PatchStyle", "TurtleStyle",
			"LinkStyle", "WayPointStyle", "TrackingLinkStyle" };
	static private String STYLE_PACKAGE_NAME = "style";
	static private String CONTEXT_PACKAGE_NAME = "context";
	static private String FACTORIES_PACKAGE_NAME = "factories";
	static private String[] RELOGO_CONTEXT_TEMPLATES = new String[] { "SimBuilder" };
	static private String[] RELOGO_FACTORIES_TEMPLATES = new String[] { "ReLogoPanelCreator" };
	static private String RELOGO_CUSTOM_TURTLE_TEMPLATE = "UserTurtle";
	static private String RELOGO_CUSTOM_PATCH_TEMPLATE = "UserPatch";
	static private String RELOGO_CUSTOM_LINK_TEMPLATE = "UserLink";
	static private String RELOGO_CUSTOM_OBSERVER_TEMPLATE = "UserObserver";
	static private String RELOGO_CUSTOM_UGPF = "UserGlobalsAndPanelFactory";
	static public String RELOGO_CUSTOM_UGPF_TEMPLATE_GROUP_FILE = "/templates/" + RELOGO_CUSTOM_UGPF
			+ ".stg";
	static public String RELOGO_USER_OTPL_CLASSES_TEMPLATE_GROUP_FILE = "/templates/userOTPLclasses.stg";

	static protected StringTemplateGroup templateGroup;
	static protected StringTemplateGroup ugpfTemplateGroup;
	static protected StringTemplateGroup javaTemplateGroup;
	static protected StringTemplateGroup userOTPLTemplateGroup;
	static protected StringTemplateGroup reLogoOTPLTemplateGroup;

	private String getTextFromResource(String resourceName) {

		try {
			File file = FileUtils.toFile(getClass().getResource(resourceName));
			return FileUtils.readFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
			return ("Couldn't get to file for resource name " + resourceName);
		}

	}

	private void exportLauncherTemplates(IFolder launchFolder, String projectName)
			throws UnsupportedEncodingException {
		StringTemplate st = catchAllTemplateGroup.getInstanceOf("modelLauncher_file");
		st.setAttribute("Model", projectName);
		exportLauncherTemplate(launchFolder, st.toString(), projectName);
	}

	private StringBuffer loadLauncherTemplate(String name, String modelName) throws IOException {
		StringBuffer buf = new StringBuffer();
		String templateName = "/templates/" + name + ".launch";
		InputStream stream = getClass().getResourceAsStream(templateName);
		BufferedReader rdr = new BufferedReader(new InputStreamReader(stream));
		for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
			line = line.replace("$MODEL$", modelName);
			buf.append(line);
			buf.append("\n");
		}
		return buf;
	}

	private InputStream loadClassTemplate(String templateName, String packageName, String bodyCode)
			throws IOException {
		// StringTemplate classTemplate = true;
		StringBuffer buf = new StringBuffer();
		InputStream stream = getClass().getResourceAsStream(templateName);
		BufferedReader rdr = new BufferedReader(new InputStreamReader(stream));
		for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
			if (packageName != null) {
				line = line.replace("$PKGNAME$", packageName);
			}
			if (bodyCode != null) {
				line = line.replace("$MODEL$", bodyCode);
			}
			buf.append(line);
			buf.append("\n");
		}
		return new ByteArrayInputStream(buf.toString().getBytes("UTF-8"));
	}

	private InputStream loadUserClassTemplate(String templateName, String packageName, String bodyCode)
			throws UnsupportedEncodingException {
		return loadUserClassTemplate(templateName, packageName, bodyCode, "");
	}

	private InputStream loadUserClassTemplate(String templateName, String packageName,
			String bodyCode, String breedCode) throws UnsupportedEncodingException {
		// StringTemplate classTemplate = true;
		StringTemplate st = userOTPLTemplateGroup.getInstanceOf(templateName);
		st.setAttribute("packageName", packageName);
		// if (templateName.equals("userTurtle") ||
		// templateName.equals("userPatch") || templateName.equals("userLink")){
		// bodyCode = WizardUtilities.replaceStopWithReturn(bodyCode);
		// }
		st.setAttribute("bodyCode", bodyCode);
		if (!breedCode.equals("")) {
			st.setAttribute("breedCode", breedCode);
		}
		// System.out.println("for templateName = " + templateName);
		// System.out.println(st.toString());
		return new ByteArrayInputStream(st.toString().getBytes("UTF-8"));
	}

	private void exportLauncherTemplate(IFolder launchFolder, String template, String outputName)
			throws UnsupportedEncodingException {
		String instantiationName = outputName + ".launch";
		createFileResource(launchFolder, instantiationName,
				new ByteArrayInputStream(template.getBytes("UTF-8")));

	}

	static protected StringTemplateGroup parametersTemplateGroup;
	static public String RELOGO_PARAMETERS_TEMPLATE_GROUP_FILE = "/templates/parameters.stg";
	static protected StringTemplateGroup modelTemplateGroup;
	static public String RELOGO_MODEL_TEMPLATE_GROUP_FILE = "/templates/model.stg";
	static protected StringTemplateGroup contextTemplateGroup;
	static public String RELOGO_CONTEXT_TEMPLATE_GROUP_FILE = "/templates/context.stg";
	static protected StringTemplateGroup displayTemplateGroup;
	static public String RELOGO_DISPLAY_TEMPLATE_GROUP_FILE = "/templates/repast.simphony.action.display_relogoDefault.xml.stg";
	static protected StringTemplateGroup catchAllTemplateGroup;
	static public String RELOGO_CATCHALL_TEMPLATE_GROUP_FILE = "/templates/catchAll.stg";

	protected void loadStringTemplates() {

		if (catchAllTemplateGroup == null) {
			InputStream catchAllTemplateStream = getClass().getResourceAsStream(
					RELOGO_CATCHALL_TEMPLATE_GROUP_FILE);
			catchAllTemplateGroup = new StringTemplateGroup(
					new InputStreamReader(catchAllTemplateStream), DefaultTemplateLexer.class);
		}

		// DefaultTemplateLexer is needed for $...$ attributes in templates
		if (displayTemplateGroup == null) {
			InputStream displayTemplateStream = getClass().getResourceAsStream(
					RELOGO_DISPLAY_TEMPLATE_GROUP_FILE);
			displayTemplateGroup = new StringTemplateGroup(new InputStreamReader(displayTemplateStream),
					DefaultTemplateLexer.class);
		}

		if (contextTemplateGroup == null) {
			InputStream contextTemplateStream = getClass().getResourceAsStream(
					RELOGO_CONTEXT_TEMPLATE_GROUP_FILE);
			contextTemplateGroup = new StringTemplateGroup(new InputStreamReader(contextTemplateStream),
					DefaultTemplateLexer.class);
		}

		if (modelTemplateGroup == null) {
			InputStream modelTemplateStream = getClass().getResourceAsStream(
					RELOGO_MODEL_TEMPLATE_GROUP_FILE);
			modelTemplateGroup = new StringTemplateGroup(new InputStreamReader(modelTemplateStream),
					DefaultTemplateLexer.class);
		}

		if (parametersTemplateGroup == null) {
			InputStream parametersTemplateStream = getClass().getResourceAsStream(
					RELOGO_PARAMETERS_TEMPLATE_GROUP_FILE);
			parametersTemplateGroup = new StringTemplateGroup(new InputStreamReader(
					parametersTemplateStream), DefaultTemplateLexer.class);
		}

		if (userOTPLTemplateGroup == null) {
			InputStream userOTPLTemplateStream = getClass().getResourceAsStream(
					RELOGO_USER_OTPL_CLASSES_TEMPLATE_GROUP_FILE);
			userOTPLTemplateGroup = new StringTemplateGroup(new InputStreamReader(userOTPLTemplateStream));
		}

		if (reLogoOTPLTemplateGroup == null) {
			InputStream reLogoOTPLTemplateStream = getClass().getResourceAsStream(
					ReLogoBuilder.RELOGO_OTPL_CLASSES_TEMPLATE_GROUP_FILE);
			reLogoOTPLTemplateGroup = new StringTemplateGroup(new InputStreamReader(
					reLogoOTPLTemplateStream));
		}

		if (ugpfTemplateGroup == null) {
			InputStream upccTemplateStream = getClass().getResourceAsStream(
					RELOGO_CUSTOM_UGPF_TEMPLATE_GROUP_FILE);
			ugpfTemplateGroup = new StringTemplateGroup(new InputStreamReader(upccTemplateStream));
		}

	}

	private void exportReLogoParameters(IFolder rsFolder, String projectName)
			throws UnsupportedEncodingException {
		loadStringTemplates();
		List<NLControl> nlControls = pageOne.getNetlogoSimulation().getNLControls();
		for (NLControl ctl : nlControls) {
			if (ctl instanceof NLGraphicsWindow) {
				StringTemplate parameters_file = parametersTemplateGroup.getInstanceOf("parameters_file");
				StringBuffer parameters = new StringBuffer();
				int minPxcor = ((NLGraphicsWindow) ctl).getMinPxcor();
				int maxPxcor = ((NLGraphicsWindow) ctl).getMaxPxcor();
				int minPycor = ((NLGraphicsWindow) ctl).getMinPycor();
				int maxPycor = ((NLGraphicsWindow) ctl).getMaxPycor();
				StringTemplate st = new StringTemplate(
						"<parameter name=\"default_observer_$mP$\" displayName=\"default_observer_$mP$\" type=\"int\" defaultValue=\"$mPVal$\"/>\n");
				StringTemplate sti = st.getInstanceOf();
				sti.setAttribute("mP", "minPxcor");
				sti.setAttribute("mPVal", minPxcor);
				parameters.append(sti.toString());
				sti = st.getInstanceOf();
				sti.setAttribute("mP", "maxPxcor");
				sti.setAttribute("mPVal", maxPxcor);
				parameters.append(sti.toString());
				sti = st.getInstanceOf();
				sti.setAttribute("mP", "minPycor");
				sti.setAttribute("mPVal", minPycor);
				parameters.append(sti.toString());
				sti = st.getInstanceOf();
				sti.setAttribute("mP", "maxPycor");
				sti.setAttribute("mPVal", maxPycor);
				parameters.append(sti.toString());
				parameters_file.setAttribute("parameters", parameters.toString());
				// System.out.println(parameters_file.toString());
				createFileResource(rsFolder, "parameters.xml", new ByteArrayInputStream(parameters_file
						.toString().getBytes("UTF-8")));
			}
		}
	}

	private void exportReLogoModel(IFolder rsFolder, String projectName)
			throws UnsupportedEncodingException {
		loadStringTemplates();
		StringTemplate model_file = modelTemplateGroup.getInstanceOf("model_file");
		model_file.setAttribute("modelName", projectName);
		createFileResource(rsFolder, "user_path.xml", new ByteArrayInputStream(model_file.toString()
				.getBytes("UTF-8")));
	}

	private void exportReLogoDataLoader(IFolder rsFolder, String packageName)
			throws UnsupportedEncodingException {
		String dataLoaderFileName = "repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_0.xml";
		StringTemplate st = catchAllTemplateGroup.getInstanceOf("dataLoader_file");
		st.setAttribute("Model", packageName);
		createFileResource(rsFolder, dataLoaderFileName, new ByteArrayInputStream(st.toString()
				.getBytes("UTF-8")));
	}

	private void exportReLogoUserPanel(IFolder rsFolder, String packageName)
			throws UnsupportedEncodingException {
		String userPanelFileName = "repast.simphony.userpanel.ui.DefaultUserPanelDescriptorAction_1.xml";
		StringTemplate st = catchAllTemplateGroup.getInstanceOf("userPanel_file");
		st.setAttribute("Model", packageName);
		createFileResource(rsFolder, userPanelFileName, new ByteArrayInputStream(st.toString()
				.getBytes("UTF-8")));
	}

	private void exportReLogoScenario(IFolder rsFolder, String projectName)
			throws UnsupportedEncodingException {
		StringTemplate st = catchAllTemplateGroup.getInstanceOf("scenario_file");
		st.setAttribute("Model", projectName);
		createFileResource(rsFolder, "scenario.xml",
				new ByteArrayInputStream(st.toString().getBytes("UTF-8")));
	}

	private void exportReLogoStyles(IFolder styleFolder) throws UnsupportedEncodingException {
		String[] stylesList = { "DirectedLinks", "UndirectedLinks", "TrackingNetwork" };
		StringTemplate st = catchAllTemplateGroup.getInstanceOf("directedLinksStyle_file");
		String styleFileName = stylesList[0] + ".style_0.xml";
		createFileResource(styleFolder, styleFileName,
				new ByteArrayInputStream(st.toString().getBytes("UTF-8")));
		st = catchAllTemplateGroup.getInstanceOf("undirectedLinksStyle_file");
		styleFileName = stylesList[1] + ".style_0.xml";
		createFileResource(styleFolder, styleFileName,
				new ByteArrayInputStream(st.toString().getBytes("UTF-8")));
		st = catchAllTemplateGroup.getInstanceOf("trackingNetworkStyle_file");
		styleFileName = stylesList[2] + ".style_0.xml";
		createFileResource(styleFolder, styleFileName,
				new ByteArrayInputStream(st.toString().getBytes("UTF-8")));
	}

	private void exportAdditionalNetworkStyleFile(String networkName, IFolder styleFolder)
			throws UnsupportedEncodingException {
		StringTemplate st = catchAllTemplateGroup.getInstanceOf("additionalNetworksStyle_file");
		String styleFileName = networkName + ".style_0.xml";
		st.setAttribute("number", Math.random());
		createFileResource(styleFolder, styleFileName,
				new ByteArrayInputStream(st.toString().getBytes("UTF-8")));
	}

	private boolean hasAttributesToGenerate(RelogoClass relogoClass) {
		for (Attribute attr : relogoClass.attributes()) {
			if (attr.generate) {
				return true;
			}
		}
		return false;
	}

	private void exportRelogoTemplates(IFolder srcFolder, IFolder srcGenFolder, IFolder rsFolder,
			IFolder styleFolder) throws IOException {
		loadStringTemplates();
		String packageName = pageOne.getPackageName();
		// first write the static Java and Groovy classes
		// two static java classes in the style package
		IFolder folder = createFolderResource(srcFolder, STYLE_PACKAGE_NAME);
		for (int i = 0; i < RELOGO_STYLE_TEMPLATES.length; i++) {
			String templateName = "/templates/" + RELOGO_STYLE_TEMPLATES[i] + ".java.txt";
			String instantiationName = RELOGO_STYLE_TEMPLATES[i] + ".java";
			InputStream stream = loadClassTemplate(templateName, packageName, "");
			createFileResource(folder, instantiationName, stream);
		}
		// a single static groovy class in the context package
		folder = createFolderResource(srcFolder, CONTEXT_PACKAGE_NAME);
		for (int i = 0; i < RELOGO_CONTEXT_TEMPLATES.length; i++) {
			String templateName = "/templates/" + RELOGO_CONTEXT_TEMPLATES[i] + ".groovy.txt";
			String instantiationName = RELOGO_CONTEXT_TEMPLATES[i] + ".groovy";
			InputStream stream = loadClassTemplate(templateName, packageName, "");
			createFileResource(folder, instantiationName, stream);
		}
		// a single static groovy class in the factories package
		IFolder factoriesFolder = createFolderResource(srcFolder, FACTORIES_PACKAGE_NAME);
		for (int i = 0; i < RELOGO_FACTORIES_TEMPLATES.length; i++) {
			String templateName = "/templates/" + RELOGO_FACTORIES_TEMPLATES[i] + ".java.txt";
			String instantiationName = RELOGO_FACTORIES_TEMPLATES[i] + ".java";
			InputStream stream = loadClassTemplate(templateName, packageName, "");
			createFileResource(factoriesFolder, instantiationName, stream);
		}

		String[] instances = { "reLogoTurtle", "reLogoPatch", "reLogoLink", "reLogoObserver" };
		String[] fileNames = { "ReLogoTurtle.java", "ReLogoPatch.java", "ReLogoLink.java",
				"ReLogoObserver.java" };
		for (int i = 0; i < instances.length; i++) {
			StringTemplate rlTemplate = reLogoOTPLTemplateGroup.getInstanceOf(instances[i]);

			rlTemplate.setAttribute("packageName", packageName);
			createFileResource(srcGenFolder, fileNames[i], new ByteArrayInputStream(
					rlTemplate.toString().getBytes("UTF-8")));
		}

		/*
		 * for (int i = 0; i < RELOGO_FACTORIES_TEMPLATES.length; i++) { String
		 * templateName = "/templates/" + RELOGO_FACTORIES_TEMPLATES[i] +
		 * ".groovy.txt"; String instantiationName = RELOGO_FACTORIES_TEMPLATES[i] +
		 * ".groovy"; InputStream stream = loadClassTemplate(templateName,
		 * packageName, ""); createFileResource(factoriesFolder, instantiationName,
		 * stream); }
		 */
		LinkedList<RelogoClass> allClasses = pageOne.getGeneratedClasses();
		IFolder relogoSourceFolder = createFolderResource(srcFolder, "relogo");
		// output the Model class, adding in all global variables
		/*
		 * { RelogoClass modelClass = null; for (RelogoClass relogoClass :
		 * allClasses) { if (relogoClass != null && relogoClass.getGenericCategory()
		 * == RelogoClass.RELOGO_CLASS_MODEL) { modelClass = relogoClass; } } if
		 * (modelClass != null) { String templateName = "/templates/" +
		 * RELOGO_CUSTOM_MODEL_TEMPLATE + ".java.txt"; String instantiationName =
		 * RELOGO_CUSTOM_MODEL_TEMPLATE + ".java"; InputStream stream =
		 * loadClassTemplate(templateName, packageName, modelClass.getJavaCode());
		 * createFileResource(srcFolder, instantiationName, stream); } else {
		 * System.err.println("No global class generated!"); } }
		 */
		// output the Plot class, adding in all plots and series.
		// Template code implies one Plot class to support all plots.
		/*
		 * { StringTemplate plotTpl = plotTemplateGroup.getInstanceOf("plot");
		 * NLPlot[] plots = pageOne.getPlots(); // do these two for each plot, axis,
		 * and series String plots_and_pens = ""; for (int plotId = 0; plotId <
		 * plots.length; plotId++) { if (plots_and_pens.length() == 0) {
		 * plots_and_pens = plots[plotId].getTitle()+":["; } else { plots_and_pens =
		 * plots_and_pens + ",\n" + plots[plotId].getTitle()+":["; } String pens =
		 * ""; for (NLPen pen : plots[plotId].getPens()) { StringTemplate seriesTpl
		 * = plotTemplateGroup.getInstanceOf("series");
		 * seriesTpl.setAttribute("plot", plots[plotId].getTitle());
		 * seriesTpl.setAttribute("axis", plots[plotId].getYTitle());
		 * seriesTpl.setAttribute("series", pen.getLabel());
		 * seriesTpl.setAttribute("series_label", pen.getLabel());
		 * plotTpl.setAttribute("series", seriesTpl.toString()); if
		 * (pens.length()==0) { pens = "\"" + pen.getLabel() + "\""; } else { pens =
		 * pens + ",\"" + pen.getLabel() + "\""; } } plots_and_pens += pens+"]"; }
		 * plotTpl.setAttribute("package", packageName);
		 * plotTpl.setAttribute("plots_and_pens", plots_and_pens);
		 * plotTpl.setAttribute("dollar", "$"); // we need this unless we code a
		 * custom TokenLexer for StringTemplate String instantiationName =
		 * RELOGO_CUSTOM_PLOT_TEMPLATE + ".groovy"; createFileResource(srcFolder,
		 * instantiationName, new StringInputStream(plotTpl.toString())); }
		 */
		// Build the template for built-ins dependent upon user breeds.
		// StringTemplate utilityTpl =
		// utilityTemplateGroup.getInstanceOf("user_utility_body");
		// utilityTpl.setAttribute("package", packageName);
		// finally output the dynamic Groovy classes and insert the new code
		// from the Netlogo model
		{
			// UserTurtle
			StringBuffer bodyCode = new StringBuffer();
			StringBuffer turtlesOwnCode = new StringBuffer();
			for (RelogoClass relogoClass : allClasses) {
				if (relogoClass != null
						&& relogoClass.getGenericCategory() == RelogoClass.RELOGO_CLASS_TURTLE) {
					String breedPlural = relogoClass.getBreed().getPluralName();
					String breedSingular = relogoClass.getBreed().getSingularName();

					// Collect the turtle attributes into a @TurtlesOwn
					// statement, turtlesOwnCode
					if (breedPlural.equals("turtles")) {
						// Generate:
						// @TurtlesOwn
						// def attribute1, attribute2
						if (hasAttributesToGenerate(relogoClass)) {
							turtlesOwnCode.append("def ");
							int attributeCounter = 0;
							for (Attribute attr : relogoClass.attributes()) {
								if (attr.generate) {
									String name = attr.toString();
									if (attributeCounter != 0) {
										turtlesOwnCode.append(", ");
									}
									turtlesOwnCode.append(name);
									attributeCounter++;
								}
							}
							turtlesOwnCode.append("\n");
						}
					}
					if (!breedPlural.equals("turtles")) {

						/**
						 * class Worker extends UserTurtle{
						 * 
						 * def destination, cargo
						 */

						if (breedPlural != null) {
							StringTemplate ctTemplate = userOTPLTemplateGroup.getInstanceOf("customTurtle");
							ctTemplate.setAttribute("packageName", packageName);

							String breedName = WizardUtilities.getJavaName(breedSingular);
							String className = Character.toString(breedName.charAt(0)).toUpperCase()
									+ breedName.substring(1);
							if (!WizardUtilities.getJavaName(breedPlural).equals(
									WizardUtilities.getJavaName(breedSingular) + "s")) {
								ctTemplate.setAttribute("pluralAnnotation",
										"@Plural('" + WizardUtilities.getJavaName(breedPlural) + "')");
							}
							ctTemplate.setAttribute("turtleClassName", className);

							if (hasAttributesToGenerate(relogoClass)) {
								StringBuffer sb = new StringBuffer();
								sb.append("def ");
								int attributeCounter = 0;
								for (Attribute attr : relogoClass.attributes()) {
									if (attr.generate) {
										String name = attr.toString();
										if (attributeCounter != 0) {
											sb.append(", ");
										}
										sb.append(name);
										attributeCounter++;
									}
								}
								ctTemplate.setAttribute("turtleVars", sb.toString());
							}
							createFileResource(relogoSourceFolder, className + ".groovy",
									new ByteArrayInputStream(ctTemplate.toString().getBytes("UTF-8")));
						}
					}
					// This should probably remain the same, and go to the
					// UserTurtle class
					bodyCode.append(relogoClass.getGroovyCode());
				}
			}
			// Accumulated information above

			StringTemplate utTemplate = userOTPLTemplateGroup.getInstanceOf("userTurtle");

			utTemplate.setAttribute("packageName", packageName);
			if (turtlesOwnCode.length() != 0) {
				utTemplate.setAttribute("turtleVars", turtlesOwnCode.toString());
			}
			utTemplate.setAttribute("bodyCode", bodyCode);
			createFileResource(relogoSourceFolder, "UserTurtle.groovy", new ByteArrayInputStream(
					utTemplate.toString().getBytes("UTF-8")));

		}
		{
			// UserPatch
			StringBuffer patchesOwnCode = new StringBuffer();
			StringBuffer bodyCode = new StringBuffer();
			StringTemplate upTemplate = userOTPLTemplateGroup.getInstanceOf("userPatch");
			upTemplate.setAttribute("packageName", packageName);
			for (RelogoClass relogoClass : allClasses) {
				if (relogoClass != null
						&& relogoClass.getGenericCategory() == RelogoClass.RELOGO_CLASS_PATCH) {
					// @PatchesOwn
					if (hasAttributesToGenerate(relogoClass)) {
						patchesOwnCode.append("@Diffusible\ndef ");
						int attributeCounter = 0;
						for (Attribute attr : relogoClass.attributes()) {
							if (attr.generate) {
								String name = attr.toString();
								if (attributeCounter != 0) {
									patchesOwnCode.append(", ");
								}
								patchesOwnCode.append(name);
								attributeCounter++;
							}
						}
						upTemplate.setAttribute("patchVars", patchesOwnCode.toString());

					}
					bodyCode.append(relogoClass.getGroovyCode());
				}
			}
			upTemplate.setAttribute("bodyCode", bodyCode.toString());
			createFileResource(relogoSourceFolder, "UserPatch.groovy", new ByteArrayInputStream(
					upTemplate.toString().getBytes("UTF-8")));
		}
		{
			// UserLink
			StringTemplate ulTemplate = userOTPLTemplateGroup.getInstanceOf("userLink");
			ulTemplate.setAttribute("packageName", packageName);
			StringBuffer linksOwnCode = new StringBuffer();
			StringBuffer bodyCode = new StringBuffer();
			StringBuffer projections = new StringBuffer();

			String[] displayStrings = { "netStylesEntry", "editedNetStylesEntry",
					"repastSimphonyScoreProjectionData", "projectionDescriptorsEntry" };
			StringTemplate displayTemplate = new StringTemplate();
			StringBuffer netStylesEntryBuffer = new StringBuffer();
			StringBuffer editedNetStylesEntryBuffer = new StringBuffer();
			StringBuffer repastSimphonyScoreProjectionDataBuffer = new StringBuffer();
			StringBuffer projectionDescriptorsEntryBuffer = new StringBuffer();
			ArrayList<StringBuffer> bufferList = new ArrayList<StringBuffer>(4);
			bufferList.add(0, netStylesEntryBuffer);
			bufferList.add(1, editedNetStylesEntryBuffer);
			bufferList.add(2, repastSimphonyScoreProjectionDataBuffer);
			bufferList.add(3, projectionDescriptorsEntryBuffer);
			// StringTemplate editedNetStylesEntryTemplate =
			// displayTemplateGroup.getInstanceOf("editedNetStylesEntry");
			// StringTemplate repastSimphonyScoreProjectionDataTemplate =
			// displayTemplateGroup.getInstanceOf("repastSimphonyScoreProjectionData");
			// StringTemplate projectionDescriptorsEntryTemplate =
			// displayTemplateGroup.getInstanceOf("projectionDescriptorsEntry");
			int linkBreedCounter = 0;
			for (RelogoClass relogoClass : allClasses) {
				if (relogoClass != null
						&& relogoClass.getGenericCategory() == RelogoClass.RELOGO_CLASS_LINK) {

					if (relogoClass.getBreed() != null) {
						String breedPlural = relogoClass.getBreed().getPluralName();
						String breedSingular = relogoClass.getBreed().getSingularName();
						boolean linkBreedDirected = relogoClass.getBreed().isLinkBreedDirected();
						// Collect the link attributes into @LinksOwn field
						// declarations
						if (breedPlural.equals("links")) {
							if (hasAttributesToGenerate(relogoClass)) {
								linksOwnCode.append("def ");
								int attributeCounter = 0;
								for (Attribute attr : relogoClass.attributes()) {
									if (attr.generate) {
										// TODO : Change to preserve
										// capitalization
										String name = attr.toString();
										if (attributeCounter != 0) {
											linksOwnCode.append(", ");
										}
										linksOwnCode.append(name);
										attributeCounter++;
									}
								}
								// linkVars to utTemplate
								ulTemplate.setAttribute("linkVars", linksOwnCode.toString());
							}
						}
						if (!breedPlural.equals("links")) {
							StringTemplate clTemplate = userOTPLTemplateGroup.getInstanceOf("customLink");
							clTemplate.setAttribute("packageName", packageName);
							String breedName = WizardUtilities.getJavaName(breedSingular);
							String className = Character.toString(breedName.charAt(0)).toUpperCase()
									+ breedName.substring(1);
							clTemplate.setAttribute("linkClassName", className);

							if (breedPlural != null) {
								projections.append("<projection id=\"" + className + "\" type=\"network\" />\n");
								for (int i = 0; i < 4; i++) {
									displayTemplate = displayTemplateGroup.getInstanceOf(displayStrings[i]);
									displayTemplate.setAttribute("networkName", className);
									if (i == 0) {
										displayTemplate.setAttribute("packageName", packageName);
									}
									if (i == 3) {
										displayTemplate.setAttribute("number", 5 + linkBreedCounter);
										linkBreedCounter++;
									}
									bufferList.get(i).append(displayTemplate.toString() + "\n");
								}
								// exportAdditionalNetworkStyleFile(camelCase(
								// breedPlural, false), styleFolder);

								// Create the @LinkBreed statement and append to
								// allLinkBreedCode

								if (!linkBreedDirected) {
									clTemplate.setAttribute("directedAnnotation", "@Undirected");
								} else {
									clTemplate.setAttribute("directedAnnotation", "@Directed");
								}
								if (!WizardUtilities.getJavaName(breedPlural).equals(
										WizardUtilities.getJavaName(breedSingular) + "s")) {
									clTemplate.setAttribute("pluralAnnotation",
											"@Plural('" + WizardUtilities.getJavaName(breedPlural) + "')");
								}

							}

							// Collect the breed's attributes into a
							// @LinksOwn('breedPlural') statement and append to
							// allBreedsOwnCode
							if (hasAttributesToGenerate(relogoClass)) {
								StringBuffer breedsOwnCode = new StringBuffer();
								breedsOwnCode.append("def ");
								int attributeCounter = 0;
								for (Attribute attr : relogoClass.attributes()) {
									if (attr.generate) {
										String name = attr.toString();
										if (attributeCounter != 0) {
											breedsOwnCode.append(", ");
										}
										breedsOwnCode.append(name);
										attributeCounter++;
									}
								}
								clTemplate.setAttribute("linkVars", breedsOwnCode.toString());
							}
							createFileResource(relogoSourceFolder, className + ".groovy",
									new ByteArrayInputStream(clTemplate.toString().getBytes("UTF-8")));
						}
						bodyCode.append(relogoClass.getGroovyCode());
					}
				}
			}
			// Create UserLink.groovy
			ulTemplate.setAttribute("bodyCode", bodyCode.toString());
			createFileResource(relogoSourceFolder, "UserLink.groovy", new ByteArrayInputStream(ulTemplate
					.toString().getBytes("UTF-8")));

			StringTemplate context_file = contextTemplateGroup.getInstanceOf("context_file");
			context_file.setAttribute("modelName", pageOne.getProjectName());
			context_file.setAttribute("additionalProjections", projections.toString());
			createFileResource(rsFolder, "context.xml", new ByteArrayInputStream(context_file.toString()
					.getBytes("UTF-8")));

			StringTemplate display_file = displayTemplateGroup.getInstanceOf("display_file");
			// display_file(packageName,additionalNetStyles,additionalEditedNetStylesEntries,additionalRepastSimphonyScoreProjectionDatas,additionalProjectionDescriptorsEntries)
			display_file.setAttribute("packageName", packageName);
			display_file.setAttribute("additionalNetStyles", bufferList.get(0).toString());
			display_file.setAttribute("additionalEditedNetStylesEntries", bufferList.get(1).toString());
			display_file.setAttribute("additionalRepastSimphonyScoreProjectionDatas", bufferList.get(2)
					.toString());
			display_file.setAttribute("additionalProjectionDescriptorsEntries", bufferList.get(3)
					.toString());
			String displayFileName = "repast.simphony.action.display_relogoDefault.xml";
			createFileResource(rsFolder, displayFileName, new ByteArrayInputStream(display_file
					.toString().getBytes("UTF-8")));

		}
		{
			// UserObserver
			String templateName = "userObserver";
			String instantiationName = "UserObserver.groovy";
			String upccInstantiationName = RELOGO_CUSTOM_UGPF + ".groovy";
			StringTemplate ugpfTemplate = ugpfTemplateGroup.getInstanceOf("ugpf");
			ugpfTemplate.setAttribute("packageName", packageName);
			StringBuffer bodyCode = new StringBuffer();
			StringBuffer globalsCode = new StringBuffer();
			List<ProcedureDefinition> observerMethods = new ArrayList<ProcedureDefinition>();
			for (RelogoClass relogoClass : allClasses) {
				if (relogoClass != null
						&& relogoClass.getGenericCategory() == RelogoClass.RELOGO_CLASS_OBSERVER) {
					// adding the non-button methods to UserObserver
					bodyCode.append(relogoClass.getGroovyCode());
					// getting all methods defined in the observer context
					Iterable<ProcedureDefinition> iter = relogoClass.methods();
					for (ProcedureDefinition proc : iter) {
						observerMethods.add(proc);
					}
				}
				// Collect the global attributes into addGlobal('attribute')
				// statement,
				// globalsCode
				if (relogoClass != null && relogoClass.getClassName() == "*global*") {

					if (hasAttributesToGenerate(relogoClass)) {
						for (Attribute attr : relogoClass.attributes()) {
							if (attr.generate) {
								String name = attr.toString();
								globalsCode.append("addGlobal('" + name + "')\n");
							}
						}
					}
				}
			}
			if (globalsCode.length() != 0) {
				ugpfTemplate.setAttribute("globals", globalsCode.toString());
			}
			/**
			 * Use the information gathered by the NetlogoSimulation scan method to
			 * generate the appropriate UPCC code, using the UPCC.stg group string
			 * template file.
			 * 
			 */
			List<ProcedureDefinition> observerNonButtonMethods = new ArrayList<ProcedureDefinition>();
			List<ProcedureDefinition> observerButtonMethods = new ArrayList<ProcedureDefinition>();
			List<ProcedureDefinition> observerToggleButtonMethods = new ArrayList<ProcedureDefinition>();
			for (ProcedureDefinition procDef : observerMethods) {
				if (procDef.getName().startsWith("button_method_")) {
					observerButtonMethods.add(procDef);
				} else if (procDef.getName().startsWith("toggle_button_method_")) {
					observerToggleButtonMethods.add(procDef);
				} else {
					observerNonButtonMethods.add(procDef);
				}
			}
			// split this into toggle and button methods
			StringBuffer ugpfComponents = new StringBuffer();

			// button methods
			StringTemplate buttonTemplate = ugpfTemplateGroup.getInstanceOf("button");
			buttonTemplate.setAttribute("observerName", "default_observer");
			for (ProcedureDefinition procDef : observerButtonMethods) {

				boolean generateButtonUsingExistingMethod = false;
				String buttonMethodName = procDef.getName();
				LinkedList ll = procDef.getCode().getContents();
				if (ll.size() == 1) {
					Object o = ll.getFirst();
					if (o instanceof ProcedureInvocation) {
						String buttonInnerMethodName = ((ProcedureInvocation) o).getProfile().getJavaName();
						for (ProcedureDefinition obsProcDef : observerNonButtonMethods) {
							String observerMethodName = obsProcDef.getProfile().getJavaName();
							if (observerMethodName.equals(buttonInnerMethodName)
									&& obsProcDef.getProfile().getSize() == 1) {
								generateButtonUsingExistingMethod = true;
								buttonMethodName = observerMethodName;
								break;
							}
						}
					}
				}

				if (generateButtonUsingExistingMethod) {
					// create button with existing method name as argument
					buttonTemplate.setAttribute("methodName", buttonMethodName);
				} else {
					// create button with button method name as argument
					buttonMethodName = buttonMethodName.substring(14);
					procDef.setName(buttonMethodName);
					buttonTemplate.setAttribute("methodName", buttonMethodName);
					// add button method to UserObserver bodyCode
					bodyCode.append(procDef);
					bodyCode.append("\n\n");
					// bodyCode.append(buttonTemplate.toString());
				}
				/**
				 * if (allBreedsOwnCode.length()!=0){ allBreedsOwnCode.append("\n"); }
				 */
				if (ugpfComponents.length() != 0) {
					ugpfComponents.append("\n");
				}
				ugpfComponents.append(buttonTemplate.toString());
				buttonTemplate.removeAttribute("methodName");
			}

			// toggle button methods
			StringTemplate toggleButtonTemplate = ugpfTemplateGroup.getInstanceOf("toggleButton");
			toggleButtonTemplate.setAttribute("observerName", "default_observer");
			for (ProcedureDefinition procDef : observerToggleButtonMethods) {
				boolean generateButtonUsingExistingMethod = false;
				String buttonMethodName = procDef.getName();
				LinkedList ll = procDef.getCode().getContents();
				if (ll.size() == 1) {
					Object o = ll.getFirst();
					if (o instanceof ProcedureInvocation) {
						String buttonInnerMethodName = ((ProcedureInvocation) o).getProfile().getJavaName();
						for (ProcedureDefinition obsProcDef : observerNonButtonMethods) {
							String observerMethodName = obsProcDef.getProfile().getJavaName();
							if (observerMethodName.equals(buttonInnerMethodName)) {
								generateButtonUsingExistingMethod = true;
								buttonMethodName = observerMethodName;
								break;
							}
						}
					}
				}

				if (generateButtonUsingExistingMethod) {
					// create button with existing method name as argument
					toggleButtonTemplate.setAttribute("methodName", buttonMethodName);
				} else {
					buttonMethodName = buttonMethodName.substring(21);
					procDef.setName(buttonMethodName);
					// create button with button method name as argument
					toggleButtonTemplate.setAttribute("methodName", buttonMethodName);
					// add button method to UserObserver bodyCode
					bodyCode.append(procDef);
					bodyCode.append("\n\n");
					// bodyCode.append(buttonTemplate.toString());
				}
				/**
				 * if (allBreedsOwnCode.length()!=0){ allBreedsOwnCode.append("\n"); }
				 */
				if (ugpfComponents.length() != 0) {
					ugpfComponents.append("\n");
				}
				ugpfComponents.append(toggleButtonTemplate.toString());
				toggleButtonTemplate.removeAttribute("methodName");
			}

			StringTemplate uoTemplate = userOTPLTemplateGroup.getInstanceOf("userObserver");
			uoTemplate.setAttribute("packageName", packageName);
			uoTemplate.setAttribute("bodyCode", bodyCode.toString());
			createFileResource(relogoSourceFolder, "UserObserver.groovy", new ByteArrayInputStream(
					uoTemplate.toString().getBytes("UTF-8")));

			// at this point the UserObserver is completed.

			List<NLControl> nlControls = pageOne.getNetlogoSimulation().getNLControls();
			for (NLControl ctl : nlControls) {
				if (ctl instanceof NLMonitor) {
					StringTemplate monitorTemplate = ugpfTemplateGroup.getInstanceOf("monitor");
					String var = ((NLMonitor) ctl).getVariable();
					String label = ((NLMonitor) ctl).getLabel();
					if (label != null && label.length() > 0) {
						label = getJavaName(label);
					} else {
						label = getJavaName(var);
					}
					label = "monitor_reporter_" + label;
					// observerName,reporterName,interval
					monitorTemplate.setAttribute("observerName", "default_observer");
					monitorTemplate.setAttribute("reporterName", label);
					monitorTemplate.setAttribute("interval", 5.0);
					if (ugpfComponents.length() != 0) {
						ugpfComponents.append("\n");
					}
					ugpfComponents.append(monitorTemplate.toString());
				} else if (ctl instanceof NLChooser) {
					StringTemplate chooserTemplate = ugpfTemplateGroup.getInstanceOf("chooser");
					// variableName and list
					String var = ((NLChooser) ctl).getVariable().trim();
					int index = ((NLChooser) ctl).getInitialValue();
					List list = ((NLChooser) ctl).getChoices();
					chooserTemplate.setAttribute("variableName", camelCase(var, false));
					chooserTemplate.setAttribute("list", list);
					chooserTemplate.setAttribute("index", index);
					if (ugpfComponents.length() != 0) {
						ugpfComponents.append("\n");
					}
					ugpfComponents.append(chooserTemplate.toString());
				} else if (ctl instanceof NLInputBox) {
					StringTemplate inputTemplate = ugpfTemplateGroup.getInstanceOf("input");
					String var = ((NLInputBox) ctl).getVariable();
					Object val = ((NLInputBox) ctl).getInitialValue();
					inputTemplate.setAttribute("variableName", camelCase(var, false));
					if (val != null) {
						inputTemplate.setAttribute("value", val);
					}
					if (ugpfComponents.length() != 0) {
						ugpfComponents.append("\n");
					}
					ugpfComponents.append(inputTemplate.toString());
				} else if (ctl instanceof NLSwitch) {
					StringTemplate switchTemplate = ugpfTemplateGroup.getInstanceOf("switch");
					String var = ((NLSwitch) ctl).getVariable();
					Boolean on = (Boolean) ((NLSwitch) ctl).getInitialValue();
					switchTemplate.setAttribute("variableName", camelCase(var, false));
					if (on != null) {
						switchTemplate.setAttribute("selected", on);
					}
					if (ugpfComponents.length() != 0) {
						ugpfComponents.append("\n");
					}
					ugpfComponents.append(switchTemplate.toString());
				} else if (ctl instanceof NLSlider) {
					StringTemplate sliderTemplate = ugpfTemplateGroup.getInstanceOf("slider");
					String var = ((NLSlider) ctl).getVariable();
					double minVal = ((NLSlider) ctl).getMinimum();
					double increment = ((NLSlider) ctl).getStep();
					double maxVal = ((NLSlider) ctl).getMaximum();
					Number val = (Number) ((NLSlider) ctl).getInitialValue();
					String units = ((NLSlider) ctl).getUnits();
					sliderTemplate.setAttribute("variableName", camelCase(var, false));
					sliderTemplate.setAttribute("minVal", minVal);
					sliderTemplate.setAttribute("increment", increment);
					sliderTemplate.setAttribute("maxVal", maxVal);
					sliderTemplate.setAttribute("val", val);
					if (units != null) {
						sliderTemplate.setAttribute("units", units);
					}
					if (ugpfComponents.length() != 0) {
						ugpfComponents.append("\n");
					}
					ugpfComponents.append(sliderTemplate.toString());
				}
			}
			ugpfTemplate.setAttribute("components", ugpfComponents.toString());

			createFileResource(relogoSourceFolder, upccInstantiationName, new ByteArrayInputStream(
					ugpfTemplate.toString().getBytes("UTF-8")));
		}
	}

	public String getJavaName(String name) {
		StringBuffer buf = new StringBuffer();

		buf.append(name.trim());

		for (int i = 0; i < buf.length(); i++) {
			if (Character.isLetterOrDigit(buf.charAt(i))) {
				continue;
			} else if (buf.charAt(i) == '_') {
				continue;
			} else if (buf.charAt(i) == '?') {
				buf.setCharAt(i, 'Q');
			} else if (buf.charAt(i) == '%') {
				buf.setCharAt(i, 'p');
			} else if (buf.charAt(i) == '!') {
				buf.setCharAt(i, 'X');
			} else if (Character.isWhitespace(buf.charAt(i)) || buf.charAt(i) == '-') {
				buf.deleteCharAt(i);
				if (i < buf.length() && Character.isLetterOrDigit(buf.charAt(i))) {
					if (buf.charAt(i) != '?' && buf.charAt(i) != '%' && buf.charAt(i) != '!') {
						buf.setCharAt(i, Character.toUpperCase(buf.charAt(i)));
					} else if (buf.charAt(i) == '_') {
						continue;
					} else if (buf.charAt(i) == '?') {
						buf.setCharAt(i, 'Q');
					} else if (buf.charAt(i) == '%') {
						buf.setCharAt(i, 'P');
					} else if (buf.charAt(i) == '!') {
						buf.setCharAt(i, 'X');
					}
				}
			} else {
				buf.setCharAt(i, 'Q');
			}
		}
		if (Character.isDigit(buf.charAt(0))) {
			buf.insert(0, '_');
		}
		return buf.toString();
	}

	protected String camelCase(String text, boolean cap) {
		StringBuffer buf = new StringBuffer(text);
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '-' && buf.length() > (i + 1)) {
				buf.deleteCharAt(i);
				if (buf.charAt(i) != '?' && buf.charAt(i) != '%') {
					buf.setCharAt(i, Character.toUpperCase(buf.charAt(i)));
				} else if (buf.charAt(i) == '?') {
					buf.setCharAt(i, 'Q');
				} else if (buf.charAt(i) == '%') {
					buf.setCharAt(i, 'P');
				}
			} else if (buf.charAt(i) == '?') {
				buf.setCharAt(i, 'Q');
			} else if (buf.charAt(i) == '%') {
				buf.setCharAt(i, 'p');
			}
		}
		if (cap)
			buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
		return buf.toString();
	}
}