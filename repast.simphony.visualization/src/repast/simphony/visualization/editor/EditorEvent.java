package repast.simphony.visualization.editor;

/**
 * Encapsulates an editor event, adding and removing an agent for example.
 *
 * @author Nick Collier
 * @deprecated 2D piccolo based code is being removed
 */
public class EditorEvent {

  private Object source;
  private Object subject;

  /**
   * Creates an EditorEvent from the specified source and subject.
   *
   * @param source the source of the event (e.g. a DisplayEditor2D)
   * @param subject the subject of the event (e.g. an agent that has been added)
   */
  public EditorEvent(Object source, Object subject) {
    this.source = source;
    this.subject = subject;
  }

  public Object getSource() {
    return source;
  }

  public Object getSubject() {
    return subject;
  }
}
