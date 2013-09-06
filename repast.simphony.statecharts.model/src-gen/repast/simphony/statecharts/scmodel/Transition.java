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
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getOnTransitionImports <em>On Transition Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isOutOfBranch <em>Out Of Branch</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isDefaultTransition <em>Default Transition</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerType <em>Trigger Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTime <em>Trigger Time</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionCode <em>Trigger Condition Code</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionImports <em>Trigger Condition Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerCodeLanguage <em>Trigger Code Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerType <em>Message Checker Type</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerClass <em>Message Checker Class</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbCode <em>Trigger Prob Code</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbeCodeImports <em>Trigger Probe Code Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCode <em>Message Checker Code</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCodeImports <em>Message Checker Code Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerConditionLanguage <em>Message Checker Condition Language</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getId <em>Id</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getGuard <em>Guard</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getGuardImports <em>Guard Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTimedCode <em>Trigger Timed Code</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTimedCodeImports <em>Trigger Timed Code Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerExpRateCode <em>Trigger Exp Rate Code</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getTriggerExpRateCodeImports <em>Trigger Exp Rate Code Imports</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#getUuid <em>Uuid</em>}</li>
 *   <li>{@link repast.simphony.statecharts.scmodel.Transition#isSelfTransition <em>Self Transition</em>}</li>
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
   * Returns the value of the '<em><b>On Transition Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Transition Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Transition Imports</em>' attribute.
   * @see #setOnTransitionImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_OnTransitionImports()
   * @model
   * @generated
   */
  String getOnTransitionImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getOnTransitionImports <em>On Transition Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Transition Imports</em>' attribute.
   * @see #getOnTransitionImports()
   * @generated
   */
  void setOnTransitionImports(String value);

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
   * The default value is <code>"1.0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Time</em>' attribute.
   * @see #setTriggerTime(double)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerTime()
   * @model default="1.0"
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
   * Returns the value of the '<em><b>Trigger Condition Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Condition Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Condition Code</em>' attribute.
   * @see #setTriggerConditionCode(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerConditionCode()
   * @model
   * @generated
   */
  String getTriggerConditionCode();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionCode <em>Trigger Condition Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Condition Code</em>' attribute.
   * @see #getTriggerConditionCode()
   * @generated
   */
  void setTriggerConditionCode(String value);

  /**
   * Returns the value of the '<em><b>Trigger Condition Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Condition Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Condition Imports</em>' attribute.
   * @see #setTriggerConditionImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerConditionImports()
   * @model
   * @generated
   */
  String getTriggerConditionImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerConditionImports <em>Trigger Condition Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Condition Imports</em>' attribute.
   * @see #getTriggerConditionImports()
   * @generated
   */
  void setTriggerConditionImports(String value);

  /**
   * Returns the value of the '<em><b>Trigger Code Language</b></em>' attribute.
   * The literals are from the enumeration {@link repast.simphony.statecharts.scmodel.LanguageTypes}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Code Language</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Code Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #setTriggerCodeLanguage(LanguageTypes)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerCodeLanguage()
   * @model
   * @generated
   */
  LanguageTypes getTriggerCodeLanguage();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerCodeLanguage <em>Trigger Code Language</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Code Language</em>' attribute.
   * @see repast.simphony.statecharts.scmodel.LanguageTypes
   * @see #getTriggerCodeLanguage()
   * @generated
   */
  void setTriggerCodeLanguage(LanguageTypes value);

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
   * Returns the value of the '<em><b>Trigger Prob Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Prob Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Prob Code</em>' attribute.
   * @see #setTriggerProbCode(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerProbCode()
   * @model
   * @generated
   */
  String getTriggerProbCode();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbCode <em>Trigger Prob Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Prob Code</em>' attribute.
   * @see #getTriggerProbCode()
   * @generated
   */
  void setTriggerProbCode(String value);

  /**
   * Returns the value of the '<em><b>Trigger Probe Code Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Probe Code Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Probe Code Imports</em>' attribute.
   * @see #setTriggerProbeCodeImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerProbeCodeImports()
   * @model
   * @generated
   */
  String getTriggerProbeCodeImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerProbeCodeImports <em>Trigger Probe Code Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Probe Code Imports</em>' attribute.
   * @see #getTriggerProbeCodeImports()
   * @generated
   */
  void setTriggerProbeCodeImports(String value);

  /**
   * Returns the value of the '<em><b>Message Checker Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Code</em>' attribute.
   * @see #setMessageCheckerCode(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerCode()
   * @model
   * @generated
   */
  String getMessageCheckerCode();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCode <em>Message Checker Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Code</em>' attribute.
   * @see #getMessageCheckerCode()
   * @generated
   */
  void setMessageCheckerCode(String value);

  /**
   * Returns the value of the '<em><b>Message Checker Code Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message Checker Code Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message Checker Code Imports</em>' attribute.
   * @see #setMessageCheckerCodeImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_MessageCheckerCodeImports()
   * @model
   * @generated
   */
  String getMessageCheckerCodeImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getMessageCheckerCodeImports <em>Message Checker Code Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message Checker Code Imports</em>' attribute.
   * @see #getMessageCheckerCodeImports()
   * @generated
   */
  void setMessageCheckerCodeImports(String value);

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

  /**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_Id()
   * @model
   * @generated
   */
  String getId();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
  void setId(String value);

  /**
   * Returns the value of the '<em><b>Guard</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Guard</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Guard</em>' attribute.
   * @see #setGuard(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_Guard()
   * @model
   * @generated
   */
  String getGuard();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getGuard <em>Guard</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Guard</em>' attribute.
   * @see #getGuard()
   * @generated
   */
  void setGuard(String value);

  /**
   * Returns the value of the '<em><b>Guard Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Guard Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Guard Imports</em>' attribute.
   * @see #setGuardImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_GuardImports()
   * @model
   * @generated
   */
  String getGuardImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getGuardImports <em>Guard Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Guard Imports</em>' attribute.
   * @see #getGuardImports()
   * @generated
   */
  void setGuardImports(String value);

  /**
   * Returns the value of the '<em><b>Trigger Timed Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Timed Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Timed Code</em>' attribute.
   * @see #setTriggerTimedCode(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerTimedCode()
   * @model
   * @generated
   */
  String getTriggerTimedCode();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTimedCode <em>Trigger Timed Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Timed Code</em>' attribute.
   * @see #getTriggerTimedCode()
   * @generated
   */
  void setTriggerTimedCode(String value);

  /**
   * Returns the value of the '<em><b>Trigger Timed Code Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Timed Code Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Timed Code Imports</em>' attribute.
   * @see #setTriggerTimedCodeImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerTimedCodeImports()
   * @model
   * @generated
   */
  String getTriggerTimedCodeImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerTimedCodeImports <em>Trigger Timed Code Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Timed Code Imports</em>' attribute.
   * @see #getTriggerTimedCodeImports()
   * @generated
   */
  void setTriggerTimedCodeImports(String value);

  /**
   * Returns the value of the '<em><b>Trigger Exp Rate Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Exp Rate Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Exp Rate Code</em>' attribute.
   * @see #setTriggerExpRateCode(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerExpRateCode()
   * @model
   * @generated
   */
  String getTriggerExpRateCode();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerExpRateCode <em>Trigger Exp Rate Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Exp Rate Code</em>' attribute.
   * @see #getTriggerExpRateCode()
   * @generated
   */
  void setTriggerExpRateCode(String value);

  /**
   * Returns the value of the '<em><b>Trigger Exp Rate Code Imports</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trigger Exp Rate Code Imports</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trigger Exp Rate Code Imports</em>' attribute.
   * @see #setTriggerExpRateCodeImports(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_TriggerExpRateCodeImports()
   * @model
   * @generated
   */
  String getTriggerExpRateCodeImports();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getTriggerExpRateCodeImports <em>Trigger Exp Rate Code Imports</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trigger Exp Rate Code Imports</em>' attribute.
   * @see #getTriggerExpRateCodeImports()
   * @generated
   */
  void setTriggerExpRateCodeImports(String value);

  /**
   * Returns the value of the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Uuid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Uuid</em>' attribute.
   * @see #setUuid(String)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_Uuid()
   * @model
   * @generated
   */
  String getUuid();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#getUuid <em>Uuid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uuid</em>' attribute.
   * @see #getUuid()
   * @generated
   */
  void setUuid(String value);

  /**
   * Returns the value of the '<em><b>Self Transition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Self Transition</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Self Transition</em>' attribute.
   * @see #setSelfTransition(boolean)
   * @see repast.simphony.statecharts.scmodel.StatechartPackage#getTransition_SelfTransition()
   * @model
   * @generated
   */
  boolean isSelfTransition();

  /**
   * Sets the value of the '{@link repast.simphony.statecharts.scmodel.Transition#isSelfTransition <em>Self Transition</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Self Transition</em>' attribute.
   * @see #isSelfTransition()
   * @generated
   */
  void setSelfTransition(boolean value);

} // Transition
