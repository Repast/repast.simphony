package repast.simphony.eclipse.ide;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.core.internal.catalog.provisional.ICatalog;
import org.eclipse.wst.xml.core.internal.catalog.provisional.ICatalogEntry;
import org.eclipse.wst.xml.core.internal.catalog.provisional.INextCatalog;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.core.internal.preferences.XMLCorePreferenceNames;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.eclipse.wst.xml.ui.internal.wizards.NewModelWizard;
import org.eclipse.wst.xml.ui.internal.wizards.NewXMLGenerator;
import org.eclipse.wst.xml.ui.internal.wizards.XMLWizardsMessages;

import com.ibm.icu.text.Collator;

/**
 * Modified version of org.eclipse.wst.xml.ui.internal.wizards.NewXMLWizard as
 * a wizard for creating Repast Simphony XML files.
 * 
 * @author Eric Tatara
 *
 */
public class NewRepastXMLFileWizard extends NewModelWizard {
	protected static final String[] filePageFilterExtensions = {".xml"}; //$NON-NLS-1$
	protected static final long XML_EDITOR_FILE_SIZE_LIMIT = 26214400; // 25 mb

	protected NewFilePage newFilePage;
	protected StartPage startPage;
	protected String cmDocumentErrorMessage;

	protected NewXMLGenerator generator;

	/**
	 * Create the new XML file wizard using the provided XML Catalog Entry key
	 * to pick the XML Catalog Entry to be used.
	 * 
	 * @param catalogEntryKey the key that matches the XML Catalog Entry to be used
	 * for the XML grammar.  This will match the name of the 
	 * org.eclipse.wst.xml.core.catalogContribution defined in the plugin that defines
	 * the extension.
	 */
	public NewRepastXMLFileWizard(String catalogEntryKey) {
		setWindowTitle(XMLWizardsMessages._UI_WIZARD_CREATE_NEW_TITLE);
		ImageDescriptor descriptor = XMLEditorPluginImageHelper.getInstance().getImageDescriptor(XMLEditorPluginImages.IMG_WIZBAN_GENERATEXML);
		setDefaultPageImageDescriptor(descriptor);
		generator = new NewXMLGenerator();

		ICatalog xmlCatalog = XMLCorePlugin.getDefault().getDefaultXMLCatalog();

		List<ICatalogEntry> catalogEntries = getXMLCatalogEntries(xmlCatalog); 

		for (ICatalogEntry catalogEntry : catalogEntries){								
			if (catalogEntry.getKey().equals(catalogEntryKey)){
				generator.setGrammarURI(catalogEntry.getURI());
				generator.setXMLCatalogEntry(catalogEntry);
				break;
			}
		}
	}

	private String getDefaultPrefix(List nsInfoList) {
		String defaultPrefix = "p"; //$NON-NLS-1$
		if (nsInfoList == null) {
			return defaultPrefix;
		}

		Vector v = new Vector();
		for (int i = 0; i < nsInfoList.size(); i++) {
			NamespaceInfo nsinfo = (NamespaceInfo) nsInfoList.get(i);
			if (nsinfo.prefix != null) {
				v.addElement(nsinfo.prefix);
			}
		}

		if (v.contains(defaultPrefix)) {
			String s = defaultPrefix;
			for (int j = 0; v.contains(s); j++) {
				s = defaultPrefix + Integer.toString(j);
			}
			return s;
		}
		return defaultPrefix;
	}


	protected List<ICatalogEntry> getXMLCatalogEntries(ICatalog xmlCatalog) {
		List<ICatalogEntry> result = null;

		result = new Vector();
		INextCatalog[] nextCatalogs = xmlCatalog.getNextCatalogs();
		for (int i = 0; i < nextCatalogs.length; i++) {
			INextCatalog catalog = nextCatalogs[i];
			ICatalog referencedCatalog = catalog.getReferencedCatalog();
			if (referencedCatalog != null) {
				if (XMLCorePlugin.SYSTEM_CATALOG_ID.equals(referencedCatalog.getId())) {
					ICatalog systemCatalog = referencedCatalog;
					ICatalogEntry[] entries = systemCatalog.getCatalogEntries(); 
					for (int j = 0; j < entries.length; j++) {
						ICatalogEntry entry = entries[j];
						result.add(entry);
					}
				}
				else if (XMLCorePlugin.USER_CATALOG_ID.equals(referencedCatalog.getId())) {
					ICatalog userCatalog = referencedCatalog;

					ICatalogEntry[] entries = userCatalog.getCatalogEntries(); 
					for (int j = 0; j < entries.length; j++) {
						ICatalogEntry entry = entries[j];
						result.add(entry);
					}
				}
			}
		}

		return result;
	}

	public void addPages() {
		String grammarURI = generator.getGrammarURI();

		// new file page
		newFilePage = new NewFilePage(fSelection);
		newFilePage.setTitle(XMLWizardsMessages._UI_WIZARD_CREATE_XML_FILE_HEADING);
		newFilePage.setDescription(XMLWizardsMessages._UI_WIZARD_CREATE_XML_FILE_EXPL);
		newFilePage.defaultName = (grammarURI != null) ? URIHelper.removeFileExtension(URIHelper.getLastSegment(grammarURI)) : "NewFile"; //$NON-NLS-1$
		Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
		String ext = preference.getString(XMLCorePreferenceNames.DEFAULT_EXTENSION);
		newFilePage.defaultFileExtension = "." + ext; //$NON-NLS-1$
		newFilePage.filterExtensions = filePageFilterExtensions;
		addPage(newFilePage);
	}


	public IWizardPage getStartingPage() {
		WizardPage result = null;
		if (startPage != null) {
			result = startPage;
		}
		else {
			result = newFilePage;
		}
		return result;
	}

	public boolean canFinish() {			
		return true;
	}

	private void configureGenerator(){
		try {
			if (generator.getCMDocument() == null) {
				final String[] errorInfo = new String[2];
				final CMDocument[] cmdocs = new CMDocument[1];
				Runnable r = new Runnable() {
					public void run() {
						cmdocs[0] = NewXMLGenerator.createCMDocument(generator.getGrammarURI(), errorInfo);
					}
				};
				org.eclipse.swt.custom.BusyIndicator.showWhile(Display.getCurrent(), r);

				generator.setCMDocument(cmdocs[0]);
				cmDocumentErrorMessage = errorInfo[1];
			}

			if ((generator.getCMDocument() != null) && (cmDocumentErrorMessage == null)) {					
				CMNamedNodeMap nameNodeMap = generator.getCMDocument().getElements();
				Vector nameNodeVector = new Vector();

				for (int i = 0; i < nameNodeMap.getLength(); i++) {
					CMElementDeclaration cmElementDeclaration = (CMElementDeclaration) nameNodeMap.item(i);
					Object value = cmElementDeclaration.getProperty("Abstract"); //$NON-NLS-1$
					if (value != Boolean.TRUE) {
						nameNodeVector.add(cmElementDeclaration.getElementName());
					}
				}

				Object[] nameNodeArray = nameNodeVector.toArray();
				if (nameNodeArray.length > 0) {
					Arrays.sort(nameNodeArray, Collator.getInstance());
				}

				generator.setRootElementName((String)nameNodeArray[0]);
			}

			generator.setDefaultSystemId(getDefaultSystemId());
			generator.createNamespaceInfoList();

			// Provide default namespace prefix if none
			for (int i = 0; i < generator.namespaceInfoList.size(); i++) {
				NamespaceInfo nsinfo = (NamespaceInfo) generator.namespaceInfoList.get(i);
				if (((nsinfo.prefix == null) || (nsinfo.prefix.trim().length() == 0)) && ((nsinfo.uri != null) && (nsinfo.uri.trim().length() != 0))) {
					nsinfo.prefix = getDefaultPrefix(generator.namespaceInfoList);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		int buildPolicy = 0;

		buildPolicy = buildPolicy | DOMContentBuilder.BUILD_FIRST_CHOICE | 
		DOMContentBuilder.BUILD_FIRST_SUBSTITUTION;
		buildPolicy = buildPolicy | DOMContentBuilder.BUILD_TEXT_NODES;

		generator.setBuildPolicy(buildPolicy);			
	}

	public boolean performFinish() {
		boolean result = super.performFinish();

		configureGenerator();

		String fileName = null;
		try {

			String[] namespaceErrors = generator.getNamespaceInfoErrors();
			if (namespaceErrors != null) {
				String title = namespaceErrors[0];
				String message = namespaceErrors[1];
				result = MessageDialog.openQuestion(getShell(), title, message);
			}

			if (result) {
				fileName = newFilePage.getFileName();
				if ((new Path(fileName)).getFileExtension() == null) {
					newFilePage.setFileName(fileName.concat(newFilePage.defaultFileExtension));
				}

				final IFile newFile = newFilePage.createNewFile();
				final String xmlFileName = newFile.getLocation().toOSString();
				final String grammarFileName = fileName;

				generator.setOptionalElementDepthLimit(2);
				setNeedsProgressMonitor(true);
				getContainer().run(true, false, new IRunnableWithProgress(){
					public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
						progressMonitor.beginTask(XMLWizardsMessages._UI_WIZARD_GENERATING_XML_DOCUMENT, IProgressMonitor.UNKNOWN);
						try {
							generator.createXMLDocument(newFile, xmlFileName);
						} catch (Exception exception) {
							Logger.logException("Exception completing New XML wizard " + grammarFileName, exception); //$NON-NLS-1$
						}
						progressMonitor.done();
					}
				});
				newFile.refreshLocal(IResource.DEPTH_ONE, null);
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				BasicNewResourceWizard.selectAndReveal(newFile, workbenchWindow);
				openEditor(newFile);
			}
		}
		catch (Exception e) {
			Logger.logException("Exception completing New XML wizard " + fileName, e); //$NON-NLS-1$
		}
		return result;
	}

	public void openEditor(IFile file) {
		long length = 0;
		IPath location = file.getLocation();
		if (location != null) {
			File localFile = location.toFile();
			length = localFile.length();
		}
		if(length < XML_EDITOR_FILE_SIZE_LIMIT) {
			// Open editor on new file.
			String editorId = null;
			try {
				IEditorDescriptor editor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getLocation().toOSString(), file.getContentDescription().getContentType());
				if (editor != null) {
					editorId = editor.getId();
				}
			}
			catch (CoreException e1) {
				// editor id could not be retrieved, so we can not open editor
				return;
			}
			IWorkbenchWindow dw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			try {
				if (dw != null) {
					IWorkbenchPage page = dw.getActivePage();
					if (page != null) {
						page.openEditor(new FileEditorInput(file), editorId, true);
					}
				}
			}
			catch (PartInitException e) {
				// editor can not open for some reason
				return;
			}
		}
	}


	protected String getDefaultSystemId() {
		String relativePath = "platform:/resource/" + newFilePage.getContainerFullPath().toString() + "/dummy"; //$NON-NLS-1$ //$NON-NLS-2$
		return URIHelper.getRelativeURI(generator.getGrammarURI(), relativePath);
	}

	public static GridLayout createOptionsPanelLayout() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		return gridLayout;
	}
}