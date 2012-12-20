/**
 */
package repast.simphony.systemdynamics.sdmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
   * The number of structural features of the '<em>System Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SYSTEM_MODEL_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.CausalLinkImpl <em>Causal Link</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.CausalLinkImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getCausalLink()
   * @generated
   */
  int CAUSAL_LINK = 1;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CAUSAL_LINK__ID = 0;

  /**
   * The feature id for the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CAUSAL_LINK__FROM = 1;

  /**
   * The feature id for the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CAUSAL_LINK__TO = 2;

  /**
   * The number of structural features of the '<em>Causal Link</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CAUSAL_LINK_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.AbstractVariableImpl <em>Abstract Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.AbstractVariableImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getAbstractVariable()
   * @generated
   */
  int ABSTRACT_VARIABLE = 2;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_VARIABLE__ID = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_VARIABLE__NAME = 1;

  /**
   * The number of structural features of the '<em>Abstract Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ABSTRACT_VARIABLE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.VariableImpl <em>Variable</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.VariableImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getVariable()
   * @generated
   */
  int VARIABLE = 3;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__ID = ABSTRACT_VARIABLE__ID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE__NAME = ABSTRACT_VARIABLE__NAME;

  /**
   * The number of structural features of the '<em>Variable</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_FEATURE_COUNT = ABSTRACT_VARIABLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.CloudImpl <em>Cloud</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.CloudImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getCloud()
   * @generated
   */
  int CLOUD = 4;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__ID = ABSTRACT_VARIABLE__ID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD__NAME = ABSTRACT_VARIABLE__NAME;

  /**
   * The number of structural features of the '<em>Cloud</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLOUD_FEATURE_COUNT = ABSTRACT_VARIABLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.StockImpl <em>Stock</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.StockImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getStock()
   * @generated
   */
  int STOCK = 5;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__ID = ABSTRACT_VARIABLE__ID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK__NAME = ABSTRACT_VARIABLE__NAME;

  /**
   * The number of structural features of the '<em>Stock</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STOCK_FEATURE_COUNT = ABSTRACT_VARIABLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link repast.simphony.systemdynamics.sdmodel.impl.RateImpl <em>Rate</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see repast.simphony.systemdynamics.sdmodel.impl.RateImpl
   * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getRate()
   * @generated
   */
  int RATE = 6;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__ID = ABSTRACT_VARIABLE__ID;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__NAME = ABSTRACT_VARIABLE__NAME;

  /**
   * The feature id for the '<em><b>To</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__TO = ABSTRACT_VARIABLE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>From</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE__FROM = ABSTRACT_VARIABLE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Rate</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RATE_FEATURE_COUNT = ABSTRACT_VARIABLE_FEATURE_COUNT + 2;


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
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.CausalLink <em>Causal Link</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Causal Link</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.CausalLink
   * @generated
   */
  EClass getCausalLink();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.CausalLink#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.CausalLink#getId()
   * @see #getCausalLink()
   * @generated
   */
  EAttribute getCausalLink_Id();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.systemdynamics.sdmodel.CausalLink#getFrom <em>From</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>From</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.CausalLink#getFrom()
   * @see #getCausalLink()
   * @generated
   */
  EReference getCausalLink_From();

  /**
   * Returns the meta object for the reference '{@link repast.simphony.systemdynamics.sdmodel.CausalLink#getTo <em>To</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>To</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.CausalLink#getTo()
   * @see #getCausalLink()
   * @generated
   */
  EReference getCausalLink_To();

  /**
   * Returns the meta object for class '{@link repast.simphony.systemdynamics.sdmodel.AbstractVariable <em>Abstract Variable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Abstract Variable</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.AbstractVariable
   * @generated
   */
  EClass getAbstractVariable();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.AbstractVariable#getId <em>Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Id</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.AbstractVariable#getId()
   * @see #getAbstractVariable()
   * @generated
   */
  EAttribute getAbstractVariable_Id();

  /**
   * Returns the meta object for the attribute '{@link repast.simphony.systemdynamics.sdmodel.AbstractVariable#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see repast.simphony.systemdynamics.sdmodel.AbstractVariable#getName()
   * @see #getAbstractVariable()
   * @generated
   */
  EAttribute getAbstractVariable_Name();

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
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.CausalLinkImpl <em>Causal Link</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.CausalLinkImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getCausalLink()
     * @generated
     */
    EClass CAUSAL_LINK = eINSTANCE.getCausalLink();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute CAUSAL_LINK__ID = eINSTANCE.getCausalLink_Id();

    /**
     * The meta object literal for the '<em><b>From</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CAUSAL_LINK__FROM = eINSTANCE.getCausalLink_From();

    /**
     * The meta object literal for the '<em><b>To</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CAUSAL_LINK__TO = eINSTANCE.getCausalLink_To();

    /**
     * The meta object literal for the '{@link repast.simphony.systemdynamics.sdmodel.impl.AbstractVariableImpl <em>Abstract Variable</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see repast.simphony.systemdynamics.sdmodel.impl.AbstractVariableImpl
     * @see repast.simphony.systemdynamics.sdmodel.impl.SDModelPackageImpl#getAbstractVariable()
     * @generated
     */
    EClass ABSTRACT_VARIABLE = eINSTANCE.getAbstractVariable();

    /**
     * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_VARIABLE__ID = eINSTANCE.getAbstractVariable_Id();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ABSTRACT_VARIABLE__NAME = eINSTANCE.getAbstractVariable_Name();

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

  }

} //SDModelPackage
