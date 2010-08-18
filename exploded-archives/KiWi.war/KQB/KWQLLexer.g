lexer grammar KWQLLexer;

options {language=JavaScript;filter=true;}

CI	:	'ci';
LINK	:	'link';
FRAG	:	'fragment';
TAG	:	'tag';

OPTIONAL	: 'OPTIONAL';	
CONJUNCTION: 'AND';
DISJUNCTION: 'OR';
NEGATION: 	'NOT';
UQ	:	'ALL';
URI : SCHEME'//' HOST PATH? QUERY? FRAGMENT?;
COMMASEP	:	',''"'(SYMBOL|NAME)+'"'{ 
this.setText(this.getText().substring(2, (this.getText().length)-1)); 
} ;
COMMA	:	',';
QUANT	:	'SOME';
COUNT	:	'COUNT';
RESQUAL	:	('target'|'origin'|'descendant'|'child')':'{ 
this.setText(this.getText().substring(0, (this.getText().length)-1)); 	
} ;
QUAL 	:	('anchorText'|'name'|'URI'|'disagree'
		|'lastEdited'|'title'|'numberEd'|'author'|'created'
		|'agree'|'text') ':'{ 
this.setText(this.getText().substring(0, (this.getText().length)-1)); 	
} ; 
INT	:	DIGIT+;
DA	:	ISOTIME;
KW	: 	('"' (NAME|EscNAME)+ (' '+(NAME|EscNAME)+)* '"')|NAME|('\'' (NAME|EscNAME)+ (' '+(NAME|EscNAME)+)* '\'');	
VARIABLE:	'$' NAME { 
this.setText(this.getText().substring(1, this.getText().length)); 
} ;
WS	:	 (' '|'\t'|'\n'|'\r')+ {$channel=HIDDEN;} ;
LEFT_PAREN:	'(';
RIGHT_PAREN: 	')';
ARROW	:	'->';
SPARQL	:	'SPARQL:('.*')';
AT	:	'@';
QUERY	:	'?'(LOWER|UPPER|DIGIT|'.'|'-'|'&'|'+'|'%'|'='|PATH|':')+'/'?;
FRAGMENT:	'#'(LOWER|UPPER|'+'|'.'|'-'|'_')+;

fragment PATH	:	('/''~'?(LOWER|UPPER|DIGIT|'.'|'-'|'_')+)+'/'?;	
fragment SCHEME	:	(LOWER|UPPER|'+'|'.'|'-')+':';
fragment HexPrefix: '0x' | '0X' ; 
fragment HexDigit: ('0'..'9'|'a'..'f'|'A'..'F') ;
fragment NAME: (('\\$')|'\\')?(LETTER | DIGIT | '_' | '\\:')+ ;
fragment EscNAME: ('$'|':')+ ;
fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';
fragment DIGIT: '0'..'9';
fragment SYMBOL: '*'|','|'<'|'>'|'.'|'/'|'|'|'#'|'!'|'@'|'-'; 
fragment HOST	:	(LOWER|UPPER|DIGIT|'.'|'-')+(':'DIGIT+)?;
fragment ISOTIME:	DIGIT DIGIT DIGIT DIGIT'-'DIGIT DIGIT'-'DIGIT DIGIT'T'DIGIT DIGIT':'DIGIT DIGIT':'DIGIT DIGIT'Z';
