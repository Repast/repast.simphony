/**
 */
package repast.simphony.statecharts.scmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getFrom <em>From</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTo <em>To</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getPriority <em>Priority</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getOnTransition <em>On Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isOutOfBranch <em>Out Of Branch</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isDefaultTransition <em>Default Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerType <em>Trigger Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTime <em>Trigger Time</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerCondition <em>Trigger Condition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionLanguage <em>Trigger Condition Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerType <em>Message Checker Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerClass <em>Message Checker Class</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerObj <em>Message Checker Obj</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbability <em>Trigger Probability</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCondition <em>Message Checker Condition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerConditionLanguage <em>Message Checker Condition Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition()
 * @model
 * @generated
 */
public interface Transition extends EObject {
  /**
   * Returns the value of the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>From</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>From</em>' reference.
   * @see #setFrom(AbstractState)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_From()
   * @model required="true"
   * @generated
   */
  AbstractState getFrom();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getFrom <em>From</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>From</em>' reference.
   * @see #getFrom()
   * @generated
   */
  void setFrom(AbstractState value);

  /**
   * Returns the value of the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>To</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>To</em>' reference.
   * @see #setTo(AbstractState)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_To()
   * @model required="true"
   * @generated
   */
  AbstractState getTo();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTo <em>To</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>To</em>' reference.
   * @see #getTo()
   * @generated
   */
  void setTo(AbstractState value);

  /**
   * Returns the value of the '<em><b>Priority</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Priority</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Priority</em>' attribute.
   * @see #setPriority(double)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_Priority()
   * @model default="0"
   * @generated
   */
  double getPriority();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getPriority <em>Priority</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Priority</em>' attribute.
   * @see #getPriority()
   * @generated
   */
  void setPriority(double value);

  /**
   * Returns the value of the '<em><b>On Transition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Transition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Transition</em>' attribute.
   * @see #setOnTransition(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_OnTransition()
   * @model
   * @generated
   */
  String getOnTransition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getOnTransition <em>On Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Transition</em>' attribute.
   * @see #getOnTransition()
   * @generated
   */
  void setOnTransition(String value);

  /**
   * Returns the value of the '<em><b>Out Of Branch</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Out Of Branch</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Out Of Branch</em>' attribute.
   * @see #setOutOfBranch(boolean)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_OutOfBranch()
   * @model default="false"
   * @generated
   */
  boolean isOutOfBranch();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#isOutOfBranch <em>Out Of Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Out Of Branch</em>' attribute.
   * @see #isOutOfBranch()
   * @generated
   */
  void setOutOfBranch(boolean value);

  /**
   * Returns the value of the '<em><b>Default Transition</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Transition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Transition</em>' attribute.
   * @see #setDefaultTransition(boolean)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_DefaultTransition()
   * @model default="false"
   * @generated
   */
  boolean isDefaultTransition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#isDefaultTransition <em>Default Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Transition</em>' attribute.
   * @see #isDefaultTransition()
   * @generated
   */
  void setDefaultTransition(boolean value);

  /**
   * Returns the value of the '<em><b>Trigger Type</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.TriggerTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.TriggerTypes
   * @see #setTriggerType(TriggerTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerType()
   * @model
   * @generated
   */
  TriggerTypes getTriggerType();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerType <em>Trigger Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.TriggerTypes
   * @see #getTriggerType()
   * @generated
   */
  void setTriggerType(TriggerTypes value);

  /**
   * Returns the value of the '<em><b>Trigger Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Time</em>' attribute.
   * @see #setTriggerTime(double)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerTime()
   * @model
   * @generated
   */
  double getTriggerTime();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTime <em>Trigger Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Time</em>' attribute.
   * @see #getTriggerTime()
   * @generated
   */
  void setTriggerTime(double value);

  /**
   * Returns the value of the '<em><b>Trigger Condition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Condition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Condition</em>' attribute.
   * @see #setTriggerCondition(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerCondition()
   * @model
   * @generated
   */
  String getTriggerCondition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerCondition <em>Trigger Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Condition</em>' attribute.
   * @see #getTriggerCondition()
   * @generated
   */
  void setTriggerCondition(String value);

  /**
   * Returns the value of the '<em><b>Trigger Condition Language</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.LanguageTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Condition Language</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Condition Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #setTriggerConditionLanguage(LanguageTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerConditionLanguage()
   * @model
   * @generated
   */
  LanguageTypes getTriggerConditionLanguage();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionLanguage <em>Trigger Condition Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Condition Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #getTriggerConditionLanguage()
   * @generated
   */
  void setTriggerConditionLanguage(LanguageTypes value);

  /**
   * Returns the value of the '<em><b>Message Checker Type</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.MessageCheckerTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
   * @see #setMessageCheckerType(MessageCheckerTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerType()
   * @model
   * @generated
   */
  MessageCheckerTypes getMessageCheckerType();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerType <em>Message Checker Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Type</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.MessageCheckerTypes
   * @see #getMessageCheckerType()
   * @generated
   */
  void setMessageCheckerType(MessageCheckerTypes value);

  /**
   * Returns the value of the '<em><b>Message Checker Class</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Class</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Class</em>' attribute.
   * @see #setMessageCheckerClass(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerClass()
   * @model
   * @generated
   */
  String getMessageCheckerClass();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerClass <em>Message Checker Class</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Class</em>' attribute.
   * @see #getMessageCheckerClass()
   * @generated
   */
  void setMessageCheckerClass(String value);

  /**
   * Returns the value of the '<em><b>Message Checker Obj</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Obj</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Obj</em>' attribute.
   * @see #setMessageCheckerObj(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerObj()
   * @model
   * @generated
   */
  String getMessageCheckerObj();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerObj <em>Message Checker Obj</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Obj</em>' attribute.
   * @see #getMessageCheckerObj()
   * @generated
   */
  void setMessageCheckerObj(String value);

  /**
   * Returns the value of the '<em><b>Trigger Probability</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Probability</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Probability</em>' attribute.
   * @see #setTriggerProbability(double)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerProbability()
   * @model
   * @generated
   */
  double getTriggerProbability();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbability <em>Trigger Probability</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Probability</em>' attribute.
   * @see #getTriggerProbability()
   * @generated
   */
  void setTriggerProbability(double value);

  /**
   * Returns the value of the '<em><b>Message Checker Condition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Condition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Condition</em>' attribute.
   * @see #setMessageCheckerCondition(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerCondition()
   * @model
   * @generated
   */
  String getMessageCheckerCondition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCondition <em>Message Checker Condition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Condition</em>' attribute.
   * @see #getMessageCheckerCondition()
   * @generated
   */
  void setMessageCheckerCondition(String value);

  /**
   * Returns the value of the '<em><b>Message Checker Condition Language</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.LanguageTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Condition Language</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Condition Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #setMessageCheckerConditionLanguage(LanguageTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerConditionLanguage()
   * @model
   * @generated
   */
  LanguageTypes getMessageCheckerConditionLanguage();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerConditionLanguage <em>Message Checker Condition Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Condition Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #getMessageCheckerConditionLanguage()
   * @generated
   */
  void setMessageCheckerConditionLanguage(LanguageTypes value);

} // Transition
