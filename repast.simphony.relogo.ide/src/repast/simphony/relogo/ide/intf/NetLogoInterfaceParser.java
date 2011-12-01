// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoInterface.g 2009-03-12 20:33:59

package repast.simphony.relogo.ide.intf;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class NetLogoInterfaceParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEWLINE", "QUOTATION", "NAME", "NUMBER", "FLOAT", "GRAPHIC_CHAR", "WS", "'TEXTBOX'", "'SLIDER'", "'BUTTON'", "'CHOOSER'", "'SWITCH'", "'OUTPUT'", "'PLOT'", "'PENS'", "'MONITOR'", "'INPUTBOX'", "'CC-WINDOW'", "'GRAPHICS-WINDOW'", "'true'", "'false'"
    };
    public static final int NAME=6;
    public static final int WS=10;
    public static final int NEWLINE=4;
    public static final int GRAPHIC_CHAR=9;
    public static final int QUOTATION=5;
    public static final int NUMBER=7;
    public static final int FLOAT=8;
    public static final int EOF=-1;

        public NetLogoInterfaceParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoInterface.g"; }



    // $ANTLR start control_section
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:15:1: control_section returns [List<NLControl> controls] : (img= control )* ;
    public final List<NLControl> control_section() throws RecognitionException {
        List<NLControl> controls = null;

        NLControl img = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:16:2: ( (img= control )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:16:4: (img= control )*
            {
            LinkedList<NLControl> list = new LinkedList<NLControl>();
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:16:64: (img= control )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=11 && LA1_0<=17)||(LA1_0>=19 && LA1_0<=22)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:16:65: img= control
            	    {
            	    pushFollow(FOLLOW_control_in_control_section35);
            	    img=control();
            	    _fsp--;

            	     list.add(img); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             controls = list; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return controls;
    }
    // $ANTLR end control_section


    // $ANTLR start control
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:20:1: control returns [NLControl img] : ( 'TEXTBOX' NEWLINE bb= bounding_box t= anything (unk_t1= anything )* NEWLINE | 'SLIDER' NEWLINE bb= bounding_box label= anything variable= anything (min= number NEWLINE | fmin= floatnum NEWLINE ) (max= number NEWLINE | fmax= floatnum NEWLINE | anything ) (val= number NEWLINE | fval= floatnum NEWLINE ) (step= number NEWLINE | fstep= floatnum NEWLINE ) unk1= number NEWLINE units= anything (orientation= anything ) NEWLINE | 'BUTTON' NEWLINE bb= bounding_box label= anything commands= anything doforever= name NEWLINE unk1= number NEWLINE unk2= name NEWLINE agent= name NEWLINE (update= anything (actionkey= anything (unk21= anything unk22= anything )? )? )? NEWLINE | 'CHOOSER' NEWLINE bb= bounding_box label= anything variable= anything (qt= quote | n= name | num1= number | fnum= floatnum )+ NEWLINE first= number NEWLINE NEWLINE | 'SWITCH' NEWLINE bb= bounding_box label= anything variable= anything val1= number NEWLINE val2= number NEWLINE val3= number NEWLINE NEWLINE | 'OUTPUT' NEWLINE bb= bounding_box ( anything )* NEWLINE | 'PLOT' NEWLINE bb= bounding_box title= anything x= anything y= anything xmin= floatnum NEWLINE xmax= floatnum NEWLINE ymin= floatnum NEWLINE ymax= floatnum NEWLINE autoplot= bool NEWLINE showLegend= bool NEWLINE ( 'PENS' NEWLINE (p= pen )* )? NEWLINE | 'MONITOR' NEWLINE bb= bounding_box label= anything variable= anything dplaces= number NEWLINE unk1= number NEWLINE (unk_m2= anything )? NEWLINE | 'INPUTBOX' NEWLINE bb= bounding_box variable= anything initial= anything ( ( bool | number ) NEWLINE ( bool | number ) NEWLINE )? (type= anything )? NEWLINE | 'CC-WINDOW' NEWLINE bb= bounding_box t= anything (unk1= number NEWLINE )? NEWLINE | 'GRAPHICS-WINDOW' NEWLINE bb= bounding_box number NEWLINE number NEWLINE floatnum NEWLINE number NEWLINE number NEWLINE number NEWLINE number NEWLINE ( number NEWLINE )* ( name NEWLINE )? NEWLINE );
    public final NLControl control() throws RecognitionException {
        NLControl img = null;

        Rectangle bb = null;

        anything_return t = null;

        anything_return unk_t1 = null;

        anything_return label = null;

        anything_return variable = null;

        number_return min = null;

        floatnum_return fmin = null;

        number_return max = null;

        floatnum_return fmax = null;

        number_return val = null;

        floatnum_return fval = null;

        number_return step = null;

        floatnum_return fstep = null;

        number_return unk1 = null;

        anything_return units = null;

        anything_return orientation = null;

        anything_return commands = null;

        String doforever = null;

        String unk2 = null;

        String agent = null;

        anything_return update = null;

        anything_return actionkey = null;

        anything_return unk21 = null;

        anything_return unk22 = null;

        String qt = null;

        String n = null;

        number_return num1 = null;

        floatnum_return fnum = null;

        number_return first = null;

        number_return val1 = null;

        number_return val2 = null;

        number_return val3 = null;

        anything_return title = null;

        anything_return x = null;

        anything_return y = null;

        floatnum_return xmin = null;

        floatnum_return xmax = null;

        floatnum_return ymin = null;

        floatnum_return ymax = null;

        boolean autoplot = false;

        boolean showLegend = false;

        NLPen p = null;

        number_return dplaces = null;

        anything_return unk_m2 = null;

        anything_return initial = null;

        anything_return type = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:20:33: ( 'TEXTBOX' NEWLINE bb= bounding_box t= anything (unk_t1= anything )* NEWLINE | 'SLIDER' NEWLINE bb= bounding_box label= anything variable= anything (min= number NEWLINE | fmin= floatnum NEWLINE ) (max= number NEWLINE | fmax= floatnum NEWLINE | anything ) (val= number NEWLINE | fval= floatnum NEWLINE ) (step= number NEWLINE | fstep= floatnum NEWLINE ) unk1= number NEWLINE units= anything (orientation= anything ) NEWLINE | 'BUTTON' NEWLINE bb= bounding_box label= anything commands= anything doforever= name NEWLINE unk1= number NEWLINE unk2= name NEWLINE agent= name NEWLINE (update= anything (actionkey= anything (unk21= anything unk22= anything )? )? )? NEWLINE | 'CHOOSER' NEWLINE bb= bounding_box label= anything variable= anything (qt= quote | n= name | num1= number | fnum= floatnum )+ NEWLINE first= number NEWLINE NEWLINE | 'SWITCH' NEWLINE bb= bounding_box label= anything variable= anything val1= number NEWLINE val2= number NEWLINE val3= number NEWLINE NEWLINE | 'OUTPUT' NEWLINE bb= bounding_box ( anything )* NEWLINE | 'PLOT' NEWLINE bb= bounding_box title= anything x= anything y= anything xmin= floatnum NEWLINE xmax= floatnum NEWLINE ymin= floatnum NEWLINE ymax= floatnum NEWLINE autoplot= bool NEWLINE showLegend= bool NEWLINE ( 'PENS' NEWLINE (p= pen )* )? NEWLINE | 'MONITOR' NEWLINE bb= bounding_box label= anything variable= anything dplaces= number NEWLINE unk1= number NEWLINE (unk_m2= anything )? NEWLINE | 'INPUTBOX' NEWLINE bb= bounding_box variable= anything initial= anything ( ( bool | number ) NEWLINE ( bool | number ) NEWLINE )? (type= anything )? NEWLINE | 'CC-WINDOW' NEWLINE bb= bounding_box t= anything (unk1= number NEWLINE )? NEWLINE | 'GRAPHICS-WINDOW' NEWLINE bb= bounding_box number NEWLINE number NEWLINE floatnum NEWLINE number NEWLINE number NEWLINE number NEWLINE number NEWLINE ( number NEWLINE )* ( name NEWLINE )? NEWLINE )
            int alt22=11;
            switch ( input.LA(1) ) {
            case 11:
                {
                alt22=1;
                }
                break;
            case 12:
                {
                alt22=2;
                }
                break;
            case 13:
                {
                alt22=3;
                }
                break;
            case 14:
                {
                alt22=4;
                }
                break;
            case 15:
                {
                alt22=5;
                }
                break;
            case 16:
                {
                alt22=6;
                }
                break;
            case 17:
                {
                alt22=7;
                }
                break;
            case 19:
                {
                alt22=8;
                }
                break;
            case 20:
                {
                alt22=9;
                }
                break;
            case 21:
                {
                alt22=10;
                }
                break;
            case 22:
                {
                alt22=11;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("20:1: control returns [NLControl img] : ( 'TEXTBOX' NEWLINE bb= bounding_box t= anything (unk_t1= anything )* NEWLINE | 'SLIDER' NEWLINE bb= bounding_box label= anything variable= anything (min= number NEWLINE | fmin= floatnum NEWLINE ) (max= number NEWLINE | fmax= floatnum NEWLINE | anything ) (val= number NEWLINE | fval= floatnum NEWLINE ) (step= number NEWLINE | fstep= floatnum NEWLINE ) unk1= number NEWLINE units= anything (orientation= anything ) NEWLINE | 'BUTTON' NEWLINE bb= bounding_box label= anything commands= anything doforever= name NEWLINE unk1= number NEWLINE unk2= name NEWLINE agent= name NEWLINE (update= anything (actionkey= anything (unk21= anything unk22= anything )? )? )? NEWLINE | 'CHOOSER' NEWLINE bb= bounding_box label= anything variable= anything (qt= quote | n= name | num1= number | fnum= floatnum )+ NEWLINE first= number NEWLINE NEWLINE | 'SWITCH' NEWLINE bb= bounding_box label= anything variable= anything val1= number NEWLINE val2= number NEWLINE val3= number NEWLINE NEWLINE | 'OUTPUT' NEWLINE bb= bounding_box ( anything )* NEWLINE | 'PLOT' NEWLINE bb= bounding_box title= anything x= anything y= anything xmin= floatnum NEWLINE xmax= floatnum NEWLINE ymin= floatnum NEWLINE ymax= floatnum NEWLINE autoplot= bool NEWLINE showLegend= bool NEWLINE ( 'PENS' NEWLINE (p= pen )* )? NEWLINE | 'MONITOR' NEWLINE bb= bounding_box label= anything variable= anything dplaces= number NEWLINE unk1= number NEWLINE (unk_m2= anything )? NEWLINE | 'INPUTBOX' NEWLINE bb= bounding_box variable= anything initial= anything ( ( bool | number ) NEWLINE ( bool | number ) NEWLINE )? (type= anything )? NEWLINE | 'CC-WINDOW' NEWLINE bb= bounding_box t= anything (unk1= number NEWLINE )? NEWLINE | 'GRAPHICS-WINDOW' NEWLINE bb= bounding_box number NEWLINE number NEWLINE floatnum NEWLINE number NEWLINE number NEWLINE number NEWLINE number NEWLINE ( number NEWLINE )* ( name NEWLINE )? NEWLINE );", 22, 0, input);

                throw nvae;
            }

            switch (alt22) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:21:3: 'TEXTBOX' NEWLINE bb= bounding_box t= anything (unk_t1= anything )* NEWLINE
                    {
                    match(input,11,FOLLOW_11_in_control60); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control62); 
                    pushFollow(FOLLOW_bounding_box_in_control66);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control70);
                    t=anything();
                    _fsp--;

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:21:48: (unk_t1= anything )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>=QUOTATION && LA2_0<=24)) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:21:49: unk_t1= anything
                    	    {
                    	    pushFollow(FOLLOW_anything_in_control75);
                    	    unk_t1=anything();
                    	    _fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control79); 
                    img =new NLTextbox(bb, input.toString(t.start,t.stop));

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:4: 'SLIDER' NEWLINE bb= bounding_box label= anything variable= anything (min= number NEWLINE | fmin= floatnum NEWLINE ) (max= number NEWLINE | fmax= floatnum NEWLINE | anything ) (val= number NEWLINE | fval= floatnum NEWLINE ) (step= number NEWLINE | fstep= floatnum NEWLINE ) unk1= number NEWLINE units= anything (orientation= anything ) NEWLINE
                    {
                    match(input,12,FOLLOW_12_in_control86); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control88); 
                    pushFollow(FOLLOW_bounding_box_in_control92);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control96);
                    label=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control100);
                    variable=anything();
                    _fsp--;

                    double minimum=0.0, maximum=0.0, sliderStep=1.0; Number value=null;
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:140: (min= number NEWLINE | fmin= floatnum NEWLINE )
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==NUMBER) ) {
                        alt3=1;
                    }
                    else if ( (LA3_0==FLOAT) ) {
                        alt3=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("22:140: (min= number NEWLINE | fmin= floatnum NEWLINE )", 3, 0, input);

                        throw nvae;
                    }
                    switch (alt3) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:141: min= number NEWLINE
                            {
                            pushFollow(FOLLOW_number_in_control107);
                            min=number();
                            _fsp--;

                            minimum=min.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control111); 

                            }
                            break;
                        case 2 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:182: fmin= floatnum NEWLINE
                            {
                            pushFollow(FOLLOW_floatnum_in_control117);
                            fmin=floatnum();
                            _fsp--;

                            minimum=fmin.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control121); 

                            }
                            break;

                    }

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:226: (max= number NEWLINE | fmax= floatnum NEWLINE | anything )
                    int alt4=3;
                    switch ( input.LA(1) ) {
                    case NUMBER:
                        {
                        int LA4_1 = input.LA(2);

                        if ( (LA4_1==NEWLINE) ) {
                            alt4=1;
                        }
                        else if ( ((LA4_1>=QUOTATION && LA4_1<=24)) ) {
                            alt4=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("22:226: (max= number NEWLINE | fmax= floatnum NEWLINE | anything )", 4, 1, input);

                            throw nvae;
                        }
                        }
                        break;
                    case FLOAT:
                        {
                        int LA4_2 = input.LA(2);

                        if ( (LA4_2==NEWLINE) ) {
                            alt4=2;
                        }
                        else if ( ((LA4_2>=QUOTATION && LA4_2<=24)) ) {
                            alt4=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("22:226: (max= number NEWLINE | fmax= floatnum NEWLINE | anything )", 4, 2, input);

                            throw nvae;
                        }
                        }
                        break;
                    case QUOTATION:
                    case NAME:
                    case GRAPHIC_CHAR:
                    case WS:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                        {
                        alt4=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("22:226: (max= number NEWLINE | fmax= floatnum NEWLINE | anything )", 4, 0, input);

                        throw nvae;
                    }

                    switch (alt4) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:227: max= number NEWLINE
                            {
                            pushFollow(FOLLOW_number_in_control127);
                            max=number();
                            _fsp--;

                            maximum=max.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control131); 

                            }
                            break;
                        case 2 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:268: fmax= floatnum NEWLINE
                            {
                            pushFollow(FOLLOW_floatnum_in_control137);
                            fmax=floatnum();
                            _fsp--;

                            maximum=fmax.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control141); 

                            }
                            break;
                        case 3 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:313: anything
                            {
                            pushFollow(FOLLOW_anything_in_control145);
                            anything();
                            _fsp--;

                            /* expression! */

                            }
                            break;

                    }

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:343: (val= number NEWLINE | fval= floatnum NEWLINE )
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==NUMBER) ) {
                        alt5=1;
                    }
                    else if ( (LA5_0==FLOAT) ) {
                        alt5=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("22:343: (val= number NEWLINE | fval= floatnum NEWLINE )", 5, 0, input);

                        throw nvae;
                    }
                    switch (alt5) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:344: val= number NEWLINE
                            {
                            pushFollow(FOLLOW_number_in_control153);
                            val=number();
                            _fsp--;

                            value=val.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control157); 

                            }
                            break;
                        case 2 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:383: fval= floatnum NEWLINE
                            {
                            pushFollow(FOLLOW_floatnum_in_control163);
                            fval=floatnum();
                            _fsp--;

                            value=fval.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control167); 

                            }
                            break;

                    }

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:425: (step= number NEWLINE | fstep= floatnum NEWLINE )
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==NUMBER) ) {
                        alt6=1;
                    }
                    else if ( (LA6_0==FLOAT) ) {
                        alt6=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("22:425: (step= number NEWLINE | fstep= floatnum NEWLINE )", 6, 0, input);

                        throw nvae;
                    }
                    switch (alt6) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:426: step= number NEWLINE
                            {
                            pushFollow(FOLLOW_number_in_control173);
                            step=number();
                            _fsp--;

                            sliderStep=step.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control177); 

                            }
                            break;
                        case 2 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:472: fstep= floatnum NEWLINE
                            {
                            pushFollow(FOLLOW_floatnum_in_control183);
                            fstep=floatnum();
                            _fsp--;

                            sliderStep=fstep.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control187); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_number_in_control192);
                    unk1=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control194); 
                    pushFollow(FOLLOW_anything_in_control198);
                    units=anything();
                    _fsp--;

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:556: (orientation= anything )
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:22:557: orientation= anything
                    {
                    pushFollow(FOLLOW_anything_in_control203);
                    orientation=anything();
                    _fsp--;


                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control206); 
                    img =new NLSlider(bb, input.toString(label.start,label.stop), input.toString(variable.start,variable.stop), minimum, maximum, value, sliderStep, unk1.num, input.toString(units.start,units.stop));

                    }
                    break;
                case 3 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:4: 'BUTTON' NEWLINE bb= bounding_box label= anything commands= anything doforever= name NEWLINE unk1= number NEWLINE unk2= name NEWLINE agent= name NEWLINE (update= anything (actionkey= anything (unk21= anything unk22= anything )? )? )? NEWLINE
                    {
                    match(input,13,FOLLOW_13_in_control214); 
                    String updateText=null, actionKeyText=null;
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control218); 
                    pushFollow(FOLLOW_bounding_box_in_control222);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control226);
                    label=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control230);
                    commands=anything();
                    _fsp--;

                    pushFollow(FOLLOW_name_in_control234);
                    doforever=name();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control236); 
                    pushFollow(FOLLOW_number_in_control240);
                    unk1=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control242); 
                    pushFollow(FOLLOW_name_in_control246);
                    unk2=name();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control248); 
                    pushFollow(FOLLOW_name_in_control252);
                    agent=name();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control254); 
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:196: (update= anything (actionkey= anything (unk21= anything unk22= anything )? )? )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( ((LA9_0>=QUOTATION && LA9_0<=24)) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:197: update= anything (actionkey= anything (unk21= anything unk22= anything )? )?
                            {
                            pushFollow(FOLLOW_anything_in_control259);
                            update=anything();
                            _fsp--;

                            updateText = input.toString(update.start,update.stop);
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:242: (actionkey= anything (unk21= anything unk22= anything )? )?
                            int alt8=2;
                            int LA8_0 = input.LA(1);

                            if ( ((LA8_0>=QUOTATION && LA8_0<=24)) ) {
                                alt8=1;
                            }
                            switch (alt8) {
                                case 1 :
                                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:243: actionkey= anything (unk21= anything unk22= anything )?
                                    {
                                    pushFollow(FOLLOW_anything_in_control266);
                                    actionkey=anything();
                                    _fsp--;

                                    actionKeyText = input.toString(actionkey.start,actionkey.stop);
                                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:297: (unk21= anything unk22= anything )?
                                    int alt7=2;
                                    int LA7_0 = input.LA(1);

                                    if ( ((LA7_0>=QUOTATION && LA7_0<=24)) ) {
                                        alt7=1;
                                    }
                                    switch (alt7) {
                                        case 1 :
                                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:23:298: unk21= anything unk22= anything
                                            {
                                            pushFollow(FOLLOW_anything_in_control273);
                                            unk21=anything();
                                            _fsp--;

                                            pushFollow(FOLLOW_anything_in_control277);
                                            unk22=anything();
                                            _fsp--;


                                            }
                                            break;

                                    }


                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control285); 
                    img =new NLButton(bb, input.toString(label.start,label.stop), input.toString(commands.start,commands.stop), doforever, unk1.num, unk2, agent, updateText, actionKeyText);

                    }
                    break;
                case 4 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:24:4: 'CHOOSER' NEWLINE bb= bounding_box label= anything variable= anything (qt= quote | n= name | num1= number | fnum= floatnum )+ NEWLINE first= number NEWLINE NEWLINE
                    {
                    match(input,14,FOLLOW_14_in_control292); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control294); 
                    pushFollow(FOLLOW_bounding_box_in_control298);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control302);
                    label=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control306);
                    variable=anything();
                    _fsp--;

                    LinkedList<String> list = new LinkedList<String>();
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:24:124: (qt= quote | n= name | num1= number | fnum= floatnum )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=5;
                        switch ( input.LA(1) ) {
                        case QUOTATION:
                            {
                            alt10=1;
                            }
                            break;
                        case NAME:
                            {
                            alt10=2;
                            }
                            break;
                        case NUMBER:
                            {
                            alt10=3;
                            }
                            break;
                        case FLOAT:
                            {
                            alt10=4;
                            }
                            break;

                        }

                        switch (alt10) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:24:125: qt= quote
                    	    {
                    	    pushFollow(FOLLOW_quote_in_control312);
                    	    qt=quote();
                    	    _fsp--;

                    	     list.add(qt); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:24:158: n= name
                    	    {
                    	    pushFollow(FOLLOW_name_in_control321);
                    	    n=name();
                    	    _fsp--;

                    	     list.add(n); 

                    	    }
                    	    break;
                    	case 3 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:24:188: num1= number
                    	    {
                    	    pushFollow(FOLLOW_number_in_control330);
                    	    num1=number();
                    	    _fsp--;

                    	     list.add(input.toString(num1.start,num1.stop)); 

                    	    }
                    	    break;
                    	case 4 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:24:229: fnum= floatnum
                    	    {
                    	    pushFollow(FOLLOW_floatnum_in_control339);
                    	    fnum=floatnum();
                    	    _fsp--;

                    	     list.add(input.toString(fnum.start,fnum.stop)); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control346); 
                    pushFollow(FOLLOW_number_in_control350);
                    first=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control352); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control354); 
                    img =new NLChooser(bb, input.toString(label.start,label.stop), input.toString(variable.start,variable.stop), list, first.num);

                    }
                    break;
                case 5 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:25:4: 'SWITCH' NEWLINE bb= bounding_box label= anything variable= anything val1= number NEWLINE val2= number NEWLINE val3= number NEWLINE NEWLINE
                    {
                    match(input,15,FOLLOW_15_in_control361); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control363); 
                    pushFollow(FOLLOW_bounding_box_in_control367);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control371);
                    label=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control375);
                    variable=anything();
                    _fsp--;

                    pushFollow(FOLLOW_number_in_control379);
                    val1=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control381); 
                    pushFollow(FOLLOW_number_in_control385);
                    val2=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control387); 
                    pushFollow(FOLLOW_number_in_control391);
                    val3=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control393); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control395); 
                    img =new NLSwitch(bb, input.toString(label.start,label.stop), input.toString(variable.start,variable.stop), val1.num, val2.num, val3.num);

                    }
                    break;
                case 6 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:26:4: 'OUTPUT' NEWLINE bb= bounding_box ( anything )* NEWLINE
                    {
                    match(input,16,FOLLOW_16_in_control402); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control404); 
                    pushFollow(FOLLOW_bounding_box_in_control408);
                    bb=bounding_box();
                    _fsp--;

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:26:37: ( anything )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>=QUOTATION && LA11_0<=24)) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:26:38: anything
                    	    {
                    	    pushFollow(FOLLOW_anything_in_control411);
                    	    anything();
                    	    _fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control415); 
                    img =new NLOutput(bb);

                    }
                    break;
                case 7 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:27:4: 'PLOT' NEWLINE bb= bounding_box title= anything x= anything y= anything xmin= floatnum NEWLINE xmax= floatnum NEWLINE ymin= floatnum NEWLINE ymax= floatnum NEWLINE autoplot= bool NEWLINE showLegend= bool NEWLINE ( 'PENS' NEWLINE (p= pen )* )? NEWLINE
                    {
                    match(input,17,FOLLOW_17_in_control422); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control424); 
                    pushFollow(FOLLOW_bounding_box_in_control428);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control432);
                    title=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control436);
                    x=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control440);
                    y=anything();
                    _fsp--;

                    pushFollow(FOLLOW_floatnum_in_control444);
                    xmin=floatnum();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control446); 
                    pushFollow(FOLLOW_floatnum_in_control450);
                    xmax=floatnum();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control452); 
                    pushFollow(FOLLOW_floatnum_in_control456);
                    ymin=floatnum();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control458); 
                    pushFollow(FOLLOW_floatnum_in_control462);
                    ymax=floatnum();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control464); 
                    pushFollow(FOLLOW_bool_in_control468);
                    autoplot=bool();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control470); 
                    pushFollow(FOLLOW_bool_in_control474);
                    showLegend=bool();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control476); 
                    LinkedList penList=new LinkedList();
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:27:245: ( 'PENS' NEWLINE (p= pen )* )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==18) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:27:246: 'PENS' NEWLINE (p= pen )*
                            {
                            match(input,18,FOLLOW_18_in_control481); 
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control483); 
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:27:261: (p= pen )*
                            loop12:
                            do {
                                int alt12=2;
                                int LA12_0 = input.LA(1);

                                if ( (LA12_0==QUOTATION) ) {
                                    alt12=1;
                                }


                                switch (alt12) {
                            	case 1 :
                            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:27:262: p= pen
                            	    {
                            	    pushFollow(FOLLOW_pen_in_control488);
                            	    p=pen();
                            	    _fsp--;

                            	    penList.add(p);

                            	    }
                            	    break;

                            	default :
                            	    break loop12;
                                }
                            } while (true);


                            }
                            break;

                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control497); 
                    img =new NLPlot(bb, input.toString(title.start,title.stop), input.toString(x.start,x.stop), input.toString(y.start,y.stop), xmin.num, xmax.num, ymin.num, ymax.num, autoplot, showLegend, penList);

                    }
                    break;
                case 8 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:28:4: 'MONITOR' NEWLINE bb= bounding_box label= anything variable= anything dplaces= number NEWLINE unk1= number NEWLINE (unk_m2= anything )? NEWLINE
                    {
                    match(input,19,FOLLOW_19_in_control504); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control506); 
                    pushFollow(FOLLOW_bounding_box_in_control510);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control514);
                    label=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control518);
                    variable=anything();
                    _fsp--;

                    pushFollow(FOLLOW_number_in_control522);
                    dplaces=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control524); 
                    pushFollow(FOLLOW_number_in_control528);
                    unk1=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control530); 
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:28:114: (unk_m2= anything )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( ((LA14_0>=QUOTATION && LA14_0<=24)) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:28:115: unk_m2= anything
                            {
                            pushFollow(FOLLOW_anything_in_control535);
                            unk_m2=anything();
                            _fsp--;


                            }
                            break;

                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control539); 
                    img =new NLMonitor(bb, input.toString(label.start,label.stop), input.toString(variable.start,variable.stop), dplaces.num, unk1.num);

                    }
                    break;
                case 9 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:29:4: 'INPUTBOX' NEWLINE bb= bounding_box variable= anything initial= anything ( ( bool | number ) NEWLINE ( bool | number ) NEWLINE )? (type= anything )? NEWLINE
                    {
                    match(input,20,FOLLOW_20_in_control546); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control548); 
                    pushFollow(FOLLOW_bounding_box_in_control552);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control556);
                    variable=anything();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control560);
                    initial=anything();
                    _fsp--;

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:3: ( ( bool | number ) NEWLINE ( bool | number ) NEWLINE )?
                    int alt17=2;
                    switch ( input.LA(1) ) {
                        case 23:
                            {
                            int LA17_1 = input.LA(2);

                            if ( (LA17_1==NEWLINE) ) {
                                int LA17_5 = input.LA(3);

                                if ( (LA17_5==NUMBER||(LA17_5>=23 && LA17_5<=24)) ) {
                                    alt17=1;
                                }
                            }
                            }
                            break;
                        case 24:
                            {
                            int LA17_2 = input.LA(2);

                            if ( (LA17_2==NEWLINE) ) {
                                int LA17_5 = input.LA(3);

                                if ( (LA17_5==NUMBER||(LA17_5>=23 && LA17_5<=24)) ) {
                                    alt17=1;
                                }
                            }
                            }
                            break;
                        case NUMBER:
                            {
                            int LA17_3 = input.LA(2);

                            if ( (LA17_3==NEWLINE) ) {
                                int LA17_5 = input.LA(3);

                                if ( (LA17_5==NUMBER||(LA17_5>=23 && LA17_5<=24)) ) {
                                    alt17=1;
                                }
                            }
                            }
                            break;
                    }

                    switch (alt17) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:5: ( bool | number ) NEWLINE ( bool | number ) NEWLINE
                            {
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:5: ( bool | number )
                            int alt15=2;
                            int LA15_0 = input.LA(1);

                            if ( ((LA15_0>=23 && LA15_0<=24)) ) {
                                alt15=1;
                            }
                            else if ( (LA15_0==NUMBER) ) {
                                alt15=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("30:5: ( bool | number )", 15, 0, input);

                                throw nvae;
                            }
                            switch (alt15) {
                                case 1 :
                                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:6: bool
                                    {
                                    pushFollow(FOLLOW_bool_in_control569);
                                    bool();
                                    _fsp--;


                                    }
                                    break;
                                case 2 :
                                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:13: number
                                    {
                                    pushFollow(FOLLOW_number_in_control573);
                                    number();
                                    _fsp--;


                                    }
                                    break;

                            }

                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control576); 
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:29: ( bool | number )
                            int alt16=2;
                            int LA16_0 = input.LA(1);

                            if ( ((LA16_0>=23 && LA16_0<=24)) ) {
                                alt16=1;
                            }
                            else if ( (LA16_0==NUMBER) ) {
                                alt16=2;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("30:29: ( bool | number )", 16, 0, input);

                                throw nvae;
                            }
                            switch (alt16) {
                                case 1 :
                                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:30: bool
                                    {
                                    pushFollow(FOLLOW_bool_in_control579);
                                    bool();
                                    _fsp--;


                                    }
                                    break;
                                case 2 :
                                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:37: number
                                    {
                                    pushFollow(FOLLOW_number_in_control583);
                                    number();
                                    _fsp--;


                                    }
                                    break;

                            }

                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control586); 

                            }
                            break;

                    }

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:55: (type= anything )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( ((LA18_0>=QUOTATION && LA18_0<=24)) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:30:56: type= anything
                            {
                            pushFollow(FOLLOW_anything_in_control593);
                            type=anything();
                            _fsp--;


                            }
                            break;

                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control600); 
                    img =new NLInputBox(bb, input.toString(variable.start,variable.stop), input.toString(initial.start,initial.stop));

                    }
                    break;
                case 10 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:32:4: 'CC-WINDOW' NEWLINE bb= bounding_box t= anything (unk1= number NEWLINE )? NEWLINE
                    {
                    match(input,21,FOLLOW_21_in_control607); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control609); 
                    pushFollow(FOLLOW_bounding_box_in_control613);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_anything_in_control617);
                    t=anything();
                    _fsp--;

                    int unkNum=-1;
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:32:68: (unk1= number NEWLINE )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==NUMBER) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:32:69: unk1= number NEWLINE
                            {
                            pushFollow(FOLLOW_number_in_control624);
                            unk1=number();
                            _fsp--;

                            unkNum=unk1.num;
                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control628); 

                            }
                            break;

                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control632); 
                    img =new NLCommandConsole(bb, input.toString(t.start,t.stop), unkNum);

                    }
                    break;
                case 11 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:33:4: 'GRAPHICS-WINDOW' NEWLINE bb= bounding_box number NEWLINE number NEWLINE floatnum NEWLINE number NEWLINE number NEWLINE number NEWLINE number NEWLINE ( number NEWLINE )* ( name NEWLINE )? NEWLINE
                    {
                    match(input,22,FOLLOW_22_in_control639); 
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control641); 
                    pushFollow(FOLLOW_bounding_box_in_control645);
                    bb=bounding_box();
                    _fsp--;

                    pushFollow(FOLLOW_number_in_control647);
                    number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control649); 
                    pushFollow(FOLLOW_number_in_control651);
                    number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control653); 
                    pushFollow(FOLLOW_floatnum_in_control655);
                    floatnum();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control657); 
                    pushFollow(FOLLOW_number_in_control662);
                    number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control664); 
                    pushFollow(FOLLOW_number_in_control666);
                    number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control668); 
                    pushFollow(FOLLOW_number_in_control670);
                    number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control672); 
                    pushFollow(FOLLOW_number_in_control674);
                    number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control676); 
                    
                    ArrayList<Integer> listOfInts = new ArrayList<Integer>(); 
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:34:63: ( number NEWLINE )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==NUMBER) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:34:64: number NEWLINE
                    	    {
                    	    pushFollow(FOLLOW_number_in_control679);
                    	    number_return tempNum = number();
                    	    listOfInts.add(tempNum.num);
                    	    _fsp--;

                    	    match(input,NEWLINE,FOLLOW_NEWLINE_in_control681); 

                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);

                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:34:81: ( name NEWLINE )?
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==NAME) ) {
                        alt21=1;
                    }
                    switch (alt21) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:34:82: name NEWLINE
                            {
                            pushFollow(FOLLOW_name_in_control686);
                            name();
                            _fsp--;

                            match(input,NEWLINE,FOLLOW_NEWLINE_in_control688); 

                            }
                            break;

                    }

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_control692); 
                    img =new NLGraphicsWindow(bb,listOfInts);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return img;
    }
    // $ANTLR end control


    // $ANTLR start bounding_box
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:37:1: bounding_box returns [Rectangle rect] : x1= number NEWLINE y1= number NEWLINE x2= number NEWLINE y2= number NEWLINE ;
    public final Rectangle bounding_box() throws RecognitionException {
        Rectangle rect = null;

        number_return x1 = null;

        number_return y1 = null;

        number_return x2 = null;

        number_return y2 = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:37:39: (x1= number NEWLINE y1= number NEWLINE x2= number NEWLINE y2= number NEWLINE )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:38:2: x1= number NEWLINE y1= number NEWLINE x2= number NEWLINE y2= number NEWLINE
            {
            pushFollow(FOLLOW_number_in_bounding_box712);
            x1=number();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_bounding_box714); 
            pushFollow(FOLLOW_number_in_bounding_box718);
            y1=number();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_bounding_box720); 
            pushFollow(FOLLOW_number_in_bounding_box724);
            x2=number();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_bounding_box726); 
            pushFollow(FOLLOW_number_in_bounding_box730);
            y2=number();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_bounding_box732); 
            rect =new Rectangle(x1.num, y1.num,(x2.num-x1.num),(y2.num-y1.num));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return rect;
    }
    // $ANTLR end bounding_box


    // $ANTLR start pen
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:41:1: pen returns [NLPen pen] : q= QUOTATION f= floatnum n1= number rgb= number showInLegend= bool NEWLINE ;
    public final NLPen pen() throws RecognitionException {
        NLPen pen = null;

        Token q=null;
        floatnum_return f = null;

        number_return n1 = null;

        number_return rgb = null;

        boolean showInLegend = false;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:41:25: (q= QUOTATION f= floatnum n1= number rgb= number showInLegend= bool NEWLINE )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:41:27: q= QUOTATION f= floatnum n1= number rgb= number showInLegend= bool NEWLINE
            {
            q=(Token)input.LT(1);
            match(input,QUOTATION,FOLLOW_QUOTATION_in_pen750); 
            pushFollow(FOLLOW_floatnum_in_pen754);
            f=floatnum();
            _fsp--;

            pushFollow(FOLLOW_number_in_pen758);
            n1=number();
            _fsp--;

            pushFollow(FOLLOW_number_in_pen762);
            rgb=number();
            _fsp--;

            pushFollow(FOLLOW_bool_in_pen766);
            showInLegend=bool();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_pen768); 
            pen =new NLPen(q.getText(), f.num, n1.num, rgb.num, showInLegend);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return pen;
    }
    // $ANTLR end pen

    public static class anything_return extends ParserRuleReturnScope {
    };

    // $ANTLR start anything
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:43:1: anything : (~ NEWLINE )+ NEWLINE ;
    public final anything_return anything() throws RecognitionException {
        anything_return retval = new anything_return();
        retval.start = input.LT(1);

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:43:9: ( (~ NEWLINE )+ NEWLINE )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:43:11: (~ NEWLINE )+ NEWLINE
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:43:11: (~ NEWLINE )+
            int cnt23=0;
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( ((LA23_0>=QUOTATION && LA23_0<=24)) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:43:12: ~ NEWLINE
            	    {
            	    if ( (input.LA(1)>=QUOTATION && input.LA(1)<=24) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_anything780);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt23 >= 1 ) break loop23;
                        EarlyExitException eee =
                            new EarlyExitException(23, input);
                        throw eee;
                }
                cnt23++;
            } while (true);

            match(input,NEWLINE,FOLLOW_NEWLINE_in_anything785); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end anything


    // $ANTLR start name
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:45:1: name returns [String s] : (n= NAME ) ( (n1= NAME ) | (n2= NUMBER ) )* ;
    public final String name() throws RecognitionException {
        String s = null;

        Token n=null;
        Token n1=null;
        Token n2=null;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:45:25: ( (n= NAME ) ( (n1= NAME ) | (n2= NUMBER ) )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:46:2: (n= NAME ) ( (n1= NAME ) | (n2= NUMBER ) )*
            {
             StringBuffer sb = new StringBuffer(); 
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:2: (n= NAME )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:3: n= NAME
            {
            n=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_name804); 
            sb.append(n.getText());

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:33: ( (n1= NAME ) | (n2= NUMBER ) )*
            loop24:
            do {
                int alt24=3;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==NAME) ) {
                    alt24=1;
                }
                else if ( (LA24_0==NUMBER) ) {
                    alt24=2;
                }


                switch (alt24) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:34: (n1= NAME )
            	    {
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:34: (n1= NAME )
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:35: n1= NAME
            	    {
            	    n1=(Token)input.LT(1);
            	    match(input,NAME,FOLLOW_NAME_in_name813); 
            	    sb.append(" "); sb.append(n1.getText());

            	    }


            	    }
            	    break;
            	case 2 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:85: (n2= NUMBER )
            	    {
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:85: (n2= NUMBER )
            	    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:47:86: n2= NUMBER
            	    {
            	    n2=(Token)input.LT(1);
            	    match(input,NUMBER,FOLLOW_NUMBER_in_name823); 
            	    sb.append(" "); sb.append(n2.getText());

            	    }


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);

             s = sb.toString(); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return s;
    }
    // $ANTLR end name


    // $ANTLR start quote
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:51:1: quote returns [String q] : QUOTATION ;
    public final String quote() throws RecognitionException {
        String q = null;

        Token QUOTATION1=null;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:51:25: ( QUOTATION )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:51:27: QUOTATION
            {
            QUOTATION1=(Token)input.LT(1);
            match(input,QUOTATION,FOLLOW_QUOTATION_in_quote846); 
            q = QUOTATION1.getText();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return q;
    }
    // $ANTLR end quote


    // $ANTLR start bool
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:53:1: bool returns [boolean b] : ( 'true' | 'false' );
    public final boolean bool() throws RecognitionException {
        boolean b = false;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:53:26: ( 'true' | 'false' )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==23) ) {
                alt25=1;
            }
            else if ( (LA25_0==24) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("53:1: bool returns [boolean b] : ( 'true' | 'false' );", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:53:28: 'true'
                    {
                    match(input,23,FOLLOW_23_in_bool861); 
                    b =true;

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:53:48: 'false'
                    {
                    match(input,24,FOLLOW_24_in_bool867); 
                    b =false;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return b;
    }
    // $ANTLR end bool


    // $ANTLR start point
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:55:1: point returns [Point pt] : x= number y= number ;
    public final Point point() throws RecognitionException {
        Point pt = null;

        number_return x = null;

        number_return y = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:55:26: (x= number y= number )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:55:28: x= number y= number
            {
            pushFollow(FOLLOW_number_in_point884);
            x=number();
            _fsp--;

            pushFollow(FOLLOW_number_in_point889);
            y=number();
            _fsp--;

            pt = new Point(x.num,y.num); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return pt;
    }
    // $ANTLR end point

    public static class number_return extends ParserRuleReturnScope {
        public int num;
    };

    // $ANTLR start number
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:57:1: number returns [int num] : NUMBER ;
    public final number_return number() throws RecognitionException {
        number_return retval = new number_return();
        retval.start = input.LT(1);

        Token NUMBER2=null;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:57:25: ( NUMBER )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:57:27: NUMBER
            {
            NUMBER2=(Token)input.LT(1);
            match(input,NUMBER,FOLLOW_NUMBER_in_number902); 
            retval.num = Integer.parseInt(NUMBER2.getText());

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end number

    public static class floatnum_return extends ParserRuleReturnScope {
        public double num;
    };

    // $ANTLR start floatnum
    // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:59:1: floatnum returns [double num] : FLOAT ;
    public final floatnum_return floatnum() throws RecognitionException {
        floatnum_return retval = new floatnum_return();
        retval.start = input.LT(1);

        Token FLOAT3=null;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:59:30: ( FLOAT )
            // C:\\projects\\netlogo\\grammars\\NetLogoInterface.g:59:32: FLOAT
            {
            FLOAT3=(Token)input.LT(1);
            match(input,FLOAT,FOLLOW_FLOAT_in_floatnum916); 
            retval.num = Double.parseDouble(FLOAT3.getText());

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end floatnum


 

    public static final BitSet FOLLOW_control_in_control_section35 = new BitSet(new long[]{0x00000000007BF802L});
    public static final BitSet FOLLOW_11_in_control60 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control62 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control66 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control70 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control75 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_NEWLINE_in_control79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_control86 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control88 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control92 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control96 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control100 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_number_in_control107 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control111 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_floatnum_in_control117 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control121 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_number_in_control127 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control131 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_floatnum_in_control137 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control141 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_anything_in_control145 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_number_in_control153 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control157 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_floatnum_in_control163 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control167 = new BitSet(new long[]{0x0000000000000180L});
    public static final BitSet FOLLOW_number_in_control173 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control177 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_floatnum_in_control183 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control187 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control192 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control194 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control198 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control203 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_control214 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control218 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control222 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control226 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control230 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_control234 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control236 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control240 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control242 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_control246 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control248 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_name_in_control252 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control254 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control259 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control266 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control273 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control277 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_control292 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control294 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control298 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control302 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control306 = new BitSet(new long[]{0x00000000000001E0L});
    public static final BitSet FOLLOW_quote_in_control312 = new BitSet(new long[]{0x00000000000001F0L});
    public static final BitSet FOLLOW_name_in_control321 = new BitSet(new long[]{0x00000000000001F0L});
    public static final BitSet FOLLOW_number_in_control330 = new BitSet(new long[]{0x00000000000001F0L});
    public static final BitSet FOLLOW_floatnum_in_control339 = new BitSet(new long[]{0x00000000000001F0L});
    public static final BitSet FOLLOW_NEWLINE_in_control346 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control350 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control352 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_control361 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control363 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control367 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control371 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control375 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control379 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control381 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control385 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control387 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control391 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control393 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_control402 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control404 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control408 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control411 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_NEWLINE_in_control415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_control422 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control424 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control428 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control432 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control436 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control440 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_floatnum_in_control444 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control446 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_floatnum_in_control450 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control452 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_floatnum_in_control456 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control458 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_floatnum_in_control462 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control464 = new BitSet(new long[]{0x0000000001800000L});
    public static final BitSet FOLLOW_bool_in_control468 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control470 = new BitSet(new long[]{0x0000000001800000L});
    public static final BitSet FOLLOW_bool_in_control474 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control476 = new BitSet(new long[]{0x0000000000040010L});
    public static final BitSet FOLLOW_18_in_control481 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control483 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_pen_in_control488 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_NEWLINE_in_control497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_control504 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control506 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control510 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control514 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control518 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control522 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control524 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control528 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control530 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control535 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_control546 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control548 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control552 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control556 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control560 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_bool_in_control569 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_number_in_control573 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control576 = new BitSet(new long[]{0x0000000001800080L});
    public static final BitSet FOLLOW_bool_in_control579 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_number_in_control583 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control586 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_anything_in_control593 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_control607 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control609 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control613 = new BitSet(new long[]{0x0000000001FFFFE0L});
    public static final BitSet FOLLOW_anything_in_control617 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_number_in_control624 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control628 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_control639 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control641 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bounding_box_in_control645 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control647 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control649 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control651 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control653 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_floatnum_in_control655 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control657 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control662 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control664 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control666 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control668 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control670 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control672 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_control674 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control676 = new BitSet(new long[]{0x00000000000000D0L});
    public static final BitSet FOLLOW_number_in_control679 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control681 = new BitSet(new long[]{0x00000000000000D0L});
    public static final BitSet FOLLOW_name_in_control686 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control688 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_control692 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_bounding_box712 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_bounding_box714 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_bounding_box718 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_bounding_box720 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_bounding_box724 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_bounding_box726 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_bounding_box730 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_bounding_box732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUOTATION_in_pen750 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_floatnum_in_pen754 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_pen758 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_pen762 = new BitSet(new long[]{0x0000000001800000L});
    public static final BitSet FOLLOW_bool_in_pen766 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_pen768 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_anything780 = new BitSet(new long[]{0x0000000001FFFFF0L});
    public static final BitSet FOLLOW_NEWLINE_in_anything785 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_name804 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_NAME_in_name813 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_NUMBER_in_name823 = new BitSet(new long[]{0x00000000000000C2L});
    public static final BitSet FOLLOW_QUOTATION_in_quote846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_bool861 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_bool867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_point884 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_number_in_point889 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_number902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_floatnum916 = new BitSet(new long[]{0x0000000000000002L});

}