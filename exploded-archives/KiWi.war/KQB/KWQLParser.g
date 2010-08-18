parser grammar KWQLParser;


options { 
  output = AST; 
  language = JavaScript;
  tokenVocab = KWQLLexer; 
  backtrack=true;
} 

tokens {VAR;
	LABEL;
	QUALIFIER;
	RESOURCE;
	TYPE;
	AND;
	RULE;
	FUNC;
	SEPARATOR;
	NUM;
	HEAD;
	BODY;
	CI;
	INTEGER;
	DATE;
	STRING;
	UR;}
	

start: rule;


rule        : (head AT)?body EOF -> ^(RULE ^(HEAD head?) ^(BODY body));

head        : (elquant|count|cresource|cvalue)+;
elquant     : (UQ LEFT_PAREN VARIABLE COMMA COMMASEP RIGHT_PAREN->^(UQ ^(VAR VARIABLE) ^(SEPARATOR COMMASEP)))
		|(QUANT LEFT_PAREN DIGIT COMMA VARIABLE COMMASEP RIGHT_PAREN->^(QUANT ^(NUM DIGIT) ^(VAR VARIABLE) ^(SEPARATOR COMMASEP)));
count       : COUNT LEFT_PAREN VARIABLE RIGHT_PAREN ->^(COUNT VARIABLE);
cresource    : res LEFT_PAREN cqualifierterm RIGHT_PAREN ->^(RESOURCE ^(TYPE res) cqualifierterm);
cqualifierterm    : (cqualifier->cqualifier)(CONJUNCTION* c=cqualifier->^(CONJUNCTION $cqualifierterm $c))*;
cqualifier   : QUAL cvalue ->^(QUALIFIER ^(LABEL QUAL) cvalue);
cvalue       : (VARIABLE->^(VAR VARIABLE))|(val->^(val))|(elquant->^(FUNC elquant))|(cresource->^(RESOURCE cresource));
// mu;tiple value text construction
//ci optional
//ci(tag(name:New York))
//SOLVED:ci(tag(name:(Java)) link(target:ci(title:XML))) 
//SOLVED: ci(tag(name:"Java") AND tag(name:"XML"))
//qual ohne res

body		:	(tmp2-> ^(RESOURCE ^(TYPE) tmp2))|(rterm -> rterm|(qterm->^(RESOURCE ^(TYPE) qterm))|(valueterm ->^(RESOURCE ^(TYPE) ^(QUALIFIER LABEL ^(valueterm)))));


rterm	:		(tmpresource->tmpresource) (DISJUNCTION r=tmpresource->^(DISJUNCTION $rterm $r))*
			| NEGATION LEFT_PAREN rterm3 RIGHT_PAREN->^(NEGATION rterm3);
rterm3	:	(tmpresource->tmpresource) (DISJUNCTION r=tmpresource->^(DISJUNCTION $rterm3 $r))+;
//rterm		:	(resource->resource) (rcon r=tmp2 -> ^(rcon $rterm $r))*;
rcon		: 	(connective->connective)|->AND;
rcon2	:	(optional->optional);
resource	: 	((res  LEFT_PAREN tmp4 RIGHT_PAREN)->^(RESOURCE ^(TYPE res) tmp4))
			| NEGATION res  LEFT_PAREN tmp4 RIGHT_PAREN->^(NEGATION ^(RESOURCE ^(TYPE res) tmp4))
			|NEGATION LEFT_PAREN res  LEFT_PAREN tmp4 RIGHT_PAREN RIGHT_PAREN->^(NEGATION ^(RESOURCE ^(TYPE res) tmp4));
			
minresource	: 	((resmin  LEFT_PAREN tmp4 RIGHT_PAREN)-> ^(RESOURCE ^(TYPE resmin) tmp4))
			| NEGATION resmin  LEFT_PAREN tmp4 RIGHT_PAREN->^(NEGATION ^(RESOURCE ^(TYPE resmin) tmp4))
			|NEGATION LEFT_PAREN resmin  LEFT_PAREN tmp4 RIGHT_PAREN RIGHT_PAREN->^(NEGATION ^(RESOURCE ^(TYPE resmin) tmp4));
			
			
ciresource	: 	((resci  LEFT_PAREN tmp4 RIGHT_PAREN)->^(RESOURCE ^(TYPE resci) tmp4))
			| NEGATION resci  LEFT_PAREN tmp4 RIGHT_PAREN->^(NEGATION ^(RESOURCE ^(TYPE resci) tmp4))
			|NEGATION LEFT_PAREN resci  LEFT_PAREN tmp4 RIGHT_PAREN RIGHT_PAREN->^(NEGATION ^(RESOURCE ^(TYPE resci) tmp4));
tmpresource	:	(minresource->^(RESOURCE ^(TYPE) minresource))
			|(ciresource->ciresource)
			|(qterm->^(RESOURCE ^(TYPE) qterm))
			|(valueterm ->^(RESOURCE ^(TYPE) ^(QUALIFIER LABEL ^(valueterm))));


			//|//(LEFT_PAREN rterm RIGHT_PAREN -^(RESOURCE rterm));
			//|(res LEFT_PAREN resource RIGHT_PAREN ->^(RESOURCE res resource));//
//tmp2		:	(qterm|(valueterm->^(QUALIFIER LABEL ^(VALUE valueterm)))|(resource -> resource)) rcon r=tmp2;
tmp2		:	NEGATION LEFT_PAREN tmp22 RIGHT_PAREN->^(NEGATION tmp22)
			|tmp22;
tmp22		:	(tmp5 rcon2)=>(tmp5->tmp5) (rcon2 r=tmp5 ->^(AND $tmp22 ^(rcon2 $r)))+
			|((tmp5->tmp5) (rcon r=tmp5 -> ^(rcon $tmp22 $r))+);
tmp4		:	(tmp3 rcon2)=>(tmp3->tmp3) (rcon2 r=tmp3 -> ^(AND $tmp4 ^(rcon2 $r)))*
			|((tmp3->tmp3) (rcon r=tmp3 -> ^(rcon $tmp4 $r))*);

tmp5	:	tmp52
		|NEGATION LEFT_PAREN tmp52 RIGHT_PAREN->^(NEGATION tmp52)
		|NEGATION tmp52 ->^(NEGATION tmp52);
tmp52	:	((qterm->qterm)
		|(valueterm->^(QUALIFIER LABEL ^(valueterm)))
		|(minresource->minresource)
		|(LEFT_PAREN tmp2 RIGHT_PAREN -> tmp2));
tmp3	:	((qterm->qterm)
		|(valueterm->^(QUALIFIER LABEL ^(valueterm)))
		|(minresource->minresource)
		|(LEFT_PAREN tmp4 RIGHT_PAREN -> tmp4));
tmp6	:	(minresource->minresource)
		|(ciresource->ciresource)
		|(value->^(RESOURCE ^(TYPE) ^(QUALIFIER ^(LABEL) ^(value))))
		|(qualifier->qualifier)->^(RESOURCE ^(TYPE)qualifier)
		|(LEFT_PAREN body RIGHT_PAREN -> body);

//qterm		:	(((qualifier->qualifier)) (valqual r=qualifier -> ^(valqual $qterm $r))*);
//qterm		:	(((qualifier->qualifier)) (valqual r=qualifier -> ^( $qterm ^(valqual $r)))*);
qterm	:	(qualifier valqual2)=>(((qualifier->qualifier)) (valqual2 r=qualifier-> ^(AND $qterm ^(valqual2 $r)))*)
		|(((qualifier->qualifier)) (valqual r=qualifier -> ^(valqual $qterm $r))*);

tmp		:	qualifier|value;
valqual		:	(connective->connective)|->AND;//|(OPTIONAL->OPTIONAL);
valqual2:	(optional->optional);

qualifier	:	(QUAL valueterm ARROW VARIABLE)->^(QUALIFIER ^(LABEL QUAL) ^(valueterm) ^(VAR VARIABLE))
			|(QUAL LEFT_PAREN valueterm ARROW VARIABLE RIGHT_PAREN)->^(QUALIFIER ^(LABEL QUAL) ^(valueterm) ^(VAR VARIABLE))
			|(QUAL VARIABLE->^(QUALIFIER ^(LABEL QUAL) ^(VAR VARIABLE)))
			//|(QUAL valueterm->^(QUALIFIER ^(LABEL QUAL) ^(valueterm)))
			|(QUAL value->^(QUALIFIER ^(LABEL QUAL) ^(value)))
			| (RESQUAL tmp6 ->^(QUALIFIER ^(LABEL RESQUAL) ^(tmp6)))
			|(LEFT_PAREN qterm RIGHT_PAREN ->^(qterm))
			|(NEGATION LEFT_PAREN qterm RIGHT_PAREN ->^(NEGATION qterm));
			
// TODO: nach descendant keine volle resource. qualifier aufteilen?
//rqual	:	(CIQUAL|FCIQUAL|FTCIQUAL);

valueterm	:	(value->value) (valcon v=value -> ^( valcon $valueterm $v))*;
valcon		:	(connective->connective)|->AND;
value		:	(NEGATION val ->^(NEGATION val))
			| (val -> val)
			|(NEGATION LEFT_PAREN valueterm RIGHT_PAREN ->^(NEGATION valueterm))
			|(LEFT_PAREN valueterm RIGHT_PAREN -> valueterm);

val	:	(INT->^(INTEGER INT))|(DA->^(DATE DA))|(KW->^(STRING KW))|(URI->^(UR URI));
res		:	(CI|LINK|FRAG|TAG);
resmin	:	(LINK|FRAG|TAG);
resci	:	CI;
connective: 	(DISJUNCTION|CONJUNCTION);
optional:	OPTIONAL LEFT_PAREN KW RIGHT_PAREN ->^(OPTIONAL KW);
