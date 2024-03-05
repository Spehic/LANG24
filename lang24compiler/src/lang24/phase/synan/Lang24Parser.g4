parser grammar Lang24Parser;

@header {

	package lang24.phase.synan;
	
	import java.util.*;
	import lang24.common.report.*;
	import lang24.data.token.*;
	import lang24.phase.lexan.*;

}

@members {

	private Location loc(Token tok) { return new Location((LocLogToken)tok); }
	private Location loc(Token     tok1, Token     tok2) { return new Location((LocLogToken)tok1, (LocLogToken)tok2); }
	private Location loc(Token     tok1, Locatable loc2) { return new Location((LocLogToken)tok1, loc2); }
	private Location loc(Locatable loc1, Token     tok2) { return new Location(loc1, (LocLogToken)tok2); }
	private Location loc(Locatable loc1, Locatable loc2) { return new Location(loc1, loc2); }

}

options{
    tokenVocab=Lang24Lexer;
}


source: definitions;

definitions: 	 ( type_definition | variable_definition | function_definition )+ ;

type_definition: IDENTIFIER '=' type ;
variable_definition: IDENTIFIER ':' type ;
function_definition: IDENTIFIER '(' (parameters)? ')' ':' type ('=' statement ('{' definitions '}')?)? ;

//parameters: ('^')? IDENTIFER ':' type ( ',' ('^')? IDENTIFIER ':' type)* ;
parameters: ('^')? IDENTIFIER ':' type ( ',' ('^')? IDENTIFIER ':' type )*;

statement: 	expression ';'
		| expression '=' expression ';' 
		| 'if' expression 'then' statement ('else' statement)?
		| 'while' expression ':' statement
		| 'return' expression ';'
		| '{' (statement)+ '}' ;

type: 	VOID | BOOL | CHAR | INT
	| '[' INT_CONST ']' IDENTIFIER 
	| '^' type
	| '(' components ')'
	| '{' components '}'
	| IDENTIFIER ;

components:	IDENTIFIER ':' type (',' IDENTIFIER ':' type)* ;

expression: 	NONE | TRUE | FALSE | INT_CONST | CHAR_LIT | STRING_LIT | NIL
		| IDENTIFIER ( '(' (expression (',' expression)* )? ')' )?
		| expression '[' expression ']' | expression '^' | expression '.' IDENTIFIER
		| 'not' expression | '+' expression | '-' expression | '^' expression | '<' type '>' expression
		| expression ( '*' | '/' | '%' ) expression 
		| expression ( '+' | '-' ) expression 
		| expression ( '==' | '!=' | '<' | '>' | '<=' | '>=' ) expression  
		| expression 'and' expression | expression 'or' expression 
		| 'sizeof' '(' expression ')'
		| '(' expression ')' ;







