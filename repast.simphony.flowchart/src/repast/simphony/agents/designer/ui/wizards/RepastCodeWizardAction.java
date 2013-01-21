package repast.simphony.agents.designer.ui.wizards;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxCellEditor;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
import repast.simphony.agents.designer.ui.editors.DecisionWizardGenerator;
import repast.simphony.agents.designer.ui.editors.LoopWizardGenerator;
import repast.simphony.agents.designer.ui.editors.TaskWizardGenerator;

public class RepastCodeWizardAction extends Action implements IWorkbenchWindowActionDelegate {

  public IWorkbenchWindow window = null;

  public static ArrayList<NewCodeWizardEntry> defaultEntryList = null;

  @Override
  public void run(IAction action) {

    ArrayList<NewCodeWizardEntry> entryList = null;
    EditableComboBoxCellEditor cellEditor = AgentEditor.getCurrentCellEditor();
    if (cellEditor == null) {

      IWorkbenchPage page = window.getActivePage();
      IEditorPart part = page.getActiveEditor();
      if (part instanceof AbstractTextEditor) {

        checkDefaultList();
        entryList = defaultEntryList;
        String results = this.runWizardDialog(entryList);
        if (results != null) {
          ITextEditor editor = (ITextEditor) part;
          IDocumentProvider dp = editor.getDocumentProvider();
          IDocument doc = dp.getDocument(editor.getEditorInput());
          try {
            ISelection selection = page.getActiveEditor().getEditorSite().getSelectionProvider()
                .getSelection();
            if (selection instanceof ITextSelection) {
              int start = ((ITextSelection) selection).getOffset();
              doc.replace(start, 0, results);
            }
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        }
      }

    } else {

      entryList = cellEditor.entryList;
      if (entryList != null) {
        String results = this.runWizardDialog(entryList);
        if (results != null) {
          cellEditor.doSetValue(results);
          cellEditor.applyEditorValueAndDeactivate();
        }
      }

    }

  }

  public boolean isEnabled() {

    EditableComboBoxCellEditor cellEditor = AgentEditor.getCurrentCellEditor();
    if (cellEditor == null) {

      IWorkbenchPage page = window.getActivePage();
      IEditorPart part = page.getActiveEditor();
      if (part instanceof AbstractTextEditor) {
        return true;
      } else
        return false;

    } else {

      ArrayList<NewCodeWizardEntry> entryList = cellEditor.entryList;
      if (entryList != null) {
        return true;
      } else {
        return false;
      }

    }

  }

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    // action.setEnabled(this.isEnabled());
  }

  @Override
  public void dispose() {
  }

  @Override
  public void init(IWorkbenchWindow newWindow) {
    this.window = newWindow;
  }

  public static void checkDefaultList() {

    if (defaultEntryList == null) {

      ArrayList<NewCodeWizardEntry> taskList = TaskWizardGenerator.createTaskWizard();
      for (NewCodeWizardEntry entry : taskList) {
        if (entry instanceof NewCodeWizardFormEntry) {
          NewCodeWizardFormEntry formEntry = (NewCodeWizardFormEntry) entry;
          formEntry.template = formEntry.template + ";\n";
        }
      }
      ArrayList<NewCodeWizardEntry> decisionList = DecisionWizardGenerator.createDecisionWizard();
      for (NewCodeWizardEntry entry : decisionList) {
        if (entry instanceof NewCodeWizardFormEntry) {
          NewCodeWizardFormEntry formEntry = (NewCodeWizardFormEntry) entry;
          formEntry.template = "if " + formEntry.template + " {\n} else {\n}\n";
        }
      }
      ArrayList<NewCodeWizardEntry> loopList = LoopWizardGenerator.createLoopWizard();
      for (NewCodeWizardEntry entry : loopList) {
        if (entry instanceof NewCodeWizardFormEntry) {
          NewCodeWizardFormEntry formEntry = (NewCodeWizardFormEntry) entry;
          if (useWhileStatement(formEntry.template)) {
            formEntry.template = "while (" + formEntry.template + ") {\n}\n";
          } else {
            formEntry.template = "for (Object " + formEntry.template + ") {\n}\n";
            if (!formEntry.template.contains("..")) {
              formEntry.template = formEntry.template.replaceFirst(" in ", " : ");
            }
          }
        }
      }

      defaultEntryList = new ArrayList<NewCodeWizardEntry>();
      defaultEntryList.add(new NewCodeWizardRadioButtonEntry("Insert Repast Code",
          "Please Select the Kind of Code to Insert", new String[] { "Task Step", "Decision Step",
              "Loop Step" }, null));
      defaultEntryList.addAll(taskList);
      defaultEntryList.addAll(decisionList);
      defaultEntryList.addAll(loopList);

    }

  }

  public static boolean useWhileStatement(String booleanStatement) {

    boolean useWhileStatement = true;

    int inIndex = booleanStatement.indexOf(" in ");
    if (inIndex >= 0) {
      if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
          && (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
        useWhileStatement = false;
      }
      if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
          && (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
        useWhileStatement = false;
      }
    }
    inIndex = booleanStatement.indexOf(";");
    if (inIndex >= 0) {
      if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
          && (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
        useWhileStatement = false;
      }
      if ((booleanStatement.substring(0, inIndex).indexOf("\"") < 0)
          && (booleanStatement.substring(0, inIndex).indexOf("{") < 0)) {
        useWhileStatement = false;
      }
    }

    return useWhileStatement;

  }

  public String runWizardDialog(ArrayList<NewCodeWizardEntry> entryList) {

    NewCodeWizard newCodeWizard = new NewCodeWizard(entryList);
    WizardDialog wizardDialog = new WizardDialog(window.getShell(), newCodeWizard);
    try {
      wizardDialog.create();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error Creating the Wizard Titled \"" + entryList.get(0).title + "\"");
    }
    if (wizardDialog.open() == WizardDialog.OK) {
      return newCodeWizard.getResults();
    } else {
      return null;
    }
  }

}
