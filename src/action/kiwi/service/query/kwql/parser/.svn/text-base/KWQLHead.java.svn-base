package kiwi.service.query.kwql.parser;
// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 KWQLHead.g 2010-04-19 10:33:12

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class KWQLHead extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CI", "LINK", "FRAG", "TAG", "OPTIONAL", "CONJUNCTION", "DISJUNCTION", "NEGATION", "UQ", "BOOLEAN", "SCHEME", "HOST", "PATH", "QUERY", "FRAGMENT", "URI", "SYMBOL", "NAME", "COMMASEP", "COMMA", "QUANT", "COUNT", "RESQUAL", "QUAL", "DIGIT", "INT", "ISOTIME", "DA", "NAME2", "EscNAME", "KW", "VARIABLE", "WS", "LEFT_PARENTHESIS", "RIGHT_PARENTHESIS", "ARROW", "SPARQL", "AT", "LOWER", "UPPER", "HexPrefix", "HexDigit", "LETTER", "VAR", "LABEL", "QUALIFIER", "RESOURCE", "TYPE", "AND", "RULE", "FUNC", "SEPARATOR", "NUM", "HEAD", "BODY", "INTEGER", "DATE", "STRING", "UR"
    };
    public static final int FRAG=6;
    public static final int QUANT=24;
    public static final int LETTER=46;
    public static final int FRAGMENT=18;
    public static final int HEAD=57;
    public static final int HOST=15;
    public static final int COUNT=25;
    public static final int SEPARATOR=55;
    public static final int AND=52;
    public static final int EOF=-1;
    public static final int RESOURCE=50;
    public static final int HexDigit=45;
    public static final int TYPE=51;
    public static final int AT=41;
    public static final int UQ=12;
    public static final int UR=62;
    public static final int NAME=21;
    public static final int BOOLEAN=13;
    public static final int RIGHT_PARENTHESIS=38;
    public static final int QUAL=27;
    public static final int COMMA=23;
    public static final int PATH=16;
    public static final int LEFT_PARENTHESIS=37;
    public static final int COMMASEP=22;
    public static final int VAR=47;
    public static final int BODY=58;
    public static final int DIGIT=28;
    public static final int HexPrefix=44;
    public static final int INTEGER=59;
    public static final int ISOTIME=30;
    public static final int RULE=53;
    public static final int SPARQL=40;
    public static final int SYMBOL=20;
    public static final int QUALIFIER=49;
    public static final int CONJUNCTION=9;
    public static final int EscNAME=33;
    public static final int INT=29;
    public static final int LINK=5;
    public static final int NUM=56;
    public static final int OPTIONAL=8;
    public static final int TAG=7;
    public static final int URI=19;
    public static final int SCHEME=14;
    public static final int DISJUNCTION=10;
    public static final int WS=36;
    public static final int NEGATION=11;
    public static final int VARIABLE=35;
    public static final int LABEL=48;
    public static final int KW=34;
    public static final int DA=31;
    public static final int FUNC=54;
    public static final int CI=4;
    public static final int QUERY=17;
    public static final int NAME2=32;
    public static final int ARROW=39;
    public static final int LOWER=42;
    public static final int DATE=60;
    public static final int RESQUAL=26;
    public static final int UPPER=43;
    public static final int STRING=61;

    // delegates
    // delegators


        public KWQLHead(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public KWQLHead(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return KWQLHead.tokenNames; }
    public String getGrammarFileName() { return "KWQLHead.g"; }


    public static class head_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "head"
    // KWQLHead.g:32:1: head : ( ( c_resource_term -> c_resource_term ) | q_c_resource_term );
    public final KWQLHead.head_return head() throws RecognitionException {
        KWQLHead.head_return retval = new KWQLHead.head_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLHead.c_resource_term_return c_resource_term1 = null;

        KWQLHead.q_c_resource_term_return q_c_resource_term2 = null;


        RewriteRuleSubtreeStream stream_c_resource_term=new RewriteRuleSubtreeStream(adaptor,"rule c_resource_term");
        try {
            // KWQLHead.g:32:13: ( ( c_resource_term -> c_resource_term ) | q_c_resource_term )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( ((LA1_0>=CI && LA1_0<=TAG)) ) {
                alt1=1;
            }
            else if ( (LA1_0==UQ) ) {
                alt1=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // KWQLHead.g:32:15: ( c_resource_term -> c_resource_term )
                    {
                    // KWQLHead.g:32:15: ( c_resource_term -> c_resource_term )
                    // KWQLHead.g:32:16: c_resource_term
                    {
                    pushFollow(FOLLOW_c_resource_term_in_head137);
                    c_resource_term1=c_resource_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_c_resource_term.add(c_resource_term1.getTree());


                    // AST REWRITE
                    // elements: c_resource_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 32:31: -> c_resource_term
                    {
                        adaptor.addChild(root_0, stream_c_resource_term.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLHead.g:32:50: q_c_resource_term
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_q_c_resource_term_in_head142);
                    q_c_resource_term2=q_c_resource_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, q_c_resource_term2.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "head"

    public static class cvalue_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "cvalue"
    // KWQLHead.g:34:1: cvalue : ( ( VARIABLE -> ^( VAR VARIABLE ) ) | ( atomar_value -> ^( atomar_value ) ) | q_cvalue );
    public final KWQLHead.cvalue_return cvalue() throws RecognitionException {
        KWQLHead.cvalue_return retval = new KWQLHead.cvalue_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token VARIABLE3=null;
        KWQLHead.atomar_value_return atomar_value4 = null;

        KWQLHead.q_cvalue_return q_cvalue5 = null;


        CommonTree VARIABLE3_tree=null;
        RewriteRuleTokenStream stream_VARIABLE=new RewriteRuleTokenStream(adaptor,"token VARIABLE");
        RewriteRuleSubtreeStream stream_atomar_value=new RewriteRuleSubtreeStream(adaptor,"rule atomar_value");
        try {
            // KWQLHead.g:34:14: ( ( VARIABLE -> ^( VAR VARIABLE ) ) | ( atomar_value -> ^( atomar_value ) ) | q_cvalue )
            int alt2=3;
            switch ( input.LA(1) ) {
            case VARIABLE:
                {
                alt2=1;
                }
                break;
            case URI:
            case INT:
            case DA:
            case KW:
                {
                alt2=2;
                }
                break;
            case UQ:
                {
                alt2=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // KWQLHead.g:34:16: ( VARIABLE -> ^( VAR VARIABLE ) )
                    {
                    // KWQLHead.g:34:16: ( VARIABLE -> ^( VAR VARIABLE ) )
                    // KWQLHead.g:34:17: VARIABLE
                    {
                    VARIABLE3=(Token)match(input,VARIABLE,FOLLOW_VARIABLE_in_cvalue157); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VARIABLE.add(VARIABLE3);



                    // AST REWRITE
                    // elements: VARIABLE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 34:25: -> ^( VAR VARIABLE )
                    {
                        // KWQLHead.g:34:27: ^( VAR VARIABLE )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VAR, "VAR"), root_1);

                        adaptor.addChild(root_1, stream_VARIABLE.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLHead.g:34:44: ( atomar_value -> ^( atomar_value ) )
                    {
                    // KWQLHead.g:34:44: ( atomar_value -> ^( atomar_value ) )
                    // KWQLHead.g:34:45: atomar_value
                    {
                    pushFollow(FOLLOW_atomar_value_in_cvalue167);
                    atomar_value4=atomar_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_atomar_value.add(atomar_value4.getTree());


                    // AST REWRITE
                    // elements: atomar_value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 34:57: -> ^( atomar_value )
                    {
                        // KWQLHead.g:34:59: ^( atomar_value )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_atomar_value.nextNode(), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLHead.g:34:76: q_cvalue
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_q_cvalue_in_cvalue174);
                    q_cvalue5=q_cvalue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, q_cvalue5.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "cvalue"

    public static class resource_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "resource"
    // KWQLHead.g:36:1: resource : ( CI | LINK | FRAG | TAG ) ;
    public final KWQLHead.resource_return resource() throws RecognitionException {
        KWQLHead.resource_return retval = new KWQLHead.resource_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set6=null;

        CommonTree set6_tree=null;

        try {
            // KWQLHead.g:36:12: ( ( CI | LINK | FRAG | TAG ) )
            // KWQLHead.g:37:4: ( CI | LINK | FRAG | TAG )
            {
            root_0 = (CommonTree)adaptor.nil();

            set6=(Token)input.LT(1);
            if ( (input.LA(1)>=CI && input.LA(1)<=TAG) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set6));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "resource"

    public static class c_resource_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "c_resource_term"
    // KWQLHead.g:39:1: c_resource_term : resource LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS -> ^( RESOURCE ^( TYPE resource ) c_resource_content ) ;
    public final KWQLHead.c_resource_term_return c_resource_term() throws RecognitionException {
        KWQLHead.c_resource_term_return retval = new KWQLHead.c_resource_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS8=null;
        Token RIGHT_PARENTHESIS10=null;
        KWQLHead.resource_return resource7 = null;

        KWQLHead.c_resource_content_return c_resource_content9 = null;


        CommonTree LEFT_PARENTHESIS8_tree=null;
        CommonTree RIGHT_PARENTHESIS10_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_resource=new RewriteRuleSubtreeStream(adaptor,"rule resource");
        RewriteRuleSubtreeStream stream_c_resource_content=new RewriteRuleSubtreeStream(adaptor,"rule c_resource_content");
        try {
            // KWQLHead.g:39:17: ( resource LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS -> ^( RESOURCE ^( TYPE resource ) c_resource_content ) )
            // KWQLHead.g:39:19: resource LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS
            {
            pushFollow(FOLLOW_resource_in_c_resource_term207);
            resource7=resource();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_resource.add(resource7.getTree());
            LEFT_PARENTHESIS8=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_c_resource_term209); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS8);

            pushFollow(FOLLOW_c_resource_content_in_c_resource_term211);
            c_resource_content9=c_resource_content();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_c_resource_content.add(c_resource_content9.getTree());
            RIGHT_PARENTHESIS10=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_c_resource_term214); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS10);



            // AST REWRITE
            // elements: resource, c_resource_content
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 39:83: -> ^( RESOURCE ^( TYPE resource ) c_resource_content )
            {
                // KWQLHead.g:39:85: ^( RESOURCE ^( TYPE resource ) c_resource_content )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                // KWQLHead.g:39:96: ^( TYPE resource )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                adaptor.addChild(root_2, stream_resource.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_c_resource_content.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "c_resource_term"

    public static class c_resource_content_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "c_resource_content"
    // KWQLHead.g:42:1: c_resource_content : ( ( c_resource_content_helper -> c_resource_content_helper ) ( conjunction c= c_resource_content_helper -> ^( conjunction $c_resource_content $c) )* | q_c_resource_content );
    public final KWQLHead.c_resource_content_return c_resource_content() throws RecognitionException {
        KWQLHead.c_resource_content_return retval = new KWQLHead.c_resource_content_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLHead.c_resource_content_helper_return c = null;

        KWQLHead.c_resource_content_helper_return c_resource_content_helper11 = null;

        KWQLHead.conjunction_return conjunction12 = null;

        KWQLHead.q_c_resource_content_return q_c_resource_content13 = null;


        RewriteRuleSubtreeStream stream_c_resource_content_helper=new RewriteRuleSubtreeStream(adaptor,"rule c_resource_content_helper");
        RewriteRuleSubtreeStream stream_conjunction=new RewriteRuleSubtreeStream(adaptor,"rule conjunction");
        try {
            // KWQLHead.g:42:20: ( ( c_resource_content_helper -> c_resource_content_helper ) ( conjunction c= c_resource_content_helper -> ^( conjunction $c_resource_content $c) )* | q_c_resource_content )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0>=CI && LA4_0<=TAG)||LA4_0==QUAL) ) {
                alt4=1;
            }
            else if ( (LA4_0==UQ) ) {
                int LA4_2 = input.LA(2);

                if ( (synpred8_KWQLHead()) ) {
                    alt4=1;
                }
                else if ( (true) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // KWQLHead.g:42:22: ( c_resource_content_helper -> c_resource_content_helper ) ( conjunction c= c_resource_content_helper -> ^( conjunction $c_resource_content $c) )*
                    {
                    // KWQLHead.g:42:22: ( c_resource_content_helper -> c_resource_content_helper )
                    // KWQLHead.g:42:23: c_resource_content_helper
                    {
                    pushFollow(FOLLOW_c_resource_content_helper_in_c_resource_content237);
                    c_resource_content_helper11=c_resource_content_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_c_resource_content_helper.add(c_resource_content_helper11.getTree());


                    // AST REWRITE
                    // elements: c_resource_content_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 42:48: -> c_resource_content_helper
                    {
                        adaptor.addChild(root_0, stream_c_resource_content_helper.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    // KWQLHead.g:42:76: ( conjunction c= c_resource_content_helper -> ^( conjunction $c_resource_content $c) )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0>=CI && LA3_0<=TAG)||LA3_0==CONJUNCTION||LA3_0==UQ||LA3_0==QUAL) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // KWQLHead.g:42:77: conjunction c= c_resource_content_helper
                    	    {
                    	    pushFollow(FOLLOW_conjunction_in_c_resource_content242);
                    	    conjunction12=conjunction();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_conjunction.add(conjunction12.getTree());
                    	    pushFollow(FOLLOW_c_resource_content_helper_in_c_resource_content246);
                    	    c=c_resource_content_helper();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_c_resource_content_helper.add(c.getTree());


                    	    // AST REWRITE
                    	    // elements: c_resource_content, c, conjunction
                    	    // token labels: 
                    	    // rule labels: retval, c
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_c=new RewriteRuleSubtreeStream(adaptor,"rule c",c!=null?c.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 42:116: -> ^( conjunction $c_resource_content $c)
                    	    {
                    	        // KWQLHead.g:42:118: ^( conjunction $c_resource_content $c)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(stream_conjunction.nextNode(), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        adaptor.addChild(root_1, stream_c.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // KWQLHead.g:42:158: q_c_resource_content
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_q_c_resource_content_in_c_resource_content260);
                    q_c_resource_content13=q_c_resource_content();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, q_c_resource_content13.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "c_resource_content"

    public static class conjunction_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conjunction"
    // KWQLHead.g:44:1: conjunction : ( ( CONJUNCTION -> CONJUNCTION ) | -> AND );
    public final KWQLHead.conjunction_return conjunction() throws RecognitionException {
        KWQLHead.conjunction_return retval = new KWQLHead.conjunction_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CONJUNCTION14=null;

        CommonTree CONJUNCTION14_tree=null;
        RewriteRuleTokenStream stream_CONJUNCTION=new RewriteRuleTokenStream(adaptor,"token CONJUNCTION");

        try {
            // KWQLHead.g:44:13: ( ( CONJUNCTION -> CONJUNCTION ) | -> AND )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==CONJUNCTION) ) {
                alt5=1;
            }
            else if ( ((LA5_0>=CI && LA5_0<=TAG)||LA5_0==UQ||LA5_0==QUAL) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // KWQLHead.g:45:4: ( CONJUNCTION -> CONJUNCTION )
                    {
                    // KWQLHead.g:45:4: ( CONJUNCTION -> CONJUNCTION )
                    // KWQLHead.g:45:5: CONJUNCTION
                    {
                    CONJUNCTION14=(Token)match(input,CONJUNCTION,FOLLOW_CONJUNCTION_in_conjunction272); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_CONJUNCTION.add(CONJUNCTION14);



                    // AST REWRITE
                    // elements: CONJUNCTION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 45:16: -> CONJUNCTION
                    {
                        adaptor.addChild(root_0, stream_CONJUNCTION.nextNode());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLHead.g:45:31: 
                    {

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 45:31: -> AND
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(AND, "AND"));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "conjunction"

    public static class c_resource_content_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "c_resource_content_helper"
    // KWQLHead.g:47:1: c_resource_content_helper : ( c_qualifier -> c_qualifier | c_resource_term -> c_resource_term ) ;
    public final KWQLHead.c_resource_content_helper_return c_resource_content_helper() throws RecognitionException {
        KWQLHead.c_resource_content_helper_return retval = new KWQLHead.c_resource_content_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLHead.c_qualifier_return c_qualifier15 = null;

        KWQLHead.c_resource_term_return c_resource_term16 = null;


        RewriteRuleSubtreeStream stream_c_resource_term=new RewriteRuleSubtreeStream(adaptor,"rule c_resource_term");
        RewriteRuleSubtreeStream stream_c_qualifier=new RewriteRuleSubtreeStream(adaptor,"rule c_qualifier");
        try {
            // KWQLHead.g:47:27: ( ( c_qualifier -> c_qualifier | c_resource_term -> c_resource_term ) )
            // KWQLHead.g:47:29: ( c_qualifier -> c_qualifier | c_resource_term -> c_resource_term )
            {
            // KWQLHead.g:47:29: ( c_qualifier -> c_qualifier | c_resource_term -> c_resource_term )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==UQ||LA6_0==QUAL) ) {
                alt6=1;
            }
            else if ( ((LA6_0>=CI && LA6_0<=TAG)) ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // KWQLHead.g:47:30: c_qualifier
                    {
                    pushFollow(FOLLOW_c_qualifier_in_c_resource_content_helper287);
                    c_qualifier15=c_qualifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_c_qualifier.add(c_qualifier15.getTree());


                    // AST REWRITE
                    // elements: c_qualifier
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 47:41: -> c_qualifier
                    {
                        adaptor.addChild(root_0, stream_c_qualifier.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // KWQLHead.g:47:55: c_resource_term
                    {
                    pushFollow(FOLLOW_c_resource_term_in_c_resource_content_helper291);
                    c_resource_term16=c_resource_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_c_resource_term.add(c_resource_term16.getTree());


                    // AST REWRITE
                    // elements: c_resource_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 47:70: -> c_resource_term
                    {
                        adaptor.addChild(root_0, stream_c_resource_term.nextTree());

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "c_resource_content_helper"

    public static class c_qualifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "c_qualifier"
    // KWQLHead.g:49:1: c_qualifier : ( ( QUAL cvalue -> ^( QUALIFIER ^( LABEL QUAL ) cvalue ) ) | q_c_qualifier );
    public final KWQLHead.c_qualifier_return c_qualifier() throws RecognitionException {
        KWQLHead.c_qualifier_return retval = new KWQLHead.c_qualifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token QUAL17=null;
        KWQLHead.cvalue_return cvalue18 = null;

        KWQLHead.q_c_qualifier_return q_c_qualifier19 = null;


        CommonTree QUAL17_tree=null;
        RewriteRuleTokenStream stream_QUAL=new RewriteRuleTokenStream(adaptor,"token QUAL");
        RewriteRuleSubtreeStream stream_cvalue=new RewriteRuleSubtreeStream(adaptor,"rule cvalue");
        try {
            // KWQLHead.g:49:15: ( ( QUAL cvalue -> ^( QUALIFIER ^( LABEL QUAL ) cvalue ) ) | q_c_qualifier )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==QUAL) ) {
                alt7=1;
            }
            else if ( (LA7_0==UQ) ) {
                alt7=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // KWQLHead.g:49:17: ( QUAL cvalue -> ^( QUALIFIER ^( LABEL QUAL ) cvalue ) )
                    {
                    // KWQLHead.g:49:17: ( QUAL cvalue -> ^( QUALIFIER ^( LABEL QUAL ) cvalue ) )
                    // KWQLHead.g:49:18: QUAL cvalue
                    {
                    QUAL17=(Token)match(input,QUAL,FOLLOW_QUAL_in_c_qualifier305); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUAL.add(QUAL17);

                    pushFollow(FOLLOW_cvalue_in_c_qualifier307);
                    cvalue18=cvalue();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_cvalue.add(cvalue18.getTree());


                    // AST REWRITE
                    // elements: cvalue, QUAL
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 49:30: -> ^( QUALIFIER ^( LABEL QUAL ) cvalue )
                    {
                        // KWQLHead.g:49:32: ^( QUALIFIER ^( LABEL QUAL ) cvalue )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        // KWQLHead.g:49:44: ^( LABEL QUAL )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_2);

                        adaptor.addChild(root_2, stream_QUAL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_cvalue.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLHead.g:49:67: q_c_qualifier
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_q_c_qualifier_in_c_qualifier323);
                    q_c_qualifier19=q_c_qualifier();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, q_c_qualifier19.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "c_qualifier"

    public static class atomar_value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atomar_value"
    // KWQLHead.g:51:1: atomar_value : ( ( INT -> ^( INTEGER INT ) ) | ( DA -> ^( DATE DA ) ) | ( KW -> ^( STRING KW ) ) | ( URI -> ^( UR URI ) ) );
    public final KWQLHead.atomar_value_return atomar_value() throws RecognitionException {
        KWQLHead.atomar_value_return retval = new KWQLHead.atomar_value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT20=null;
        Token DA21=null;
        Token KW22=null;
        Token URI23=null;

        CommonTree INT20_tree=null;
        CommonTree DA21_tree=null;
        CommonTree KW22_tree=null;
        CommonTree URI23_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_KW=new RewriteRuleTokenStream(adaptor,"token KW");
        RewriteRuleTokenStream stream_DA=new RewriteRuleTokenStream(adaptor,"token DA");
        RewriteRuleTokenStream stream_URI=new RewriteRuleTokenStream(adaptor,"token URI");

        try {
            // KWQLHead.g:51:15: ( ( INT -> ^( INTEGER INT ) ) | ( DA -> ^( DATE DA ) ) | ( KW -> ^( STRING KW ) ) | ( URI -> ^( UR URI ) ) )
            int alt8=4;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt8=1;
                }
                break;
            case DA:
                {
                alt8=2;
                }
                break;
            case KW:
                {
                alt8=3;
                }
                break;
            case URI:
                {
                alt8=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // KWQLHead.g:52:4: ( INT -> ^( INTEGER INT ) )
                    {
                    // KWQLHead.g:52:4: ( INT -> ^( INTEGER INT ) )
                    // KWQLHead.g:52:5: INT
                    {
                    INT20=(Token)match(input,INT,FOLLOW_INT_in_atomar_value340); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(INT20);



                    // AST REWRITE
                    // elements: INT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 52:8: -> ^( INTEGER INT )
                    {
                        // KWQLHead.g:52:10: ^( INTEGER INT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(INTEGER, "INTEGER"), root_1);

                        adaptor.addChild(root_1, stream_INT.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLHead.g:52:26: ( DA -> ^( DATE DA ) )
                    {
                    // KWQLHead.g:52:26: ( DA -> ^( DATE DA ) )
                    // KWQLHead.g:52:27: DA
                    {
                    DA21=(Token)match(input,DA,FOLLOW_DA_in_atomar_value350); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DA.add(DA21);



                    // AST REWRITE
                    // elements: DA
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 52:29: -> ^( DATE DA )
                    {
                        // KWQLHead.g:52:31: ^( DATE DA )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DATE, "DATE"), root_1);

                        adaptor.addChild(root_1, stream_DA.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLHead.g:52:43: ( KW -> ^( STRING KW ) )
                    {
                    // KWQLHead.g:52:43: ( KW -> ^( STRING KW ) )
                    // KWQLHead.g:52:44: KW
                    {
                    KW22=(Token)match(input,KW,FOLLOW_KW_in_atomar_value360); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW.add(KW22);



                    // AST REWRITE
                    // elements: KW
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 52:46: -> ^( STRING KW )
                    {
                        // KWQLHead.g:52:48: ^( STRING KW )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRING, "STRING"), root_1);

                        adaptor.addChild(root_1, stream_KW.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLHead.g:52:62: ( URI -> ^( UR URI ) )
                    {
                    // KWQLHead.g:52:62: ( URI -> ^( UR URI ) )
                    // KWQLHead.g:52:63: URI
                    {
                    URI23=(Token)match(input,URI,FOLLOW_URI_in_atomar_value370); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_URI.add(URI23);



                    // AST REWRITE
                    // elements: URI
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 52:66: -> ^( UR URI )
                    {
                        // KWQLHead.g:52:68: ^( UR URI )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UR, "UR"), root_1);

                        adaptor.addChild(root_1, stream_URI.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "atomar_value"

    public static class q_c_resource_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "q_c_resource_term"
    // KWQLHead.g:54:1: q_c_resource_term : ( UQ LEFT_PARENTHESIS c_resource_term RIGHT_PARENTHESIS ) -> ^( UQ c_resource_term ) ;
    public final KWQLHead.q_c_resource_term_return q_c_resource_term() throws RecognitionException {
        KWQLHead.q_c_resource_term_return retval = new KWQLHead.q_c_resource_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token UQ24=null;
        Token LEFT_PARENTHESIS25=null;
        Token RIGHT_PARENTHESIS27=null;
        KWQLHead.c_resource_term_return c_resource_term26 = null;


        CommonTree UQ24_tree=null;
        CommonTree LEFT_PARENTHESIS25_tree=null;
        CommonTree RIGHT_PARENTHESIS27_tree=null;
        RewriteRuleTokenStream stream_UQ=new RewriteRuleTokenStream(adaptor,"token UQ");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_c_resource_term=new RewriteRuleSubtreeStream(adaptor,"rule c_resource_term");
        try {
            // KWQLHead.g:55:2: ( ( UQ LEFT_PARENTHESIS c_resource_term RIGHT_PARENTHESIS ) -> ^( UQ c_resource_term ) )
            // KWQLHead.g:55:4: ( UQ LEFT_PARENTHESIS c_resource_term RIGHT_PARENTHESIS )
            {
            // KWQLHead.g:55:4: ( UQ LEFT_PARENTHESIS c_resource_term RIGHT_PARENTHESIS )
            // KWQLHead.g:55:5: UQ LEFT_PARENTHESIS c_resource_term RIGHT_PARENTHESIS
            {
            UQ24=(Token)match(input,UQ,FOLLOW_UQ_in_q_c_resource_term390); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_UQ.add(UQ24);

            LEFT_PARENTHESIS25=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_q_c_resource_term392); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS25);

            pushFollow(FOLLOW_c_resource_term_in_q_c_resource_term394);
            c_resource_term26=c_resource_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_c_resource_term.add(c_resource_term26.getTree());
            RIGHT_PARENTHESIS27=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_q_c_resource_term396); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS27);


            }



            // AST REWRITE
            // elements: c_resource_term, UQ
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 55:59: -> ^( UQ c_resource_term )
            {
                // KWQLHead.g:55:61: ^( UQ c_resource_term )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_UQ.nextNode(), root_1);

                adaptor.addChild(root_1, stream_c_resource_term.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "q_c_resource_term"

    public static class q_c_qualifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "q_c_qualifier"
    // KWQLHead.g:57:1: q_c_qualifier : ( UQ LEFT_PARENTHESIS c_qualifier RIGHT_PARENTHESIS ) -> ^( UQ c_qualifier ) ;
    public final KWQLHead.q_c_qualifier_return q_c_qualifier() throws RecognitionException {
        KWQLHead.q_c_qualifier_return retval = new KWQLHead.q_c_qualifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token UQ28=null;
        Token LEFT_PARENTHESIS29=null;
        Token RIGHT_PARENTHESIS31=null;
        KWQLHead.c_qualifier_return c_qualifier30 = null;


        CommonTree UQ28_tree=null;
        CommonTree LEFT_PARENTHESIS29_tree=null;
        CommonTree RIGHT_PARENTHESIS31_tree=null;
        RewriteRuleTokenStream stream_UQ=new RewriteRuleTokenStream(adaptor,"token UQ");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_c_qualifier=new RewriteRuleSubtreeStream(adaptor,"rule c_qualifier");
        try {
            // KWQLHead.g:58:2: ( ( UQ LEFT_PARENTHESIS c_qualifier RIGHT_PARENTHESIS ) -> ^( UQ c_qualifier ) )
            // KWQLHead.g:58:4: ( UQ LEFT_PARENTHESIS c_qualifier RIGHT_PARENTHESIS )
            {
            // KWQLHead.g:58:4: ( UQ LEFT_PARENTHESIS c_qualifier RIGHT_PARENTHESIS )
            // KWQLHead.g:58:5: UQ LEFT_PARENTHESIS c_qualifier RIGHT_PARENTHESIS
            {
            UQ28=(Token)match(input,UQ,FOLLOW_UQ_in_q_c_qualifier414); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_UQ.add(UQ28);

            LEFT_PARENTHESIS29=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_q_c_qualifier416); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS29);

            pushFollow(FOLLOW_c_qualifier_in_q_c_qualifier418);
            c_qualifier30=c_qualifier();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_c_qualifier.add(c_qualifier30.getTree());
            RIGHT_PARENTHESIS31=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_q_c_qualifier420); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS31);


            }



            // AST REWRITE
            // elements: c_qualifier, UQ
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 58:55: -> ^( UQ c_qualifier )
            {
                // KWQLHead.g:58:57: ^( UQ c_qualifier )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_UQ.nextNode(), root_1);

                adaptor.addChild(root_1, stream_c_qualifier.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "q_c_qualifier"

    public static class q_c_resource_content_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "q_c_resource_content"
    // KWQLHead.g:60:1: q_c_resource_content : ( UQ LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS ) -> ^( UQ c_resource_content ) ;
    public final KWQLHead.q_c_resource_content_return q_c_resource_content() throws RecognitionException {
        KWQLHead.q_c_resource_content_return retval = new KWQLHead.q_c_resource_content_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token UQ32=null;
        Token LEFT_PARENTHESIS33=null;
        Token RIGHT_PARENTHESIS35=null;
        KWQLHead.c_resource_content_return c_resource_content34 = null;


        CommonTree UQ32_tree=null;
        CommonTree LEFT_PARENTHESIS33_tree=null;
        CommonTree RIGHT_PARENTHESIS35_tree=null;
        RewriteRuleTokenStream stream_UQ=new RewriteRuleTokenStream(adaptor,"token UQ");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_c_resource_content=new RewriteRuleSubtreeStream(adaptor,"rule c_resource_content");
        try {
            // KWQLHead.g:61:2: ( ( UQ LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS ) -> ^( UQ c_resource_content ) )
            // KWQLHead.g:61:4: ( UQ LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS )
            {
            // KWQLHead.g:61:4: ( UQ LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS )
            // KWQLHead.g:61:5: UQ LEFT_PARENTHESIS c_resource_content RIGHT_PARENTHESIS
            {
            UQ32=(Token)match(input,UQ,FOLLOW_UQ_in_q_c_resource_content438); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_UQ.add(UQ32);

            LEFT_PARENTHESIS33=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_q_c_resource_content440); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS33);

            pushFollow(FOLLOW_c_resource_content_in_q_c_resource_content442);
            c_resource_content34=c_resource_content();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_c_resource_content.add(c_resource_content34.getTree());
            RIGHT_PARENTHESIS35=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_q_c_resource_content444); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS35);


            }



            // AST REWRITE
            // elements: UQ, c_resource_content
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 61:62: -> ^( UQ c_resource_content )
            {
                // KWQLHead.g:61:64: ^( UQ c_resource_content )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_UQ.nextNode(), root_1);

                adaptor.addChild(root_1, stream_c_resource_content.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "q_c_resource_content"

    public static class q_cvalue_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "q_cvalue"
    // KWQLHead.g:63:1: q_cvalue : ( UQ LEFT_PARENTHESIS cvalue RIGHT_PARENTHESIS ) -> ^( UQ cvalue ) ;
    public final KWQLHead.q_cvalue_return q_cvalue() throws RecognitionException {
        KWQLHead.q_cvalue_return retval = new KWQLHead.q_cvalue_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token UQ36=null;
        Token LEFT_PARENTHESIS37=null;
        Token RIGHT_PARENTHESIS39=null;
        KWQLHead.cvalue_return cvalue38 = null;


        CommonTree UQ36_tree=null;
        CommonTree LEFT_PARENTHESIS37_tree=null;
        CommonTree RIGHT_PARENTHESIS39_tree=null;
        RewriteRuleTokenStream stream_UQ=new RewriteRuleTokenStream(adaptor,"token UQ");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_cvalue=new RewriteRuleSubtreeStream(adaptor,"rule cvalue");
        try {
            // KWQLHead.g:63:9: ( ( UQ LEFT_PARENTHESIS cvalue RIGHT_PARENTHESIS ) -> ^( UQ cvalue ) )
            // KWQLHead.g:63:11: ( UQ LEFT_PARENTHESIS cvalue RIGHT_PARENTHESIS )
            {
            // KWQLHead.g:63:11: ( UQ LEFT_PARENTHESIS cvalue RIGHT_PARENTHESIS )
            // KWQLHead.g:63:12: UQ LEFT_PARENTHESIS cvalue RIGHT_PARENTHESIS
            {
            UQ36=(Token)match(input,UQ,FOLLOW_UQ_in_q_cvalue460); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_UQ.add(UQ36);

            LEFT_PARENTHESIS37=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_q_cvalue462); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS37);

            pushFollow(FOLLOW_cvalue_in_q_cvalue464);
            cvalue38=cvalue();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_cvalue.add(cvalue38.getTree());
            RIGHT_PARENTHESIS39=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_q_cvalue466); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS39);


            }



            // AST REWRITE
            // elements: cvalue, UQ
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 63:57: -> ^( UQ cvalue )
            {
                // KWQLHead.g:63:59: ^( UQ cvalue )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_UQ.nextNode(), root_1);

                adaptor.addChild(root_1, stream_cvalue.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "q_cvalue"

    // $ANTLR start synpred8_KWQLHead
    public final void synpred8_KWQLHead_fragment() throws RecognitionException {   
        KWQLHead.c_resource_content_helper_return c = null;


        // KWQLHead.g:42:22: ( ( c_resource_content_helper ) ( conjunction c= c_resource_content_helper )* )
        // KWQLHead.g:42:22: ( c_resource_content_helper ) ( conjunction c= c_resource_content_helper )*
        {
        // KWQLHead.g:42:22: ( c_resource_content_helper )
        // KWQLHead.g:42:23: c_resource_content_helper
        {
        pushFollow(FOLLOW_c_resource_content_helper_in_synpred8_KWQLHead237);
        c_resource_content_helper();

        state._fsp--;
        if (state.failed) return ;

        }

        // KWQLHead.g:42:76: ( conjunction c= c_resource_content_helper )*
        loop9:
        do {
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( ((LA9_0>=CI && LA9_0<=TAG)||LA9_0==CONJUNCTION||LA9_0==UQ||LA9_0==QUAL) ) {
                alt9=1;
            }


            switch (alt9) {
        	case 1 :
        	    // KWQLHead.g:42:77: conjunction c= c_resource_content_helper
        	    {
        	    pushFollow(FOLLOW_conjunction_in_synpred8_KWQLHead242);
        	    conjunction();

        	    state._fsp--;
        	    if (state.failed) return ;
        	    pushFollow(FOLLOW_c_resource_content_helper_in_synpred8_KWQLHead246);
        	    c=c_resource_content_helper();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop9;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred8_KWQLHead

    // Delegated rules

    public final boolean synpred8_KWQLHead() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_KWQLHead_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_c_resource_term_in_head137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_q_c_resource_term_in_head142 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VARIABLE_in_cvalue157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomar_value_in_cvalue167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_q_cvalue_in_cvalue174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_resource188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_resource_in_c_resource_term207 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_c_resource_term209 = new BitSet(new long[]{0x00000000080010F0L});
    public static final BitSet FOLLOW_c_resource_content_in_c_resource_term211 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_c_resource_term214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_c_resource_content_helper_in_c_resource_content237 = new BitSet(new long[]{0x00000000080012F2L});
    public static final BitSet FOLLOW_conjunction_in_c_resource_content242 = new BitSet(new long[]{0x00000000080010F0L});
    public static final BitSet FOLLOW_c_resource_content_helper_in_c_resource_content246 = new BitSet(new long[]{0x00000000080012F2L});
    public static final BitSet FOLLOW_q_c_resource_content_in_c_resource_content260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONJUNCTION_in_conjunction272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_c_qualifier_in_c_resource_content_helper287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_c_resource_term_in_c_resource_content_helper291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_c_qualifier305 = new BitSet(new long[]{0x0000000CA0081000L});
    public static final BitSet FOLLOW_cvalue_in_c_qualifier307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_q_c_qualifier_in_c_qualifier323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_atomar_value340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DA_in_atomar_value350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_in_atomar_value360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_URI_in_atomar_value370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UQ_in_q_c_resource_term390 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_q_c_resource_term392 = new BitSet(new long[]{0x00000000080010F0L});
    public static final BitSet FOLLOW_c_resource_term_in_q_c_resource_term394 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_q_c_resource_term396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UQ_in_q_c_qualifier414 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_q_c_qualifier416 = new BitSet(new long[]{0x0000000008001000L});
    public static final BitSet FOLLOW_c_qualifier_in_q_c_qualifier418 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_q_c_qualifier420 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UQ_in_q_c_resource_content438 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_q_c_resource_content440 = new BitSet(new long[]{0x00000000080010F0L});
    public static final BitSet FOLLOW_c_resource_content_in_q_c_resource_content442 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_q_c_resource_content444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UQ_in_q_cvalue460 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_q_cvalue462 = new BitSet(new long[]{0x0000000CA0081000L});
    public static final BitSet FOLLOW_cvalue_in_q_cvalue464 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_q_cvalue466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_c_resource_content_helper_in_synpred8_KWQLHead237 = new BitSet(new long[]{0x00000000080012F2L});
    public static final BitSet FOLLOW_conjunction_in_synpred8_KWQLHead242 = new BitSet(new long[]{0x00000000080010F0L});
    public static final BitSet FOLLOW_c_resource_content_helper_in_synpred8_KWQLHead246 = new BitSet(new long[]{0x00000000080012F2L});

}
