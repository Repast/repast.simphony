// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoImages.g 2008-06-12 17:50:15

package repast.simphony.relogo.ide.image;

import java.awt.Point;
import java.util.LinkedList;

import repast.simphony.relogo.ide.image.NLCirclePrimitive;
import repast.simphony.relogo.ide.image.NLImage;
import repast.simphony.relogo.ide.image.NLImagePrimitive;
import repast.simphony.relogo.ide.image.NLLinePrimitive;
import repast.simphony.relogo.ide.image.NLPolygonPrimitive;
import repast.simphony.relogo.ide.image.NLRectanglePrimitive;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class NetLogoImagesParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEWLINE", "NAME", "NUMBER", "SECT", "WS", "'true'", "'false'", "'Line'", "'Circle'", "'Rectangle'", "'Polygon'"
    };
    public static final int NAME=5;
    public static final int WS=8;
    public static final int NEWLINE=4;
    public static final int SECT=7;
    public static final int NUMBER=6;
    public static final int EOF=-1;

        public NetLogoImagesParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoImages.g"; }



    // $ANTLR start model
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:20:1: model returns [List<NLImage> images] : is= image_section ;
    public final List<NLImage> model() throws RecognitionException {
        List<NLImage> images = null;

        List<NLImage> is = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:20:38: (is= image_section )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:20:40: is= image_section
            {
            pushFollow(FOLLOW_image_section_in_model31);
            is=image_section();
            _fsp--;

             images = is; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return images;
    }
    // $ANTLR end model


    // $ANTLR start image_section
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:23:1: image_section returns [List<NLImage> images] : (img= image )* ;
    public final List<NLImage> image_section() throws RecognitionException {
        List<NLImage> images = null;

        NLImage img = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:24:2: ( (img= image )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:24:4: (img= image )*
            {
            LinkedList<NLImage> list = new LinkedList<NLImage>();
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:24:60: (img= image )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==NAME) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:24:61: img= image
            	    {
            	    pushFollow(FOLLOW_image_in_image_section64);
            	    img=image();
            	    _fsp--;

            	     list.add(img); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             images = list; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return images;
    }
    // $ANTLR end image_section


    // $ANTLR start image
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:28:1: image returns [NLImage img] : n= name NEWLINE rot= bool NEWLINE cc= number NEWLINE (sp= shape_prim )* NEWLINE ;
    public final NLImage image() throws RecognitionException {
        NLImage img = null;

        String n = null;

        boolean rot = false;

        int cc = 0;

        NLImagePrimitive sp = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:28:29: (n= name NEWLINE rot= bool NEWLINE cc= number NEWLINE (sp= shape_prim )* NEWLINE )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:29:2: n= name NEWLINE rot= bool NEWLINE cc= number NEWLINE (sp= shape_prim )* NEWLINE
            {
            pushFollow(FOLLOW_name_in_image90);
            n=name();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_image92); 
            pushFollow(FOLLOW_bool_in_image104);
            rot=bool();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_image106); 
            pushFollow(FOLLOW_number_in_image118);
            cc=number();
            _fsp--;

            match(input,NEWLINE,FOLLOW_NEWLINE_in_image120); 
             LinkedList<NLImagePrimitive> prims=new LinkedList<NLImagePrimitive>(); 
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:33:9: (sp= shape_prim )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=11 && LA2_0<=14)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:33:10: sp= shape_prim
            	    {
            	    pushFollow(FOLLOW_shape_prim_in_image144);
            	    sp=shape_prim();
            	    _fsp--;

            	    prims.add(sp);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match(input,NEWLINE,FOLLOW_NEWLINE_in_image159); 
            img = new NLImage(n, rot, cc, prims); 

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
    // $ANTLR end image


    // $ANTLR start name
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:37:1: name returns [String s] : n= NAME (n1= NAME | n2= NUMBER )* ;
    public final String name() throws RecognitionException {
        String s = null;

        Token n=null;
        Token n1=null;
        Token n2=null;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:37:25: (n= NAME (n1= NAME | n2= NUMBER )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:38:2: n= NAME (n1= NAME | n2= NUMBER )*
            {
             StringBuffer sb = new StringBuffer(); 
            n=(Token)input.LT(1);
            match(input,NAME,FOLLOW_NAME_in_name182); 
            sb.append(n.getText());
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:39:31: (n1= NAME | n2= NUMBER )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==NAME) ) {
                    alt3=1;
                }
                else if ( (LA3_0==NUMBER) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:39:33: n1= NAME
            	    {
            	    n1=(Token)input.LT(1);
            	    match(input,NAME,FOLLOW_NAME_in_name190); 
            	    sb.append(" "); sb.append(n1.getText());

            	    }
            	    break;
            	case 2 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:39:82: n2= NUMBER
            	    {
            	    n2=(Token)input.LT(1);
            	    match(input,NUMBER,FOLLOW_NUMBER_in_name198); 
            	    sb.append(" "); sb.append(n2.getText());

            	    }
            	    break;

            	default :
            	    break loop3;
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


    // $ANTLR start bool
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:43:1: bool returns [boolean b] : ( 'true' | 'false' );
    public final boolean bool() throws RecognitionException {
        boolean b = false;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:43:26: ( 'true' | 'false' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==9) ) {
                alt4=1;
            }
            else if ( (LA4_0==10) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("43:1: bool returns [boolean b] : ( 'true' | 'false' );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:43:28: 'true'
                    {
                    match(input,9,FOLLOW_9_in_bool221); 
                    b =true;

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:43:48: 'false'
                    {
                    match(input,10,FOLLOW_10_in_bool227); 
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


    // $ANTLR start shape_prim
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:45:1: shape_prim returns [NLImagePrimitive prim] : ( 'Line' n= number cc= bool fr= point to= point NEWLINE | 'Circle' n= number ol= bool cc= bool ul= point d= number NEWLINE | 'Rectangle' n= number ol= bool cc= bool ul= point lr= point NEWLINE | 'Polygon' n= number ol= bool cc= bool (pt= point )+ NEWLINE );
    public final NLImagePrimitive shape_prim() throws RecognitionException {
        NLImagePrimitive prim = null;

        int n = 0;

        boolean cc = false;

        Point fr = null;

        Point to = null;

        boolean ol = false;

        Point ul = null;

        int d = 0;

        Point lr = null;

        Point pt = null;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:46:2: ( 'Line' n= number cc= bool fr= point to= point NEWLINE | 'Circle' n= number ol= bool cc= bool ul= point d= number NEWLINE | 'Rectangle' n= number ol= bool cc= bool ul= point lr= point NEWLINE | 'Polygon' n= number ol= bool cc= bool (pt= point )+ NEWLINE )
            int alt6=4;
            switch ( input.LA(1) ) {
            case 11:
                {
                alt6=1;
                }
                break;
            case 12:
                {
                alt6=2;
                }
                break;
            case 13:
                {
                alt6=3;
                }
                break;
            case 14:
                {
                alt6=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("45:1: shape_prim returns [NLImagePrimitive prim] : ( 'Line' n= number cc= bool fr= point to= point NEWLINE | 'Circle' n= number ol= bool cc= bool ul= point d= number NEWLINE | 'Rectangle' n= number ol= bool cc= bool ul= point lr= point NEWLINE | 'Polygon' n= number ol= bool cc= bool (pt= point )+ NEWLINE );", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:46:4: 'Line' n= number cc= bool fr= point to= point NEWLINE
                    {
                    match(input,11,FOLLOW_11_in_shape_prim243); 
                    pushFollow(FOLLOW_number_in_shape_prim247);
                    n=number();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim252);
                    cc=bool();
                    _fsp--;

                    pushFollow(FOLLOW_point_in_shape_prim257);
                    fr=point();
                    _fsp--;

                    pushFollow(FOLLOW_point_in_shape_prim262);
                    to=point();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_shape_prim264); 
                    prim = new NLLinePrimitive(n, false, cc, fr, to); 

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:47:4: 'Circle' n= number ol= bool cc= bool ul= point d= number NEWLINE
                    {
                    match(input,12,FOLLOW_12_in_shape_prim271); 
                    pushFollow(FOLLOW_number_in_shape_prim276);
                    n=number();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim281);
                    ol=bool();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim286);
                    cc=bool();
                    _fsp--;

                    pushFollow(FOLLOW_point_in_shape_prim291);
                    ul=point();
                    _fsp--;

                    pushFollow(FOLLOW_number_in_shape_prim296);
                    d=number();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_shape_prim298); 
                    prim = new NLCirclePrimitive(n, ol, cc, ul, d); 

                    }
                    break;
                case 3 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:48:4: 'Rectangle' n= number ol= bool cc= bool ul= point lr= point NEWLINE
                    {
                    match(input,13,FOLLOW_13_in_shape_prim305); 
                    pushFollow(FOLLOW_number_in_shape_prim310);
                    n=number();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim315);
                    ol=bool();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim320);
                    cc=bool();
                    _fsp--;

                    pushFollow(FOLLOW_point_in_shape_prim325);
                    ul=point();
                    _fsp--;

                    pushFollow(FOLLOW_point_in_shape_prim330);
                    lr=point();
                    _fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_shape_prim332); 
                    prim = new NLRectanglePrimitive(n, ol, cc, ul, lr); 

                    }
                    break;
                case 4 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:49:4: 'Polygon' n= number ol= bool cc= bool (pt= point )+ NEWLINE
                    {
                    match(input,14,FOLLOW_14_in_shape_prim339); 
                     LinkedList<Point> points=new LinkedList<Point>(); 
                    pushFollow(FOLLOW_number_in_shape_prim346);
                    n=number();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim351);
                    ol=bool();
                    _fsp--;

                    pushFollow(FOLLOW_bool_in_shape_prim356);
                    cc=bool();
                    _fsp--;


                    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:49:99: (pt= point )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==NUMBER) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:49:101: pt= point
                    	    {
                    	    pushFollow(FOLLOW_point_in_shape_prim364);
                    	    pt=point();
                    	    _fsp--;

                    	    points.add(pt);

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

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_shape_prim371); 
                    prim = new NLPolygonPrimitive(n, ol, cc, points); 

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
        return prim;
    }
    // $ANTLR end shape_prim


    // $ANTLR start point
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:52:1: point returns [Point pt] : x= number y= number ;
    public final Point point() throws RecognitionException {
        Point pt = null;

        int x = 0;

        int y = 0;


        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:52:26: (x= number y= number )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:52:28: x= number y= number
            {
            pushFollow(FOLLOW_number_in_point390);
            x=number();
            _fsp--;

            pushFollow(FOLLOW_number_in_point395);
            y=number();
            _fsp--;

            pt = new Point(x,y); 

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


    // $ANTLR start number
    // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:54:1: number returns [int num] : NUMBER ;
    public final int number() throws RecognitionException {
        int num = 0;

        Token NUMBER1=null;

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:54:25: ( NUMBER )
            // C:\\projects\\netlogo\\grammars\\NetLogoImages.g:54:27: NUMBER
            {
            NUMBER1=(Token)input.LT(1);
            match(input,NUMBER,FOLLOW_NUMBER_in_number408); 
            num = Integer.parseInt(NUMBER1.getText());

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return num;
    }
    // $ANTLR end number


 

    public static final BitSet FOLLOW_image_section_in_model31 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_image_in_image_section64 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_name_in_image90 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_image92 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_image104 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_image106 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_image118 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_image120 = new BitSet(new long[]{0x0000000000007810L});
    public static final BitSet FOLLOW_shape_prim_in_image144 = new BitSet(new long[]{0x0000000000007810L});
    public static final BitSet FOLLOW_NEWLINE_in_image159 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_name182 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_NAME_in_name190 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_NUMBER_in_name198 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_9_in_bool221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_10_in_bool227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_shape_prim243 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_shape_prim247 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim252 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_point_in_shape_prim257 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_point_in_shape_prim262 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_shape_prim264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_shape_prim271 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_shape_prim276 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim281 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim286 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_point_in_shape_prim291 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_shape_prim296 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_shape_prim298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_shape_prim305 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_shape_prim310 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim315 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim320 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_point_in_shape_prim325 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_point_in_shape_prim330 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_shape_prim332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_shape_prim339 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_shape_prim346 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim351 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_bool_in_shape_prim356 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_point_in_shape_prim364 = new BitSet(new long[]{0x0000000000000050L});
    public static final BitSet FOLLOW_NEWLINE_in_shape_prim371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_point390 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_number_in_point395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_number408 = new BitSet(new long[]{0x0000000000000002L});

}