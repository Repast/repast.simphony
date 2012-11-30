/**
 * Copyright (c) 2012 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 	itemis AG - initial API and implementation
 * 
 */
package repast.simphony.statecharts.sheets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * 
 * @author andreas muelder - Initial contribution and API
 * 
 */
public class StatePropertySection extends AbstractEditorPropertySection {

  private Control nameText;

  public void createControls(Composite parent) {
    createNameControl(parent);
  }

  @Override
  public void bindModel(EMFDataBindingContext context) {
    bindNameControl(context);
  }

  private void createNameControl(Composite parent) {
    getToolkit().createLabel(parent, "ID: ");
    nameText = getToolkit().createText(parent, "");
    GridDataFactory.fillDefaults().applyTo(nameText);
  }

  private void bindNameControl(EMFDataBindingContext context) {
    IEMFValueProperty property = EMFEditProperties.value(TransactionUtil.getEditingDomain(eObject),
        StatechartPackage.Literals.ABSTRACT_STATE__ID);
    ISWTObservableValue observe = WidgetProperties.text(
        new int[] { SWT.FocusOut, SWT.DefaultSelection }).observe(nameText);
    context.bindValue(observe, property.observe(eObject));
  }
}
