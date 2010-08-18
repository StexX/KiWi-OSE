package kiwi.service.query.kwql.parser;
// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 KWQL.g 2010-04-19 10:33:14

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class KWQL extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CI", "LINK", "FRAG", "TAG", "OPTIONAL", "CONJUNCTION", "DISJUNCTION", "NEGATION", "UQ", "BOOLEAN", "SCHEME", "HOST", "PATH", "QUERY", "FRAGMENT", "URI", "SYMBOL", "NAME", "COMMASEP", "COMMA", "QUANT", "COUNT", "RESQUAL", "QUAL", "DIGIT", "INT", "ISOTIME", "DA", "NAME2", "EscNAME", "KW", "VARIABLE", "WS", "LEFT_PARENTHESIS", "RIGHT_PARENTHESIS", "ARROW", "SPARQL", "AT", "LOWER", "UPPER", "HexPrefix", "HexDigit", "LETTER", "VAR", "LABEL", "QUALIFIER", "RESOURCE", "TYPE", "AND", "RULE", "FUNC", "SEPARATOR", "NUM", "HEAD", "BODY", "INTEGER", "DATE", "STRING", "UR", "BOOL_BODY", "BOOL_QUERY", "CI_BODY", "SEL_QUERY", "CI_QUERY", "CI_COMP"
    };
    public static final int FRAG=6;
    public static final int QUANT=24;
    public static final int SEL_QUERY=85;
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
    public static final int UR=63;
    public static final int NAME=21;
    public static final int BOOLEAN=13;
    public static final int RIGHT_PARENTHESIS=38;
    public static final int QUAL=27;
    public static final int COMMA=23;
    public static final int PATH=16;
    public static final int LEFT_PARENTHESIS=37;
    public static final int COMMASEP=22;
    public static final int BOOL_BODY=82;
    public static final int CI_COMP=87;
    public static final int VAR=47;
    public static final int BODY=58;
    public static final int DIGIT=28;
    public static final int CI_QUERY=86;
    public static final int HexPrefix=44;
    public static final int INTEGER=60;
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
    public static final int BOOL_QUERY=83;
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
    public static final int CI_BODY=84;
    public static final int CI=4;
    public static final int QUERY=17;
    public static final int NAME2=32;
    public static final int ARROW=39;
    public static final int LOWER=42;
    public static final int DATE=61;
    public static final int RESQUAL=26;
    public static final int UPPER=43;
    public static final int STRING=62;

    // delegates
    public KWQL_KWQLBody gKWQLBody;
    public KWQL_KWQLHead gKWQLHead;
    // delegators


        public KWQL(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public KWQL(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            gKWQLBody = new KWQL_KWQLBody(input, state, this);
            gKWQLHead = new KWQL_KWQLHead(input, state, this);         
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
        gKWQLBody.setTreeAdaptor(this.adaptor);gKWQLHead.setTreeAdaptor(this.adaptor);
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return KWQL.tokenNames; }
    public String getGrammarFileName() { return "KWQL.g"; }


    public static class kwqlrule_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "kwqlrule"
    // KWQL.g:20:1: kwqlrule : ( head AT complex_boolean_query EOF -> ^( RULE ^( HEAD head ) ^( BOOL_QUERY complex_boolean_query ) ) | head AT unique_ci_query EOF -> ^( RULE ^( HEAD head ) ^( CI_QUERY unique_ci_query ) ) | unique_ci_query EOF -> ^( RULE ^( HEAD ) ^( CI_QUERY unique_ci_query ) ) | head AT selection_query EOF -> ^( RULE ^( HEAD head ) ^( SEL_QUERY selection_query ) ) );
    public final KWQL.kwqlrule_return kwqlrule() throws RecognitionException {
        KWQL.kwqlrule_return retval = new KWQL.kwqlrule_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token AT2=null;
        Token EOF4=null;
        Token AT6=null;
        Token EOF8=null;
        Token EOF10=null;
        Token AT12=null;
        Token EOF14=null;
        KWQL_KWQLHead.head_return head1 = null;

        KWQL.complex_boolean_query_return complex_boolean_query3 = null;

        KWQL_KWQLHead.head_return head5 = null;

        KWQL.unique_ci_query_return unique_ci_query7 = null;

        KWQL.unique_ci_query_return unique_ci_query9 = null;

        KWQL_KWQLHead.head_return head11 = null;

        KWQL.selection_query_return selection_query13 = null;


        CommonTree AT2_tree=null;
        CommonTree EOF4_tree=null;
        CommonTree AT6_tree=null;
        CommonTree EOF8_tree=null;
        CommonTree EOF10_tree=null;
        CommonTree AT12_tree=null;
        CommonTree EOF14_tree=null;
        RewriteRuleTokenStream stream_AT=new RewriteRuleTokenStream(adaptor,"token AT");
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_unique_ci_query=new RewriteRuleSubtreeStream(adaptor,"rule unique_ci_query");
        RewriteRuleSubtreeStream stream_complex_boolean_query=new RewriteRuleSubtreeStream(adaptor,"rule complex_boolean_query");
        RewriteRuleSubtreeStream stream_head=new RewriteRuleSubtreeStream(adaptor,"rule head");
        RewriteRuleSubtreeStream stream_selection_query=new RewriteRuleSubtreeStream(adaptor,"rule selection_query");
        try {
            // KWQL.g:21:3: ( head AT complex_boolean_query EOF -> ^( RULE ^( HEAD head ) ^( BOOL_QUERY complex_boolean_query ) ) | head AT unique_ci_query EOF -> ^( RULE ^( HEAD head ) ^( CI_QUERY unique_ci_query ) ) | unique_ci_query EOF -> ^( RULE ^( HEAD ) ^( CI_QUERY unique_ci_query ) ) | head AT selection_query EOF -> ^( RULE ^( HEAD head ) ^( SEL_QUERY selection_query ) ) )
            int alt1=4;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // KWQL.g:22:3: head AT complex_boolean_query EOF
                    {
                    pushFollow(FOLLOW_head_in_kwqlrule105);
                    head1=head();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_head.add(head1.getTree());
                    AT2=(Token)match(input,AT,FOLLOW_AT_in_kwqlrule107); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AT.add(AT2);

                    pushFollow(FOLLOW_complex_boolean_query_in_kwqlrule109);
                    complex_boolean_query3=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_boolean_query.add(complex_boolean_query3.getTree());
                    EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_kwqlrule111); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EOF.add(EOF4);



                    // AST REWRITE
                    // elements: complex_boolean_query, head
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 22:37: -> ^( RULE ^( HEAD head ) ^( BOOL_QUERY complex_boolean_query ) )
                    {
                        // KWQL.g:22:40: ^( RULE ^( HEAD head ) ^( BOOL_QUERY complex_boolean_query ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RULE, "RULE"), root_1);

                        // KWQL.g:22:47: ^( HEAD head )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(HEAD, "HEAD"), root_2);

                        adaptor.addChild(root_2, stream_head.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:22:60: ^( BOOL_QUERY complex_boolean_query )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOOL_QUERY, "BOOL_QUERY"), root_2);

                        adaptor.addChild(root_2, stream_complex_boolean_query.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // KWQL.g:23:4: head AT unique_ci_query EOF
                    {
                    pushFollow(FOLLOW_head_in_kwqlrule134);
                    head5=head();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_head.add(head5.getTree());
                    AT6=(Token)match(input,AT,FOLLOW_AT_in_kwqlrule136); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AT.add(AT6);

                    pushFollow(FOLLOW_unique_ci_query_in_kwqlrule138);
                    unique_ci_query7=unique_ci_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unique_ci_query.add(unique_ci_query7.getTree());
                    EOF8=(Token)match(input,EOF,FOLLOW_EOF_in_kwqlrule140); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EOF.add(EOF8);



                    // AST REWRITE
                    // elements: head, unique_ci_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 23:32: -> ^( RULE ^( HEAD head ) ^( CI_QUERY unique_ci_query ) )
                    {
                        // KWQL.g:23:35: ^( RULE ^( HEAD head ) ^( CI_QUERY unique_ci_query ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RULE, "RULE"), root_1);

                        // KWQL.g:23:42: ^( HEAD head )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(HEAD, "HEAD"), root_2);

                        adaptor.addChild(root_2, stream_head.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:23:55: ^( CI_QUERY unique_ci_query )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_QUERY, "CI_QUERY"), root_2);

                        adaptor.addChild(root_2, stream_unique_ci_query.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQL.g:24:4: unique_ci_query EOF
                    {
                    pushFollow(FOLLOW_unique_ci_query_in_kwqlrule163);
                    unique_ci_query9=unique_ci_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unique_ci_query.add(unique_ci_query9.getTree());
                    EOF10=(Token)match(input,EOF,FOLLOW_EOF_in_kwqlrule165); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EOF.add(EOF10);



                    // AST REWRITE
                    // elements: unique_ci_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 24:24: -> ^( RULE ^( HEAD ) ^( CI_QUERY unique_ci_query ) )
                    {
                        // KWQL.g:24:27: ^( RULE ^( HEAD ) ^( CI_QUERY unique_ci_query ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RULE, "RULE"), root_1);

                        // KWQL.g:24:34: ^( HEAD )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(HEAD, "HEAD"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:24:42: ^( CI_QUERY unique_ci_query )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_QUERY, "CI_QUERY"), root_2);

                        adaptor.addChild(root_2, stream_unique_ci_query.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // KWQL.g:25:4: head AT selection_query EOF
                    {
                    pushFollow(FOLLOW_head_in_kwqlrule186);
                    head11=head();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_head.add(head11.getTree());
                    AT12=(Token)match(input,AT,FOLLOW_AT_in_kwqlrule188); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_AT.add(AT12);

                    pushFollow(FOLLOW_selection_query_in_kwqlrule190);
                    selection_query13=selection_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_selection_query.add(selection_query13.getTree());
                    EOF14=(Token)match(input,EOF,FOLLOW_EOF_in_kwqlrule192); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_EOF.add(EOF14);



                    // AST REWRITE
                    // elements: head, selection_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 25:32: -> ^( RULE ^( HEAD head ) ^( SEL_QUERY selection_query ) )
                    {
                        // KWQL.g:25:35: ^( RULE ^( HEAD head ) ^( SEL_QUERY selection_query ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RULE, "RULE"), root_1);

                        // KWQL.g:25:42: ^( HEAD head )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(HEAD, "HEAD"), root_2);

                        adaptor.addChild(root_2, stream_head.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:25:55: ^( SEL_QUERY selection_query )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SEL_QUERY, "SEL_QUERY"), root_2);

                        adaptor.addChild(root_2, stream_selection_query.nextTree());

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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "kwqlrule"

    public static class global_body_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "global_body"
    // KWQL.g:28:1: global_body : ( ( global_body_helper -> global_body_helper ) ( connectives v= global_body_helper -> ^( connectives $global_body $v) )* | NEGATION LEFT_PARENTHESIS resource_term_helper RIGHT_PARENTHESIS -> ^( NEGATION resource_term_helper ) );
    public final KWQL.global_body_return global_body() throws RecognitionException {
        KWQL.global_body_return retval = new KWQL.global_body_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION17=null;
        Token LEFT_PARENTHESIS18=null;
        Token RIGHT_PARENTHESIS20=null;
        KWQL.global_body_helper_return v = null;

        KWQL.global_body_helper_return global_body_helper15 = null;

        KWQL_KWQLBody.connectives_return connectives16 = null;

        KWQL_KWQLBody.resource_term_helper_return resource_term_helper19 = null;


        CommonTree NEGATION17_tree=null;
        CommonTree LEFT_PARENTHESIS18_tree=null;
        CommonTree RIGHT_PARENTHESIS20_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        RewriteRuleSubtreeStream stream_global_body_helper=new RewriteRuleSubtreeStream(adaptor,"rule global_body_helper");
        RewriteRuleSubtreeStream stream_resource_term_helper=new RewriteRuleSubtreeStream(adaptor,"rule resource_term_helper");
        try {
            // KWQL.g:29:3: ( ( global_body_helper -> global_body_helper ) ( connectives v= global_body_helper -> ^( connectives $global_body $v) )* | NEGATION LEFT_PARENTHESIS resource_term_helper RIGHT_PARENTHESIS -> ^( NEGATION resource_term_helper ) )
            int alt3=2;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // KWQL.g:30:3: ( global_body_helper -> global_body_helper ) ( connectives v= global_body_helper -> ^( connectives $global_body $v) )*
                    {
                    // KWQL.g:30:3: ( global_body_helper -> global_body_helper )
                    // KWQL.g:30:4: global_body_helper
                    {
                    pushFollow(FOLLOW_global_body_helper_in_global_body226);
                    global_body_helper15=global_body_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_global_body_helper.add(global_body_helper15.getTree());


                    // AST REWRITE
                    // elements: global_body_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 30:22: -> global_body_helper
                    {
                        adaptor.addChild(root_0, stream_global_body_helper.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    // KWQL.g:30:44: ( connectives v= global_body_helper -> ^( connectives $global_body $v) )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>=CI && LA2_0<=TAG)||(LA2_0>=CONJUNCTION && LA2_0<=NEGATION)||LA2_0==BOOLEAN||LA2_0==URI||(LA2_0>=RESQUAL && LA2_0<=QUAL)||LA2_0==INT||LA2_0==DA||LA2_0==KW||LA2_0==LEFT_PARENTHESIS) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // KWQL.g:30:45: connectives v= global_body_helper
                    	    {
                    	    pushFollow(FOLLOW_connectives_in_global_body232);
                    	    connectives16=connectives();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_connectives.add(connectives16.getTree());
                    	    pushFollow(FOLLOW_global_body_helper_in_global_body236);
                    	    v=global_body_helper();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_global_body_helper.add(v.getTree());


                    	    // AST REWRITE
                    	    // elements: v, global_body, connectives
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
                    	    // 30:78: -> ^( connectives $global_body $v)
                    	    {
                    	        // KWQL.g:30:81: ^( connectives $global_body $v)
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
                    	    break loop2;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // KWQL.g:31:5: NEGATION LEFT_PARENTHESIS resource_term_helper RIGHT_PARENTHESIS
                    {
                    NEGATION17=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_global_body256); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION17);

                    LEFT_PARENTHESIS18=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_global_body258); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS18);

                    pushFollow(FOLLOW_resource_term_helper_in_global_body260);
                    resource_term_helper19=resource_term_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_resource_term_helper.add(resource_term_helper19.getTree());
                    RIGHT_PARENTHESIS20=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_global_body262); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS20);



                    // AST REWRITE
                    // elements: NEGATION, resource_term_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 31:69: -> ^( NEGATION resource_term_helper )
                    {
                        // KWQL.g:31:71: ^( NEGATION resource_term_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_resource_term_helper.nextTree());

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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "global_body"

    public static class global_body_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "global_body_helper"
    // KWQL.g:33:1: global_body_helper : ( body | LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS );
    public final KWQL.global_body_helper_return global_body_helper() throws RecognitionException {
        KWQL.global_body_helper_return retval = new KWQL.global_body_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS22=null;
        Token RIGHT_PARENTHESIS24=null;
        KWQL_KWQLBody.body_return body21 = null;

        KWQL.global_body_return global_body23 = null;


        CommonTree LEFT_PARENTHESIS22_tree=null;
        CommonTree RIGHT_PARENTHESIS24_tree=null;

        try {
            // KWQL.g:34:3: ( body | LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS )
            int alt4=2;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // KWQL.g:35:3: body
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_body_in_global_body_helper281);
                    body21=body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, body21.getTree());

                    }
                    break;
                case 2 :
                    // KWQL.g:36:4: LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LEFT_PARENTHESIS22=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_global_body_helper286); if (state.failed) return retval;
                    pushFollow(FOLLOW_global_body_in_global_body_helper289);
                    global_body23=global_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, global_body23.getTree());
                    RIGHT_PARENTHESIS24=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_global_body_helper291); if (state.failed) return retval;

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
    // $ANTLR end "global_body_helper"

    public static class complex_boolean_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "complex_boolean_query"
    // KWQL.g:38:1: complex_boolean_query : ( complex_boolean_query_helper -> complex_boolean_query_helper ) ( connectives v= complex_boolean_query_helper -> ^( connectives $complex_boolean_query $v) )* ;
    public final KWQL.complex_boolean_query_return complex_boolean_query() throws RecognitionException {
        KWQL.complex_boolean_query_return retval = new KWQL.complex_boolean_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQL.complex_boolean_query_helper_return v = null;

        KWQL.complex_boolean_query_helper_return complex_boolean_query_helper25 = null;

        KWQL_KWQLBody.connectives_return connectives26 = null;


        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        RewriteRuleSubtreeStream stream_complex_boolean_query_helper=new RewriteRuleSubtreeStream(adaptor,"rule complex_boolean_query_helper");
        try {
            // KWQL.g:39:3: ( ( complex_boolean_query_helper -> complex_boolean_query_helper ) ( connectives v= complex_boolean_query_helper -> ^( connectives $complex_boolean_query $v) )* )
            // KWQL.g:40:3: ( complex_boolean_query_helper -> complex_boolean_query_helper ) ( connectives v= complex_boolean_query_helper -> ^( connectives $complex_boolean_query $v) )*
            {
            // KWQL.g:40:3: ( complex_boolean_query_helper -> complex_boolean_query_helper )
            // KWQL.g:40:4: complex_boolean_query_helper
            {
            pushFollow(FOLLOW_complex_boolean_query_helper_in_complex_boolean_query308);
            complex_boolean_query_helper25=complex_boolean_query_helper();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_complex_boolean_query_helper.add(complex_boolean_query_helper25.getTree());


            // AST REWRITE
            // elements: complex_boolean_query_helper
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 40:32: -> complex_boolean_query_helper
            {
                adaptor.addChild(root_0, stream_complex_boolean_query_helper.nextTree());

            }

            retval.tree = root_0;}
            }

            // KWQL.g:40:64: ( connectives v= complex_boolean_query_helper -> ^( connectives $complex_boolean_query $v) )*
            loop5:
            do {
                int alt5=2;
                alt5 = dfa5.predict(input);
                switch (alt5) {
            	case 1 :
            	    // KWQL.g:40:65: connectives v= complex_boolean_query_helper
            	    {
            	    pushFollow(FOLLOW_connectives_in_complex_boolean_query314);
            	    connectives26=connectives();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_connectives.add(connectives26.getTree());
            	    pushFollow(FOLLOW_complex_boolean_query_helper_in_complex_boolean_query318);
            	    v=complex_boolean_query_helper();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_complex_boolean_query_helper.add(v.getTree());


            	    // AST REWRITE
            	    // elements: connectives, complex_boolean_query, v
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
            	    // 40:108: -> ^( connectives $complex_boolean_query $v)
            	    {
            	        // KWQL.g:40:111: ^( connectives $complex_boolean_query $v)
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
            	    break loop5;
                }
            } while (true);


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
    // $ANTLR end "complex_boolean_query"

    public static class complex_boolean_query_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "complex_boolean_query_helper"
    // KWQL.g:42:1: complex_boolean_query_helper : ( boolean_query | NEGATION LEFT_PARENTHESIS complex_boolean_query RIGHT_PARENTHESIS -> ^( NEGATION complex_boolean_query ) | LEFT_PARENTHESIS complex_boolean_query RIGHT_PARENTHESIS );
    public final KWQL.complex_boolean_query_helper_return complex_boolean_query_helper() throws RecognitionException {
        KWQL.complex_boolean_query_helper_return retval = new KWQL.complex_boolean_query_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEGATION28=null;
        Token LEFT_PARENTHESIS29=null;
        Token RIGHT_PARENTHESIS31=null;
        Token LEFT_PARENTHESIS32=null;
        Token RIGHT_PARENTHESIS34=null;
        KWQL.boolean_query_return boolean_query27 = null;

        KWQL.complex_boolean_query_return complex_boolean_query30 = null;

        KWQL.complex_boolean_query_return complex_boolean_query33 = null;


        CommonTree NEGATION28_tree=null;
        CommonTree LEFT_PARENTHESIS29_tree=null;
        CommonTree RIGHT_PARENTHESIS31_tree=null;
        CommonTree LEFT_PARENTHESIS32_tree=null;
        CommonTree RIGHT_PARENTHESIS34_tree=null;
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_complex_boolean_query=new RewriteRuleSubtreeStream(adaptor,"rule complex_boolean_query");
        try {
            // KWQL.g:43:3: ( boolean_query | NEGATION LEFT_PARENTHESIS complex_boolean_query RIGHT_PARENTHESIS -> ^( NEGATION complex_boolean_query ) | LEFT_PARENTHESIS complex_boolean_query RIGHT_PARENTHESIS )
            int alt6=3;
            switch ( input.LA(1) ) {
            case BOOLEAN:
                {
                alt6=1;
                }
                break;
            case NEGATION:
                {
                int LA6_2 = input.LA(2);

                if ( (LA6_2==BOOLEAN) ) {
                    alt6=1;
                }
                else if ( (LA6_2==LEFT_PARENTHESIS) ) {
                    alt6=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;
                }
                }
                break;
            case LEFT_PARENTHESIS:
                {
                alt6=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // KWQL.g:44:3: boolean_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_boolean_query_in_complex_boolean_query_helper347);
                    boolean_query27=boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_query27.getTree());

                    }
                    break;
                case 2 :
                    // KWQL.g:45:4: NEGATION LEFT_PARENTHESIS complex_boolean_query RIGHT_PARENTHESIS
                    {
                    NEGATION28=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_complex_boolean_query_helper352); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION28);

                    LEFT_PARENTHESIS29=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_complex_boolean_query_helper354); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS29);

                    pushFollow(FOLLOW_complex_boolean_query_in_complex_boolean_query_helper356);
                    complex_boolean_query30=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_boolean_query.add(complex_boolean_query30.getTree());
                    RIGHT_PARENTHESIS31=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_complex_boolean_query_helper358); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS31);



                    // AST REWRITE
                    // elements: complex_boolean_query, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 45:69: -> ^( NEGATION complex_boolean_query )
                    {
                        // KWQL.g:45:71: ^( NEGATION complex_boolean_query )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_complex_boolean_query.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQL.g:46:4: LEFT_PARENTHESIS complex_boolean_query RIGHT_PARENTHESIS
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LEFT_PARENTHESIS32=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_complex_boolean_query_helper369); if (state.failed) return retval;
                    pushFollow(FOLLOW_complex_boolean_query_in_complex_boolean_query_helper372);
                    complex_boolean_query33=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, complex_boolean_query33.getTree());
                    RIGHT_PARENTHESIS34=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_complex_boolean_query_helper374); if (state.failed) return retval;

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
    // $ANTLR end "complex_boolean_query_helper"

    public static class boolean_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_query"
    // KWQL.g:48:1: boolean_query : ( BOOLEAN LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS | NEGATION BOOLEAN LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS -> ^( NEGATION global_body ) );
    public final KWQL.boolean_query_return boolean_query() throws RecognitionException {
        KWQL.boolean_query_return retval = new KWQL.boolean_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token BOOLEAN35=null;
        Token LEFT_PARENTHESIS36=null;
        Token RIGHT_PARENTHESIS38=null;
        Token NEGATION39=null;
        Token BOOLEAN40=null;
        Token LEFT_PARENTHESIS41=null;
        Token RIGHT_PARENTHESIS43=null;
        KWQL.global_body_return global_body37 = null;

        KWQL.global_body_return global_body42 = null;


        CommonTree BOOLEAN35_tree=null;
        CommonTree LEFT_PARENTHESIS36_tree=null;
        CommonTree RIGHT_PARENTHESIS38_tree=null;
        CommonTree NEGATION39_tree=null;
        CommonTree BOOLEAN40_tree=null;
        CommonTree LEFT_PARENTHESIS41_tree=null;
        CommonTree RIGHT_PARENTHESIS43_tree=null;
        RewriteRuleTokenStream stream_BOOLEAN=new RewriteRuleTokenStream(adaptor,"token BOOLEAN");
        RewriteRuleTokenStream stream_NEGATION=new RewriteRuleTokenStream(adaptor,"token NEGATION");
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_global_body=new RewriteRuleSubtreeStream(adaptor,"rule global_body");
        try {
            // KWQL.g:49:3: ( BOOLEAN LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS | NEGATION BOOLEAN LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS -> ^( NEGATION global_body ) )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==BOOLEAN) ) {
                alt7=1;
            }
            else if ( (LA7_0==NEGATION) ) {
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
                    // KWQL.g:50:3: BOOLEAN LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    BOOLEAN35=(Token)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_boolean_query389); if (state.failed) return retval;
                    LEFT_PARENTHESIS36=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_boolean_query392); if (state.failed) return retval;
                    pushFollow(FOLLOW_global_body_in_boolean_query395);
                    global_body37=global_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, global_body37.getTree());
                    RIGHT_PARENTHESIS38=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_boolean_query397); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // KWQL.g:51:5: NEGATION BOOLEAN LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS
                    {
                    NEGATION39=(Token)match(input,NEGATION,FOLLOW_NEGATION_in_boolean_query404); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEGATION.add(NEGATION39);

                    BOOLEAN40=(Token)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_boolean_query406); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BOOLEAN.add(BOOLEAN40);

                    LEFT_PARENTHESIS41=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_boolean_query408); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS41);

                    pushFollow(FOLLOW_global_body_in_boolean_query410);
                    global_body42=global_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_global_body.add(global_body42.getTree());
                    RIGHT_PARENTHESIS43=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_boolean_query412); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS43);



                    // AST REWRITE
                    // elements: global_body, NEGATION
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 51:69: -> ^( NEGATION global_body )
                    {
                        // KWQL.g:51:72: ^( NEGATION global_body )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_NEGATION.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_global_body.nextTree());

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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "boolean_query"

    public static class unique_ci_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unique_ci_query"
    // KWQL.g:54:1: unique_ci_query : ( unique_ci_query_helper -> unique_ci_query_helper ) ( DISJUNCTION s= unique_ci_query_helper -> ^( DISJUNCTION $unique_ci_query $s) )* ;
    public final KWQL.unique_ci_query_return unique_ci_query() throws RecognitionException {
        KWQL.unique_ci_query_return retval = new KWQL.unique_ci_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token DISJUNCTION45=null;
        KWQL.unique_ci_query_helper_return s = null;

        KWQL.unique_ci_query_helper_return unique_ci_query_helper44 = null;


        CommonTree DISJUNCTION45_tree=null;
        RewriteRuleTokenStream stream_DISJUNCTION=new RewriteRuleTokenStream(adaptor,"token DISJUNCTION");
        RewriteRuleSubtreeStream stream_unique_ci_query_helper=new RewriteRuleSubtreeStream(adaptor,"rule unique_ci_query_helper");
        try {
            // KWQL.g:55:3: ( ( unique_ci_query_helper -> unique_ci_query_helper ) ( DISJUNCTION s= unique_ci_query_helper -> ^( DISJUNCTION $unique_ci_query $s) )* )
            // KWQL.g:56:3: ( unique_ci_query_helper -> unique_ci_query_helper ) ( DISJUNCTION s= unique_ci_query_helper -> ^( DISJUNCTION $unique_ci_query $s) )*
            {
            // KWQL.g:56:3: ( unique_ci_query_helper -> unique_ci_query_helper )
            // KWQL.g:56:4: unique_ci_query_helper
            {
            pushFollow(FOLLOW_unique_ci_query_helper_in_unique_ci_query436);
            unique_ci_query_helper44=unique_ci_query_helper();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_unique_ci_query_helper.add(unique_ci_query_helper44.getTree());


            // AST REWRITE
            // elements: unique_ci_query_helper
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 56:26: -> unique_ci_query_helper
            {
                adaptor.addChild(root_0, stream_unique_ci_query_helper.nextTree());

            }

            retval.tree = root_0;}
            }

            // KWQL.g:56:52: ( DISJUNCTION s= unique_ci_query_helper -> ^( DISJUNCTION $unique_ci_query $s) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==DISJUNCTION) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // KWQL.g:56:53: DISJUNCTION s= unique_ci_query_helper
            	    {
            	    DISJUNCTION45=(Token)match(input,DISJUNCTION,FOLLOW_DISJUNCTION_in_unique_ci_query442); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_DISJUNCTION.add(DISJUNCTION45);

            	    pushFollow(FOLLOW_unique_ci_query_helper_in_unique_ci_query446);
            	    s=unique_ci_query_helper();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_unique_ci_query_helper.add(s.getTree());


            	    // AST REWRITE
            	    // elements: unique_ci_query, s, DISJUNCTION
            	    // token labels: 
            	    // rule labels: retval, s
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            	    RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

            	    root_0 = (CommonTree)adaptor.nil();
            	    // 56:89: -> ^( DISJUNCTION $unique_ci_query $s)
            	    {
            	        // KWQL.g:56:92: ^( DISJUNCTION $unique_ci_query $s)
            	        {
            	        CommonTree root_1 = (CommonTree)adaptor.nil();
            	        root_1 = (CommonTree)adaptor.becomeRoot(stream_DISJUNCTION.nextNode(), root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        adaptor.addChild(root_1, stream_s.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;}
            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


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
    // $ANTLR end "unique_ci_query"

    public static class unique_ci_query_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unique_ci_query_helper"
    // KWQL.g:58:1: unique_ci_query_helper : ( ci_query | LEFT_PARENTHESIS ( unique_ci_query -> unique_ci_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $unique_ci_query_helper helper_bool ) )? | helper_bool LEFT_PARENTHESIS unique_ci_query RIGHT_PARENTHESIS -> ^( AND unique_ci_query helper_bool ) );
    public final KWQL.unique_ci_query_helper_return unique_ci_query_helper() throws RecognitionException {
        KWQL.unique_ci_query_helper_return retval = new KWQL.unique_ci_query_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS47=null;
        Token RIGHT_PARENTHESIS49=null;
        Token LEFT_PARENTHESIS52=null;
        Token RIGHT_PARENTHESIS54=null;
        KWQL.ci_query_return ci_query46 = null;

        KWQL.unique_ci_query_return unique_ci_query48 = null;

        KWQL.helper_bool_return helper_bool50 = null;

        KWQL.helper_bool_return helper_bool51 = null;

        KWQL.unique_ci_query_return unique_ci_query53 = null;


        CommonTree LEFT_PARENTHESIS47_tree=null;
        CommonTree RIGHT_PARENTHESIS49_tree=null;
        CommonTree LEFT_PARENTHESIS52_tree=null;
        CommonTree RIGHT_PARENTHESIS54_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_helper_bool=new RewriteRuleSubtreeStream(adaptor,"rule helper_bool");
        RewriteRuleSubtreeStream stream_unique_ci_query=new RewriteRuleSubtreeStream(adaptor,"rule unique_ci_query");
        try {
            // KWQL.g:59:3: ( ci_query | LEFT_PARENTHESIS ( unique_ci_query -> unique_ci_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $unique_ci_query_helper helper_bool ) )? | helper_bool LEFT_PARENTHESIS unique_ci_query RIGHT_PARENTHESIS -> ^( AND unique_ci_query helper_bool ) )
            int alt10=3;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // KWQL.g:60:3: ci_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ci_query_in_unique_ci_query_helper474);
                    ci_query46=ci_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ci_query46.getTree());

                    }
                    break;
                case 2 :
                    // KWQL.g:61:4: LEFT_PARENTHESIS ( unique_ci_query -> unique_ci_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $unique_ci_query_helper helper_bool ) )?
                    {
                    LEFT_PARENTHESIS47=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_unique_ci_query_helper479); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS47);

                    // KWQL.g:61:21: ( unique_ci_query -> unique_ci_query )
                    // KWQL.g:61:22: unique_ci_query
                    {
                    pushFollow(FOLLOW_unique_ci_query_in_unique_ci_query_helper482);
                    unique_ci_query48=unique_ci_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unique_ci_query.add(unique_ci_query48.getTree());


                    // AST REWRITE
                    // elements: unique_ci_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 61:37: -> unique_ci_query
                    {
                        adaptor.addChild(root_0, stream_unique_ci_query.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    RIGHT_PARENTHESIS49=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_unique_ci_query_helper487); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS49);

                    // KWQL.g:61:74: ( helper_bool -> ^( AND $unique_ci_query_helper helper_bool ) )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==NEGATION||LA9_0==BOOLEAN||LA9_0==LEFT_PARENTHESIS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // KWQL.g:61:75: helper_bool
                            {
                            pushFollow(FOLLOW_helper_bool_in_unique_ci_query_helper490);
                            helper_bool50=helper_bool();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_helper_bool.add(helper_bool50.getTree());


                            // AST REWRITE
                            // elements: unique_ci_query_helper, helper_bool
                            // token labels: 
                            // rule labels: retval
                            // token list labels: 
                            // rule list labels: 
                            // wildcard labels: 
                            if ( state.backtracking==0 ) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                            root_0 = (CommonTree)adaptor.nil();
                            // 61:86: -> ^( AND $unique_ci_query_helper helper_bool )
                            {
                                // KWQL.g:61:88: ^( AND $unique_ci_query_helper helper_bool )
                                {
                                CommonTree root_1 = (CommonTree)adaptor.nil();
                                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                                adaptor.addChild(root_1, stream_retval.nextTree());
                                adaptor.addChild(root_1, stream_helper_bool.nextTree());

                                adaptor.addChild(root_0, root_1);
                                }

                            }

                            retval.tree = root_0;}
                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // KWQL.g:62:4: helper_bool LEFT_PARENTHESIS unique_ci_query RIGHT_PARENTHESIS
                    {
                    pushFollow(FOLLOW_helper_bool_in_unique_ci_query_helper506);
                    helper_bool51=helper_bool();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_helper_bool.add(helper_bool51.getTree());
                    LEFT_PARENTHESIS52=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_unique_ci_query_helper508); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS52);

                    pushFollow(FOLLOW_unique_ci_query_in_unique_ci_query_helper510);
                    unique_ci_query53=unique_ci_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_unique_ci_query.add(unique_ci_query53.getTree());
                    RIGHT_PARENTHESIS54=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_unique_ci_query_helper512); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS54);



                    // AST REWRITE
                    // elements: unique_ci_query, helper_bool
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 62:67: -> ^( AND unique_ci_query helper_bool )
                    {
                        // KWQL.g:62:70: ^( AND unique_ci_query helper_bool )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                        adaptor.addChild(root_1, stream_unique_ci_query.nextTree());
                        adaptor.addChild(root_1, stream_helper_bool.nextTree());

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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "unique_ci_query_helper"

    public static class selection_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "selection_query"
    // KWQL.g:64:1: selection_query : ( selection_query_helper -> selection_query_helper ) ( connectives s= selection_query_helper -> ^( connectives $selection_query $s) )* ;
    public final KWQL.selection_query_return selection_query() throws RecognitionException {
        KWQL.selection_query_return retval = new KWQL.selection_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQL.selection_query_helper_return s = null;

        KWQL.selection_query_helper_return selection_query_helper55 = null;

        KWQL_KWQLBody.connectives_return connectives56 = null;


        RewriteRuleSubtreeStream stream_connectives=new RewriteRuleSubtreeStream(adaptor,"rule connectives");
        RewriteRuleSubtreeStream stream_selection_query_helper=new RewriteRuleSubtreeStream(adaptor,"rule selection_query_helper");
        try {
            // KWQL.g:65:3: ( ( selection_query_helper -> selection_query_helper ) ( connectives s= selection_query_helper -> ^( connectives $selection_query $s) )* )
            // KWQL.g:66:3: ( selection_query_helper -> selection_query_helper ) ( connectives s= selection_query_helper -> ^( connectives $selection_query $s) )*
            {
            // KWQL.g:66:3: ( selection_query_helper -> selection_query_helper )
            // KWQL.g:66:4: selection_query_helper
            {
            pushFollow(FOLLOW_selection_query_helper_in_selection_query535);
            selection_query_helper55=selection_query_helper();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_selection_query_helper.add(selection_query_helper55.getTree());


            // AST REWRITE
            // elements: selection_query_helper
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 66:26: -> selection_query_helper
            {
                adaptor.addChild(root_0, stream_selection_query_helper.nextTree());

            }

            retval.tree = root_0;}
            }

            // KWQL.g:66:52: ( connectives s= selection_query_helper -> ^( connectives $selection_query $s) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=CI && LA11_0<=TAG)||(LA11_0>=CONJUNCTION && LA11_0<=NEGATION)||LA11_0==BOOLEAN||LA11_0==URI||(LA11_0>=RESQUAL && LA11_0<=QUAL)||LA11_0==INT||LA11_0==DA||LA11_0==KW||LA11_0==LEFT_PARENTHESIS) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // KWQL.g:66:53: connectives s= selection_query_helper
            	    {
            	    pushFollow(FOLLOW_connectives_in_selection_query541);
            	    connectives56=connectives();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_connectives.add(connectives56.getTree());
            	    pushFollow(FOLLOW_selection_query_helper_in_selection_query545);
            	    s=selection_query_helper();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_selection_query_helper.add(s.getTree());


            	    // AST REWRITE
            	    // elements: selection_query, s, connectives
            	    // token labels: 
            	    // rule labels: retval, s
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    if ( state.backtracking==0 ) {
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            	    RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

            	    root_0 = (CommonTree)adaptor.nil();
            	    // 66:89: -> ^( connectives $selection_query $s)
            	    {
            	        // KWQL.g:66:92: ^( connectives $selection_query $s)
            	        {
            	        CommonTree root_1 = (CommonTree)adaptor.nil();
            	        root_1 = (CommonTree)adaptor.becomeRoot(stream_connectives.nextNode(), root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        adaptor.addChild(root_1, stream_s.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;}
            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


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
    // $ANTLR end "selection_query"

    public static class selection_query_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "selection_query_helper"
    // KWQL.g:68:1: selection_query_helper : ( ci_query | LEFT_PARENTHESIS ( selection_query -> selection_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $selection_query_helper helper_bool ) )? | helper_bool LEFT_PARENTHESIS selection_query RIGHT_PARENTHESIS -> ^( AND selection_query helper_bool ) );
    public final KWQL.selection_query_helper_return selection_query_helper() throws RecognitionException {
        KWQL.selection_query_helper_return retval = new KWQL.selection_query_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS58=null;
        Token RIGHT_PARENTHESIS60=null;
        Token LEFT_PARENTHESIS63=null;
        Token RIGHT_PARENTHESIS65=null;
        KWQL.ci_query_return ci_query57 = null;

        KWQL.selection_query_return selection_query59 = null;

        KWQL.helper_bool_return helper_bool61 = null;

        KWQL.helper_bool_return helper_bool62 = null;

        KWQL.selection_query_return selection_query64 = null;


        CommonTree LEFT_PARENTHESIS58_tree=null;
        CommonTree RIGHT_PARENTHESIS60_tree=null;
        CommonTree LEFT_PARENTHESIS63_tree=null;
        CommonTree RIGHT_PARENTHESIS65_tree=null;
        RewriteRuleTokenStream stream_RIGHT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token RIGHT_PARENTHESIS");
        RewriteRuleTokenStream stream_LEFT_PARENTHESIS=new RewriteRuleTokenStream(adaptor,"token LEFT_PARENTHESIS");
        RewriteRuleSubtreeStream stream_helper_bool=new RewriteRuleSubtreeStream(adaptor,"rule helper_bool");
        RewriteRuleSubtreeStream stream_selection_query=new RewriteRuleSubtreeStream(adaptor,"rule selection_query");
        try {
            // KWQL.g:69:3: ( ci_query | LEFT_PARENTHESIS ( selection_query -> selection_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $selection_query_helper helper_bool ) )? | helper_bool LEFT_PARENTHESIS selection_query RIGHT_PARENTHESIS -> ^( AND selection_query helper_bool ) )
            int alt13=3;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // KWQL.g:70:3: ci_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ci_query_in_selection_query_helper571);
                    ci_query57=ci_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ci_query57.getTree());

                    }
                    break;
                case 2 :
                    // KWQL.g:71:4: LEFT_PARENTHESIS ( selection_query -> selection_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $selection_query_helper helper_bool ) )?
                    {
                    LEFT_PARENTHESIS58=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_selection_query_helper576); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS58);

                    // KWQL.g:71:21: ( selection_query -> selection_query )
                    // KWQL.g:71:22: selection_query
                    {
                    pushFollow(FOLLOW_selection_query_in_selection_query_helper579);
                    selection_query59=selection_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_selection_query.add(selection_query59.getTree());


                    // AST REWRITE
                    // elements: selection_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 71:37: -> selection_query
                    {
                        adaptor.addChild(root_0, stream_selection_query.nextTree());

                    }

                    retval.tree = root_0;}
                    }

                    RIGHT_PARENTHESIS60=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_selection_query_helper584); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS60);

                    // KWQL.g:71:74: ( helper_bool -> ^( AND $selection_query_helper helper_bool ) )?
                    int alt12=2;
                    alt12 = dfa12.predict(input);
                    switch (alt12) {
                        case 1 :
                            // KWQL.g:71:75: helper_bool
                            {
                            pushFollow(FOLLOW_helper_bool_in_selection_query_helper587);
                            helper_bool61=helper_bool();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_helper_bool.add(helper_bool61.getTree());


                            // AST REWRITE
                            // elements: helper_bool, selection_query_helper
                            // token labels: 
                            // rule labels: retval
                            // token list labels: 
                            // rule list labels: 
                            // wildcard labels: 
                            if ( state.backtracking==0 ) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                            root_0 = (CommonTree)adaptor.nil();
                            // 71:86: -> ^( AND $selection_query_helper helper_bool )
                            {
                                // KWQL.g:71:88: ^( AND $selection_query_helper helper_bool )
                                {
                                CommonTree root_1 = (CommonTree)adaptor.nil();
                                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                                adaptor.addChild(root_1, stream_retval.nextTree());
                                adaptor.addChild(root_1, stream_helper_bool.nextTree());

                                adaptor.addChild(root_0, root_1);
                                }

                            }

                            retval.tree = root_0;}
                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // KWQL.g:72:4: helper_bool LEFT_PARENTHESIS selection_query RIGHT_PARENTHESIS
                    {
                    pushFollow(FOLLOW_helper_bool_in_selection_query_helper603);
                    helper_bool62=helper_bool();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_helper_bool.add(helper_bool62.getTree());
                    LEFT_PARENTHESIS63=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_selection_query_helper605); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_LEFT_PARENTHESIS.add(LEFT_PARENTHESIS63);

                    pushFollow(FOLLOW_selection_query_in_selection_query_helper607);
                    selection_query64=selection_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_selection_query.add(selection_query64.getTree());
                    RIGHT_PARENTHESIS65=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_selection_query_helper609); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_RIGHT_PARENTHESIS.add(RIGHT_PARENTHESIS65);



                    // AST REWRITE
                    // elements: helper_bool, selection_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 72:67: -> ^( AND selection_query helper_bool )
                    {
                        // KWQL.g:72:70: ^( AND selection_query helper_bool )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(AND, "AND"), root_1);

                        adaptor.addChild(root_1, stream_selection_query.nextTree());
                        adaptor.addChild(root_1, stream_helper_bool.nextTree());

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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "selection_query_helper"

    public static class ci_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ci_query"
    // KWQL.g:74:1: ci_query : ( complex_boolean_query ( conjunctive_connectives )? ci_query_helper ( conjunctive_connectives )? c= complex_boolean_query -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query $c) ) | ci_query_helper ( conjunctive_connectives )? complex_boolean_query -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) ) | complex_boolean_query ( conjunctive_connectives )? ci_query_helper -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) ) | ci_query_helper -> ^( CI_BODY ci_query_helper ) );
    public final KWQL.ci_query_return ci_query() throws RecognitionException {
        KWQL.ci_query_return retval = new KWQL.ci_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQL.complex_boolean_query_return c = null;

        KWQL.complex_boolean_query_return complex_boolean_query66 = null;

        KWQL.conjunctive_connectives_return conjunctive_connectives67 = null;

        KWQL.ci_query_helper_return ci_query_helper68 = null;

        KWQL.conjunctive_connectives_return conjunctive_connectives69 = null;

        KWQL.ci_query_helper_return ci_query_helper70 = null;

        KWQL.conjunctive_connectives_return conjunctive_connectives71 = null;

        KWQL.complex_boolean_query_return complex_boolean_query72 = null;

        KWQL.complex_boolean_query_return complex_boolean_query73 = null;

        KWQL.conjunctive_connectives_return conjunctive_connectives74 = null;

        KWQL.ci_query_helper_return ci_query_helper75 = null;

        KWQL.ci_query_helper_return ci_query_helper76 = null;


        RewriteRuleSubtreeStream stream_conjunctive_connectives=new RewriteRuleSubtreeStream(adaptor,"rule conjunctive_connectives");
        RewriteRuleSubtreeStream stream_complex_boolean_query=new RewriteRuleSubtreeStream(adaptor,"rule complex_boolean_query");
        RewriteRuleSubtreeStream stream_ci_query_helper=new RewriteRuleSubtreeStream(adaptor,"rule ci_query_helper");
        try {
            // KWQL.g:75:3: ( complex_boolean_query ( conjunctive_connectives )? ci_query_helper ( conjunctive_connectives )? c= complex_boolean_query -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query $c) ) | ci_query_helper ( conjunctive_connectives )? complex_boolean_query -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) ) | complex_boolean_query ( conjunctive_connectives )? ci_query_helper -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) ) | ci_query_helper -> ^( CI_BODY ci_query_helper ) )
            int alt18=4;
            alt18 = dfa18.predict(input);
            switch (alt18) {
                case 1 :
                    // KWQL.g:76:3: complex_boolean_query ( conjunctive_connectives )? ci_query_helper ( conjunctive_connectives )? c= complex_boolean_query
                    {
                    pushFollow(FOLLOW_complex_boolean_query_in_ci_query633);
                    complex_boolean_query66=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_boolean_query.add(complex_boolean_query66.getTree());
                    // KWQL.g:76:25: ( conjunctive_connectives )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==CONJUNCTION) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // KWQL.g:0:0: conjunctive_connectives
                            {
                            pushFollow(FOLLOW_conjunctive_connectives_in_ci_query635);
                            conjunctive_connectives67=conjunctive_connectives();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_conjunctive_connectives.add(conjunctive_connectives67.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_ci_query_helper_in_ci_query638);
                    ci_query_helper68=ci_query_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_query_helper.add(ci_query_helper68.getTree());
                    // KWQL.g:76:66: ( conjunctive_connectives )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==CONJUNCTION) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // KWQL.g:0:0: conjunctive_connectives
                            {
                            pushFollow(FOLLOW_conjunctive_connectives_in_ci_query640);
                            conjunctive_connectives69=conjunctive_connectives();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_conjunctive_connectives.add(conjunctive_connectives69.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_complex_boolean_query_in_ci_query645);
                    c=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_boolean_query.add(c.getTree());


                    // AST REWRITE
                    // elements: complex_boolean_query, ci_query_helper, c
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
                    // 76:114: -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query $c) )
                    {
                        // KWQL.g:76:116: ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query $c) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_COMP, "CI_COMP"), root_1);

                        // KWQL.g:76:126: ^( CI_BODY ci_query_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_BODY, "CI_BODY"), root_2);

                        adaptor.addChild(root_2, stream_ci_query_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:76:153: ^( BOOL_BODY complex_boolean_query $c)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOOL_BODY, "BOOL_BODY"), root_2);

                        adaptor.addChild(root_2, stream_complex_boolean_query.nextTree());
                        adaptor.addChild(root_2, stream_c.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // KWQL.g:77:4: ci_query_helper ( conjunctive_connectives )? complex_boolean_query
                    {
                    pushFollow(FOLLOW_ci_query_helper_in_ci_query669);
                    ci_query_helper70=ci_query_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_query_helper.add(ci_query_helper70.getTree());
                    // KWQL.g:77:20: ( conjunctive_connectives )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==CONJUNCTION) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // KWQL.g:0:0: conjunctive_connectives
                            {
                            pushFollow(FOLLOW_conjunctive_connectives_in_ci_query671);
                            conjunctive_connectives71=conjunctive_connectives();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_conjunctive_connectives.add(conjunctive_connectives71.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_complex_boolean_query_in_ci_query674);
                    complex_boolean_query72=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_boolean_query.add(complex_boolean_query72.getTree());


                    // AST REWRITE
                    // elements: complex_boolean_query, ci_query_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 77:67: -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) )
                    {
                        // KWQL.g:77:69: ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_COMP, "CI_COMP"), root_1);

                        // KWQL.g:77:79: ^( CI_BODY ci_query_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_BODY, "CI_BODY"), root_2);

                        adaptor.addChild(root_2, stream_ci_query_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:77:106: ^( BOOL_BODY complex_boolean_query )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOOL_BODY, "BOOL_BODY"), root_2);

                        adaptor.addChild(root_2, stream_complex_boolean_query.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // KWQL.g:78:5: complex_boolean_query ( conjunctive_connectives )? ci_query_helper
                    {
                    pushFollow(FOLLOW_complex_boolean_query_in_ci_query697);
                    complex_boolean_query73=complex_boolean_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_complex_boolean_query.add(complex_boolean_query73.getTree());
                    // KWQL.g:78:27: ( conjunctive_connectives )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==CONJUNCTION) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // KWQL.g:0:0: conjunctive_connectives
                            {
                            pushFollow(FOLLOW_conjunctive_connectives_in_ci_query699);
                            conjunctive_connectives74=conjunctive_connectives();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_conjunctive_connectives.add(conjunctive_connectives74.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_ci_query_helper_in_ci_query702);
                    ci_query_helper75=ci_query_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_query_helper.add(ci_query_helper75.getTree());


                    // AST REWRITE
                    // elements: complex_boolean_query, ci_query_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 78:68: -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) )
                    {
                        // KWQL.g:78:70: ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_COMP, "CI_COMP"), root_1);

                        // KWQL.g:78:80: ^( CI_BODY ci_query_helper )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_BODY, "CI_BODY"), root_2);

                        adaptor.addChild(root_2, stream_ci_query_helper.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // KWQL.g:78:107: ^( BOOL_BODY complex_boolean_query )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOOL_BODY, "BOOL_BODY"), root_2);

                        adaptor.addChild(root_2, stream_complex_boolean_query.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // KWQL.g:79:4: ci_query_helper
                    {
                    pushFollow(FOLLOW_ci_query_helper_in_ci_query724);
                    ci_query_helper76=ci_query_helper();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_ci_query_helper.add(ci_query_helper76.getTree());


                    // AST REWRITE
                    // elements: ci_query_helper
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 79:19: -> ^( CI_BODY ci_query_helper )
                    {
                        // KWQL.g:79:21: ^( CI_BODY ci_query_helper )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CI_BODY, "CI_BODY"), root_1);

                        adaptor.addChild(root_1, stream_ci_query_helper.nextTree());

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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ci_query"

    public static class ci_query_helper_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ci_query_helper"
    // KWQL.g:81:1: ci_query_helper : ( LEFT_PARENTHESIS ci_body RIGHT_PARENTHESIS | ci_body );
    public final KWQL.ci_query_helper_return ci_query_helper() throws RecognitionException {
        KWQL.ci_query_helper_return retval = new KWQL.ci_query_helper_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token LEFT_PARENTHESIS77=null;
        Token RIGHT_PARENTHESIS79=null;
        KWQL.ci_body_return ci_body78 = null;

        KWQL.ci_body_return ci_body80 = null;


        CommonTree LEFT_PARENTHESIS77_tree=null;
        CommonTree RIGHT_PARENTHESIS79_tree=null;

        try {
            // KWQL.g:82:3: ( LEFT_PARENTHESIS ci_body RIGHT_PARENTHESIS | ci_body )
            int alt19=2;
            alt19 = dfa19.predict(input);
            switch (alt19) {
                case 1 :
                    // KWQL.g:83:3: LEFT_PARENTHESIS ci_body RIGHT_PARENTHESIS
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    LEFT_PARENTHESIS77=(Token)match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_ci_query_helper746); if (state.failed) return retval;
                    pushFollow(FOLLOW_ci_body_in_ci_query_helper749);
                    ci_body78=ci_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ci_body78.getTree());
                    RIGHT_PARENTHESIS79=(Token)match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_ci_query_helper751); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // KWQL.g:84:4: ci_body
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_ci_body_in_ci_query_helper757);
                    ci_body80=ci_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ci_body80.getTree());

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
    // $ANTLR end "ci_query_helper"

    public static class conjunctive_connectives_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conjunctive_connectives"
    // KWQL.g:86:1: conjunctive_connectives : ( CONJUNCTION ->) ;
    public final KWQL.conjunctive_connectives_return conjunctive_connectives() throws RecognitionException {
        KWQL.conjunctive_connectives_return retval = new KWQL.conjunctive_connectives_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token CONJUNCTION81=null;

        CommonTree CONJUNCTION81_tree=null;
        RewriteRuleTokenStream stream_CONJUNCTION=new RewriteRuleTokenStream(adaptor,"token CONJUNCTION");

        try {
            // KWQL.g:87:3: ( ( CONJUNCTION ->) )
            // KWQL.g:88:3: ( CONJUNCTION ->)
            {
            // KWQL.g:88:3: ( CONJUNCTION ->)
            // KWQL.g:88:4: CONJUNCTION
            {
            CONJUNCTION81=(Token)match(input,CONJUNCTION,FOLLOW_CONJUNCTION_in_conjunctive_connectives777); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_CONJUNCTION.add(CONJUNCTION81);



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
            // 88:15: ->
            {
                root_0 = null;
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
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "conjunctive_connectives"

    public static class ci_body_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ci_body"
    // KWQL.g:90:1: ci_body : body -> body ;
    public final KWQL.ci_body_return ci_body() throws RecognitionException {
        KWQL.ci_body_return retval = new KWQL.ci_body_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQL_KWQLBody.body_return body82 = null;


        RewriteRuleSubtreeStream stream_body=new RewriteRuleSubtreeStream(adaptor,"rule body");
        try {
            // KWQL.g:91:3: ( body -> body )
            // KWQL.g:92:3: body
            {
            pushFollow(FOLLOW_body_in_ci_body792);
            body82=body();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_body.add(body82.getTree());


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
            // 92:7: -> body
            {
                adaptor.addChild(root_0, stream_body.nextTree());

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
    // $ANTLR end "ci_body"

    public static class helper_bool_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "helper_bool"
    // KWQL.g:94:1: helper_bool : complex_boolean_query -> ^( BOOL_BODY complex_boolean_query ) ;
    public final KWQL.helper_bool_return helper_bool() throws RecognitionException {
        KWQL.helper_bool_return retval = new KWQL.helper_bool_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        KWQL.complex_boolean_query_return complex_boolean_query83 = null;


        RewriteRuleSubtreeStream stream_complex_boolean_query=new RewriteRuleSubtreeStream(adaptor,"rule complex_boolean_query");
        try {
            // KWQL.g:95:2: ( complex_boolean_query -> ^( BOOL_BODY complex_boolean_query ) )
            // KWQL.g:95:4: complex_boolean_query
            {
            pushFollow(FOLLOW_complex_boolean_query_in_helper_bool805);
            complex_boolean_query83=complex_boolean_query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_complex_boolean_query.add(complex_boolean_query83.getTree());


            // AST REWRITE
            // elements: complex_boolean_query
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 95:25: -> ^( BOOL_BODY complex_boolean_query )
            {
                // KWQL.g:95:27: ^( BOOL_BODY complex_boolean_query )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOOL_BODY, "BOOL_BODY"), root_1);

                adaptor.addChild(root_1, stream_complex_boolean_query.nextTree());

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
    // $ANTLR end "helper_bool"

    // $ANTLR start synpred1_KWQL
    public final void synpred1_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:22:3: ( head AT complex_boolean_query EOF )
        // KWQL.g:22:3: head AT complex_boolean_query EOF
        {
        pushFollow(FOLLOW_head_in_synpred1_KWQL105);
        head();

        state._fsp--;
        if (state.failed) return ;
        match(input,AT,FOLLOW_AT_in_synpred1_KWQL107); if (state.failed) return ;
        pushFollow(FOLLOW_complex_boolean_query_in_synpred1_KWQL109);
        complex_boolean_query();

        state._fsp--;
        if (state.failed) return ;
        match(input,EOF,FOLLOW_EOF_in_synpred1_KWQL111); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_KWQL

    // $ANTLR start synpred2_KWQL
    public final void synpred2_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:23:4: ( head AT unique_ci_query EOF )
        // KWQL.g:23:4: head AT unique_ci_query EOF
        {
        pushFollow(FOLLOW_head_in_synpred2_KWQL134);
        head();

        state._fsp--;
        if (state.failed) return ;
        match(input,AT,FOLLOW_AT_in_synpred2_KWQL136); if (state.failed) return ;
        pushFollow(FOLLOW_unique_ci_query_in_synpred2_KWQL138);
        unique_ci_query();

        state._fsp--;
        if (state.failed) return ;
        match(input,EOF,FOLLOW_EOF_in_synpred2_KWQL140); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_KWQL

    // $ANTLR start synpred3_KWQL
    public final void synpred3_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:24:4: ( unique_ci_query EOF )
        // KWQL.g:24:4: unique_ci_query EOF
        {
        pushFollow(FOLLOW_unique_ci_query_in_synpred3_KWQL163);
        unique_ci_query();

        state._fsp--;
        if (state.failed) return ;
        match(input,EOF,FOLLOW_EOF_in_synpred3_KWQL165); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred3_KWQL

    // $ANTLR start synpred5_KWQL
    public final void synpred5_KWQL_fragment() throws RecognitionException {   
        KWQL.global_body_helper_return v = null;


        // KWQL.g:30:3: ( ( global_body_helper ) ( connectives v= global_body_helper )* )
        // KWQL.g:30:3: ( global_body_helper ) ( connectives v= global_body_helper )*
        {
        // KWQL.g:30:3: ( global_body_helper )
        // KWQL.g:30:4: global_body_helper
        {
        pushFollow(FOLLOW_global_body_helper_in_synpred5_KWQL226);
        global_body_helper();

        state._fsp--;
        if (state.failed) return ;

        }

        // KWQL.g:30:44: ( connectives v= global_body_helper )*
        loop20:
        do {
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( ((LA20_0>=CI && LA20_0<=TAG)||(LA20_0>=CONJUNCTION && LA20_0<=NEGATION)||LA20_0==BOOLEAN||LA20_0==URI||(LA20_0>=RESQUAL && LA20_0<=QUAL)||LA20_0==INT||LA20_0==DA||LA20_0==KW||LA20_0==LEFT_PARENTHESIS) ) {
                alt20=1;
            }


            switch (alt20) {
        	case 1 :
        	    // KWQL.g:30:45: connectives v= global_body_helper
        	    {
        	    pushFollow(FOLLOW_connectives_in_synpred5_KWQL232);
        	    connectives();

        	    state._fsp--;
        	    if (state.failed) return ;
        	    pushFollow(FOLLOW_global_body_helper_in_synpred5_KWQL236);
        	    v=global_body_helper();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop20;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred5_KWQL

    // $ANTLR start synpred6_KWQL
    public final void synpred6_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:35:3: ( body )
        // KWQL.g:35:3: body
        {
        pushFollow(FOLLOW_body_in_synpred6_KWQL281);
        body();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred6_KWQL

    // $ANTLR start synpred7_KWQL
    public final void synpred7_KWQL_fragment() throws RecognitionException {   
        KWQL.complex_boolean_query_helper_return v = null;


        // KWQL.g:40:65: ( connectives v= complex_boolean_query_helper )
        // KWQL.g:40:65: connectives v= complex_boolean_query_helper
        {
        pushFollow(FOLLOW_connectives_in_synpred7_KWQL314);
        connectives();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_complex_boolean_query_helper_in_synpred7_KWQL318);
        v=complex_boolean_query_helper();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred7_KWQL

    // $ANTLR start synpred12_KWQL
    public final void synpred12_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:60:3: ( ci_query )
        // KWQL.g:60:3: ci_query
        {
        pushFollow(FOLLOW_ci_query_in_synpred12_KWQL474);
        ci_query();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_KWQL

    // $ANTLR start synpred14_KWQL
    public final void synpred14_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:61:4: ( LEFT_PARENTHESIS ( unique_ci_query ) RIGHT_PARENTHESIS ( helper_bool )? )
        // KWQL.g:61:4: LEFT_PARENTHESIS ( unique_ci_query ) RIGHT_PARENTHESIS ( helper_bool )?
        {
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred14_KWQL479); if (state.failed) return ;
        // KWQL.g:61:21: ( unique_ci_query )
        // KWQL.g:61:22: unique_ci_query
        {
        pushFollow(FOLLOW_unique_ci_query_in_synpred14_KWQL482);
        unique_ci_query();

        state._fsp--;
        if (state.failed) return ;

        }

        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred14_KWQL487); if (state.failed) return ;
        // KWQL.g:61:74: ( helper_bool )?
        int alt21=2;
        int LA21_0 = input.LA(1);

        if ( (LA21_0==NEGATION||LA21_0==BOOLEAN||LA21_0==LEFT_PARENTHESIS) ) {
            alt21=1;
        }
        switch (alt21) {
            case 1 :
                // KWQL.g:61:75: helper_bool
                {
                pushFollow(FOLLOW_helper_bool_in_synpred14_KWQL490);
                helper_bool();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred14_KWQL

    // $ANTLR start synpred16_KWQL
    public final void synpred16_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:70:3: ( ci_query )
        // KWQL.g:70:3: ci_query
        {
        pushFollow(FOLLOW_ci_query_in_synpred16_KWQL571);
        ci_query();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred16_KWQL

    // $ANTLR start synpred17_KWQL
    public final void synpred17_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:71:75: ( helper_bool )
        // KWQL.g:71:75: helper_bool
        {
        pushFollow(FOLLOW_helper_bool_in_synpred17_KWQL587);
        helper_bool();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred17_KWQL

    // $ANTLR start synpred18_KWQL
    public final void synpred18_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:71:4: ( LEFT_PARENTHESIS ( selection_query ) RIGHT_PARENTHESIS ( helper_bool )? )
        // KWQL.g:71:4: LEFT_PARENTHESIS ( selection_query ) RIGHT_PARENTHESIS ( helper_bool )?
        {
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred18_KWQL576); if (state.failed) return ;
        // KWQL.g:71:21: ( selection_query )
        // KWQL.g:71:22: selection_query
        {
        pushFollow(FOLLOW_selection_query_in_synpred18_KWQL579);
        selection_query();

        state._fsp--;
        if (state.failed) return ;

        }

        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred18_KWQL584); if (state.failed) return ;
        // KWQL.g:71:74: ( helper_bool )?
        int alt22=2;
        int LA22_0 = input.LA(1);

        if ( (LA22_0==NEGATION||LA22_0==BOOLEAN||LA22_0==LEFT_PARENTHESIS) ) {
            alt22=1;
        }
        switch (alt22) {
            case 1 :
                // KWQL.g:71:75: helper_bool
                {
                pushFollow(FOLLOW_helper_bool_in_synpred18_KWQL587);
                helper_bool();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        }
    }
    // $ANTLR end synpred18_KWQL

    // $ANTLR start synpred21_KWQL
    public final void synpred21_KWQL_fragment() throws RecognitionException {   
        KWQL.complex_boolean_query_return c = null;


        // KWQL.g:76:3: ( complex_boolean_query ( conjunctive_connectives )? ci_query_helper ( conjunctive_connectives )? c= complex_boolean_query )
        // KWQL.g:76:3: complex_boolean_query ( conjunctive_connectives )? ci_query_helper ( conjunctive_connectives )? c= complex_boolean_query
        {
        pushFollow(FOLLOW_complex_boolean_query_in_synpred21_KWQL633);
        complex_boolean_query();

        state._fsp--;
        if (state.failed) return ;
        // KWQL.g:76:25: ( conjunctive_connectives )?
        int alt23=2;
        int LA23_0 = input.LA(1);

        if ( (LA23_0==CONJUNCTION) ) {
            alt23=1;
        }
        switch (alt23) {
            case 1 :
                // KWQL.g:0:0: conjunctive_connectives
                {
                pushFollow(FOLLOW_conjunctive_connectives_in_synpred21_KWQL635);
                conjunctive_connectives();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_ci_query_helper_in_synpred21_KWQL638);
        ci_query_helper();

        state._fsp--;
        if (state.failed) return ;
        // KWQL.g:76:66: ( conjunctive_connectives )?
        int alt24=2;
        int LA24_0 = input.LA(1);

        if ( (LA24_0==CONJUNCTION) ) {
            alt24=1;
        }
        switch (alt24) {
            case 1 :
                // KWQL.g:0:0: conjunctive_connectives
                {
                pushFollow(FOLLOW_conjunctive_connectives_in_synpred21_KWQL640);
                conjunctive_connectives();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_complex_boolean_query_in_synpred21_KWQL645);
        c=complex_boolean_query();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred21_KWQL

    // $ANTLR start synpred23_KWQL
    public final void synpred23_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:77:4: ( ci_query_helper ( conjunctive_connectives )? complex_boolean_query )
        // KWQL.g:77:4: ci_query_helper ( conjunctive_connectives )? complex_boolean_query
        {
        pushFollow(FOLLOW_ci_query_helper_in_synpred23_KWQL669);
        ci_query_helper();

        state._fsp--;
        if (state.failed) return ;
        // KWQL.g:77:20: ( conjunctive_connectives )?
        int alt25=2;
        int LA25_0 = input.LA(1);

        if ( (LA25_0==CONJUNCTION) ) {
            alt25=1;
        }
        switch (alt25) {
            case 1 :
                // KWQL.g:0:0: conjunctive_connectives
                {
                pushFollow(FOLLOW_conjunctive_connectives_in_synpred23_KWQL671);
                conjunctive_connectives();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_complex_boolean_query_in_synpred23_KWQL674);
        complex_boolean_query();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_KWQL

    // $ANTLR start synpred25_KWQL
    public final void synpred25_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:78:5: ( complex_boolean_query ( conjunctive_connectives )? ci_query_helper )
        // KWQL.g:78:5: complex_boolean_query ( conjunctive_connectives )? ci_query_helper
        {
        pushFollow(FOLLOW_complex_boolean_query_in_synpred25_KWQL697);
        complex_boolean_query();

        state._fsp--;
        if (state.failed) return ;
        // KWQL.g:78:27: ( conjunctive_connectives )?
        int alt26=2;
        int LA26_0 = input.LA(1);

        if ( (LA26_0==CONJUNCTION) ) {
            alt26=1;
        }
        switch (alt26) {
            case 1 :
                // KWQL.g:0:0: conjunctive_connectives
                {
                pushFollow(FOLLOW_conjunctive_connectives_in_synpred25_KWQL699);
                conjunctive_connectives();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_ci_query_helper_in_synpred25_KWQL702);
        ci_query_helper();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred25_KWQL

    // $ANTLR start synpred26_KWQL
    public final void synpred26_KWQL_fragment() throws RecognitionException {   
        // KWQL.g:83:3: ( LEFT_PARENTHESIS ci_body RIGHT_PARENTHESIS )
        // KWQL.g:83:3: LEFT_PARENTHESIS ci_body RIGHT_PARENTHESIS
        {
        match(input,LEFT_PARENTHESIS,FOLLOW_LEFT_PARENTHESIS_in_synpred26_KWQL746); if (state.failed) return ;
        pushFollow(FOLLOW_ci_body_in_synpred26_KWQL749);
        ci_body();

        state._fsp--;
        if (state.failed) return ;
        match(input,RIGHT_PARENTHESIS,FOLLOW_RIGHT_PARENTHESIS_in_synpred26_KWQL751); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred26_KWQL

    // Delegated rules
    public KWQL_KWQLBody.opt_return opt() throws RecognitionException { return gKWQLBody.opt(); }
    public KWQL_KWQLBody.subquery_term_return subquery_term() throws RecognitionException { return gKWQLBody.subquery_term(); }
    public KWQL_KWQLBody.optional_return optional() throws RecognitionException { return gKWQLBody.optional(); }
    public KWQL_KWQLBody.unbound_contentterm_helper_return unbound_contentterm_helper() throws RecognitionException { return gKWQLBody.unbound_contentterm_helper(); }
    public KWQL_KWQLBody.expanded_resource_return expanded_resource() throws RecognitionException { return gKWQLBody.expanded_resource(); }
    public KWQL_KWQLBody.complex_qualifierterm_return complex_qualifierterm() throws RecognitionException { return gKWQLBody.complex_qualifierterm(); }
    public KWQL_KWQLBody.non_ci_resource_return non_ci_resource() throws RecognitionException { return gKWQLBody.non_ci_resource(); }
    public KWQL_KWQLBody.non_ci_resourceterm_return non_ci_resourceterm() throws RecognitionException { return gKWQLBody.non_ci_resourceterm(); }
    public KWQL_KWQLHead.q_cvalue_return q_cvalue() throws RecognitionException { return gKWQLHead.q_cvalue(); }
    public KWQL_KWQLHead.resource_return resource() throws RecognitionException { return gKWQLHead.resource(); }
    public KWQL_KWQLHead.c_resource_content_return c_resource_content() throws RecognitionException { return gKWQLHead.c_resource_content(); }
    public KWQL_KWQLHead.q_c_qualifier_return q_c_qualifier() throws RecognitionException { return gKWQLHead.q_c_qualifier(); }
    public KWQL_KWQLBody.contentterm_return contentterm() throws RecognitionException { return gKWQLBody.contentterm(); }
    public KWQL_KWQLBody.res_return res() throws RecognitionException { return gKWQLBody.res(); }
    public KWQL_KWQLBody.connective_return connective() throws RecognitionException { return gKWQLBody.connective(); }
    public KWQL_KWQLBody.resource_term_return resource_term() throws RecognitionException { return gKWQLBody.resource_term(); }
    public KWQL_KWQLBody.connectives_return connectives() throws RecognitionException { return gKWQLBody.connectives(); }
    public KWQL_KWQLHead.conjunction_return conjunction() throws RecognitionException { return gKWQLHead.conjunction(); }
    public KWQL_KWQLBody.qualifierterm_return qualifierterm() throws RecognitionException { return gKWQLBody.qualifierterm(); }
    public KWQL_KWQLBody.complex_valueterm_return complex_valueterm() throws RecognitionException { return gKWQLBody.complex_valueterm(); }
    public KWQL_KWQLBody.body_return body() throws RecognitionException { return gKWQLBody.body(); }
    public KWQL_KWQLHead.c_resource_term_return c_resource_term() throws RecognitionException { return gKWQLHead.c_resource_term(); }
    public KWQL_KWQLBody.ci_resourceterm_return ci_resourceterm() throws RecognitionException { return gKWQLBody.ci_resourceterm(); }
    public KWQL_KWQLBody.complex_contentterm_helper_return complex_contentterm_helper() throws RecognitionException { return gKWQLBody.complex_contentterm_helper(); }
    public KWQL_KWQLBody.valueterm_return valueterm() throws RecognitionException { return gKWQLBody.valueterm(); }
    public KWQL_KWQLHead.cvalue_return cvalue() throws RecognitionException { return gKWQLHead.cvalue(); }
    public KWQL_KWQLHead.q_c_resource_term_return q_c_resource_term() throws RecognitionException { return gKWQLHead.q_c_resource_term(); }
    public KWQL_KWQLBody.unbound_contentterm_return unbound_contentterm() throws RecognitionException { return gKWQLBody.unbound_contentterm(); }
    public KWQL_KWQLHead.head_return head() throws RecognitionException { return gKWQLHead.head(); }
    public KWQL_KWQLBody.atomar_value_return atomar_value() throws RecognitionException { return gKWQLBody.atomar_value(); }
    public KWQL_KWQLBody.complex_contentterm_return complex_contentterm() throws RecognitionException { return gKWQLBody.complex_contentterm(); }
    public KWQL_KWQLBody.resource_term_helper_return resource_term_helper() throws RecognitionException { return gKWQLBody.resource_term_helper(); }
    public KWQL_KWQLHead.q_c_resource_content_return q_c_resource_content() throws RecognitionException { return gKWQLHead.q_c_resource_content(); }
    public KWQL_KWQLBody.unbound_complex_contentterm_helper_return unbound_complex_contentterm_helper() throws RecognitionException { return gKWQLBody.unbound_complex_contentterm_helper(); }
    public KWQL_KWQLHead.c_qualifier_return c_qualifier() throws RecognitionException { return gKWQLHead.c_qualifier(); }
    public KWQL_KWQLBody.unbound_complex_contentterm_return unbound_complex_contentterm() throws RecognitionException { return gKWQLBody.unbound_complex_contentterm(); }
    public KWQL_KWQLHead.c_resource_content_helper_return c_resource_content_helper() throws RecognitionException { return gKWQLHead.c_resource_content_helper(); }
    public KWQL_KWQLBody.ci_resource_return ci_resource() throws RecognitionException { return gKWQLBody.ci_resource(); }

    public final boolean synpred3_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred21_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred21_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred23_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred23_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred7_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred7_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred26_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred26_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred16_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred16_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred14_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred14_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred18_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred18_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred25_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred25_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred6_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred6_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred17_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred17_KWQL_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_KWQL() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_KWQL_fragment(); // can never throw exception
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
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA5 dfa5 = new DFA5(this);
    protected DFA10 dfa10 = new DFA10(this);
    protected DFA13 dfa13 = new DFA13(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA18 dfa18 = new DFA18(this);
    protected DFA19 dfa19 = new DFA19(this);
    static final String DFA1_eotS =
        "\20\uffff";
    static final String DFA1_eofS =
        "\20\uffff";
    static final String DFA1_minS =
        "\1\4\2\0\11\uffff\1\0\3\uffff";
    static final String DFA1_maxS =
        "\1\45\2\0\11\uffff\1\0\3\uffff";
    static final String DFA1_acceptS =
        "\3\uffff\1\3\11\uffff\1\1\1\2\1\4";
    static final String DFA1_specialS =
        "\1\uffff\1\0\1\1\11\uffff\1\2\3\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\14\3\1\3\uffff\1\3\1\2\1\3\5\uffff\1\3\6\uffff\2\3\1\uffff"+
            "\1\3\1\uffff\1\3\2\uffff\1\3\2\uffff\1\3",
            "\1\uffff",
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
            "\1\uffff",
            "",
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
            return "20:1: kwqlrule : ( head AT complex_boolean_query EOF -> ^( RULE ^( HEAD head ) ^( BOOL_QUERY complex_boolean_query ) ) | head AT unique_ci_query EOF -> ^( RULE ^( HEAD head ) ^( CI_QUERY unique_ci_query ) ) | unique_ci_query EOF -> ^( RULE ^( HEAD ) ^( CI_QUERY unique_ci_query ) ) | head AT selection_query EOF -> ^( RULE ^( HEAD head ) ^( SEL_QUERY selection_query ) ) );";
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
                        if ( (synpred1_KWQL()) ) {s = 13;}

                        else if ( (synpred2_KWQL()) ) {s = 14;}

                        else if ( (synpred3_KWQL()) ) {s = 3;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index1_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA1_2 = input.LA(1);

                         
                        int index1_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQL()) ) {s = 13;}

                        else if ( (synpred2_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index1_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA1_12 = input.LA(1);

                         
                        int index1_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_KWQL()) ) {s = 13;}

                        else if ( (synpred2_KWQL()) ) {s = 14;}

                        else if ( (synpred3_KWQL()) ) {s = 3;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index1_12);
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
        "\1\4\1\0\12\uffff";
    static final String DFA3_maxS =
        "\1\45\1\0\12\uffff";
    static final String DFA3_acceptS =
        "\2\uffff\1\1\10\uffff\1\2";
    static final String DFA3_specialS =
        "\1\uffff\1\0\12\uffff}>";
    static final String[] DFA3_transitionS = {
            "\4\2\3\uffff\1\1\7\uffff\1\2\6\uffff\2\2\1\uffff\1\2\1\uffff"+
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
            return "28:1: global_body : ( ( global_body_helper -> global_body_helper ) ( connectives v= global_body_helper -> ^( connectives $global_body $v) )* | NEGATION LEFT_PARENTHESIS resource_term_helper RIGHT_PARENTHESIS -> ^( NEGATION resource_term_helper ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_1 = input.LA(1);

                         
                        int index3_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred5_KWQL()) ) {s = 2;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index3_1);
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
    static final String DFA4_eotS =
        "\14\uffff";
    static final String DFA4_eofS =
        "\14\uffff";
    static final String DFA4_minS =
        "\1\4\3\uffff\1\0\7\uffff";
    static final String DFA4_maxS =
        "\1\45\3\uffff\1\0\7\uffff";
    static final String DFA4_acceptS =
        "\1\uffff\1\1\11\uffff\1\2";
    static final String DFA4_specialS =
        "\4\uffff\1\0\7\uffff}>";
    static final String[] DFA4_transitionS = {
            "\4\1\3\uffff\1\1\7\uffff\1\1\6\uffff\2\1\1\uffff\1\1\1\uffff"+
            "\1\1\2\uffff\1\1\2\uffff\1\4",
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

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "33:1: global_body_helper : ( body | LEFT_PARENTHESIS global_body RIGHT_PARENTHESIS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA4_4 = input.LA(1);

                         
                        int index4_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred6_KWQL()) ) {s = 1;}

                        else if ( (true) ) {s = 11;}

                         
                        input.seek(index4_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 4, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA5_eotS =
        "\21\uffff";
    static final String DFA5_eofS =
        "\1\1\20\uffff";
    static final String DFA5_minS =
        "\1\4\2\uffff\3\0\10\uffff\2\0\1\uffff";
    static final String DFA5_maxS =
        "\1\46\2\uffff\3\0\10\uffff\2\0\1\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\2\16\uffff\1\1";
    static final String DFA5_specialS =
        "\3\uffff\1\0\1\1\1\2\10\uffff\1\3\1\4\1\uffff}>";
    static final String[] DFA5_transitionS = {
            "\4\1\1\uffff\1\3\1\16\1\5\1\uffff\1\17\5\uffff\1\1\6\uffff\2"+
            "\1\1\uffff\1\1\1\uffff\1\1\2\uffff\1\1\2\uffff\1\4\1\1",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "()* loopback of 40:64: ( connectives v= complex_boolean_query_helper -> ^( connectives $complex_boolean_query $v) )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA5_3 = input.LA(1);

                         
                        int index5_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_KWQL()) ) {s = 16;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index5_3);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA5_4 = input.LA(1);

                         
                        int index5_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_KWQL()) ) {s = 16;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index5_4);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA5_5 = input.LA(1);

                         
                        int index5_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_KWQL()) ) {s = 16;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index5_5);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA5_14 = input.LA(1);

                         
                        int index5_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_KWQL()) ) {s = 16;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index5_14);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA5_15 = input.LA(1);

                         
                        int index5_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred7_KWQL()) ) {s = 16;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index5_15);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 5, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA10_eotS =
        "\16\uffff";
    static final String DFA10_eofS =
        "\16\uffff";
    static final String DFA10_minS =
        "\1\4\3\0\12\uffff";
    static final String DFA10_maxS =
        "\1\45\3\0\12\uffff";
    static final String DFA10_acceptS =
        "\4\uffff\1\1\7\uffff\1\3\1\2";
    static final String DFA10_specialS =
        "\1\uffff\1\0\1\1\1\2\12\uffff}>";
    static final String[] DFA10_transitionS = {
            "\4\4\3\uffff\1\2\1\uffff\1\1\5\uffff\1\4\6\uffff\2\4\1\uffff"+
            "\1\4\1\uffff\1\4\2\uffff\1\4\2\uffff\1\3",
            "\1\uffff",
            "\1\uffff",
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
            return "58:1: unique_ci_query_helper : ( ci_query | LEFT_PARENTHESIS ( unique_ci_query -> unique_ci_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $unique_ci_query_helper helper_bool ) )? | helper_bool LEFT_PARENTHESIS unique_ci_query RIGHT_PARENTHESIS -> ^( AND unique_ci_query helper_bool ) );";
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
                        if ( (synpred12_KWQL()) ) {s = 4;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index10_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA10_2 = input.LA(1);

                         
                        int index10_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQL()) ) {s = 4;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index10_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA10_3 = input.LA(1);

                         
                        int index10_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_KWQL()) ) {s = 4;}

                        else if ( (synpred14_KWQL()) ) {s = 13;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index10_3);
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
    static final String DFA13_eotS =
        "\16\uffff";
    static final String DFA13_eofS =
        "\16\uffff";
    static final String DFA13_minS =
        "\1\4\3\0\12\uffff";
    static final String DFA13_maxS =
        "\1\45\3\0\12\uffff";
    static final String DFA13_acceptS =
        "\4\uffff\1\1\7\uffff\1\3\1\2";
    static final String DFA13_specialS =
        "\1\uffff\1\0\1\1\1\2\12\uffff}>";
    static final String[] DFA13_transitionS = {
            "\4\4\3\uffff\1\2\1\uffff\1\1\5\uffff\1\4\6\uffff\2\4\1\uffff"+
            "\1\4\1\uffff\1\4\2\uffff\1\4\2\uffff\1\3",
            "\1\uffff",
            "\1\uffff",
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
            return "68:1: selection_query_helper : ( ci_query | LEFT_PARENTHESIS ( selection_query -> selection_query ) RIGHT_PARENTHESIS ( helper_bool -> ^( AND $selection_query_helper helper_bool ) )? | helper_bool LEFT_PARENTHESIS selection_query RIGHT_PARENTHESIS -> ^( AND selection_query helper_bool ) );";
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
                        if ( (synpred16_KWQL()) ) {s = 4;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index13_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA13_2 = input.LA(1);

                         
                        int index13_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred16_KWQL()) ) {s = 4;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index13_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA13_3 = input.LA(1);

                         
                        int index13_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred16_KWQL()) ) {s = 4;}

                        else if ( (synpred18_KWQL()) ) {s = 13;}

                        else if ( (true) ) {s = 12;}

                         
                        input.seek(index13_3);
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
    static final String DFA12_eotS =
        "\20\uffff";
    static final String DFA12_eofS =
        "\1\4\17\uffff";
    static final String DFA12_minS =
        "\1\4\3\0\14\uffff";
    static final String DFA12_maxS =
        "\1\46\3\0\14\uffff";
    static final String DFA12_acceptS =
        "\4\uffff\1\2\12\uffff\1\1";
    static final String DFA12_specialS =
        "\1\uffff\1\0\1\1\1\2\14\uffff}>";
    static final String[] DFA12_transitionS = {
            "\4\4\1\uffff\2\4\1\2\1\uffff\1\1\5\uffff\1\4\6\uffff\2\4\1\uffff"+
            "\1\4\1\uffff\1\4\2\uffff\1\4\2\uffff\1\3\1\4",
            "\1\uffff",
            "\1\uffff",
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
            return "71:74: ( helper_bool -> ^( AND $selection_query_helper helper_bool ) )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_1 = input.LA(1);

                         
                        int index12_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_KWQL()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index12_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_2 = input.LA(1);

                         
                        int index12_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_KWQL()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index12_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA12_3 = input.LA(1);

                         
                        int index12_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred17_KWQL()) ) {s = 15;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index12_3);
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
    static final String DFA18_eotS =
        "\20\uffff";
    static final String DFA18_eofS =
        "\20\uffff";
    static final String DFA18_minS =
        "\1\4\13\0\4\uffff";
    static final String DFA18_maxS =
        "\1\45\13\0\4\uffff";
    static final String DFA18_acceptS =
        "\14\uffff\1\1\1\3\1\2\1\4";
    static final String DFA18_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\4\uffff}>";
    static final String[] DFA18_transitionS = {
            "\1\13\3\12\3\uffff\1\2\1\uffff\1\1\5\uffff\1\11\6\uffff\1\5"+
            "\1\4\1\uffff\1\6\1\uffff\1\7\2\uffff\1\10\2\uffff\1\3",
            "\1\uffff",
            "\1\uffff",
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
            return "74:1: ci_query : ( complex_boolean_query ( conjunctive_connectives )? ci_query_helper ( conjunctive_connectives )? c= complex_boolean_query -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query $c) ) | ci_query_helper ( conjunctive_connectives )? complex_boolean_query -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) ) | complex_boolean_query ( conjunctive_connectives )? ci_query_helper -> ^( CI_COMP ^( CI_BODY ci_query_helper ) ^( BOOL_BODY complex_boolean_query ) ) | ci_query_helper -> ^( CI_BODY ci_query_helper ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA18_1 = input.LA(1);

                         
                        int index18_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQL()) ) {s = 12;}

                        else if ( (synpred25_KWQL()) ) {s = 13;}

                         
                        input.seek(index18_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA18_2 = input.LA(1);

                         
                        int index18_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQL()) ) {s = 12;}

                        else if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (synpred25_KWQL()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA18_3 = input.LA(1);

                         
                        int index18_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred21_KWQL()) ) {s = 12;}

                        else if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (synpred25_KWQL()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA18_4 = input.LA(1);

                         
                        int index18_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA18_5 = input.LA(1);

                         
                        int index18_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA18_6 = input.LA(1);

                         
                        int index18_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA18_7 = input.LA(1);

                         
                        int index18_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA18_8 = input.LA(1);

                         
                        int index18_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA18_9 = input.LA(1);

                         
                        int index18_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA18_10 = input.LA(1);

                         
                        int index18_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA18_11 = input.LA(1);

                         
                        int index18_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_KWQL()) ) {s = 14;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index18_11);
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
        "\14\uffff";
    static final String DFA19_eofS =
        "\14\uffff";
    static final String DFA19_minS =
        "\1\4\1\0\12\uffff";
    static final String DFA19_maxS =
        "\1\45\1\0\12\uffff";
    static final String DFA19_acceptS =
        "\2\uffff\1\2\10\uffff\1\1";
    static final String DFA19_specialS =
        "\1\uffff\1\0\12\uffff}>";
    static final String[] DFA19_transitionS = {
            "\4\2\3\uffff\1\2\7\uffff\1\2\6\uffff\2\2\1\uffff\1\2\1\uffff"+
            "\1\2\2\uffff\1\2\2\uffff\1\1",
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
            return "81:1: ci_query_helper : ( LEFT_PARENTHESIS ci_body RIGHT_PARENTHESIS | ci_body );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA19_1 = input.LA(1);

                         
                        int index19_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred26_KWQL()) ) {s = 11;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index19_1);
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
 

    public static final BitSet FOLLOW_head_in_kwqlrule105 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_AT_in_kwqlrule107 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_kwqlrule109 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_kwqlrule111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_head_in_kwqlrule134 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_AT_in_kwqlrule136 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_unique_ci_query_in_kwqlrule138 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_kwqlrule140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unique_ci_query_in_kwqlrule163 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_kwqlrule165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_head_in_kwqlrule186 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_AT_in_kwqlrule188 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_selection_query_in_kwqlrule190 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_kwqlrule192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_global_body_helper_in_global_body226 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_connectives_in_global_body232 = new BitSet(new long[]{0x00000024AC080EF0L});
    public static final BitSet FOLLOW_global_body_helper_in_global_body236 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_NEGATION_in_global_body256 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_global_body258 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_resource_term_helper_in_global_body260 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_global_body262 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_body_in_global_body_helper281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_global_body_helper286 = new BitSet(new long[]{0x00000024AC080EF0L});
    public static final BitSet FOLLOW_global_body_in_global_body_helper289 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_global_body_helper291 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_boolean_query_helper_in_complex_boolean_query308 = new BitSet(new long[]{0x0000002000002E02L});
    public static final BitSet FOLLOW_connectives_in_complex_boolean_query314 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_helper_in_complex_boolean_query318 = new BitSet(new long[]{0x0000002000002E02L});
    public static final BitSet FOLLOW_boolean_query_in_complex_boolean_query_helper347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_complex_boolean_query_helper352 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_complex_boolean_query_helper354 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_complex_boolean_query_helper356 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_complex_boolean_query_helper358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_complex_boolean_query_helper369 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_complex_boolean_query_helper372 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_complex_boolean_query_helper374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOLEAN_in_boolean_query389 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_boolean_query392 = new BitSet(new long[]{0x00000024AC080EF0L});
    public static final BitSet FOLLOW_global_body_in_boolean_query395 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_boolean_query397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEGATION_in_boolean_query404 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_BOOLEAN_in_boolean_query406 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_boolean_query408 = new BitSet(new long[]{0x00000024AC080EF0L});
    public static final BitSet FOLLOW_global_body_in_boolean_query410 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_boolean_query412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unique_ci_query_helper_in_unique_ci_query436 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_DISJUNCTION_in_unique_ci_query442 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_unique_ci_query_helper_in_unique_ci_query446 = new BitSet(new long[]{0x0000000000000402L});
    public static final BitSet FOLLOW_ci_query_in_unique_ci_query_helper474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_unique_ci_query_helper479 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_unique_ci_query_in_unique_ci_query_helper482 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_unique_ci_query_helper487 = new BitSet(new long[]{0x00000024AC0828F2L});
    public static final BitSet FOLLOW_helper_bool_in_unique_ci_query_helper490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_helper_bool_in_unique_ci_query_helper506 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_unique_ci_query_helper508 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_unique_ci_query_in_unique_ci_query_helper510 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_unique_ci_query_helper512 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selection_query_helper_in_selection_query535 = new BitSet(new long[]{0x00000024AC082EF2L});
    public static final BitSet FOLLOW_connectives_in_selection_query541 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_selection_query_helper_in_selection_query545 = new BitSet(new long[]{0x00000024AC082EF2L});
    public static final BitSet FOLLOW_ci_query_in_selection_query_helper571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_selection_query_helper576 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_selection_query_in_selection_query_helper579 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_selection_query_helper584 = new BitSet(new long[]{0x00000024AC0828F2L});
    public static final BitSet FOLLOW_helper_bool_in_selection_query_helper587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_helper_bool_in_selection_query_helper603 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_selection_query_helper605 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_selection_query_in_selection_query_helper607 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_selection_query_helper609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_boolean_query_in_ci_query633 = new BitSet(new long[]{0x00000024AC080AF0L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_ci_query635 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_ci_query_helper_in_ci_query638 = new BitSet(new long[]{0x0000002000002A00L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_ci_query640 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_ci_query645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_query_helper_in_ci_query669 = new BitSet(new long[]{0x0000002000002A00L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_ci_query671 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_ci_query674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_boolean_query_in_ci_query697 = new BitSet(new long[]{0x00000024AC080AF0L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_ci_query699 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_ci_query_helper_in_ci_query702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_query_helper_in_ci_query724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_ci_query_helper746 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_ci_body_in_ci_query_helper749 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_ci_query_helper751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_body_in_ci_query_helper757 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONJUNCTION_in_conjunctive_connectives777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_body_in_ci_body792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_boolean_query_in_helper_bool805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_head_in_synpred1_KWQL105 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_AT_in_synpred1_KWQL107 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_synpred1_KWQL109 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_synpred1_KWQL111 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_head_in_synpred2_KWQL134 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_AT_in_synpred2_KWQL136 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_unique_ci_query_in_synpred2_KWQL138 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_synpred2_KWQL140 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unique_ci_query_in_synpred3_KWQL163 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_synpred3_KWQL165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_global_body_helper_in_synpred5_KWQL226 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_connectives_in_synpred5_KWQL232 = new BitSet(new long[]{0x00000024AC080EF0L});
    public static final BitSet FOLLOW_global_body_helper_in_synpred5_KWQL236 = new BitSet(new long[]{0x00000024AC080EF2L});
    public static final BitSet FOLLOW_body_in_synpred6_KWQL281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_connectives_in_synpred7_KWQL314 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_helper_in_synpred7_KWQL318 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_query_in_synpred12_KWQL474 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred14_KWQL479 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_unique_ci_query_in_synpred14_KWQL482 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred14_KWQL487 = new BitSet(new long[]{0x00000024AC0828F2L});
    public static final BitSet FOLLOW_helper_bool_in_synpred14_KWQL490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_query_in_synpred16_KWQL571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_helper_bool_in_synpred17_KWQL587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred18_KWQL576 = new BitSet(new long[]{0x00000024AC0828F0L});
    public static final BitSet FOLLOW_selection_query_in_synpred18_KWQL579 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred18_KWQL584 = new BitSet(new long[]{0x00000024AC0828F2L});
    public static final BitSet FOLLOW_helper_bool_in_synpred18_KWQL587 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_boolean_query_in_synpred21_KWQL633 = new BitSet(new long[]{0x00000024AC080AF0L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_synpred21_KWQL635 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_ci_query_helper_in_synpred21_KWQL638 = new BitSet(new long[]{0x0000002000002A00L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_synpred21_KWQL640 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_synpred21_KWQL645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ci_query_helper_in_synpred23_KWQL669 = new BitSet(new long[]{0x0000002000002A00L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_synpred23_KWQL671 = new BitSet(new long[]{0x0000002000002800L});
    public static final BitSet FOLLOW_complex_boolean_query_in_synpred23_KWQL674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_complex_boolean_query_in_synpred25_KWQL697 = new BitSet(new long[]{0x00000024AC080AF0L});
    public static final BitSet FOLLOW_conjunctive_connectives_in_synpred25_KWQL699 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_ci_query_helper_in_synpred25_KWQL702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PARENTHESIS_in_synpred26_KWQL746 = new BitSet(new long[]{0x00000024AC0808F0L});
    public static final BitSet FOLLOW_ci_body_in_synpred26_KWQL749 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RIGHT_PARENTHESIS_in_synpred26_KWQL751 = new BitSet(new long[]{0x0000000000000002L});

}
