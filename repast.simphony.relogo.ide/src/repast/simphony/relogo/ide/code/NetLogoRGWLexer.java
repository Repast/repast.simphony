// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoRGW.g 2008-07-23 16:47:55

package repast.simphony.relogo.ide.code;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class NetLogoRGWLexer extends Lexer {
    public static final int GLOBALS=10;
    public static final int ARITHSYM=20;
    public static final int FNUM=13;
    public static final int CB=6;
    public static final int BREED=9;
    public static final int ToEndBlock=15;
    public static final int T29=29;
    public static final int FLOAT=22;
    public static final int INT=21;
    public static final int T28=28;
    public static final int ToReportBlock=16;
    public static final int T27=27;
    public static final int T26=26;
    public static final int ID=18;
    public static final int Tokens=36;
    public static final int EOF=-1;
    public static final int IDN=7;
    public static final int INCLUDES=11;
    public static final int STR=8;
    public static final int WS=25;
    public static final int OWN=14;
    public static final int NEWLINE=24;
    public static final int INUM=12;
    public static final int CS=5;
    public static final int Paren=17;
    public static final int ARGS=4;
    public static final int T34=34;
    public static final int COMMENT=23;
    public static final int T33=33;
    public static final int T35=35;
    public static final int T30=30;
    public static final int T32=32;
    public static final int STRING=19;
    public static final int T31=31;
    public NetLogoRGWLexer() {;} 
    public NetLogoRGWLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoRGW.g"; }

    // $ANTLR start T26
    public final void mT26() throws RecognitionException {
        try {
            int _type = T26;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:6:5: ( 'breed' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:6:7: 'breed'
            {
            match("breed"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T26

    // $ANTLR start T27
    public final void mT27() throws RecognitionException {
        try {
            int _type = T27;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:7:5: ( '[' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:7:7: '['
            {
            match('['); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T27

    // $ANTLR start T28
    public final void mT28() throws RecognitionException {
        try {
            int _type = T28;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:8:5: ( ']' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:8:7: ']'
            {
            match(']'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T28

    // $ANTLR start T29
    public final void mT29() throws RecognitionException {
        try {
            int _type = T29;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:9:5: ( 'globals' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:9:7: 'globals'
            {
            match("globals"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T29

    // $ANTLR start T30
    public final void mT30() throws RecognitionException {
        try {
            int _type = T30;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:10:5: ( '__includes' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:10:7: '__includes'
            {
            match("__includes"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T30

    // $ANTLR start T31
    public final void mT31() throws RecognitionException {
        try {
            int _type = T31;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:11:5: ( 'to' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:11:7: 'to'
            {
            match("to"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T31

    // $ANTLR start T32
    public final void mT32() throws RecognitionException {
        try {
            int _type = T32;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:12:5: ( 'end' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:12:7: 'end'
            {
            match("end"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T32

    // $ANTLR start T33
    public final void mT33() throws RecognitionException {
        try {
            int _type = T33;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:13:5: ( 'to-report' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:13:7: 'to-report'
            {
            match("to-report"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T33

    // $ANTLR start T34
    public final void mT34() throws RecognitionException {
        try {
            int _type = T34;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:14:5: ( '(' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:14:7: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T34

    // $ANTLR start T35
    public final void mT35() throws RecognitionException {
        try {
            int _type = T35;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:15:5: ( ')' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:15:7: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T35

    // $ANTLR start ID
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:76:6: ( ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '+' | '*' | '/' | '<' | '>' | '_' | ':' | '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:76:10: ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '+' | '*' | '/' | '<' | '>' | '_' | ':' | '0' .. '9' )*
            {
            if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||input.LA(1)=='?'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:76:53: ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '+' | '*' | '/' | '<' | '>' | '_' | ':' | '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='!'||(LA1_0>='#' && LA1_0<='%')||(LA1_0>='*' && LA1_0<='+')||LA1_0=='-'||(LA1_0>='/' && LA1_0<=':')||LA1_0=='<'||(LA1_0>='>' && LA1_0<='?')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:
            	    {
            	    if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||(input.LA(1)>='*' && input.LA(1)<='+')||input.LA(1)=='-'||(input.LA(1)>='/' && input.LA(1)<=':')||input.LA(1)=='<'||(input.LA(1)>='>' && input.LA(1)<='?')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:77:8: ( '\"' (~ '\"' | '\\\\\"' )* '\"' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:77:10: '\"' (~ '\"' | '\\\\\"' )* '\"'
            {
            match('\"'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:77:14: (~ '\"' | '\\\\\"' )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\\') ) {
                    int LA2_2 = input.LA(2);

                    if ( (LA2_2=='\"') ) {
                        int LA2_4 = input.LA(3);

                        if ( ((LA2_4>='\u0000' && LA2_4<='\uFFFE')) ) {
                            alt2=2;
                        }

                        else {
                            alt2=1;
                        }

                    }
                    else if ( ((LA2_2>='\u0000' && LA2_2<='!')||(LA2_2>='#' && LA2_2<='\uFFFE')) ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='[')||(LA2_0>=']' && LA2_0<='\uFFFE')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:77:15: ~ '\"'
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
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:77:22: '\\\\\"'
            	    {
            	    match("\\\""); 


            	    }
            	    break;

            	default :
            	    break loop2;
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

    // $ANTLR start ARITHSYM
    public final void mARITHSYM() throws RecognitionException {
        try {
            int _type = ARITHSYM;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:9: ( ( '-' | '+' | '/' | '<' | '>' | '<=' | '>=' | '*' | '=' | '!=' | '^' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '+' | '*' | '/' | '<' | '>' | '_' | ':' | '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:11: ( '-' | '+' | '/' | '<' | '>' | '<=' | '>=' | '*' | '=' | '!=' | '^' ) ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '+' | '*' | '/' | '<' | '>' | '_' | ':' | '0' .. '9' )*
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:11: ( '-' | '+' | '/' | '<' | '>' | '<=' | '>=' | '*' | '=' | '!=' | '^' )
            int alt3=11;
            switch ( input.LA(1) ) {
            case '-':
                {
                alt3=1;
                }
                break;
            case '+':
                {
                alt3=2;
                }
                break;
            case '/':
                {
                alt3=3;
                }
                break;
            case '<':
                {
                int LA3_4 = input.LA(2);

                if ( (LA3_4=='=') ) {
                    alt3=6;
                }
                else {
                    alt3=4;}
                }
                break;
            case '>':
                {
                int LA3_5 = input.LA(2);

                if ( (LA3_5=='=') ) {
                    alt3=7;
                }
                else {
                    alt3=5;}
                }
                break;
            case '*':
                {
                alt3=8;
                }
                break;
            case '=':
                {
                alt3=9;
                }
                break;
            case '!':
                {
                alt3=10;
                }
                break;
            case '^':
                {
                alt3=11;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("78:11: ( '-' | '+' | '/' | '<' | '>' | '<=' | '>=' | '*' | '=' | '!=' | '^' )", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:12: '-'
                    {
                    match('-'); 

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:16: '+'
                    {
                    match('+'); 

                    }
                    break;
                case 3 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:20: '/'
                    {
                    match('/'); 

                    }
                    break;
                case 4 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:24: '<'
                    {
                    match('<'); 

                    }
                    break;
                case 5 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:28: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 6 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:32: '<='
                    {
                    match("<="); 


                    }
                    break;
                case 7 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:37: '>='
                    {
                    match(">="); 


                    }
                    break;
                case 8 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:42: '*'
                    {
                    match('*'); 

                    }
                    break;
                case 9 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:46: '='
                    {
                    match('='); 

                    }
                    break;
                case 10 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:50: '!='
                    {
                    match("!="); 


                    }
                    break;
                case 11 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:55: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:78:59: ( 'a' .. 'z' | 'A' .. 'Z' | '?' | '!' | '#' | '$' | '%' | '-' | '+' | '*' | '/' | '<' | '>' | '_' | ':' | '0' .. '9' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='!'||(LA4_0>='#' && LA4_0<='%')||(LA4_0>='*' && LA4_0<='+')||LA4_0=='-'||(LA4_0>='/' && LA4_0<=':')||LA4_0=='<'||(LA4_0>='>' && LA4_0<='?')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:
            	    {
            	    if ( input.LA(1)=='!'||(input.LA(1)>='#' && input.LA(1)<='%')||(input.LA(1)>='*' && input.LA(1)<='+')||input.LA(1)=='-'||(input.LA(1)>='/' && input.LA(1)<=':')||input.LA(1)=='<'||(input.LA(1)>='>' && input.LA(1)<='?')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
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
            	    break loop4;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ARITHSYM

    // $ANTLR start INT
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:79:6: ( ( '-' | '+' )? ( '0' .. '9' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:79:8: ( '-' | '+' )? ( '0' .. '9' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:79:8: ( '-' | '+' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='+'||LA5_0=='-') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:
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

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:79:18: ( '0' .. '9' )+
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
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:79:19: '0' .. '9'
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
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:8: ( ( '-' | '+' )? ( '0' .. '9' )* ( '.' ( '0' .. '9' )* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:11: ( '-' | '+' )? ( '0' .. '9' )* ( '.' ( '0' .. '9' )* )
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:11: ( '-' | '+' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='+'||LA7_0=='-') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:
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

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:21: ( '0' .. '9' )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:22: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:33: ( '.' ( '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:34: '.' ( '0' .. '9' )*
            {
            match('.'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:38: ( '0' .. '9' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:80:38: '0' .. '9'
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

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:82:8: ( ';' ( . )* '\\n' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:82:10: ';' ( . )* '\\n'
            {
            match(';'); 
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:82:14: ( . )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\n') ) {
                    alt10=2;
                }
                else if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\uFFFE')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:82:14: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match('\n'); 
             channel = HIDDEN; 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start NEWLINE
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:83:8: ( '\\n' )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:83:10: '\\n'
            {
            match('\n'); 
             channel = HIDDEN; 

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
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:84:4: ( ( ' ' | '\\t' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:84:6: ( ' ' | '\\t' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:84:6: ( ' ' | '\\t' )+
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
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:
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

    public void mTokens() throws RecognitionException {
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:8: ( T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | ID | STRING | ARITHSYM | INT | FLOAT | COMMENT | NEWLINE | WS )
        int alt12=18;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:10: T26
                {
                mT26(); 

                }
                break;
            case 2 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:14: T27
                {
                mT27(); 

                }
                break;
            case 3 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:18: T28
                {
                mT28(); 

                }
                break;
            case 4 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:22: T29
                {
                mT29(); 

                }
                break;
            case 5 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:26: T30
                {
                mT30(); 

                }
                break;
            case 6 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:30: T31
                {
                mT31(); 

                }
                break;
            case 7 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:34: T32
                {
                mT32(); 

                }
                break;
            case 8 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:38: T33
                {
                mT33(); 

                }
                break;
            case 9 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:42: T34
                {
                mT34(); 

                }
                break;
            case 10 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:46: T35
                {
                mT35(); 

                }
                break;
            case 11 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:50: ID
                {
                mID(); 

                }
                break;
            case 12 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:53: STRING
                {
                mSTRING(); 

                }
                break;
            case 13 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:60: ARITHSYM
                {
                mARITHSYM(); 

                }
                break;
            case 14 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:69: INT
                {
                mINT(); 

                }
                break;
            case 15 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:73: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 16 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:79: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 17 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:87: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 18 :
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:1:95: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\1\uffff\1\17\2\uffff\4\17\2\uffff\1\17\1\uffff\2\16\2\uffff\1\33"+
        "\4\uffff\3\17\1\40\1\17\1\16\1\uffff\4\17\1\uffff\1\46\4\17\1\uffff"+
        "\1\53\3\17\1\uffff\3\17\1\62\2\17\1\uffff\3\17\1\70\1\71\2\uffff";
    static final String DFA12_eofS =
        "\72\uffff";
    static final String DFA12_minS =
        "\1\11\1\162\2\uffff\1\154\1\137\1\157\1\156\2\uffff\1\75\1\uffff"+
        "\2\56\2\uffff\1\56\4\uffff\1\145\1\157\1\151\1\41\1\144\1\56\1\uffff"+
        "\1\145\1\142\1\156\1\162\1\uffff\1\41\1\144\1\141\1\143\1\145\1"+
        "\uffff\1\41\2\154\1\160\1\uffff\1\163\1\165\1\157\1\41\1\144\1\162"+
        "\1\uffff\1\145\1\164\1\163\2\41\2\uffff";
    static final String DFA12_maxS =
        "\1\172\1\162\2\uffff\1\154\1\137\1\157\1\156\2\uffff\1\75\1\uffff"+
        "\2\71\2\uffff\1\71\4\uffff\1\145\1\157\1\151\1\172\1\144\1\71\1"+
        "\uffff\1\145\1\142\1\156\1\162\1\uffff\1\172\1\144\1\141\1\143\1"+
        "\145\1\uffff\1\172\2\154\1\160\1\uffff\1\163\1\165\1\157\1\172\1"+
        "\144\1\162\1\uffff\1\145\1\164\1\163\2\172\2\uffff";
    static final String DFA12_acceptS =
        "\2\uffff\1\2\1\3\4\uffff\1\11\1\12\1\uffff\1\14\2\uffff\1\15\1\13"+
        "\1\uffff\1\17\1\20\1\21\1\22\6\uffff\1\16\4\uffff\1\6\5\uffff\1"+
        "\7\4\uffff\1\1\6\uffff\1\4\5\uffff\1\10\1\5";
    static final String DFA12_specialS =
        "\72\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\24\1\23\25\uffff\1\24\1\12\1\13\3\17\2\uffff\1\10\1\11\1"+
            "\16\1\15\1\uffff\1\14\1\21\1\16\12\20\1\uffff\1\22\3\16\1\17"+
            "\1\uffff\32\17\1\2\1\uffff\1\3\1\16\1\5\1\uffff\1\17\1\1\2\17"+
            "\1\7\1\17\1\4\14\17\1\6\6\17",
            "\1\25",
            "",
            "",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "",
            "",
            "\1\16",
            "",
            "\1\21\1\uffff\12\32",
            "\1\21\1\uffff\12\32",
            "",
            "",
            "\1\21\1\uffff\12\20",
            "",
            "",
            "",
            "",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\17\1\uffff\3\17\4\uffff\2\17\1\uffff\1\37\1\uffff\14\17\1"+
            "\uffff\1\17\1\uffff\2\17\1\uffff\32\17\4\uffff\1\17\1\uffff"+
            "\32\17",
            "\1\41",
            "\1\21\1\uffff\12\32",
            "",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "",
            "\1\17\1\uffff\3\17\4\uffff\2\17\1\uffff\1\17\1\uffff\14\17\1"+
            "\uffff\1\17\1\uffff\2\17\1\uffff\32\17\4\uffff\1\17\1\uffff"+
            "\32\17",
            "\1\47",
            "\1\50",
            "\1\51",
            "\1\52",
            "",
            "\1\17\1\uffff\3\17\4\uffff\2\17\1\uffff\1\17\1\uffff\14\17\1"+
            "\uffff\1\17\1\uffff\2\17\1\uffff\32\17\4\uffff\1\17\1\uffff"+
            "\32\17",
            "\1\54",
            "\1\55",
            "\1\56",
            "",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\17\1\uffff\3\17\4\uffff\2\17\1\uffff\1\17\1\uffff\14\17\1"+
            "\uffff\1\17\1\uffff\2\17\1\uffff\32\17\4\uffff\1\17\1\uffff"+
            "\32\17",
            "\1\63",
            "\1\64",
            "",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\17\1\uffff\3\17\4\uffff\2\17\1\uffff\1\17\1\uffff\14\17\1"+
            "\uffff\1\17\1\uffff\2\17\1\uffff\32\17\4\uffff\1\17\1\uffff"+
            "\32\17",
            "\1\17\1\uffff\3\17\4\uffff\2\17\1\uffff\1\17\1\uffff\14\17\1"+
            "\uffff\1\17\1\uffff\2\17\1\uffff\32\17\4\uffff\1\17\1\uffff"+
            "\32\17",
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
            return "1:1: Tokens : ( T26 | T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | ID | STRING | ARITHSYM | INT | FLOAT | COMMENT | NEWLINE | WS );";
        }
    }
 

}