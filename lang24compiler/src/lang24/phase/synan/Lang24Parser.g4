parser grammar Lang24Parser;

@header {

	package lang24.phase.synan;
	
	import java.util.*;
	import lang24.common.report.*;
	import lang24.data.token.*;
	import lang24.data.ast.tree.*;
	import lang24.data.ast.tree.defn.*;
	import lang24.data.ast.tree.expr.*;
	import lang24.data.ast.tree.stmt.*;
	import lang24.data.ast.tree.type.*;
	import lang24.phase.lexan.*;

}

@members {

	private Location loc(Token tok) { return new Location((LocLogToken)tok); }
	private Location loc(Token     tok1, Token     tok2) { return new Location((LocLogToken)tok1, (LocLogToken)tok2); }
	private Location loc(Token     tok1, Locatable loc2) { return new Location((LocLogToken)tok1, loc2); }
	private Location loc(Locatable loc1, Token     tok2) { return new Location(loc1, (LocLogToken)tok2); }
	private Location loc(Locatable loc1, Locatable loc2) { return new Location(loc1, loc2); }
	
	private AstBinExpr.Oper getBinOp(String str){
		return switch(str){
			case "and" -> AstBinExpr.Oper.OR;
			case "or" -> AstBinExpr.Oper.AND;
			case "==" -> AstBinExpr.Oper.EQU;
			case "!=" -> AstBinExpr.Oper.NEQ;
			case "<" -> AstBinExpr.Oper.LTH;
			case ">" -> AstBinExpr.Oper.GTH;
			case ">=" -> AstBinExpr.Oper.GEQ;
			case "<=" -> AstBinExpr.Oper.LEQ;
			case "+" -> AstBinExpr.Oper.ADD;
			case "-" -> AstBinExpr.Oper.SUB;
			case "*" -> AstBinExpr.Oper.MUL;
			case "/" -> AstBinExpr.Oper.DIV;
			case "%" -> AstBinExpr.Oper.MOD;
			default -> null;
		};
	}
}

options{
    tokenVocab=Lang24Lexer;
}


source returns [AstNodes ast]: d=definitions {$ast = new AstNodes($d.ast);} ;

definitions returns [ArrayList<AstDefn> ast]
@init{
	$ast = new ArrayList<AstDefn>();
}
		:
		( t=type_definition {$ast.add($t.ast);}
		| v=variable_definition {$ast.add($v.ast);}
		| f=function_definition {$ast.add($f.ast);}
		)+ ;

type_definition returns [AstTypDefn ast] 
		: i=IDENTIFIER '=' t=type { $ast = new AstTypDefn( loc($i, $t.l), $i.text, $t.ast);};

variable_definition returns [AstVarDefn ast]
		: i=IDENTIFIER ':' t=type { $ast = new AstVarDefn( loc($i, $t.l) , $i.text, $t.ast);};

function_definition returns [AstFunDefn ast, Location l]
		: i=IDENTIFIER '(' (parameters)? ')' ':' type ('=' s=statement ('{' d=definitions '}')?)? 
		{$l=loc($i);};

parameters 
@init{

}
		:('^')? ia=IDENTIFIER ':' ta=type ( ',' ('^')? IDENTIFIER ':' type )*; 


statement returns [AstStmt ast, Location l]
locals[ ArrayList<AstStmt> arr]
@init{
	ArrayList<AstStmt> arr = new ArrayList<AstStmt>();
}
		: 	
		e=expression r=';' {$l = loc($e.l, $r); $ast = new AstExprStmt($l, $e.ast);}
		| ls=expression '=' e=expression r=';' {$l = loc($ls.l, $r); $ast = new AstAssignStmt($l, $ls.ast, $e.ast);} 
		| i='if' e=expression 'then' s=statement ('else' ss=statement)? {$l = loc($i); $ast = new AstIfStmt($l, $e.ast, $s.ast, $ss.ast);}
		| w='while' e=expression ':' s=statement { $l = loc($w, $e.l); $ast = new AstWhileStmt($l, $e.ast, $s.ast);}
		| r='return' e=expression k=';' { $l = loc($r, $e.l); $ast = new AstReturnStmt($l, $e.ast);}
		| ll='{' (s=statement)+ r='}' {$l = loc($ll, $r); $arr.add($s.ast); $ast = new AstBlockStmt($l, $arr);} 
		;

type returns [AstType ast, Location l]
	: v=VOID {$ast = new AstAtomType(loc($v), AstAtomType.Type.VOID); $l = loc($v);} 
	| b=BOOL {$ast = new AstAtomType(loc($b), AstAtomType.Type.BOOL); $l = loc($b);} 
	| c=CHAR {$ast = new AstAtomType(loc($c), AstAtomType.Type.CHAR); $l = loc($c);} 
	| i=INT {$ast = new AstAtomType(loc($i), AstAtomType.Type.INT); $l = loc($i);} 
	| s='[' i=INT_CONST ']' t=type {$l = loc($s, $t.l); $ast = new AstArrType($l, $t.ast, new AstAtomExpr(loc($i), AstAtomExpr.Type.INT, $i.text));} 
	| '^' type
	| '(' components ')'
	| '{' components '}'
	| i=IDENTIFIER {$l = loc($i); $ast = new AstNameType($l, $i.text);}
	;

components:	IDENTIFIER ':' type (',' IDENTIFIER ':' type)* ;

expression returns[AstExpr ast, Location l]
		: n=NONE {$ast = new AstAtomExpr(loc($n), AstAtomExpr.Type.VOID, $n.text); $l = loc($n);} 
		| t=TRUE {$ast = new AstAtomExpr(loc($t), AstAtomExpr.Type.BOOL, $t.text); $l = loc($t);}
		| f=FALSE {$ast = new AstAtomExpr(loc($f), AstAtomExpr.Type.BOOL, $f.text); $l = loc($f);}
		| i=INT_CONST  {$ast = new AstAtomExpr(loc($i), AstAtomExpr.Type.INT, $i.text); $l = loc($i);}
		| c=CHAR_LIT {$ast = new AstAtomExpr(loc($c), AstAtomExpr.Type.CHAR, $c.text); $l = loc($c);}
		| s=STRING_LIT {$ast = new AstAtomExpr(loc($s), AstAtomExpr.Type.STR, $s.text); $l = loc($s);} 
		| n=NIL {$ast = new AstAtomExpr(loc($n), AstAtomExpr.Type.PTR, $n.text); $l = loc($n);}
		| i=IDENTIFIER ( '(' (expression (',' expression)* )? ')' )? 
		| e=expression '[' INT_CONST b=']' 
		| e=expression b='^' { $ast = new AstSfxExpr(loc($e.l, $b), AstSfxExpr.Oper.PTR, $e.ast);} 
		| e=expression '.' i=IDENTIFIER {$l=loc($e.l, $i); $ast = new AstCmpExpr($l, $e.ast, $i.text); }
		| ae='not' e=expression { $l = loc($ae, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.NOT, $e.ast);}
		| aa='+' e=expression { $l = loc($aa, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.ADD, $e.ast);}
		| ab='-' e=expression { $l = loc($ab, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.SUB, $e.ast);}
		| ac='^' e=expression { $l = loc($ac, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.PTR, $e.ast);}
		| ad='<' tb=type '>' e=expression { $l = loc($ad, $e.l); $ast = new AstCastExpr($l, $tb.ast, $e.ast); }
		| as=expression op=( '*' | '/' | '%' ) ba=expression { $l =loc($as.l, $ba.l); new AstBinExpr($l, getBinOp($op.text), $as.ast, $ba.ast);} 
		| as=expression ( '+' | '-' ) ba=expression { $l =loc($as.l, $ba.l); new AstBinExpr($l, getBinOp($op.text), $as.ast, $ba.ast);} 
		| as=expression ( '==' | '!=' | '<' | '>' | '<=' | '>=' ) ba=expression { $l =loc($as.l, $ba.l); new AstBinExpr($l, getBinOp($op.text), $as.ast, $ba.ast);}
		| as=expression 'and' ba=expression { $l = loc($as.l, $ba.l); new AstBinExpr($l, AstBinExpr.Oper.AND, $as.ast, $ba.ast);}
		| as=expression 'or' ba=expression { $l =loc($as.l, $ba.l); new AstBinExpr($l, AstBinExpr.Oper.OR, $as.ast, $ba.ast);}
		| s='sizeof' '(' ta=type r=')' {$l = loc($s, $r); $ast = new AstSizeofExpr($l, $ta.ast);}
		| ls='(' e=expression r=')' {$l = loc($ls, $r); $ast = $e.ast;}  
		;







