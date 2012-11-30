/**
 */
package repast.simphony.statecharts.scmodel.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartFactory;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * This is the item provider adapter for a {@link repast.simphony.statecharts.scmodel.StateMachine} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class StateMachineItemProvider
  extends ItemProviderAdapter
  implements
    IEditingDomainItemProvider,
    IStructuredItemContentProvider,
    ITreeItemContentProvider,
    IItemLabelProvider,
    IItemPropertySource {
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StateMachineItemProvider(AdapterFactory adapterFactory) {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
    if (itemPropertyDescriptors == null) {
      super.getPropertyDescriptors(object);

      addAgentTypePropertyDescriptor(object);
      addPackagePropertyDescriptor(object);
      addClassNamePropertyDescriptor(object);
      addLanguagePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Agent Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAgentTypePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_StateMachine_agentType_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_StateMachine_agentType_feature", "_UI_StateMachine_type"),
         StatechartPackage.Literals.STATE_MACHINE__AGENT_TYPE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Package feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPackagePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_StateMachine_package_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_StateMachine_package_feature", "_UI_StateMachine_type"),
         StatechartPackage.Literals.STATE_MACHINE__PACKAGE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Class Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addClassNamePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_StateMachine_className_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_StateMachine_className_feature", "_UI_StateMachine_type"),
         StatechartPackage.Literals.STATE_MACHINE__CLASS_NAME,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Language feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLanguagePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_StateMachine_language_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_StateMachine_language_feature", "_UI_StateMachine_type"),
         StatechartPackage.Literals.STATE_MACHINE__LANGUAGE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
    if (childrenFeatures == null) {
      super.getChildrenFeatures(object);
      childrenFeatures.add(StatechartPackage.Literals.STATE_MACHINE__STATES);
      childrenFeatures.add(StatechartPackage.Literals.STATE_MACHINE__TRANSITIONS);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child) {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * This returns StateMachine.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object) {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/StateMachine"));
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getText(Object object) {
    String label = ((StateMachine)object).getClassName();
    return label == null || label.length() == 0 ?
      getString("_UI_StateMachine_type") :
      getString("_UI_StateMachine_type") + " " + label;
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification) {
    updateChildren(notification);

    switch (notification.getFeatureID(StateMachine.class)) {
      case StatechartPackage.STATE_MACHINE__AGENT_TYPE:
      case StatechartPackage.STATE_MACHINE__PACKAGE:
      case StatechartPackage.STATE_MACHINE__CLASS_NAME:
      case StatechartPackage.STATE_MACHINE__LANGUAGE:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case StatechartPackage.STATE_MACHINE__STATES:
      case StatechartPackage.STATE_MACHINE__TRANSITIONS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add
      (createChildParameter
        (StatechartPackage.Literals.STATE_MACHINE__STATES,
         StatechartFactory.eINSTANCE.createState()));

    newChildDescriptors.add
      (createChildParameter
        (StatechartPackage.Literals.STATE_MACHINE__STATES,
         StatechartFactory.eINSTANCE.createFinalState()));

    newChildDescriptors.add
      (createChildParameter
        (StatechartPackage.Literals.STATE_MACHINE__STATES,
         StatechartFactory.eINSTANCE.createCompositeState()));

    newChildDescriptors.add
      (createChildParameter
        (StatechartPackage.Literals.STATE_MACHINE__STATES,
         StatechartFactory.eINSTANCE.createPseudoState()));

    newChildDescriptors.add
      (createChildParameter
        (StatechartPackage.Literals.STATE_MACHINE__STATES,
         StatechartFactory.eINSTANCE.createHistory()));

    newChildDescriptors.add
      (createChildParameter
        (StatechartPackage.Literals.STATE_MACHINE__TRANSITIONS,
         StatechartFactory.eINSTANCE.createTransition()));
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator() {
    return StatechartEditPlugin.INSTANCE;
  }

}
