// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoImages.g 2008-06-12 17:50:15

package repast.simphony.relogo.ide.image;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class NetLogoImagesLexer extends Lexer {
    public static final int NAME=5;
    public static final int WS=8;
    public static final int NEWLINE=4;
    public static final int SECT=7;
    public static final int NUMBER=6;
    public static final int T10=10;
    public static final int T11=11;
    public static final int T12=12;
    public static final int T13=13;
    public static final int T14=14;
    public static final int T9=9;
    public static final int Tokens=15;
    public static final int EOF=-1;
    public NetLogoImagesLexer() {;} 
    public NetLogoImagesLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoImages.g"; }

    // $ANTLR start T9
    public final void mT9() throws RecognitionException {
        try {
            int _type = T9;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:6:4: ( 'true' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:6:6: 'true'
            {
            match("true"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T9

    // $ANTLR start T10
    public final void mT10() throws RecognitionException {
        try {
            int _type = T10;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:7:5: ( 'false' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:7:7: 'false'
            {
            match("false"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T10

    // $ANTLR start T11
    public final void mT11() throws RecognitionException {
        try {
            int _type = T11;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:8:5: ( 'Line' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:8:7: 'Line'
            {
            match("Line"); 


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
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:9:5: ( 'Circle' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:9:7: 'Circle'
            {
            match("Circle"); 


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
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:10:5: ( 'Rectangle' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:10:7: 'Rectangle'
            {
            match("Rectangle"); 


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
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:11:5: ( 'Polygon' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:11:7: 'Polygon'
            {
            match("Polygon"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end T14

    // $ANTLR start NUMBER
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:56:9: ( ( '-' )? ( '0' .. '9' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:56:11: ( '-' )? ( '0' .. '9' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:56:11: ( '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:56:12: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:56:18: ( '0' .. '9' )+
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
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:56:20: '0' .. '9'
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

    // $ANTLR start NAME
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:59:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:59:7: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:60:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='-'||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:
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
            	    break loop3;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NAME

    // $ANTLR start SECT
    public final void mSECT() throws RecognitionException {
        try {
            int _type = SECT;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:63:7: ( '@#$#@#$#@' ( '\\r' )? '\\n' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:63:9: '@#$#@#$#@' ( '\\r' )? '\\n'
            {
            match("@#$#@#$#@"); 

            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:63:21: ( '\\r' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\r') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:63:22: '\\r'
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
    // $ANTLR end SECT

    // $ANTLR start WS
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:65:4: ( ( ' ' | '\\t' )+ )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:65:6: ( ' ' | '\\t' )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:65:6: ( ' ' | '\\t' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\t'||LA5_0==' ') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:
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
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
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
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:67:9: ( ( '\\r' )? '\\n' )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:67:11: ( '\\r' )? '\\n'
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:67:11: ( '\\r' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\r') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:67:12: '\\r'
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
        // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:8: ( T9 | T10 | T11 | T12 | T13 | T14 | NUMBER | NAME | SECT | WS | NEWLINE )
        int alt7=11;
        switch ( input.LA(1) ) {
        case 't':
            {
            int LA7_1 = input.LA(2);

            if ( (LA7_1=='r') ) {
                int LA7_12 = input.LA(3);

                if ( (LA7_12=='u') ) {
                    int LA7_18 = input.LA(4);

                    if ( (LA7_18=='e') ) {
                        int LA7_24 = input.LA(5);

                        if ( (LA7_24=='-'||(LA7_24>='0' && LA7_24<='9')||(LA7_24>='A' && LA7_24<='Z')||LA7_24=='_'||(LA7_24>='a' && LA7_24<='z')) ) {
                            alt7=8;
                        }
                        else {
                            alt7=1;}
                    }
                    else {
                        alt7=8;}
                }
                else {
                    alt7=8;}
            }
            else {
                alt7=8;}
            }
            break;
        case 'f':
            {
            int LA7_2 = input.LA(2);

            if ( (LA7_2=='a') ) {
                int LA7_13 = input.LA(3);

                if ( (LA7_13=='l') ) {
                    int LA7_19 = input.LA(4);

                    if ( (LA7_19=='s') ) {
                        int LA7_25 = input.LA(5);

                        if ( (LA7_25=='e') ) {
                            int LA7_31 = input.LA(6);

                            if ( (LA7_31=='-'||(LA7_31>='0' && LA7_31<='9')||(LA7_31>='A' && LA7_31<='Z')||LA7_31=='_'||(LA7_31>='a' && LA7_31<='z')) ) {
                                alt7=8;
                            }
                            else {
                                alt7=2;}
                        }
                        else {
                            alt7=8;}
                    }
                    else {
                        alt7=8;}
                }
                else {
                    alt7=8;}
            }
            else {
                alt7=8;}
            }
            break;
        case 'L':
            {
            int LA7_3 = input.LA(2);

            if ( (LA7_3=='i') ) {
                int LA7_14 = input.LA(3);

                if ( (LA7_14=='n') ) {
                    int LA7_20 = input.LA(4);

                    if ( (LA7_20=='e') ) {
                        int LA7_26 = input.LA(5);

                        if ( (LA7_26=='-'||(LA7_26>='0' && LA7_26<='9')||(LA7_26>='A' && LA7_26<='Z')||LA7_26=='_'||(LA7_26>='a' && LA7_26<='z')) ) {
                            alt7=8;
                        }
                        else {
                            alt7=3;}
                    }
                    else {
                        alt7=8;}
                }
                else {
                    alt7=8;}
            }
            else {
                alt7=8;}
            }
            break;
        case 'C':
            {
            int LA7_4 = input.LA(2);

            if ( (LA7_4=='i') ) {
                int LA7_15 = input.LA(3);

                if ( (LA7_15=='r') ) {
                    int LA7_21 = input.LA(4);

                    if ( (LA7_21=='c') ) {
                        int LA7_27 = input.LA(5);

                        if ( (LA7_27=='l') ) {
                            int LA7_33 = input.LA(6);

                            if ( (LA7_33=='e') ) {
                                int LA7_37 = input.LA(7);

                                if ( (LA7_37=='-'||(LA7_37>='0' && LA7_37<='9')||(LA7_37>='A' && LA7_37<='Z')||LA7_37=='_'||(LA7_37>='a' && LA7_37<='z')) ) {
                                    alt7=8;
                                }
                                else {
                                    alt7=4;}
                            }
                            else {
                                alt7=8;}
                        }
                        else {
                            alt7=8;}
                    }
                    else {
                        alt7=8;}
                }
                else {
                    alt7=8;}
            }
            else {
                alt7=8;}
            }
            break;
        case 'R':
            {
            int LA7_5 = input.LA(2);

            if ( (LA7_5=='e') ) {
                int LA7_16 = input.LA(3);

                if ( (LA7_16=='c') ) {
                    int LA7_22 = input.LA(4);

                    if ( (LA7_22=='t') ) {
                        int LA7_28 = input.LA(5);

                        if ( (LA7_28=='a') ) {
                            int LA7_34 = input.LA(6);

                            if ( (LA7_34=='n') ) {
                                int LA7_38 = input.LA(7);

                                if ( (LA7_38=='g') ) {
                                    int LA7_41 = input.LA(8);

                                    if ( (LA7_41=='l') ) {
                                        int LA7_43 = input.LA(9);

                                        if ( (LA7_43=='e') ) {
                                            int LA7_45 = input.LA(10);

                                            if ( (LA7_45=='-'||(LA7_45>='0' && LA7_45<='9')||(LA7_45>='A' && LA7_45<='Z')||LA7_45=='_'||(LA7_45>='a' && LA7_45<='z')) ) {
                                                alt7=8;
                                            }
                                            else {
                                                alt7=5;}
                                        }
                                        else {
                                            alt7=8;}
                                    }
                                    else {
                                        alt7=8;}
                                }
                                else {
                                    alt7=8;}
                            }
                            else {
                                alt7=8;}
                        }
                        else {
                            alt7=8;}
                    }
                    else {
                        alt7=8;}
                }
                else {
                    alt7=8;}
            }
            else {
                alt7=8;}
            }
            break;
        case 'P':
            {
            int LA7_6 = input.LA(2);

            if ( (LA7_6=='o') ) {
                int LA7_17 = input.LA(3);

                if ( (LA7_17=='l') ) {
                    int LA7_23 = input.LA(4);

                    if ( (LA7_23=='y') ) {
                        int LA7_29 = input.LA(5);

                        if ( (LA7_29=='g') ) {
                            int LA7_35 = input.LA(6);

                            if ( (LA7_35=='o') ) {
                                int LA7_39 = input.LA(7);

                                if ( (LA7_39=='n') ) {
                                    int LA7_42 = input.LA(8);

                                    if ( (LA7_42=='-'||(LA7_42>='0' && LA7_42<='9')||(LA7_42>='A' && LA7_42<='Z')||LA7_42=='_'||(LA7_42>='a' && LA7_42<='z')) ) {
                                        alt7=8;
                                    }
                                    else {
                                        alt7=6;}
                                }
                                else {
                                    alt7=8;}
                            }
                            else {
                                alt7=8;}
                        }
                        else {
                            alt7=8;}
                    }
                    else {
                        alt7=8;}
                }
                else {
                    alt7=8;}
            }
            else {
                alt7=8;}
            }
            break;
        case '-':
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt7=7;
            }
            break;
        case 'A':
        case 'B':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'M':
        case 'N':
        case 'O':
        case 'Q':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt7=8;
            }
            break;
        case '@':
            {
            alt7=9;
            }
            break;
        case '\t':
        case ' ':
            {
            alt7=10;
            }
            break;
        case '\n':
        case '\r':
            {
            alt7=11;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("1:1: Tokens : ( T9 | T10 | T11 | T12 | T13 | T14 | NUMBER | NAME | SECT | WS | NEWLINE );", 7, 0, input);

            throw nvae;
        }

        switch (alt7) {
            case 1 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:10: T9
                {
                mT9(); 

                }
                break;
            case 2 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:13: T10
                {
                mT10(); 

                }
                break;
            case 3 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:17: T11
                {
                mT11(); 

                }
                break;
            case 4 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:21: T12
                {
                mT12(); 

                }
                break;
            case 5 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:25: T13
                {
                mT13(); 

                }
                break;
            case 6 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:29: T14
                {
                mT14(); 

                }
                break;
            case 7 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:33: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 8 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:40: NAME
                {
                mNAME(); 

                }
                break;
            case 9 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:45: SECT
                {
                mSECT(); 

                }
                break;
            case 10 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:50: WS
                {
                mWS(); 

                }
                break;
            case 11 :
                // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:1:53: NEWLINE
                {
                mNEWLINE(); 

                }
                break;

        }

    }


 

}