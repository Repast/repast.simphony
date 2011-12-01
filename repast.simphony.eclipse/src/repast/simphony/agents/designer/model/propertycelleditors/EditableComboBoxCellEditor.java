package repast.simphony.agents.designer.model.propertycelleditors;

import java.util.ArrayList;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.model.propertydescriptors.EditableComboBoxPropertyDescriptor;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;

public class EditableComboBoxCellEditor extends CellEditor {

	protected CCombo comboBoxControl;

	protected String[] itemsList;

	protected String selection;

	protected String resultText;

	protected String previousText = "";

	protected boolean dialogShown = false;

	protected boolean multipleSelect = false;

	public ArrayList<NewCodeWizardEntry> entryList = null;

	public EditableComboBoxCellEditor(Composite newParent,
			String[] newItemsList, int newStyle, boolean multipleSelect,
			ArrayList<NewCodeWizardEntry> entryList) {
		super(newParent, newStyle);
		this.setItems(newItemsList);
		this.selection = this.comboBoxControl.getText();
		this.previousText = this.selection;
		this.multipleSelect = multipleSelect;
		this.entryList = entryList;
	}

	public static Object threadLock = new Object();

	public void applyEditorValueAndDeactivate() {

		synchronized (threadLock) {

			this.selection = this.comboBoxControl.getText();
			if ((this.selection != null) && (!this.dialogShown)) {
				if (this.selection
						.equals(EditableComboBoxPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR)) {
					dialogShown = true;
					this.selection = this.openTypeSelectionDialogBox();
				} else if (this.selection
						.equals(EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG)) {
					dialogShown = true;
					this.selection = Util.convertFromLineFeeds(this
							.openTextEditingDialogBox(Util
									.convertToLineFeeds(this.previousText)));
				}
			} else {
				this.dialogShown = false;
			}

		}

		this.checkSelection();
		this.markDirty();
		this.setValueValid(true);
		this.fireApplyEditorValue();
		this.deactivate();
		AgentEditor.setCurrentCellEditor(null);
	}

	protected Control createControl(Composite parent) {

		this.comboBoxControl = new CCombo(parent, getStyle());
		this.comboBoxControl.setVisibleItemCount(Math.min(15,
				this.comboBoxControl.getItemCount()));
		this.comboBoxControl.setFont(parent.getFont());
		this.comboBoxControl.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent newKeyEvent) {
				AgentEditor.setCurrentCellEditor(EditableComboBoxCellEditor.this);
				if (newKeyEvent.character == '\u001b') {
					EditableComboBoxCellEditor.this.fireCancelEditor();
				} else if (newKeyEvent.character == '\n') {
					EditableComboBoxCellEditor.this
							.applyEditorValueAndDeactivate();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\f') {
					EditableComboBoxCellEditor.this
							.applyEditorValueAndDeactivate();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\r') {
					EditableComboBoxCellEditor.this
							.applyEditorValueAndDeactivate();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\t') {
					EditableComboBoxCellEditor.this
							.applyEditorValueAndDeactivate();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.keyCode == SWT.CR) {
					EditableComboBoxCellEditor.this
							.applyEditorValueAndDeactivate();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.keyCode == SWT.DEL) {
					comboBoxControl.clearSelection();
					newKeyEvent.doit = false;
				} else {
					EditableComboBoxCellEditor.super
							.keyReleaseOccured(newKeyEvent);
					newKeyEvent.doit = true;
				}

			}

			public void keyReleased(KeyEvent newKeyEvent) {
				AgentEditor.setCurrentCellEditor(EditableComboBoxCellEditor.this);
				if (newKeyEvent.character == '\u0001') {
					String text = comboBoxControl.getText();
					if (text != null) {
						comboBoxControl
								.setSelection(new Point(0, text.length()));
					}
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\u007F') {
					String text = comboBoxControl.getText();
					if (text != null) {
						Point point = comboBoxControl.getSelection();
						int lowerBound = Math.max(0, point.x);
						int upperBound = Math.min(point.y + 1, text.length());
						text = text.substring(0, lowerBound)
								+ text.substring(upperBound, text.length());
						comboBoxControl.setText(text);
						comboBoxControl.setSelection(point);
					}
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\u0003') {
					comboBoxControl.copy();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\u0016') {
					comboBoxControl.paste();
					newKeyEvent.doit = false;
				} else if (newKeyEvent.character == '\u0018') {
					comboBoxControl.cut();
					newKeyEvent.doit = false;
				}
			}

		});

		this.comboBoxControl.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				EditableComboBoxCellEditor.this.selection = EditableComboBoxCellEditor.this.comboBoxControl
						.getText();
				EditableComboBoxCellEditor.this.previousText = EditableComboBoxCellEditor.this.selection;
				AgentEditor.setCurrentCellEditor(EditableComboBoxCellEditor.this);
			}

			public void focusLost(FocusEvent e) {
				EditableComboBoxCellEditor.this.applyEditorValueAndDeactivate();
			}
		});

		this.comboBoxControl.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
			}

			public void widgetSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

		});

		return this.comboBoxControl;
	}

	protected Object doGetValue() {
		return this.selection;
	}

	protected void doSetFocus() {
		this.comboBoxControl.setFocus();
		AgentEditor.setCurrentCellEditor(EditableComboBoxCellEditor.this);
	}

	public void doSetValue(Object value) {
		if (value instanceof String) {
			comboBoxControl.setText((String) value);
		}
	}

	public String[] getItems() {
		return this.itemsList;
	}

	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		if ((comboBoxControl == null) || comboBoxControl.isDisposed())
			layoutData.minimumWidth = 65;
		else {
			GC gc = new GC(comboBoxControl);
			layoutData.minimumWidth = (gc.getFontMetrics()
					.getAverageCharWidth() * 11) + 11;
			gc.dispose();
		}
		return layoutData;
	}

	private void initializeItemsList() {
		if ((this.itemsList != null) && (this.comboBoxControl != null)) {
			this.comboBoxControl.removeAll();
			for (int counter = 0; counter < itemsList.length; counter++) {
				this.comboBoxControl.add(itemsList[counter], counter);
			}
			setValueValid(true);
			if (this.itemsList.length > 0) {
				selection = this.comboBoxControl.getItem(0);
				this.checkSelection();
			} else {
				selection = "";
			}
			this.comboBoxControl.setVisibleItemCount(Math.min(15,
					this.comboBoxControl.getItemCount()));
		}
	}

	protected String openTypeSelectionDialogBox() {

		SelectionDialog dialog;
		Control cellEditorWindow = this.getControl();
		try {
			// IJavaSearchScope scope = SearchEngine
			// .createHierarchyScope(this.baseType);
			IJavaElement[] javaElements = new IJavaElement[1];
			javaElements[0] = AgentBuilderPlugin.getActiveJavaProject();
			IJavaSearchScope scope = SearchEngine
					.createJavaSearchScope(javaElements);
			dialog = JavaUI.createTypeDialog(cellEditorWindow.getShell(),
					new ProgressMonitorDialog(cellEditorWindow.getShell()),
					scope, IJavaElementSearchConstants.CONSIDER_ALL_TYPES,
					false, "");
			dialog
					.setTitle("Please Select a Type (Press the \"Cancel\" Button to Select Nothing)");
			dialog
					.setMessage("Please Select a Type (Press the \"Cancel\" Button to Select Nothing)");
		} catch (JavaModelException e) {
			e.printStackTrace();
			return "";
		}
		if (dialog.open() == IDialogConstants.CANCEL_ID)
			return "";

		Object[] types = dialog.getResult();
		if (types == null || types.length == 0) {
			return "";
		}

		IType selection = (IType) types[0];
		return selection.getFullyQualifiedName();

	}

	protected String openTextEditingDialogBox(String initialText) {

		Control cellEditorWindow = this.getControl();

		final Shell shell = new Shell(cellEditorWindow.getShell(), SWT.BORDER
				| SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		shell.setSize(500, 500);
		shell.setText("Please Enter Text");

		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		gridLayout.numColumns = 2;

		this.resultText = initialText;

		final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		text.setLayoutData(gridData);
		text
				.setText("                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR
						+ "                                                                          "
						+ Util.SYSTEM_LINE_SEPARATOR);

		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText("OK");
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				resultText = text.getText();
				shell.dispose();
			}
		});
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.END;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		okButton.setLayoutData(gridData);

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.BEGINNING;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		cancelButton.setLayoutData(gridData);

		shell.pack();
		shell.open();

		text.setText(initialText);

		Display display = cellEditorWindow.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return this.resultText;

	}

	public void setItems(String[] newItemsList) {
		this.itemsList = newItemsList;
		this.initializeItemsList();
	}

	public void checkSelection() {

		if ((this.selection != null)
				&& ((this.selection
						.equals(EditableComboBoxPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR)))
				|| (this.selection
						.equals(EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG))
				|| (this.selection
						.equals(EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG))) {
			this.selection = this.previousText;
			if ((this.selection != null)
					&& ((this.selection
							.equals(EditableComboBoxPropertyDescriptor.CHOOSE_USING_TYPE_SELECTOR)))
					|| (this.selection
							.equals(EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG))
					|| (this.selection
							.equals(EditableComboBoxPropertyDescriptor.EDIT_USING_TEXT_BOX_DIALOG))) {
				this.selection = "";
				this.previousText = "";
			}
			if (this.selection != null) {
				this.comboBoxControl.setText(this.selection);
			}
		} else {
			if (this.multipleSelect) {
				if ((this.selection != null) && (!this.selection.equals(""))
						&& (this.previousText != null)
						&& (!this.previousText.equals(""))
						&& (!this.previousText.equals(this.selection))
						&& (!this.previousText.endsWith(", " + this.selection))) {
					this.selection = this.previousText + ", " + this.selection;
				}
			}
			this.previousText = this.selection;
		}

	}

}