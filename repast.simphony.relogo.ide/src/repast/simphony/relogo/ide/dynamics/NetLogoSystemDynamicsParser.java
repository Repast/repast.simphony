// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g 2008-07-16 19:32:37

package repast.simphony.relogo.ide.dynamics;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class NetLogoSystemDynamicsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "MODEL", "ENTRY", "REF", "STR", "INTG", "FLT", "INDENT", "FLOAT", "NEWLINE", "ID", "WS", "STRING", "INT"
    };
    public static final int INTG=8;
    public static final int STR=7;
    public static final int WS=14;
    public static final int NEWLINE=12;
    public static final int MODEL=4;
    public static final int INT=16;
    public static final int FLOAT=11;
    public static final int ENTRY=5;
    public static final int ID=13;
    public static final int EOF=-1;
    public static final int FLT=9;
    public static final int INDENT=10;
    public static final int STRING=15;
    public static final int REF=6;

        public NetLogoSystemDynamicsParser(TokenStream input) {
            super(input);
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g"; }


    public static class file_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start file
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:26:1: file : (mdl+= model )+ -> ( $mdl)* ;
    public final file_return file() throws RecognitionException {
        file_return retval = new file_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_mdl=null;
        RuleReturnScope mdl = null;
        RewriteRuleSubtreeStream stream_model=new RewriteRuleSubtreeStream(adaptor,"rule model");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:26:6: ( (mdl+= model )+ -> ( $mdl)* )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:26:8: (mdl+= model )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:26:11: (mdl+= model )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==FLOAT) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:26:11: mdl+= model
            	    {
            	    pushFollow(FOLLOW_model_in_file68);
            	    mdl=model();
            	    _fsp--;

            	    stream_model.add(mdl.getTree());
            	    if (list_mdl==null) list_mdl=new ArrayList();
            	    list_mdl.add(mdl);


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            // AST REWRITE
            // elements: mdl
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: mdl
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_mdl=new RewriteRuleSubtreeStream(adaptor,"token mdl",list_mdl);
            root_0 = (CommonTree)adaptor.nil();
            // 26:20: -> ( $mdl)*
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:26:23: ( $mdl)*
                while ( stream_mdl.hasNext() ) {
                    adaptor.addChild(root_0, ((ParserRuleReturnScope)stream_mdl.next()).getTree());

                }
                stream_mdl.reset();

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end file

    public static class model_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start model
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:1: model : f= FLOAT NEWLINE (ent+= entry )* -> ^( MODEL $f ( $ent)* ) ;
    public final model_return model() throws RecognitionException {
        model_return retval = new model_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token f=null;
        Token NEWLINE1=null;
        List list_ent=null;
        RuleReturnScope ent = null;
        CommonTree f_tree=null;
        CommonTree NEWLINE1_tree=null;
        RewriteRuleTokenStream stream_FLOAT=new RewriteRuleTokenStream(adaptor,"token FLOAT");
        RewriteRuleTokenStream stream_NEWLINE=new RewriteRuleTokenStream(adaptor,"token NEWLINE");
        RewriteRuleSubtreeStream stream_entry=new RewriteRuleSubtreeStream(adaptor,"rule entry");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:7: (f= FLOAT NEWLINE (ent+= entry )* -> ^( MODEL $f ( $ent)* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:9: f= FLOAT NEWLINE (ent+= entry )*
            {
            f=(Token)input.LT(1);
            match(input,FLOAT,FOLLOW_FLOAT_in_model85); 
            stream_FLOAT.add(f);

            NEWLINE1=(Token)input.LT(1);
            match(input,NEWLINE,FOLLOW_NEWLINE_in_model87); 
            stream_NEWLINE.add(NEWLINE1);

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:28: (ent+= entry )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==WS) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:28: ent+= entry
            	    {
            	    pushFollow(FOLLOW_entry_in_model91);
            	    ent=entry();
            	    _fsp--;

            	    stream_entry.add(ent.getTree());
            	    if (list_ent==null) list_ent=new ArrayList();
            	    list_ent.add(ent);


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            // AST REWRITE
            // elements: ent, f
            // token labels: f
            // rule labels: retval
            // token list labels: 
            // rule list labels: ent
            retval.tree = root_0;
            RewriteRuleTokenStream stream_f=new RewriteRuleTokenStream(adaptor,"token f",f);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ent=new RewriteRuleSubtreeStream(adaptor,"token ent",list_ent);
            root_0 = (CommonTree)adaptor.nil();
            // 28:37: -> ^( MODEL $f ( $ent)* )
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:40: ^( MODEL $f ( $ent)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(MODEL, "MODEL"), root_1);

                adaptor.addChild(root_1, stream_f.next());
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:28:51: ( $ent)*
                while ( stream_ent.hasNext() ) {
                    adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_ent.next()).getTree());

                }
                stream_ent.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end model

    public static class entry_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start entry
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:1: entry : w= wsp cls= ID ( WS (args+= ref | args+= str | args+= intg | args+= fltgt ) )* NEWLINE -> ^( ENTRY $w $cls ( $args)* ) ;
    public final entry_return entry() throws RecognitionException {
        entry_return retval = new entry_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token cls=null;
        Token WS2=null;
        Token NEWLINE3=null;
        List list_args=null;
        wsp_return w = null;

        RuleReturnScope args = null;
        CommonTree cls_tree=null;
        CommonTree WS2_tree=null;
        CommonTree NEWLINE3_tree=null;
        RewriteRuleTokenStream stream_WS=new RewriteRuleTokenStream(adaptor,"token WS");
        RewriteRuleTokenStream stream_NEWLINE=new RewriteRuleTokenStream(adaptor,"token NEWLINE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_ref=new RewriteRuleSubtreeStream(adaptor,"rule ref");
        RewriteRuleSubtreeStream stream_wsp=new RewriteRuleSubtreeStream(adaptor,"rule wsp");
        RewriteRuleSubtreeStream stream_intg=new RewriteRuleSubtreeStream(adaptor,"rule intg");
        RewriteRuleSubtreeStream stream_str=new RewriteRuleSubtreeStream(adaptor,"rule str");
        RewriteRuleSubtreeStream stream_fltgt=new RewriteRuleSubtreeStream(adaptor,"rule fltgt");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:7: (w= wsp cls= ID ( WS (args+= ref | args+= str | args+= intg | args+= fltgt ) )* NEWLINE -> ^( ENTRY $w $cls ( $args)* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:9: w= wsp cls= ID ( WS (args+= ref | args+= str | args+= intg | args+= fltgt ) )* NEWLINE
            {
            pushFollow(FOLLOW_wsp_in_entry115);
            w=wsp();
            _fsp--;

            stream_wsp.add(w.getTree());
            cls=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_entry119); 
            stream_ID.add(cls);

            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:22: ( WS (args+= ref | args+= str | args+= intg | args+= fltgt ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==WS) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:23: WS (args+= ref | args+= str | args+= intg | args+= fltgt )
            	    {
            	    WS2=(Token)input.LT(1);
            	    match(input,WS,FOLLOW_WS_in_entry122); 
            	    stream_WS.add(WS2);

            	    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:26: (args+= ref | args+= str | args+= intg | args+= fltgt )
            	    int alt3=4;
            	    switch ( input.LA(1) ) {
            	    case ID:
            	        {
            	        alt3=1;
            	        }
            	        break;
            	    case STRING:
            	        {
            	        alt3=2;
            	        }
            	        break;
            	    case INT:
            	        {
            	        alt3=3;
            	        }
            	        break;
            	    case FLOAT:
            	        {
            	        alt3=4;
            	        }
            	        break;
            	    default:
            	        NoViableAltException nvae =
            	            new NoViableAltException("30:26: (args+= ref | args+= str | args+= intg | args+= fltgt )", 3, 0, input);

            	        throw nvae;
            	    }

            	    switch (alt3) {
            	        case 1 :
            	            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:27: args+= ref
            	            {
            	            pushFollow(FOLLOW_ref_in_entry127);
            	            args=ref();
            	            _fsp--;

            	            stream_ref.add(args.getTree());
            	            if (list_args==null) list_args=new ArrayList();
            	            list_args.add(args);


            	            }
            	            break;
            	        case 2 :
            	            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:39: args+= str
            	            {
            	            pushFollow(FOLLOW_str_in_entry133);
            	            args=str();
            	            _fsp--;

            	            stream_str.add(args.getTree());
            	            if (list_args==null) list_args=new ArrayList();
            	            list_args.add(args);


            	            }
            	            break;
            	        case 3 :
            	            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:51: args+= intg
            	            {
            	            pushFollow(FOLLOW_intg_in_entry139);
            	            args=intg();
            	            _fsp--;

            	            stream_intg.add(args.getTree());
            	            if (list_args==null) list_args=new ArrayList();
            	            list_args.add(args);


            	            }
            	            break;
            	        case 4 :
            	            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:64: args+= fltgt
            	            {
            	            pushFollow(FOLLOW_fltgt_in_entry145);
            	            args=fltgt();
            	            _fsp--;

            	            stream_fltgt.add(args.getTree());
            	            if (list_args==null) list_args=new ArrayList();
            	            list_args.add(args);


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            NEWLINE3=(Token)input.LT(1);
            match(input,NEWLINE,FOLLOW_NEWLINE_in_entry150); 
            stream_NEWLINE.add(NEWLINE3);


            // AST REWRITE
            // elements: w, args, cls
            // token labels: cls
            // rule labels: w, retval
            // token list labels: 
            // rule list labels: args
            retval.tree = root_0;
            RewriteRuleTokenStream stream_cls=new RewriteRuleTokenStream(adaptor,"token cls",cls);
            RewriteRuleSubtreeStream stream_w=new RewriteRuleSubtreeStream(adaptor,"token w",w!=null?w.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"token args",list_args);
            root_0 = (CommonTree)adaptor.nil();
            // 30:87: -> ^( ENTRY $w $cls ( $args)* )
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:90: ^( ENTRY $w $cls ( $args)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(ENTRY, "ENTRY"), root_1);

                adaptor.addChild(root_1, stream_w.next());
                adaptor.addChild(root_1, stream_cls.next());
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:30:106: ( $args)*
                while ( stream_args.hasNext() ) {
                    adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_args.next()).getTree());

                }
                stream_args.reset();

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end entry

    public static class ref_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start ref
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:32:1: ref : a1= ID -> ^( REF $a1) ;
    public final ref_return ref() throws RecognitionException {
        ref_return retval = new ref_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a1=null;

        CommonTree a1_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:32:5: (a1= ID -> ^( REF $a1) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:32:7: a1= ID
            {
            a1=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_ref176); 
            stream_ID.add(a1);


            // AST REWRITE
            // elements: a1
            // token labels: a1
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_a1=new RewriteRuleTokenStream(adaptor,"token a1",a1);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 32:13: -> ^( REF $a1)
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:32:16: ^( REF $a1)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(REF, "REF"), root_1);

                adaptor.addChild(root_1, stream_a1.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end ref

    public static class str_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start str
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:34:1: str : a2= STRING -> ^( STR $a2) ;
    public final str_return str() throws RecognitionException {
        str_return retval = new str_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a2=null;

        CommonTree a2_tree=null;
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:34:5: (a2= STRING -> ^( STR $a2) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:34:7: a2= STRING
            {
            a2=(Token)input.LT(1);
            match(input,STRING,FOLLOW_STRING_in_str195); 
            stream_STRING.add(a2);


            // AST REWRITE
            // elements: a2
            // token labels: a2
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_a2=new RewriteRuleTokenStream(adaptor,"token a2",a2);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 34:17: -> ^( STR $a2)
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:34:20: ^( STR $a2)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(STR, "STR"), root_1);

                adaptor.addChild(root_1, stream_a2.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end str

    public static class intg_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start intg
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:36:1: intg : a3= INT -> ^( INTG $a3) ;
    public final intg_return intg() throws RecognitionException {
        intg_return retval = new intg_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a3=null;

        CommonTree a3_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:36:6: (a3= INT -> ^( INTG $a3) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:36:8: a3= INT
            {
            a3=(Token)input.LT(1);
            match(input,INT,FOLLOW_INT_in_intg214); 
            stream_INT.add(a3);


            // AST REWRITE
            // elements: a3
            // token labels: a3
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_a3=new RewriteRuleTokenStream(adaptor,"token a3",a3);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 36:15: -> ^( INTG $a3)
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:36:18: ^( INTG $a3)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(INTG, "INTG"), root_1);

                adaptor.addChild(root_1, stream_a3.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end intg

    public static class fltgt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start fltgt
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:38:1: fltgt : a4= FLOAT -> ^( FLT $a4) ;
    public final fltgt_return fltgt() throws RecognitionException {
        fltgt_return retval = new fltgt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a4=null;

        CommonTree a4_tree=null;
        RewriteRuleTokenStream stream_FLOAT=new RewriteRuleTokenStream(adaptor,"token FLOAT");

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:38:7: (a4= FLOAT -> ^( FLT $a4) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:38:9: a4= FLOAT
            {
            a4=(Token)input.LT(1);
            match(input,FLOAT,FOLLOW_FLOAT_in_fltgt233); 
            stream_FLOAT.add(a4);


            // AST REWRITE
            // elements: a4
            // token labels: a4
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_a4=new RewriteRuleTokenStream(adaptor,"token a4",a4);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 38:18: -> ^( FLT $a4)
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:38:21: ^( FLT $a4)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(FLT, "FLT"), root_1);

                adaptor.addChild(root_1, stream_a4.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end fltgt

    public static class wsp_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start wsp
    // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:40:1: wsp : ws= WS -> ^( INDENT $ws) ;
    public final wsp_return wsp() throws RecognitionException {
        wsp_return retval = new wsp_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ws=null;

        CommonTree ws_tree=null;
        RewriteRuleTokenStream stream_WS=new RewriteRuleTokenStream(adaptor,"token WS");

        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:40:5: (ws= WS -> ^( INDENT $ws) )
            // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:40:7: ws= WS
            {
            ws=(Token)input.LT(1);
            match(input,WS,FOLLOW_WS_in_wsp252); 
            stream_WS.add(ws);


            // AST REWRITE
            // elements: ws
            // token labels: ws
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_ws=new RewriteRuleTokenStream(adaptor,"token ws",ws);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 40:13: -> ^( INDENT $ws)
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoSystemDynamics.g:40:16: ^( INDENT $ws)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(INDENT, "INDENT"), root_1);

                adaptor.addChild(root_1, stream_ws.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end wsp


 

    public static final BitSet FOLLOW_model_in_file68 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_FLOAT_in_model85 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_NEWLINE_in_model87 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_entry_in_model91 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_wsp_in_entry115 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_ID_in_entry119 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_WS_in_entry122 = new BitSet(new long[]{0x000000000001A800L});
    public static final BitSet FOLLOW_ref_in_entry127 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_str_in_entry133 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_intg_in_entry139 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_fltgt_in_entry145 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_NEWLINE_in_entry150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_ref176 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_str195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_intg214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_fltgt233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_wsp252 = new BitSet(new long[]{0x0000000000000002L});

}