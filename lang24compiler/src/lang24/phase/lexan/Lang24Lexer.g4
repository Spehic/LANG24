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
DIV:		'/';
MOD:		'%';
PLUS: 		'+';
MINUS: 		'-';
POWER:		'^';
ASSIGN:		'=';


IDENTIFIER: 	[a-zA-Z_][a-zA-Z0-9_]*;
INT_LIT:	[0-9]+;
CHAR_LIT: '\'' CHARACTER '\'';
STRING_LIT: '"' SCHARACTER* '"';
fragment CHARACTER: ([\u0020-\u005B] | [\u005D-\u005F] | [\u0061-\u007E] | '\\n' | '\\\\' | '\\\'' | HEX_CODE);
fragment SCHARACTER: ([\u0020-\u0021] | [\u0023-\u005B] | [\u005D-\u007E] | '\\n' | '\\\\' | '\\"' | HEX_CODE);
fragment HEX_CODE : '\\'[0-9A-F][0-9A-F];


WHITESPACE: (' ' | '\n' | '\r') -> skip;
TAB:	'\t' {setCharPositionInLine((getCharPositionInLine() + 7)/8 * 8);skip();};
COMMENT: '#' ~[\r\n]* '\r'? '\n' -> skip;


