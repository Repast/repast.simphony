package repast.simphony.statecharts.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.statecharts.edit.parts.CompositeStateName2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateNameEditPart;
import repast.simphony.statecharts.edit.parts.StateName2EditPart;
import repast.simphony.statecharts.edit.parts.StateNameEditPart;
import repast.simphony.statecharts.parsers.MessageFormatParser;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * @generated
 */
public class StatechartParserProvider extends AbstractProvider implements IParserProvider {

  /**
   * @generated
   */
  private IParser stateId_5001Parser;

  /**
   * @generated
   */
  private IParser getStateId_5001Parser() {
    if (stateId_5001Parser == null) {
      EAttribute[] features = new EAttribute[] { StatechartPackage.eINSTANCE.getAbstractState_Id() };
      MessageFormatParser parser = new MessageFormatParser(features);
      stateId_5001Parser = parser;
    }
    return stateId_5001Parser;
  }

  /**
   * @generated
   */
  private IParser compositeStateId_5004Parser;

  /**
   * @generated
   */
  private IParser getCompositeStateId_5004Parser() {
    if (compositeStateId_5004Parser == null) {
      EAttribute[] features = new EAttribute[] { StatechartPackage.eINSTANCE.getAbstractState_Id() };
      MessageFormatParser parser = new MessageFormatParser(features);
      compositeStateId_5004Parser = parser;
    }
    return compositeStateId_5004Parser;
  }

  /**
   * @generated
   */
  private IParser stateId_5002Parser;

  /**
   * @generated
   */
  private IParser getStateId_5002Parser() {
    if (stateId_5002Parser == null) {
      EAttribute[] features = new EAttribute[] { StatechartPackage.eINSTANCE.getAbstractState_Id() };
      MessageFormatParser parser = new MessageFormatParser(features);
      stateId_5002Parser = parser;
    }
    return stateId_5002Parser;
  }

  /**
   * @generated
   */
  private IParser compositeStateId_5003Parser;

  /**
   * @generated
   */
  private IParser getCompositeStateId_5003Parser() {
    if (compositeStateId_5003Parser == null) {
      EAttribute[] features = new EAttribute[] { StatechartPackage.eINSTANCE.getAbstractState_Id() };
      MessageFormatParser parser = new MessageFormatParser(features);
      compositeStateId_5003Parser = parser;
    }
    return compositeStateId_5003Parser;
  }

  /**
   * @generated
   */
  protected IParser getParser(int visualID) {
    switch (visualID) {
    case StateNameEditPart.VISUAL_ID:
      return getStateId_5001Parser();
    case CompositeStateNameEditPart.VISUAL_ID:
      return getCompositeStateId_5004Parser();
    case StateName2EditPart.VISUAL_ID:
      return getStateId_5002Parser();
    case CompositeStateName2EditPart.VISUAL_ID:
      return getCompositeStateId_5003Parser();
    }
    return null;
  }

  /**
   * Utility method that consults ParserService
   * @generated
   */
  public static IParser getParser(IElementType type, EObject object, String parserHint) {
    return ParserService.getInstance().getParser(new HintAdapter(type, object, parserHint));
  }

  /**
   * @generated
   */
  public IParser getParser(IAdaptable hint) {
    String vid = (String) hint.getAdapter(String.class);
    if (vid != null) {
      return getParser(StatechartVisualIDRegistry.getVisualID(vid));
    }
    View view = (View) hint.getAdapter(View.class);
    if (view != null) {
      return getParser(StatechartVisualIDRegistry.getVisualID(view));
    }
    return null;
  }

  /**
   * @generated
   */
  public boolean provides(IOperation operation) {
    if (operation instanceof GetParserOperation) {
      IAdaptable hint = ((GetParserOperation) operation).getHint();
      if (StatechartElementTypes.getElement(hint) == null) {
        return false;
      }
      return getParser(hint) != null;
    }
    return false;
  }

  /**
   * @generated
   */
  private static class HintAdapter extends ParserHintAdapter {

    /**
     * @generated
     */
    private final IElementType elementType;

    /**
     * @generated
     */
    public HintAdapter(IElementType type, EObject object, String parserHint) {
      super(object, parserHint);
      assert type != null;
      elementType = type;
    }

    /**
     * @generated
     */
    public Object getAdapter(Class adapter) {
      if (IElementType.class.equals(adapter)) {
        return elementType;
      }
      return super.getAdapter(adapter);
    }
  }

}
