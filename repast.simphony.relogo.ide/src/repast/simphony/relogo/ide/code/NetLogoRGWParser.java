// $ANTLR 3.0.1 C:\\projects\\netlogo\\grammars\\NetLogoRGW.g 2008-07-23 16:47:54

package repast.simphony.relogo.ide.code;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class NetLogoRGWParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ARGS", "CS", "CB", "IDN", "STR", "BREED", "GLOBALS", "INCLUDES", "INUM", "FNUM", "OWN", "ToEndBlock", "ToReportBlock", "Paren", "ID", "STRING", "ARITHSYM", "INT", "FLOAT", "COMMENT", "NEWLINE", "WS", "'breed'", "'['", "']'", "'globals'", "'__includes'", "'to'", "'end'", "'to-report'", "'('", "')'"
    };
    public static final int ARITHSYM=20;
    public static final int GLOBALS=10;
    public static final int FNUM=13;
    public static final int CB=6;
    public static final int BREED=9;
    public static final int ToEndBlock=15;
    public static final int FLOAT=22;
    public static final int INT=21;
    public static final int ToReportBlock=16;
    public static final int ID=18;
    public static final int EOF=-1;
    public static final int IDN=7;
    public static final int INCLUDES=11;
    public static final int STR=8;
    public static final int WS=25;
    public static final int OWN=14;
    public static final int NEWLINE=24;
    public static final int CS=5;
    public static final int INUM=12;
    public static final int Paren=17;
    public static final int ARGS=4;
    public static final int COMMENT=23;
    public static final int STRING=19;

        public NetLogoRGWParser(TokenStream input) {
            super(input);
            ruleMemo = new HashMap[35+1];
         }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\projects\\netlogo\\grammars\\NetLogoRGW.g"; }


    public static class prog_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start prog
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:35:1: prog : ( stat )* ;
    public final prog_return prog() throws RecognitionException {
        prog_return retval = new prog_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        stat_return stat1 = null;



        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:35:6: ( ( stat )* )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:35:8: ( stat )*
            {
            root_0 = (CommonTree)adaptor.nil();

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:35:8: ( stat )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID||LA1_0==26||(LA1_0>=29 && LA1_0<=31)||LA1_0==33) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:0:0: stat
            	    {
            	    pushFollow(FOLLOW_stat_in_prog94);
            	    stat1=stat();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) adaptor.addChild(root_0, stat1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end prog

    public static class stat_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start stat
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:39:1: stat : ( 'breed' '[' a= ID (b= ID )? ']' -> ^( BREED $a ( $b)* ) | 'globals' codeBlock -> ^( GLOBALS codeBlock ) | '__includes' codeBlock -> ^( INCLUDES codeBlock ) | a= ID codeBlock -> ^( OWN $a codeBlock ) | toEndBlock -> toEndBlock | toReportBlock -> toReportBlock );
    public final stat_return stat() throws RecognitionException {
        stat_return retval = new stat_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a=null;
        Token b=null;
        Token string_literal2=null;
        Token char_literal3=null;
        Token char_literal4=null;
        Token string_literal5=null;
        Token string_literal7=null;
        codeBlock_return codeBlock6 = null;

        codeBlock_return codeBlock8 = null;

        codeBlock_return codeBlock9 = null;

        toEndBlock_return toEndBlock10 = null;

        toReportBlock_return toReportBlock11 = null;


        CommonTree a_tree=null;
        CommonTree b_tree=null;
        CommonTree string_literal2_tree=null;
        CommonTree char_literal3_tree=null;
        CommonTree char_literal4_tree=null;
        CommonTree string_literal5_tree=null;
        CommonTree string_literal7_tree=null;
        RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleTokenStream stream_29=new RewriteRuleTokenStream(adaptor,"token 29");
        RewriteRuleSubtreeStream stream_toEndBlock=new RewriteRuleSubtreeStream(adaptor,"rule toEndBlock");
        RewriteRuleSubtreeStream stream_toReportBlock=new RewriteRuleSubtreeStream(adaptor,"rule toReportBlock");
        RewriteRuleSubtreeStream stream_codeBlock=new RewriteRuleSubtreeStream(adaptor,"rule codeBlock");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:39:5: ( 'breed' '[' a= ID (b= ID )? ']' -> ^( BREED $a ( $b)* ) | 'globals' codeBlock -> ^( GLOBALS codeBlock ) | '__includes' codeBlock -> ^( INCLUDES codeBlock ) | a= ID codeBlock -> ^( OWN $a codeBlock ) | toEndBlock -> toEndBlock | toReportBlock -> toReportBlock )
            int alt3=6;
            switch ( input.LA(1) ) {
            case 26:
                {
                alt3=1;
                }
                break;
            case 29:
                {
                alt3=2;
                }
                break;
            case 30:
                {
                alt3=3;
                }
                break;
            case ID:
                {
                alt3=4;
                }
                break;
            case 31:
                {
                alt3=5;
                }
                break;
            case 33:
                {
                alt3=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("39:1: stat : ( 'breed' '[' a= ID (b= ID )? ']' -> ^( BREED $a ( $b)* ) | 'globals' codeBlock -> ^( GLOBALS codeBlock ) | '__includes' codeBlock -> ^( INCLUDES codeBlock ) | a= ID codeBlock -> ^( OWN $a codeBlock ) | toEndBlock -> toEndBlock | toReportBlock -> toReportBlock );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:39:7: 'breed' '[' a= ID (b= ID )? ']'
                    {
                    string_literal2=(Token)input.LT(1);
                    match(input,26,FOLLOW_26_in_stat105); if (failed) return retval;
                    if ( backtracking==0 ) stream_26.add(string_literal2);

                    char_literal3=(Token)input.LT(1);
                    match(input,27,FOLLOW_27_in_stat107); if (failed) return retval;
                    if ( backtracking==0 ) stream_27.add(char_literal3);

                    a=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_stat111); if (failed) return retval;
                    if ( backtracking==0 ) stream_ID.add(a);

                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:39:25: (b= ID )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==ID) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:0:0: b= ID
                            {
                            b=(Token)input.LT(1);
                            match(input,ID,FOLLOW_ID_in_stat115); if (failed) return retval;
                            if ( backtracking==0 ) stream_ID.add(b);


                            }
                            break;

                    }

                    char_literal4=(Token)input.LT(1);
                    match(input,28,FOLLOW_28_in_stat118); if (failed) return retval;
                    if ( backtracking==0 ) stream_28.add(char_literal4);


                    // AST REWRITE
                    // elements: a, b
                    // token labels: b, a
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_b=new RewriteRuleTokenStream(adaptor,"token b",b);
                    RewriteRuleTokenStream stream_a=new RewriteRuleTokenStream(adaptor,"token a",a);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 39:35: -> ^( BREED $a ( $b)* )
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:39:38: ^( BREED $a ( $b)* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(BREED, "BREED"), root_1);

                        adaptor.addChild(root_1, stream_a.next());
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:39:49: ( $b)*
                        while ( stream_b.hasNext() ) {
                            adaptor.addChild(root_1, stream_b.next());

                        }
                        stream_b.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:40:4: 'globals' codeBlock
                    {
                    string_literal5=(Token)input.LT(1);
                    match(input,29,FOLLOW_29_in_stat137); if (failed) return retval;
                    if ( backtracking==0 ) stream_29.add(string_literal5);

                    pushFollow(FOLLOW_codeBlock_in_stat139);
                    codeBlock6=codeBlock();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) stream_codeBlock.add(codeBlock6.getTree());

                    // AST REWRITE
                    // elements: codeBlock
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 40:25: -> ^( GLOBALS codeBlock )
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:40:28: ^( GLOBALS codeBlock )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(GLOBALS, "GLOBALS"), root_1);

                        adaptor.addChild(root_1, stream_codeBlock.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 3 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:41:4: '__includes' codeBlock
                    {
                    string_literal7=(Token)input.LT(1);
                    match(input,30,FOLLOW_30_in_stat153); if (failed) return retval;
                    if ( backtracking==0 ) stream_30.add(string_literal7);

                    pushFollow(FOLLOW_codeBlock_in_stat155);
                    codeBlock8=codeBlock();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) stream_codeBlock.add(codeBlock8.getTree());

                    // AST REWRITE
                    // elements: codeBlock
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 41:27: -> ^( INCLUDES codeBlock )
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:41:30: ^( INCLUDES codeBlock )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(INCLUDES, "INCLUDES"), root_1);

                        adaptor.addChild(root_1, stream_codeBlock.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 4 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:42:4: a= ID codeBlock
                    {
                    a=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_stat170); if (failed) return retval;
                    if ( backtracking==0 ) stream_ID.add(a);

                    pushFollow(FOLLOW_codeBlock_in_stat172);
                    codeBlock9=codeBlock();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) stream_codeBlock.add(codeBlock9.getTree());

                    // AST REWRITE
                    // elements: codeBlock, a
                    // token labels: a
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_a=new RewriteRuleTokenStream(adaptor,"token a",a);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 42:20: -> ^( OWN $a codeBlock )
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:42:23: ^( OWN $a codeBlock )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(OWN, "OWN"), root_1);

                        adaptor.addChild(root_1, stream_a.next());
                        adaptor.addChild(root_1, stream_codeBlock.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 5 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:43:5: toEndBlock
                    {
                    pushFollow(FOLLOW_toEndBlock_in_stat190);
                    toEndBlock10=toEndBlock();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) stream_toEndBlock.add(toEndBlock10.getTree());

                    // AST REWRITE
                    // elements: toEndBlock
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 43:16: -> toEndBlock
                    {
                        adaptor.addChild(root_0, stream_toEndBlock.next());

                    }

                    }

                    }
                    break;
                case 6 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:44:5: toReportBlock
                    {
                    pushFollow(FOLLOW_toReportBlock_in_stat200);
                    toReportBlock11=toReportBlock();
                    _fsp--;
                    if (failed) return retval;
                    if ( backtracking==0 ) stream_toReportBlock.add(toReportBlock11.getTree());

                    // AST REWRITE
                    // elements: toReportBlock
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 44:19: -> toReportBlock
                    {
                        adaptor.addChild(root_0, stream_toReportBlock.next());

                    }

                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end stat

    public static class toEndBlock_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start toEndBlock
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:47:1: toEndBlock : 'to' a= ID ( '[' (args+= codeElement )+ ']' )? (c+= codeLine )* 'end' -> ^( ToEndBlock $a ^( ARGS ( $args)* ) ( $c)* ) ;
    public final toEndBlock_return toEndBlock() throws RecognitionException {
        toEndBlock_return retval = new toEndBlock_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a=null;
        Token string_literal12=null;
        Token char_literal13=null;
        Token char_literal14=null;
        Token string_literal15=null;
        List list_args=null;
        List list_c=null;
        RuleReturnScope args = null;
        RuleReturnScope c = null;
        CommonTree a_tree=null;
        CommonTree string_literal12_tree=null;
        CommonTree char_literal13_tree=null;
        CommonTree char_literal14_tree=null;
        CommonTree string_literal15_tree=null;
        RewriteRuleTokenStream stream_32=new RewriteRuleTokenStream(adaptor,"token 32");
        RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleSubtreeStream stream_codeElement=new RewriteRuleSubtreeStream(adaptor,"rule codeElement");
        RewriteRuleSubtreeStream stream_codeLine=new RewriteRuleSubtreeStream(adaptor,"rule codeLine");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:2: ( 'to' a= ID ( '[' (args+= codeElement )+ ']' )? (c+= codeLine )* 'end' -> ^( ToEndBlock $a ^( ARGS ( $args)* ) ( $c)* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:4: 'to' a= ID ( '[' (args+= codeElement )+ ']' )? (c+= codeLine )* 'end'
            {
            string_literal12=(Token)input.LT(1);
            match(input,31,FOLLOW_31_in_toEndBlock214); if (failed) return retval;
            if ( backtracking==0 ) stream_31.add(string_literal12);

            a=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_toEndBlock218); if (failed) return retval;
            if ( backtracking==0 ) stream_ID.add(a);

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:14: ( '[' (args+= codeElement )+ ']' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==27) ) {
                int LA5_1 = input.LA(2);

                if ( (synpred9()) ) {
                    alt5=1;
                }
            }
            switch (alt5) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:15: '[' (args+= codeElement )+ ']'
                    {
                    char_literal13=(Token)input.LT(1);
                    match(input,27,FOLLOW_27_in_toEndBlock221); if (failed) return retval;
                    if ( backtracking==0 ) stream_27.add(char_literal13);

                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:19: (args+= codeElement )+
                    int cnt4=0;
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0>=ID && LA4_0<=FLOAT)||LA4_0==26||LA4_0==29||LA4_0==31||(LA4_0>=33 && LA4_0<=34)) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:20: args+= codeElement
                    	    {
                    	    pushFollow(FOLLOW_codeElement_in_toEndBlock226);
                    	    args=codeElement();
                    	    _fsp--;
                    	    if (failed) return retval;
                    	    if ( backtracking==0 ) stream_codeElement.add(args.getTree());
                    	    if (list_args==null) list_args=new ArrayList();
                    	    list_args.add(args);


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt4 >= 1 ) break loop4;
                    	    if (backtracking>0) {failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);

                    char_literal14=(Token)input.LT(1);
                    match(input,28,FOLLOW_28_in_toEndBlock230); if (failed) return retval;
                    if ( backtracking==0 ) stream_28.add(char_literal14);


                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:47: (c+= codeLine )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>=ID && LA6_0<=FLOAT)||(LA6_0>=26 && LA6_0<=27)||LA6_0==29||LA6_0==31||(LA6_0>=33 && LA6_0<=34)) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:48: c+= codeLine
            	    {
            	    pushFollow(FOLLOW_codeLine_in_toEndBlock238);
            	    c=codeLine();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) stream_codeLine.add(c.getTree());
            	    if (list_c==null) list_c=new ArrayList();
            	    list_c.add(c);


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            string_literal15=(Token)input.LT(1);
            match(input,32,FOLLOW_32_in_toEndBlock242); if (failed) return retval;
            if ( backtracking==0 ) stream_32.add(string_literal15);


            // AST REWRITE
            // elements: args, a, c
            // token labels: a
            // rule labels: retval
            // token list labels: 
            // rule list labels: c, args
            if ( backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_a=new RewriteRuleTokenStream(adaptor,"token a",a);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_c=new RewriteRuleSubtreeStream(adaptor,"token c",list_c);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"token args",list_args);
            root_0 = (CommonTree)adaptor.nil();
            // 48:68: -> ^( ToEndBlock $a ^( ARGS ( $args)* ) ( $c)* )
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:49:15: ^( ToEndBlock $a ^( ARGS ( $args)* ) ( $c)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(ToEndBlock, "ToEndBlock"), root_1);

                adaptor.addChild(root_1, stream_a.next());
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:49:31: ^( ARGS ( $args)* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot(adaptor.create(ARGS, "ARGS"), root_2);

                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:49:38: ( $args)*
                while ( stream_args.hasNext() ) {
                    adaptor.addChild(root_2, ((ParserRuleReturnScope)stream_args.next()).getTree());

                }
                stream_args.reset();

                adaptor.addChild(root_1, root_2);
                }
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:49:46: ( $c)*
                while ( stream_c.hasNext() ) {
                    adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_c.next()).getTree());

                }
                stream_c.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            }

            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end toEndBlock

    public static class toReportBlock_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start toReportBlock
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:52:1: toReportBlock : 'to-report' a= ID ( '[' (args+= codeElement )+ ']' )? (c+= codeLine )* 'end' -> ^( ToReportBlock $a ^( ARGS ( $args)* ) ( $c)* ) ;
    public final toReportBlock_return toReportBlock() throws RecognitionException {
        toReportBlock_return retval = new toReportBlock_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token a=null;
        Token string_literal16=null;
        Token char_literal17=null;
        Token char_literal18=null;
        Token string_literal19=null;
        List list_args=null;
        List list_c=null;
        RuleReturnScope args = null;
        RuleReturnScope c = null;
        CommonTree a_tree=null;
        CommonTree string_literal16_tree=null;
        CommonTree char_literal17_tree=null;
        CommonTree char_literal18_tree=null;
        CommonTree string_literal19_tree=null;
        RewriteRuleTokenStream stream_32=new RewriteRuleTokenStream(adaptor,"token 32");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleSubtreeStream stream_codeElement=new RewriteRuleSubtreeStream(adaptor,"rule codeElement");
        RewriteRuleSubtreeStream stream_codeLine=new RewriteRuleSubtreeStream(adaptor,"rule codeLine");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:2: ( 'to-report' a= ID ( '[' (args+= codeElement )+ ']' )? (c+= codeLine )* 'end' -> ^( ToReportBlock $a ^( ARGS ( $args)* ) ( $c)* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:4: 'to-report' a= ID ( '[' (args+= codeElement )+ ']' )? (c+= codeLine )* 'end'
            {
            string_literal16=(Token)input.LT(1);
            match(input,33,FOLLOW_33_in_toReportBlock287); if (failed) return retval;
            if ( backtracking==0 ) stream_33.add(string_literal16);

            a=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_toReportBlock291); if (failed) return retval;
            if ( backtracking==0 ) stream_ID.add(a);

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:21: ( '[' (args+= codeElement )+ ']' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==27) ) {
                int LA8_1 = input.LA(2);

                if ( (synpred12()) ) {
                    alt8=1;
                }
            }
            switch (alt8) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:22: '[' (args+= codeElement )+ ']'
                    {
                    char_literal17=(Token)input.LT(1);
                    match(input,27,FOLLOW_27_in_toReportBlock294); if (failed) return retval;
                    if ( backtracking==0 ) stream_27.add(char_literal17);

                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:26: (args+= codeElement )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>=ID && LA7_0<=FLOAT)||LA7_0==26||LA7_0==29||LA7_0==31||(LA7_0>=33 && LA7_0<=34)) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:27: args+= codeElement
                    	    {
                    	    pushFollow(FOLLOW_codeElement_in_toReportBlock299);
                    	    args=codeElement();
                    	    _fsp--;
                    	    if (failed) return retval;
                    	    if ( backtracking==0 ) stream_codeElement.add(args.getTree());
                    	    if (list_args==null) list_args=new ArrayList();
                    	    list_args.add(args);


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                    	    if (backtracking>0) {failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);

                    char_literal18=(Token)input.LT(1);
                    match(input,28,FOLLOW_28_in_toReportBlock303); if (failed) return retval;
                    if ( backtracking==0 ) stream_28.add(char_literal18);


                    }
                    break;

            }

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:53: (c+= codeLine )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>=ID && LA9_0<=FLOAT)||(LA9_0>=26 && LA9_0<=27)||LA9_0==29||LA9_0==31||(LA9_0>=33 && LA9_0<=34)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:54: c+= codeLine
            	    {
            	    pushFollow(FOLLOW_codeLine_in_toReportBlock310);
            	    c=codeLine();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) stream_codeLine.add(c.getTree());
            	    if (list_c==null) list_c=new ArrayList();
            	    list_c.add(c);


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            string_literal19=(Token)input.LT(1);
            match(input,32,FOLLOW_32_in_toReportBlock315); if (failed) return retval;
            if ( backtracking==0 ) stream_32.add(string_literal19);


            // AST REWRITE
            // elements: args, c, a
            // token labels: a
            // rule labels: retval
            // token list labels: 
            // rule list labels: c, args
            if ( backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_a=new RewriteRuleTokenStream(adaptor,"token a",a);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_c=new RewriteRuleSubtreeStream(adaptor,"token c",list_c);
            RewriteRuleSubtreeStream stream_args=new RewriteRuleSubtreeStream(adaptor,"token args",list_args);
            root_0 = (CommonTree)adaptor.nil();
            // 53:75: -> ^( ToReportBlock $a ^( ARGS ( $args)* ) ( $c)* )
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:54:15: ^( ToReportBlock $a ^( ARGS ( $args)* ) ( $c)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(ToReportBlock, "ToReportBlock"), root_1);

                adaptor.addChild(root_1, stream_a.next());
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:54:34: ^( ARGS ( $args)* )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot(adaptor.create(ARGS, "ARGS"), root_2);

                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:54:41: ( $args)*
                while ( stream_args.hasNext() ) {
                    adaptor.addChild(root_2, ((ParserRuleReturnScope)stream_args.next()).getTree());

                }
                stream_args.reset();

                adaptor.addChild(root_1, root_2);
                }
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:54:49: ( $c)*
                while ( stream_c.hasNext() ) {
                    adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_c.next()).getTree());

                }
                stream_c.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            }

            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end toReportBlock

    public static class codeLine_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start codeLine
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:1: codeLine : (a+= codeElement | a+= codeBlock )+ -> ^( CS ( $a)* ) ;
    public final codeLine_return codeLine() throws RecognitionException {
        codeLine_return retval = new codeLine_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        List list_a=null;
        RuleReturnScope a = null;
        RewriteRuleSubtreeStream stream_codeElement=new RewriteRuleSubtreeStream(adaptor,"rule codeElement");
        RewriteRuleSubtreeStream stream_codeBlock=new RewriteRuleSubtreeStream(adaptor,"rule codeBlock");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:9: ( (a+= codeElement | a+= codeBlock )+ -> ^( CS ( $a)* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:11: (a+= codeElement | a+= codeBlock )+
            {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:11: (a+= codeElement | a+= codeBlock )+
            int cnt10=0;
            loop10:
            do {
                int alt10=3;
                switch ( input.LA(1) ) {
                case 34:
                    {
                    int LA10_2 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case ID:
                    {
                    int LA10_3 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case 26:
                    {
                    int LA10_4 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case 29:
                    {
                    int LA10_5 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case 31:
                    {
                    int LA10_6 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case 33:
                    {
                    int LA10_7 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case STRING:
                    {
                    int LA10_8 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case ARITHSYM:
                    {
                    int LA10_9 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case INT:
                    {
                    int LA10_10 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case FLOAT:
                    {
                    int LA10_11 = input.LA(2);

                    if ( (synpred14()) ) {
                        alt10=1;
                    }


                    }
                    break;
                case 27:
                    {
                    int LA10_12 = input.LA(2);

                    if ( (synpred15()) ) {
                        alt10=2;
                    }


                    }
                    break;

                }

                switch (alt10) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:12: a+= codeElement
            	    {
            	    pushFollow(FOLLOW_codeElement_in_codeLine361);
            	    a=codeElement();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) stream_codeElement.add(a.getTree());
            	    if (list_a==null) list_a=new ArrayList();
            	    list_a.add(a);


            	    }
            	    break;
            	case 2 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:29: a+= codeBlock
            	    {
            	    pushFollow(FOLLOW_codeBlock_in_codeLine367);
            	    a=codeBlock();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) stream_codeBlock.add(a.getTree());
            	    if (list_a==null) list_a=new ArrayList();
            	    list_a.add(a);


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
            	    if (backtracking>0) {failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);


            // AST REWRITE
            // elements: a
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: a
            if ( backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"token a",list_a);
            root_0 = (CommonTree)adaptor.nil();
            // 57:44: -> ^( CS ( $a)* )
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:47: ^( CS ( $a)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CS, "CS"), root_1);

                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:52: ( $a)*
                while ( stream_a.hasNext() ) {
                    adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_a.next()).getTree());

                }
                stream_a.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            }

            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end codeLine

    public static class codeBlock_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start codeBlock
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:59:1: codeBlock : '[' (a+= codeElement | a+= codeBlock )* ']' -> ^( CB ( $a)* ) ;
    public final codeBlock_return codeBlock() throws RecognitionException {
        codeBlock_return retval = new codeBlock_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal20=null;
        Token char_literal21=null;
        List list_a=null;
        RuleReturnScope a = null;
        CommonTree char_literal20_tree=null;
        CommonTree char_literal21_tree=null;
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleSubtreeStream stream_codeElement=new RewriteRuleSubtreeStream(adaptor,"rule codeElement");
        RewriteRuleSubtreeStream stream_codeBlock=new RewriteRuleSubtreeStream(adaptor,"rule codeBlock");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:2: ( '[' (a+= codeElement | a+= codeBlock )* ']' -> ^( CB ( $a)* ) )
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:4: '[' (a+= codeElement | a+= codeBlock )* ']'
            {
            char_literal20=(Token)input.LT(1);
            match(input,27,FOLLOW_27_in_codeBlock388); if (failed) return retval;
            if ( backtracking==0 ) stream_27.add(char_literal20);

            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:8: (a+= codeElement | a+= codeBlock )*
            loop11:
            do {
                int alt11=3;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=ID && LA11_0<=FLOAT)||LA11_0==26||LA11_0==29||LA11_0==31||(LA11_0>=33 && LA11_0<=34)) ) {
                    alt11=1;
                }
                else if ( (LA11_0==27) ) {
                    alt11=2;
                }


                switch (alt11) {
            	case 1 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:9: a+= codeElement
            	    {
            	    pushFollow(FOLLOW_codeElement_in_codeBlock393);
            	    a=codeElement();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) stream_codeElement.add(a.getTree());
            	    if (list_a==null) list_a=new ArrayList();
            	    list_a.add(a);


            	    }
            	    break;
            	case 2 :
            	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:26: a+= codeBlock
            	    {
            	    pushFollow(FOLLOW_codeBlock_in_codeBlock399);
            	    a=codeBlock();
            	    _fsp--;
            	    if (failed) return retval;
            	    if ( backtracking==0 ) stream_codeBlock.add(a.getTree());
            	    if (list_a==null) list_a=new ArrayList();
            	    list_a.add(a);


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            char_literal21=(Token)input.LT(1);
            match(input,28,FOLLOW_28_in_codeBlock403); if (failed) return retval;
            if ( backtracking==0 ) stream_28.add(char_literal21);


            // AST REWRITE
            // elements: a
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: a
            if ( backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"token a",list_a);
            root_0 = (CommonTree)adaptor.nil();
            // 60:45: -> ^( CB ( $a)* )
            {
                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:48: ^( CB ( $a)* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(CB, "CB"), root_1);

                // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:60:53: ( $a)*
                while ( stream_a.hasNext() ) {
                    adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_a.next()).getTree());

                }
                stream_a.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            }

            }

            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end codeBlock

    public static class codeElement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start codeElement
    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:63:1: codeElement : ( '(' (a+= codeElement | a+= codeBlock )+ ')' -> ^( Paren ( $a)* ) | (i= ID | i= 'breed' | i= 'globals' | i= 'to' | i= 'to-report' ) -> ^( IDN $i) | (str= STRING ) -> ^( STR $str) | ar= ARITHSYM -> ^( IDN $ar) | ni= INT -> ^( INUM $ni) | nf= FLOAT -> ^( FNUM $nf) );
    public final codeElement_return codeElement() throws RecognitionException {
        codeElement_return retval = new codeElement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token i=null;
        Token str=null;
        Token ar=null;
        Token ni=null;
        Token nf=null;
        Token char_literal22=null;
        Token char_literal23=null;
        List list_a=null;
        RuleReturnScope a = null;
        CommonTree i_tree=null;
        CommonTree str_tree=null;
        CommonTree ar_tree=null;
        CommonTree ni_tree=null;
        CommonTree nf_tree=null;
        CommonTree char_literal22_tree=null;
        CommonTree char_literal23_tree=null;
        RewriteRuleTokenStream stream_ARITHSYM=new RewriteRuleTokenStream(adaptor,"token ARITHSYM");
        RewriteRuleTokenStream stream_FLOAT=new RewriteRuleTokenStream(adaptor,"token FLOAT");
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
        RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");
        RewriteRuleTokenStream stream_29=new RewriteRuleTokenStream(adaptor,"token 29");
        RewriteRuleSubtreeStream stream_codeElement=new RewriteRuleSubtreeStream(adaptor,"rule codeElement");
        RewriteRuleSubtreeStream stream_codeBlock=new RewriteRuleSubtreeStream(adaptor,"rule codeBlock");
        try {
            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:2: ( '(' (a+= codeElement | a+= codeBlock )+ ')' -> ^( Paren ( $a)* ) | (i= ID | i= 'breed' | i= 'globals' | i= 'to' | i= 'to-report' ) -> ^( IDN $i) | (str= STRING ) -> ^( STR $str) | ar= ARITHSYM -> ^( IDN $ar) | ni= INT -> ^( INUM $ni) | nf= FLOAT -> ^( FNUM $nf) )
            int alt14=6;
            switch ( input.LA(1) ) {
            case 34:
                {
                alt14=1;
                }
                break;
            case ID:
            case 26:
            case 29:
            case 31:
            case 33:
                {
                alt14=2;
                }
                break;
            case STRING:
                {
                alt14=3;
                }
                break;
            case ARITHSYM:
                {
                alt14=4;
                }
                break;
            case INT:
                {
                alt14=5;
                }
                break;
            case FLOAT:
                {
                alt14=6;
                }
                break;
            default:
                if (backtracking>0) {failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("63:1: codeElement : ( '(' (a+= codeElement | a+= codeBlock )+ ')' -> ^( Paren ( $a)* ) | (i= ID | i= 'breed' | i= 'globals' | i= 'to' | i= 'to-report' ) -> ^( IDN $i) | (str= STRING ) -> ^( STR $str) | ar= ARITHSYM -> ^( IDN $ar) | ni= INT -> ^( INUM $ni) | nf= FLOAT -> ^( FNUM $nf) );", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:4: '(' (a+= codeElement | a+= codeBlock )+ ')'
                    {
                    char_literal22=(Token)input.LT(1);
                    match(input,34,FOLLOW_34_in_codeElement424); if (failed) return retval;
                    if ( backtracking==0 ) stream_34.add(char_literal22);

                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:8: (a+= codeElement | a+= codeBlock )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=3;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0>=ID && LA12_0<=FLOAT)||LA12_0==26||LA12_0==29||LA12_0==31||(LA12_0>=33 && LA12_0<=34)) ) {
                            alt12=1;
                        }
                        else if ( (LA12_0==27) ) {
                            alt12=2;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:9: a+= codeElement
                    	    {
                    	    pushFollow(FOLLOW_codeElement_in_codeElement429);
                    	    a=codeElement();
                    	    _fsp--;
                    	    if (failed) return retval;
                    	    if ( backtracking==0 ) stream_codeElement.add(a.getTree());
                    	    if (list_a==null) list_a=new ArrayList();
                    	    list_a.add(a);


                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:26: a+= codeBlock
                    	    {
                    	    pushFollow(FOLLOW_codeBlock_in_codeElement435);
                    	    a=codeBlock();
                    	    _fsp--;
                    	    if (failed) return retval;
                    	    if ( backtracking==0 ) stream_codeBlock.add(a.getTree());
                    	    if (list_a==null) list_a=new ArrayList();
                    	    list_a.add(a);


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                    	    if (backtracking>0) {failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);

                    char_literal23=(Token)input.LT(1);
                    match(input,35,FOLLOW_35_in_codeElement439); if (failed) return retval;
                    if ( backtracking==0 ) stream_35.add(char_literal23);


                    // AST REWRITE
                    // elements: a
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: a
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_a=new RewriteRuleSubtreeStream(adaptor,"token a",list_a);
                    root_0 = (CommonTree)adaptor.nil();
                    // 64:45: -> ^( Paren ( $a)* )
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:48: ^( Paren ( $a)* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(Paren, "Paren"), root_1);

                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:64:57: ( $a)*
                        while ( stream_a.hasNext() ) {
                            adaptor.addChild(root_1, ((ParserRuleReturnScope)stream_a.next()).getTree());

                        }
                        stream_a.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 2 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:4: (i= ID | i= 'breed' | i= 'globals' | i= 'to' | i= 'to-report' )
                    {
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:4: (i= ID | i= 'breed' | i= 'globals' | i= 'to' | i= 'to-report' )
                    int alt13=5;
                    switch ( input.LA(1) ) {
                    case ID:
                        {
                        alt13=1;
                        }
                        break;
                    case 26:
                        {
                        alt13=2;
                        }
                        break;
                    case 29:
                        {
                        alt13=3;
                        }
                        break;
                    case 31:
                        {
                        alt13=4;
                        }
                        break;
                    case 33:
                        {
                        alt13=5;
                        }
                        break;
                    default:
                        if (backtracking>0) {failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("65:4: (i= ID | i= 'breed' | i= 'globals' | i= 'to' | i= 'to-report' )", 13, 0, input);

                        throw nvae;
                    }

                    switch (alt13) {
                        case 1 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:5: i= ID
                            {
                            i=(Token)input.LT(1);
                            match(input,ID,FOLLOW_ID_in_codeElement459); if (failed) return retval;
                            if ( backtracking==0 ) stream_ID.add(i);


                            }
                            break;
                        case 2 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:12: i= 'breed'
                            {
                            i=(Token)input.LT(1);
                            match(input,26,FOLLOW_26_in_codeElement465); if (failed) return retval;
                            if ( backtracking==0 ) stream_26.add(i);


                            }
                            break;
                        case 3 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:24: i= 'globals'
                            {
                            i=(Token)input.LT(1);
                            match(input,29,FOLLOW_29_in_codeElement471); if (failed) return retval;
                            if ( backtracking==0 ) stream_29.add(i);


                            }
                            break;
                        case 4 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:38: i= 'to'
                            {
                            i=(Token)input.LT(1);
                            match(input,31,FOLLOW_31_in_codeElement477); if (failed) return retval;
                            if ( backtracking==0 ) stream_31.add(i);


                            }
                            break;
                        case 5 :
                            // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:47: i= 'to-report'
                            {
                            i=(Token)input.LT(1);
                            match(input,33,FOLLOW_33_in_codeElement483); if (failed) return retval;
                            if ( backtracking==0 ) stream_33.add(i);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: i
                    // token labels: i
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 65:62: -> ^( IDN $i)
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:65:65: ^( IDN $i)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(IDN, "IDN"), root_1);

                        adaptor.addChild(root_1, stream_i.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 3 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:66:4: (str= STRING )
                    {
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:66:4: (str= STRING )
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:66:5: str= STRING
                    {
                    str=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_codeElement501); if (failed) return retval;
                    if ( backtracking==0 ) stream_STRING.add(str);


                    }


                    // AST REWRITE
                    // elements: str
                    // token labels: str
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_str=new RewriteRuleTokenStream(adaptor,"token str",str);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 66:17: -> ^( STR $str)
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:66:20: ^( STR $str)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(STR, "STR"), root_1);

                        adaptor.addChild(root_1, stream_str.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 4 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:67:5: ar= ARITHSYM
                    {
                    ar=(Token)input.LT(1);
                    match(input,ARITHSYM,FOLLOW_ARITHSYM_in_codeElement519); if (failed) return retval;
                    if ( backtracking==0 ) stream_ARITHSYM.add(ar);


                    // AST REWRITE
                    // elements: ar
                    // token labels: ar
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_ar=new RewriteRuleTokenStream(adaptor,"token ar",ar);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 67:17: -> ^( IDN $ar)
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:67:20: ^( IDN $ar)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(IDN, "IDN"), root_1);

                        adaptor.addChild(root_1, stream_ar.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 5 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:68:4: ni= INT
                    {
                    ni=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_codeElement535); if (failed) return retval;
                    if ( backtracking==0 ) stream_INT.add(ni);


                    // AST REWRITE
                    // elements: ni
                    // token labels: ni
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_ni=new RewriteRuleTokenStream(adaptor,"token ni",ni);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 68:11: -> ^( INUM $ni)
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:68:14: ^( INUM $ni)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(INUM, "INUM"), root_1);

                        adaptor.addChild(root_1, stream_ni.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;
                case 6 :
                    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:69:4: nf= FLOAT
                    {
                    nf=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_codeElement551); if (failed) return retval;
                    if ( backtracking==0 ) stream_FLOAT.add(nf);


                    // AST REWRITE
                    // elements: nf
                    // token labels: nf
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    if ( backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_nf=new RewriteRuleTokenStream(adaptor,"token nf",nf);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 69:13: -> ^( FNUM $nf)
                    {
                        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:69:16: ^( FNUM $nf)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(adaptor.create(FNUM, "FNUM"), root_1);

                        adaptor.addChild(root_1, stream_nf.next());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( backtracking==0 ) {
                retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end codeElement

    // $ANTLR start synpred9
    public final void synpred9_fragment() throws RecognitionException {   
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:15: ( '[' ( codeElement )+ ']' )
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:15: '[' ( codeElement )+ ']'
        {
        match(input,27,FOLLOW_27_in_synpred9221); if (failed) return ;
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:19: ( codeElement )+
        int cnt16=0;
        loop16:
        do {
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( ((LA16_0>=ID && LA16_0<=FLOAT)||LA16_0==26||LA16_0==29||LA16_0==31||(LA16_0>=33 && LA16_0<=34)) ) {
                alt16=1;
            }


            switch (alt16) {
        	case 1 :
        	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:48:20: codeElement
        	    {
        	    pushFollow(FOLLOW_codeElement_in_synpred9226);
        	    codeElement();
        	    _fsp--;
        	    if (failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt16 >= 1 ) break loop16;
        	    if (backtracking>0) {failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(16, input);
                    throw eee;
            }
            cnt16++;
        } while (true);

        match(input,28,FOLLOW_28_in_synpred9230); if (failed) return ;

        }
    }
    // $ANTLR end synpred9

    // $ANTLR start synpred12
    public final void synpred12_fragment() throws RecognitionException {   
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:22: ( '[' ( codeElement )+ ']' )
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:22: '[' ( codeElement )+ ']'
        {
        match(input,27,FOLLOW_27_in_synpred12294); if (failed) return ;
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:26: ( codeElement )+
        int cnt17=0;
        loop17:
        do {
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0>=ID && LA17_0<=FLOAT)||LA17_0==26||LA17_0==29||LA17_0==31||(LA17_0>=33 && LA17_0<=34)) ) {
                alt17=1;
            }


            switch (alt17) {
        	case 1 :
        	    // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:53:27: codeElement
        	    {
        	    pushFollow(FOLLOW_codeElement_in_synpred12299);
        	    codeElement();
        	    _fsp--;
        	    if (failed) return ;

        	    }
        	    break;

        	default :
        	    if ( cnt17 >= 1 ) break loop17;
        	    if (backtracking>0) {failed=true; return ;}
                    EarlyExitException eee =
                        new EarlyExitException(17, input);
                    throw eee;
            }
            cnt17++;
        } while (true);

        match(input,28,FOLLOW_28_in_synpred12303); if (failed) return ;

        }
    }
    // $ANTLR end synpred12

    // $ANTLR start synpred14
    public final void synpred14_fragment() throws RecognitionException {   
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:12: ( codeElement )
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:12: codeElement
        {
        pushFollow(FOLLOW_codeElement_in_synpred14361);
        codeElement();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred14

    // $ANTLR start synpred15
    public final void synpred15_fragment() throws RecognitionException {   
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:29: ( codeBlock )
        // C:\\projects\\netlogo\\grammars\\NetLogoRGW.g:57:29: codeBlock
        {
        pushFollow(FOLLOW_codeBlock_in_synpred15367);
        codeBlock();
        _fsp--;
        if (failed) return ;

        }
    }
    // $ANTLR end synpred15

    public final boolean synpred9() {
        backtracking++;
        int start = input.mark();
        try {
            synpred9_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred14() {
        backtracking++;
        int start = input.mark();
        try {
            synpred14_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred15() {
        backtracking++;
        int start = input.mark();
        try {
            synpred15_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }
    public final boolean synpred12() {
        backtracking++;
        int start = input.mark();
        try {
            synpred12_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !failed;
        input.rewind(start);
        backtracking--;
        failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_stat_in_prog94 = new BitSet(new long[]{0x00000002E4040002L});
    public static final BitSet FOLLOW_26_in_stat105 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_stat107 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_ID_in_stat111 = new BitSet(new long[]{0x0000000010040000L});
    public static final BitSet FOLLOW_ID_in_stat115 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_stat118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_stat137 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_codeBlock_in_stat139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_stat153 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_codeBlock_in_stat155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_stat170 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_codeBlock_in_stat172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_toEndBlock_in_stat190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_toReportBlock_in_stat200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_toEndBlock214 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_ID_in_toEndBlock218 = new BitSet(new long[]{0x00000007AC7C0000L});
    public static final BitSet FOLLOW_27_in_toEndBlock221 = new BitSet(new long[]{0x00000006A47C0000L});
    public static final BitSet FOLLOW_codeElement_in_toEndBlock226 = new BitSet(new long[]{0x00000006B47C0000L});
    public static final BitSet FOLLOW_28_in_toEndBlock230 = new BitSet(new long[]{0x00000007AC7C0000L});
    public static final BitSet FOLLOW_codeLine_in_toEndBlock238 = new BitSet(new long[]{0x00000007AC7C0000L});
    public static final BitSet FOLLOW_32_in_toEndBlock242 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_toReportBlock287 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_ID_in_toReportBlock291 = new BitSet(new long[]{0x00000007AC7C0000L});
    public static final BitSet FOLLOW_27_in_toReportBlock294 = new BitSet(new long[]{0x00000006A47C0000L});
    public static final BitSet FOLLOW_codeElement_in_toReportBlock299 = new BitSet(new long[]{0x00000006B47C0000L});
    public static final BitSet FOLLOW_28_in_toReportBlock303 = new BitSet(new long[]{0x00000007AC7C0000L});
    public static final BitSet FOLLOW_codeLine_in_toReportBlock310 = new BitSet(new long[]{0x00000007AC7C0000L});
    public static final BitSet FOLLOW_32_in_toReportBlock315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_codeElement_in_codeLine361 = new BitSet(new long[]{0x00000006AC7C0002L});
    public static final BitSet FOLLOW_codeBlock_in_codeLine367 = new BitSet(new long[]{0x00000006AC7C0002L});
    public static final BitSet FOLLOW_27_in_codeBlock388 = new BitSet(new long[]{0x00000006BC7C0000L});
    public static final BitSet FOLLOW_codeElement_in_codeBlock393 = new BitSet(new long[]{0x00000006BC7C0000L});
    public static final BitSet FOLLOW_codeBlock_in_codeBlock399 = new BitSet(new long[]{0x00000006BC7C0000L});
    public static final BitSet FOLLOW_28_in_codeBlock403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_codeElement424 = new BitSet(new long[]{0x00000006AC7C0000L});
    public static final BitSet FOLLOW_codeElement_in_codeElement429 = new BitSet(new long[]{0x0000000EAC7C0000L});
    public static final BitSet FOLLOW_codeBlock_in_codeElement435 = new BitSet(new long[]{0x0000000EAC7C0000L});
    public static final BitSet FOLLOW_35_in_codeElement439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_codeElement459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_codeElement465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_codeElement471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_codeElement477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_codeElement483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_codeElement501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARITHSYM_in_codeElement519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_codeElement535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_codeElement551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_synpred9221 = new BitSet(new long[]{0x00000006A47C0000L});
    public static final BitSet FOLLOW_codeElement_in_synpred9226 = new BitSet(new long[]{0x00000006B47C0000L});
    public static final BitSet FOLLOW_28_in_synpred9230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_synpred12294 = new BitSet(new long[]{0x00000006A47C0000L});
    public static final BitSet FOLLOW_codeElement_in_synpred12299 = new BitSet(new long[]{0x00000006B47C0000L});
    public static final BitSet FOLLOW_28_in_synpred12303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_codeElement_in_synpred14361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_codeBlock_in_synpred15367 = new BitSet(new long[]{0x0000000000000002L});

}