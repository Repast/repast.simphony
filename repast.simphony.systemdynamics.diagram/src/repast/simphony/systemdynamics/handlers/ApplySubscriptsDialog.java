package repast.simphony.systemdynamics.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import repast.simphony.systemdynamics.sdmodel.Subscript;

public class ApplySubscriptsDialog extends TitleAreaDialog {
  
  private java.util.List<String> subscripts = new ArrayList<String>();
  
  private Button btnAdd;
  private Button btnAddAll;
  private Button btnRemove;
  private Button btnRemoveAll, btnOK;
  private List lstTo;
  private List lstFrom;

  /**
   * Create the dialog.
   * @param parentShell
   */
  public ApplySubscriptsDialog(Shell parentShell) {
    super(parentShell);
  }
  
  /**
   * Initialize this list with the possible subscripts.
   * 
   * @param subscripts
   */
  public void init(java.util.List<Subscript> subscripts) {
    this.subscripts.clear();
    for (Subscript sb : subscripts) {
      this.subscripts.add(sb.getName());
    }
    Collections.sort(this.subscripts);
  }

  /**
   * Gets the choosen subscripts.
   * 
   * @return the choosen subscripts.
   */
  public java.util.List<String> getSubscripts() {
    return subscripts;
  }

  /**
   * Create contents of the dialog.
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    this.setTitle("Apply Subscripts");
    this.setMessage("Apply selected subscripts to the currently selected variables.");
    this.setHelpAvailable(false);
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayout(new GridLayout(3, false));
    container.setLayoutData(new GridData(GridData.FILL_BOTH));
    
    lstFrom = new List(container, SWT.BORDER);
    lstFrom.setItems(this.subscripts.toArray(new String[]{}));
    subscripts.clear();
    lstFrom.setSelection(0);
    lstFrom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    
    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));
    
    btnAdd = new Button(composite, SWT.NONE);
    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    btnAdd.setText("Add ->");
    
    btnAddAll = new Button(composite, SWT.NONE);
    btnAddAll.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    btnAddAll.setText("Add All ->");
    
    btnRemove = new Button(composite, SWT.NONE);
    btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    btnRemove.setText("<- Remove");
    btnRemove.setEnabled(false);
    
    btnRemoveAll = new Button(composite, SWT.NONE);
    btnRemoveAll.setText("<- Remove All");
    btnRemoveAll.setEnabled(false);
    
    lstTo = new List(container, SWT.BORDER);
    lstTo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    
    addListeners();

    return area;
  }
  
  private void fillCollection(java.util.Collection<String> list, String[] array) {
    for (String s : array) {
      list.add(s);
    }
  }
  
  private void addSelections(List to, List from, String[] fromSelection) {
    Set<String> selectedSet = new HashSet<String>();
    fillCollection(selectedSet, fromSelection);
    String[] toItems = to.getItems();
    java.util.List<String> newList = new ArrayList<String>();
    fillCollection(newList, fromSelection);
    fillCollection(newList, toItems);
    Collections.sort(newList);
    to.setItems(newList.toArray(new String[]{}));
    to.setSelection(fromSelection);
    
    newList.clear();
    for (String item: from.getItems()) {
      if (!selectedSet.contains(item)) {
        newList.add(item);
      }
    }
    
    from.setItems(newList.toArray(new String[]{}));
    btnAdd.setEnabled(lstFrom.getItems().length > 0 && lstFrom.getSelectionCount() > 0);
    btnAddAll.setEnabled(lstFrom.getItems().length > 0);
    btnRemove.setEnabled(lstTo.getItems().length > 0 && lstFrom.getSelectionCount() > 0);
    btnRemoveAll.setEnabled(lstTo.getItems().length > 0);
    btnOK.setEnabled(lstTo.getItems().length > 0);
    
    subscripts.clear();
    subscripts.addAll(Arrays.asList(lstTo.getItems()));
  }
  
  private void addListeners() {
    btnAdd.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        addSelections(lstTo, lstFrom, lstFrom.getSelection());
      }
    });
    
    btnAddAll.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        addSelections(lstTo, lstFrom, lstFrom.getItems());
      }
    });
    
    btnRemove.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        addSelections(lstFrom, lstTo, lstTo.getSelection());
      }
    });
    
    btnRemoveAll.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        addSelections(lstFrom, lstTo, lstTo.getItems());
      }
    });
    
    lstFrom.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        btnAdd.setEnabled(lstFrom.getSelectionCount() > 0);
      }
    });
    
    lstTo.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        btnRemove.setEnabled(lstTo.getSelectionCount() > 0);
      }
    });
  }

  /**
   * Create contents of the button bar.
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    btnOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    btnOK.setEnabled(false);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

}
