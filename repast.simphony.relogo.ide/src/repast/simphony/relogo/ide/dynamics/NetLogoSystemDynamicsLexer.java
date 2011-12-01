// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g 2008-07-16 19:32:37

package repast.simphony.relogo.ide.dynamics;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class NetLogoSystemDynamicsLexer extends Lexer {
    public static final int INTG=8;
    public static final int STR=7;
    public static final int WS=14;
    public static final int NEWLINE=12;
    public static final int MODEL=4;
    public static final int FLOAT=11;
    public static final int INT=16;
    public static final int ENTRY=5;
    public static final int ID=13;
    public static final int Tokens=17;
    public static final int EOF=-1;
    public static final int FLT=9;
    public static final int INDENT=10;
    public static final int REF=6;
    public static final int STRING=15;
    public NetLogoSystemDynamicsLexer() {;} 
    public NetLogoSystemDynamicsLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g"; }

    // $ANTLR start ID
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:42:6: ( ( ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )* ) ( '.' ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )* )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:42:10: ( ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )* ) ( '.' ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )* )*
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:42:10: ( ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:42:11: ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )*
            {
            if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||input.LA(1)=='?'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:42:54: ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='!'||(LA1_0>='#' && LA1_0<='%')||LA1_0=='-'||(LA1_0>='0' && LA1_0<=':')||LA1_0=='?'||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:
            	    {
            	    if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<=':')||input.LA(1)=='?'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            	    break loop1;
                }
            } while (true);


            }

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:43:17: ( '.' ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )* )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='.') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:43:18: '.' ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )*
            	    {
            	    match('.'); 
            	    if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||input.LA(1)=='?'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }

            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:43:64: ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '_' | ':' | '0' .. '9' )*
            	    loop2:
            	    do {
            	        int alt2=2;
            	        int LA2_0 = input.LA(1);

            	        if ( (LA2_0=='!'||(LA2_0>='#' && LA2_0<='%')||LA2_0=='-'||(LA2_0>='0' && LA2_0<=':')||LA2_0=='?'||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
            	            alt2=1;
            	        }


            	        switch (alt2) {
            	    	case 1 :
            	    	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:
            	    	    {
            	    	    if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<=':')||input.LA(1)=='?'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            	    	    break loop2;
            	        }
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ID

    // $ANTLR start STRING
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:44:8: ( '\"' (~ '\"' | '\\\\\"' )* '\"' )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:44:10: '\"' (~ '\"' | '\\\\\"' )* '\"'
            {
            match('\"'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:44:14: (~ '\"' | '\\\\\"' )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='\\') ) {
                    int LA4_2 = input.LA(2);

                    if ( (LA4_2=='\"') ) {
                        int LA4_4 = input.LA(3);

                        if ( ((LA4_4>='\u0000' && LA4_4<='\uFFFE')) ) {
                            alt4=2;
                        }

                        else {
                            alt4=1;
                        }

                    }
                    else if ( ((LA4_2>='\u0000' && LA4_2<='!')||(LA4_2>='#' && LA4_2<='\uFFFE')) ) {
                        alt4=1;
                    }


                }
                else if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='[')||(LA4_0>=']' && LA4_0<='\uFFFE')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:44:15: ~ '\"'
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
            	case 2 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:44:22: '\\\\\"'
            	    {
            	    match("\\\""); 


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            match('\"'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING

    // $ANTLR start INT
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:45:6: ( ( '-' | '+' )? ( '0' .. '9' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:45:8: ( '-' | '+' )? ( '0' .. '9' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:45:8: ( '-' | '+' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='+'||LA5_0=='-') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:45:18: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:45:19: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INT

    // $ANTLR start FLOAT
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:8: ( ( '-' | '+' )? ( '0' .. '9' )* ( '.' ( '0' .. '9' )* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:11: ( '-' | '+' )? ( '0' .. '9' )* ( '.' ( '0' .. '9' )* )
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:11: ( '-' | '+' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='+'||LA7_0=='-') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:21: ( '0' .. '9' )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:22: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:33: ( '.' ( '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:34: '.' ( '0' .. '9' )*
            {
            match('.'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:38: ( '0' .. '9' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:46:38: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FLOAT

    // $ANTLR start NEWLINE
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:48:9: ( ( '\\r' )? '\\n' )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:48:11: ( '\\r' )? '\\n'
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:48:11: ( '\\r' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\r') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:48:12: '\\r'
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

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:49:4: ( ( ' ' | '\\t' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:49:6: ( ' ' | '\\t' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:49:6: ( ' ' | '\\t' )+
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
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:
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


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WS

    public void mTokens() throws RecognitionException {
        // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:8: ( ID | STRING | INT | FLOAT | NEWLINE | WS )
        int alt12=6;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:10: ID
                {
                mID(); 

                }
                break;
            case 2 :
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:13: STRING
                {
                mSTRING(); 

                }
                break;
            case 3 :
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:20: INT
                {
                mINT(); 

                }
                break;
            case 4 :
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:24: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 5 :
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:30: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 6 :
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:1:38: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\4\uffff\1\10\4\uffff";
    static final String DFA12_eofS =
        "\11\uffff";
    static final String DFA12_minS =
        "\1\11\2\uffff\2\56\4\uffff";
    static final String DFA12_maxS =
        "\1\172\2\uffff\2\71\4\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\1\2\2\uffff\1\4\1\5\1\6\1\3";
    static final String DFA12_specialS =
        "\11\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\7\1\6\2\uffff\1\6\22\uffff\1\7\1\1\1\2\3\1\5\uffff\1\3\1"+
            "\uffff\1\3\1\5\1\uffff\12\4\5\uffff\1\1\1\uffff\32\1\4\uffff"+
            "\1\1\1\uffff\32\1",
            "",
            "",
            "\1\5\1\uffff\12\4",
            "\1\5\1\uffff\12\4",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ID | STRING | INT | FLOAT | NEWLINE | WS );";
        }
    }
 

}