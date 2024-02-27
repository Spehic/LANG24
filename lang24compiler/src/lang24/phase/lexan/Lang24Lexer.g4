lexer grammar Lang24Lexer;

@header {
	package lang24.phase.lexan;
	import lang24.common.report.*;
	import lang24.data.token.*;
}

@members {
    @Override
	public LocLogToken nextToken() {
		return (LocLogToken) super.nextToken();
	}
}

AND:		'and';
BOOL:		'bool';
CHAR:		'char';
ELSE:		'else';
IF:		'if';
INT:		'int';
NIL:		'nil';
NONE:		'none';
NOT:		'not';
OR:		'or';
SIZEOF:		'sizeof';
THEN:		'then';
RETURN:		'return';
VOID:		'void';
WHILE:		'while';


LBRACKET: 	'(';
RBRACKET: 	')';
LCURLY: 	'{';
RCURLY: 	'}';
LSQUARE: 	'[';
RSQUARE: 	']';
DOT: 		'.';
COMMA: 		',';
COL: 		':';
SEMICOL: 	';';
EQUAL: 		'==';
NOTEQUAL: 	'!=';
LESSTHAN: 	'<';
GREATERTHAN: 	'>';
LESSEREQ:	'<=';
GREATERQ:	'>=';
MUL:		'*';
DIV:		'\\';
MOD:		'%';
PLUS: 		'+';
MINUS: 		'-';
POWER:		'^';
ASSIGN:		'=';


IDENTIFIER: 	[a-zA-Z_][a-zA-Z0-9_]*;
INT_LIT:	[0-9]+;
WHITESPACE: (' ' | '\n' | '\r' | '\t') -> skip;
COMMENT: '#' ~[\r\n]* '\r'? '\n' -> skip;
