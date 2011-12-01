// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoInterface.g 2009-03-12 20:33:59

package repast.simphony.relogo.ide.intf;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class NetLogoInterfaceLexer extends Lexer {
    public static final int GRAPHIC_CHAR=9;
    public static final int QUOTATION=5;
    public static final int NUMBER=7;
    public static final int FLOAT=8;
    public static final int Tokens=25;
    public static final int T24=24;
    public static final int EOF=-1;
    public static final int T23=23;
    public static final int T22=22;
    public static final int T21=21;
    public static final int T20=20;
    public static final int NAME=6;
    public static final int WS=10;
    public static final int NEWLINE=4;
    public static final int T11=11;
    public static final int T12=12;
    public static final int T13=13;
    public static final int T14=14;
    public static final int T15=15;
    public static final int T16=16;
    public static final int T17=17;
    public static final int T18=18;
    public static final int T19=19;
    public NetLogoInterfaceLexer() {;} 
    public NetLogoInterfaceLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoInterface.g"; }

    // $ANTLR start T11
    public final void mT11() throws RecognitionException {
        try {
            int _type = T11;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:6:5: ( 'TEXTBOX' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:6:7: 'TEXTBOX'
            {
            match("TEXTBOX"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T11

    // $ANTLR start T12
    public final void mT12() throws RecognitionException {
        try {
            int _type = T12;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:7:5: ( 'SLIDER' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:7:7: 'SLIDER'
            {
            match("SLIDER"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T12

    // $ANTLR start T13
    public final void mT13() throws RecognitionException {
        try {
            int _type = T13;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:8:5: ( 'BUTTON' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:8:7: 'BUTTON'
            {
            match("BUTTON"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T13

    // $ANTLR start T14
    public final void mT14() throws RecognitionException {
        try {
            int _type = T14;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:9:5: ( 'CHOOSER' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:9:7: 'CHOOSER'
            {
            match("CHOOSER"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T14

    // $ANTLR start T15
    public final void mT15() throws RecognitionException {
        try {
            int _type = T15;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:10:5: ( 'SWITCH' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:10:7: 'SWITCH'
            {
            match("SWITCH"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T15

    // $ANTLR start T16
    public final void mT16() throws RecognitionException {
        try {
            int _type = T16;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:11:5: ( 'OUTPUT' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:11:7: 'OUTPUT'
            {
            match("OUTPUT"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T16

    // $ANTLR start T17
    public final void mT17() throws RecognitionException {
        try {
            int _type = T17;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:12:5: ( 'PLOT' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:12:7: 'PLOT'
            {
            match("PLOT"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T17

    // $ANTLR start T18
    public final void mT18() throws RecognitionException {
        try {
            int _type = T18;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:13:5: ( 'PENS' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:13:7: 'PENS'
            {
            match("PENS"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T18

    // $ANTLR start T19
    public final void mT19() throws RecognitionException {
        try {
            int _type = T19;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:14:5: ( 'MONITOR' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:14:7: 'MONITOR'
            {
            match("MONITOR"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T19

    // $ANTLR start T20
    public final void mT20() throws RecognitionException {
        try {
            int _type = T20;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:15:5: ( 'INPUTBOX' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:15:7: 'INPUTBOX'
            {
            match("INPUTBOX"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T20

    // $ANTLR start T21
    public final void mT21() throws RecognitionException {
        try {
            int _type = T21;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:16:5: ( 'CC-WINDOW' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:16:7: 'CC-WINDOW'
            {
            match("CC-WINDOW"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T21

    // $ANTLR start T22
    public final void mT22() throws RecognitionException {
        try {
            int _type = T22;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:17:5: ( 'GRAPHICS-WINDOW' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:17:7: 'GRAPHICS-WINDOW'
            {
            match("GRAPHICS-WINDOW"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T22

    // $ANTLR start T23
    public final void mT23() throws RecognitionException {
        try {
            int _type = T23;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:18:5: ( 'true' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:18:7: 'true'
            {
            match("true"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T23

    // $ANTLR start T24
    public final void mT24() throws RecognitionException {
        try {
            int _type = T24;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:19:5: ( 'false' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:19:7: 'false'
            {
            match("false"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T24

    // $ANTLR start GRAPHIC_CHAR
    public final void mGRAPHIC_CHAR() throws RecognitionException {
        try {
            int _type = GRAPHIC_CHAR;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:62:2: ( '!' | '@' | '#' | '$' | '%' | '^' | '&' | '*' | '(' | ')' | '-' | '_' | '+' | '=' | '{' | '[' | '}' | ']' | ':' | ';' | '\\'' | '\"' | ',' | '.' | '<' | '>' | '/' | '?' | '\\\\' | '|' | '~' | '`' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:
            {
            if ( (input.LA(1)>='!' && input.LA(1)<='/')||(input.LA(1)>=':' && input.LA(1)<='@')||(input.LA(1)>='[' && input.LA(1)<='`')||(input.LA(1)>='{' && input.LA(1)<='~') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GRAPHIC_CHAR

    // $ANTLR start NUMBER
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:64:9: ( ( '-' )? ( '0' .. '9' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:64:11: ( '-' )? ( '0' .. '9' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:64:11: ( '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:64:12: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:64:18: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:64:20: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NUMBER

    // $ANTLR start FLOAT
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:8: ( ( '-' )? ( '0' .. '9' )* '.' ( '0' .. '9' )* ( 'E' ( '-' )? ( '0' .. '9' )+ )? )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:10: ( '-' )? ( '0' .. '9' )* '.' ( '0' .. '9' )* ( 'E' ( '-' )? ( '0' .. '9' )+ )?
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:10: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:11: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:17: ( '0' .. '9' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:19: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('.'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:37: ( '0' .. '9' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:38: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:51: ( 'E' ( '-' )? ( '0' .. '9' )+ )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='E') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:52: 'E' ( '-' )? ( '0' .. '9' )+
                    {
                    match('E'); 
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:56: ( '-' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0=='-') ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:57: '-'
                            {
                            match('-'); 

                            }
                            break;

                    }

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:63: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:66:64: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    }
                    break;

            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FLOAT

    // $ANTLR start QUOTATION
    public final void mQUOTATION() throws RecognitionException {
        try {
            int _type = QUOTATION;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:69:2: ( '\"' (~ '\"' )* '\"' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:69:4: '\"' (~ '\"' )* '\"'
            {
            match('\"'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:69:8: (~ '\"' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='\u0000' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='\uFFFE')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:69:9: ~ '\"'
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match('\"'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end QUOTATION

    // $ANTLR start NAME
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:72:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:72:7: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:73:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='-'||(LA10_0>='0' && LA10_0<='9')||(LA10_0>='A' && LA10_0<='Z')||LA10_0=='_'||(LA10_0>='a' && LA10_0<='z')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NAME

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:76:4: ( ( ' ' | '\\t' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:76:6: ( ' ' | '\\t' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:76:6: ( ' ' | '\\t' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='\t'||LA11_0==' ') ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);

             channel = HIDDEN; 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    // $ANTLR start NEWLINE
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:78:9: ( ( '\\r' )? '\\n' )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:78:11: ( '\\r' )? '\\n'
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:78:11: ( '\\r' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\r') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:78:12: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NEWLINE

    public void mTokens() throws RecognitionException {
        // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:8: ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | GRAPHIC_CHAR | NUMBER | FLOAT | QUOTATION | NAME | WS | NEWLINE )
        int alt13=21;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:10: T11
                {
                mT11(); 

                }
                break;
            case 2 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:14: T12
                {
                mT12(); 

                }
                break;
            case 3 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:18: T13
                {
                mT13(); 

                }
                break;
            case 4 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:22: T14
                {
                mT14(); 

                }
                break;
            case 5 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:26: T15
                {
                mT15(); 

                }
                break;
            case 6 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:30: T16
                {
                mT16(); 

                }
                break;
            case 7 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:34: T17
                {
                mT17(); 

                }
                break;
            case 8 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:38: T18
                {
                mT18(); 

                }
                break;
            case 9 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:42: T19
                {
                mT19(); 

                }
                break;
            case 10 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:46: T20
                {
                mT20(); 

                }
                break;
            case 11 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:50: T21
                {
                mT21(); 

                }
                break;
            case 12 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:54: T22
                {
                mT22(); 

                }
                break;
            case 13 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:58: T23
                {
                mT23(); 

                }
                break;
            case 14 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:62: T24
                {
                mT24(); 

                }
                break;
            case 15 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:66: GRAPHIC_CHAR
                {
                mGRAPHIC_CHAR(); 

                }
                break;
            case 16 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:79: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 17 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:86: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 18 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:92: QUOTATION
                {
                mQUOTATION(); 

                }
                break;
            case 19 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:102: NAME
                {
                mNAME(); 

                }
                break;
            case 20 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:107: WS
                {
                mWS(); 

                }
                break;
            case 21 :
                // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:1:110: NEWLINE
                {
                mNEWLINE(); 

                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\1\uffff\13\21\2\20\1\43\1\20\4\uffff\16\21\3\uffff\25\21\1\110"+
        "\1\111\3\21\1\115\10\21\2\uffff\3\21\1\uffff\1\131\1\21\1\133\1"+
        "\134\1\135\2\21\1\140\3\21\1\uffff\1\144\3\uffff\1\145\1\21\1\uffff"+
        "\1\147\2\21\2\uffff\1\21\1\uffff\1\153\1\21\1\155\1\uffff\1\21\1"+
        "\uffff\5\21\1\164\1\uffff";
    static final String DFA13_eofS =
        "\165\uffff";
    static final String DFA13_minS =
        "\1\11\1\105\1\114\1\125\1\103\1\125\1\105\1\117\1\116\1\122\1\162"+
        "\1\141\1\56\1\60\1\56\1\0\4\uffff\1\130\2\111\1\124\1\117\1\55\1"+
        "\124\1\116\1\117\1\116\1\120\1\101\1\165\1\154\3\uffff\1\124\1\104"+
        "\2\124\1\117\1\127\1\120\1\123\1\124\1\111\1\125\1\120\1\145\1\163"+
        "\1\102\1\105\1\103\1\117\1\123\1\111\1\125\2\55\2\124\1\110\1\55"+
        "\1\145\1\117\1\122\1\110\1\116\1\105\1\116\1\124\2\uffff\1\117\1"+
        "\102\1\111\1\uffff\1\55\1\130\3\55\1\122\1\104\1\55\1\122\1\117"+
        "\1\103\1\uffff\1\55\3\uffff\1\55\1\117\1\uffff\1\55\1\130\1\123"+
        "\2\uffff\1\127\1\uffff\3\55\1\uffff\1\127\1\uffff\1\111\1\116\1"+
        "\104\1\117\1\127\1\55\1\uffff";
    static final String DFA13_maxS =
        "\1\176\1\105\1\127\1\125\1\110\1\125\1\114\1\117\1\116\1\122\1\162"+
        "\1\141\1\71\1\105\1\71\1\ufffe\4\uffff\1\130\2\111\1\124\1\117\1"+
        "\55\1\124\1\116\1\117\1\116\1\120\1\101\1\165\1\154\3\uffff\1\124"+
        "\1\104\2\124\1\117\1\127\1\120\1\123\1\124\1\111\1\125\1\120\1\145"+
        "\1\163\1\102\1\105\1\103\1\117\1\123\1\111\1\125\2\172\2\124\1\110"+
        "\1\172\1\145\1\117\1\122\1\110\1\116\1\105\1\116\1\124\2\uffff\1"+
        "\117\1\102\1\111\1\uffff\1\172\1\130\3\172\1\122\1\104\1\172\1\122"+
        "\1\117\1\103\1\uffff\1\172\3\uffff\1\172\1\117\1\uffff\1\172\1\130"+
        "\1\123\2\uffff\1\127\1\uffff\1\172\1\55\1\172\1\uffff\1\127\1\uffff"+
        "\1\111\1\116\1\104\1\117\1\127\1\172\1\uffff";
    static final String DFA13_acceptS =
        "\20\uffff\1\17\1\23\1\24\1\25\16\uffff\1\21\1\20\1\22\43\uffff\1"+
        "\10\1\7\3\uffff\1\15\13\uffff\1\16\1\uffff\1\2\1\5\1\3\2\uffff\1"+
        "\6\3\uffff\1\1\1\4\1\uffff\1\11\3\uffff\1\12\1\uffff\1\13\6\uffff"+
        "\1\14";
    static final String DFA13_specialS =
        "\165\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\22\1\23\2\uffff\1\23\22\uffff\1\22\1\20\1\17\12\20\1\14\1"+
            "\15\1\20\12\16\7\20\1\21\1\3\1\4\3\21\1\11\1\21\1\10\3\21\1"+
            "\7\1\21\1\5\1\6\2\21\1\2\1\1\6\21\6\20\5\21\1\13\15\21\1\12"+
            "\6\21\4\20",
            "\1\24",
            "\1\25\12\uffff\1\26",
            "\1\27",
            "\1\31\4\uffff\1\30",
            "\1\32",
            "\1\33\6\uffff\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42\1\uffff\12\16",
            "\12\42\13\uffff\1\42",
            "\1\42\1\uffff\12\16",
            "\uffff\44",
            "",
            "",
            "",
            "",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "\1\53",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "",
            "",
            "",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "",
            "",
            "\1\126",
            "\1\127",
            "\1\130",
            "",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\132",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\136",
            "\1\137",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\141",
            "\1\142",
            "\1\143",
            "",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "",
            "",
            "",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\146",
            "",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\150",
            "\1\151",
            "",
            "",
            "\1\152",
            "",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "\1\154",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            "",
            "\1\156",
            "",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\21\2\uffff\12\21\7\uffff\32\21\4\uffff\1\21\1\uffff\32\21",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T11 | T12 | T13 | T14 | T15 | T16 | T17 | T18 | T19 | T20 | T21 | T22 | T23 | T24 | GRAPHIC_CHAR | NUMBER | FLOAT | QUOTATION | NAME | WS | NEWLINE );";
        }
    }
 

}