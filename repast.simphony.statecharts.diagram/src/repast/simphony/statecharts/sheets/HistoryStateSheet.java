package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.statecharts.scmodel.StatechartPackage;

public class HistoryStateSheet extends Composite {

  private Text idTxt;
  private Text onEnterTxt;
  private Button btnShallow;
  private LanguageButtonsGroup buttonGroup;

  public HistoryStateSheet(FormToolkit toolkit, Composite parent) {
    super(parent, SWT.NONE);
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    GridLayout gridLayout = new GridLayout(3, false);
    setLayout(gridLayout);

    Label lblId = new Label(this, SWT.NONE);
    toolkit.adapt(lblId, true, true);
    lblId.setText("ID:");

    idTxt = new Text(this, SWT.BORDER);
    GridData gd_idTxt = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_idTxt.widthHint = 200;
    idTxt.setLayoutData(gd_idTxt);
    toolkit.adapt(idTxt, true, true);

    btnShallow = new Button(this, SWT.CHECK);
    GridData gd_btnShallow = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnShallow.horizontalIndent = 9;
    btnShallow.setLayoutData(gd_btnShallow);
    btnShallow.setText("Shallow");
    toolkit.adapt(btnShallow, true, true);

    Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
    label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
    toolkit.adapt(label, true, true);

    Composite composite = new Composite(this, SWT.H_SCROLL);
    GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
    gd_composite.heightHint = 183;
    composite.setLayoutData(gd_composite);
    GridLayout gl_composite = new GridLayout(1, false);
    gl_composite.horizontalSpacing = 12;
    gl_composite.marginHeight = 0;
    composite.setLayout(gl_composite);
    toolkit.adapt(composite);
    toolkit.paintBordersFor(composite);

    Composite composite_1 = new Composite(composite, SWT.NONE);
    GridLayout gl_composite_1 = new GridLayout(5, false);
    gl_composite_1.verticalSpacing = 0;
    gl_composite_1.horizontalSpacing = 0;
    gl_composite_1.marginHeight = 0;
    gl_composite_1.marginWidth = 0;
    composite_1.setLayout(gl_composite_1);
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_composite_1.widthHint = 353;
    composite_1.setLayoutData(gd_composite_1);
    toolkit.adapt(composite_1);
    toolkit.paintBordersFor(composite_1);

    Label lblLanguage = new Label(composite_1, SWT.NONE);
    lblLanguage.setBounds(0, 0, 59, 14);
    toolkit.adapt(lblLanguage, true, true);
    lblLanguage.setText("Language:");
    new Label(composite_1, SWT.NONE);

    Button btnJava = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnJava, true, true);
    btnJava.setText("Java");

    Button btnRelogo = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnRelogo, true, true);
    btnRelogo.setText("ReLogo");

    Button btnGroovy = new Button(composite_1, SWT.RADIO);
    toolkit.adapt(btnGroovy, true, true);
    btnGroovy.setText("Groovy");

    buttonGroup = new LanguageButtonsGroup(btnJava, btnRelogo, btnGroovy);

    Label lblOnEnter = new Label(composite, SWT.NONE);
    toolkit.adapt(lblOnEnter, true, true);
    lblOnEnter.setText("On Enter:");

    onEnterTxt = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    onEnterTxt.setText("");
    GridData data = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
    data.heightHint = 120;
    onEnterTxt.setLayoutData(data);
    toolkit.adapt(onEnterTxt, true, true);

    onEnterTxt.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
          e.detail = SWT.TRAVERSE_NONE;
        }
      }
    });
  }

  public void bindModel(EMFDataBindingContext context, EObject eObject) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(
        new int[] { SWT.Modify }).observeDelayed(400, idTxt);
    context.bindValue(observe, property.observe(eObject));

    context.bindValue(
        WidgetProperties.selection().observe(btnShallow),
        EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
            StatechartPackage.Literals.HISTORY__SHALLOW).observe(eObject));

    context
        .bindValue(
            WidgetProperties.text(new int[] { SWT.Modify}).observeDelayed(400,
                onEnterTxt),
            EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
                StatechartPackage.Literals.ABSTRACT_STATE__ON_ENTER).observe(eObject));

    buttonGroup.bindModel(context, eObject, StatechartPackage.Literals.ABSTRACT_STATE__LANGUAGE);
  }
}
