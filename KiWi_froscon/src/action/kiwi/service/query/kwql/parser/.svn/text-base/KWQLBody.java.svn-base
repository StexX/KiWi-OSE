package kiwi.service.query.kwql.parser;
// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 KWQLBody.g 2010-04-19 10:33:10

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class KWQLBody extends Parser {
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


        public KWQLBody(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public KWQLBody(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return KWQLBody.tokenNames; }
    public String getGrammarFileName() { return "KWQLBody.g"; }

     
        protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) 
        throws RecognitionException 
        { RecognitionException e = new UnwantedTokenException(ttype, input);
    			throw e;
        } 
        
        
        public Object match(IntStream input, int ttype, BitSet follow)
                        throws RecognitionException
                 {
                         //System.out.println("match "+((TokenStream)input).LT(1));
                         Object matchedSymbol = getCurrentInputSymbol(input);
                         if ( input.LA(1)==ttype ) {
                                 input.consume();
                                 state.errorRecovery = false;
                                 state.failed = false;
                                 return matchedSymbol;
                         }
                         if ( state.backtracking>0 ) {
                                 state.failed = true;
                                 return matchedSymbol;
                         }
                         matchedSymbol = recoverFromMismatchedToken(input, ttype, follow);
                         return matchedSymbol;
                 }


    public static class body_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "body"
    // KWQLBody.g:67:1: body : ( ( unbound_complex_contentterm -> ^( RESOURCE ^( TYPE ) unbound_complex_contentterm ) ) | ( resource_term -> resource_term ) );
    public final KWQLBody.body_return body() throws RecognitionException {
        KWQLBody.body_return retval = new KWQLBody.body_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.unbound_complex_contentterm_return unbound_complex_contentterm1 = null;

        KWQLBody.resource_term_return resource_term2 = null;


        RewriteRuleSubtreeStream stream_resource_term=new RewriteRuleSubtreeStream(adaptor,"rule resource_term");
        RewriteRuleSubtreeStream stream_unbound_complex_contentterm=new RewriteRuleSubtreeStream(adaptor,"rule unbound_complex_contentterm");
        try {
            // KWQLBody.g:67:8: ( ( unbound_complex_contentterm -> ^( RESOURCE ^( TYPE ) unbound_complex_contentterm ) ) | ( resource_term -> resource_term ) )
            int alt1=2;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // KWQLBody.g:68:4: ( unbound_complex_contentterm -> ^( RESOURCE ^( TYPE ) unbound_complex_contentterm ) )
                    {
                    // KWQLBody.g:68:4: ( unbound_complex_contentterm -> ^( RESOURCE ^( TYPE ) unbound_complex_contentterm ) )
                    // KWQLBody.g:68:5: unbound_complex_contentterm
                    {
                    pushFollow(FOLLOW_unbound_complex_contentterm_in_body151);
                    unbound_complex_contentterm1=unbound_complex_contentterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_complex_contentterm.add(unbound_complex_contentterm1.getTree());


                    // AST REWRITE
                    // elements: unbound_complex_contentterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 68:32: -> ^( RESOURCE ^( TYPE ) unbound_complex_contentterm )
                    {
                        // KWQLBody.g:68:35: ^( RESOURCE ^( TYPE ) unbound_complex_contentterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:68:46: ^( TYPE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_unbound_complex_contentterm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:69:5: ( resource_term -> resource_term )
                    {
                    // KWQLBody.g:69:5: ( resource_term -> resource_term )
                    // KWQLBody.g:69:6: resource_term
                    {
                    pushFollow(FOLLOW_resource_term_in_body170);
                    resource_term2=resource_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_resource_term.add(resource_term2.getTree());


                    // AST REWRITE
                    // elements: resource_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 69:20: -> resource_term
                    {
                        adaptor.addChild(root_0, stream_resource_term.nextTree());

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "body"

    public static class resource_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "resource_term"
    // KWQLBody.g:70:1: resource_term : ( resource_term_helper -> resource_term_helper ) ( DISJUNCTION r= resource_term_helper -> ^( DISJUNCTION $resource_term $r) )* ;
    public final KWQLBody.resource_term_return resource_term() throws RecognitionException {
        KWQLBody.resource_term_return retval = new KWQLBody.resource_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token DISJUNCTION4=null;
        KWQLBody.resource_term_helper_return r = null;

        KWQLBody.resource_term_helper_return resource_term_helper3 = null;


        CommonTree DISJUNCTION4_tree=null;
        RewriteRuleTokenStream stream_DISJUNCTION=new RewriteRuleTokenStream(adaptor,"token DISJUNCTION");
        RewriteRuleSubtreeStream stream_resource_term_helper=new RewriteRuleSubtreeStream(adaptor,"rule resource_term_helper");
        try {
            // KWQLBody.g:70:16: ( ( resource_term_helper -> resource_term_helper ) ( DISJUNCTION r= resource_term_helper -> ^( DISJUNCTION $resource_term $r) )* )
            // KWQLBody.g:71:4: ( resource_term_helper -> resource_term_helper ) ( DISJUNCTION r= resource_term_helper -> ^( DISJUNCTION $resource_term $r) )*
            {
            // KWQLBody.g:71:4: ( resource_term_helper -> resource_term_helper )
            // KWQLBody.g:71:5: resource_term_helper
            {
            pushFollow(FOLLOW_resource_term_helper_in_resource_term188);
            resource_term_helper3=resource_term_helper();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_resource_term_helper.add(resource_term_helper3.getTree());


            // AST REWRITE
            // elements: resource_term_helper
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 71:25: -> resource_term_helper
            {
                adaptor.addChild(root_0, stream_resource_term_helper.nextTree());

            }

            retval.tree = root_0;}
            }

            // KWQLBody.g:71:49: ( DISJUNCTION r= resource_term_helper -> ^( DISJUNCTION $resource_term $r) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==DISJUNCTION) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // KWQLBody.g:71:50: DISJUNCTION r= resource_term_helper
            	    {
            	    DISJUNCTION4=(Token)match(input,DISJUNCTION,FOLLOW_DISJUNCTION_in_resource_term194); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_DISJUNCTION.add(DISJUNCTION4);

            	    pushFollow(FOLLOW_resource_term_helper_in_resource_term198);
            	    r=resource_term_helper();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_resource_term_helper.add(r.getTree());


            	    // AST REWRITE
            	    // elements: DISJUNCTION, r, resource_term
            	    // token labels: 
            	    // rule labels: retval, r
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

            	    root_0 = (CommonTree)adaptor.nil();
            	    // 71:84: -> ^( DISJUNCTION $resource_term $r)
            	    {
            	        // KWQLBody.g:71:86: ^( DISJUNCTION $resource_term $r)
            	        {
            	        CommonTree root_1 = (CommonTree)adaptor.nil();
            	        root_1 = (CommonTree)adaptor.becomeRoot(stream_DISJUNCTION.nextNode(), root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        adaptor.addChild(root_1, stream_r.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;}
            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "resource_term"

    public static class resource_term_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "resource_term_helper"
    // KWQLBody.g:74:1: resource_term_helper : ( ( expanded_resource -> expanded_resource ) | LEFT_PARENTHESIS ( resource_term -> resource_term ) RIGHT_PARENTHESIS );
    public final KWQLBody.resource_term_helper_return resource_term_helper() throws RecognitionException {
        KWQLBody.resource_term_helper_return retval = new KWQLBody.resource_term_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS6=null;
        Token RIGHT_PARENTHESIS8=null;
        KWQLBody.expanded_resource_return expanded_resource5 = null;

        KWQLBody.resource_term_return resource_term7 = null;


        CommonTree LEFT_PARENTHESIS6_tree=null;
        CommonTree RIGHT_PARENTHESIS8_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_expanded_resource=new RewriteRuleSubtreeStream(adaptor,"rule expanded_resource");
        RewriteRuleSubtreeStream stream_resource_term=new RewriteRuleSubtreeStream(adaptor,"rule resource_term");
        try {
            // KWQLBody.g:74:22: ( ( expanded_resource -> expanded_resource ) | LEFT_PARENTHESIS ( resource_term -> resource_term ) RIGHT_PARENTHESIS )
            int alt3=2;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // KWQLBody.g:75:4: ( expanded_resource -> expanded_resource )
                    {
                    // KWQLBody.g:75:4: ( expanded_resource -> expanded_resource )
                    // KWQLBody.g:75:5: expanded_resource
                    {
                    pushFollow(FOLLOW_expanded_resource_in_resource_term_helper227);
                    expanded_resource5=expanded_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_expanded_resource.add(expanded_resource5.getTree());


                    // AST REWRITE
                    // elements: expanded_resource
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 75:22: -> expanded_resource
                    {
                        adaptor.addChild(root_0, stream_expanded_resource.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:76:6: LEFT_PARENTHESIS ( resource_term -> resource_term ) RIGHT_PARENTHESIS
                    {
                    LEFT_PARENTHESIS6=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_resource_term_helper237); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS6);

                    // KWQLBody.g:76:23: ( resource_term -> resource_term )
                    // KWQLBody.g:76:24: resource_term
                    {
                    pushFollow(FOLLOW_resource_term_in_resource_term_helper240);
                    resource_term7=resource_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_resource_term.add(resource_term7.getTree());


                    // AST REWRITE
                    // elements: resource_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 76:37: -> resource_term
                    {
                        adaptor.addChild(root_0, stream_resource_term.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    RIGHT_PARENTHESIS8=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_resource_term_helper245); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS8);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "resource_term_helper"

    public static class non_ci_resourceterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "non_ci_resourceterm"
    // KWQLBody.g:78:1: non_ci_resourceterm : ( ( ( non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) ) | NEGATION non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) ) | NEGATION LEFT_PARENTHESIS non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) ) );
    public final KWQLBody.non_ci_resourceterm_return non_ci_resourceterm() throws RecognitionException {
        KWQLBody.non_ci_resourceterm_return retval = new KWQLBody.non_ci_resourceterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS10=null;
        Token RIGHT_PARENTHESIS12=null;
        Token NEGATION13=null;
        Token LEFT_PARENTHESIS15=null;
        Token RIGHT_PARENTHESIS17=null;
        Token NEGATION18=null;
        Token LEFT_PARENTHESIS19=null;
        Token LEFT_PARENTHESIS21=null;
        Token RIGHT_PARENTHESIS23=null;
        Token RIGHT_PARENTHESIS24=null;
        KWQLBody.non_ci_resource_return non_ci_resource9 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper11 = null;

        KWQLBody.non_ci_resource_return non_ci_resource14 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper16 = null;

        KWQLBody.non_ci_resource_return non_ci_resource20 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper22 = null;


        CommonTree LEFT_PARENTHESIS10_tree=null;
        CommonTree RIGHT_PARENTHESIS12_tree=null;
        CommonTree NEGATION13_tree=null;
        CommonTree LEFT_PARENTHESIS15_tree=null;
        CommonTree RIGHT_PARENTHESIS17_tree=null;
        CommonTree NEGATION18_tree=null;
        CommonTree LEFT_PARENTHESIS19_tree=null;
        CommonTree LEFT_PARENTHESIS21_tree=null;
        CommonTree RIGHT_PARENTHESIS23_tree=null;
        CommonTree RIGHT_PARENTHESIS24_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_non_ci_resource=new RewriteRuleSubtreeStream(adaptor,"rule non_ci_resource");
        RewriteRuleSubtreeStream stream_complex_contentterm_helper=new RewriteRuleSubtreeStream(adaptor,"rule complex_contentterm_helper");
        try {
            // KWQLBody.g:78:21: ( ( ( non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) ) | NEGATION non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) ) | NEGATION LEFT_PARENTHESIS non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) ) )
            int alt4=3;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0>=LINK && LA4_0<=TAG)) ) {
                alt4=1;
            }
            else if ( (LA4_0==NEGATION) ) {
                int LA4_2 = input.LA(2);

                if ( (LA4_2==LEFT_PARENTHESIS) ) {
                    alt4=3;
                }
                else if ( ((LA4_2>=LINK && LA4_2<=TAG)) ) {
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
                    // KWQLBody.g:79:4: ( ( non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) )
                    {
                    // KWQLBody.g:79:4: ( ( non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) )
                    // KWQLBody.g:79:5: ( non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS )
                    {
                    // KWQLBody.g:79:5: ( non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS )
                    // KWQLBody.g:79:6: non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS
                    {
                    pushFollow(FOLLOW_non_ci_resource_in_non_ci_resourceterm263);
                    non_ci_resource9=non_ci_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resource.add(non_ci_resource9.getTree());
                    LEFT_PARENTHESIS10=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm266); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS10);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_non_ci_resourceterm268);
                    complex_contentterm_helper11=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper11.getTree());
                    RIGHT_PARENTHESIS12=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm270); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS12);


                    }



                    // AST REWRITE
                    // elements: complex_contentterm_helper, non_ci_resource
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 79:85: -> ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper )
                    {
                        // KWQLBody.g:79:88: ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:79:99: ^( TYPE non_ci_resource )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_2, stream_non_ci_resource.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:80:6: NEGATION non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS
                    {
                    NEGATION13=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_non_ci_resourceterm292); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION13);

                    pushFollow(FOLLOW_non_ci_resource_in_non_ci_resourceterm294);
                    non_ci_resource14=non_ci_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resource.add(non_ci_resource14.getTree());
                    LEFT_PARENTHESIS15=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm297); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS15);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_non_ci_resourceterm299);
                    complex_contentterm_helper16=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper16.getTree());
                    RIGHT_PARENTHESIS17=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm301); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS17);



                    // AST REWRITE
                    // elements: NEGATION, non_ci_resource, complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 80:93: -> ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) )
                    {
                        // KWQLBody.g:80:95: ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        // KWQLBody.g:80:106: ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_2);

                        // KWQLBody.g:80:117: ^( TYPE non_ci_resource )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_3);

                        adaptor.addChild(root_3, stream_non_ci_resource.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQLBody.g:81:5: NEGATION LEFT_PARENTHESIS non_ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS RIGHT_PARENTHESIS
                    {
                    NEGATION18=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_non_ci_resourceterm323); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION18);

                    LEFT_PARENTHESIS19=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm325); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS19);

                    pushFollow(FOLLOW_non_ci_resource_in_non_ci_resourceterm327);
                    non_ci_resource20=non_ci_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resource.add(non_ci_resource20.getTree());
                    LEFT_PARENTHESIS21=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm330); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS21);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_non_ci_resourceterm332);
                    complex_contentterm_helper22=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper22.getTree());
                    RIGHT_PARENTHESIS23=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm334); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS23);

                    RIGHT_PARENTHESIS24=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm336); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS24);



                    // AST REWRITE
                    // elements: NEGATION, non_ci_resource, complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 81:127: -> ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) )
                    {
                        // KWQLBody.g:81:129: ^( NEGATION ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        // KWQLBody.g:81:140: ^( RESOURCE ^( TYPE non_ci_resource ) complex_contentterm_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_2);

                        // KWQLBody.g:81:151: ^( TYPE non_ci_resource )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_3);

                        adaptor.addChild(root_3, stream_non_ci_resource.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "non_ci_resourceterm"

    public static class ci_resourceterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ci_resourceterm"
    // KWQLBody.g:83:1: ci_resourceterm : ( ( ( ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) ) | NEGATION ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) ) | NEGATION LEFT_PARENTHESIS ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) ) );
    public final KWQLBody.ci_resourceterm_return ci_resourceterm() throws RecognitionException {
        KWQLBody.ci_resourceterm_return retval = new KWQLBody.ci_resourceterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS26=null;
        Token RIGHT_PARENTHESIS28=null;
        Token NEGATION29=null;
        Token LEFT_PARENTHESIS31=null;
        Token RIGHT_PARENTHESIS33=null;
        Token NEGATION34=null;
        Token LEFT_PARENTHESIS35=null;
        Token LEFT_PARENTHESIS37=null;
        Token RIGHT_PARENTHESIS39=null;
        Token RIGHT_PARENTHESIS40=null;
        KWQLBody.ci_resource_return ci_resource25 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper27 = null;

        KWQLBody.ci_resource_return ci_resource30 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper32 = null;

        KWQLBody.ci_resource_return ci_resource36 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper38 = null;


        CommonTree LEFT_PARENTHESIS26_tree=null;
        CommonTree RIGHT_PARENTHESIS28_tree=null;
        CommonTree NEGATION29_tree=null;
        CommonTree LEFT_PARENTHESIS31_tree=null;
        CommonTree RIGHT_PARENTHESIS33_tree=null;
        CommonTree NEGATION34_tree=null;
        CommonTree LEFT_PARENTHESIS35_tree=null;
        CommonTree LEFT_PARENTHESIS37_tree=null;
        CommonTree RIGHT_PARENTHESIS39_tree=null;
        CommonTree RIGHT_PARENTHESIS40_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_ci_resource=new RewriteRuleSubtreeStream(adaptor,"rule ci_resource");
        RewriteRuleSubtreeStream stream_complex_contentterm_helper=new RewriteRuleSubtreeStream(adaptor,"rule complex_contentterm_helper");
        try {
            // KWQLBody.g:83:18: ( ( ( ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) ) | NEGATION ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) ) | NEGATION LEFT_PARENTHESIS ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS RIGHT_PARENTHESIS -> ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) ) )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==CI) ) {
                alt5=1;
            }
            else if ( (LA5_0==NEGATION) ) {
                int LA5_2 = input.LA(2);

                if ( (LA5_2==LEFT_PARENTHESIS) ) {
                    alt5=3;
                }
                else if ( (LA5_2==CI) ) {
                    alt5=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // KWQLBody.g:84:5: ( ( ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) )
                    {
                    // KWQLBody.g:84:5: ( ( ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS ) -> ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) )
                    // KWQLBody.g:84:6: ( ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS )
                    {
                    // KWQLBody.g:84:6: ( ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS )
                    // KWQLBody.g:84:7: ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS
                    {
                    pushFollow(FOLLOW_ci_resource_in_ci_resourceterm373);
                    ci_resource25=ci_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_resource.add(ci_resource25.getTree());
                    LEFT_PARENTHESIS26=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm376); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS26);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_ci_resourceterm378);
                    complex_contentterm_helper27=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper27.getTree());
                    RIGHT_PARENTHESIS28=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm380); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS28);


                    }



                    // AST REWRITE
                    // elements: ci_resource, complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 84:82: -> ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper )
                    {
                        // KWQLBody.g:84:84: ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:84:95: ^( TYPE ci_resource )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_2, stream_ci_resource.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:85:5: NEGATION ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS
                    {
                    NEGATION29=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_ci_resourceterm400); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION29);

                    pushFollow(FOLLOW_ci_resource_in_ci_resourceterm402);
                    ci_resource30=ci_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_resource.add(ci_resource30.getTree());
                    LEFT_PARENTHESIS31=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm405); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS31);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_ci_resourceterm407);
                    complex_contentterm_helper32=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper32.getTree());
                    RIGHT_PARENTHESIS33=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm409); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS33);



                    // AST REWRITE
                    // elements: NEGATION, complex_contentterm_helper, ci_resource
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 85:88: -> ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) )
                    {
                        // KWQLBody.g:85:90: ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        // KWQLBody.g:85:101: ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_2);

                        // KWQLBody.g:85:112: ^( TYPE ci_resource )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_3);

                        adaptor.addChild(root_3, stream_ci_resource.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQLBody.g:86:5: NEGATION LEFT_PARENTHESIS ci_resource LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS RIGHT_PARENTHESIS
                    {
                    NEGATION34=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_ci_resourceterm431); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION34);

                    LEFT_PARENTHESIS35=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm433); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS35);

                    pushFollow(FOLLOW_ci_resource_in_ci_resourceterm435);
                    ci_resource36=ci_resource();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_resource.add(ci_resource36.getTree());
                    LEFT_PARENTHESIS37=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm438); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS37);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_ci_resourceterm440);
                    complex_contentterm_helper38=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper38.getTree());
                    RIGHT_PARENTHESIS39=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm442); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS39);

                    RIGHT_PARENTHESIS40=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm444); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS40);



                    // AST REWRITE
                    // elements: ci_resource, NEGATION, complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 86:123: -> ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) )
                    {
                        // KWQLBody.g:86:125: ^( NEGATION ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        // KWQLBody.g:86:136: ^( RESOURCE ^( TYPE ci_resource ) complex_contentterm_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_2);

                        // KWQLBody.g:86:147: ^( TYPE ci_resource )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_3);

                        adaptor.addChild(root_3, stream_ci_resource.nextTree());

                        adaptor.addChild(root_2, root_3);
                        }
                        adaptor.addChild(root_2, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "ci_resourceterm"

    public static class expanded_resource_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expanded_resource"
    // KWQLBody.g:88:1: expanded_resource : ( ( non_ci_resourceterm -> ^( RESOURCE ^( TYPE ) non_ci_resourceterm ) ) | ( ci_resourceterm -> ci_resourceterm ) | ( complex_qualifierterm -> ^( RESOURCE ^( TYPE ) complex_qualifierterm ) ) | ( complex_valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) ) );
    public final KWQLBody.expanded_resource_return expanded_resource() throws RecognitionException {
        KWQLBody.expanded_resource_return retval = new KWQLBody.expanded_resource_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.non_ci_resourceterm_return non_ci_resourceterm41 = null;

        KWQLBody.ci_resourceterm_return ci_resourceterm42 = null;

        KWQLBody.complex_qualifierterm_return complex_qualifierterm43 = null;

        KWQLBody.complex_valueterm_return complex_valueterm44 = null;


        RewriteRuleSubtreeStream stream_ci_resourceterm=new RewriteRuleSubtreeStream(adaptor,"rule ci_resourceterm");
        RewriteRuleSubtreeStream stream_complex_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_valueterm");
        RewriteRuleSubtreeStream stream_non_ci_resourceterm=new RewriteRuleSubtreeStream(adaptor,"rule non_ci_resourceterm");
        RewriteRuleSubtreeStream stream_complex_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_qualifierterm");
        try {
            // KWQLBody.g:88:19: ( ( non_ci_resourceterm -> ^( RESOURCE ^( TYPE ) non_ci_resourceterm ) ) | ( ci_resourceterm -> ci_resourceterm ) | ( complex_qualifierterm -> ^( RESOURCE ^( TYPE ) complex_qualifierterm ) ) | ( complex_valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) ) )
            int alt6=4;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // KWQLBody.g:89:4: ( non_ci_resourceterm -> ^( RESOURCE ^( TYPE ) non_ci_resourceterm ) )
                    {
                    // KWQLBody.g:89:4: ( non_ci_resourceterm -> ^( RESOURCE ^( TYPE ) non_ci_resourceterm ) )
                    // KWQLBody.g:89:5: non_ci_resourceterm
                    {
                    pushFollow(FOLLOW_non_ci_resourceterm_in_expanded_resource477);
                    non_ci_resourceterm41=non_ci_resourceterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resourceterm.add(non_ci_resourceterm41.getTree());


                    // AST REWRITE
                    // elements: non_ci_resourceterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 89:24: -> ^( RESOURCE ^( TYPE ) non_ci_resourceterm )
                    {
                        // KWQLBody.g:89:26: ^( RESOURCE ^( TYPE ) non_ci_resourceterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:89:37: ^( TYPE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_non_ci_resourceterm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:90:5: ( ci_resourceterm -> ci_resourceterm )
                    {
                    // KWQLBody.g:90:5: ( ci_resourceterm -> ci_resourceterm )
                    // KWQLBody.g:90:6: ci_resourceterm
                    {
                    pushFollow(FOLLOW_ci_resourceterm_in_expanded_resource495);
                    ci_resourceterm42=ci_resourceterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_resourceterm.add(ci_resourceterm42.getTree());


                    // AST REWRITE
                    // elements: ci_resourceterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 90:21: -> ci_resourceterm
                    {
                        adaptor.addChild(root_0, stream_ci_resourceterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLBody.g:91:5: ( complex_qualifierterm -> ^( RESOURCE ^( TYPE ) complex_qualifierterm ) )
                    {
                    // KWQLBody.g:91:5: ( complex_qualifierterm -> ^( RESOURCE ^( TYPE ) complex_qualifierterm ) )
                    // KWQLBody.g:91:6: complex_qualifierterm
                    {
                    pushFollow(FOLLOW_complex_qualifierterm_in_expanded_resource505);
                    complex_qualifierterm43=complex_qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_qualifierterm.add(complex_qualifierterm43.getTree());


                    // AST REWRITE
                    // elements: complex_qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 91:27: -> ^( RESOURCE ^( TYPE ) complex_qualifierterm )
                    {
                        // KWQLBody.g:91:29: ^( RESOURCE ^( TYPE ) complex_qualifierterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:91:40: ^( TYPE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_complex_qualifierterm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLBody.g:92:5: ( complex_valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) )
                    {
                    // KWQLBody.g:92:5: ( complex_valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) )
                    // KWQLBody.g:92:6: complex_valueterm
                    {
                    pushFollow(FOLLOW_complex_valueterm_in_expanded_resource523);
                    complex_valueterm44=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm44.getTree());


                    // AST REWRITE
                    // elements: complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 92:24: -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) )
                    {
                        // KWQLBody.g:92:26: ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:92:37: ^( TYPE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:92:45: ^( QUALIFIER LABEL ^( complex_valueterm ) )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_2);

                        adaptor.addChild(root_2, (CommonTree)adaptor.create(LABEL, "LABEL"));
                        // KWQLBody.g:92:63: ^( complex_valueterm )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot(stream_complex_valueterm.nextNode(), root_3);

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "expanded_resource"

    public static class unbound_complex_contentterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unbound_complex_contentterm"
    // KWQLBody.g:94:1: unbound_complex_contentterm : ( NEGATION LEFT_PARENTHESIS unbound_complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION unbound_complex_contentterm_helper ) | unbound_complex_contentterm_helper );
    public final KWQLBody.unbound_complex_contentterm_return unbound_complex_contentterm() throws RecognitionException {
        KWQLBody.unbound_complex_contentterm_return retval = new KWQLBody.unbound_complex_contentterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION45=null;
        Token LEFT_PARENTHESIS46=null;
        Token RIGHT_PARENTHESIS48=null;
        KWQLBody.unbound_complex_contentterm_helper_return unbound_complex_contentterm_helper47 = null;

        KWQLBody.unbound_complex_contentterm_helper_return unbound_complex_contentterm_helper49 = null;


        CommonTree NEGATION45_tree=null;
        CommonTree LEFT_PARENTHESIS46_tree=null;
        CommonTree RIGHT_PARENTHESIS48_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_unbound_complex_contentterm_helper=new RewriteRuleSubtreeStream(adaptor,"rule unbound_complex_contentterm_helper");
        try {
            // KWQLBody.g:94:28: ( NEGATION LEFT_PARENTHESIS unbound_complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION unbound_complex_contentterm_helper ) | unbound_complex_contentterm_helper )
            int alt7=2;
            alt7 = dfa7.predict(input);
            switch (alt7) {
                case 1 :
                    // KWQLBody.g:95:4: NEGATION LEFT_PARENTHESIS unbound_complex_contentterm_helper RIGHT_PARENTHESIS
                    {
                    NEGATION45=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_unbound_complex_contentterm554); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION45);

                    LEFT_PARENTHESIS46=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_unbound_complex_contentterm556); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS46);

                    pushFollow(FOLLOW_unbound_complex_contentterm_helper_in_unbound_complex_contentterm558);
                    unbound_complex_contentterm_helper47=unbound_complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_complex_contentterm_helper.add(unbound_complex_contentterm_helper47.getTree());
                    RIGHT_PARENTHESIS48=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_unbound_complex_contentterm560); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS48);



                    // AST REWRITE
                    // elements: unbound_complex_contentterm_helper, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 95:82: -> ^( NEGATION unbound_complex_contentterm_helper )
                    {
                        // KWQLBody.g:95:84: ^( NEGATION unbound_complex_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_unbound_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // KWQLBody.g:96:5: unbound_complex_contentterm_helper
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_unbound_complex_contentterm_helper_in_unbound_complex_contentterm572);
                    unbound_complex_contentterm_helper49=unbound_complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unbound_complex_contentterm_helper49.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "unbound_complex_contentterm"

    public static class unbound_complex_contentterm_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unbound_complex_contentterm_helper"
    // KWQLBody.g:98:1: unbound_complex_contentterm_helper : ( ( unbound_contentterm opt )=> ( unbound_contentterm -> unbound_contentterm ) ( opt r= unbound_contentterm -> ^( AND $unbound_complex_contentterm_helper ^( opt $r) ) )+ | ( ( unbound_contentterm -> unbound_contentterm ) ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+ ) );
    public final KWQLBody.unbound_complex_contentterm_helper_return unbound_complex_contentterm_helper() throws RecognitionException {
        KWQLBody.unbound_complex_contentterm_helper_return retval = new KWQLBody.unbound_complex_contentterm_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.unbound_contentterm_return r = null;

        KWQLBody.unbound_contentterm_return unbound_contentterm50 = null;

        KWQLBody.opt_return opt51 = null;

        KWQLBody.unbound_contentterm_return unbound_contentterm52 = null;

        KWQLBody.connectives_return connectives53 = null;


        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        RewriteRuleSubtreeStream stream_unbound_contentterm=new RewriteRuleSubtreeStream(adaptor,"rule unbound_contentterm");
        RewriteRuleSubtreeStream stream_opt=new RewriteRuleSubtreeStream(adaptor,"rule opt");
        try {
            // KWQLBody.g:98:35: ( ( unbound_contentterm opt )=> ( unbound_contentterm -> unbound_contentterm ) ( opt r= unbound_contentterm -> ^( AND $unbound_complex_contentterm_helper ^( opt $r) ) )+ | ( ( unbound_contentterm -> unbound_contentterm ) ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+ ) )
            int alt10=2;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // KWQLBody.g:99:4: ( unbound_contentterm opt )=> ( unbound_contentterm -> unbound_contentterm ) ( opt r= unbound_contentterm -> ^( AND $unbound_complex_contentterm_helper ^( opt $r) ) )+
                    {
                    // KWQLBody.g:99:31: ( unbound_contentterm -> unbound_contentterm )
                    // KWQLBody.g:99:32: unbound_contentterm
                    {
                    pushFollow(FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper594);
                    unbound_contentterm50=unbound_contentterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_contentterm.add(unbound_contentterm50.getTree());


                    // AST REWRITE
                    // elements: unbound_contentterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 99:51: -> unbound_contentterm
                    {
                        adaptor.addChild(root_0, stream_unbound_contentterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    // KWQLBody.g:99:74: ( opt r= unbound_contentterm -> ^( AND $unbound_complex_contentterm_helper ^( opt $r) ) )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==OPTIONAL) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // KWQLBody.g:99:75: opt r= unbound_contentterm
                    	    {
                    	    pushFollow(FOLLOW_opt_in_unbound_complex_contentterm_helper600);
                    	    opt51=opt();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_opt.add(opt51.getTree());
                    	    pushFollow(FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper604);
                    	    r=unbound_contentterm();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_unbound_contentterm.add(r.getTree());


                    	    // AST REWRITE
                    	    // elements: opt, r, unbound_complex_contentterm_helper
                    	    // token labels: 
                    	    // rule labels: retval, r
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 99:101: -> ^( AND $unbound_complex_contentterm_helper ^( opt $r) )
                    	    {
                    	        // KWQLBody.g:99:103: ^( AND $unbound_complex_contentterm_helper ^( opt $r) )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        // KWQLBody.g:99:145: ^( opt $r)
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(stream_opt.nextNode(), root_2);

                    	        adaptor.addChild(root_2, stream_r.nextTree());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    if ( cnt8 >= 1 ) break loop8;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(8, input);
                                throw eee;
                        }
                        cnt8++;
                    } while (true);


                    }
                    break;
                case 2 :
                    // KWQLBody.g:100:5: ( ( unbound_contentterm -> unbound_contentterm ) ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+ )
                    {
                    // KWQLBody.g:100:5: ( ( unbound_contentterm -> unbound_contentterm ) ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+ )
                    // KWQLBody.g:100:6: ( unbound_contentterm -> unbound_contentterm ) ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+
                    {
                    // KWQLBody.g:100:6: ( unbound_contentterm -> unbound_contentterm )
                    // KWQLBody.g:100:7: unbound_contentterm
                    {
                    pushFollow(FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper629);
                    unbound_contentterm52=unbound_contentterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_contentterm.add(unbound_contentterm52.getTree());


                    // AST REWRITE
                    // elements: unbound_contentterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 100:26: -> unbound_contentterm
                    {
                        adaptor.addChild(root_0, stream_unbound_contentterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    // KWQLBody.g:100:49: ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>=LINK && LA9_0<=TAG)||(LA9_0>=CONJUNCTION && LA9_0<=NEGATION)||LA9_0==URI||(LA9_0>=RESQUAL && LA9_0<=QUAL)||LA9_0==INT||LA9_0==DA||LA9_0==KW||LA9_0==LEFT_PARENTHESIS) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // KWQLBody.g:100:50: connectives r= unbound_contentterm
                    	    {
                    	    pushFollow(FOLLOW_connectives_in_unbound_complex_contentterm_helper635);
                    	    connectives53=connectives();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_connectives.add(connectives53.getTree());
                    	    pushFollow(FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper639);
                    	    r=unbound_contentterm();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_unbound_contentterm.add(r.getTree());


                    	    // AST REWRITE
                    	    // elements: connectives, r, unbound_complex_contentterm_helper
                    	    // token labels: 
                    	    // rule labels: retval, r
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 100:84: -> ^( connectives $unbound_complex_contentterm_helper $r)
                    	    {
                    	        // KWQLBody.g:100:87: ^( connectives $unbound_complex_contentterm_helper $r)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(stream_connectives.nextNode(), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        adaptor.addChild(root_1, stream_r.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                    	    if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);


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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "unbound_complex_contentterm_helper"

    public static class unbound_contentterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unbound_contentterm"
    // KWQLBody.g:102:1: unbound_contentterm : ( unbound_contentterm_helper | NEGATION LEFT_PARENTHESIS unbound_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION unbound_contentterm_helper ) | NEGATION unbound_contentterm_helper -> ^( NEGATION unbound_contentterm_helper ) );
    public final KWQLBody.unbound_contentterm_return unbound_contentterm() throws RecognitionException {
        KWQLBody.unbound_contentterm_return retval = new KWQLBody.unbound_contentterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION55=null;
        Token LEFT_PARENTHESIS56=null;
        Token RIGHT_PARENTHESIS58=null;
        Token NEGATION59=null;
        KWQLBody.unbound_contentterm_helper_return unbound_contentterm_helper54 = null;

        KWQLBody.unbound_contentterm_helper_return unbound_contentterm_helper57 = null;

        KWQLBody.unbound_contentterm_helper_return unbound_contentterm_helper60 = null;


        CommonTree NEGATION55_tree=null;
        CommonTree LEFT_PARENTHESIS56_tree=null;
        CommonTree RIGHT_PARENTHESIS58_tree=null;
        CommonTree NEGATION59_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_unbound_contentterm_helper=new RewriteRuleSubtreeStream(adaptor,"rule unbound_contentterm_helper");
        try {
            // KWQLBody.g:102:21: ( unbound_contentterm_helper | NEGATION LEFT_PARENTHESIS unbound_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION unbound_contentterm_helper ) | NEGATION unbound_contentterm_helper -> ^( NEGATION unbound_contentterm_helper ) )
            int alt11=3;
            alt11 = dfa11.predict(input);
            switch (alt11) {
                case 1 :
                    // KWQLBody.g:103:4: unbound_contentterm_helper
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_unbound_contentterm_helper_in_unbound_contentterm669);
                    unbound_contentterm_helper54=unbound_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, unbound_contentterm_helper54.getTree());

                    }
                    break;
                case 2 :
                    // KWQLBody.g:104:5: NEGATION LEFT_PARENTHESIS unbound_contentterm_helper RIGHT_PARENTHESIS
                    {
                    NEGATION55=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_unbound_contentterm675); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION55);

                    LEFT_PARENTHESIS56=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_unbound_contentterm677); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS56);

                    pushFollow(FOLLOW_unbound_contentterm_helper_in_unbound_contentterm679);
                    unbound_contentterm_helper57=unbound_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_contentterm_helper.add(unbound_contentterm_helper57.getTree());
                    RIGHT_PARENTHESIS58=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_unbound_contentterm681); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS58);



                    // AST REWRITE
                    // elements: unbound_contentterm_helper, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 104:75: -> ^( NEGATION unbound_contentterm_helper )
                    {
                        // KWQLBody.g:104:77: ^( NEGATION unbound_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_unbound_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQLBody.g:105:5: NEGATION unbound_contentterm_helper
                    {
                    NEGATION59=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_unbound_contentterm693); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION59);

                    pushFollow(FOLLOW_unbound_contentterm_helper_in_unbound_contentterm695);
                    unbound_contentterm_helper60=unbound_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_contentterm_helper.add(unbound_contentterm_helper60.getTree());


                    // AST REWRITE
                    // elements: unbound_contentterm_helper, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 105:41: -> ^( NEGATION unbound_contentterm_helper )
                    {
                        // KWQLBody.g:105:43: ^( NEGATION unbound_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_unbound_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "unbound_contentterm"

    public static class unbound_contentterm_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unbound_contentterm_helper"
    // KWQLBody.g:107:1: unbound_contentterm_helper : ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm ) ) ;
    public final KWQLBody.unbound_contentterm_helper_return unbound_contentterm_helper() throws RecognitionException {
        KWQLBody.unbound_contentterm_helper_return retval = new KWQLBody.unbound_contentterm_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS64=null;
        Token RIGHT_PARENTHESIS66=null;
        KWQLBody.complex_qualifierterm_return complex_qualifierterm61 = null;

        KWQLBody.complex_valueterm_return complex_valueterm62 = null;

        KWQLBody.non_ci_resourceterm_return non_ci_resourceterm63 = null;

        KWQLBody.unbound_complex_contentterm_return unbound_complex_contentterm65 = null;


        CommonTree LEFT_PARENTHESIS64_tree=null;
        CommonTree RIGHT_PARENTHESIS66_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_complex_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_valueterm");
        RewriteRuleSubtreeStream stream_unbound_complex_contentterm=new RewriteRuleSubtreeStream(adaptor,"rule unbound_complex_contentterm");
        RewriteRuleSubtreeStream stream_non_ci_resourceterm=new RewriteRuleSubtreeStream(adaptor,"rule non_ci_resourceterm");
        RewriteRuleSubtreeStream stream_complex_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_qualifierterm");
        try {
            // KWQLBody.g:107:27: ( ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm ) ) )
            // KWQLBody.g:108:4: ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm ) )
            {
            // KWQLBody.g:108:4: ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm ) )
            int alt12=4;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // KWQLBody.g:108:5: ( complex_qualifierterm -> complex_qualifierterm )
                    {
                    // KWQLBody.g:108:5: ( complex_qualifierterm -> complex_qualifierterm )
                    // KWQLBody.g:108:6: complex_qualifierterm
                    {
                    pushFollow(FOLLOW_complex_qualifierterm_in_unbound_contentterm_helper722);
                    complex_qualifierterm61=complex_qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_qualifierterm.add(complex_qualifierterm61.getTree());


                    // AST REWRITE
                    // elements: complex_qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 108:27: -> complex_qualifierterm
                    {
                        adaptor.addChild(root_0, stream_complex_qualifierterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:109:5: ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) )
                    {
                    // KWQLBody.g:109:5: ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) )
                    // KWQLBody.g:109:6: complex_valueterm
                    {
                    pushFollow(FOLLOW_complex_valueterm_in_unbound_contentterm_helper732);
                    complex_valueterm62=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm62.getTree());


                    // AST REWRITE
                    // elements: complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 109:23: -> ^( QUALIFIER LABEL ^( complex_valueterm ) )
                    {
                        // KWQLBody.g:109:25: ^( QUALIFIER LABEL ^( complex_valueterm ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(LABEL, "LABEL"));
                        // KWQLBody.g:109:43: ^( complex_valueterm )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_complex_valueterm.nextNode(), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLBody.g:110:5: ( non_ci_resourceterm -> non_ci_resourceterm )
                    {
                    // KWQLBody.g:110:5: ( non_ci_resourceterm -> non_ci_resourceterm )
                    // KWQLBody.g:110:6: non_ci_resourceterm
                    {
                    pushFollow(FOLLOW_non_ci_resourceterm_in_unbound_contentterm_helper750);
                    non_ci_resourceterm63=non_ci_resourceterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resourceterm.add(non_ci_resourceterm63.getTree());


                    // AST REWRITE
                    // elements: non_ci_resourceterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 110:25: -> non_ci_resourceterm
                    {
                        adaptor.addChild(root_0, stream_non_ci_resourceterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLBody.g:111:5: ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm )
                    {
                    // KWQLBody.g:111:5: ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm )
                    // KWQLBody.g:111:6: LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS
                    {
                    LEFT_PARENTHESIS64=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_unbound_contentterm_helper760); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS64);

                    pushFollow(FOLLOW_unbound_complex_contentterm_in_unbound_contentterm_helper762);
                    unbound_complex_contentterm65=unbound_complex_contentterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unbound_complex_contentterm.add(unbound_complex_contentterm65.getTree());
                    RIGHT_PARENTHESIS66=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_unbound_contentterm_helper764); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS66);



                    // AST REWRITE
                    // elements: unbound_complex_contentterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 111:69: -> unbound_complex_contentterm
                    {
                        adaptor.addChild(root_0, stream_unbound_complex_contentterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "unbound_contentterm_helper"

    public static class complex_contentterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "complex_contentterm"
    // KWQLBody.g:113:1: complex_contentterm : ( NEGATION LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) | complex_contentterm_helper );
    public final KWQLBody.complex_contentterm_return complex_contentterm() throws RecognitionException {
        KWQLBody.complex_contentterm_return retval = new KWQLBody.complex_contentterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION67=null;
        Token LEFT_PARENTHESIS68=null;
        Token RIGHT_PARENTHESIS70=null;
        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper69 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper71 = null;


        CommonTree NEGATION67_tree=null;
        CommonTree LEFT_PARENTHESIS68_tree=null;
        CommonTree RIGHT_PARENTHESIS70_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_complex_contentterm_helper=new RewriteRuleSubtreeStream(adaptor,"rule complex_contentterm_helper");
        try {
            // KWQLBody.g:113:21: ( NEGATION LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) | complex_contentterm_helper )
            int alt13=2;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // KWQLBody.g:114:4: NEGATION LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS
                    {
                    NEGATION67=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_complex_contentterm783); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION67);

                    LEFT_PARENTHESIS68=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_complex_contentterm785); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS68);

                    pushFollow(FOLLOW_complex_contentterm_helper_in_complex_contentterm787);
                    complex_contentterm_helper69=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper69.getTree());
                    RIGHT_PARENTHESIS70=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_complex_contentterm789); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS70);



                    // AST REWRITE
                    // elements: complex_contentterm_helper, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 114:74: -> ^( NEGATION complex_contentterm_helper )
                    {
                        // KWQLBody.g:114:76: ^( NEGATION complex_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // KWQLBody.g:115:5: complex_contentterm_helper
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_complex_contentterm_helper_in_complex_contentterm801);
                    complex_contentterm_helper71=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, complex_contentterm_helper71.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "complex_contentterm"

    public static class complex_contentterm_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "complex_contentterm_helper"
    // KWQLBody.g:117:1: complex_contentterm_helper : ( ( contentterm opt )=> ( contentterm -> contentterm ) ( opt r= contentterm -> ^( AND $complex_contentterm_helper ^( opt $r) ) )* | ( ( contentterm -> contentterm ) ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )* ) );
    public final KWQLBody.complex_contentterm_helper_return complex_contentterm_helper() throws RecognitionException {
        KWQLBody.complex_contentterm_helper_return retval = new KWQLBody.complex_contentterm_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.contentterm_return r = null;

        KWQLBody.contentterm_return contentterm72 = null;

        KWQLBody.opt_return opt73 = null;

        KWQLBody.contentterm_return contentterm74 = null;

        KWQLBody.connectives_return connectives75 = null;


        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        RewriteRuleSubtreeStream stream_contentterm=new RewriteRuleSubtreeStream(adaptor,"rule contentterm");
        RewriteRuleSubtreeStream stream_opt=new RewriteRuleSubtreeStream(adaptor,"rule opt");
        try {
            // KWQLBody.g:117:28: ( ( contentterm opt )=> ( contentterm -> contentterm ) ( opt r= contentterm -> ^( AND $complex_contentterm_helper ^( opt $r) ) )* | ( ( contentterm -> contentterm ) ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )* ) )
            int alt16=2;
            alt16 = dfa16.predict(input);
            switch (alt16) {
                case 1 :
                    // KWQLBody.g:118:4: ( contentterm opt )=> ( contentterm -> contentterm ) ( opt r= contentterm -> ^( AND $complex_contentterm_helper ^( opt $r) ) )*
                    {
                    // KWQLBody.g:118:23: ( contentterm -> contentterm )
                    // KWQLBody.g:118:24: contentterm
                    {
                    pushFollow(FOLLOW_contentterm_in_complex_contentterm_helper823);
                    contentterm72=contentterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_contentterm.add(contentterm72.getTree());


                    // AST REWRITE
                    // elements: contentterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 118:35: -> contentterm
                    {
                        adaptor.addChild(root_0, stream_contentterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    // KWQLBody.g:118:50: ( opt r= contentterm -> ^( AND $complex_contentterm_helper ^( opt $r) ) )*
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( (LA14_0==OPTIONAL) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // KWQLBody.g:118:51: opt r= contentterm
                    	    {
                    	    pushFollow(FOLLOW_opt_in_complex_contentterm_helper829);
                    	    opt73=opt();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_opt.add(opt73.getTree());
                    	    pushFollow(FOLLOW_contentterm_in_complex_contentterm_helper833);
                    	    r=contentterm();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_contentterm.add(r.getTree());


                    	    // AST REWRITE
                    	    // elements: r, complex_contentterm_helper, opt
                    	    // token labels: 
                    	    // rule labels: retval, r
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 118:69: -> ^( AND $complex_contentterm_helper ^( opt $r) )
                    	    {
                    	        // KWQLBody.g:118:72: ^( AND $complex_contentterm_helper ^( opt $r) )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        // KWQLBody.g:118:106: ^( opt $r)
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(stream_opt.nextNode(), root_2);

                    	        adaptor.addChild(root_2, stream_r.nextTree());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    break loop14;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // KWQLBody.g:119:5: ( ( contentterm -> contentterm ) ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )* )
                    {
                    // KWQLBody.g:119:5: ( ( contentterm -> contentterm ) ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )* )
                    // KWQLBody.g:119:6: ( contentterm -> contentterm ) ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )*
                    {
                    // KWQLBody.g:119:6: ( contentterm -> contentterm )
                    // KWQLBody.g:119:7: contentterm
                    {
                    pushFollow(FOLLOW_contentterm_in_complex_contentterm_helper859);
                    contentterm74=contentterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_contentterm.add(contentterm74.getTree());


                    // AST REWRITE
                    // elements: contentterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 119:18: -> contentterm
                    {
                        adaptor.addChild(root_0, stream_contentterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    // KWQLBody.g:119:33: ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( ((LA15_0>=LINK && LA15_0<=TAG)||(LA15_0>=CONJUNCTION && LA15_0<=NEGATION)||LA15_0==URI||(LA15_0>=RESQUAL && LA15_0<=QUAL)||LA15_0==INT||LA15_0==DA||LA15_0==KW||LA15_0==LEFT_PARENTHESIS) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // KWQLBody.g:119:34: connectives r= contentterm
                    	    {
                    	    pushFollow(FOLLOW_connectives_in_complex_contentterm_helper865);
                    	    connectives75=connectives();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_connectives.add(connectives75.getTree());
                    	    pushFollow(FOLLOW_contentterm_in_complex_contentterm_helper869);
                    	    r=contentterm();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_contentterm.add(r.getTree());


                    	    // AST REWRITE
                    	    // elements: r, complex_contentterm_helper, connectives
                    	    // token labels: 
                    	    // rule labels: retval, r
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 119:60: -> ^( connectives $complex_contentterm_helper $r)
                    	    {
                    	        // KWQLBody.g:119:63: ^( connectives $complex_contentterm_helper $r)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(stream_connectives.nextNode(), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        adaptor.addChild(root_1, stream_r.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "complex_contentterm_helper"

    public static class contentterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "contentterm"
    // KWQLBody.g:121:1: contentterm : ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) ) | ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS ) ) ;
    public final KWQLBody.contentterm_return contentterm() throws RecognitionException {
        KWQLBody.contentterm_return retval = new KWQLBody.contentterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION79=null;
        Token LEFT_PARENTHESIS80=null;
        Token RIGHT_PARENTHESIS82=null;
        Token LEFT_PARENTHESIS83=null;
        Token RIGHT_PARENTHESIS85=null;
        KWQLBody.complex_qualifierterm_return complex_qualifierterm76 = null;

        KWQLBody.complex_valueterm_return complex_valueterm77 = null;

        KWQLBody.non_ci_resourceterm_return non_ci_resourceterm78 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper81 = null;

        KWQLBody.complex_contentterm_helper_return complex_contentterm_helper84 = null;


        CommonTree NEGATION79_tree=null;
        CommonTree LEFT_PARENTHESIS80_tree=null;
        CommonTree RIGHT_PARENTHESIS82_tree=null;
        CommonTree LEFT_PARENTHESIS83_tree=null;
        CommonTree RIGHT_PARENTHESIS85_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_complex_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_valueterm");
        RewriteRuleSubtreeStream stream_non_ci_resourceterm=new RewriteRuleSubtreeStream(adaptor,"rule non_ci_resourceterm");
        RewriteRuleSubtreeStream stream_complex_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_qualifierterm");
        RewriteRuleSubtreeStream stream_complex_contentterm_helper=new RewriteRuleSubtreeStream(adaptor,"rule complex_contentterm_helper");
        try {
            // KWQLBody.g:121:14: ( ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) ) | ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS ) ) )
            // KWQLBody.g:122:4: ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) ) | ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS ) )
            {
            // KWQLBody.g:122:4: ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) ) | ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS ) )
            int alt17=5;
            alt17 = dfa17.predict(input);
            switch (alt17) {
                case 1 :
                    // KWQLBody.g:122:5: ( complex_qualifierterm -> complex_qualifierterm )
                    {
                    // KWQLBody.g:122:5: ( complex_qualifierterm -> complex_qualifierterm )
                    // KWQLBody.g:122:6: complex_qualifierterm
                    {
                    pushFollow(FOLLOW_complex_qualifierterm_in_contentterm902);
                    complex_qualifierterm76=complex_qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_qualifierterm.add(complex_qualifierterm76.getTree());


                    // AST REWRITE
                    // elements: complex_qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 122:27: -> complex_qualifierterm
                    {
                        adaptor.addChild(root_0, stream_complex_qualifierterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:123:5: ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) )
                    {
                    // KWQLBody.g:123:5: ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) )
                    // KWQLBody.g:123:6: complex_valueterm
                    {
                    pushFollow(FOLLOW_complex_valueterm_in_contentterm912);
                    complex_valueterm77=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm77.getTree());


                    // AST REWRITE
                    // elements: complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 123:23: -> ^( QUALIFIER LABEL ^( complex_valueterm ) )
                    {
                        // KWQLBody.g:123:25: ^( QUALIFIER LABEL ^( complex_valueterm ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        adaptor.addChild(root_1, (CommonTree)adaptor.create(LABEL, "LABEL"));
                        // KWQLBody.g:123:43: ^( complex_valueterm )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_complex_valueterm.nextNode(), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLBody.g:124:5: ( non_ci_resourceterm -> non_ci_resourceterm )
                    {
                    // KWQLBody.g:124:5: ( non_ci_resourceterm -> non_ci_resourceterm )
                    // KWQLBody.g:124:6: non_ci_resourceterm
                    {
                    pushFollow(FOLLOW_non_ci_resourceterm_in_contentterm930);
                    non_ci_resourceterm78=non_ci_resourceterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resourceterm.add(non_ci_resourceterm78.getTree());


                    // AST REWRITE
                    // elements: non_ci_resourceterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 124:25: -> non_ci_resourceterm
                    {
                        adaptor.addChild(root_0, stream_non_ci_resourceterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLBody.g:125:5: ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) )
                    {
                    // KWQLBody.g:125:5: ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) )
                    // KWQLBody.g:125:6: NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS
                    {
                    NEGATION79=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_contentterm940); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION79);

                    LEFT_PARENTHESIS80=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_contentterm942); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS80);

                    // KWQLBody.g:125:32: ( complex_contentterm_helper -> complex_contentterm_helper )
                    // KWQLBody.g:125:33: complex_contentterm_helper
                    {
                    pushFollow(FOLLOW_complex_contentterm_helper_in_contentterm945);
                    complex_contentterm_helper81=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper81.getTree());


                    // AST REWRITE
                    // elements: complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 125:59: -> complex_contentterm_helper
                    {
                        adaptor.addChild(root_0, stream_complex_contentterm_helper.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    RIGHT_PARENTHESIS82=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_contentterm950); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS82);



                    // AST REWRITE
                    // elements: NEGATION, complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 125:107: -> ^( NEGATION complex_contentterm_helper )
                    {
                        // KWQLBody.g:125:109: ^( NEGATION complex_contentterm_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_complex_contentterm_helper.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 5 :
                    // KWQLBody.g:126:5: ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS )
                    {
                    // KWQLBody.g:126:5: ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS )
                    // KWQLBody.g:126:7: LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS
                    {
                    LEFT_PARENTHESIS83=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_contentterm966); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS83);

                    // KWQLBody.g:126:24: ( complex_contentterm_helper -> complex_contentterm_helper )
                    // KWQLBody.g:126:25: complex_contentterm_helper
                    {
                    pushFollow(FOLLOW_complex_contentterm_helper_in_contentterm969);
                    complex_contentterm_helper84=complex_contentterm_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_contentterm_helper.add(complex_contentterm_helper84.getTree());


                    // AST REWRITE
                    // elements: complex_contentterm_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 126:51: -> complex_contentterm_helper
                    {
                        adaptor.addChild(root_0, stream_complex_contentterm_helper.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    RIGHT_PARENTHESIS85=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_contentterm974); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS85);


                    }


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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "contentterm"

    public static class subquery_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subquery_term"
    // KWQLBody.g:128:1: subquery_term : ( ( non_ci_resourceterm -> non_ci_resourceterm ) | ( ci_resourceterm -> ci_resourceterm ) | ( valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) ) ) | ( qualifierterm -> qualifierterm ) -> ^( RESOURCE ^( TYPE ) qualifierterm ) | ( LEFT_PARENTHESIS body RIGHT_PARENTHESIS -> body ) );
    public final KWQLBody.subquery_term_return subquery_term() throws RecognitionException {
        KWQLBody.subquery_term_return retval = new KWQLBody.subquery_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS90=null;
        Token RIGHT_PARENTHESIS92=null;
        KWQLBody.non_ci_resourceterm_return non_ci_resourceterm86 = null;

        KWQLBody.ci_resourceterm_return ci_resourceterm87 = null;

        KWQLBody.valueterm_return valueterm88 = null;

        KWQLBody.qualifierterm_return qualifierterm89 = null;

        KWQLBody.body_return body91 = null;


        CommonTree LEFT_PARENTHESIS90_tree=null;
        CommonTree RIGHT_PARENTHESIS92_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule valueterm");
        RewriteRuleSubtreeStream stream_body=new RewriteRuleSubtreeStream(adaptor,"rule body");
        RewriteRuleSubtreeStream stream_ci_resourceterm=new RewriteRuleSubtreeStream(adaptor,"rule ci_resourceterm");
        RewriteRuleSubtreeStream stream_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule qualifierterm");
        RewriteRuleSubtreeStream stream_non_ci_resourceterm=new RewriteRuleSubtreeStream(adaptor,"rule non_ci_resourceterm");
        try {
            // KWQLBody.g:128:16: ( ( non_ci_resourceterm -> non_ci_resourceterm ) | ( ci_resourceterm -> ci_resourceterm ) | ( valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) ) ) | ( qualifierterm -> qualifierterm ) -> ^( RESOURCE ^( TYPE ) qualifierterm ) | ( LEFT_PARENTHESIS body RIGHT_PARENTHESIS -> body ) )
            int alt18=5;
            alt18 = dfa18.predict(input);
            switch (alt18) {
                case 1 :
                    // KWQLBody.g:129:4: ( non_ci_resourceterm -> non_ci_resourceterm )
                    {
                    // KWQLBody.g:129:4: ( non_ci_resourceterm -> non_ci_resourceterm )
                    // KWQLBody.g:129:5: non_ci_resourceterm
                    {
                    pushFollow(FOLLOW_non_ci_resourceterm_in_subquery_term994);
                    non_ci_resourceterm86=non_ci_resourceterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_non_ci_resourceterm.add(non_ci_resourceterm86.getTree());


                    // AST REWRITE
                    // elements: non_ci_resourceterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 129:24: -> non_ci_resourceterm
                    {
                        adaptor.addChild(root_0, stream_non_ci_resourceterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:130:5: ( ci_resourceterm -> ci_resourceterm )
                    {
                    // KWQLBody.g:130:5: ( ci_resourceterm -> ci_resourceterm )
                    // KWQLBody.g:130:6: ci_resourceterm
                    {
                    pushFollow(FOLLOW_ci_resourceterm_in_subquery_term1004);
                    ci_resourceterm87=ci_resourceterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_resourceterm.add(ci_resourceterm87.getTree());


                    // AST REWRITE
                    // elements: ci_resourceterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 130:21: -> ci_resourceterm
                    {
                        adaptor.addChild(root_0, stream_ci_resourceterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLBody.g:131:5: ( valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) ) )
                    {
                    // KWQLBody.g:131:5: ( valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) ) )
                    // KWQLBody.g:131:6: valueterm
                    {
                    pushFollow(FOLLOW_valueterm_in_subquery_term1014);
                    valueterm88=valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_valueterm.add(valueterm88.getTree());


                    // AST REWRITE
                    // elements: valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 131:15: -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) )
                    {
                        // KWQLBody.g:131:17: ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:131:28: ^( TYPE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:131:36: ^( QUALIFIER ^( LABEL ) ^( valueterm ) )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_2);

                        // KWQLBody.g:131:48: ^( LABEL )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_3);

                        adaptor.addChild(root_2, root_3);
                        }
                        // KWQLBody.g:131:57: ^( valueterm )
                        {
                        CommonTree root_3 = (CommonTree)adaptor.nil();
                        root_3 = (CommonTree)adaptor.becomeRoot(stream_valueterm.nextNode(), root_3);

                        adaptor.addChild(root_2, root_3);
                        }

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLBody.g:132:5: ( qualifierterm -> qualifierterm )
                    {
                    // KWQLBody.g:132:5: ( qualifierterm -> qualifierterm )
                    // KWQLBody.g:132:6: qualifierterm
                    {
                    pushFollow(FOLLOW_qualifierterm_in_subquery_term1042);
                    qualifierterm89=qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_qualifierterm.add(qualifierterm89.getTree());


                    // AST REWRITE
                    // elements: qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 132:19: -> qualifierterm
                    {
                        adaptor.addChild(root_0, stream_qualifierterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }



                    // AST REWRITE
                    // elements: qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 132:35: -> ^( RESOURCE ^( TYPE ) qualifierterm )
                    {
                        // KWQLBody.g:132:37: ^( RESOURCE ^( TYPE ) qualifierterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RESOURCE, "RESOURCE"), root_1);

                        // KWQLBody.g:132:48: ^( TYPE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TYPE, "TYPE"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_qualifierterm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // KWQLBody.g:133:5: ( LEFT_PARENTHESIS body RIGHT_PARENTHESIS -> body )
                    {
                    // KWQLBody.g:133:5: ( LEFT_PARENTHESIS body RIGHT_PARENTHESIS -> body )
                    // KWQLBody.g:133:6: LEFT_PARENTHESIS body RIGHT_PARENTHESIS
                    {
                    LEFT_PARENTHESIS90=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_subquery_term1061); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS90);

                    pushFollow(FOLLOW_body_in_subquery_term1063);
                    body91=body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_body.add(body91.getTree());
                    RIGHT_PARENTHESIS92=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_subquery_term1065); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS92);



                    // AST REWRITE
                    // elements: body
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 133:46: -> body
                    {
                        adaptor.addChild(root_0, stream_body.nextTree());

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "subquery_term"

    public static class complex_qualifierterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "complex_qualifierterm"
    // KWQLBody.g:135:1: complex_qualifierterm : ( ( qualifierterm opt )=> ( ( ( qualifierterm -> qualifierterm ) ) ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )* ) | ( ( ( qualifierterm -> qualifierterm ) ) ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )* ) );
    public final KWQLBody.complex_qualifierterm_return complex_qualifierterm() throws RecognitionException {
        KWQLBody.complex_qualifierterm_return retval = new KWQLBody.complex_qualifierterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.qualifierterm_return r = null;

        KWQLBody.qualifierterm_return qualifierterm93 = null;

        KWQLBody.opt_return opt94 = null;

        KWQLBody.qualifierterm_return qualifierterm95 = null;

        KWQLBody.connectives_return connectives96 = null;


        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        RewriteRuleSubtreeStream stream_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule qualifierterm");
        RewriteRuleSubtreeStream stream_opt=new RewriteRuleSubtreeStream(adaptor,"rule opt");
        try {
            // KWQLBody.g:135:23: ( ( qualifierterm opt )=> ( ( ( qualifierterm -> qualifierterm ) ) ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )* ) | ( ( ( qualifierterm -> qualifierterm ) ) ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )* ) )
            int alt21=2;
            switch ( input.LA(1) ) {
            case QUAL:
                {
                int LA21_1 = input.LA(2);

                if ( (synpred32_KWQLBody()) ) {
                    alt21=1;
                }
                else if ( (true) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }
                }
                break;
            case RESQUAL:
                {
                int LA21_2 = input.LA(2);

                if ( (synpred32_KWQLBody()) ) {
                    alt21=1;
                }
                else if ( (true) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 2, input);

                    throw nvae;
                }
                }
                break;
            case LEFT_PARENTHESIS:
                {
                int LA21_3 = input.LA(2);

                if ( (synpred32_KWQLBody()) ) {
                    alt21=1;
                }
                else if ( (true) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 3, input);

                    throw nvae;
                }
                }
                break;
            case NEGATION:
                {
                int LA21_4 = input.LA(2);

                if ( (synpred32_KWQLBody()) ) {
                    alt21=1;
                }
                else if ( (true) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // KWQLBody.g:136:4: ( qualifierterm opt )=> ( ( ( qualifierterm -> qualifierterm ) ) ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )* )
                    {
                    // KWQLBody.g:136:25: ( ( ( qualifierterm -> qualifierterm ) ) ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )* )
                    // KWQLBody.g:136:26: ( ( qualifierterm -> qualifierterm ) ) ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )*
                    {
                    // KWQLBody.g:136:26: ( ( qualifierterm -> qualifierterm ) )
                    // KWQLBody.g:136:27: ( qualifierterm -> qualifierterm )
                    {
                    // KWQLBody.g:136:27: ( qualifierterm -> qualifierterm )
                    // KWQLBody.g:136:28: qualifierterm
                    {
                    pushFollow(FOLLOW_qualifierterm_in_complex_qualifierterm1091);
                    qualifierterm93=qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_qualifierterm.add(qualifierterm93.getTree());


                    // AST REWRITE
                    // elements: qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 136:41: -> qualifierterm
                    {
                        adaptor.addChild(root_0, stream_qualifierterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }

                    // KWQLBody.g:136:59: ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )*
                    loop19:
                    do {
                        int alt19=2;
                        alt19 = dfa19.predict(input);
                        switch (alt19) {
                    	case 1 :
                    	    // KWQLBody.g:136:60: opt r= qualifierterm
                    	    {
                    	    pushFollow(FOLLOW_opt_in_complex_qualifierterm1098);
                    	    opt94=opt();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_opt.add(opt94.getTree());
                    	    pushFollow(FOLLOW_qualifierterm_in_complex_qualifierterm1102);
                    	    r=qualifierterm();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_qualifierterm.add(r.getTree());


                    	    // AST REWRITE
                    	    // elements: opt, complex_qualifierterm, r
                    	    // token labels: 
                    	    // rule labels: retval, r
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 136:79: -> ^( AND $complex_qualifierterm ^( opt $r) )
                    	    {
                    	        // KWQLBody.g:136:82: ^( AND $complex_qualifierterm ^( opt $r) )
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        // KWQLBody.g:136:111: ^( opt $r)
                    	        {
                    	        CommonTree root_2 = (CommonTree)adaptor.nil();
                    	        root_2 = (CommonTree)adaptor.becomeRoot(stream_opt.nextNode(), root_2);

                    	        adaptor.addChild(root_2, stream_r.nextTree());

                    	        adaptor.addChild(root_1, root_2);
                    	        }

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:137:5: ( ( ( qualifierterm -> qualifierterm ) ) ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )* )
                    {
                    // KWQLBody.g:137:5: ( ( ( qualifierterm -> qualifierterm ) ) ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )* )
                    // KWQLBody.g:137:6: ( ( qualifierterm -> qualifierterm ) ) ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )*
                    {
                    // KWQLBody.g:137:6: ( ( qualifierterm -> qualifierterm ) )
                    // KWQLBody.g:137:7: ( qualifierterm -> qualifierterm )
                    {
                    // KWQLBody.g:137:7: ( qualifierterm -> qualifierterm )
                    // KWQLBody.g:137:8: qualifierterm
                    {
                    pushFollow(FOLLOW_qualifierterm_in_complex_qualifierterm1129);
                    qualifierterm95=qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_qualifierterm.add(qualifierterm95.getTree());


                    // AST REWRITE
                    // elements: qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 137:21: -> qualifierterm
                    {
                        adaptor.addChild(root_0, stream_qualifierterm.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }

                    // KWQLBody.g:137:39: ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )*
                    loop20:
                    do {
                        int alt20=2;
                        alt20 = dfa20.predict(input);
                        switch (alt20) {
                    	case 1 :
                    	    // KWQLBody.g:137:40: connectives r= qualifierterm
                    	    {
                    	    pushFollow(FOLLOW_connectives_in_complex_qualifierterm1136);
                    	    connectives96=connectives();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_connectives.add(connectives96.getTree());
                    	    pushFollow(FOLLOW_qualifierterm_in_complex_qualifierterm1140);
                    	    r=qualifierterm();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_qualifierterm.add(r.getTree());


                    	    // AST REWRITE
                    	    // elements: connectives, r, complex_qualifierterm
                    	    // token labels: 
                    	    // rule labels: retval, r
                    	    // token list labels: 
                    	    // rule list labels: 
                    	    // wildcard labels: 
                    	    if ( state.backtracking==0 ) {
                    	    retval.tree = root_0;
                    	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    	    RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);

                    	    root_0 = (CommonTree)adaptor.nil();
                    	    // 137:68: -> ^( connectives $complex_qualifierterm $r)
                    	    {
                    	        // KWQLBody.g:137:71: ^( connectives $complex_qualifierterm $r)
                    	        {
                    	        CommonTree root_1 = (CommonTree)adaptor.nil();
                    	        root_1 = (CommonTree)adaptor.becomeRoot(stream_connectives.nextNode(), root_1);

                    	        adaptor.addChild(root_1, stream_retval.nextTree());
                    	        adaptor.addChild(root_1, stream_r.nextTree());

                    	        adaptor.addChild(root_0, root_1);
                    	        }

                    	    }

                    	    retval.tree = root_0;}
                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);


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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "complex_qualifierterm"

    public static class opt_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "opt"
    // KWQLBody.g:139:1: opt : ( optional -> optional ) ;
    public final KWQLBody.opt_return opt() throws RecognitionException {
        KWQLBody.opt_return retval = new KWQLBody.opt_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.optional_return optional97 = null;


        RewriteRuleSubtreeStream stream_optional=new RewriteRuleSubtreeStream(adaptor,"rule optional");
        try {
            // KWQLBody.g:139:7: ( ( optional -> optional ) )
            // KWQLBody.g:140:4: ( optional -> optional )
            {
            // KWQLBody.g:140:4: ( optional -> optional )
            // KWQLBody.g:140:5: optional
            {
            pushFollow(FOLLOW_optional_in_opt1170);
            optional97=optional();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_optional.add(optional97.getTree());


            // AST REWRITE
            // elements: optional
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 140:13: -> optional
            {
                adaptor.addChild(root_0, stream_optional.nextTree());

            }

            retval.tree = root_0;}
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "opt"

    public static class qualifierterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "qualifierterm"
    // KWQLBody.g:142:1: qualifierterm : ( ( QUAL complex_valueterm ARROW VARIABLE ) -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) ) | ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS ) -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) ) | ( QUAL VARIABLE -> ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) ) ) | ( QUAL valueterm -> ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) ) ) | ( RESQUAL subquery_term -> ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) ) ) | ( LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( complex_qualifierterm ) ) | ( ( NEGATION qualifierterm ) -> ^( NEGATION qualifierterm ) ) | ( NEGATION LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( NEGATION complex_qualifierterm ) ) );
    public final KWQLBody.qualifierterm_return qualifierterm() throws RecognitionException {
        KWQLBody.qualifierterm_return retval = new KWQLBody.qualifierterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token QUAL98=null;
        Token ARROW100=null;
        Token VARIABLE101=null;
        Token QUAL102=null;
        Token LEFT_PARENTHESIS103=null;
        Token ARROW105=null;
        Token VARIABLE106=null;
        Token RIGHT_PARENTHESIS107=null;
        Token QUAL108=null;
        Token VARIABLE109=null;
        Token QUAL110=null;
        Token RESQUAL112=null;
        Token LEFT_PARENTHESIS114=null;
        Token RIGHT_PARENTHESIS116=null;
        Token NEGATION117=null;
        Token NEGATION119=null;
        Token LEFT_PARENTHESIS120=null;
        Token RIGHT_PARENTHESIS122=null;
        KWQLBody.complex_valueterm_return complex_valueterm99 = null;

        KWQLBody.complex_valueterm_return complex_valueterm104 = null;

        KWQLBody.valueterm_return valueterm111 = null;

        KWQLBody.subquery_term_return subquery_term113 = null;

        KWQLBody.complex_qualifierterm_return complex_qualifierterm115 = null;

        KWQLBody.qualifierterm_return qualifierterm118 = null;

        KWQLBody.complex_qualifierterm_return complex_qualifierterm121 = null;


        CommonTree QUAL98_tree=null;
        CommonTree ARROW100_tree=null;
        CommonTree VARIABLE101_tree=null;
        CommonTree QUAL102_tree=null;
        CommonTree LEFT_PARENTHESIS103_tree=null;
        CommonTree ARROW105_tree=null;
        CommonTree VARIABLE106_tree=null;
        CommonTree RIGHT_PARENTHESIS107_tree=null;
        CommonTree QUAL108_tree=null;
        CommonTree VARIABLE109_tree=null;
        CommonTree QUAL110_tree=null;
        CommonTree RESQUAL112_tree=null;
        CommonTree LEFT_PARENTHESIS114_tree=null;
        CommonTree RIGHT_PARENTHESIS116_tree=null;
        CommonTree NEGATION117_tree=null;
        CommonTree NEGATION119_tree=null;
        CommonTree LEFT_PARENTHESIS120_tree=null;
        CommonTree RIGHT_PARENTHESIS122_tree=null;
        RewriteRuleTokenStream stream_ARROW=new RewriteRuleTokenStream(adaptor,"token ARROW");
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_VARIABLE=new RewriteRuleTokenStream(adaptor,"token VARIABLE");
        RewriteRuleTokenStream stream_RESQUAL=new RewriteRuleTokenStream(adaptor,"token RESQUAL");
        RewriteRuleTokenStream stream_QUAL=new RewriteRuleTokenStream(adaptor,"token QUAL");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule valueterm");
        RewriteRuleSubtreeStream stream_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule qualifierterm");
        RewriteRuleSubtreeStream stream_subquery_term=new RewriteRuleSubtreeStream(adaptor,"rule subquery_term");
        RewriteRuleSubtreeStream stream_complex_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_valueterm");
        RewriteRuleSubtreeStream stream_complex_qualifierterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_qualifierterm");
        try {
            // KWQLBody.g:142:16: ( ( QUAL complex_valueterm ARROW VARIABLE ) -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) ) | ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS ) -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) ) | ( QUAL VARIABLE -> ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) ) ) | ( QUAL valueterm -> ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) ) ) | ( RESQUAL subquery_term -> ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) ) ) | ( LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( complex_qualifierterm ) ) | ( ( NEGATION qualifierterm ) -> ^( NEGATION qualifierterm ) ) | ( NEGATION LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( NEGATION complex_qualifierterm ) ) )
            int alt22=8;
            alt22 = dfa22.predict(input);
            switch (alt22) {
                case 1 :
                    // KWQLBody.g:143:4: ( QUAL complex_valueterm ARROW VARIABLE )
                    {
                    // KWQLBody.g:143:4: ( QUAL complex_valueterm ARROW VARIABLE )
                    // KWQLBody.g:143:5: QUAL complex_valueterm ARROW VARIABLE
                    {
                    QUAL98=(Token)match(input,QUAL,FOLLOW_QUAL_in_qualifierterm1187); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUAL.add(QUAL98);

                    pushFollow(FOLLOW_complex_valueterm_in_qualifierterm1189);
                    complex_valueterm99=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm99.getTree());
                    ARROW100=(Token)match(input,ARROW,FOLLOW_ARROW_in_qualifierterm1191); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW100);

                    VARIABLE101=(Token)match(input,VARIABLE,FOLLOW_VARIABLE_in_qualifierterm1193); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VARIABLE.add(VARIABLE101);


                    }



                    // AST REWRITE
                    // elements: QUAL, VARIABLE, complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 143:43: -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) )
                    {
                        // KWQLBody.g:143:45: ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        // KWQLBody.g:143:57: ^( LABEL QUAL )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_2);

                        adaptor.addChild(root_2, stream_QUAL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:143:71: ^( complex_valueterm )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_complex_valueterm.nextNode(), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:143:92: ^( VAR VARIABLE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VAR, "VAR"), root_2);

                        adaptor.addChild(root_2, stream_VARIABLE.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // KWQLBody.g:144:5: ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS )
                    {
                    // KWQLBody.g:144:5: ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS )
                    // KWQLBody.g:144:6: QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS
                    {
                    QUAL102=(Token)match(input,QUAL,FOLLOW_QUAL_in_qualifierterm1221); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUAL.add(QUAL102);

                    LEFT_PARENTHESIS103=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_qualifierterm1223); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS103);

                    pushFollow(FOLLOW_complex_valueterm_in_qualifierterm1225);
                    complex_valueterm104=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm104.getTree());
                    ARROW105=(Token)match(input,ARROW,FOLLOW_ARROW_in_qualifierterm1227); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ARROW.add(ARROW105);

                    VARIABLE106=(Token)match(input,VARIABLE,FOLLOW_VARIABLE_in_qualifierterm1229); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VARIABLE.add(VARIABLE106);

                    RIGHT_PARENTHESIS107=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_qualifierterm1231); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS107);


                    }



                    // AST REWRITE
                    // elements: VARIABLE, QUAL, complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 144:79: -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) )
                    {
                        // KWQLBody.g:144:81: ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        // KWQLBody.g:144:93: ^( LABEL QUAL )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_2);

                        adaptor.addChild(root_2, stream_QUAL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:144:107: ^( complex_valueterm )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_complex_valueterm.nextNode(), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:144:128: ^( VAR VARIABLE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VAR, "VAR"), root_2);

                        adaptor.addChild(root_2, stream_VARIABLE.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQLBody.g:145:5: ( QUAL VARIABLE -> ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) ) )
                    {
                    // KWQLBody.g:145:5: ( QUAL VARIABLE -> ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) ) )
                    // KWQLBody.g:145:6: QUAL VARIABLE
                    {
                    QUAL108=(Token)match(input,QUAL,FOLLOW_QUAL_in_qualifierterm1259); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUAL.add(QUAL108);

                    VARIABLE109=(Token)match(input,VARIABLE,FOLLOW_VARIABLE_in_qualifierterm1261); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_VARIABLE.add(VARIABLE109);



                    // AST REWRITE
                    // elements: QUAL, VARIABLE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 145:19: -> ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) )
                    {
                        // KWQLBody.g:145:21: ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        // KWQLBody.g:145:33: ^( LABEL QUAL )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_2);

                        adaptor.addChild(root_2, stream_QUAL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:145:47: ^( VAR VARIABLE )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VAR, "VAR"), root_2);

                        adaptor.addChild(root_2, stream_VARIABLE.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLBody.g:146:5: ( QUAL valueterm -> ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) ) )
                    {
                    // KWQLBody.g:146:5: ( QUAL valueterm -> ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) ) )
                    // KWQLBody.g:146:6: QUAL valueterm
                    {
                    QUAL110=(Token)match(input,QUAL,FOLLOW_QUAL_in_qualifierterm1285); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_QUAL.add(QUAL110);

                    pushFollow(FOLLOW_valueterm_in_qualifierterm1287);
                    valueterm111=valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_valueterm.add(valueterm111.getTree());


                    // AST REWRITE
                    // elements: QUAL, valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 146:20: -> ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) )
                    {
                        // KWQLBody.g:146:22: ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        // KWQLBody.g:146:34: ^( LABEL QUAL )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_2);

                        adaptor.addChild(root_2, stream_QUAL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:146:48: ^( valueterm )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_valueterm.nextNode(), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 5 :
                    // KWQLBody.g:147:5: ( RESQUAL subquery_term -> ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) ) )
                    {
                    // KWQLBody.g:147:5: ( RESQUAL subquery_term -> ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) ) )
                    // KWQLBody.g:147:6: RESQUAL subquery_term
                    {
                    RESQUAL112=(Token)match(input,RESQUAL,FOLLOW_RESQUAL_in_qualifierterm1309); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RESQUAL.add(RESQUAL112);

                    pushFollow(FOLLOW_subquery_term_in_qualifierterm1311);
                    subquery_term113=subquery_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_subquery_term.add(subquery_term113.getTree());


                    // AST REWRITE
                    // elements: RESQUAL, subquery_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 147:28: -> ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) )
                    {
                        // KWQLBody.g:147:30: ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUALIFIER, "QUALIFIER"), root_1);

                        // KWQLBody.g:147:42: ^( LABEL RESQUAL )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LABEL, "LABEL"), root_2);

                        adaptor.addChild(root_2, stream_RESQUAL.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQLBody.g:147:59: ^( subquery_term )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_subquery_term.nextNode(), root_2);

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 6 :
                    // KWQLBody.g:148:5: ( LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( complex_qualifierterm ) )
                    {
                    // KWQLBody.g:148:5: ( LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( complex_qualifierterm ) )
                    // KWQLBody.g:148:6: LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS
                    {
                    LEFT_PARENTHESIS114=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_qualifierterm1334); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS114);

                    pushFollow(FOLLOW_complex_qualifierterm_in_qualifierterm1336);
                    complex_qualifierterm115=complex_qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_qualifierterm.add(complex_qualifierterm115.getTree());
                    RIGHT_PARENTHESIS116=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_qualifierterm1338); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS116);



                    // AST REWRITE
                    // elements: complex_qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 148:63: -> ^( complex_qualifierterm )
                    {
                        // KWQLBody.g:148:65: ^( complex_qualifierterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_complex_qualifierterm.nextNode(), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 7 :
                    // KWQLBody.g:149:5: ( ( NEGATION qualifierterm ) -> ^( NEGATION qualifierterm ) )
                    {
                    // KWQLBody.g:149:5: ( ( NEGATION qualifierterm ) -> ^( NEGATION qualifierterm ) )
                    // KWQLBody.g:149:6: ( NEGATION qualifierterm )
                    {
                    // KWQLBody.g:149:6: ( NEGATION qualifierterm )
                    // KWQLBody.g:149:7: NEGATION qualifierterm
                    {
                    NEGATION117=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_qualifierterm1352); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION117);

                    pushFollow(FOLLOW_qualifierterm_in_qualifierterm1354);
                    qualifierterm118=qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_qualifierterm.add(qualifierterm118.getTree());

                    }



                    // AST REWRITE
                    // elements: qualifierterm, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 149:30: -> ^( NEGATION qualifierterm )
                    {
                        // KWQLBody.g:149:32: ^( NEGATION qualifierterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_qualifierterm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 8 :
                    // KWQLBody.g:150:5: ( NEGATION LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( NEGATION complex_qualifierterm ) )
                    {
                    // KWQLBody.g:150:5: ( NEGATION LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( NEGATION complex_qualifierterm ) )
                    // KWQLBody.g:150:6: NEGATION LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS
                    {
                    NEGATION119=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_qualifierterm1369); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION119);

                    LEFT_PARENTHESIS120=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_qualifierterm1371); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS120);

                    pushFollow(FOLLOW_complex_qualifierterm_in_qualifierterm1373);
                    complex_qualifierterm121=complex_qualifierterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_qualifierterm.add(complex_qualifierterm121.getTree());
                    RIGHT_PARENTHESIS122=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_qualifierterm1375); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS122);



                    // AST REWRITE
                    // elements: NEGATION, complex_qualifierterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 150:72: -> ^( NEGATION complex_qualifierterm )
                    {
                        // KWQLBody.g:150:74: ^( NEGATION complex_qualifierterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_complex_qualifierterm.nextTree());

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "qualifierterm"

    public static class complex_valueterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "complex_valueterm"
    // KWQLBody.g:152:1: complex_valueterm : ( valueterm -> valueterm ) ( connectives v= valueterm -> ^( connectives $complex_valueterm $v) )* ;
    public final KWQLBody.complex_valueterm_return complex_valueterm() throws RecognitionException {
        KWQLBody.complex_valueterm_return retval = new KWQLBody.complex_valueterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.valueterm_return v = null;

        KWQLBody.valueterm_return valueterm123 = null;

        KWQLBody.connectives_return connectives124 = null;


        RewriteRuleSubtreeStream stream_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule valueterm");
        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        try {
            // KWQLBody.g:152:19: ( ( valueterm -> valueterm ) ( connectives v= valueterm -> ^( connectives $complex_valueterm $v) )* )
            // KWQLBody.g:153:4: ( valueterm -> valueterm ) ( connectives v= valueterm -> ^( connectives $complex_valueterm $v) )*
            {
            // KWQLBody.g:153:4: ( valueterm -> valueterm )
            // KWQLBody.g:153:5: valueterm
            {
            pushFollow(FOLLOW_valueterm_in_complex_valueterm1396);
            valueterm123=valueterm();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_valueterm.add(valueterm123.getTree());


            // AST REWRITE
            // elements: valueterm
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 153:14: -> valueterm
            {
                adaptor.addChild(root_0, stream_valueterm.nextTree());

            }

            retval.tree = root_0;}
            }

            // KWQLBody.g:153:27: ( connectives v= valueterm -> ^( connectives $complex_valueterm $v) )*
            loop23:
            do {
                int alt23=2;
                alt23 = dfa23.predict(input);
                switch (alt23) {
            	case 1 :
            	    // KWQLBody.g:153:28: connectives v= valueterm
            	    {
            	    pushFollow(FOLLOW_connectives_in_complex_valueterm1402);
            	    connectives124=connectives();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_connectives.add(connectives124.getTree());
            	    pushFollow(FOLLOW_valueterm_in_complex_valueterm1406);
            	    v=valueterm();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_valueterm.add(v.getTree());


            	    // AST REWRITE
            	    // elements: complex_valueterm, v, connectives
            	    // token labels: 
            	    // rule labels: v, retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_v=new RewriteRuleSubtreeStream(adaptor,"rule v",v!=null?v.tree:null);
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (CommonTree)adaptor.nil();
            	    // 153:52: -> ^( connectives $complex_valueterm $v)
            	    {
            	        // KWQLBody.g:153:55: ^( connectives $complex_valueterm $v)
            	        {
            	        CommonTree root_1 = (CommonTree)adaptor.nil();
            	        root_1 = (CommonTree)adaptor.becomeRoot(stream_connectives.nextNode(), root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        adaptor.addChild(root_1, stream_v.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;}
            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "complex_valueterm"

    public static class connectives_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "connectives"
    // KWQLBody.g:155:1: connectives : ( ( connective -> connective ) | -> AND );
    public final KWQLBody.connectives_return connectives() throws RecognitionException {
        KWQLBody.connectives_return retval = new KWQLBody.connectives_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQLBody.connective_return connective125 = null;


        RewriteRuleSubtreeStream stream_connective=new RewriteRuleSubtreeStream(adaptor,"rule connective");
        try {
            // KWQLBody.g:155:14: ( ( connective -> connective ) | -> AND )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( ((LA24_0>=CONJUNCTION && LA24_0<=DISJUNCTION)) ) {
                alt24=1;
            }
            else if ( ((LA24_0>=LINK && LA24_0<=TAG)||LA24_0==NEGATION||LA24_0==URI||(LA24_0>=RESQUAL && LA24_0<=QUAL)||LA24_0==INT||LA24_0==DA||LA24_0==KW||LA24_0==LEFT_PARENTHESIS) ) {
                alt24=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // KWQLBody.g:156:4: ( connective -> connective )
                    {
                    // KWQLBody.g:156:4: ( connective -> connective )
                    // KWQLBody.g:156:5: connective
                    {
                    pushFollow(FOLLOW_connective_in_connectives1439);
                    connective125=connective();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_connective.add(connective125.getTree());


                    // AST REWRITE
                    // elements: connective
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 156:15: -> connective
                    {
                        adaptor.addChild(root_0, stream_connective.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:156:29: 
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
                    // 156:29: -> AND
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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "connectives"

    public static class valueterm_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "valueterm"
    // KWQLBody.g:158:1: valueterm : ( ( NEGATION atomar_value -> ^( NEGATION atomar_value ) ) | ( atomar_value -> atomar_value ) | ( NEGATION LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> ^( NEGATION complex_valueterm ) ) | ( LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> complex_valueterm ) );
    public final KWQLBody.valueterm_return valueterm() throws RecognitionException {
        KWQLBody.valueterm_return retval = new KWQLBody.valueterm_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION126=null;
        Token NEGATION129=null;
        Token LEFT_PARENTHESIS130=null;
        Token RIGHT_PARENTHESIS132=null;
        Token LEFT_PARENTHESIS133=null;
        Token RIGHT_PARENTHESIS135=null;
        KWQLBody.atomar_value_return atomar_value127 = null;

        KWQLBody.atomar_value_return atomar_value128 = null;

        KWQLBody.complex_valueterm_return complex_valueterm131 = null;

        KWQLBody.complex_valueterm_return complex_valueterm134 = null;


        CommonTree NEGATION126_tree=null;
        CommonTree NEGATION129_tree=null;
        CommonTree LEFT_PARENTHESIS130_tree=null;
        CommonTree RIGHT_PARENTHESIS132_tree=null;
        CommonTree LEFT_PARENTHESIS133_tree=null;
        CommonTree RIGHT_PARENTHESIS135_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_atomar_value=new RewriteRuleSubtreeStream(adaptor,"rule atomar_value");
        RewriteRuleSubtreeStream stream_complex_valueterm=new RewriteRuleSubtreeStream(adaptor,"rule complex_valueterm");
        try {
            // KWQLBody.g:158:12: ( ( NEGATION atomar_value -> ^( NEGATION atomar_value ) ) | ( atomar_value -> atomar_value ) | ( NEGATION LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> ^( NEGATION complex_valueterm ) ) | ( LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> complex_valueterm ) )
            int alt25=4;
            switch ( input.LA(1) ) {
            case NEGATION:
                {
                int LA25_1 = input.LA(2);

                if ( (LA25_1==LEFT_PARENTHESIS) ) {
                    alt25=3;
                }
                else if ( (LA25_1==URI||LA25_1==INT||LA25_1==DA||LA25_1==KW) ) {
                    alt25=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 25, 1, input);

                    throw nvae;
                }
                }
                break;
            case URI:
            case INT:
            case DA:
            case KW:
                {
                alt25=2;
                }
                break;
            case LEFT_PARENTHESIS:
                {
                alt25=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // KWQLBody.g:159:4: ( NEGATION atomar_value -> ^( NEGATION atomar_value ) )
                    {
                    // KWQLBody.g:159:4: ( NEGATION atomar_value -> ^( NEGATION atomar_value ) )
                    // KWQLBody.g:159:5: NEGATION atomar_value
                    {
                    NEGATION126=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_valueterm1463); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION126);

                    pushFollow(FOLLOW_atomar_value_in_valueterm1465);
                    atomar_value127=atomar_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_atomar_value.add(atomar_value127.getTree());


                    // AST REWRITE
                    // elements: atomar_value, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 159:27: -> ^( NEGATION atomar_value )
                    {
                        // KWQLBody.g:159:29: ^( NEGATION atomar_value )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_atomar_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 2 :
                    // KWQLBody.g:160:5: ( atomar_value -> atomar_value )
                    {
                    // KWQLBody.g:160:5: ( atomar_value -> atomar_value )
                    // KWQLBody.g:160:6: atomar_value
                    {
                    pushFollow(FOLLOW_atomar_value_in_valueterm1480);
                    atomar_value128=atomar_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_atomar_value.add(atomar_value128.getTree());


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
                    // 160:19: -> atomar_value
                    {
                        adaptor.addChild(root_0, stream_atomar_value.nextTree());

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 3 :
                    // KWQLBody.g:161:5: ( NEGATION LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> ^( NEGATION complex_valueterm ) )
                    {
                    // KWQLBody.g:161:5: ( NEGATION LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> ^( NEGATION complex_valueterm ) )
                    // KWQLBody.g:161:6: NEGATION LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS
                    {
                    NEGATION129=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_valueterm1492); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION129);

                    LEFT_PARENTHESIS130=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_valueterm1494); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS130);

                    pushFollow(FOLLOW_complex_valueterm_in_valueterm1496);
                    complex_valueterm131=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm131.getTree());
                    RIGHT_PARENTHESIS132=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_valueterm1498); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS132);



                    // AST REWRITE
                    // elements: NEGATION, complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 161:68: -> ^( NEGATION complex_valueterm )
                    {
                        // KWQLBody.g:161:70: ^( NEGATION complex_valueterm )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_complex_valueterm.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }


                    }
                    break;
                case 4 :
                    // KWQLBody.g:162:5: ( LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> complex_valueterm )
                    {
                    // KWQLBody.g:162:5: ( LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS -> complex_valueterm )
                    // KWQLBody.g:162:6: LEFT_PARENTHESIS complex_valueterm RIGHT_PARENTHESIS
                    {
                    LEFT_PARENTHESIS133=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_valueterm1513); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS133);

                    pushFollow(FOLLOW_complex_valueterm_in_valueterm1515);
                    complex_valueterm134=complex_valueterm();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_valueterm.add(complex_valueterm134.getTree());
                    RIGHT_PARENTHESIS135=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_valueterm1517); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS135);



                    // AST REWRITE
                    // elements: complex_valueterm
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 162:59: -> complex_valueterm
                    {
                        adaptor.addChild(root_0, stream_complex_valueterm.nextTree());

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "valueterm"

    public static class atomar_value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atomar_value"
    // KWQLBody.g:164:1: atomar_value : ( ( INT -> ^( INTEGER INT ) ) | ( DA -> ^( DATE DA ) ) | ( KW -> ^( STRING KW ) ) | ( URI -> ^( UR URI ) ) );
    public final KWQLBody.atomar_value_return atomar_value() throws RecognitionException {
        KWQLBody.atomar_value_return retval = new KWQLBody.atomar_value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT136=null;
        Token DA137=null;
        Token KW138=null;
        Token URI139=null;

        CommonTree INT136_tree=null;
        CommonTree DA137_tree=null;
        CommonTree KW138_tree=null;
        CommonTree URI139_tree=null;
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_KW=new RewriteRuleTokenStream(adaptor,"token KW");
        RewriteRuleTokenStream stream_DA=new RewriteRuleTokenStream(adaptor,"token DA");
        RewriteRuleTokenStream stream_URI=new RewriteRuleTokenStream(adaptor,"token URI");

        try {
            // KWQLBody.g:164:15: ( ( INT -> ^( INTEGER INT ) ) | ( DA -> ^( DATE DA ) ) | ( KW -> ^( STRING KW ) ) | ( URI -> ^( UR URI ) ) )
            int alt26=4;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt26=1;
                }
                break;
            case DA:
                {
                alt26=2;
                }
                break;
            case KW:
                {
                alt26=3;
                }
                break;
            case URI:
                {
                alt26=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // KWQLBody.g:165:4: ( INT -> ^( INTEGER INT ) )
                    {
                    // KWQLBody.g:165:4: ( INT -> ^( INTEGER INT ) )
                    // KWQLBody.g:165:5: INT
                    {
                    INT136=(Token)match(input,INT,FOLLOW_INT_in_atomar_value1536); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_INT.add(INT136);



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
                    // 165:8: -> ^( INTEGER INT )
                    {
                        // KWQLBody.g:165:10: ^( INTEGER INT )
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
                    // KWQLBody.g:165:26: ( DA -> ^( DATE DA ) )
                    {
                    // KWQLBody.g:165:26: ( DA -> ^( DATE DA ) )
                    // KWQLBody.g:165:27: DA
                    {
                    DA137=(Token)match(input,DA,FOLLOW_DA_in_atomar_value1546); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_DA.add(DA137);



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
                    // 165:29: -> ^( DATE DA )
                    {
                        // KWQLBody.g:165:31: ^( DATE DA )
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
                    // KWQLBody.g:165:43: ( KW -> ^( STRING KW ) )
                    {
                    // KWQLBody.g:165:43: ( KW -> ^( STRING KW ) )
                    // KWQLBody.g:165:44: KW
                    {
                    KW138=(Token)match(input,KW,FOLLOW_KW_in_atomar_value1556); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_KW.add(KW138);



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
                    // 165:46: -> ^( STRING KW )
                    {
                        // KWQLBody.g:165:48: ^( STRING KW )
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
                    // KWQLBody.g:165:62: ( URI -> ^( UR URI ) )
                    {
                    // KWQLBody.g:165:62: ( URI -> ^( UR URI ) )
                    // KWQLBody.g:165:63: URI
                    {
                    URI139=(Token)match(input,URI,FOLLOW_URI_in_atomar_value1566); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_URI.add(URI139);



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
                    // 165:66: -> ^( UR URI )
                    {
                        // KWQLBody.g:165:68: ^( UR URI )
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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "atomar_value"

    public static class res_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "res"
    // KWQLBody.g:167:1: res : ( CI | LINK | FRAG | TAG ) ;
    public final KWQLBody.res_return res() throws RecognitionException {
        KWQLBody.res_return retval = new KWQLBody.res_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set140=null;

        CommonTree set140_tree=null;

        try {
            // KWQLBody.g:167:7: ( ( CI | LINK | FRAG | TAG ) )
            // KWQLBody.g:168:4: ( CI | LINK | FRAG | TAG )
            {
            root_0 = (CommonTree)adaptor.nil();

            set140=(Token)input.LT(1);
            if ( (input.LA(1)>=CI && input.LA(1)<=TAG) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set140));
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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "res"

    public static class non_ci_resource_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "non_ci_resource"
    // KWQLBody.g:170:1: non_ci_resource : ( LINK | FRAG | TAG ) ;
    public final KWQLBody.non_ci_resource_return non_ci_resource() throws RecognitionException {
        KWQLBody.non_ci_resource_return retval = new KWQLBody.non_ci_resource_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set141=null;

        CommonTree set141_tree=null;

        try {
            // KWQLBody.g:170:18: ( ( LINK | FRAG | TAG ) )
            // KWQLBody.g:171:4: ( LINK | FRAG | TAG )
            {
            root_0 = (CommonTree)adaptor.nil();

            set141=(Token)input.LT(1);
            if ( (input.LA(1)>=LINK && input.LA(1)<=TAG) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set141));
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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "non_ci_resource"

    public static class ci_resource_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ci_resource"
    // KWQLBody.g:173:1: ci_resource : CI ;
    public final KWQLBody.ci_resource_return ci_resource() throws RecognitionException {
        KWQLBody.ci_resource_return retval = new KWQLBody.ci_resource_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CI142=null;

        CommonTree CI142_tree=null;

        try {
            // KWQLBody.g:173:14: ( CI )
            // KWQLBody.g:174:4: CI
            {
            root_0 = (CommonTree)adaptor.nil();

            CI142=(Token)match(input,CI,FOLLOW_CI_in_ci_resource1633); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            CI142_tree = (CommonTree)adaptor.create(CI142);
            adaptor.addChild(root_0, CI142_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "ci_resource"

    public static class connective_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "connective"
    // KWQLBody.g:176:1: connective : ( DISJUNCTION | CONJUNCTION ) ;
    public final KWQLBody.connective_return connective() throws RecognitionException {
        KWQLBody.connective_return retval = new KWQLBody.connective_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set143=null;

        CommonTree set143_tree=null;

        try {
            // KWQLBody.g:176:13: ( ( DISJUNCTION | CONJUNCTION ) )
            // KWQLBody.g:177:4: ( DISJUNCTION | CONJUNCTION )
            {
            root_0 = (CommonTree)adaptor.nil();

            set143=(Token)input.LT(1);
            if ( (input.LA(1)>=CONJUNCTION && input.LA(1)<=DISJUNCTION) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set143));
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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "connective"

    public static class optional_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "optional"
    // KWQLBody.g:179:1: optional : OPTIONAL LEFT_PARENTHESIS KW RIGHT_PARENTHESIS -> ^( OPTIONAL KW ) ;
    public final KWQLBody.optional_return optional() throws RecognitionException {
        KWQLBody.optional_return retval = new KWQLBody.optional_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token OPTIONAL144=null;
        Token LEFT_PARENTHESIS145=null;
        Token KW146=null;
        Token RIGHT_PARENTHESIS147=null;

        CommonTree OPTIONAL144_tree=null;
        CommonTree LEFT_PARENTHESIS145_tree=null;
        CommonTree KW146_tree=null;
        CommonTree RIGHT_PARENTHESIS147_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_KW=new RewriteRuleTokenStream(adaptor,"token KW");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleTokenStream stream_OPTIONAL=new RewriteRuleTokenStream(adaptor,"token OPTIONAL");

        try {
            // KWQLBody.g:179:11: ( OPTIONAL LEFT_PARENTHESIS KW RIGHT_PARENTHESIS -> ^( OPTIONAL KW ) )
            // KWQLBody.g:180:4: OPTIONAL LEFT_PARENTHESIS KW RIGHT_PARENTHESIS
            {
            OPTIONAL144=(Token)match(input,OPTIONAL,FOLLOW_OPTIONAL_in_optional1668); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_OPTIONAL.add(OPTIONAL144);

            LEFT_PARENTHESIS145=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_optional1670); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS145);

            KW146=(Token)match(input,KW,FOLLOW_KW_in_optional1672); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_KW.add(KW146);

            RIGHT_PARENTHESIS147=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_optional1674); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS147);



            // AST REWRITE
            // elements: KW, OPTIONAL
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 180:51: -> ^( OPTIONAL KW )
            {
                // KWQLBody.g:180:53: ^( OPTIONAL KW )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(stream_OPTIONAL.nextNode(), root_1);

                adaptor.addChild(root_1, stream_KW.nextNode());

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
         
        catch (RecognitionException e) { 
        throw e; 
        } 

        finally {
        }
        return retval;
    }
    // $ANTLR end "optional"

    // $ANTLR start synpred1_KWQLBody
    public final void synpred1_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:68:4: ( ( unbound_complex_contentterm ) )
        // KWQLBody.g:68:4: ( unbound_complex_contentterm )
        {
        // KWQLBody.g:68:4: ( unbound_complex_contentterm )
        // KWQLBody.g:68:5: unbound_complex_contentterm
        {
        pushFollow(FOLLOW_unbound_complex_contentterm_in_synpred1_KWQLBody151);
        unbound_complex_contentterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred1_KWQLBody

    // $ANTLR start synpred3_KWQLBody
    public final void synpred3_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:75:4: ( ( expanded_resource ) )
        // KWQLBody.g:75:4: ( expanded_resource )
        {
        // KWQLBody.g:75:4: ( expanded_resource )
        // KWQLBody.g:75:5: expanded_resource
        {
        pushFollow(FOLLOW_expanded_resource_in_synpred3_KWQLBody227);
        expanded_resource();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred3_KWQLBody

    // $ANTLR start synpred8_KWQLBody
    public final void synpred8_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:89:4: ( ( non_ci_resourceterm ) )
        // KWQLBody.g:89:4: ( non_ci_resourceterm )
        {
        // KWQLBody.g:89:4: ( non_ci_resourceterm )
        // KWQLBody.g:89:5: non_ci_resourceterm
        {
        pushFollow(FOLLOW_non_ci_resourceterm_in_synpred8_KWQLBody477);
        non_ci_resourceterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred8_KWQLBody

    // $ANTLR start synpred9_KWQLBody
    public final void synpred9_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:90:5: ( ( ci_resourceterm ) )
        // KWQLBody.g:90:5: ( ci_resourceterm )
        {
        // KWQLBody.g:90:5: ( ci_resourceterm )
        // KWQLBody.g:90:6: ci_resourceterm
        {
        pushFollow(FOLLOW_ci_resourceterm_in_synpred9_KWQLBody495);
        ci_resourceterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred9_KWQLBody

    // $ANTLR start synpred10_KWQLBody
    public final void synpred10_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:91:5: ( ( complex_qualifierterm ) )
        // KWQLBody.g:91:5: ( complex_qualifierterm )
        {
        // KWQLBody.g:91:5: ( complex_qualifierterm )
        // KWQLBody.g:91:6: complex_qualifierterm
        {
        pushFollow(FOLLOW_complex_qualifierterm_in_synpred10_KWQLBody505);
        complex_qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred10_KWQLBody

    // $ANTLR start synpred11_KWQLBody
    public final void synpred11_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:95:4: ( NEGATION LEFT_PARENTHESIS unbound_complex_contentterm_helper RIGHT_PARENTHESIS )
        // KWQLBody.g:95:4: NEGATION LEFT_PARENTHESIS unbound_complex_contentterm_helper RIGHT_PARENTHESIS
        {
        match(input,NEGATION,FOLLOW_NEGATION_in_synpred11_KWQLBody554); if (state.failed) return ;
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred11_KWQLBody556); if (state.failed) return ;
        pushFollow(FOLLOW_unbound_complex_contentterm_helper_in_synpred11_KWQLBody558);
        unbound_complex_contentterm_helper();

        state._fsp--;
        if (state.failed) return ;
        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred11_KWQLBody560); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred11_KWQLBody

    // $ANTLR start synpred12_KWQLBody
    public final void synpred12_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:99:4: ( unbound_contentterm opt )
        // KWQLBody.g:99:5: unbound_contentterm opt
        {
        pushFollow(FOLLOW_unbound_contentterm_in_synpred12_KWQLBody588);
        unbound_contentterm();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_opt_in_synpred12_KWQLBody590);
        opt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_KWQLBody

    // $ANTLR start synpred15_KWQLBody
    public final void synpred15_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:103:4: ( unbound_contentterm_helper )
        // KWQLBody.g:103:4: unbound_contentterm_helper
        {
        pushFollow(FOLLOW_unbound_contentterm_helper_in_synpred15_KWQLBody669);
        unbound_contentterm_helper();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred15_KWQLBody

    // $ANTLR start synpred16_KWQLBody
    public final void synpred16_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:104:5: ( NEGATION LEFT_PARENTHESIS unbound_contentterm_helper RIGHT_PARENTHESIS )
        // KWQLBody.g:104:5: NEGATION LEFT_PARENTHESIS unbound_contentterm_helper RIGHT_PARENTHESIS
        {
        match(input,NEGATION,FOLLOW_NEGATION_in_synpred16_KWQLBody675); if (state.failed) return ;
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred16_KWQLBody677); if (state.failed) return ;
        pushFollow(FOLLOW_unbound_contentterm_helper_in_synpred16_KWQLBody679);
        unbound_contentterm_helper();

        state._fsp--;
        if (state.failed) return ;
        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred16_KWQLBody681); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred16_KWQLBody

    // $ANTLR start synpred17_KWQLBody
    public final void synpred17_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:108:5: ( ( complex_qualifierterm ) )
        // KWQLBody.g:108:5: ( complex_qualifierterm )
        {
        // KWQLBody.g:108:5: ( complex_qualifierterm )
        // KWQLBody.g:108:6: complex_qualifierterm
        {
        pushFollow(FOLLOW_complex_qualifierterm_in_synpred17_KWQLBody722);
        complex_qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred17_KWQLBody

    // $ANTLR start synpred18_KWQLBody
    public final void synpred18_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:109:5: ( ( complex_valueterm ) )
        // KWQLBody.g:109:5: ( complex_valueterm )
        {
        // KWQLBody.g:109:5: ( complex_valueterm )
        // KWQLBody.g:109:6: complex_valueterm
        {
        pushFollow(FOLLOW_complex_valueterm_in_synpred18_KWQLBody732);
        complex_valueterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred18_KWQLBody

    // $ANTLR start synpred19_KWQLBody
    public final void synpred19_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:110:5: ( ( non_ci_resourceterm ) )
        // KWQLBody.g:110:5: ( non_ci_resourceterm )
        {
        // KWQLBody.g:110:5: ( non_ci_resourceterm )
        // KWQLBody.g:110:6: non_ci_resourceterm
        {
        pushFollow(FOLLOW_non_ci_resourceterm_in_synpred19_KWQLBody750);
        non_ci_resourceterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred19_KWQLBody

    // $ANTLR start synpred20_KWQLBody
    public final void synpred20_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:114:4: ( NEGATION LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS )
        // KWQLBody.g:114:4: NEGATION LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS
        {
        match(input,NEGATION,FOLLOW_NEGATION_in_synpred20_KWQLBody783); if (state.failed) return ;
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred20_KWQLBody785); if (state.failed) return ;
        pushFollow(FOLLOW_complex_contentterm_helper_in_synpred20_KWQLBody787);
        complex_contentterm_helper();

        state._fsp--;
        if (state.failed) return ;
        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred20_KWQLBody789); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred20_KWQLBody

    // $ANTLR start synpred21_KWQLBody
    public final void synpred21_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:118:4: ( contentterm opt )
        // KWQLBody.g:118:5: contentterm opt
        {
        pushFollow(FOLLOW_contentterm_in_synpred21_KWQLBody817);
        contentterm();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_opt_in_synpred21_KWQLBody819);
        opt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred21_KWQLBody

    // $ANTLR start synpred24_KWQLBody
    public final void synpred24_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:122:5: ( ( complex_qualifierterm ) )
        // KWQLBody.g:122:5: ( complex_qualifierterm )
        {
        // KWQLBody.g:122:5: ( complex_qualifierterm )
        // KWQLBody.g:122:6: complex_qualifierterm
        {
        pushFollow(FOLLOW_complex_qualifierterm_in_synpred24_KWQLBody902);
        complex_qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred24_KWQLBody

    // $ANTLR start synpred25_KWQLBody
    public final void synpred25_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:123:5: ( ( complex_valueterm ) )
        // KWQLBody.g:123:5: ( complex_valueterm )
        {
        // KWQLBody.g:123:5: ( complex_valueterm )
        // KWQLBody.g:123:6: complex_valueterm
        {
        pushFollow(FOLLOW_complex_valueterm_in_synpred25_KWQLBody912);
        complex_valueterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred25_KWQLBody

    // $ANTLR start synpred26_KWQLBody
    public final void synpred26_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:124:5: ( ( non_ci_resourceterm ) )
        // KWQLBody.g:124:5: ( non_ci_resourceterm )
        {
        // KWQLBody.g:124:5: ( non_ci_resourceterm )
        // KWQLBody.g:124:6: non_ci_resourceterm
        {
        pushFollow(FOLLOW_non_ci_resourceterm_in_synpred26_KWQLBody930);
        non_ci_resourceterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred26_KWQLBody

    // $ANTLR start synpred27_KWQLBody
    public final void synpred27_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:125:5: ( ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper ) RIGHT_PARENTHESIS ) )
        // KWQLBody.g:125:5: ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper ) RIGHT_PARENTHESIS )
        {
        // KWQLBody.g:125:5: ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper ) RIGHT_PARENTHESIS )
        // KWQLBody.g:125:6: NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper ) RIGHT_PARENTHESIS
        {
        match(input,NEGATION,FOLLOW_NEGATION_in_synpred27_KWQLBody940); if (state.failed) return ;
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred27_KWQLBody942); if (state.failed) return ;
        // KWQLBody.g:125:32: ( complex_contentterm_helper )
        // KWQLBody.g:125:33: complex_contentterm_helper
        {
        pushFollow(FOLLOW_complex_contentterm_helper_in_synpred27_KWQLBody945);
        complex_contentterm_helper();

        state._fsp--;
        if (state.failed) return ;

        }

        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred27_KWQLBody950); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred27_KWQLBody

    // $ANTLR start synpred28_KWQLBody
    public final void synpred28_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:129:4: ( ( non_ci_resourceterm ) )
        // KWQLBody.g:129:4: ( non_ci_resourceterm )
        {
        // KWQLBody.g:129:4: ( non_ci_resourceterm )
        // KWQLBody.g:129:5: non_ci_resourceterm
        {
        pushFollow(FOLLOW_non_ci_resourceterm_in_synpred28_KWQLBody994);
        non_ci_resourceterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred28_KWQLBody

    // $ANTLR start synpred29_KWQLBody
    public final void synpred29_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:130:5: ( ( ci_resourceterm ) )
        // KWQLBody.g:130:5: ( ci_resourceterm )
        {
        // KWQLBody.g:130:5: ( ci_resourceterm )
        // KWQLBody.g:130:6: ci_resourceterm
        {
        pushFollow(FOLLOW_ci_resourceterm_in_synpred29_KWQLBody1004);
        ci_resourceterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred29_KWQLBody

    // $ANTLR start synpred30_KWQLBody
    public final void synpred30_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:131:5: ( ( valueterm ) )
        // KWQLBody.g:131:5: ( valueterm )
        {
        // KWQLBody.g:131:5: ( valueterm )
        // KWQLBody.g:131:6: valueterm
        {
        pushFollow(FOLLOW_valueterm_in_synpred30_KWQLBody1014);
        valueterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred30_KWQLBody

    // $ANTLR start synpred31_KWQLBody
    public final void synpred31_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:132:5: ( ( qualifierterm ) )
        // KWQLBody.g:132:5: ( qualifierterm )
        {
        // KWQLBody.g:132:5: ( qualifierterm )
        // KWQLBody.g:132:6: qualifierterm
        {
        pushFollow(FOLLOW_qualifierterm_in_synpred31_KWQLBody1042);
        qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred31_KWQLBody

    // $ANTLR start synpred32_KWQLBody
    public final void synpred32_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:136:4: ( qualifierterm opt )
        // KWQLBody.g:136:5: qualifierterm opt
        {
        pushFollow(FOLLOW_qualifierterm_in_synpred32_KWQLBody1083);
        qualifierterm();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_opt_in_synpred32_KWQLBody1085);
        opt();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred32_KWQLBody

    // $ANTLR start synpred33_KWQLBody
    public final void synpred33_KWQLBody_fragment() throws RecognitionException {   
        KWQLBody.qualifierterm_return r = null;


        // KWQLBody.g:136:60: ( opt r= qualifierterm )
        // KWQLBody.g:136:60: opt r= qualifierterm
        {
        pushFollow(FOLLOW_opt_in_synpred33_KWQLBody1098);
        opt();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_qualifierterm_in_synpred33_KWQLBody1102);
        r=qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred33_KWQLBody

    // $ANTLR start synpred34_KWQLBody
    public final void synpred34_KWQLBody_fragment() throws RecognitionException {   
        KWQLBody.qualifierterm_return r = null;


        // KWQLBody.g:137:40: ( connectives r= qualifierterm )
        // KWQLBody.g:137:40: connectives r= qualifierterm
        {
        pushFollow(FOLLOW_connectives_in_synpred34_KWQLBody1136);
        connectives();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_qualifierterm_in_synpred34_KWQLBody1140);
        r=qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred34_KWQLBody

    // $ANTLR start synpred35_KWQLBody
    public final void synpred35_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:143:4: ( ( QUAL complex_valueterm ARROW VARIABLE ) )
        // KWQLBody.g:143:4: ( QUAL complex_valueterm ARROW VARIABLE )
        {
        // KWQLBody.g:143:4: ( QUAL complex_valueterm ARROW VARIABLE )
        // KWQLBody.g:143:5: QUAL complex_valueterm ARROW VARIABLE
        {
        match(input,QUAL,FOLLOW_QUAL_in_synpred35_KWQLBody1187); if (state.failed) return ;
        pushFollow(FOLLOW_complex_valueterm_in_synpred35_KWQLBody1189);
        complex_valueterm();

        state._fsp--;
        if (state.failed) return ;
        match(input,ARROW,FOLLOW_ARROW_in_synpred35_KWQLBody1191); if (state.failed) return ;
        match(input,VARIABLE,FOLLOW_VARIABLE_in_synpred35_KWQLBody1193); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred35_KWQLBody

    // $ANTLR start synpred36_KWQLBody
    public final void synpred36_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:144:5: ( ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS ) )
        // KWQLBody.g:144:5: ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS )
        {
        // KWQLBody.g:144:5: ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS )
        // KWQLBody.g:144:6: QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS
        {
        match(input,QUAL,FOLLOW_QUAL_in_synpred36_KWQLBody1221); if (state.failed) return ;
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred36_KWQLBody1223); if (state.failed) return ;
        pushFollow(FOLLOW_complex_valueterm_in_synpred36_KWQLBody1225);
        complex_valueterm();

        state._fsp--;
        if (state.failed) return ;
        match(input,ARROW,FOLLOW_ARROW_in_synpred36_KWQLBody1227); if (state.failed) return ;
        match(input,VARIABLE,FOLLOW_VARIABLE_in_synpred36_KWQLBody1229); if (state.failed) return ;
        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred36_KWQLBody1231); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred36_KWQLBody

    // $ANTLR start synpred37_KWQLBody
    public final void synpred37_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:145:5: ( ( QUAL VARIABLE ) )
        // KWQLBody.g:145:5: ( QUAL VARIABLE )
        {
        // KWQLBody.g:145:5: ( QUAL VARIABLE )
        // KWQLBody.g:145:6: QUAL VARIABLE
        {
        match(input,QUAL,FOLLOW_QUAL_in_synpred37_KWQLBody1259); if (state.failed) return ;
        match(input,VARIABLE,FOLLOW_VARIABLE_in_synpred37_KWQLBody1261); if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred37_KWQLBody

    // $ANTLR start synpred38_KWQLBody
    public final void synpred38_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:146:5: ( ( QUAL valueterm ) )
        // KWQLBody.g:146:5: ( QUAL valueterm )
        {
        // KWQLBody.g:146:5: ( QUAL valueterm )
        // KWQLBody.g:146:6: QUAL valueterm
        {
        match(input,QUAL,FOLLOW_QUAL_in_synpred38_KWQLBody1285); if (state.failed) return ;
        pushFollow(FOLLOW_valueterm_in_synpred38_KWQLBody1287);
        valueterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }
    }
    // $ANTLR end synpred38_KWQLBody

    // $ANTLR start synpred41_KWQLBody
    public final void synpred41_KWQLBody_fragment() throws RecognitionException {   
        // KWQLBody.g:149:5: ( ( ( NEGATION qualifierterm ) ) )
        // KWQLBody.g:149:5: ( ( NEGATION qualifierterm ) )
        {
        // KWQLBody.g:149:5: ( ( NEGATION qualifierterm ) )
        // KWQLBody.g:149:6: ( NEGATION qualifierterm )
        {
        // KWQLBody.g:149:6: ( NEGATION qualifierterm )
        // KWQLBody.g:149:7: NEGATION qualifierterm
        {
        match(input,NEGATION,FOLLOW_NEGATION_in_synpred41_KWQLBody1352); if (state.failed) return ;
        pushFollow(FOLLOW_qualifierterm_in_synpred41_KWQLBody1354);
        qualifierterm();

        state._fsp--;
        if (state.failed) return ;

        }


        }


        }
    }
    // $ANTLR end synpred41_KWQLBody

    // $ANTLR start synpred42_KWQLBody
    public final void synpred42_KWQLBody_fragment() throws RecognitionException {   
        KWQLBody.valueterm_return v = null;


        // KWQLBody.g:153:28: ( connectives v= valueterm )
        // KWQLBody.g:153:28: connectives v= valueterm
        {
        pushFollow(FOLLOW_connectives_in_synpred42_KWQLBody1402);
        connectives();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_valueterm_in_synpred42_KWQLBody1406);
        v=valueterm();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred42_KWQLBody

    // Delegated rules

    public final boolean synpred18_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred18_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred42_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred42_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred32_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred32_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred36_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred36_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred33_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred33_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred16_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred16_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred3_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred10_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred10_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred27_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred27_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred38_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred38_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred17_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred17_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred26_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred26_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred11_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred11_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred24_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred24_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred34_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred34_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred8_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred8_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred31_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred31_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred29_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred29_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred25_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred25_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred30_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred30_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred15_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred15_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred35_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred35_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred20_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred20_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred9_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred9_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred41_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred41_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred37_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred37_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred19_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred19_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred28_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred28_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred21_KWQLBody() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred21_KWQLBody_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA3 dfa3 = new DFA3(this);
    protected DFA6 dfa6 = new DFA6(this);
    protected DFA7 dfa7 = new DFA7(this);
    protected DFA10 dfa10 = new DFA10(this);
    protected DFA11 dfa11 = new DFA11(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA17 dfa17 = new DFA17(this);
    protected DFA18 dfa18 = new DFA18(this);
    protected DFA19 dfa19 = new DFA19(this);
    protected DFA20 dfa20 = new DFA20(this);
    protected DFA22 dfa22 = new DFA22(this);
    protected DFA23 dfa23 = new DFA23(this);
    static final String DFA1_eotS =
        "\14\uffff";
    static final String DFA1_eofS =
        "\14\uffff";
    static final String DFA1_minS =
        "\1\4\11\0\2\uffff";
    static final String DFA1_maxS =
        "\1\45\11\0\2\uffff";
    static final String DFA1_acceptS =
        "\12\uffff\1\2\1\1";
    static final String DFA1_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\12\3\11\3\uffff\1\1\7\uffff\1\10\6\uffff\1\3\1\2\1\uffff"+
            "\1\5\1\uffff\1\6\2\uffff\1\7\2\uffff\1\4",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "67:1: body : ( ( unbound_complex_contentterm -> ^( RESOURCE ^( TYPE ) unbound_complex_contentterm ) ) | ( resource_term -> resource_term ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA1_1 = input.LA(1);

                         
                        int index1_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA1_2 = input.LA(1);

                         
                        int index1_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA1_3 = input.LA(1);

                         
                        int index1_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA1_4 = input.LA(1);

                         
                        int index1_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA1_5 = input.LA(1);

                         
                        int index1_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA1_6 = input.LA(1);

                         
                        int index1_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA1_7 = input.LA(1);

                         
                        int index1_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA1_8 = input.LA(1);

                         
                        int index1_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA1_9 = input.LA(1);

                         
                        int index1_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQLBody()) ) {s = 11;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index1_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 1, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA3_eotS =
        "\14\uffff";
    static final String DFA3_eofS =
        "\14\uffff";
    static final String DFA3_minS =
        "\1\4\5\uffff\1\0\5\uffff";
    static final String DFA3_maxS =
        "\1\45\5\uffff\1\0\5\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\1\11\uffff\1\2";
    static final String DFA3_specialS =
        "\6\uffff\1\0\5\uffff}>";
    static final String[] DFA3_transitionS = {
            "\4\1\3\uffff\1\1\7\uffff\1\1\6\uffff\2\1\1\uffff\1\1\1\uffff"+
            "\1\1\2\uffff\1\1\2\uffff\1\6",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "74:1: resource_term_helper : ( ( expanded_resource -> expanded_resource ) | LEFT_PARENTHESIS ( resource_term -> resource_term ) RIGHT_PARENTHESIS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_6 = input.LA(1);

                         
                        int index3_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred3_KWQLBody()) ) {s = 1;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index3_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA6_eotS =
        "\13\uffff";
    static final String DFA6_eofS =
        "\13\uffff";
    static final String DFA6_minS =
        "\1\4\1\uffff\1\0\3\uffff\1\0\4\uffff";
    static final String DFA6_maxS =
        "\1\45\1\uffff\1\0\3\uffff\1\0\4\uffff";
    static final String DFA6_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\1\3\2\uffff\1\4\3\uffff";
    static final String DFA6_specialS =
        "\2\uffff\1\0\3\uffff\1\1\4\uffff}>";
    static final String[] DFA6_transitionS = {
            "\1\3\3\1\3\uffff\1\2\7\uffff\1\7\6\uffff\2\4\1\uffff\1\7\1\uffff"+
            "\1\7\2\uffff\1\7\2\uffff\1\6",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "88:1: expanded_resource : ( ( non_ci_resourceterm -> ^( RESOURCE ^( TYPE ) non_ci_resourceterm ) ) | ( ci_resourceterm -> ci_resourceterm ) | ( complex_qualifierterm -> ^( RESOURCE ^( TYPE ) complex_qualifierterm ) ) | ( complex_valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_2 = input.LA(1);

                         
                        int index6_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred8_KWQLBody()) ) {s = 1;}

                        else if ( (synpred9_KWQLBody()) ) {s = 3;}

                        else if ( (synpred10_KWQLBody()) ) {s = 4;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index6_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA6_6 = input.LA(1);

                         
                        int index6_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred10_KWQLBody()) ) {s = 4;}

                        else if ( (true) ) {s = 7;}

                         
                        input.seek(index6_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA7_eotS =
        "\13\uffff";
    static final String DFA7_eofS =
        "\13\uffff";
    static final String DFA7_minS =
        "\1\5\1\0\11\uffff";
    static final String DFA7_maxS =
        "\1\45\1\0\11\uffff";
    static final String DFA7_acceptS =
        "\2\uffff\1\2\7\uffff\1\1";
    static final String DFA7_specialS =
        "\1\uffff\1\0\11\uffff}>";
    static final String[] DFA7_transitionS = {
            "\3\2\3\uffff\1\1\7\uffff\1\2\6\uffff\2\2\1\uffff\1\2\1\uffff"+
            "\1\2\2\uffff\1\2\2\uffff\1\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "94:1: unbound_complex_contentterm : ( NEGATION LEFT_PARENTHESIS unbound_complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION unbound_complex_contentterm_helper ) | unbound_complex_contentterm_helper );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA7_1 = input.LA(1);

                         
                        int index7_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred11_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index7_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 7, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA10_eotS =
        "\14\uffff";
    static final String DFA10_eofS =
        "\14\uffff";
    static final String DFA10_minS =
        "\1\5\11\0\2\uffff";
    static final String DFA10_maxS =
        "\1\45\11\0\2\uffff";
    static final String DFA10_acceptS =
        "\12\uffff\1\1\1\2";
    static final String DFA10_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff}>";
    static final String[] DFA10_transitionS = {
            "\3\11\3\uffff\1\4\7\uffff\1\10\6\uffff\1\2\1\1\1\uffff\1\5\1"+
            "\uffff\1\6\2\uffff\1\7\2\uffff\1\3",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "98:1: unbound_complex_contentterm_helper : ( ( unbound_contentterm opt )=> ( unbound_contentterm -> unbound_contentterm ) ( opt r= unbound_contentterm -> ^( AND $unbound_complex_contentterm_helper ^( opt $r) ) )+ | ( ( unbound_contentterm -> unbound_contentterm ) ( connectives r= unbound_contentterm -> ^( connectives $unbound_complex_contentterm_helper $r) )+ ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_1 = input.LA(1);

                         
                        int index10_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA10_2 = input.LA(1);

                         
                        int index10_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA10_3 = input.LA(1);

                         
                        int index10_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA10_4 = input.LA(1);

                         
                        int index10_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA10_5 = input.LA(1);

                         
                        int index10_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA10_6 = input.LA(1);

                         
                        int index10_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA10_7 = input.LA(1);

                         
                        int index10_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA10_8 = input.LA(1);

                         
                        int index10_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA10_9 = input.LA(1);

                         
                        int index10_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index10_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 10, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA11_eotS =
        "\14\uffff";
    static final String DFA11_eofS =
        "\14\uffff";
    static final String DFA11_minS =
        "\1\5\3\uffff\1\0\7\uffff";
    static final String DFA11_maxS =
        "\1\45\3\uffff\1\0\7\uffff";
    static final String DFA11_acceptS =
        "\1\uffff\1\1\10\uffff\1\2\1\3";
    static final String DFA11_specialS =
        "\4\uffff\1\0\7\uffff}>";
    static final String[] DFA11_transitionS = {
            "\3\1\3\uffff\1\4\7\uffff\1\1\6\uffff\2\1\1\uffff\1\1\1\uffff"+
            "\1\1\2\uffff\1\1\2\uffff\1\1",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
    static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
    static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
    static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
    static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
    static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
    static final short[][] DFA11_transition;

    static {
        int numStates = DFA11_transitionS.length;
        DFA11_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
        }
    }

    class DFA11 extends DFA {

        public DFA11(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 11;
            this.eot = DFA11_eot;
            this.eof = DFA11_eof;
            this.min = DFA11_min;
            this.max = DFA11_max;
            this.accept = DFA11_accept;
            this.special = DFA11_special;
            this.transition = DFA11_transition;
        }
        public String getDescription() {
            return "102:1: unbound_contentterm : ( unbound_contentterm_helper | NEGATION LEFT_PARENTHESIS unbound_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION unbound_contentterm_helper ) | NEGATION unbound_contentterm_helper -> ^( NEGATION unbound_contentterm_helper ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA11_4 = input.LA(1);

                         
                        int index11_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred15_KWQLBody()) ) {s = 1;}

                        else if ( (synpred16_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index11_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 11, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA12_eotS =
        "\13\uffff";
    static final String DFA12_eofS =
        "\13\uffff";
    static final String DFA12_minS =
        "\1\5\2\uffff\2\0\6\uffff";
    static final String DFA12_maxS =
        "\1\45\2\uffff\2\0\6\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\3\uffff\1\2\3\uffff\1\3\1\4";
    static final String DFA12_specialS =
        "\3\uffff\1\0\1\1\6\uffff}>";
    static final String[] DFA12_transitionS = {
            "\3\11\3\uffff\1\4\7\uffff\1\5\6\uffff\2\1\1\uffff\1\5\1\uffff"+
            "\1\5\2\uffff\1\5\2\uffff\1\3",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
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
            return "108:4: ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( LEFT_PARENTHESIS unbound_complex_contentterm RIGHT_PARENTHESIS -> unbound_complex_contentterm ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_3 = input.LA(1);

                         
                        int index12_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_KWQLBody()) ) {s = 1;}

                        else if ( (synpred18_KWQLBody()) ) {s = 5;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index12_3);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_4 = input.LA(1);

                         
                        int index12_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_KWQLBody()) ) {s = 1;}

                        else if ( (synpred18_KWQLBody()) ) {s = 5;}

                        else if ( (synpred19_KWQLBody()) ) {s = 9;}

                         
                        input.seek(index12_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA13_eotS =
        "\13\uffff";
    static final String DFA13_eofS =
        "\13\uffff";
    static final String DFA13_minS =
        "\1\5\1\0\11\uffff";
    static final String DFA13_maxS =
        "\1\45\1\0\11\uffff";
    static final String DFA13_acceptS =
        "\2\uffff\1\2\7\uffff\1\1";
    static final String DFA13_specialS =
        "\1\uffff\1\0\11\uffff}>";
    static final String[] DFA13_transitionS = {
            "\3\2\3\uffff\1\1\7\uffff\1\2\6\uffff\2\2\1\uffff\1\2\1\uffff"+
            "\1\2\2\uffff\1\2\2\uffff\1\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
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
            return "113:1: complex_contentterm : ( NEGATION LEFT_PARENTHESIS complex_contentterm_helper RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) | complex_contentterm_helper );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_1 = input.LA(1);

                         
                        int index13_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred20_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index13_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA16_eotS =
        "\14\uffff";
    static final String DFA16_eofS =
        "\14\uffff";
    static final String DFA16_minS =
        "\1\5\11\0\2\uffff";
    static final String DFA16_maxS =
        "\1\45\11\0\2\uffff";
    static final String DFA16_acceptS =
        "\12\uffff\1\1\1\2";
    static final String DFA16_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff}>";
    static final String[] DFA16_transitionS = {
            "\3\11\3\uffff\1\4\7\uffff\1\10\6\uffff\1\2\1\1\1\uffff\1\5\1"+
            "\uffff\1\6\2\uffff\1\7\2\uffff\1\3",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "117:1: complex_contentterm_helper : ( ( contentterm opt )=> ( contentterm -> contentterm ) ( opt r= contentterm -> ^( AND $complex_contentterm_helper ^( opt $r) ) )* | ( ( contentterm -> contentterm ) ( connectives r= contentterm -> ^( connectives $complex_contentterm_helper $r) )* ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA16_1 = input.LA(1);

                         
                        int index16_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA16_2 = input.LA(1);

                         
                        int index16_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA16_3 = input.LA(1);

                         
                        int index16_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA16_4 = input.LA(1);

                         
                        int index16_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA16_5 = input.LA(1);

                         
                        int index16_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA16_6 = input.LA(1);

                         
                        int index16_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA16_7 = input.LA(1);

                         
                        int index16_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA16_8 = input.LA(1);

                         
                        int index16_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA16_9 = input.LA(1);

                         
                        int index16_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQLBody()) ) {s = 10;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index16_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 16, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA17_eotS =
        "\14\uffff";
    static final String DFA17_eofS =
        "\14\uffff";
    static final String DFA17_minS =
        "\1\5\2\uffff\2\0\7\uffff";
    static final String DFA17_maxS =
        "\1\45\2\uffff\2\0\7\uffff";
    static final String DFA17_acceptS =
        "\1\uffff\1\1\3\uffff\1\2\3\uffff\1\3\1\5\1\4";
    static final String DFA17_specialS =
        "\3\uffff\1\0\1\1\7\uffff}>";
    static final String[] DFA17_transitionS = {
            "\3\11\3\uffff\1\4\7\uffff\1\5\6\uffff\2\1\1\uffff\1\5\1\uffff"+
            "\1\5\2\uffff\1\5\2\uffff\1\3",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
    static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
    static final char[] DFA17_min = DFA.unpackEncodedStringToUnsignedChars(DFA17_minS);
    static final char[] DFA17_max = DFA.unpackEncodedStringToUnsignedChars(DFA17_maxS);
    static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
    static final short[] DFA17_special = DFA.unpackEncodedString(DFA17_specialS);
    static final short[][] DFA17_transition;

    static {
        int numStates = DFA17_transitionS.length;
        DFA17_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
        }
    }

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = DFA17_eot;
            this.eof = DFA17_eof;
            this.min = DFA17_min;
            this.max = DFA17_max;
            this.accept = DFA17_accept;
            this.special = DFA17_special;
            this.transition = DFA17_transition;
        }
        public String getDescription() {
            return "122:4: ( ( complex_qualifierterm -> complex_qualifierterm ) | ( complex_valueterm -> ^( QUALIFIER LABEL ^( complex_valueterm ) ) ) | ( non_ci_resourceterm -> non_ci_resourceterm ) | ( NEGATION LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS -> ^( NEGATION complex_contentterm_helper ) ) | ( LEFT_PARENTHESIS ( complex_contentterm_helper -> complex_contentterm_helper ) RIGHT_PARENTHESIS ) )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA17_3 = input.LA(1);

                         
                        int index17_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred24_KWQLBody()) ) {s = 1;}

                        else if ( (synpred25_KWQLBody()) ) {s = 5;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index17_3);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA17_4 = input.LA(1);

                         
                        int index17_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred24_KWQLBody()) ) {s = 1;}

                        else if ( (synpred25_KWQLBody()) ) {s = 5;}

                        else if ( (synpred26_KWQLBody()) ) {s = 9;}

                        else if ( (synpred27_KWQLBody()) ) {s = 11;}

                         
                        input.seek(index17_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 17, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA18_eotS =
        "\14\uffff";
    static final String DFA18_eofS =
        "\14\uffff";
    static final String DFA18_minS =
        "\1\4\1\uffff\1\0\5\uffff\1\0\3\uffff";
    static final String DFA18_maxS =
        "\1\45\1\uffff\1\0\5\uffff\1\0\3\uffff";
    static final String DFA18_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\1\3\4\uffff\1\4\1\uffff\1\5";
    static final String DFA18_specialS =
        "\2\uffff\1\0\5\uffff\1\1\3\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\3\3\1\3\uffff\1\2\7\uffff\1\4\6\uffff\2\11\1\uffff\1\4\1"+
            "\uffff\1\4\2\uffff\1\4\2\uffff\1\10",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA18_eot = DFA.unpackEncodedString(DFA18_eotS);
    static final short[] DFA18_eof = DFA.unpackEncodedString(DFA18_eofS);
    static final char[] DFA18_min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
    static final char[] DFA18_max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
    static final short[] DFA18_accept = DFA.unpackEncodedString(DFA18_acceptS);
    static final short[] DFA18_special = DFA.unpackEncodedString(DFA18_specialS);
    static final short[][] DFA18_transition;

    static {
        int numStates = DFA18_transitionS.length;
        DFA18_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA18_transition[i] = DFA.unpackEncodedString(DFA18_transitionS[i]);
        }
    }

    class DFA18 extends DFA {

        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA18_eot;
            this.eof = DFA18_eof;
            this.min = DFA18_min;
            this.max = DFA18_max;
            this.accept = DFA18_accept;
            this.special = DFA18_special;
            this.transition = DFA18_transition;
        }
        public String getDescription() {
            return "128:1: subquery_term : ( ( non_ci_resourceterm -> non_ci_resourceterm ) | ( ci_resourceterm -> ci_resourceterm ) | ( valueterm -> ^( RESOURCE ^( TYPE ) ^( QUALIFIER ^( LABEL ) ^( valueterm ) ) ) ) | ( qualifierterm -> qualifierterm ) -> ^( RESOURCE ^( TYPE ) qualifierterm ) | ( LEFT_PARENTHESIS body RIGHT_PARENTHESIS -> body ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA18_2 = input.LA(1);

                         
                        int index18_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred28_KWQLBody()) ) {s = 1;}

                        else if ( (synpred29_KWQLBody()) ) {s = 3;}

                        else if ( (synpred30_KWQLBody()) ) {s = 4;}

                        else if ( (synpred31_KWQLBody()) ) {s = 9;}

                         
                        input.seek(index18_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA18_8 = input.LA(1);

                         
                        int index18_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred30_KWQLBody()) ) {s = 4;}

                        else if ( (synpred31_KWQLBody()) ) {s = 9;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index18_8);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 18, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA19_eotS =
        "\20\uffff";
    static final String DFA19_eofS =
        "\1\1\17\uffff";
    static final String DFA19_minS =
        "\1\5\3\uffff\1\0\13\uffff";
    static final String DFA19_maxS =
        "\1\46\3\uffff\1\0\13\uffff";
    static final String DFA19_acceptS =
        "\1\uffff\1\2\15\uffff\1\1";
    static final String DFA19_specialS =
        "\4\uffff\1\0\13\uffff}>";
    static final String[] DFA19_transitionS = {
            "\3\1\1\4\3\1\7\uffff\1\1\6\uffff\2\1\1\uffff\1\1\1\uffff\1\1"+
            "\2\uffff\1\1\2\uffff\2\1",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
    static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
    static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
    static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
    static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
    static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
    static final short[][] DFA19_transition;

    static {
        int numStates = DFA19_transitionS.length;
        DFA19_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
        }
    }

    class DFA19 extends DFA {

        public DFA19(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 19;
            this.eot = DFA19_eot;
            this.eof = DFA19_eof;
            this.min = DFA19_min;
            this.max = DFA19_max;
            this.accept = DFA19_accept;
            this.special = DFA19_special;
            this.transition = DFA19_transition;
        }
        public String getDescription() {
            return "()* loopback of 136:59: ( opt r= qualifierterm -> ^( AND $complex_qualifierterm ^( opt $r) ) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA19_4 = input.LA(1);

                         
                        int index19_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred33_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index19_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 19, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA20_eotS =
        "\20\uffff";
    static final String DFA20_eofS =
        "\1\2\17\uffff";
    static final String DFA20_minS =
        "\1\5\1\0\3\uffff\5\0\6\uffff";
    static final String DFA20_maxS =
        "\1\46\1\0\3\uffff\5\0\6\uffff";
    static final String DFA20_acceptS =
        "\2\uffff\1\2\14\uffff\1\1";
    static final String DFA20_specialS =
        "\1\uffff\1\0\3\uffff\1\1\1\2\1\3\1\4\1\5\6\uffff}>";
    static final String[] DFA20_transitionS = {
            "\4\2\1\5\1\1\1\11\7\uffff\1\2\6\uffff\1\7\1\6\1\uffff\1\2\1"+
            "\uffff\1\2\2\uffff\1\2\2\uffff\1\10\1\2",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "()* loopback of 137:39: ( connectives r= qualifierterm -> ^( connectives $complex_qualifierterm $r) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_1 = input.LA(1);

                         
                        int index20_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred34_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index20_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA20_5 = input.LA(1);

                         
                        int index20_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred34_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index20_5);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA20_6 = input.LA(1);

                         
                        int index20_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred34_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index20_6);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA20_7 = input.LA(1);

                         
                        int index20_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred34_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index20_7);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA20_8 = input.LA(1);

                         
                        int index20_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred34_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index20_8);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA20_9 = input.LA(1);

                         
                        int index20_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred34_KWQLBody()) ) {s = 15;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index20_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA22_eotS =
        "\13\uffff";
    static final String DFA22_eofS =
        "\13\uffff";
    static final String DFA22_minS =
        "\1\13\1\0\2\uffff\1\0\6\uffff";
    static final String DFA22_maxS =
        "\1\45\1\0\2\uffff\1\0\6\uffff";
    static final String DFA22_acceptS =
        "\2\uffff\1\5\1\6\1\uffff\1\1\1\2\1\3\1\4\1\7\1\10";
    static final String DFA22_specialS =
        "\1\uffff\1\0\2\uffff\1\1\6\uffff}>";
    static final String[] DFA22_transitionS = {
            "\1\4\16\uffff\1\2\1\1\11\uffff\1\3",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "142:1: qualifierterm : ( ( QUAL complex_valueterm ARROW VARIABLE ) -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) ) | ( QUAL LEFT_PARENTHESIS complex_valueterm ARROW VARIABLE RIGHT_PARENTHESIS ) -> ^( QUALIFIER ^( LABEL QUAL ) ^( complex_valueterm ) ^( VAR VARIABLE ) ) | ( QUAL VARIABLE -> ^( QUALIFIER ^( LABEL QUAL ) ^( VAR VARIABLE ) ) ) | ( QUAL valueterm -> ^( QUALIFIER ^( LABEL QUAL ) ^( valueterm ) ) ) | ( RESQUAL subquery_term -> ^( QUALIFIER ^( LABEL RESQUAL ) ^( subquery_term ) ) ) | ( LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( complex_qualifierterm ) ) | ( ( NEGATION qualifierterm ) -> ^( NEGATION qualifierterm ) ) | ( NEGATION LEFT_PARENTHESIS complex_qualifierterm RIGHT_PARENTHESIS -> ^( NEGATION complex_qualifierterm ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_1 = input.LA(1);

                         
                        int index22_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred35_KWQLBody()) ) {s = 5;}

                        else if ( (synpred36_KWQLBody()) ) {s = 6;}

                        else if ( (synpred37_KWQLBody()) ) {s = 7;}

                        else if ( (synpred38_KWQLBody()) ) {s = 8;}

                         
                        input.seek(index22_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA22_4 = input.LA(1);

                         
                        int index22_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred41_KWQLBody()) ) {s = 9;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index22_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA23_eotS =
        "\21\uffff";
    static final String DFA23_eofS =
        "\1\2\20\uffff";
    static final String DFA23_minS =
        "\1\5\1\0\3\uffff\1\0\2\uffff\6\0\3\uffff";
    static final String DFA23_maxS =
        "\1\47\1\0\3\uffff\1\0\2\uffff\6\0\3\uffff";
    static final String DFA23_acceptS =
        "\2\uffff\1\2\15\uffff\1\1";
    static final String DFA23_specialS =
        "\1\uffff\1\0\3\uffff\1\1\2\uffff\1\2\1\3\1\4\1\5\1\6\1\7\3\uffff}>";
    static final String[] DFA23_transitionS = {
            "\4\2\1\5\1\1\1\11\7\uffff\1\15\6\uffff\2\2\1\uffff\1\12\1\uffff"+
            "\1\13\2\uffff\1\14\2\uffff\1\10\2\2",
            "\1\uffff",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA23_eot = DFA.unpackEncodedString(DFA23_eotS);
    static final short[] DFA23_eof = DFA.unpackEncodedString(DFA23_eofS);
    static final char[] DFA23_min = DFA.unpackEncodedStringToUnsignedChars(DFA23_minS);
    static final char[] DFA23_max = DFA.unpackEncodedStringToUnsignedChars(DFA23_maxS);
    static final short[] DFA23_accept = DFA.unpackEncodedString(DFA23_acceptS);
    static final short[] DFA23_special = DFA.unpackEncodedString(DFA23_specialS);
    static final short[][] DFA23_transition;

    static {
        int numStates = DFA23_transitionS.length;
        DFA23_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA23_transition[i] = DFA.unpackEncodedString(DFA23_transitionS[i]);
        }
    }

    class DFA23 extends DFA {

        public DFA23(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 23;
            this.eot = DFA23_eot;
            this.eof = DFA23_eof;
            this.min = DFA23_min;
            this.max = DFA23_max;
            this.accept = DFA23_accept;
            this.special = DFA23_special;
            this.transition = DFA23_transition;
        }
        public String getDescription() {
            return "()* loopback of 153:27: ( connectives v= valueterm -> ^( connectives $complex_valueterm $v) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA23_1 = input.LA(1);

                         
                        int index23_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA23_5 = input.LA(1);

                         
                        int index23_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_5);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA23_8 = input.LA(1);

                         
                        int index23_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_8);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA23_9 = input.LA(1);

                         
                        int index23_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_9);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA23_10 = input.LA(1);

                         
                        int index23_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_10);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA23_11 = input.LA(1);

                         
                        int index23_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_11);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA23_12 = input.LA(1);

                         
                        int index23_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_12);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA23_13 = input.LA(1);

                         
                        int index23_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_KWQLBody()) ) {s = 16;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index23_13);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 23, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_unbound_complex_contentterm_in_body151 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_resource_term_in_body170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_resource_term_helper_in_resource_term188 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_DISJUNCTION_in_resource_term194 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_resource_term_helper_in_resource_term198 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_expanded_resource_in_resource_term_helper227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_resource_term_helper237 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_resource_term_in_resource_term_helper240 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_resource_term_helper245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resource_in_non_ci_resourceterm263 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm266 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_non_ci_resourceterm268 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_non_ci_resourceterm292 = new BitSet(new long[]{0x00000000000000E0L});
    public static final BitSet FOLLOW_non_ci_resource_in_non_ci_resourceterm294 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm297 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_non_ci_resourceterm299 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_non_ci_resourceterm323 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm325 = new BitSet(new long[]{0x00000000000000E0L});
    public static final BitSet FOLLOW_non_ci_resource_in_non_ci_resourceterm327 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_non_ci_resourceterm330 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_non_ci_resourceterm332 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm334 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_non_ci_resourceterm336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_resource_in_ci_resourceterm373 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm376 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_ci_resourceterm378 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm380 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_ci_resourceterm400 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ci_resource_in_ci_resourceterm402 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm405 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_ci_resourceterm407 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_ci_resourceterm431 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm433 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ci_resource_in_ci_resourceterm435 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_ci_resourceterm438 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_ci_resourceterm440 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm442 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_ci_resourceterm444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_expanded_resource477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_resourceterm_in_expanded_resource495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_expanded_resource505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_valueterm_in_expanded_resource523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_unbound_complex_contentterm554 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_unbound_complex_contentterm556 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_complex_contentterm_helper_in_unbound_complex_contentterm558 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_unbound_complex_contentterm560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unbound_complex_contentterm_helper_in_unbound_complex_contentterm572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper594 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_opt_in_unbound_complex_contentterm_helper600 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper604 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper629 = new BitSet(new long[]{0x00000024AC080EF0L});
    public static final BitSet FOLLOW_connectives_in_unbound_complex_contentterm_helper635 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_contentterm_in_unbound_complex_contentterm_helper639 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_unbound_contentterm_helper_in_unbound_contentterm669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_unbound_contentterm675 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_unbound_contentterm677 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_contentterm_helper_in_unbound_contentterm679 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_unbound_contentterm681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_unbound_contentterm693 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_contentterm_helper_in_unbound_contentterm695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_unbound_contentterm_helper722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_valueterm_in_unbound_contentterm_helper732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_unbound_contentterm_helper750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_unbound_contentterm_helper760 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_complex_contentterm_in_unbound_contentterm_helper762 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_unbound_contentterm_helper764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_complex_contentterm783 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_complex_contentterm785 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_complex_contentterm787 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_complex_contentterm789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_complex_contentterm801 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_contentterm_in_complex_contentterm_helper823 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_opt_in_complex_contentterm_helper829 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_contentterm_in_complex_contentterm_helper833 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_contentterm_in_complex_contentterm_helper859 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_connectives_in_complex_contentterm_helper865 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_contentterm_in_complex_contentterm_helper869 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_contentterm902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_valueterm_in_contentterm912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_contentterm930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_contentterm940 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_contentterm942 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_contentterm945 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_contentterm950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_contentterm966 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_contentterm969 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_contentterm974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_subquery_term994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_resourceterm_in_subquery_term1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueterm_in_subquery_term1014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifierterm_in_subquery_term1042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_subquery_term1061 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_body_in_subquery_term1063 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_subquery_term1065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifierterm_in_complex_qualifierterm1091 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_opt_in_complex_qualifierterm1098 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_qualifierterm_in_complex_qualifierterm1102 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_qualifierterm_in_complex_qualifierterm1129 = new BitSet(new long[]{0x000000200C000E02L});
    public static final BitSet FOLLOW_connectives_in_complex_qualifierterm1136 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_qualifierterm_in_complex_qualifierterm1140 = new BitSet(new long[]{0x000000200C000E02L});
    public static final BitSet FOLLOW_optional_in_opt1170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_qualifierterm1187 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_valueterm_in_qualifierterm1189 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ARROW_in_qualifierterm1191 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_VARIABLE_in_qualifierterm1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_qualifierterm1221 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_qualifierterm1223 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_valueterm_in_qualifierterm1225 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ARROW_in_qualifierterm1227 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_VARIABLE_in_qualifierterm1229 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_qualifierterm1231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_qualifierterm1259 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_VARIABLE_in_qualifierterm1261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_qualifierterm1285 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_valueterm_in_qualifierterm1287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RESQUAL_in_qualifierterm1309 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_subquery_term_in_qualifierterm1311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_qualifierterm1334 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_qualifierterm1336 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_qualifierterm1338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_qualifierterm1352 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_qualifierterm_in_qualifierterm1354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_qualifierterm1369 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_qualifierterm1371 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_qualifierterm1373 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_qualifierterm1375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueterm_in_complex_valueterm1396 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_connectives_in_complex_valueterm1402 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_valueterm_in_complex_valueterm1406 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_connective_in_connectives1439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_valueterm1463 = new BitSet(new long[]{0x00000004A0080000L});
    public static final BitSet FOLLOW_atomar_value_in_valueterm1465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomar_value_in_valueterm1480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_valueterm1492 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_valueterm1494 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_valueterm_in_valueterm1496 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_valueterm1498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_valueterm1513 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_valueterm_in_valueterm1515 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_valueterm1517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_atomar_value1536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DA_in_atomar_value1546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_KW_in_atomar_value1556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_URI_in_atomar_value1566 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_res1589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_non_ci_resource1612 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CI_in_ci_resource1633 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_connective1649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPTIONAL_in_optional1668 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_optional1670 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_KW_in_optional1672 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_optional1674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unbound_complex_contentterm_in_synpred1_KWQLBody151 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expanded_resource_in_synpred3_KWQLBody227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_synpred8_KWQLBody477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_resourceterm_in_synpred9_KWQLBody495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_synpred10_KWQLBody505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_synpred11_KWQLBody554 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred11_KWQLBody556 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_complex_contentterm_helper_in_synpred11_KWQLBody558 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred11_KWQLBody560 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unbound_contentterm_in_synpred12_KWQLBody588 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_opt_in_synpred12_KWQLBody590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unbound_contentterm_helper_in_synpred15_KWQLBody669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_synpred16_KWQLBody675 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred16_KWQLBody677 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_unbound_contentterm_helper_in_synpred16_KWQLBody679 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred16_KWQLBody681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_synpred17_KWQLBody722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_valueterm_in_synpred18_KWQLBody732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_synpred19_KWQLBody750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_synpred20_KWQLBody783 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred20_KWQLBody785 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_synpred20_KWQLBody787 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred20_KWQLBody789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_contentterm_in_synpred21_KWQLBody817 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_opt_in_synpred21_KWQLBody819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_qualifierterm_in_synpred24_KWQLBody902 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_valueterm_in_synpred25_KWQLBody912 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_synpred26_KWQLBody930 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_synpred27_KWQLBody940 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred27_KWQLBody942 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_contentterm_helper_in_synpred27_KWQLBody945 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred27_KWQLBody950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_ci_resourceterm_in_synpred28_KWQLBody994 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_resourceterm_in_synpred29_KWQLBody1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_valueterm_in_synpred30_KWQLBody1014 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifierterm_in_synpred31_KWQLBody1042 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifierterm_in_synpred32_KWQLBody1083 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_opt_in_synpred32_KWQLBody1085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opt_in_synpred33_KWQLBody1098 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_qualifierterm_in_synpred33_KWQLBody1102 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_connectives_in_synpred34_KWQLBody1136 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_qualifierterm_in_synpred34_KWQLBody1140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_synpred35_KWQLBody1187 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_valueterm_in_synpred35_KWQLBody1189 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ARROW_in_synpred35_KWQLBody1191 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_VARIABLE_in_synpred35_KWQLBody1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_synpred36_KWQLBody1221 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred36_KWQLBody1223 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_complex_valueterm_in_synpred36_KWQLBody1225 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_ARROW_in_synpred36_KWQLBody1227 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_VARIABLE_in_synpred36_KWQLBody1229 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred36_KWQLBody1231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_synpred37_KWQLBody1259 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_VARIABLE_in_synpred37_KWQLBody1261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUAL_in_synpred38_KWQLBody1285 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_valueterm_in_synpred38_KWQLBody1287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_synpred41_KWQLBody1352 = new BitSet(new long[]{0x000000200C000800L});
    public static final BitSet FOLLOW_qualifierterm_in_synpred41_KWQLBody1354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_connectives_in_synpred42_KWQLBody1402 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_valueterm_in_synpred42_KWQLBody1406 = new BitSet(new long[]{0x0000000000000002L});

}
