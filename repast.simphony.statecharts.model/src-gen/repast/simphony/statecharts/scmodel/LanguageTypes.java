/**
 */
package repast.simphony.statecharts.scmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Language Types</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see repast.simphony.statecharts.scmodel.StatechartPackage#getLanguageTypes()
 * @model
 * @generated
 */
public enum LanguageTypes implements Enumerator {
  /**
   * The '<em><b>Java</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #JAVA_VALUE
   * @generated
   * @ordered
   */
  JAVA(0, "java", "java"),

  /**
   * The '<em><b>Groovy</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #GROOVY_VALUE
   * @generated
   * @ordered
   */
  GROOVY(0, "groovy", "groovy"),

  /**
   * The '<em><b>Relogo</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RELOGO_VALUE
   * @generated
   * @ordered
   */
  RELOGO(0, "relogo", "relogo");

  /**
   * The '<em><b>Java</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Java</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #JAVA
   * @model name="java"
   * @generated
   * @ordered
   */
  public static final int JAVA_VALUE = 0;

  /**
   * The '<em><b>Groovy</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Groovy</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #GROOVY
   * @model name="groovy"
   * @generated
   * @ordered
   */
  public static final int GROOVY_VALUE = 0;

  /**
   * The '<em><b>Relogo</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Relogo</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RELOGO
   * @model name="relogo"
   * @generated
   * @ordered
   */
  public static final int RELOGO_VALUE = 0;

  /**
   * An array of all the '<em><b>Language Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final LanguageTypes[] VALUES_ARRAY =
    new LanguageTypes[] {
      JAVA,
      GROOVY,
      RELOGO,
    };

  /**
   * A public read-only list of all the '<em><b>Language Types</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<LanguageTypes> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Language Types</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static LanguageTypes get(String literal) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      LanguageTypes result = VALUES_ARRAY[i];
      if (result.toString().equals(literal)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Language Types</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static LanguageTypes getByName(String name) {
    for (int i = 0; i < VALUES_ARRAY.length; ++i) {
      LanguageTypes result = VALUES_ARRAY[i];
      if (result.getName().equals(name)) {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Language Types</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static LanguageTypes get(int value) {
    switch (value) {
      case JAVA_VALUE: return JAVA;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private LanguageTypes(int value, String name, String literal) {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue() {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName() {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLiteral() {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    return literal;
  }
  
} //LanguageTypes
