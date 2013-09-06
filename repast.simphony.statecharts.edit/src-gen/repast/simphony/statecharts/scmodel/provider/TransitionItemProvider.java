/**
 */
package repast.simphony.statecharts.scmodel.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

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

import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * This is the item provider adapter for a {@link repast.simphony.statecharts.scmodel.Transition} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TransitionItemProvider
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
  public TransitionItemProvider(AdapterFactory adapterFactory) {
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

      addFromPropertyDescriptor(object);
      addToPropertyDescriptor(object);
      addPriorityPropertyDescriptor(object);
      addOnTransitionPropertyDescriptor(object);
      addOnTransitionImportsPropertyDescriptor(object);
      addOutOfBranchPropertyDescriptor(object);
      addDefaultTransitionPropertyDescriptor(object);
      addTriggerTypePropertyDescriptor(object);
      addTriggerTimePropertyDescriptor(object);
      addTriggerConditionCodePropertyDescriptor(object);
      addTriggerConditionImportsPropertyDescriptor(object);
      addTriggerCodeLanguagePropertyDescriptor(object);
      addMessageCheckerTypePropertyDescriptor(object);
      addMessageCheckerClassPropertyDescriptor(object);
      addTriggerProbCodePropertyDescriptor(object);
      addTriggerProbeCodeImportsPropertyDescriptor(object);
      addMessageCheckerCodePropertyDescriptor(object);
      addMessageCheckerCodeImportsPropertyDescriptor(object);
      addMessageCheckerConditionLanguagePropertyDescriptor(object);
      addIdPropertyDescriptor(object);
      addGuardPropertyDescriptor(object);
      addGuardImportsPropertyDescriptor(object);
      addTriggerTimedCodePropertyDescriptor(object);
      addTriggerTimedCodeImportsPropertyDescriptor(object);
      addTriggerExpRateCodePropertyDescriptor(object);
      addTriggerExpRateCodeImportsPropertyDescriptor(object);
      addUuidPropertyDescriptor(object);
      addSelfTransitionPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the From feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addFromPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_from_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_from_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__FROM,
         true,
         false,
         true,
         null,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the To feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addToPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_to_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_to_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TO,
         true,
         false,
         true,
         null,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Priority feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPriorityPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_priority_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_priority_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__PRIORITY,
         true,
         false,
         false,
         ItemPropertyDescriptor.REAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the On Transition feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addOnTransitionPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_onTransition_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_onTransition_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__ON_TRANSITION,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the On Transition Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addOnTransitionImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_onTransitionImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_onTransitionImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__ON_TRANSITION_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Out Of Branch feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addOutOfBranchPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_outOfBranch_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_outOfBranch_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__OUT_OF_BRANCH,
         true,
         false,
         false,
         ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Default Transition feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDefaultTransitionPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_defaultTransition_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_defaultTransition_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__DEFAULT_TRANSITION,
         true,
         false,
         false,
         ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerTypePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerType_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerType_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_TYPE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Time feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerTimePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerTime_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerTime_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_TIME,
         true,
         false,
         false,
         ItemPropertyDescriptor.REAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Condition Code feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerConditionCodePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerConditionCode_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerConditionCode_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_CONDITION_CODE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Condition Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerConditionImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerConditionImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerConditionImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_CONDITION_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Code Language feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerCodeLanguagePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerCodeLanguage_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerCodeLanguage_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_CODE_LANGUAGE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Message Checker Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMessageCheckerTypePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_messageCheckerType_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_messageCheckerType_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_TYPE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Message Checker Class feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMessageCheckerClassPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_messageCheckerClass_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_messageCheckerClass_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CLASS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Prob Code feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerProbCodePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerProbCode_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerProbCode_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_PROB_CODE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Probe Code Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerProbeCodeImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerProbeCodeImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerProbeCodeImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_PROBE_CODE_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Message Checker Code feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMessageCheckerCodePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_messageCheckerCode_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_messageCheckerCode_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CODE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Message Checker Code Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMessageCheckerCodeImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_messageCheckerCodeImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_messageCheckerCodeImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CODE_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Message Checker Condition Language feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMessageCheckerConditionLanguagePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_messageCheckerConditionLanguage_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_messageCheckerConditionLanguage_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIdPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_id_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_id_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__ID,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Guard feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGuardPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_guard_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_guard_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__GUARD,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Guard Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGuardImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_guardImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_guardImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__GUARD_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Timed Code feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerTimedCodePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerTimedCode_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerTimedCode_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_TIMED_CODE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Timed Code Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerTimedCodeImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerTimedCodeImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerTimedCodeImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_TIMED_CODE_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Exp Rate Code feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerExpRateCodePropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerExpRateCode_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerExpRateCode_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_EXP_RATE_CODE,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Trigger Exp Rate Code Imports feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTriggerExpRateCodeImportsPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_triggerExpRateCodeImports_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_triggerExpRateCodeImports_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__TRIGGER_EXP_RATE_CODE_IMPORTS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Uuid feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addUuidPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_uuid_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_uuid_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__UUID,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Self Transition feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSelfTransitionPropertyDescriptor(Object object) {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_Transition_selfTransition_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_Transition_selfTransition_feature", "_UI_Transition_type"),
         StatechartPackage.Literals.TRANSITION__SELF_TRANSITION,
         true,
         false,
         false,
         ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This returns Transition.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object) {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Transition"));
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getText(Object object) {
    String label = ((Transition)object).getId();
    return label == null || label.length() == 0 ?
      getString("_UI_Transition_type") :
      getString("_UI_Transition_type") + " " + label;
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

    switch (notification.getFeatureID(Transition.class)) {
      case StatechartPackage.TRANSITION__PRIORITY:
      case StatechartPackage.TRANSITION__ON_TRANSITION:
      case StatechartPackage.TRANSITION__ON_TRANSITION_IMPORTS:
      case StatechartPackage.TRANSITION__OUT_OF_BRANCH:
      case StatechartPackage.TRANSITION__DEFAULT_TRANSITION:
      case StatechartPackage.TRANSITION__TRIGGER_TYPE:
      case StatechartPackage.TRANSITION__TRIGGER_TIME:
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION_CODE:
      case StatechartPackage.TRANSITION__TRIGGER_CONDITION_IMPORTS:
      case StatechartPackage.TRANSITION__TRIGGER_CODE_LANGUAGE:
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_TYPE:
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CLASS:
      case StatechartPackage.TRANSITION__TRIGGER_PROB_CODE:
      case StatechartPackage.TRANSITION__TRIGGER_PROBE_CODE_IMPORTS:
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CODE:
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CODE_IMPORTS:
      case StatechartPackage.TRANSITION__MESSAGE_CHECKER_CONDITION_LANGUAGE:
      case StatechartPackage.TRANSITION__ID:
      case StatechartPackage.TRANSITION__GUARD:
      case StatechartPackage.TRANSITION__GUARD_IMPORTS:
      case StatechartPackage.TRANSITION__TRIGGER_TIMED_CODE:
      case StatechartPackage.TRANSITION__TRIGGER_TIMED_CODE_IMPORTS:
      case StatechartPackage.TRANSITION__TRIGGER_EXP_RATE_CODE:
      case StatechartPackage.TRANSITION__TRIGGER_EXP_RATE_CODE_IMPORTS:
      case StatechartPackage.TRANSITION__UUID:
      case StatechartPackage.TRANSITION__SELF_TRANSITION:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
