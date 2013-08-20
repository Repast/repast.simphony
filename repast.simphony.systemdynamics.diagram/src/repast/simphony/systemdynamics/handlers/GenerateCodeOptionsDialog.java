package repast.simphony.systemdynamics.handlers;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;


	public class GenerateCodeOptionsDialog extends TitleAreaDialog {
		  
		  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
		  
		  private String title;
		  
		  private boolean generateODE = false;
		  private boolean overwriteParameters = false;
		  private boolean overwriteObjects = false;
		  
		  private boolean parametersExist;
		  private boolean objectsExist;


		  private Button btnOK;
		  private Button btnCancel;

		  /**
		   * Create the dialog.
		   * 
		   * @param parentShell
		   */
		  public GenerateCodeOptionsDialog(Shell parentShell, boolean parametersExist, boolean objectsExist, String title) {
		    super(parentShell);
		    this.parametersExist = parametersExist;
		    this.objectsExist = objectsExist;
		    this.title = title;
		  }

		  /**
		   * Create contents of the dialog.
		   * 
		   * @param parent
		   */
		  @Override
		  protected Control createDialogArea(Composite parent) {
		    this.setTitle(title);

		    this.setHelpAvailable(false);

		    Composite area = (Composite) super.createDialogArea(parent);
		    Composite container = new Composite(area, SWT.NONE);
		    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		    container.setLayout(new GridLayout(2, false));

		    Composite composite_1 = formToolkit.createComposite(container, SWT.NONE);
		    composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		    composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		    composite_1.setLayout(new GridLayout(2, false));
		    formToolkit.paintBordersFor(composite_1);

		    

		    Composite composite = formToolkit.createComposite(container, SWT.NONE);
		    composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		    composite.setLayout(new GridLayout(2, false));
		    formToolkit.paintBordersFor(composite);


		    Label label = formToolkit.createLabel(composite, "Messages:", SWT.NONE);
		    label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		    new Label(composite, SWT.NONE);
		    
////		    ScrolledForm scrollForm = formToolkit.createScrolledForm(composite);
//
//		    txtElements = formToolkit.createText(composite, "", /* SWT.WRAP  | */ SWT.H_SCROLL | SWT.CANCEL | SWT.V_SCROLL
//		        | SWT.MULTI);
//		  
//		    GridData gd_txtElements = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
//		    gd_txtElements.horizontalIndent = 0; // 6
//		    txtElements.setLayoutData(gd_txtElements);
//		    
//		    txtElements.setText(messages);
		    return area;
		  }


		  @Override
		  protected void okPressed() {
		    super.okPressed();
		  }

		  /**
		   * Create contents of the button bar.
		   * 
		   * @param parent
		   */
		  @Override
		  protected void createButtonsForButtonBar(Composite parent) {
		    btnOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		    btnOK.setEnabled(true);
		    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		  }

		  /**
		   * Return the initial size of the dialog.
		   */
		  @Override
		  protected Point getInitialSize() {
		    return new Point(750, 500); // 509, 343
		  }

		public boolean isGenerateODE() {
			return generateODE;
		}

		public void setGenerateODE(boolean generateODE) {
			this.generateODE = generateODE;
		}

		public boolean isOverwriteParameters() {
			return overwriteParameters;
		}

		public void setOverwriteParameters(boolean overwriteParameters) {
			this.overwriteParameters = overwriteParameters;
		}

		public boolean isOverwriteObjects() {
			return overwriteObjects;
		}

		public void setOverwriteObjects(boolean overwriteObjects) {
			this.overwriteObjects = overwriteObjects;
		}
		}