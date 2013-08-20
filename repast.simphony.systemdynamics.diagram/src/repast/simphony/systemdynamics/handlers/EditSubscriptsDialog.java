package repast.simphony.systemdynamics.handlers;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

public class EditSubscriptsDialog extends TitleAreaDialog {

  private static class CurrentItem {
    Subscript sub = null;
    int index = -1;

    void set(Subscript sub, int index) {
      this.sub = sub;
      this.index = index;
    }

    void reset() {
      sub = null;
      index = -1;
    }
  }

  private static String DEFAULT_NAME = "<Name>";

  private SystemModel model;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Text txtName;
  private Text txtElements;

  private java.util.List<Subscript> subscripts = new ArrayList<Subscript>();
  private CurrentItem currentItem = new CurrentItem();
  private List lstSubscripts;

  private Button btnAdd, btnDelete, btnOK;

  /**
   * Create the dialog.
   * 
   * @param parentShell
   */
  public EditSubscriptsDialog(Shell parentShell, SystemModel model) {
    super(parentShell);
    this.model = model;
  }

  /**
   * Create contents of the dialog.
   * 
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    this.setTitle("Subscripts");
    this.setMessage("Create new subscripts or edit existing ones.");
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

    btnAdd = formToolkit.createButton(composite_1, "Add", SWT.NONE);
    btnAdd.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        add();
      }
    });

    btnDelete = formToolkit.createButton(composite_1, "Delete", SWT.NONE);
    btnDelete.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        delete();
      }
    });

    lstSubscripts = new List(composite_1, SWT.BORDER);
    lstSubscripts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
    formToolkit.adapt(lstSubscripts, true, true);
    lstSubscripts.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        if (currentItem.sub != null) {
          boolean isValid = validateSubscript();
          updateButtons(isValid);
          if (isValid) {
            updateCurrent();
            subSelected();
          } else {
            lstSubscripts.setSelection(currentItem.index);
          }
        } else {
          subSelected();
        }
      }
    });

    Composite composite = formToolkit.createComposite(container, SWT.NONE);
    composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    composite.setLayout(new GridLayout(2, false));
    formToolkit.paintBordersFor(composite);

    Label label_1 = formToolkit.createLabel(composite, "Name:", SWT.NONE);
    label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
    new Label(composite, SWT.NONE);

    txtName = formToolkit.createText(composite, "", SWT.NONE);
    GridData gd_txtName = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
    gd_txtName.horizontalIndent = 6;
    txtName.setLayoutData(gd_txtName);
    txtName.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        updateButtons(validateSubscript());
        lstSubscripts.setItem(currentItem.index, txtName.getText().trim());
      }
    });

    Label label = formToolkit.createLabel(composite, "Elements:", SWT.NONE);
    label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
    new Label(composite, SWT.NONE);

    txtElements = formToolkit.createText(composite, "", SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL
        | SWT.MULTI);
    GridData gd_txtElements = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
    gd_txtElements.horizontalIndent = 6;
    txtElements.setLayoutData(gd_txtElements);
    txtElements.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        updateButtons(validateSubscript());
      }
    });
    
    return area;
  }

  private void updateButtons(boolean isValid) {
    btnAdd.setEnabled(isValid);
    btnOK.setEnabled(isValid);
    btnDelete.setEnabled(lstSubscripts.getSelectionIndex() != -1);
  }

  private boolean validateSubscript() {
    if (txtName.getText().trim().equals(DEFAULT_NAME) || txtName.getText().trim().length() == 0) {
      setErrorMessage("Invalid subscript name.");
      return false;
    }

    if (txtElements.getText().trim().length() == 0) {
      setErrorMessage("Subscript elements are missing.");
      return false;
    }

    setErrorMessage(null);
    return true;
  }

  private void subSelected() {
    int index = lstSubscripts.getSelectionIndex();
    if (index > -1) {
      Subscript sub = subscripts.get(index);
      txtName.setText(sub.getName());
      txtElements.setText(strFromList(sub.getElements()));
      txtName.setFocus();
      currentItem.set(sub, index);
      btnDelete.setEnabled(true);
      updateButtons(validateSubscript());
    } else {
      currentItem.reset();
      btnDelete.setEnabled(false);
      txtName.setText("");
      txtElements.setText("");
    }
  }

  private String strFromList(java.util.List<String> list) {
    StringBuilder buf = new StringBuilder();
    boolean first = true;
    for (String val : list) {
      if (!first)
        buf.append(", ");
      buf.append(val);
      first = false;
    }
    return buf.toString();

  }

  private void fillList() {
    subscripts.clear();
    java.util.List<String> names = new ArrayList<String>();
    for (Subscript subscript : model.getSubscripts()) {
      Subscript copy = SDModelFactory.eINSTANCE.createSubscript();
      copy.setName(subscript.getName());
      copy.getElements().addAll(subscript.getElements());
      subscripts.add(copy);
      names.add(subscript.getName());
    }
    lstSubscripts.setItems(names.toArray(new String[] {}));
    if (lstSubscripts.getItemCount() > 0) {
      lstSubscripts.setSelection(0);
    }
    subSelected();
  }

  private void updateCurrent() {
    currentItem.sub.setName(txtName.getText().trim());
    String[] elements = txtElements.getText().trim().split(",");
    currentItem.sub.getElements().clear();
    for (String element : elements) {
      currentItem.sub.getElements().add(element.trim());
    }
    lstSubscripts.setItem(currentItem.index, txtName.getText().trim());
  }

  private void add() {
    if (currentItem.sub != null) {
      updateCurrent();
    }

    btnAdd.setEnabled(false);
    Subscript sub = SDModelFactory.eINSTANCE.createSubscript();
    sub.setName("<Name>");
    String[] items = new String[lstSubscripts.getItemCount() + 1];
    System.arraycopy(lstSubscripts.getItems(), 0, items, 0, lstSubscripts.getItems().length);
    items[items.length - 1] = sub.getName();
    lstSubscripts.setItems(items);
    lstSubscripts.setSelection(items.length - 1);
    subscripts.add(sub);
    validateSubscript();
    updateButtons(false);
    subSelected();
  }

  private void delete() {
    int index = lstSubscripts.getSelectionIndex();
    if (index != -1) {
      if (currentItem != null && currentItem.index == index) {
        currentItem.reset();
      }
      subscripts.remove(index);
      lstSubscripts.remove(index);
      int itemCount = lstSubscripts.getItemCount();
      if (itemCount > 0) {
        if (index > itemCount - 1)
          lstSubscripts.setSelection(itemCount - 1);
        else
          lstSubscripts.setSelection(index);
      }
      subSelected();
    }
  }
  
  public java.util.List<Subscript> getSubscripts() {
    return subscripts;
  }
  
  

  @Override
  protected void okPressed() {
    if (currentItem.sub != null) updateCurrent();
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
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    fillList();
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(509, 343);
  }
}
