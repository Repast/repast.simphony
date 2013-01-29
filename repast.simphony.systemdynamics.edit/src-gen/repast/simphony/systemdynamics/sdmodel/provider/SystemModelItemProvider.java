/**
 */
package repast.simphony.systemdynamics.sdmodel.provider;


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

import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.SystemModel;

/**
 * This is the item provider adapter for a {@link repast.simphony.systemdynamics.sdmodel.SystemModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SystemModelItemProvider
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
  public SystemModelItemProvider(AdapterFactory adapterFactory) {
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

      addStartTimePropertyDescriptor(object);
      addEndTimePropertyDescriptor(object);
      addTimeStepPropertyDescriptor(object);
      addUnitsPropertyDescriptor(object);
      addReportingIntervalPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Start Time feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addStartTimePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SystemModel_startTime_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SystemModel_startTime_feature", "_UI_SystemModel_type"),
         SDModelPackage.Literals.SYSTEM_MODEL__START_TIME,
         true,
         false,
         false,
         ItemPropertyDescriptor.REAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the End Time feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addEndTimePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SystemModel_endTime_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SystemModel_endTime_feature", "_UI_SystemModel_type"),
         SDModelPackage.Literals.SYSTEM_MODEL__END_TIME,
         true,
         false,
         false,
         ItemPropertyDescriptor.REAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Time Step feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTimeStepPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SystemModel_timeStep_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SystemModel_timeStep_feature", "_UI_SystemModel_type"),
         SDModelPackage.Literals.SYSTEM_MODEL__TIME_STEP,
         true,
         false,
         false,
         ItemPropertyDescriptor.REAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Units feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addUnitsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SystemModel_units_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SystemModel_units_feature", "_UI_SystemModel_type"),
         SDModelPackage.Literals.SYSTEM_MODEL__UNITS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Reporting Interval feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addReportingIntervalPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SystemModel_reportingInterval_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SystemModel_reportingInterval_feature", "_UI_SystemModel_type"),
         SDModelPackage.Literals.SYSTEM_MODEL__REPORTING_INTERVAL,
         true,
         false,
         false,
         ItemPropertyDescriptor.REAL_VALUE_IMAGE,
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
      childrenFeatures.add(SDModelPackage.Literals.SYSTEM_MODEL__LINKS);
      childrenFeatures.add(SDModelPackage.Literals.SYSTEM_MODEL__VARIABLES);
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
   * This returns SystemModel.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object) {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/SystemModel"));
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getText(Object object) {
    SystemModel systemModel = (SystemModel)object;
    return getString("_UI_SystemModel_type") + " " + systemModel.getStartTime();
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

    switch (notification.getFeatureID(SystemModel.class)) {
      case SDModelPackage.SYSTEM_MODEL__START_TIME:
      case SDModelPackage.SYSTEM_MODEL__END_TIME:
      case SDModelPackage.SYSTEM_MODEL__TIME_STEP:
      case SDModelPackage.SYSTEM_MODEL__UNITS:
      case SDModelPackage.SYSTEM_MODEL__REPORTING_INTERVAL:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SDModelPackage.SYSTEM_MODEL__LINKS:
      case SDModelPackage.SYSTEM_MODEL__VARIABLES:
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
        (SDModelPackage.Literals.SYSTEM_MODEL__LINKS,
         SDModelFactory.eINSTANCE.createInfluenceLink()));

    newChildDescriptors.add
      (createChildParameter
        (SDModelPackage.Literals.SYSTEM_MODEL__VARIABLES,
         SDModelFactory.eINSTANCE.createVariable()));

    newChildDescriptors.add
      (createChildParameter
        (SDModelPackage.Literals.SYSTEM_MODEL__VARIABLES,
         SDModelFactory.eINSTANCE.createStock()));

    newChildDescriptors.add
      (createChildParameter
        (SDModelPackage.Literals.SYSTEM_MODEL__VARIABLES,
         SDModelFactory.eINSTANCE.createCloud()));

    newChildDescriptors.add
      (createChildParameter
        (SDModelPackage.Literals.SYSTEM_MODEL__VARIABLES,
         SDModelFactory.eINSTANCE.createRate()));
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator() {
    return SystemdynamicsEditPlugin.INSTANCE;
  }

}
