package repast.simphony.statecharts.editor;

import java.util.Stack;

import org.codehaus.groovy.eclipse.editor.GroovyPartitionScanner;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.text.JavaHeuristicScanner;
import org.eclipse.jdt.internal.ui.text.JavaPartitionScanner;
import org.eclipse.jdt.internal.ui.text.Symbols;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.link.ILinkedModeListener;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedModeUI.ExitFlags;
import org.eclipse.jface.text.link.LinkedModeUI.IExitPolicy;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

/**
 * Taken from GroovyEditor which also Borrowed from
 * {@link CompilationUnitEditor.BracketInserter}
 * 
 * Changes marked with // GROOVY
 */
public class GroovyBracketInserter implements VerifyKeyListener, ILinkedModeListener {

  /**
   * Borrowed from {@link CompilationUnitEditor.ExclusivePositionUpdater}
   * Position updater that takes any changes at the borders of a position to not
   * belong to the position.
   * 
   * @since 3.0
   */
  private static class GroovyExclusivePositionUpdater implements IPositionUpdater {

    /** The position category. */
    private final String fCategory;

    /**
     * Creates a new updater for the given <code>category</code>.
     * 
     * @param category
     *          the new category.
     */
    public GroovyExclusivePositionUpdater(String category) {
      fCategory = category;
    }

    /*
     * @see
     * org.eclipse.jface.text.IPositionUpdater#update(org.eclipse.jface.text
     * .DocumentEvent)
     */
    public void update(DocumentEvent event) {

      int eventOffset = event.getOffset();
      int eventOldLength = event.getLength();
      int eventNewLength = event.getText() == null ? 0 : event.getText().length();
      int deltaLength = eventNewLength - eventOldLength;

      try {
        Position[] positions = event.getDocument().getPositions(fCategory);

        for (int i = 0; i != positions.length; i++) {

          Position position = positions[i];

          if (position.isDeleted())
            continue;

          int offset = position.getOffset();
          int length = position.getLength();
          int end = offset + length;

          if (offset >= eventOffset + eventOldLength)
            // position comes
            // after change - shift
            position.setOffset(offset + deltaLength);
          else if (end <= eventOffset) {
            // position comes way before change -
            // leave alone
          } else if (offset <= eventOffset && end >= eventOffset + eventOldLength) {
            // event completely internal to the position - adjust length
            position.setLength(length + deltaLength);
          } else if (offset < eventOffset) {
            // event extends over end of position - adjust length
            int newEnd = eventOffset;
            position.setLength(newEnd - offset);
          } else if (end > eventOffset + eventOldLength) {
            // event extends from before position into it - adjust offset
            // and length
            // offset becomes end of event, length adjusted accordingly
            int newOffset = eventOffset + eventNewLength;
            position.setOffset(newOffset);
            position.setLength(end - newOffset);
          } else {
            // event consumes the position - delete it
            position.delete();
          }
        }
      } catch (BadPositionCategoryException e) {
        // ignore and return
      }
    }

  }

  /**
   * Borrowed from {@link CompilationUnitEditor.ExitPolicy}
   */
  private class GroovyExitPolicy implements IExitPolicy {

    final char fExitCharacter;
    final char fEscapeCharacter;
    final Stack<GroovyBracketLevel> fStack;
    final int fSize;

    public GroovyExitPolicy(char exitCharacter, char escapeCharacter,
        Stack<GroovyBracketLevel> stack) {
      fExitCharacter = exitCharacter;
      fEscapeCharacter = escapeCharacter;
      fStack = stack;
      fSize = fStack.size();
    }

    /*
     * @see
     * org.eclipse.jdt.internal.ui.text.link.LinkedPositionUI.ExitPolicy#doExit
     * (org.eclipse.jdt.internal.ui.text.link.LinkedPositionManager,
     * org.eclipse.swt.events.VerifyEvent, int, int)
     */
    public ExitFlags doExit(LinkedModeModel model, VerifyEvent event, int offset, int length) {

      if (fSize == fStack.size() && !isMasked(offset)) {
        if (event.character == fExitCharacter) {
          GroovyBracketLevel level = fStack.peek();
          if (level.fFirstPosition.offset > offset || level.fSecondPosition.offset < offset)
            return null;
          if (level.fSecondPosition.offset == offset && length == 0)
            // don't enter the character if if its the closing peer
            return new ExitFlags(ILinkedModeListener.UPDATE_CARET, false);
        }
        // when entering an anonymous class between the parenthesis', we don't
        // want
        // to jump after the closing parenthesis when return is pressed
        if (event.character == SWT.CR && offset > 0) {
          IDocument document = getSourceViewer().getDocument();
          try {
            if (document.getChar(offset - 1) == '{')
              return new ExitFlags(ILinkedModeListener.EXIT_ALL, true);
          } catch (BadLocationException e) {
          }
        }
      }
      return null;
    }

    private boolean isMasked(int offset) {
      IDocument document = getSourceViewer().getDocument();
      try {
        return fEscapeCharacter == document.getChar(offset - 1);
      } catch (BadLocationException e) {
      }
      return false;
    }

  }

  /**
   * Borrowed from {@link CompilationUnitEditor.BracketLevel}
   */
  private static class GroovyBracketLevel {
    LinkedModeUI fUI;
    Position fFirstPosition;
    Position fSecondPosition;
  }

  private boolean fCloseBrackets = true;

  private boolean fCloseBraces = true; // GROOVY change
  private boolean fCloseStrings = true;
  private boolean fCloseAngularBrackets = true;
  private final String CATEGORY = toString();
  private final IPositionUpdater fUpdater = new GroovyExclusivePositionUpdater(CATEGORY);
  private final Stack<GroovyBracketLevel> fBracketLevelStack = new Stack<GroovyBracketLevel>();
  private final SourceViewer viewer;

  public GroovyBracketInserter(SourceViewer viewer) {
    this.viewer = viewer;
  }

  public SourceViewer getSourceViewer() {
    return viewer;
  }

  public void setCloseBracketsEnabled(boolean enabled) {
    fCloseBrackets = enabled;
  }

  public void setCloseStringsEnabled(boolean enabled) {
    fCloseStrings = enabled;
  }

  /**
   * closing curly braces
   * 
   * @param enabled
   */
  public void setCloseBracesEnabled(boolean enabled) {
    fCloseBraces = enabled;
  }

  public void setCloseAngularBracketsEnabled(boolean enabled) {
    fCloseAngularBrackets = enabled;
  }

  private boolean isAngularIntroducer(String identifier) {
    return identifier.length() > 0
        && (Character.isUpperCase(identifier.charAt(0)) || identifier.startsWith("final") //$NON-NLS-1$
            || identifier.startsWith("public") //$NON-NLS-1$
            || identifier.startsWith("public") //$NON-NLS-1$
            || identifier.startsWith("protected") //$NON-NLS-1$
        || identifier.startsWith("private")); //$NON-NLS-1$
  }

  private boolean isMultilineSelection() {
    ISelection selection = getSourceViewer().getSelectionProvider().getSelection();
    if (selection instanceof ITextSelection) {
      ITextSelection ts = (ITextSelection) selection;
      return ts.getStartLine() != ts.getEndLine();
    }
    return false;
  }

  /*
   * @see
   * org.eclipse.swt.custom.VerifyKeyListener#verifyKey(org.eclipse.swt.events
   * .VerifyEvent)
   */
  public void verifyKey(VerifyEvent event) {

    // early pruning to slow down normal typing as little as possible
    if (!event.doit /*
                     * || getInsertMode() != SMART_INSERT ||
                     * isBlockSelectionModeEnabled()
                     */
        && isMultilineSelection())
      return;
    switch (event.character) {
    case '(':
    case '<':
    case '[':
    case '\'':
    case '\"':
      // GROOVY change. Allow autoclosing of curly braces in
      // GStrings
    case '{':
      // GROOVy end change
      break;
    default:
      return;
    }

    final ISourceViewer sourceViewer = getSourceViewer();
    IDocument document = sourceViewer.getDocument();

    final Point selection = sourceViewer.getSelectedRange();
    int offset = selection.x;
    final int length = selection.y;

    try {
      IRegion startLine = document.getLineInformationOfOffset(offset);
      IRegion endLine = document.getLineInformationOfOffset(offset + length);

      JavaHeuristicScanner scanner = new JavaHeuristicScanner(document);
      int nextToken = scanner.nextToken(offset + length, endLine.getOffset() + endLine.getLength());
      String next = nextToken == Symbols.TokenEOF ? null : document.get(offset,
          scanner.getPosition() - offset).trim();
      int prevToken = scanner.previousToken(offset - 1, startLine.getOffset());
      int prevTokenOffset = scanner.getPosition() + 1;
      String previous = prevToken == Symbols.TokenEOF ? null : document.get(prevTokenOffset,
          offset - prevTokenOffset).trim();

      switch (event.character) {
      case '(':
        if (!fCloseBrackets || nextToken == Symbols.TokenLPAREN || nextToken == Symbols.TokenIDENT
            || next != null && next.length() > 1)
          return;
        break;

      case '<':
        if (!(fCloseAngularBrackets && fCloseBrackets) || nextToken == Symbols.TokenLESSTHAN
            || prevToken != Symbols.TokenLBRACE && prevToken != Symbols.TokenRBRACE
            && prevToken != Symbols.TokenSEMICOLON && prevToken != Symbols.TokenSYNCHRONIZED
            && prevToken != Symbols.TokenSTATIC
            && (prevToken != Symbols.TokenIDENT || !isAngularIntroducer(previous))
            && prevToken != Symbols.TokenEOF)
          return;
        break;

      case '[':
        if (!fCloseBrackets || nextToken == Symbols.TokenIDENT || next != null && next.length() > 1)
          return;
        break;

      case '\'':
      case '"':
        // GROOVY change, allow quote closing when there are no parens
        if (!fCloseStrings || nextToken == Symbols.TokenIDENT
        // || prevToken == Symbols.TokenIDENT
            || next != null && next.length() > 1
        // || previous != null && previous.length() > 1
        )
          // GROOVY end change
          return;
        break;

      // GROOVY change, allow curly braces closing in GStrings
      case '{':
        if (!fCloseBraces || nextToken == Symbols.TokenIDENT || next != null && next.length() > 1)
          return;
        break;
      // GROOVY end change
      default:
        return;
      }

      ITypedRegion partition = TextUtilities.getPartition(document,
          IJavaPartitions.JAVA_PARTITIONING, offset, true);
      if (event.character != '{' && !IDocument.DEFAULT_CONTENT_TYPE.equals(partition.getType()) && // original
          // GROOVY change autoclose triple quotes
          !shouldCloseTripleQuotes(document, offset, partition, getPeerCharacter(event.character))) { // GROOVY
        // change
        return;
      }

      // GROOVY change check for autoclose curly braces
      if (event.character == '{'
          && !shouldCloseCurly(document, offset, partition, getPeerCharacter(event.character))) {
        return;
      }

      // if (!validateEditorInputState())
      // return;

      final char character = event.character;
      final char closingCharacter = getPeerCharacter(character);
      final StringBuffer buffer = new StringBuffer();
      buffer.append(character);
      buffer.append(closingCharacter);

      // GROOVY special case: multiline strings
      // Insert the closing of a triple quoted string whenever
      // there is a "" or a """ before
      int insertedLength = 1;
      if (fCloseStrings && offset > 1) {
        String start = document.get(offset - 2, 2);
        boolean doit = false;
        if (event.character == closingCharacter) {
          doit = start.equals(Character.toString(closingCharacter) + closingCharacter);
        }
        if (doit) {
          buffer.append(closingCharacter);
          insertedLength++;
          // now check for a preexisting third quote
          insertedLength++;
          if (offset > 2 && document.getChar(offset - 3) == closingCharacter) {
            offset--;
          } else {
            // if no third quote already, must add another
            buffer.append(closingCharacter);
          }
        }
      }
      // GROOVY end

      document.replace(offset, length, buffer.toString());

      GroovyBracketLevel level = new GroovyBracketLevel();
      fBracketLevelStack.push(level);

      LinkedPositionGroup group = new LinkedPositionGroup();
      // group.addPosition(new LinkedPosition(document, offset + 1, 0,
      // LinkedPositionGroup.NO_STOP));
      group.addPosition(new LinkedPosition(document, offset + insertedLength, 0,
          LinkedPositionGroup.NO_STOP)); // GROOVY change

      LinkedModeModel model = new LinkedModeModel();
      model.addLinkingListener(this);
      model.addGroup(group);
      model.forceInstall();

      // set up position tracking for our magic peers
      if (fBracketLevelStack.size() == 1) {
        document.addPositionCategory(CATEGORY);
        document.addPositionUpdater(fUpdater);
      }
      level.fFirstPosition = new Position(offset, 1);
      // level.fSecondPosition= new Position(offset + 1, 1);
      level.fSecondPosition = new Position(offset + insertedLength, 1); // GROOVY
                                                                        // change
      document.addPosition(CATEGORY, level.fFirstPosition);
      document.addPosition(CATEGORY, level.fSecondPosition);

      level.fUI = new EditorLinkedModeUI(model, sourceViewer);
      level.fUI.setSimpleMode(true);
      level.fUI.setExitPolicy(new GroovyExitPolicy(closingCharacter,
          getEscapeCharacter(closingCharacter), fBracketLevelStack));
      // level.fUI.setExitPosition(sourceViewer, offset + 2, 0,
      // Integer.MAX_VALUE);
      level.fUI.setExitPosition(sourceViewer, offset + 1 + insertedLength, 0, Integer.MAX_VALUE); // GROOVY
                                                                                                  // change
      level.fUI.setCyclingMode(LinkedModeUI.CYCLE_NEVER);
      level.fUI.enter();

      IRegion newSelection = level.fUI.getSelectedRegion();
      // sourceViewer.setSelectedRange(newSelection.getOffset(),
      // newSelection.getLength());
      sourceViewer.setSelectedRange(newSelection.getOffset() - insertedLength + 1,
          newSelection.getLength()); // GROOVY change

      event.doit = false;

    } catch (BadLocationException e) {
      JavaPlugin.log(e);
    } catch (BadPositionCategoryException e) {
      JavaPlugin.log(e);
    }
  }

  // copied to make accessible from super-class
  private static char getEscapeCharacter(char character) {
    switch (character) {
    case '"':
    case '\'':
      return '\\';
    default:
      return 0;
    }
  }

  // copied to make accessible from super-class
  private static char getPeerCharacter(char character) {
    switch (character) {
    case '(':
      return ')';

    case ')':
      return '(';

    case '<':
      return '>';

    case '>':
      return '<';

    case '[':
      return ']';

    case ']':
      return '[';

    case '"':
      return character;

    case '\'':
      return character;

      // GROOVY change
    case '{':
      return '}';
      // GROOVY change end

    default:
      throw new IllegalArgumentException();
    }
  }

  /**
   * GROOVY change
   * 
   * @param document
   * @param offset
   * @param partition
   * @param peer
   * @return true iff we should be closing a curly bracket. Only happens as part
   *         of a GString
   * @throws BadLocationException
   */
  private boolean shouldCloseCurly(IDocument document, int offset, ITypedRegion partition, char peer)
      throws BadLocationException {
    if (offset < 2
        || !(JavaPartitionScanner.JAVA_STRING.equals(partition.getType()) || GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS
            .equals(partition.getType()))) {
      return false;
    }

    char maybeOpen = document.getChar(offset - 1);
    if (maybeOpen != '$') {
      return false;
    }

    char maybeNext = document.getChar(offset);
    return Character.isWhitespace(maybeNext) || maybeNext == '\"' || maybeNext == '\'';
  }

  /**
   * GROOVY change
   * 
   * @param document
   * @param offset
   * @param partition
   * @param quote
   * @return true if we are at a position of triple quotes
   * @throws BadLocationException
   */
  private boolean shouldCloseTripleQuotes(IDocument document, int offset, ITypedRegion partition,
      char quote) throws BadLocationException {
    if (offset < 3 || GroovyPartitionScanner.GROOVY_MULTILINE_STRINGS.equals(partition.getType())) {
      return false;
    }
    String maybequotes = document.get(offset - 3, 3);
    return maybequotes.equals(Character.toString(quote) + quote + quote);
  }

  /*
   * @see
   * org.eclipse.jface.text.link.ILinkedModeListener#left(org.eclipse.jface.
   * text.link.LinkedModeModel, int)
   */
  public void left(LinkedModeModel environment, int flags) {

    final GroovyBracketLevel level = fBracketLevelStack.pop();

    if (flags != ILinkedModeListener.EXTERNAL_MODIFICATION)
      return;

    // remove brackets
    final ISourceViewer sourceViewer = getSourceViewer();
    final IDocument document = sourceViewer.getDocument();
    if (document instanceof IDocumentExtension) {
      IDocumentExtension extension = (IDocumentExtension) document;
      extension.registerPostNotificationReplace(null, new IDocumentExtension.IReplace() {

        public void perform(IDocument d, IDocumentListener owner) {
          if ((level.fFirstPosition.isDeleted || level.fFirstPosition.length == 0)
              && !level.fSecondPosition.isDeleted
              && level.fSecondPosition.offset == level.fFirstPosition.offset) {
            try {
              document.replace(level.fSecondPosition.offset, level.fSecondPosition.length, ""); //$NON-NLS-1$
            } catch (BadLocationException e) {
              JavaPlugin.log(e);
            }
          }

          if (fBracketLevelStack.size() == 0) {
            document.removePositionUpdater(fUpdater);
            try {
              document.removePositionCategory(CATEGORY);
            } catch (BadPositionCategoryException e) {
              JavaPlugin.log(e);
            }
          }
        }
      });
    }

  }

  /*
   * @see
   * org.eclipse.jface.text.link.ILinkedModeListener#suspend(org.eclipse.jface
   * .text.link.LinkedModeModel)
   */
  public void suspend(LinkedModeModel environment) {
  }

  /*
   * @see
   * org.eclipse.jface.text.link.ILinkedModeListener#resume(org.eclipse.jface
   * .text.link.LinkedModeModel, int)
   */
  public void resume(LinkedModeModel environment, int flags) {
  }
}
