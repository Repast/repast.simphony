/**
 */
package repast.simphony.systemdynamics.sdmodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import repast.simphony.systemdynamics.sdmodel.Cloud;
import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.Subscript;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SDModelPackageImpl extends EPackageImpl implements SDModelPackage {
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass systemModelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass influenceLinkEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass cloudEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stockEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass variableEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass subscriptEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum variableTypeEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see repast.simphony.systemdynamics.sdmodel.SDModelPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SDModelPackageImpl() {
    super(eNS_URI, SDModelFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link SDModelPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static SDModelPackage init() {
    if (isInited) return (SDModelPackage)EPackage.Registry.INSTANCE.getEPackage(SDModelPackage.eNS_URI);

    // Obtain or create and register package
    SDModelPackageImpl theSDModelPackage = (SDModelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SDModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SDModelPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theSDModelPackage.createPackageContents();

    // Initialize created meta-data
    theSDModelPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theSDModelPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(SDModelPackage.eNS_URI, theSDModelPackage);
    return theSDModelPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSystemModel() {
    return systemModelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSystemModel_Links() {
    return (EReference)systemModelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSystemModel_Variables() {
    return (EReference)systemModelEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSystemModel_StartTime() {
    return (EAttribute)systemModelEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSystemModel_EndTime() {
    return (EAttribute)systemModelEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSystemModel_TimeStep() {
    return (EAttribute)systemModelEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSystemModel_Units() {
    return (EAttribute)systemModelEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSystemModel_ReportingInterval() {
    return (EAttribute)systemModelEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSystemModel_Subscripts() {
    return (EReference)systemModelEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInfluenceLink() {
    return influenceLinkEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getInfluenceLink_Uuid() {
    return (EAttribute)influenceLinkEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInfluenceLink_From() {
    return (EReference)influenceLinkEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInfluenceLink_To() {
    return (EReference)influenceLinkEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCloud() {
    return cloudEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStock() {
    return stockEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStock_InitialValue() {
    return (EAttribute)stockEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRate() {
    return rateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRate_To() {
    return (EReference)rateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRate_From() {
    return (EReference)rateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVariable() {
    return variableEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariable_Uuid() {
    return (EAttribute)variableEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariable_Name() {
    return (EAttribute)variableEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariable_Type() {
    return (EAttribute)variableEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariable_Units() {
    return (EAttribute)variableEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariable_Equation() {
    return (EAttribute)variableEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSubscript() {
    return subscriptEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSubscript_Name() {
    return (EAttribute)subscriptEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSubscript_Elements() {
    return (EAttribute)subscriptEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getVariableType() {
    return variableTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SDModelFactory getSDModelFactory() {
    return (SDModelFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents() {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    systemModelEClass = createEClass(SYSTEM_MODEL);
    createEReference(systemModelEClass, SYSTEM_MODEL__LINKS);
    createEReference(systemModelEClass, SYSTEM_MODEL__VARIABLES);
    createEAttribute(systemModelEClass, SYSTEM_MODEL__START_TIME);
    createEAttribute(systemModelEClass, SYSTEM_MODEL__END_TIME);
    createEAttribute(systemModelEClass, SYSTEM_MODEL__TIME_STEP);
    createEAttribute(systemModelEClass, SYSTEM_MODEL__UNITS);
    createEAttribute(systemModelEClass, SYSTEM_MODEL__REPORTING_INTERVAL);
    createEReference(systemModelEClass, SYSTEM_MODEL__SUBSCRIPTS);

    influenceLinkEClass = createEClass(INFLUENCE_LINK);
    createEAttribute(influenceLinkEClass, INFLUENCE_LINK__UUID);
    createEReference(influenceLinkEClass, INFLUENCE_LINK__FROM);
    createEReference(influenceLinkEClass, INFLUENCE_LINK__TO);

    cloudEClass = createEClass(CLOUD);

    stockEClass = createEClass(STOCK);
    createEAttribute(stockEClass, STOCK__INITIAL_VALUE);

    rateEClass = createEClass(RATE);
    createEReference(rateEClass, RATE__TO);
    createEReference(rateEClass, RATE__FROM);

    variableEClass = createEClass(VARIABLE);
    createEAttribute(variableEClass, VARIABLE__UUID);
    createEAttribute(variableEClass, VARIABLE__NAME);
    createEAttribute(variableEClass, VARIABLE__TYPE);
    createEAttribute(variableEClass, VARIABLE__UNITS);
    createEAttribute(variableEClass, VARIABLE__EQUATION);

    subscriptEClass = createEClass(SUBSCRIPT);
    createEAttribute(subscriptEClass, SUBSCRIPT__NAME);
    createEAttribute(subscriptEClass, SUBSCRIPT__ELEMENTS);

    // Create enums
    variableTypeEEnum = createEEnum(VARIABLE_TYPE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents() {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    cloudEClass.getESuperTypes().add(this.getStock());
    stockEClass.getESuperTypes().add(this.getVariable());
    rateEClass.getESuperTypes().add(this.getVariable());

    // Initialize classes and features; add operations and parameters
    initEClass(systemModelEClass, SystemModel.class, "SystemModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSystemModel_Links(), this.getInfluenceLink(), null, "links", null, 0, -1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSystemModel_Variables(), this.getVariable(), null, "variables", null, 0, -1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSystemModel_StartTime(), ecorePackage.getEDouble(), "startTime", null, 0, 1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSystemModel_EndTime(), ecorePackage.getEDouble(), "endTime", null, 0, 1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSystemModel_TimeStep(), ecorePackage.getEDouble(), "timeStep", null, 0, 1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSystemModel_Units(), ecorePackage.getEString(), "units", null, 0, 1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSystemModel_ReportingInterval(), ecorePackage.getEDouble(), "reportingInterval", null, 0, 1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSystemModel_Subscripts(), this.getSubscript(), null, "subscripts", null, 0, -1, SystemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(influenceLinkEClass, InfluenceLink.class, "InfluenceLink", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getInfluenceLink_Uuid(), ecorePackage.getEString(), "uuid", null, 0, 1, InfluenceLink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInfluenceLink_From(), this.getVariable(), null, "from", null, 1, 1, InfluenceLink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInfluenceLink_To(), this.getVariable(), null, "to", null, 1, 1, InfluenceLink.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(cloudEClass, Cloud.class, "Cloud", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(stockEClass, Stock.class, "Stock", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStock_InitialValue(), ecorePackage.getEDouble(), "initialValue", "0", 0, 1, Stock.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(rateEClass, Rate.class, "Rate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRate_To(), this.getStock(), null, "to", null, 1, 1, Rate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRate_From(), this.getStock(), null, "from", null, 1, 1, Rate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(variableEClass, Variable.class, "Variable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getVariable_Uuid(), ecorePackage.getEString(), "uuid", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariable_Name(), ecorePackage.getEString(), "name", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariable_Type(), this.getVariableType(), "type", "", 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariable_Units(), ecorePackage.getEString(), "units", "", 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariable_Equation(), ecorePackage.getEString(), "equation", "", 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(subscriptEClass, Subscript.class, "Subscript", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getSubscript_Name(), ecorePackage.getEString(), "name", null, 0, 1, Subscript.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSubscript_Elements(), ecorePackage.getEString(), "elements", null, 0, -1, Subscript.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(variableTypeEEnum, VariableType.class, "VariableType");
    addEEnumLiteral(variableTypeEEnum, VariableType.CONSTANT);
    addEEnumLiteral(variableTypeEEnum, VariableType.AUXILIARY);
    addEEnumLiteral(variableTypeEEnum, VariableType.STOCK);
    addEEnumLiteral(variableTypeEEnum, VariableType.RATE);
    addEEnumLiteral(variableTypeEEnum, VariableType.LOOKUP);

    // Create resource
    createResource(eNS_URI);
  }

} //SDModelPackageImpl
