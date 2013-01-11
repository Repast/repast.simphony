/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see repast.simphony.systemdynamics.sdmodel.SDModelFactory
 * @model kind="package"
 * @generated
 */
public interface SDModelPackage extends EPackage {
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "sdmodel";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://repast.sf.net/systemdynamics";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SDModelPackage eINSTANCE = repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl.init();

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl <em>System Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getSystemModel()
   * @generated
   */
  int SYSTEM_MODEL = 0;

  /**
   * The feature id for the '<em><b>Links</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL__LINKS = 0;

  /**
   * The feature id for the '<em><b>Variables</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL__VARIABLES = 1;

  /**
   * The feature id for the '<em><b>Start Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL__START_TIME = 2;

  /**
   * The feature id for the '<em><b>End Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL__END_TIME = 3;

  /**
   * The feature id for the '<em><b>Time Step</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL__TIME_STEP = 4;

  /**
   * The feature id for the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL__UNITS = 5;

  /**
   * The number of structural features of the '<em>System Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl <em>Influence Link</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getInfluenceLink()
   * @generated
   */
  int INFLUENCE_LINK = 1;

  /**
   * The feature id for the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INFLUENCE_LINK__UUID = 0;

  /**
   * The feature id for the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INFLUENCE_LINK__FROM = 1;

  /**
   * The feature id for the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INFLUENCE_LINK__TO = 2;

  /**
   * The number of structural features of the '<em>Influence Link</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INFLUENCE_LINK_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl <em>Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.VariableImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getVariable()
   * @generated
   */
  int VARIABLE = 5;

  /**
   * The feature id for the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__UUID = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__NAME = 1;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__TYPE = 2;

  /**
   * The feature id for the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__UNITS = 3;

  /**
   * The feature id for the '<em><b>Equation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__EQUATION = 4;

  /**
   * The number of structural features of the '<em>Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.StockImpl <em>Stock</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.StockImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getStock()
   * @generated
   */
  int STOCK = 3;

  /**
   * The feature id for the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__UUID = VARIABLE__UUID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__NAME = VARIABLE__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__TYPE = VARIABLE__TYPE;

  /**
   * The feature id for the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__UNITS = VARIABLE__UNITS;

  /**
   * The feature id for the '<em><b>Equation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__EQUATION = VARIABLE__EQUATION;

  /**
   * The feature id for the '<em><b>Initial Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__INITIAL_VALUE = VARIABLE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Stock</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK_FEATURE_COUNT = VARIABLE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.CloudImpl <em>Cloud</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.CloudImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getCloud()
   * @generated
   */
  int CLOUD = 2;

  /**
   * The feature id for the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__UUID = STOCK__UUID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__NAME = STOCK__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__TYPE = STOCK__TYPE;

  /**
   * The feature id for the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__UNITS = STOCK__UNITS;

  /**
   * The feature id for the '<em><b>Equation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__EQUATION = STOCK__EQUATION;

  /**
   * The feature id for the '<em><b>Initial Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__INITIAL_VALUE = STOCK__INITIAL_VALUE;

  /**
   * The number of structural features of the '<em>Cloud</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD_FEATURE_COUNT = STOCK_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.RateImpl <em>Rate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.RateImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getRate()
   * @generated
   */
  int RATE = 4;

  /**
   * The feature id for the '<em><b>Uuid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__UUID = VARIABLE__UUID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__NAME = VARIABLE__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__TYPE = VARIABLE__TYPE;

  /**
   * The feature id for the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__UNITS = VARIABLE__UNITS;

  /**
   * The feature id for the '<em><b>Equation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__EQUATION = VARIABLE__EQUATION;

  /**
   * The feature id for the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__TO = VARIABLE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__FROM = VARIABLE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Rate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE_FEATURE_COUNT = VARIABLE_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.VariableType <em>Variable Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.VariableType
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getVariableType()
   * @generated
   */
  int VARIABLE_TYPE = 6;


  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.SystemModel <em>System Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>System Model</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel
   * @generated
   */
  EClass getSystemModel();

  /**
   * Returns the meta object for the containment reference list '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getLinks <em>Links</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Links</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel#getLinks()
   * @see #getSystemModel()
   * @generated
   */
  EReference getSystemModel_Links();

  /**
   * Returns the meta object for the containment reference list '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getVariables <em>Variables</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Variables</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel#getVariables()
   * @see #getSystemModel()
   * @generated
   */
  EReference getSystemModel_Variables();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getStartTime <em>Start Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Start Time</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel#getStartTime()
   * @see #getSystemModel()
   * @generated
   */
  EAttribute getSystemModel_StartTime();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getEndTime <em>End Time</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>End Time</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel#getEndTime()
   * @see #getSystemModel()
   * @generated
   */
  EAttribute getSystemModel_EndTime();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getTimeStep <em>Time Step</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Time Step</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel#getTimeStep()
   * @see #getSystemModel()
   * @generated
   */
  EAttribute getSystemModel_TimeStep();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.SystemModel#getUnits <em>Units</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Units</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.SystemModel#getUnits()
   * @see #getSystemModel()
   * @generated
   */
  EAttribute getSystemModel_Units();

  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink <em>Influence Link</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Influence Link</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.InfluenceLink
   * @generated
   */
  EClass getInfluenceLink();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getUuid <em>Uuid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Uuid</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.InfluenceLink#getUuid()
   * @see #getInfluenceLink()
   * @generated
   */
  EAttribute getInfluenceLink_Uuid();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getFrom <em>From</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>From</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.InfluenceLink#getFrom()
   * @see #getInfluenceLink()
   * @generated
   */
  EReference getInfluenceLink_From();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.systemdynamics.sdmodel.InfluenceLink#getTo <em>To</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>To</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.InfluenceLink#getTo()
   * @see #getInfluenceLink()
   * @generated
   */
  EReference getInfluenceLink_To();

  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.Cloud <em>Cloud</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Cloud</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Cloud
   * @generated
   */
  EClass getCloud();

  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.Stock <em>Stock</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Stock</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Stock
   * @generated
   */
  EClass getStock();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.Stock#getInitialValue <em>Initial Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Initial Value</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Stock#getInitialValue()
   * @see #getStock()
   * @generated
   */
  EAttribute getStock_InitialValue();

  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.Rate <em>Rate</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Rate</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Rate
   * @generated
   */
  EClass getRate();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.systemdynamics.sdmodel.Rate#getTo <em>To</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>To</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Rate#getTo()
   * @see #getRate()
   * @generated
   */
  EReference getRate_To();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.systemdynamics.sdmodel.Rate#getFrom <em>From</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>From</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Rate#getFrom()
   * @see #getRate()
   * @generated
   */
  EReference getRate_From();

  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.Variable <em>Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Variable
   * @generated
   */
  EClass getVariable();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.Variable#getUuid <em>Uuid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Uuid</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Variable#getUuid()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Uuid();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.Variable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Variable#getName()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Name();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.Variable#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Variable#getType()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Type();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.Variable#getUnits <em>Units</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Units</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Variable#getUnits()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Units();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.Variable#getEquation <em>Equation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Equation</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.Variable#getEquation()
   * @see #getVariable()
   * @generated
   */
  EAttribute getVariable_Equation();

  /**
   * Returns the meta object for enum '{@link repast.simphony.systemdynamics.sdmodel.VariableType <em>Variable Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Variable Type</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.VariableType
   * @generated
   */
  EEnum getVariableType();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SDModelFactory getSDModelFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals {
    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl <em>System Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.SystemModelImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getSystemModel()
     * @generated
     */
    EClass SYSTEM_MODEL = eINSTANCE.getSystemModel();

    /**
     * The meta object literal for the '<em><b>Links</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYSTEM_MODEL__LINKS = eINSTANCE.getSystemModel_Links();

    /**
     * The meta object literal for the '<em><b>Variables</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SYSTEM_MODEL__VARIABLES = eINSTANCE.getSystemModel_Variables();

    /**
     * The meta object literal for the '<em><b>Start Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYSTEM_MODEL__START_TIME = eINSTANCE.getSystemModel_StartTime();

    /**
     * The meta object literal for the '<em><b>End Time</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYSTEM_MODEL__END_TIME = eINSTANCE.getSystemModel_EndTime();

    /**
     * The meta object literal for the '<em><b>Time Step</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYSTEM_MODEL__TIME_STEP = eINSTANCE.getSystemModel_TimeStep();

    /**
     * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SYSTEM_MODEL__UNITS = eINSTANCE.getSystemModel_Units();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl <em>Influence Link</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.InfluenceLinkImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getInfluenceLink()
     * @generated
     */
    EClass INFLUENCE_LINK = eINSTANCE.getInfluenceLink();

    /**
     * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INFLUENCE_LINK__UUID = eINSTANCE.getInfluenceLink_Uuid();

    /**
     * The meta object literal for the '<em><b>From</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INFLUENCE_LINK__FROM = eINSTANCE.getInfluenceLink_From();

    /**
     * The meta object literal for the '<em><b>To</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INFLUENCE_LINK__TO = eINSTANCE.getInfluenceLink_To();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.CloudImpl <em>Cloud</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.CloudImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getCloud()
     * @generated
     */
    EClass CLOUD = eINSTANCE.getCloud();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.StockImpl <em>Stock</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.StockImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getStock()
     * @generated
     */
    EClass STOCK = eINSTANCE.getStock();

    /**
     * The meta object literal for the '<em><b>Initial Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STOCK__INITIAL_VALUE = eINSTANCE.getStock_InitialValue();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.RateImpl <em>Rate</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.RateImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getRate()
     * @generated
     */
    EClass RATE = eINSTANCE.getRate();

    /**
     * The meta object literal for the '<em><b>To</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RATE__TO = eINSTANCE.getRate_To();

    /**
     * The meta object literal for the '<em><b>From</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RATE__FROM = eINSTANCE.getRate_From();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl <em>Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.VariableImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getVariable()
     * @generated
     */
    EClass VARIABLE = eINSTANCE.getVariable();

    /**
     * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__UUID = eINSTANCE.getVariable_Uuid();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__NAME = eINSTANCE.getVariable_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__TYPE = eINSTANCE.getVariable_Type();

    /**
     * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__UNITS = eINSTANCE.getVariable_Units();

    /**
     * The meta object literal for the '<em><b>Equation</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE__EQUATION = eINSTANCE.getVariable_Equation();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.VariableType <em>Variable Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.VariableType
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getVariableType()
     * @generated
     */
    EEnum VARIABLE_TYPE = eINSTANCE.getVariableType();

  }

} //SDModelPackage
