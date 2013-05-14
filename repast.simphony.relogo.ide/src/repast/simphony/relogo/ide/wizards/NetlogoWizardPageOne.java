package repast.simphony.relogo.ide.wizards;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.preferences.CompliancePreferencePage;
import org.eclipse.jdt.internal.ui.preferences.PropertyAndPreferencePage;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathSupport;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.CheckedListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMStandin;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.osgi.framework.Bundle;

import repast.simphony.relogo.ide.code.MethodPartition;
import repast.simphony.relogo.ide.code.RelogoClass;
import repast.simphony.relogo.ide.gui.NetlogoSimulation;
import repast.simphony.relogo.ide.intf.NLPlot;
import repast.simphony.relogo.image.NLImage;


/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (nlogo).
 */

public class NetlogoWizardPageOne extends WizardPage {
	
	/**
	 * Request a project name. Fires an event whenever the text field is
	 * changed, regardless of its content. Derived from Eclipse 
	 * 'New Java Project' wizard.
	 */
	private final class NameGroup extends Observable implements IDialogFieldListener, IStringButtonAdapter {

		protected final StringDialogField fNameField;
		protected final StringDialogField fPackageNameField;
		protected final StringButtonDialogField fLocation;
		protected String netlogoSourcePath;

		private String fPreviousExternalLocation;

		private static final String DIALOGSTORE_LAST_EXTERNAL_LOC= JavaUI.ID_PLUGIN + ".last.external.project"; //$NON-NLS-1$

		protected NetlogoSimulation netlogoSimulation;
		protected MethodPartition xref = null;

		public NameGroup() {
			// text field for project name
			fNameField= new StringDialogField();
			fNameField.setLabelText("Project name"); 
			fNameField.setDialogFieldListener(this);
			
			fPackageNameField = new StringDialogField();
			fPackageNameField.setLabelText("Package name (Optional)");
			fPackageNameField.setDialogFieldListener(this);
			
			fLocation= new StringButtonDialogField(this);
			fLocation.setDialogFieldListener(this);
			fLocation.setLabelText("NetLogo Model"); 
			fLocation.setButtonLabel("Browse"); 
			
			fPreviousExternalLocation= ""; //$NON-NLS-1$
			netlogoSourcePath = null;
			netlogoSimulation = new NetlogoSimulation();
		}

		public Control createControl(Composite composite) {
			Group nameGroup= new Group(composite, SWT.NONE);
			nameGroup.setText("Project source"); 
			nameGroup.setFont(composite.getFont());
			nameGroup.setLayout(initGridLayout(new GridLayout(3, false), true));

			// if this is an import, display the NetLogo file location dialog
			if (!isNewProject){
			  fLocation.doFillIntoGrid(nameGroup, 3);
			  LayoutUtil.setHorizontalGrabbing(fLocation.getTextControl(null));
			}
			fNameField.doFillIntoGrid(nameGroup, 3);
			LayoutUtil.setHorizontalGrabbing(fNameField.getTextControl(null));
			fPackageNameField.doFillIntoGrid(nameGroup, 3);
			LayoutUtil.setHorizontalGrabbing(fPackageNameField.getTextControl(null));


			return nameGroup;
		}

		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		public String getName() {
			return fNameField.getText().trim();
		}
		
		public String getPackageName(){
			if (fPackageNameField.getText().trim().equals("")){
				return getName().toLowerCase();
			}
			return fPackageNameField.getText().trim();
		}

		protected String getDefaultPath(String name) {
			final IPath path= Platform.getLocation().append(name);
			return path.toOSString();
		}

		public String getNetlogoSourceFile() {
			return netlogoSourcePath;
		}
		
		public IPath getLocation() {
			return Path.fromOSString(fLocation.getText().trim());
		}

		public void postSetFocus() {
			fNameField.postSetFocusOnDialogField(getShell().getDisplay());
		}

		public void setName(String name) {
			fNameField.setText(name);
		}
		
		public void setPackageName(String name) {
			fPackageNameField.setText(name);
		}

		public void setLocation(IPath path) {
			if (path != null) {
				fLocation.setText(path.toOSString());
			} else {
				fLocation.setText("");//getDefaultPath(fNameGroup.getName()));
			}
			fireEvent();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter#changeControlPressed(org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void changeControlPressed(DialogField field) {
			final FileDialog dialog= new FileDialog(getShell());
			//dialog.setMessage("Select the Netlogo file to import"); 
			String directoryName = fLocation.getText().trim();
			if (directoryName.length() == 0) {
				String prevLocation= JavaPlugin.getDefault().getDialogSettings().get(DIALOGSTORE_LAST_EXTERNAL_LOC);
				if (prevLocation != null) {
					directoryName= prevLocation;
				}
			}

			if (directoryName.length() > 0) {
				final File path = new File(directoryName);
				if (path.exists())
					dialog.setFilterPath(directoryName);
			}
			final String selectedDirectory = dialog.open();
			if (selectedDirectory != null) {
				fLocation.setText(selectedDirectory);
				JavaPlugin.getDefault().getDialogSettings().put(DIALOGSTORE_LAST_EXTERNAL_LOC, selectedDirectory);
			}
		}

		protected String extractJavaName(String name) {
			String[] nameBits = name.split("\\p{Space}|\\p{Punct}");
			name = nameBits[0];
			for (int i=1; i<nameBits.length; i++) {
				name = name + nameBits[i];
			}
			return name;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void dialogFieldChanged(DialogField field) {
			if (field == fLocation) {
				// initialize the project name
				String directoryName = fLocation.getText().trim();
				try {
					File sourceFile = new File(directoryName);
					String fileName = sourceFile.getName();
					if (fileName.endsWith(".nlogo") || fileName.endsWith("null.model")) {
						netlogoSourcePath = fileName;
						fileName = extractJavaName(fileName.substring(0, fileName.length() - 6));
						netlogoSimulation.scan(directoryName);
						setName(fileName);
						xref = null;
					} else {
						netlogoSourcePath = null;
						setName("");
					}
				} catch (Exception e) {
					// just silently ignore any problems with the filename, and allow the Validator
					// to rule on overall validity of the spec.
				}
			}
			fireEvent();
		}
	}
	
	private final class ShapesListLabelProvider extends BaseLabelProvider implements ILabelProvider{

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof NLImage){
				NLImage image = (NLImage) element;
				return image.getName();
			}
			return null;
		}
		
	}
	
	
	private final class ShapesGroup extends Observable {
		
		protected final CheckedListDialogField fCheckedListField;
		
		public ShapesGroup(){
			/*IListAdapter listAdapter = new IListAdapter(){

				@Override
				public void customButtonPressed(ListDialogField field, int index) {
					
					
				}

				@Override
				public void selectionChanged(ListDialogField field) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void doubleClicked(ListDialogField field) {
					// TODO Auto-generated method stub
					
				}
				
			};*/
			String[] customButtonLabels = {"Deselect All"};
			ShapesListLabelProvider lprovider = new ShapesListLabelProvider();
			fCheckedListField = new CheckedListDialogField(null, customButtonLabels, lprovider);
			fCheckedListField.setUncheckAllButtonIndex(0);
			fCheckedListField.setLabelText("Please only import licensed shapes (e.g., those you've created).");
		}
		
		public List<NLImage> getCheckedShapesNames(){
			List<NLImage> checkedShapesNames = new ArrayList<NLImage>();
			for (Object o: fCheckedListField.getCheckedElements()){
				if (o instanceof NLImage){
					NLImage image = (NLImage) o;
					checkedShapesNames.add(image);
				}
			}
			return checkedShapesNames;
		}
		
		public Control createControl(Composite composite){
			Group shapesGroup= new Group(composite, SWT.NONE);
			shapesGroup.setText("Shapes to import"); 
			shapesGroup.setFont(composite.getFont());
			shapesGroup.setLayout(initGridLayout(new GridLayout(1, false), true));
			fCheckedListField.doFillIntoGrid(shapesGroup, fCheckedListField.getNumberOfControls());
			LayoutUtil.setHorizontalGrabbing(fCheckedListField.getListControl(null));

			return shapesGroup;
		}
	}

	/**
	 * Show a warning when the project location contains files.
	 */
	private final class DetectGroup extends Observable implements Observer, SelectionListener {

		private Link fHintText;
		private Label fIcon;
		private boolean fDetect;

		public DetectGroup() {
			fDetect= false;
		}

		public Control createControl(Composite parent) {

			Composite composite= new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			GridLayout layout= new GridLayout(2, false);
			layout.horizontalSpacing= 10;
			composite.setLayout(layout);

			fIcon= new Label(composite, SWT.LEFT);
			fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_WARNING));
			GridData gridData= new GridData(SWT.LEFT, SWT.CENTER, false, false);
			fIcon.setLayoutData(gridData);

			fHintText= new Link(composite, SWT.WRAP);
			fHintText.setFont(composite.getFont());
			fHintText.addSelectionListener(this);
			gridData= new GridData(GridData.FILL, SWT.FILL, true, true);
			gridData.widthHint= convertWidthInCharsToPixels(50);
			gridData.heightHint= convertHeightInCharsToPixels(3);
			fHintText.setLayoutData(gridData);

			handlePossibleJVMChange();
			return composite;
		}

		public void handlePossibleJVMChange() {

			if (JavaRuntime.getDefaultVMInstall() == null) {
				fHintText.setText(NewWizardMessages.NewJavaProjectWizardPageOne_NoJREFound_link);
				fHintText.setVisible(true);
				fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_WARNING));
				fIcon.setVisible(true);
				return;
			}

			String selectedCompliance= fJREGroup.getSelectedCompilerCompliance();
			if (selectedCompliance != null) {
				String defaultCompliance= JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE);
				if (selectedCompliance.equals(defaultCompliance)) {
					fHintText.setVisible(false);
					fIcon.setVisible(false);
				} else {
					fHintText.setText(Messages.format(NewWizardMessages.NewJavaProjectWizardPageOne_DetectGroup_differendWorkspaceCC_message, new String[] {  BasicElementLabels.getVersionName(defaultCompliance), BasicElementLabels.getVersionName(selectedCompliance)}));
					fHintText.setVisible(true);
					fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
					fIcon.setVisible(true);
				}
				return;
			}

			selectedCompliance= JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE);
			IVMInstall selectedJVM= fJREGroup.getSelectedJVM();
			if (selectedJVM == null) {
				selectedJVM= JavaRuntime.getDefaultVMInstall();
			}
			String jvmCompliance= JavaCore.VERSION_1_4;
			if (selectedJVM instanceof IVMInstall2) {
				jvmCompliance= JavaModelUtil.getCompilerCompliance((IVMInstall2) selectedJVM, JavaCore.VERSION_1_4);
			}
			if (!selectedCompliance.equals(jvmCompliance) && (JavaModelUtil.is50OrHigher(selectedCompliance) || JavaModelUtil.is50OrHigher(jvmCompliance))) {
				if (selectedCompliance.equals(JavaCore.VERSION_1_5))
					selectedCompliance= "5.0"; //$NON-NLS-1$
				else if (selectedCompliance.equals(JavaCore.VERSION_1_6))
					selectedCompliance= "6.0"; //$NON-NLS-1$

				fHintText.setText(Messages.format(NewWizardMessages.NewJavaProjectWizardPageOne_DetectGroup_jre_message, new String[] {BasicElementLabels.getVersionName(selectedCompliance), BasicElementLabels.getVersionName(jvmCompliance)}));
				fHintText.setVisible(true);
				fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_WARNING));
				fIcon.setVisible(true);
			} else {
				fHintText.setVisible(false);
				fIcon.setVisible(false);
			}
		}

		private boolean computeDetectState() {
				final File directory= fNameGroup.getLocation().toFile();
				return directory.isDirectory();
		}

		public void update(Observable o, Object arg) {
			if (o instanceof NameGroup) {
				boolean oldDetectState= fDetect;
				fDetect= computeDetectState();

				if (oldDetectState != fDetect) {
					setChanged();
					notifyObservers();

					if (fDetect) {
						fHintText.setVisible(true);
						fHintText.setText(NewWizardMessages.NewJavaProjectWizardPageOne_DetectGroup_message);
						fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
						fIcon.setVisible(true);
					} else {
						handlePossibleJVMChange();
					}
				}
			}
		}

		public boolean mustDetect() {
			return fDetect;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			String jreID= BuildPathSupport.JRE_PREF_PAGE_ID;
			String complianceId= CompliancePreferencePage.PREF_ID;
			Map data= new HashMap();
			data.put(PropertyAndPreferencePage.DATA_NO_LINK, Boolean.TRUE);
			String id= "JRE".equals(e.text) ? jreID : complianceId; //$NON-NLS-1$
			PreferencesUtil.createPreferenceDialogOn(getShell(), id, new String[] { jreID, complianceId  }, data).open();

			fJREGroup.handlePossibleJVMChange();
			handlePossibleJVMChange();
		}
	}

	private final class JREGroup implements Observer, SelectionListener, IDialogFieldListener {

		private static final String LAST_SELECTED_EE_SETTINGS_KEY= JavaUI.ID_PLUGIN + ".last.selected.execution.enviroment"; //$NON-NLS-1$
		private static final String LAST_SELECTED_JRE_SETTINGS_KEY= JavaUI.ID_PLUGIN + ".last.selected.project.jre"; //$NON-NLS-1$
		private static final String LAST_SELECTED_JRE_KIND= JavaUI.ID_PLUGIN + ".last.selected.jre.kind"; //$NON-NLS-1$

		private static final int DEFAULT_JRE= 0;
		private static final int PROJECT_JRE= 1;
		private static final int EE_JRE= 2;

		private final SelectionButtonDialogField fUseDefaultJRE, fUseProjectJRE, fUseEEJRE;
		private final ComboDialogField fJRECombo;
		private final ComboDialogField fEECombo;
		private Group fGroup;
		private Link fPreferenceLink;
		private IVMInstall[] fInstalledJVMs;
		private String[] fJRECompliance;
		private IExecutionEnvironment[] fInstalledEEs;
		private String[] fEECompliance;

		public JREGroup() {
			fUseDefaultJRE= new SelectionButtonDialogField(SWT.RADIO);
			fUseDefaultJRE.setLabelText(getDefaultJVMLabel());

			fUseProjectJRE= new SelectionButtonDialogField(SWT.RADIO);
			fUseProjectJRE.setLabelText(NewWizardMessages.NewJavaProjectWizardPageOne_JREGroup_specific_compliance);

			fJRECombo= new ComboDialogField(SWT.READ_ONLY);
			fillInstalledJREs(fJRECombo);
			fJRECombo.setDialogFieldListener(this);

			fUseEEJRE= new SelectionButtonDialogField(SWT.RADIO);
			fUseEEJRE.setLabelText(NewWizardMessages.NewJavaProjectWizardPageOne_JREGroup_specific_EE);

			fEECombo= new ComboDialogField(SWT.READ_ONLY);
			fillExecutionEnvironments(fEECombo);
			fEECombo.setDialogFieldListener(this);

			switch (getLastSelectedJREKind()) {
				case DEFAULT_JRE:				
					fUseDefaultJRE.setSelection(true);
					break;
				case PROJECT_JRE:
					fUseProjectJRE.setSelection(true);
					break;
				case EE_JRE:
					fUseEEJRE.setSelection(true);
					break;
			}

			fJRECombo.setEnabled(fUseProjectJRE.isSelected());
			fEECombo.setEnabled(fUseEEJRE.isSelected());

			fUseDefaultJRE.setDialogFieldListener(this);
			fUseProjectJRE.setDialogFieldListener(this);
			fUseEEJRE.setDialogFieldListener(this);
		}

		public Control createControl(Composite composite) {
			fGroup= new Group(composite, SWT.NONE);
			fGroup.setFont(composite.getFont());
			fGroup.setLayout(initGridLayout(new GridLayout(2, false), true));
			fGroup.setText(NewWizardMessages.NewJavaProjectWizardPageOne_JREGroup_title);

			fUseDefaultJRE.doFillIntoGrid(fGroup, 1);

			fPreferenceLink= new Link(fGroup, SWT.NONE);
			fPreferenceLink.setFont(fGroup.getFont());
			fPreferenceLink.setText(NewWizardMessages.NewJavaProjectWizardPageOne_JREGroup_link_description);
			fPreferenceLink.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			fPreferenceLink.addSelectionListener(this);

			Composite nonDefaultJREComposite= new Composite(fGroup, SWT.NONE);
			nonDefaultJREComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			GridLayout layout= new GridLayout(2, false);
			layout.marginHeight= 0;
			layout.marginWidth= 0;
			nonDefaultJREComposite.setLayout(layout);

			fUseProjectJRE.doFillIntoGrid(nonDefaultJREComposite, 1);

			Combo comboControl= fJRECombo.getComboControl(nonDefaultJREComposite);
			comboControl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			comboControl.setVisibleItemCount(30);

			fUseEEJRE.doFillIntoGrid(nonDefaultJREComposite, 1);

			Combo eeComboControl= fEECombo.getComboControl(nonDefaultJREComposite);
			eeComboControl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			eeComboControl.setVisibleItemCount(30);

			updateEnableState();
			return fGroup;
		}


		private void fillInstalledJREs(ComboDialogField comboField) {
			String selectedItem= getLastSelectedJRE();
			int selectionIndex= -1;
			if (fUseProjectJRE.isSelected()) {
				selectionIndex= comboField.getSelectionIndex();
				if (selectionIndex != -1) {//paranoia
					selectedItem= comboField.getItems()[selectionIndex];
				}
			}

			fInstalledJVMs= getWorkspaceJREs();
			Arrays.sort(fInstalledJVMs, new Comparator() {

				public int compare(Object arg0, Object arg1) {
					IVMInstall i0= (IVMInstall)arg0;
					IVMInstall i1= (IVMInstall)arg1;
					if (i1 instanceof IVMInstall2 && i0 instanceof IVMInstall2) {
						String cc0= JavaModelUtil.getCompilerCompliance((IVMInstall2) i0, JavaCore.VERSION_1_4);
						String cc1= JavaModelUtil.getCompilerCompliance((IVMInstall2) i1, JavaCore.VERSION_1_4);
						int result= cc1.compareTo(cc0);
						if (result != 0)
							return result;
					}
					return Policy.getComparator().compare(i0.getName(), i1.getName());
				}

			});
			selectionIndex= -1;//find new index
			String[] jreLabels= new String[fInstalledJVMs.length];
			fJRECompliance= new String[fInstalledJVMs.length];
			for (int i= 0; i < fInstalledJVMs.length; i++) {
				jreLabels[i]= fInstalledJVMs[i].getName();
				if (selectedItem != null && jreLabels[i].equals(selectedItem)) {
					selectionIndex= i;
				}
				if (fInstalledJVMs[i] instanceof IVMInstall2) {
					fJRECompliance[i]= JavaModelUtil.getCompilerCompliance((IVMInstall2) fInstalledJVMs[i], JavaCore.VERSION_1_4);
				} else {
					fJRECompliance[i]= JavaCore.VERSION_1_4;
				}
			}
			comboField.setItems(jreLabels);
			if (selectionIndex == -1) {
				comboField.selectItem(getDefaultJVMName());
			} else {
				comboField.selectItem(selectedItem);
			}
		}

		private void fillExecutionEnvironments(ComboDialogField comboField) {
			String selectedItem= getLastSelectedEE();
			int selectionIndex= -1;
			if (fUseEEJRE.isSelected()) {
				selectionIndex= comboField.getSelectionIndex();
				if (selectionIndex != -1) {// paranoia
					selectedItem= comboField.getItems()[selectionIndex];
				}
			}

			fInstalledEEs= JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
			Arrays.sort(fInstalledEEs, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return Policy.getComparator().compare(((IExecutionEnvironment)arg0).getId(), ((IExecutionEnvironment)arg1).getId());
				}
			});
			selectionIndex= -1;//find new index
			String[] eeLabels= new String[fInstalledEEs.length];
			fEECompliance= new String[fInstalledEEs.length];
			for (int i= 0; i < fInstalledEEs.length; i++) {
				eeLabels[i]= fInstalledEEs[i].getId();
				if (selectedItem != null && eeLabels[i].equals(selectedItem)) {
					selectionIndex= i;
				}
				fEECompliance[i]= JavaModelUtil.getExecutionEnvironmentCompliance(fInstalledEEs[i]);
			}
			comboField.setItems(eeLabels);
			if (selectionIndex == -1) {
				comboField.selectItem(getDefaultEEName());
			} else {
				comboField.selectItem(selectedItem);
			}
		}

		private IVMInstall[] getWorkspaceJREs() {
			List standins = new ArrayList();
			IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
			for (int i = 0; i < types.length; i++) {
				IVMInstallType type = types[i];
				IVMInstall[] installs = type.getVMInstalls();
				for (int j = 0; j < installs.length; j++) {
					IVMInstall install = installs[j];
					standins.add(new VMStandin(install));
				}
			}
			return ((IVMInstall[])standins.toArray(new IVMInstall[standins.size()]));	
		}

		private String getDefaultJVMName() {
			IVMInstall install= JavaRuntime.getDefaultVMInstall();
			if (install != null) {
				return install.getName();
			} else {
				return NewWizardMessages.NewJavaProjectWizardPageOne_UnknownDefaultJRE_name;
			}
		}

		private String getDefaultEEName() {
			IVMInstall defaultVM= JavaRuntime.getDefaultVMInstall();

			IExecutionEnvironment[] environments= JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
			if (defaultVM != null) {
				for (int i= 0; i < environments.length; i++) {
					IVMInstall eeDefaultVM= environments[i].getDefaultVM();
					if (eeDefaultVM != null && defaultVM.getId().equals(eeDefaultVM.getId()))
						return environments[i].getId();			
				}
			}

			String defaultCC;
			if (defaultVM instanceof IVMInstall2) {
				defaultCC= JavaModelUtil.getCompilerCompliance((IVMInstall2)defaultVM, JavaCore.VERSION_1_4);
			} else {
				defaultCC= JavaCore.VERSION_1_4;
			}

			for (int i= 0; i < environments.length; i++) {
				String eeCompliance= JavaModelUtil.getExecutionEnvironmentCompliance(environments[i]);
				if (defaultCC.endsWith(eeCompliance))
					return environments[i].getId();
			}

			return "J2SE-1.5"; //$NON-NLS-1$
		}

		private String getDefaultJVMLabel() {
			return Messages.format(NewWizardMessages.NewJavaProjectWizardPageOne_JREGroup_default_compliance, getDefaultJVMName());
		}

		public void update(Observable o, Object arg) {
			updateEnableState();
		}

		private void updateEnableState() {
			final boolean detect= fDetectGroup.mustDetect();
			fUseDefaultJRE.setEnabled(!detect);
			fUseProjectJRE.setEnabled(!detect);
			fUseEEJRE.setEnabled(!detect);
			fJRECombo.setEnabled(!detect && fUseProjectJRE.isSelected());
			fEECombo.setEnabled(!detect && fUseEEJRE.isSelected());
			if (fPreferenceLink != null) {
				fPreferenceLink.setEnabled(!detect);
			}
			if (fGroup != null) {
				fGroup.setEnabled(!detect);
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			String jreID= BuildPathSupport.JRE_PREF_PAGE_ID;
			String complianceId= CompliancePreferencePage.PREF_ID;
			Map data= new HashMap();
			data.put(PropertyAndPreferencePage.DATA_NO_LINK, Boolean.TRUE);
			PreferencesUtil.createPreferenceDialogOn(getShell(), jreID, new String[] { jreID, complianceId  }, data).open();

			handlePossibleJVMChange();
			fDetectGroup.handlePossibleJVMChange();
		}

		public void handlePossibleJVMChange() {
			fUseDefaultJRE.setLabelText(getDefaultJVMLabel());
			fillInstalledJREs(fJRECombo);
			fillExecutionEnvironments(fEECombo);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void dialogFieldChanged(DialogField field) {
			updateEnableState();
			fDetectGroup.handlePossibleJVMChange();
			if (field == fJRECombo) {
				if (fUseProjectJRE.isSelected()) {
					storeSelectionValue(fJRECombo, LAST_SELECTED_JRE_SETTINGS_KEY);
				}
			} else if (field == fEECombo) {
				if (fUseEEJRE.isSelected()) {
					storeSelectionValue(fEECombo, LAST_SELECTED_EE_SETTINGS_KEY);
				}
			} else if (field == fUseDefaultJRE) {
				if (fUseDefaultJRE.isSelected()) {
					JavaPlugin.getDefault().getDialogSettings().put(LAST_SELECTED_JRE_KIND, DEFAULT_JRE);
					fUseProjectJRE.setSelection(false);
					fUseEEJRE.setSelection(false);
				}
			} else if (field == fUseProjectJRE) {
				if (fUseProjectJRE.isSelected()) {
					JavaPlugin.getDefault().getDialogSettings().put(LAST_SELECTED_JRE_KIND, PROJECT_JRE);
					fUseDefaultJRE.setSelection(false);
					fUseEEJRE.setSelection(false);
				}
			} else if (field == fUseEEJRE) {
				if (fUseEEJRE.isSelected()) {
					JavaPlugin.getDefault().getDialogSettings().put(LAST_SELECTED_JRE_KIND, EE_JRE);
					fUseDefaultJRE.setSelection(false);
					fUseProjectJRE.setSelection(false);
				}
			}
		}

		private void storeSelectionValue(ComboDialogField combo, String preferenceKey) {
			int index= combo.getSelectionIndex();
			if (index == -1)
				return;

			String item= combo.getItems()[index];
			JavaPlugin.getDefault().getDialogSettings().put(preferenceKey, item);
		}

		private int getLastSelectedJREKind() {
			IDialogSettings settings= JavaPlugin.getDefault().getDialogSettings();
			if (settings.get(LAST_SELECTED_JRE_KIND) == null)
				return DEFAULT_JRE;

			return settings.getInt(LAST_SELECTED_JRE_KIND);
		}

		private String getLastSelectedEE() {
			IDialogSettings settings= JavaPlugin.getDefault().getDialogSettings();
			return settings.get(LAST_SELECTED_EE_SETTINGS_KEY);
		}

		private String getLastSelectedJRE() {
			IDialogSettings settings= JavaPlugin.getDefault().getDialogSettings();
			return settings.get(LAST_SELECTED_JRE_SETTINGS_KEY);
		}

		public IVMInstall getSelectedJVM() {
			if (fUseProjectJRE.isSelected()) {
				int index= fJRECombo.getSelectionIndex();
				if (index >= 0 && index < fInstalledJVMs.length) { // paranoia
					return fInstalledJVMs[index];
				}
			} else if (fUseEEJRE.isSelected()) {

			}
			return null;
		}

		public IPath getJREContainerPath() {
			if (fUseProjectJRE.isSelected()) {
				int index= fJRECombo.getSelectionIndex();
				if (index >= 0 && index < fInstalledJVMs.length) { // paranoia
					return JavaRuntime.newJREContainerPath(fInstalledJVMs[index]);
				}
			} else if (fUseEEJRE.isSelected()) {
				int index= fEECombo.getSelectionIndex();
				if (index >= 0 && index < fInstalledEEs.length) { // paranoia
					return JavaRuntime.newJREContainerPath(fInstalledEEs[index]);
				}
			}
			return null;
		}

		public String getSelectedCompilerCompliance() {
			if (fUseProjectJRE.isSelected()) {
				int index= fJRECombo.getSelectionIndex();
				if (index >= 0 && index < fJRECompliance.length) { // paranoia
					return fJRECompliance[index];
				}
			} else if (fUseEEJRE.isSelected()) {
				int index= fEECombo.getSelectionIndex();
				if (index >= 0 && index < fEECompliance.length) { // paranoia
					return fEECompliance[index];
				}
			}
			return null;
		}
	}

	/**
	 * Validate this page and show appropriate warnings and error NewWizardMessages.
	 */
	private final class Validator implements Observer {

		public void update(Observable o, Object arg) {

			final IWorkspace workspace= JavaPlugin.getWorkspace();

			final String netlogoSourceFile= fNameGroup.getNetlogoSourceFile();
			final String netlogoSourcePath = fNameGroup.netlogoSourcePath; 
			final String name= fNameGroup.getName();
			final String packageName = fNameGroup.getPackageName(); 

			// check whether a NetLogo file or null.model file was selected
			if (netlogoSourceFile == null) { 
				setErrorMessage(null);
				setMessage("Select a NetLogo Model to import"); 
				setPageComplete(false);
				return;
			}

			// check whether the project name field is empty
			if (name.length() == 0) { 
				setErrorMessage(null);
				setMessage("Enter a project name"); 
				setPageComplete(false);
				return;
			}
			
			// check whether the project name has spaces
			if (!name.replace(" ","").equals(name)) { 
				setErrorMessage(null);
				setMessage("Project name should not contain spaces"); 
				setPageComplete(false);
				return;
			}
			
			// check whether the project name is valid
			final IStatus nameStatus= workspace.validateName(name, IResource.PROJECT);
			if (!nameStatus.isOK()) {
				setErrorMessage(nameStatus.getMessage());
				setPageComplete(false);
				return;
			}

			// check whether project already exists
			final IProject handle= workspace.getRoot().getProject(name);
			if (handle.exists()) {
				setErrorMessage(NewWizardMessages.NewJavaProjectWizardPageOne_Message_projectAlreadyExists); 
				setPageComplete(false);
				return;
			}

			IPath projectLocation= ResourcesPlugin.getWorkspace().getRoot().getLocation().append(name);
			String location= projectLocation.toString();

			if (projectLocation.toFile().exists()) {
				try {
					//correct casing
					String canonicalPath= projectLocation.toFile().getCanonicalPath();
					projectLocation= new Path(canonicalPath);
				} catch (IOException e) {
					JavaPlugin.log(e);
				}

				String existingName= projectLocation.lastSegment();
				if (!existingName.equals(fNameGroup.getName())) {
					setErrorMessage(Messages.format(NewWizardMessages.NewJavaProjectWizardPageOne_Message_invalidProjectNameForWorkspaceRoot, BasicElementLabels.getResourceName(existingName)));
					setPageComplete(false);
					return;
				}

			}
			
//			IWorkspaceDescription desc= workspace.getDescription();
//			if (desc.isAutoBuilding()){
//				desc.setAutoBuilding(false);
//				try {
//					workspace.setDescription(desc);
//				} catch (CoreException e) {
//					e.printStackTrace();
//				}
//			}
			
//			if (packageName.equals("")){
//				fNameGroup.setPackageName(name.toLowerCase());
//			}
			
			
			
			setPageComplete(true);

			if (!isNewProject){
				if (getNetlogoSimulation() != null){
					List<NLImage> images = getNetlogoSimulation().getNLImages();
					fShapesGroup.fCheckedListField.setElements(images);
				}
			}
			
			

			setErrorMessage(null);
			setMessage(null);
		}

		private boolean canCreate(File file) {
			while (!file.exists()) {
				file= file.getParentFile();
				if (file == null)
					return false;
			}

			return file.canWrite();
		}
	}

	private static final String PAGE_NAME= "NewRelogoProjectWizardPageOne"; //$NON-NLS-1$

	private final NameGroup fNameGroup;
	private final DetectGroup fDetectGroup;
	private final JREGroup fJREGroup;
	private final Validator fValidator;
	private final ShapesGroup fShapesGroup;

	private boolean isNewProject;
	private Text containerText;

	private Text fileText;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NetlogoWizardPageOne(boolean isNewProject) {
		super(PAGE_NAME);
		setPageComplete(false);
		setTitle("Specify NetLogo source and environment"); 
		

		this.isNewProject = isNewProject;
		fNameGroup= new NameGroup();
		fJREGroup= new JREGroup();
		fDetectGroup= new DetectGroup();
		
		// establish connections
		fDetectGroup.addObserver(fJREGroup);

		// initialize all elements
		fNameGroup.notifyObservers();

		// create and connect validator
		fValidator= new Validator();
		fNameGroup.addObserver(fValidator);
		
		fShapesGroup = new ShapesGroup();

		// initialize defaults
		setProjectName(""); //$NON-NLS-1$
		initializeDefaultVM();

	if (!isNewProject){
			setProjectLocationURI(null);
			setTitle("NetLogo to Repast Simphony");
			setDescription("This wizard creates a new Repast Simphony project "+
					       "with implementations of simulation classes translated from "+
					       "the NetLogo source into the Logo-like ReLogo modeling language.");
			
		}
		else {
			setTitle("Create a new ReLogo Project");
			setDescription("This wizard creates a new ReLogo Repast Simphony project.");
			Bundle myBundle = Platform.getBundle("repast.simphony.relogo.ide");
			IPath ppath = null;
			try {
				ppath = new Path(FileLocator.toFileURL(myBundle.getEntry("/templates/null.model")).getPath());
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			
			fNameGroup.setLocation(ppath);
		}
	fNameGroup.setName("NewProject");
	fNameGroup.setPackageName("");
	}

	private void initializeDefaultVM() {
		JavaRuntime.getDefaultVMInstall();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		final Composite composite= new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// create UI elements
		Control nameControl= createNameControl(composite);
		nameControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Control jreControl= createJRESelectionControl(composite);
		jreControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		if (!isNewProject){
			Control shapesControl = createShapesControl(composite);
			shapesControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

//		Control infoControl= createInfoControl(composite);
//		infoControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		setControl(composite);
	}

	protected void setControl(Control newControl) {
		Dialog.applyDialogFont(newControl);

		// For now, before I understand the Eclipse help system, no-op this.
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(newControl, IJavaHelpContextIds.NEW_JAVAPROJECT_WIZARD_PAGE);

		super.setControl(newControl);
	}


	/**
	 * Creates the controls for the name field. 
	 *  
	 * @param composite the parent composite
	 * @return the created control
	 */		
	protected Control createNameControl(Composite composite) {
		return fNameGroup.createControl(composite);
	}
	
	protected Control createShapesControl(Composite composite) {
		return fShapesGroup.createControl(composite);
	}

	/**
	 * Creates the controls for the JRE selection
	 *  
	 * @param composite the parent composite
	 * @return the created control
	 */		
	protected Control createJRESelectionControl(Composite composite) {
		return fJREGroup.createControl(composite);
	}

	/**
	 * Creates the controls for the info section. 
	 *  
	 * @param composite the parent composite
	 * @return the created control
	 */		
	protected Control createInfoControl(Composite composite) {
		return fDetectGroup.createControl(composite);
	}

	/**
	 * Gets a project name for the new project.
	 * 
	 * @return the new project name
	 */
	public String getProjectName() {
		return fNameGroup.getName();
	}
	
	/**
	 * Gets the package name for the new project.
	 * 
	 * @return the package name
	 */
	public String getPackageName() {
		return fNameGroup.getPackageName();
	}

	/**
	 * Sets the name of the new project
	 * 
	 * @param name the new name
	 */
	public void setProjectName(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		fNameGroup.setName(name);
	}

	/**
	 * Returns the current project location path as entered by the user, or <code>null</code>
	 * if the project should be created in the workspace. 

	 * @return the project location path or its anticipated initial value.
	 */
	public URI getProjectLocationURI() {
		return null;
	}

	/**
	 * Sets the project location of the new project or <code>null</code> if the project
	 * should be created in the workspace
	 * 
	 * @param uri the new project location
	 */
	public void setProjectLocationURI(URI uri) {
		IPath path= uri != null ? URIUtil.toPath(uri) : null;
		fNameGroup.setLocation(path);
	}

	/**
	 * Returns the compiler compliance to be used for the project, or <code>null</code> to use the workspace
	 * compiler compliance.
	 * 
	 * @return compiler compliance to be used for the project or <code>null</code>
	 */
	public String getCompilerCompliance() {
		return fJREGroup.getSelectedCompilerCompliance();
	}

	/**
	 * Returns the default class path entries to be added on new projects. By default this is the JRE container as
	 * selected by the user.
	 * 
	 * @return returns the default class path entries
	 */
	public IClasspathEntry[] getDefaultClasspathEntries() {
		IClasspathEntry[] defaultJRELibrary= PreferenceConstants.getDefaultJRELibrary();
		String compliance= getCompilerCompliance();
		IPath jreContainerPath= new Path(JavaRuntime.JRE_CONTAINER);
		if (compliance == null || defaultJRELibrary.length > 1 || !jreContainerPath.isPrefixOf(defaultJRELibrary[0].getPath())) {
			// use default
			return defaultJRELibrary;
		}
		IPath newPath= fJREGroup.getJREContainerPath();
		if (newPath != null) {
			return new IClasspathEntry[] { JavaCore.newContainerEntry(newPath) };
		}
		return defaultJRELibrary;
	}

	/**
	 * Returns the source class path entries to be added on new projects.
	 * The underlying resources may not exist. All entries that are returned must be of kind
	 * {@link IClasspathEntry#CPE_SOURCE}.
	 * 
	 * @return returns the source class path entries for the new project
	 */
	public IClasspathEntry[] getSourceClasspathEntries() {
		IPath sourceFolderPath= new Path(getProjectName()).makeAbsolute();

			IPath srcPath= new Path(PreferenceConstants.getPreferenceStore().getString(PreferenceConstants.SRCBIN_SRCNAME));
			if (srcPath.segmentCount() > 0) {
				sourceFolderPath= sourceFolderPath.append(srcPath);
			}
		return new IClasspathEntry[] {  JavaCore.newSourceEntry(sourceFolderPath) };
	}

	/**
	 * Returns the class path entries for Repast-S projects, to be added on new projects.
	 * The underlying resources may not exist. All entries that are returned must be of kind
	 * {@link IClasspathEntry#CPE_PROJECT}.
	 * 
	 * @return returns the source class path entries for the new project
	 */
	public IClasspathEntry[] getProjectClasspathEntries() {
		String[] projectNameList = new String[]{"libs.bsf",
		"libs.piccolo",
		"repast.simphony.chart",
		"repast.simphony.core",
		"repast.simphony.data",
		"repast.simphony.data.bsf",
		"repast.simphony.data.ui",
		"repast.simphony.dataLoader",
		"repast.simphony.dataLoader.ui",
		"repast.simphony.essentials",
		"repast.simphony.freezedry",
		"repast.simphony.gis",
		"repast.simphony.groovy",
		"repast.simphony.gui",
		"repast.simphony.integration",
		"repast.simphony.jdbcfreezedryer",
		"repast.simphony.plugin.util",
		"repast.simphony.runtime",
		"repast.simphony.score",
		"repast.simphony.score.runtime",
		"repast.simphony.visualization",
		"saf.core.ui"};
		IClasspathEntry[] projectEntries = new IClasspathEntry[projectNameList.length];
		for (int p=0; p<projectNameList.length; p++) {
		    projectEntries[p] = JavaCore.newProjectEntry(new Path(projectNameList[p]).makeAbsolute());
		}
		return projectEntries;
	}
/*
			IPath projectLocation= ResourcesPlugin.getWorkspace().getRoot().getLocation().append(name);
 * 
 */
	/**
	 * Returns the source class path entries to be added on new projects.
	 * The underlying resource may not exist.
	 * 
	 * @return returns the default class path entries
	 */
	public IPath getOutputLocation() {
		IPath outputLocationPath= new Path(getProjectName()).makeAbsolute();
			IPath binPath= new Path(PreferenceConstants.getPreferenceStore().getString(PreferenceConstants.SRCBIN_BINNAME));
			if (binPath.segmentCount() > 0) {
				outputLocationPath= outputLocationPath.append(binPath);
			}
		return outputLocationPath;
	}

    public LinkedList<RelogoClass> getGeneratedClasses() {
        if (fNameGroup.xref == null) {
        	fNameGroup.xref = fNameGroup.netlogoSimulation.getMethodPartition();
        }
    	return fNameGroup.xref.getAllClasses();
    }
    
    public NetlogoSimulation getNetlogoSimulation(){
    	return fNameGroup.netlogoSimulation;
    }
    
    public NLPlot[] getPlots() {
    	return fNameGroup.netlogoSimulation.getPlots();
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			fNameGroup.postSetFocus();
		}
	}

	private GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth= 0;
			layout.marginHeight= 0;
		}
		return layout;
	}
	
	public List<NLImage> getImportShapes(){
		return fShapesGroup.getCheckedShapesNames();
	}
}