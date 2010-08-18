// $ANTLR 3.1.2 E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g 2009-10-15 17:19:46

var KWQLLexer = function(input, state) {
// alternate constructor @todo
// public KWQLLexer(CharStream input)
// public KWQLLexer(CharStream input, RecognizerSharedState state) {
    if (!state) {
        state = new org.antlr.runtime.RecognizerSharedState();
    }

    (function(){
    }).call(this);

    this.dfa6 = new KWQLLexer.DFA6(this);
    this.dfa19 = new KWQLLexer.DFA19(this);
    this.dfa34 = new KWQLLexer.DFA34(this);
    KWQLLexer.superclass.constructor.call(this, input, state);


};

org.antlr.lang.augmentObject(KWQLLexer, {
    FRAG: 6,
    QUANT: 23,
    LETTER: 44,
    FRAGMENT: 17,
    HOST: 14,
    COUNT: 24,
    EOF: -1,
    HexDigit: 43,
    UQ: 12,
    AT: 39,
    RIGHT_PAREN: 36,
    NAME: 20,
    QUAL: 26,
    PATH: 15,
    COMMA: 22,
    COMMASEP: 21,
    DIGIT: 27,
    HexPrefix: 42,
    ISOTIME: 29,
    SPARQL: 38,
    SYMBOL: 19,
    CONJUNCTION: 9,
    EscNAME: 31,
    INT: 28,
    LINK: 5,
    OPTIONAL: 8,
    TAG: 7,
    URI: 18,
    SCHEME: 13,
    DISJUNCTION: 10,
    WS: 34,
    NEGATION: 11,
    VARIABLE: 33,
    KW: 32,
    DA: 30,
    CI: 4,
    LEFT_PAREN: 35,
    QUERY: 16,
    ARROW: 37,
    LOWER: 40,
    RESQUAL: 25,
    UPPER: 41
});

(function(){
var HIDDEN = org.antlr.runtime.Token.HIDDEN_CHANNEL,
    EOF = org.antlr.runtime.Token.EOF;
org.antlr.lang.extend(KWQLLexer, org.antlr.runtime.Lexer, {
    FRAG : 6,
    QUANT : 23,
    LETTER : 44,
    FRAGMENT : 17,
    HOST : 14,
    COUNT : 24,
    EOF : -1,
    HexDigit : 43,
    UQ : 12,
    AT : 39,
    RIGHT_PAREN : 36,
    NAME : 20,
    QUAL : 26,
    PATH : 15,
    COMMA : 22,
    COMMASEP : 21,
    DIGIT : 27,
    HexPrefix : 42,
    ISOTIME : 29,
    SPARQL : 38,
    SYMBOL : 19,
    CONJUNCTION : 9,
    EscNAME : 31,
    INT : 28,
    LINK : 5,
    OPTIONAL : 8,
    TAG : 7,
    URI : 18,
    SCHEME : 13,
    DISJUNCTION : 10,
    WS : 34,
    NEGATION : 11,
    VARIABLE : 33,
    KW : 32,
    DA : 30,
    CI : 4,
    LEFT_PAREN : 35,
    QUERY : 16,
    ARROW : 37,
    LOWER : 40,
    RESQUAL : 25,
    UPPER : 41,
    getGrammarFileName: function() { return "E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g"; }
});
org.antlr.lang.augmentObject(KWQLLexer.prototype, {
    nextToken: function() {
        while (true) {
            if ( this.input.LA(1)==org.antlr.runtime.CharStream.EOF ) {
                return org.antlr.runtime.Token.EOF_TOKEN;
            }
            this.state.token = null;
            this.state.channel = org.antlr.runtime.Token.DEFAULT_CHANNEL;
            this.state.tokenStartCharIndex = this.input.index();
            this.state.tokenStartCharPositionInLine = this.input.getCharPositionInLine();
            this.state.tokenStartLine = this.input.getLine();
            this.state.text = null;
            try {
                var m = this.input.mark();
                this.state.backtracking=1; 
                this.state.failed=false;
                this.mTokens();
                this.state.backtracking=0;

                if ( this.state.failed ) {
                    this.input.rewind(m);
                    this.input.consume(); 
                }
                else {
                    this.emit();
                    return this.state.token;
                }
            }
            catch (re) {
                // shouldn't happen in backtracking mode, but...
                if (re instanceof org.antlr.runtime.RecognitionException) {
                    this.reportError(re);
                    this.recover(re);
                } else {
                    throw re;
                }
            }
        }
    },

    memoize: function(input, ruleIndex, ruleStartIndex) {
        if (this.state.backtracking>1) {
            KWQLLexer.superclass.prototype.memoize.call(this, input, ruleIndex, ruleStartIndex);
        }
    },

    alreadyParsedRule: function(input, ruleIndex) {
        if (this.state.backtracking>1) {
            return KWQLLexer.superclass.prototype.alreadyParsedRule.call(this, input, ruleIndex);
        }
        return false;
    },

    // $ANTLR start CI
    mCI: function()  {
        try {
            var _type = this.CI;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:5:4: ( 'ci' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:5:6: 'ci'
            this.match("ci"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "CI",

    // $ANTLR start LINK
    mLINK: function()  {
        try {
            var _type = this.LINK;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:6:6: ( 'link' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:6:8: 'link'
            this.match("link"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "LINK",

    // $ANTLR start FRAG
    mFRAG: function()  {
        try {
            var _type = this.FRAG;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:7:6: ( 'fragment' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:7:8: 'fragment'
            this.match("fragment"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "FRAG",

    // $ANTLR start TAG
    mTAG: function()  {
        try {
            var _type = this.TAG;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:8:5: ( 'tag' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:8:7: 'tag'
            this.match("tag"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "TAG",

    // $ANTLR start OPTIONAL
    mOPTIONAL: function()  {
        try {
            var _type = this.OPTIONAL;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:10:10: ( 'OPTIONAL' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:10:12: 'OPTIONAL'
            this.match("OPTIONAL"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "OPTIONAL",

    // $ANTLR start CONJUNCTION
    mCONJUNCTION: function()  {
        try {
            var _type = this.CONJUNCTION;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:11:12: ( 'AND' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:11:14: 'AND'
            this.match("AND"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "CONJUNCTION",

    // $ANTLR start DISJUNCTION
    mDISJUNCTION: function()  {
        try {
            var _type = this.DISJUNCTION;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:12:12: ( 'OR' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:12:14: 'OR'
            this.match("OR"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "DISJUNCTION",

    // $ANTLR start NEGATION
    mNEGATION: function()  {
        try {
            var _type = this.NEGATION;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:13:9: ( 'NOT' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:13:12: 'NOT'
            this.match("NOT"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "NEGATION",

    // $ANTLR start UQ
    mUQ: function()  {
        try {
            var _type = this.UQ;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:14:4: ( 'ALL' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:14:6: 'ALL'
            this.match("ALL"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "UQ",

    // $ANTLR start URI
    mURI: function()  {
        try {
            var _type = this.URI;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:5: ( SCHEME '//' HOST ( PATH )? ( QUERY )? ( FRAGMENT )? )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:7: SCHEME '//' HOST ( PATH )? ( QUERY )? ( FRAGMENT )?
            this.mSCHEME(); if (this.state.failed) return ;
            this.match("//"); if (this.state.failed) return ;

            this.mHOST(); if (this.state.failed) return ;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:23: ( PATH )?
            var alt1=2;
            var LA1_0 = this.input.LA(1);

            if ( (LA1_0=='/') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:23: PATH
                    this.mPATH(); if (this.state.failed) return ;


                    break;

            }

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:29: ( QUERY )?
            var alt2=2;
            var LA2_0 = this.input.LA(1);

            if ( (LA2_0=='?') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:29: QUERY
                    this.mQUERY(); if (this.state.failed) return ;


                    break;

            }

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:36: ( FRAGMENT )?
            var alt3=2;
            var LA3_0 = this.input.LA(1);

            if ( (LA3_0=='#') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:15:36: FRAGMENT
                    this.mFRAGMENT(); if (this.state.failed) return ;


                    break;

            }




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "URI",

    // $ANTLR start COMMASEP
    mCOMMASEP: function()  {
        try {
            var _type = this.COMMASEP;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:16:10: ( ',' '\"' ( SYMBOL | NAME )+ '\"' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:16:12: ',' '\"' ( SYMBOL | NAME )+ '\"'
            this.match(','); if (this.state.failed) return ;
            this.match('\"'); if (this.state.failed) return ;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:16:18: ( SYMBOL | NAME )+
            var cnt4=0;
            loop4:
            do {
                var alt4=3;
                var LA4_0 = this.input.LA(1);

                if ( (LA4_0=='!'||LA4_0=='#'||LA4_0=='*'||(LA4_0>=',' && LA4_0<='/')||LA4_0=='<'||LA4_0=='>'||LA4_0=='@'||LA4_0=='|') ) {
                    alt4=1;
                }
                else if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='\\'||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=2;
                }


                switch (alt4) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:16:19: SYMBOL
                    this.mSYMBOL(); if (this.state.failed) return ;


                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:16:26: NAME
                    this.mNAME(); if (this.state.failed) return ;


                    break;

                default :
                    if ( cnt4 >= 1 ) {
                        break loop4;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(4, this.input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            this.match('\"'); if (this.state.failed) return ;
            if ( this.state.backtracking===1 ) {
               
              this.setText(this.getText().substring(2, (this.getText().length)-1)); 

            }



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "COMMASEP",

    // $ANTLR start COMMA
    mCOMMA: function()  {
        try {
            var _type = this.COMMA;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:19:7: ( ',' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:19:9: ','
            this.match(','); if (this.state.failed) return ;



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "COMMA",

    // $ANTLR start QUANT
    mQUANT: function()  {
        try {
            var _type = this.QUANT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:20:7: ( 'SOME' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:20:9: 'SOME'
            this.match("SOME"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "QUANT",

    // $ANTLR start COUNT
    mCOUNT: function()  {
        try {
            var _type = this.COUNT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:21:7: ( 'COUNT' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:21:9: 'COUNT'
            this.match("COUNT"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "COUNT",

    // $ANTLR start RESQUAL
    mRESQUAL: function()  {
        try {
            var _type = this.RESQUAL;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:9: ( ( 'target' | 'origin' | 'descendant' | 'child' ) ':' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:11: ( 'target' | 'origin' | 'descendant' | 'child' ) ':'
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:11: ( 'target' | 'origin' | 'descendant' | 'child' )
            var alt5=4;
            switch ( this.input.LA(1) ) {
            case 't':
                alt5=1;
                break;
            case 'o':
                alt5=2;
                break;
            case 'd':
                alt5=3;
                break;
            case 'c':
                alt5=4;
                break;
            default:
                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 5, 0, this.input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:12: 'target'
                    this.match("target"); if (this.state.failed) return ;



                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:21: 'origin'
                    this.match("origin"); if (this.state.failed) return ;



                    break;
                case 3 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:30: 'descendant'
                    this.match("descendant"); if (this.state.failed) return ;



                    break;
                case 4 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:22:43: 'child'
                    this.match("child"); if (this.state.failed) return ;



                    break;

            }

            this.match(':'); if (this.state.failed) return ;
            if ( this.state.backtracking===1 ) {
               
              this.setText(this.getText().substring(0, (this.getText().length)-1)); 	

            }



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "RESQUAL",

    // $ANTLR start QUAL
    mQUAL: function()  {
        try {
            var _type = this.QUAL;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:7: ( ( 'anchorText' | 'name' | 'URI' | 'disagree' | 'lastEdited' | 'title' | 'numberEd' | 'author' | 'created' | 'agree' | 'text' ) ':' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:9: ( 'anchorText' | 'name' | 'URI' | 'disagree' | 'lastEdited' | 'title' | 'numberEd' | 'author' | 'created' | 'agree' | 'text' ) ':'
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:9: ( 'anchorText' | 'name' | 'URI' | 'disagree' | 'lastEdited' | 'title' | 'numberEd' | 'author' | 'created' | 'agree' | 'text' )
            var alt6=11;
            alt6 = this.dfa6.predict(this.input);
            switch (alt6) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:10: 'anchorText'
                    this.match("anchorText"); if (this.state.failed) return ;



                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:23: 'name'
                    this.match("name"); if (this.state.failed) return ;



                    break;
                case 3 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:30: 'URI'
                    this.match("URI"); if (this.state.failed) return ;



                    break;
                case 4 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:25:36: 'disagree'
                    this.match("disagree"); if (this.state.failed) return ;



                    break;
                case 5 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:26:4: 'lastEdited'
                    this.match("lastEdited"); if (this.state.failed) return ;



                    break;
                case 6 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:26:17: 'title'
                    this.match("title"); if (this.state.failed) return ;



                    break;
                case 7 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:26:25: 'numberEd'
                    this.match("numberEd"); if (this.state.failed) return ;



                    break;
                case 8 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:26:36: 'author'
                    this.match("author"); if (this.state.failed) return ;



                    break;
                case 9 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:26:45: 'created'
                    this.match("created"); if (this.state.failed) return ;



                    break;
                case 10 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:27:4: 'agree'
                    this.match("agree"); if (this.state.failed) return ;



                    break;
                case 11 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:27:12: 'text'
                    this.match("text"); if (this.state.failed) return ;



                    break;

            }

            this.match(':'); if (this.state.failed) return ;
            if ( this.state.backtracking===1 ) {
               
              this.setText(this.getText().substring(0, (this.getText().length)-1)); 	

            }



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "QUAL",

    // $ANTLR start INT
    mINT: function()  {
        try {
            var _type = this.INT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:30:5: ( ( DIGIT )+ )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:30:7: ( DIGIT )+
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:30:7: ( DIGIT )+
            var cnt7=0;
            loop7:
            do {
                var alt7=2;
                var LA7_0 = this.input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:30:7: DIGIT
                    this.mDIGIT(); if (this.state.failed) return ;


                    break;

                default :
                    if ( cnt7 >= 1 ) {
                        break loop7;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(7, this.input);
                        throw eee;
                }
                cnt7++;
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "INT",

    // $ANTLR start DA
    mDA: function()  {
        try {
            var _type = this.DA;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:31:4: ( ISOTIME )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:31:6: ISOTIME
            this.mISOTIME(); if (this.state.failed) return ;



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "DA",

    // $ANTLR start KW
    mKW: function()  {
        try {
            var _type = this.KW;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:4: ( ( '\"' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\"' ) | NAME | ( '\\'' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\\'' ) )
            var alt16=3;
            switch ( this.input.LA(1) ) {
            case '\"':
                alt16=1;
                break;
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
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '\\':
            case '_':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
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
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                alt16=2;
                break;
            case '\'':
                alt16=3;
                break;
            default:
                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 16, 0, this.input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:7: ( '\"' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\"' )
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:7: ( '\"' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\"' )
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:8: '\"' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\"'
                    this.match('\"'); if (this.state.failed) return ;
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:12: ( NAME | EscNAME )+
                    var cnt8=0;
                    loop8:
                    do {
                        var alt8=3;
                        var LA8_0 = this.input.LA(1);

                        if ( ((LA8_0>='0' && LA8_0<='9')||(LA8_0>='A' && LA8_0<='Z')||LA8_0=='\\'||LA8_0=='_'||(LA8_0>='a' && LA8_0<='z')) ) {
                            alt8=1;
                        }
                        else if ( (LA8_0=='$'||LA8_0==':') ) {
                            alt8=2;
                        }


                        switch (alt8) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:13: NAME
                            this.mNAME(); if (this.state.failed) return ;


                            break;
                        case 2 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:18: EscNAME
                            this.mEscNAME(); if (this.state.failed) return ;


                            break;

                        default :
                            if ( cnt8 >= 1 ) {
                                break loop8;
                            }
                            if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                var eee = new org.antlr.runtime.EarlyExitException(8, this.input);
                                throw eee;
                        }
                        cnt8++;
                    } while (true);

                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:28: ( ( ' ' )+ ( NAME | EscNAME )+ )*
                    loop11:
                    do {
                        var alt11=2;
                        var LA11_0 = this.input.LA(1);

                        if ( (LA11_0==' ') ) {
                            alt11=1;
                        }


                        switch (alt11) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:29: ( ' ' )+ ( NAME | EscNAME )+
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:29: ( ' ' )+
                            var cnt9=0;
                            loop9:
                            do {
                                var alt9=2;
                                var LA9_0 = this.input.LA(1);

                                if ( (LA9_0==' ') ) {
                                    alt9=1;
                                }


                                switch (alt9) {
                                case 1 :
                                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:29: ' '
                                    this.match(' '); if (this.state.failed) return ;


                                    break;

                                default :
                                    if ( cnt9 >= 1 ) {
                                        break loop9;
                                    }
                                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                        var eee = new org.antlr.runtime.EarlyExitException(9, this.input);
                                        throw eee;
                                }
                                cnt9++;
                            } while (true);

                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:33: ( NAME | EscNAME )+
                            var cnt10=0;
                            loop10:
                            do {
                                var alt10=3;
                                var LA10_0 = this.input.LA(1);

                                if ( ((LA10_0>='0' && LA10_0<='9')||(LA10_0>='A' && LA10_0<='Z')||LA10_0=='\\'||LA10_0=='_'||(LA10_0>='a' && LA10_0<='z')) ) {
                                    alt10=1;
                                }
                                else if ( (LA10_0=='$'||LA10_0==':') ) {
                                    alt10=2;
                                }


                                switch (alt10) {
                                case 1 :
                                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:34: NAME
                                    this.mNAME(); if (this.state.failed) return ;


                                    break;
                                case 2 :
                                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:39: EscNAME
                                    this.mEscNAME(); if (this.state.failed) return ;


                                    break;

                                default :
                                    if ( cnt10 >= 1 ) {
                                        break loop10;
                                    }
                                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                        var eee = new org.antlr.runtime.EarlyExitException(10, this.input);
                                        throw eee;
                                }
                                cnt10++;
                            } while (true);



                            break;

                        default :
                            break loop11;
                        }
                    } while (true);

                    this.match('\"'); if (this.state.failed) return ;





                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:56: NAME
                    this.mNAME(); if (this.state.failed) return ;


                    break;
                case 3 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:61: ( '\\'' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\\'' )
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:61: ( '\\'' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\\'' )
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:62: '\\'' ( NAME | EscNAME )+ ( ( ' ' )+ ( NAME | EscNAME )+ )* '\\''
                    this.match('\''); if (this.state.failed) return ;
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:67: ( NAME | EscNAME )+
                    var cnt12=0;
                    loop12:
                    do {
                        var alt12=3;
                        var LA12_0 = this.input.LA(1);

                        if ( ((LA12_0>='0' && LA12_0<='9')||(LA12_0>='A' && LA12_0<='Z')||LA12_0=='\\'||LA12_0=='_'||(LA12_0>='a' && LA12_0<='z')) ) {
                            alt12=1;
                        }
                        else if ( (LA12_0=='$'||LA12_0==':') ) {
                            alt12=2;
                        }


                        switch (alt12) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:68: NAME
                            this.mNAME(); if (this.state.failed) return ;


                            break;
                        case 2 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:73: EscNAME
                            this.mEscNAME(); if (this.state.failed) return ;


                            break;

                        default :
                            if ( cnt12 >= 1 ) {
                                break loop12;
                            }
                            if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                var eee = new org.antlr.runtime.EarlyExitException(12, this.input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);

                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:83: ( ( ' ' )+ ( NAME | EscNAME )+ )*
                    loop15:
                    do {
                        var alt15=2;
                        var LA15_0 = this.input.LA(1);

                        if ( (LA15_0==' ') ) {
                            alt15=1;
                        }


                        switch (alt15) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:84: ( ' ' )+ ( NAME | EscNAME )+
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:84: ( ' ' )+
                            var cnt13=0;
                            loop13:
                            do {
                                var alt13=2;
                                var LA13_0 = this.input.LA(1);

                                if ( (LA13_0==' ') ) {
                                    alt13=1;
                                }


                                switch (alt13) {
                                case 1 :
                                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:84: ' '
                                    this.match(' '); if (this.state.failed) return ;


                                    break;

                                default :
                                    if ( cnt13 >= 1 ) {
                                        break loop13;
                                    }
                                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                        var eee = new org.antlr.runtime.EarlyExitException(13, this.input);
                                        throw eee;
                                }
                                cnt13++;
                            } while (true);

                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:88: ( NAME | EscNAME )+
                            var cnt14=0;
                            loop14:
                            do {
                                var alt14=3;
                                var LA14_0 = this.input.LA(1);

                                if ( ((LA14_0>='0' && LA14_0<='9')||(LA14_0>='A' && LA14_0<='Z')||LA14_0=='\\'||LA14_0=='_'||(LA14_0>='a' && LA14_0<='z')) ) {
                                    alt14=1;
                                }
                                else if ( (LA14_0=='$'||LA14_0==':') ) {
                                    alt14=2;
                                }


                                switch (alt14) {
                                case 1 :
                                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:89: NAME
                                    this.mNAME(); if (this.state.failed) return ;


                                    break;
                                case 2 :
                                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:32:94: EscNAME
                                    this.mEscNAME(); if (this.state.failed) return ;


                                    break;

                                default :
                                    if ( cnt14 >= 1 ) {
                                        break loop14;
                                    }
                                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                        var eee = new org.antlr.runtime.EarlyExitException(14, this.input);
                                        throw eee;
                                }
                                cnt14++;
                            } while (true);



                            break;

                        default :
                            break loop15;
                        }
                    } while (true);

                    this.match('\''); if (this.state.failed) return ;





                    break;

            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "KW",

    // $ANTLR start VARIABLE
    mVARIABLE: function()  {
        try {
            var _type = this.VARIABLE;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:33:9: ( '$' NAME )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:33:11: '$' NAME
            this.match('$'); if (this.state.failed) return ;
            this.mNAME(); if (this.state.failed) return ;
            if ( this.state.backtracking===1 ) {
               
              this.setText(this.getText().substring(1, this.getText().length)); 

            }



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "VARIABLE",

    // $ANTLR start WS
    mWS: function()  {
        try {
            var _type = this.WS;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:36:4: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:36:7: ( ' ' | '\\t' | '\\n' | '\\r' )+
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:36:7: ( ' ' | '\\t' | '\\n' | '\\r' )+
            var cnt17=0;
            loop17:
            do {
                var alt17=2;
                var LA17_0 = this.input.LA(1);

                if ( ((LA17_0>='\t' && LA17_0<='\n')||LA17_0=='\r'||LA17_0==' ') ) {
                    alt17=1;
                }


                switch (alt17) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
                    if ( (this.input.LA(1)>='\t' && this.input.LA(1)<='\n')||this.input.LA(1)=='\r'||this.input.LA(1)==' ' ) {
                        this.input.consume();
                    this.state.failed=false;
                    }
                    else {
                        if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    if ( cnt17 >= 1 ) {
                        break loop17;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(17, this.input);
                        throw eee;
                }
                cnt17++;
            } while (true);

            if ( this.state.backtracking===1 ) {
              _channel=HIDDEN;
            }



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "WS",

    // $ANTLR start LEFT_PAREN
    mLEFT_PAREN: function()  {
        try {
            var _type = this.LEFT_PAREN;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:37:11: ( '(' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:37:13: '('
            this.match('('); if (this.state.failed) return ;



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "LEFT_PAREN",

    // $ANTLR start RIGHT_PAREN
    mRIGHT_PAREN: function()  {
        try {
            var _type = this.RIGHT_PAREN;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:38:12: ( ')' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:38:15: ')'
            this.match(')'); if (this.state.failed) return ;



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "RIGHT_PAREN",

    // $ANTLR start ARROW
    mARROW: function()  {
        try {
            var _type = this.ARROW;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:39:7: ( '->' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:39:9: '->'
            this.match("->"); if (this.state.failed) return ;




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "ARROW",

    // $ANTLR start SPARQL
    mSPARQL: function()  {
        try {
            var _type = this.SPARQL;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:40:8: ( 'SPARQL:(' ( . )* ')' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:40:10: 'SPARQL:(' ( . )* ')'
            this.match("SPARQL:("); if (this.state.failed) return ;

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:40:20: ( . )*
            loop18:
            do {
                var alt18=2;
                var LA18_0 = this.input.LA(1);

                if ( (LA18_0==')') ) {
                    alt18=2;
                }
                else if ( ((LA18_0>='\u0000' && LA18_0<='(')||(LA18_0>='*' && LA18_0<='\uFFFF')) ) {
                    alt18=1;
                }


                switch (alt18) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:40:20: .
                    this.matchAny(); if (this.state.failed) return ;


                    break;

                default :
                    break loop18;
                }
            } while (true);

            this.match(')'); if (this.state.failed) return ;



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "SPARQL",

    // $ANTLR start AT
    mAT: function()  {
        try {
            var _type = this.AT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:41:4: ( '@' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:41:6: '@'
            this.match('@'); if (this.state.failed) return ;



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "AT",

    // $ANTLR start QUERY
    mQUERY: function()  {
        try {
            var _type = this.QUERY;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:7: ( '?' ( LOWER | UPPER | DIGIT | '.' | '-' | '&' | '+' | '%' | '=' | PATH | ':' )+ ( '/' )? )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:9: '?' ( LOWER | UPPER | DIGIT | '.' | '-' | '&' | '+' | '%' | '=' | PATH | ':' )+ ( '/' )?
            this.match('?'); if (this.state.failed) return ;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:12: ( LOWER | UPPER | DIGIT | '.' | '-' | '&' | '+' | '%' | '=' | PATH | ':' )+
            var cnt19=0;
            loop19:
            do {
                var alt19=12;
                alt19 = this.dfa19.predict(this.input);
                switch (alt19) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:13: LOWER
                    this.mLOWER(); if (this.state.failed) return ;


                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:19: UPPER
                    this.mUPPER(); if (this.state.failed) return ;


                    break;
                case 3 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:25: DIGIT
                    this.mDIGIT(); if (this.state.failed) return ;


                    break;
                case 4 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:31: '.'
                    this.match('.'); if (this.state.failed) return ;


                    break;
                case 5 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:35: '-'
                    this.match('-'); if (this.state.failed) return ;


                    break;
                case 6 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:39: '&'
                    this.match('&'); if (this.state.failed) return ;


                    break;
                case 7 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:43: '+'
                    this.match('+'); if (this.state.failed) return ;


                    break;
                case 8 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:47: '%'
                    this.match('%'); if (this.state.failed) return ;


                    break;
                case 9 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:51: '='
                    this.match('='); if (this.state.failed) return ;


                    break;
                case 10 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:55: PATH
                    this.mPATH(); if (this.state.failed) return ;


                    break;
                case 11 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:60: ':'
                    this.match(':'); if (this.state.failed) return ;


                    break;

                default :
                    if ( cnt19 >= 1 ) {
                        break loop19;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(19, this.input);
                        throw eee;
                }
                cnt19++;
            } while (true);

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:65: ( '/' )?
            var alt20=2;
            var LA20_0 = this.input.LA(1);

            if ( (LA20_0=='/') ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:42:65: '/'
                    this.match('/'); if (this.state.failed) return ;


                    break;

            }




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "QUERY",

    // $ANTLR start FRAGMENT
    mFRAGMENT: function()  {
        try {
            var _type = this.FRAGMENT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:43:9: ( '#' ( LOWER | UPPER | '+' | '.' | '-' | '_' )+ )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:43:11: '#' ( LOWER | UPPER | '+' | '.' | '-' | '_' )+
            this.match('#'); if (this.state.failed) return ;
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:43:14: ( LOWER | UPPER | '+' | '.' | '-' | '_' )+
            var cnt21=0;
            loop21:
            do {
                var alt21=2;
                var LA21_0 = this.input.LA(1);

                if ( (LA21_0=='+'||(LA21_0>='-' && LA21_0<='.')||(LA21_0>='A' && LA21_0<='Z')||LA21_0=='_'||(LA21_0>='a' && LA21_0<='z')) ) {
                    alt21=1;
                }


                switch (alt21) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
                    if ( this.input.LA(1)=='+'||(this.input.LA(1)>='-' && this.input.LA(1)<='.')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();
                    this.state.failed=false;
                    }
                    else {
                        if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    if ( cnt21 >= 1 ) {
                        break loop21;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(21, this.input);
                        throw eee;
                }
                cnt21++;
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "FRAGMENT",

    // $ANTLR start PATH
    mPATH: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:15: ( ( '/' ( '~' )? ( LOWER | UPPER | DIGIT | '.' | '-' | '_' )+ )+ ( '/' )? )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:17: ( '/' ( '~' )? ( LOWER | UPPER | DIGIT | '.' | '-' | '_' )+ )+ ( '/' )?
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:17: ( '/' ( '~' )? ( LOWER | UPPER | DIGIT | '.' | '-' | '_' )+ )+
            var cnt24=0;
            loop24:
            do {
                var alt24=2;
                var LA24_0 = this.input.LA(1);

                if ( (LA24_0=='/') ) {
                    var LA24_1 = this.input.LA(2);

                    if ( ((LA24_1>='-' && LA24_1<='.')||(LA24_1>='0' && LA24_1<='9')||(LA24_1>='A' && LA24_1<='Z')||LA24_1=='_'||(LA24_1>='a' && LA24_1<='z')||LA24_1=='~') ) {
                        alt24=1;
                    }


                }


                switch (alt24) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:18: '/' ( '~' )? ( LOWER | UPPER | DIGIT | '.' | '-' | '_' )+
                    this.match('/'); if (this.state.failed) return ;
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:21: ( '~' )?
                    var alt22=2;
                    var LA22_0 = this.input.LA(1);

                    if ( (LA22_0=='~') ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:21: '~'
                            this.match('~'); if (this.state.failed) return ;


                            break;

                    }

                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:25: ( LOWER | UPPER | DIGIT | '.' | '-' | '_' )+
                    var cnt23=0;
                    loop23:
                    do {
                        var alt23=2;
                        var LA23_0 = this.input.LA(1);

                        if ( ((LA23_0>='-' && LA23_0<='.')||(LA23_0>='0' && LA23_0<='9')||(LA23_0>='A' && LA23_0<='Z')||LA23_0=='_'||(LA23_0>='a' && LA23_0<='z')) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
                            if ( (this.input.LA(1)>='-' && this.input.LA(1)<='.')||(this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                                this.input.consume();
                            this.state.failed=false;
                            }
                            else {
                                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                                this.recover(mse);
                                throw mse;}



                            break;

                        default :
                            if ( cnt23 >= 1 ) {
                                break loop23;
                            }
                            if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                var eee = new org.antlr.runtime.EarlyExitException(23, this.input);
                                throw eee;
                        }
                        cnt23++;
                    } while (true);



                    break;

                default :
                    if ( cnt24 >= 1 ) {
                        break loop24;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(24, this.input);
                        throw eee;
                }
                cnt24++;
            } while (true);

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:59: ( '/' )?
            var alt25=2;
            var LA25_0 = this.input.LA(1);

            if ( (LA25_0=='/') ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:45:59: '/'
                    this.match('/'); if (this.state.failed) return ;


                    break;

            }




        }
        finally {
        }
    },
    // $ANTLR end "PATH",

    // $ANTLR start SCHEME
    mSCHEME: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:46:17: ( ( LOWER | UPPER | '+' | '.' | '-' )+ ':' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:46:19: ( LOWER | UPPER | '+' | '.' | '-' )+ ':'
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:46:19: ( LOWER | UPPER | '+' | '.' | '-' )+
            var cnt26=0;
            loop26:
            do {
                var alt26=2;
                var LA26_0 = this.input.LA(1);

                if ( (LA26_0=='+'||(LA26_0>='-' && LA26_0<='.')||(LA26_0>='A' && LA26_0<='Z')||(LA26_0>='a' && LA26_0<='z')) ) {
                    alt26=1;
                }


                switch (alt26) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
                    if ( this.input.LA(1)=='+'||(this.input.LA(1)>='-' && this.input.LA(1)<='.')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();
                    this.state.failed=false;
                    }
                    else {
                        if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    if ( cnt26 >= 1 ) {
                        break loop26;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(26, this.input);
                        throw eee;
                }
                cnt26++;
            } while (true);

            this.match(':'); if (this.state.failed) return ;



        }
        finally {
        }
    },
    // $ANTLR end "SCHEME",

    // $ANTLR start HexPrefix
    mHexPrefix: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:47:19: ( '0x' | '0X' )
            var alt27=2;
            var LA27_0 = this.input.LA(1);

            if ( (LA27_0=='0') ) {
                var LA27_1 = this.input.LA(2);

                if ( (LA27_1=='x') ) {
                    alt27=1;
                }
                else if ( (LA27_1=='X') ) {
                    alt27=2;
                }
                else {
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                    var nvae =
                        new org.antlr.runtime.NoViableAltException("", 27, 1, this.input);

                    throw nvae;
                }
            }
            else {
                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 27, 0, this.input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:47:21: '0x'
                    this.match("0x"); if (this.state.failed) return ;



                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:47:28: '0X'
                    this.match("0X"); if (this.state.failed) return ;



                    break;

            }
        }
        finally {
        }
    },
    // $ANTLR end "HexPrefix",

    // $ANTLR start HexDigit
    mHexDigit: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:48:18: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:48:20: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            if ( (this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='F')||(this.input.LA(1)>='a' && this.input.LA(1)<='f') ) {
                this.input.consume();
            this.state.failed=false;
            }
            else {
                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}




        }
        finally {
        }
    },
    // $ANTLR end "HexDigit",

    // $ANTLR start NAME
    mNAME: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:14: ( ( ( '\\\\$' ) | '\\\\' )? ( LETTER | DIGIT | '_' | '\\\\:' )+ )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:16: ( ( '\\\\$' ) | '\\\\' )? ( LETTER | DIGIT | '_' | '\\\\:' )+
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:16: ( ( '\\\\$' ) | '\\\\' )?
            var alt28=3;
            var LA28_0 = this.input.LA(1);

            if ( (LA28_0=='\\') ) {
                var LA28_1 = this.input.LA(2);

                if ( (LA28_1=='$') ) {
                    alt28=1;
                }
                else if ( ((LA28_1>='0' && LA28_1<='9')||(LA28_1>='A' && LA28_1<='Z')||LA28_1=='\\'||LA28_1=='_'||(LA28_1>='a' && LA28_1<='z')) ) {
                    alt28=2;
                }
            }
            switch (alt28) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:17: ( '\\\\$' )
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:17: ( '\\\\$' )
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:18: '\\\\$'
                    this.match("\\$"); if (this.state.failed) return ;






                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:25: '\\\\'
                    this.match('\\'); if (this.state.failed) return ;


                    break;

            }

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:31: ( LETTER | DIGIT | '_' | '\\\\:' )+
            var cnt29=0;
            loop29:
            do {
                var alt29=5;
                switch ( this.input.LA(1) ) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
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
                case 'f':
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
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    alt29=1;
                    break;
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
                    alt29=2;
                    break;
                case '_':
                    alt29=3;
                    break;
                case '\\':
                    alt29=4;
                    break;

                }

                switch (alt29) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:32: LETTER
                    this.mLETTER(); if (this.state.failed) return ;


                    break;
                case 2 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:41: DIGIT
                    this.mDIGIT(); if (this.state.failed) return ;


                    break;
                case 3 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:49: '_'
                    this.match('_'); if (this.state.failed) return ;


                    break;
                case 4 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:49:55: '\\\\:'
                    this.match("\\:"); if (this.state.failed) return ;



                    break;

                default :
                    if ( cnt29 >= 1 ) {
                        break loop29;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(29, this.input);
                        throw eee;
                }
                cnt29++;
            } while (true);




        }
        finally {
        }
    },
    // $ANTLR end "NAME",

    // $ANTLR start EscNAME
    mEscNAME: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:50:17: ( ( '$' | ':' )+ )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:50:19: ( '$' | ':' )+
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:50:19: ( '$' | ':' )+
            var cnt30=0;
            loop30:
            do {
                var alt30=2;
                var LA30_0 = this.input.LA(1);

                if ( (LA30_0=='$'||LA30_0==':') ) {
                    alt30=1;
                }


                switch (alt30) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
                    if ( this.input.LA(1)=='$'||this.input.LA(1)==':' ) {
                        this.input.consume();
                    this.state.failed=false;
                    }
                    else {
                        if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    if ( cnt30 >= 1 ) {
                        break loop30;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(30, this.input);
                        throw eee;
                }
                cnt30++;
            } while (true);




        }
        finally {
        }
    },
    // $ANTLR end "EscNAME",

    // $ANTLR start LETTER
    mLETTER: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:51:16: ( LOWER | UPPER )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
            if ( (this.input.LA(1)>='A' && this.input.LA(1)<='Z')||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                this.input.consume();
            this.state.failed=false;
            }
            else {
                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}




        }
        finally {
        }
    },
    // $ANTLR end "LETTER",

    // $ANTLR start LOWER
    mLOWER: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:52:15: ( 'a' .. 'z' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:52:17: 'a' .. 'z'
            this.matchRange('a','z'); if (this.state.failed) return ;



        }
        finally {
        }
    },
    // $ANTLR end "LOWER",

    // $ANTLR start UPPER
    mUPPER: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:53:15: ( 'A' .. 'Z' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:53:17: 'A' .. 'Z'
            this.matchRange('A','Z'); if (this.state.failed) return ;



        }
        finally {
        }
    },
    // $ANTLR end "UPPER",

    // $ANTLR start DIGIT
    mDIGIT: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:54:15: ( '0' .. '9' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:54:17: '0' .. '9'
            this.matchRange('0','9'); if (this.state.failed) return ;



        }
        finally {
        }
    },
    // $ANTLR end "DIGIT",

    // $ANTLR start SYMBOL
    mSYMBOL: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:55:16: ( '*' | ',' | '<' | '>' | '.' | '/' | '|' | '#' | '!' | '@' | '-' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
            if ( this.input.LA(1)=='!'||this.input.LA(1)=='#'||this.input.LA(1)=='*'||(this.input.LA(1)>=',' && this.input.LA(1)<='/')||this.input.LA(1)=='<'||this.input.LA(1)=='>'||this.input.LA(1)=='@'||this.input.LA(1)=='|' ) {
                this.input.consume();
            this.state.failed=false;
            }
            else {
                if (this.state.backtracking>0) {this.state.failed=true; return ;}
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}




        }
        finally {
        }
    },
    // $ANTLR end "SYMBOL",

    // $ANTLR start HOST
    mHOST: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:15: ( ( LOWER | UPPER | DIGIT | '.' | '-' )+ ( ':' ( DIGIT )+ )? )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:17: ( LOWER | UPPER | DIGIT | '.' | '-' )+ ( ':' ( DIGIT )+ )?
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:17: ( LOWER | UPPER | DIGIT | '.' | '-' )+
            var cnt31=0;
            loop31:
            do {
                var alt31=2;
                var LA31_0 = this.input.LA(1);

                if ( ((LA31_0>='-' && LA31_0<='.')||(LA31_0>='0' && LA31_0<='9')||(LA31_0>='A' && LA31_0<='Z')||(LA31_0>='a' && LA31_0<='z')) ) {
                    alt31=1;
                }


                switch (alt31) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:
                    if ( (this.input.LA(1)>='-' && this.input.LA(1)<='.')||(this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();
                    this.state.failed=false;
                    }
                    else {
                        if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    if ( cnt31 >= 1 ) {
                        break loop31;
                    }
                    if (this.state.backtracking>0) {this.state.failed=true; return ;}
                        var eee = new org.antlr.runtime.EarlyExitException(31, this.input);
                        throw eee;
                }
                cnt31++;
            } while (true);

            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:45: ( ':' ( DIGIT )+ )?
            var alt33=2;
            var LA33_0 = this.input.LA(1);

            if ( (LA33_0==':') ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:46: ':' ( DIGIT )+
                    this.match(':'); if (this.state.failed) return ;
                    // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:49: ( DIGIT )+
                    var cnt32=0;
                    loop32:
                    do {
                        var alt32=2;
                        var LA32_0 = this.input.LA(1);

                        if ( ((LA32_0>='0' && LA32_0<='9')) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                        case 1 :
                            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:56:49: DIGIT
                            this.mDIGIT(); if (this.state.failed) return ;


                            break;

                        default :
                            if ( cnt32 >= 1 ) {
                                break loop32;
                            }
                            if (this.state.backtracking>0) {this.state.failed=true; return ;}
                                var eee = new org.antlr.runtime.EarlyExitException(32, this.input);
                                throw eee;
                        }
                        cnt32++;
                    } while (true);



                    break;

            }




        }
        finally {
        }
    },
    // $ANTLR end "HOST",

    // $ANTLR start ISOTIME
    mISOTIME: function()  {
        try {
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:57:17: ( DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT 'Z' )
            // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:57:19: DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT 'Z'
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.match('-'); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.match('-'); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.match('T'); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.match(':'); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.match(':'); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.mDIGIT(); if (this.state.failed) return ;
            this.match('Z'); if (this.state.failed) return ;



        }
        finally {
        }
    },
    // $ANTLR end "ISOTIME",

    mTokens: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:39: ( CI | LINK | FRAG | TAG | OPTIONAL | CONJUNCTION | DISJUNCTION | NEGATION | UQ | URI | COMMASEP | COMMA | QUANT | COUNT | RESQUAL | QUAL | INT | DA | KW | VARIABLE | WS | LEFT_PAREN | RIGHT_PAREN | ARROW | SPARQL | AT | QUERY | FRAGMENT )
        var alt34=28;
        alt34 = this.dfa34.predict(this.input);
        switch (alt34) {
            case 1 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:41: CI
                this.mCI(); if (this.state.failed) return ;


                break;
            case 2 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:44: LINK
                this.mLINK(); if (this.state.failed) return ;


                break;
            case 3 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:49: FRAG
                this.mFRAG(); if (this.state.failed) return ;


                break;
            case 4 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:54: TAG
                this.mTAG(); if (this.state.failed) return ;


                break;
            case 5 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:58: OPTIONAL
                this.mOPTIONAL(); if (this.state.failed) return ;


                break;
            case 6 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:67: CONJUNCTION
                this.mCONJUNCTION(); if (this.state.failed) return ;


                break;
            case 7 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:79: DISJUNCTION
                this.mDISJUNCTION(); if (this.state.failed) return ;


                break;
            case 8 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:91: NEGATION
                this.mNEGATION(); if (this.state.failed) return ;


                break;
            case 9 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:100: UQ
                this.mUQ(); if (this.state.failed) return ;


                break;
            case 10 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:103: URI
                this.mURI(); if (this.state.failed) return ;


                break;
            case 11 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:107: COMMASEP
                this.mCOMMASEP(); if (this.state.failed) return ;


                break;
            case 12 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:116: COMMA
                this.mCOMMA(); if (this.state.failed) return ;


                break;
            case 13 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:122: QUANT
                this.mQUANT(); if (this.state.failed) return ;


                break;
            case 14 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:128: COUNT
                this.mCOUNT(); if (this.state.failed) return ;


                break;
            case 15 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:134: RESQUAL
                this.mRESQUAL(); if (this.state.failed) return ;


                break;
            case 16 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:142: QUAL
                this.mQUAL(); if (this.state.failed) return ;


                break;
            case 17 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:147: INT
                this.mINT(); if (this.state.failed) return ;


                break;
            case 18 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:151: DA
                this.mDA(); if (this.state.failed) return ;


                break;
            case 19 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:154: KW
                this.mKW(); if (this.state.failed) return ;


                break;
            case 20 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:157: VARIABLE
                this.mVARIABLE(); if (this.state.failed) return ;


                break;
            case 21 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:166: WS
                this.mWS(); if (this.state.failed) return ;


                break;
            case 22 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:169: LEFT_PAREN
                this.mLEFT_PAREN(); if (this.state.failed) return ;


                break;
            case 23 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:180: RIGHT_PAREN
                this.mRIGHT_PAREN(); if (this.state.failed) return ;


                break;
            case 24 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:192: ARROW
                this.mARROW(); if (this.state.failed) return ;


                break;
            case 25 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:198: SPARQL
                this.mSPARQL(); if (this.state.failed) return ;


                break;
            case 26 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:205: AT
                this.mAT(); if (this.state.failed) return ;


                break;
            case 27 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:208: QUERY
                this.mQUERY(); if (this.state.failed) return ;


                break;
            case 28 :
                // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:214: FRAGMENT
                this.mFRAGMENT(); if (this.state.failed) return ;


                break;

        }

    },

    // $ANTLR start "synpred1_KWQLLexer"
    synpred1_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:41: ( CI )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:41: CI
        this.mCI(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred1_KWQLLexer",

    // $ANTLR start "synpred2_KWQLLexer"
    synpred2_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:44: ( LINK )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:44: LINK
        this.mLINK(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred2_KWQLLexer",

    // $ANTLR start "synpred3_KWQLLexer"
    synpred3_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:49: ( FRAG )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:49: FRAG
        this.mFRAG(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred3_KWQLLexer",

    // $ANTLR start "synpred4_KWQLLexer"
    synpred4_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:54: ( TAG )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:54: TAG
        this.mTAG(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred4_KWQLLexer",

    // $ANTLR start "synpred5_KWQLLexer"
    synpred5_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:58: ( OPTIONAL )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:58: OPTIONAL
        this.mOPTIONAL(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred5_KWQLLexer",

    // $ANTLR start "synpred6_KWQLLexer"
    synpred6_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:67: ( CONJUNCTION )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:67: CONJUNCTION
        this.mCONJUNCTION(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred6_KWQLLexer",

    // $ANTLR start "synpred7_KWQLLexer"
    synpred7_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:79: ( DISJUNCTION )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:79: DISJUNCTION
        this.mDISJUNCTION(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred7_KWQLLexer",

    // $ANTLR start "synpred8_KWQLLexer"
    synpred8_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:91: ( NEGATION )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:91: NEGATION
        this.mNEGATION(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred8_KWQLLexer",

    // $ANTLR start "synpred9_KWQLLexer"
    synpred9_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:100: ( UQ )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:100: UQ
        this.mUQ(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred9_KWQLLexer",

    // $ANTLR start "synpred10_KWQLLexer"
    synpred10_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:103: ( URI )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:103: URI
        this.mURI(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred10_KWQLLexer",

    // $ANTLR start "synpred11_KWQLLexer"
    synpred11_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:107: ( COMMASEP )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:107: COMMASEP
        this.mCOMMASEP(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred11_KWQLLexer",

    // $ANTLR start "synpred12_KWQLLexer"
    synpred12_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:116: ( COMMA )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:116: COMMA
        this.mCOMMA(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred12_KWQLLexer",

    // $ANTLR start "synpred13_KWQLLexer"
    synpred13_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:122: ( QUANT )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:122: QUANT
        this.mQUANT(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred13_KWQLLexer",

    // $ANTLR start "synpred14_KWQLLexer"
    synpred14_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:128: ( COUNT )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:128: COUNT
        this.mCOUNT(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred14_KWQLLexer",

    // $ANTLR start "synpred15_KWQLLexer"
    synpred15_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:134: ( RESQUAL )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:134: RESQUAL
        this.mRESQUAL(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred15_KWQLLexer",

    // $ANTLR start "synpred16_KWQLLexer"
    synpred16_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:142: ( QUAL )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:142: QUAL
        this.mQUAL(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred16_KWQLLexer",

    // $ANTLR start "synpred17_KWQLLexer"
    synpred17_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:147: ( INT )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:147: INT
        this.mINT(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred17_KWQLLexer",

    // $ANTLR start "synpred18_KWQLLexer"
    synpred18_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:151: ( DA )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:151: DA
        this.mDA(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred18_KWQLLexer",

    // $ANTLR start "synpred19_KWQLLexer"
    synpred19_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:154: ( KW )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:154: KW
        this.mKW(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred19_KWQLLexer",

    // $ANTLR start "synpred24_KWQLLexer"
    synpred24_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:192: ( ARROW )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:192: ARROW
        this.mARROW(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred24_KWQLLexer",

    // $ANTLR start "synpred25_KWQLLexer"
    synpred25_KWQLLexer_fragment: function() {
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:198: ( SPARQL )
        // E:\\Diplomarbeit\\Implementation\\KQB\\KWQLLexer.g:1:198: SPARQL
        this.mSPARQL(); if (this.state.failed) return ;


    },
    // $ANTLR end "synpred25_KWQLLexer"

    synpred18_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred18_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred10_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred10_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred19_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred19_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred17_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred17_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred14_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred14_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred13_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred13_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred3_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred3_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred8_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred8_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred16_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred16_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred6_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred6_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred2_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred2_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred24_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred24_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred25_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred25_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred7_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred7_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred12_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred12_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred5_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred5_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred4_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred4_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred15_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred15_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred1_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred1_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred11_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred11_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    },
    synpred9_KWQLLexer: function() {
        this.state.backtracking++;
        var start = this.input.mark();
        try {
            this.synpred9_KWQLLexer_fragment(); // can never throw exception
        } catch (re) {
            alert("impossible: "+re.toString());
        }
        var success = !this.state.failed;
        this.input.rewind(start);
        this.state.backtracking--;
        this.state.failed=false;
        return success;
    }
}, true); // important to pass true to overwrite default implementations

org.antlr.lang.augmentObject(KWQLLexer, {
    DFA6_eotS:
        "\u000f\uffff",
    DFA6_eofS:
        "\u000f\uffff",
    DFA6_minS:
        "\u0001\u0055\u0001\u0067\u0001\u0061\u0003\uffff\u0001\u0065\u0008"+
    "\uffff",
    DFA6_maxS:
        "\u0001\u0074\u0002\u0075\u0003\uffff\u0001\u0069\u0008\uffff",
    DFA6_acceptS:
        "\u0003\uffff\u0001\u0003\u0001\u0004\u0001\u0005\u0001\uffff\u0001"+
    "\u0009\u0001\u0001\u0001\u0008\u0001\u000a\u0001\u0002\u0001\u0007\u0001"+
    "\u0006\u0001\u000b",
    DFA6_specialS:
        "\u000f\uffff}>",
    DFA6_transitionS: [
            "\u0001\u0003\u000b\uffff\u0001\u0001\u0001\uffff\u0001\u0007"+
            "\u0001\u0004\u0007\uffff\u0001\u0005\u0001\uffff\u0001\u0002"+
            "\u0005\uffff\u0001\u0006",
            "\u0001\u000a\u0006\uffff\u0001\u0008\u0006\uffff\u0001\u0009",
            "\u0001\u000b\u0013\uffff\u0001\u000c",
            "",
            "",
            "",
            "\u0001\u000e\u0003\uffff\u0001\u000d",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    ]
});

org.antlr.lang.augmentObject(KWQLLexer, {
    DFA6_eot:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA6_eotS),
    DFA6_eof:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA6_eofS),
    DFA6_min:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(KWQLLexer.DFA6_minS),
    DFA6_max:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(KWQLLexer.DFA6_maxS),
    DFA6_accept:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA6_acceptS),
    DFA6_special:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA6_specialS),
    DFA6_transition: (function() {
        var a = [],
            i,
            numStates = KWQLLexer.DFA6_transitionS.length;
        for (i=0; i<numStates; i++) {
            a.push(org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA6_transitionS[i]));
        }
        return a;
    })()
});

KWQLLexer.DFA6 = function(recognizer) {
    this.recognizer = recognizer;
    this.decisionNumber = 6;
    this.eot = KWQLLexer.DFA6_eot;
    this.eof = KWQLLexer.DFA6_eof;
    this.min = KWQLLexer.DFA6_min;
    this.max = KWQLLexer.DFA6_max;
    this.accept = KWQLLexer.DFA6_accept;
    this.special = KWQLLexer.DFA6_special;
    this.transition = KWQLLexer.DFA6_transition;
};

org.antlr.lang.extend(KWQLLexer.DFA6, org.antlr.runtime.DFA, {
    getDescription: function() {
        return "25:9: ( 'anchorText' | 'name' | 'URI' | 'disagree' | 'lastEdited' | 'title' | 'numberEd' | 'author' | 'created' | 'agree' | 'text' )";
    },
    dummy: null
});
org.antlr.lang.augmentObject(KWQLLexer, {
    DFA19_eotS:
        "\u0002\u0002\u000c\uffff",
    DFA19_eofS:
        "\u000e\uffff",
    DFA19_minS:
        "\u0001\u0025\u0001\u002d\u000c\uffff",
    DFA19_maxS:
        "\u0001\u007a\u0001\u007e\u000c\uffff",
    DFA19_acceptS:
        "\u0002\uffff\u0001\u000c\u0001\u0001\u0001\u0002\u0001\u0003\u0001"+
    "\u0004\u0001\u0005\u0001\u0006\u0001\u0007\u0001\u0008\u0001\u0009\u0001"+
    "\u000b\u0001\u000a",
    DFA19_specialS:
        "\u000e\uffff}>",
    DFA19_transitionS: [
            "\u0001\u000a\u0001\u0008\u0004\uffff\u0001\u0009\u0001\uffff"+
            "\u0001\u0007\u0001\u0006\u0001\u0001\u000a\u0005\u0001\u000c"+
            "\u0002\uffff\u0001\u000b\u0003\uffff\u001a\u0004\u0006\uffff"+
            "\u001a\u0003",
            "\u0002\u000d\u0001\uffff\u000a\u000d\u0007\uffff\u001a\u000d"+
            "\u0004\uffff\u0001\u000d\u0001\uffff\u001a\u000d\u0003\uffff"+
            "\u0001\u000d",
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
    ]
});

org.antlr.lang.augmentObject(KWQLLexer, {
    DFA19_eot:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA19_eotS),
    DFA19_eof:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA19_eofS),
    DFA19_min:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(KWQLLexer.DFA19_minS),
    DFA19_max:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(KWQLLexer.DFA19_maxS),
    DFA19_accept:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA19_acceptS),
    DFA19_special:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA19_specialS),
    DFA19_transition: (function() {
        var a = [],
            i,
            numStates = KWQLLexer.DFA19_transitionS.length;
        for (i=0; i<numStates; i++) {
            a.push(org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA19_transitionS[i]));
        }
        return a;
    })()
});

KWQLLexer.DFA19 = function(recognizer) {
    this.recognizer = recognizer;
    this.decisionNumber = 19;
    this.eot = KWQLLexer.DFA19_eot;
    this.eof = KWQLLexer.DFA19_eof;
    this.min = KWQLLexer.DFA19_min;
    this.max = KWQLLexer.DFA19_max;
    this.accept = KWQLLexer.DFA19_accept;
    this.special = KWQLLexer.DFA19_special;
    this.transition = KWQLLexer.DFA19_transition;
};

org.antlr.lang.extend(KWQLLexer.DFA19, org.antlr.runtime.DFA, {
    getDescription: function() {
        return "()+ loopback of 42:12: ( LOWER | UPPER | DIGIT | '.' | '-' | '&' | '+' | '%' | '=' | PATH | ':' )+";
    },
    dummy: null
});
org.antlr.lang.augmentObject(KWQLLexer, {
    DFA34_eotS:
        "\u002f\uffff",
    DFA34_eofS:
        "\u002f\uffff",
    DFA34_minS:
        "\u0001\u0009\u0001\u0000\u0004\uffff\u0001\u0000\u0002\uffff\u0001"+
    "\u0000\u0001\uffff\u0001\u0000\u0004\uffff\u0001\u0000\u0001\uffff\u0001"+
    "\u0000\u0004\uffff\u0002\u0000\u0002\uffff\u0001\u0000\u0001\uffff\u0001"+
    "\u0000\u0002\uffff\u0002\u0000\u0001\uffff\u0001\u0000\u0002\uffff\u0001"+
    "\u0000\u0003\uffff\u0001\u0000\u0001\uffff\u0001\u0000\u0002\uffff",
    DFA34_maxS:
        "\u0001\u007a\u0001\u0000\u0004\uffff\u0001\u0000\u0002\uffff\u0001"+
    "\u0000\u0001\uffff\u0001\u0000\u0004\uffff\u0001\u0000\u0001\uffff\u0001"+
    "\u0000\u0004\uffff\u0002\u0000\u0002\uffff\u0001\u0000\u0001\uffff\u0001"+
    "\u0000\u0002\uffff\u0002\u0000\u0001\uffff\u0001\u0000\u0002\uffff\u0001"+
    "\u0000\u0003\uffff\u0001\u0000\u0001\uffff\u0001\u0000\u0002\uffff",
    DFA34_acceptS:
        "\u0002\uffff\u0001\u0006\u0001\u0009\u0001\u000a\u0001\u0013\u0001"+
    "\uffff\u0001\u0002\u0001\u0010\u0001\uffff\u0001\u0003\u0001\uffff\u0001"+
    "\u000e\u0001\u0017\u0001\u001b\u0001\u0015\u0001\uffff\u0001\u0018\u0001"+
    "\uffff\u0001\u0004\u0001\u000f\u0001\u0014\u0001\u0016\u0002\uffff\u0001"+
    "\u000b\u0001\u000c\u0001\uffff\u0001\u0001\u0001\uffff\u0001\u0011\u0001"+
    "\u0012\u0002\uffff\u0001\u001a\u0001\uffff\u0001\u0005\u0001\u0007\u0001"+
    "\uffff\u0001\u000d\u0001\u0019\u0001\u0013\u0001\uffff\u0001\u000a\u0001"+
    "\uffff\u0001\u0008\u0001\u001c",
    DFA34_specialS:
        "\u0001\uffff\u0001\u0000\u0004\uffff\u0001\u0001\u0002\uffff\u0001"+
    "\u0002\u0001\uffff\u0001\u0003\u0004\uffff\u0001\u0004\u0001\uffff\u0001"+
    "\u0005\u0004\uffff\u0001\u0006\u0001\u0007\u0002\uffff\u0001\u0008\u0001"+
    "\uffff\u0001\u0009\u0002\uffff\u0001\u000a\u0001\u000b\u0001\uffff\u0001"+
    "\u000c\u0002\uffff\u0001\u000d\u0003\uffff\u0001\u000e\u0001\uffff\u0001"+
    "\u000f\u0002\uffff}>",
    DFA34_transitionS: [
            "\u0002\u000f\u0002\uffff\u0001\u000f\u0012\uffff\u0001\u000f"+
            "\u0001\uffff\u0001\u0029\u0001\u002e\u0001\u0015\u0002\uffff"+
            "\u0001\u0029\u0001\u0016\u0001\u000d\u0001\uffff\u0001\u002b"+
            "\u0001\u0018\u0001\u0010\u0001\u002b\u0001\uffff\u000a\u001d"+
            "\u0005\uffff\u0001\u000e\u0001\u0022\u0001\u0001\u0001\u0021"+
            "\u0001\u000b\u000a\u0021\u0001\u002c\u0001\u0023\u0003\u0021"+
            "\u0001\u0026\u0001\u0021\u0001\u0020\u0005\u0021\u0001\uffff"+
            "\u0001\u0029\u0002\uffff\u0001\u0029\u0001\uffff\u0001\u0020"+
            "\u0001\u0021\u0001\u001b\u0001\u0017\u0001\u0021\u0001\u0009"+
            "\u0005\u0021\u0001\u0006\u0001\u0021\u0001\u0020\u0001\u002a"+
            "\u0004\u0021\u0001\u0012\u0006\u0021",
            "\u0001\uffff",
            "",
            "",
            "",
            "",
            "\u0001\uffff",
            "",
            "",
            "\u0001\uffff",
            "",
            "\u0001\uffff",
            "",
            "",
            "",
            "",
            "\u0001\uffff",
            "",
            "\u0001\uffff",
            "",
            "",
            "",
            "",
            "\u0001\uffff",
            "\u0001\uffff",
            "",
            "",
            "\u0001\uffff",
            "",
            "\u0001\uffff",
            "",
            "",
            "\u0001\uffff",
            "\u0001\uffff",
            "",
            "\u0001\uffff",
            "",
            "",
            "\u0001\uffff",
            "",
            "",
            "",
            "\u0001\uffff",
            "",
            "\u0001\uffff",
            "",
            ""
    ]
});

org.antlr.lang.augmentObject(KWQLLexer, {
    DFA34_eot:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA34_eotS),
    DFA34_eof:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA34_eofS),
    DFA34_min:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(KWQLLexer.DFA34_minS),
    DFA34_max:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(KWQLLexer.DFA34_maxS),
    DFA34_accept:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA34_acceptS),
    DFA34_special:
        org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA34_specialS),
    DFA34_transition: (function() {
        var a = [],
            i,
            numStates = KWQLLexer.DFA34_transitionS.length;
        for (i=0; i<numStates; i++) {
            a.push(org.antlr.runtime.DFA.unpackEncodedString(KWQLLexer.DFA34_transitionS[i]));
        }
        return a;
    })()
});

KWQLLexer.DFA34 = function(recognizer) {
    this.recognizer = recognizer;
    this.decisionNumber = 34;
    this.eot = KWQLLexer.DFA34_eot;
    this.eof = KWQLLexer.DFA34_eof;
    this.min = KWQLLexer.DFA34_min;
    this.max = KWQLLexer.DFA34_max;
    this.accept = KWQLLexer.DFA34_accept;
    this.special = KWQLLexer.DFA34_special;
    this.transition = KWQLLexer.DFA34_transition;
};

org.antlr.lang.extend(KWQLLexer.DFA34, org.antlr.runtime.DFA, {
    getDescription: function() {
        return "1:1: Tokens options {k=1; backtrack=true; } : ( CI | LINK | FRAG | TAG | OPTIONAL | CONJUNCTION | DISJUNCTION | NEGATION | UQ | URI | COMMASEP | COMMA | QUANT | COUNT | RESQUAL | QUAL | INT | DA | KW | VARIABLE | WS | LEFT_PAREN | RIGHT_PAREN | ARROW | SPARQL | AT | QUERY | FRAGMENT );";
    },
    specialStateTransition: function(s, input) {
        var _s = s;
        /* bind to recognizer so semantic predicates can be evaluated */
        var retval = (function(s, input) {
            switch ( s ) {
                        case 0 : 
                            var LA34_1 = input.LA(1);

                             
                            var index34_1 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred6_KWQLLexer()) ) {s = 2;}

                            else if ( (this.synpred9_KWQLLexer()) ) {s = 3;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_1);
                            if ( s>=0 ) return s;
                            break;
                        case 1 : 
                            var LA34_6 = input.LA(1);

                             
                            var index34_6 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred2_KWQLLexer()) ) {s = 7;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred16_KWQLLexer()) ) {s = 8;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_6);
                            if ( s>=0 ) return s;
                            break;
                        case 2 : 
                            var LA34_9 = input.LA(1);

                             
                            var index34_9 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred3_KWQLLexer()) ) {s = 10;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_9);
                            if ( s>=0 ) return s;
                            break;
                        case 3 : 
                            var LA34_11 = input.LA(1);

                             
                            var index34_11 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred14_KWQLLexer()) ) {s = 12;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_11);
                            if ( s>=0 ) return s;
                            break;
                        case 4 : 
                            var LA34_16 = input.LA(1);

                             
                            var index34_16 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred24_KWQLLexer()) ) {s = 17;}

                             
                            input.seek(index34_16);
                            if ( s>=0 ) return s;
                            break;
                        case 5 : 
                            var LA34_18 = input.LA(1);

                             
                            var index34_18 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred4_KWQLLexer()) ) {s = 19;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred15_KWQLLexer()) ) {s = 20;}

                            else if ( (this.synpred16_KWQLLexer()) ) {s = 8;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_18);
                            if ( s>=0 ) return s;
                            break;
                        case 6 : 
                            var LA34_23 = input.LA(1);

                             
                            var index34_23 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred15_KWQLLexer()) ) {s = 20;}

                            else if ( (this.synpred16_KWQLLexer()) ) {s = 8;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_23);
                            if ( s>=0 ) return s;
                            break;
                        case 7 : 
                            var LA34_24 = input.LA(1);

                             
                            var index34_24 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred11_KWQLLexer()) ) {s = 25;}

                            else if ( (this.synpred12_KWQLLexer()) ) {s = 26;}

                             
                            input.seek(index34_24);
                            if ( s>=0 ) return s;
                            break;
                        case 8 : 
                            var LA34_27 = input.LA(1);

                             
                            var index34_27 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred1_KWQLLexer()) ) {s = 28;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred15_KWQLLexer()) ) {s = 20;}

                            else if ( (this.synpred16_KWQLLexer()) ) {s = 8;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_27);
                            if ( s>=0 ) return s;
                            break;
                        case 9 : 
                            var LA34_29 = input.LA(1);

                             
                            var index34_29 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred17_KWQLLexer()) ) {s = 30;}

                            else if ( (this.synpred18_KWQLLexer()) ) {s = 31;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_29);
                            if ( s>=0 ) return s;
                            break;
                        case 10 : 
                            var LA34_32 = input.LA(1);

                             
                            var index34_32 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred16_KWQLLexer()) ) {s = 8;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_32);
                            if ( s>=0 ) return s;
                            break;
                        case 11 : 
                            var LA34_33 = input.LA(1);

                             
                            var index34_33 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_33);
                            if ( s>=0 ) return s;
                            break;
                        case 12 : 
                            var LA34_35 = input.LA(1);

                             
                            var index34_35 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred5_KWQLLexer()) ) {s = 36;}

                            else if ( (this.synpred7_KWQLLexer()) ) {s = 37;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                             
                            input.seek(index34_35);
                            if ( s>=0 ) return s;
                            break;
                        case 13 : 
                            var LA34_38 = input.LA(1);

                             
                            var index34_38 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred13_KWQLLexer()) ) {s = 39;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 5;}

                            else if ( (this.synpred25_KWQLLexer()) ) {s = 40;}

                             
                            input.seek(index34_38);
                            if ( s>=0 ) return s;
                            break;
                        case 14 : 
                            var LA34_42 = input.LA(1);

                             
                            var index34_42 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred10_KWQLLexer()) ) {s = 4;}

                            else if ( (this.synpred15_KWQLLexer()) ) {s = 20;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 41;}

                             
                            input.seek(index34_42);
                            if ( s>=0 ) return s;
                            break;
                        case 15 : 
                            var LA34_44 = input.LA(1);

                             
                            var index34_44 = input.index();
                            input.rewind();
                            s = -1;
                            if ( (this.synpred8_KWQLLexer()) ) {s = 45;}

                            else if ( (this.synpred10_KWQLLexer()) ) {s = 43;}

                            else if ( (this.synpred19_KWQLLexer()) ) {s = 41;}

                             
                            input.seek(index34_44);
                            if ( s>=0 ) return s;
                            break;
            }
        }).call(this.recognizer, s, input);
        if (!org.antlr.lang.isUndefined(retval)) {
            return retval;
        }
        if (this.recognizer.state.backtracking>0) {this.recognizer.state.failed=true; return -1;}
        var nvae =
            new org.antlr.runtime.NoViableAltException(this.getDescription(), 34, _s, input);
        this.error(nvae);
        throw nvae;
    },
    dummy: null
});
 
})();