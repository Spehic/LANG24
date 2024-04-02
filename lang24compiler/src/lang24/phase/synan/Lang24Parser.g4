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
			case "and" -> AstBinExpr.Oper.AND;
			case "or" -> AstBinExpr.Oper.OR;
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


source returns [AstNodes ast]: d=definitions {$ast = $d.ast;} ;

definitions returns [AstNodes<AstDefn> ast]
@init{
	ArrayList<AstDefn> arr = new ArrayList<AstDefn>();
}
@after{
	$ast = new AstNodes<AstDefn>(arr);
}
		:
		( t=type_definition {arr.add($t.ast);}
		| v=variable_definition {arr.add($v.ast);}
		| f=function_definition {arr.add($f.ast);}
		)+ ;

type_definition returns [AstTypDefn ast] 
		: i=IDENTIFIER '=' t=type { $ast = new AstTypDefn( loc($i, $t.l), $i.text, $t.ast);};

variable_definition returns [AstVarDefn ast]
		: i=IDENTIFIER ':' t=type { $ast = new AstVarDefn( loc($i, $t.l) , $i.text, $t.ast);};

function_definition returns [AstFunDefn ast, Location l]
		: i=IDENTIFIER '(' (p=parameters)? ')' ':' t=type ('=' s=statement ('{' d=definitions endd='}')?)? 
		{ 	
			if($d.ctx != null)
				$l=loc($i, $endd);
			else if($s.ctx != null)
				$l=loc($i, $s.l);
			else 
				$l = loc($i, $t.l);
			AstNodes<AstFunDefn.AstParDefn> params = null;
			AstNodes<AstDefn> defns = null;
			AstStmt stmts = null;
			if($p.ctx != null) params = $p.pars;
			if($d.ctx != null) defns = $d.ast;
			if($s.ctx != null) stmts = $s.ast;
			$ast = new AstFunDefn($l,$i.text,params,$t.ast,stmts,defns);	
		};

parameters returns [AstNodes<AstFunDefn.AstParDefn> pars]
@init{
	ArrayList<AstFunDefn.AstParDefn> arr = new ArrayList<>();
	Location l;
	boolean flag;
}
@after{
	if(flag) $pars = null;
	else $pars = new AstNodes(arr);
}
		:{flag = true;}
		ta=tick ia=IDENTIFIER ':' t=type 
		{
			flag = false;
			if($ta.b){
				l = loc($ta.l, $t.l);		
				arr.add(new AstFunDefn.AstRefParDefn(l, $ia.text, $t.ast));
			} else{
				l = loc($ia, $t.l);		
				arr.add(new AstFunDefn.AstValParDefn(l, $ia.text, $t.ast));
			}
		}

		( o=other_params {  if($o.ctx != null) arr.add($o.othrs);} )*
		; 


other_params returns [AstFunDefn.AstParDefn othrs]
@init{
	Location l;
}
@after{
}
		: ',' tb=tick i=IDENTIFIER ':' t=type {
			if($tb.b){
				l = loc($tb.l, $t.l);		
				$othrs = new AstFunDefn.AstRefParDefn(l, $i.text, $t.ast);
			} else{
				l = loc($i, $t.l);		
				$othrs = new AstFunDefn.AstValParDefn(l, $i.text, $t.ast);
			}
		}
		;


tick returns [boolean b, Location l]
	: {$b = false;}
	| a='^' {$b = true; $l = loc($a);};


statement returns [AstStmt ast, Location l]
@init{
	ArrayList<AstStmt> arr = new ArrayList<AstStmt>();
}
		: 	
		e=expression r=';' {$l = loc($e.l, $r); $ast = new AstExprStmt($l, $e.ast);}
		| ls=expression '=' e=expression r=';' {$l = loc($ls.l, $r); $ast = new AstAssignStmt($l, $ls.ast, $e.ast);} 
		| w='while' e=expression ':' s=statement { $l = loc($w, $s.l); $ast = new AstWhileStmt($l, $e.ast, $s.ast);}
		| r='return' e=expression k=';' { $l = loc($r, $k); $ast = new AstReturnStmt($l, $e.ast);}
		| ll='{' (s=statement {arr.add($s.ast);})+ r='}'  {$l = loc($ll, $r);  $ast = new AstBlockStmt($l, arr);}
		| i='if' e=expression 'then' s=statement ('else' ss=statement)? 
		{
			$l = loc($i, $s.l);
			if($ss.ctx != null){
				$l = loc($i, $ss.l); 
				$ast = new AstIfStmt($l, $e.ast, $s.ast, $ss.ast);
			}
			else{
				$ast = new AstIfStmt($l, $e.ast, $s.ast, null);
			}	
		}
		;

type returns [AstType ast, Location l]
	: v=VOID {$ast = new AstAtomType(loc($v), AstAtomType.Type.VOID); $l = loc($v);} 
	| b=BOOL {$ast = new AstAtomType(loc($b), AstAtomType.Type.BOOL); $l = loc($b);} 
	| c=CHAR {$ast = new AstAtomType(loc($c), AstAtomType.Type.CHAR); $l = loc($c);} 
	| i=INT {$ast = new AstAtomType(loc($i), AstAtomType.Type.INT); $l = loc($i);} 
	| s='[' i=INT_CONST ']' t=type {$l = loc($s, $t.l); $ast = new AstArrType($l, $t.ast, new AstAtomExpr(loc($i), AstAtomExpr.Type.INT, $i.text));} 
	| a='^' t=type {$l = loc($a, $t.l); $ast = new AstPtrType($l, $t.ast);}
	| lb='(' cm=components r=')' {$l = loc($lb, $r); $ast = new AstStrType($l, $cm.ast);}
	| lb='{' cm=components r='}' {$l = loc($lb, $r); $ast = new AstUniType($l, $cm.ast);}
	| i=IDENTIFIER {$l = loc($i); $ast = new AstNameType($l, $i.text);}
	;

components returns [AstNodes<AstRecType.AstCmpDefn> ast]
@init{
	ArrayList<AstRecType.AstCmpDefn> arr = new ArrayList<>();
	Location l;
}
@after{
	$ast = new AstNodes<AstRecType.AstCmpDefn>(arr);
}

	: i=IDENTIFIER ':' t=type  { l = loc($i, $t.l); arr.add(new AstRecType.AstCmpDefn(l, $i.text, $t.ast));}	 ( o=other_components { if($o.ctx != null) arr.add($o.ast);})* {	
		 
	};

other_components returns [AstRecType.AstCmpDefn ast]
@init{
	Location l;
}
	: ll=',' i=IDENTIFIER ':' t=type {l = loc($ll, $t.l); $ast = new AstRecType.AstCmpDefn(l, $i.text, $t.ast);};


expression returns[AstExpr ast, Location l]
		: n=NONE {$ast = new AstAtomExpr(loc($n), AstAtomExpr.Type.VOID, $n.text); $l = loc($n);} 
		| t=TRUE {$ast = new AstAtomExpr(loc($t), AstAtomExpr.Type.BOOL, $t.text); $l = loc($t);}
		| f=FALSE {$ast = new AstAtomExpr(loc($f), AstAtomExpr.Type.BOOL, $f.text); $l = loc($f);}
		| i=INT_CONST  {$ast = new AstAtomExpr(loc($i), AstAtomExpr.Type.INT, $i.text); $l = loc($i);}
		| c=CHAR_LIT {$ast = new AstAtomExpr(loc($c), AstAtomExpr.Type.CHAR, $c.text); $l = loc($c);}
		| s=STRING_LIT {$ast = new AstAtomExpr(loc($s), AstAtomExpr.Type.STR, $s.text); $l = loc($s);} 
		| n=NIL {$ast = new AstAtomExpr(loc($n), AstAtomExpr.Type.PTR, $n.text); $l = loc($n);}
		| e=expression '[' eb=expression b=']' {$l = loc($e.l, $b); $ast = new AstArrExpr($l, $e.ast, $eb.ast);}
		| e=expression b='^' { $l = loc($e.l, $b); $ast = new AstSfxExpr( $l, AstSfxExpr.Oper.PTR, $e.ast);} 
		| e=expression '.' i=IDENTIFIER {$l=loc($e.l, $i); $ast = new AstCmpExpr($l, $e.ast, $i.text); }
		| ae='not' e=expression { $l = loc($ae, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.NOT, $e.ast);}
		| aa='+' e=expression { $l = loc($aa, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.ADD, $e.ast);}
		| ab='-' e=expression { $l = loc($ab, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.SUB, $e.ast);}
		| ac='^' e=expression { $l = loc($ac, $e.l); $ast = new AstPfxExpr($l, AstPfxExpr.Oper.PTR, $e.ast);}
		| ad='<' tb=type '>' e=expression { $l = loc($ad, $e.l); $ast = new AstCastExpr($l, $tb.ast, $e.ast); }
		| as=expression op=( '*' | '/' | '%' ) ba=expression { $l =loc($as.l, $ba.l); $ast =new AstBinExpr($l, getBinOp($op.text), $as.ast, $ba.ast);} 
		| as=expression op=( '+' | '-' ) ba=expression { $l =loc($as.l, $ba.l); $ast = new AstBinExpr($l, getBinOp($op.text), $as.ast, $ba.ast);} 
		| as=expression op=( '==' | '!=' | '<' | '>' | '<=' | '>=' ) ba=expression { $l =loc($as.l, $ba.l); $ast =new AstBinExpr($l, getBinOp($op.text), $as.ast, $ba.ast);}
		| as=expression 'and' ba=expression { $l = loc($as.l, $ba.l); $ast =new AstBinExpr($l, AstBinExpr.Oper.AND, $as.ast, $ba.ast);}
		| as=expression 'or' ba=expression { $l =loc($as.l, $ba.l); $ast = new AstBinExpr($l, AstBinExpr.Oper.OR, $as.ast, $ba.ast);}
		| s='sizeof' '(' ta=type r=')' {$l = loc($s, $r); $ast = new AstSizeofExpr($l, $ta.ast);}
		| ls='(' e=expression r=')' {$l = loc($ls, $r); $ast = $e.ast;}  
		| i=IDENTIFIER ( ll='(' ( fnc=fnc_call_expr )? rr=')' )?
		{
			$l = loc($i);
			if($ctx.ll == null) $ast = new AstNameExpr(loc($i), $i.text);
			else{
				AstNodes<AstExpr> tmp = null;
				if($fnc.ctx != null) tmp = $fnc.ast;
				$l = loc($i, $rr);
			 	$ast = new AstCallExpr($l, $i.text, tmp);
			}
		} 
		;
 
 fnc_call_expr returns [AstNodes<AstExpr> ast]
 @init{
		ArrayList<AstExpr> arr = new ArrayList<AstExpr>();
 }
 @after{
		$ast = new AstNodes(arr);
 }
 	: ea=expression {arr.add($ea.ast);}  ( ',' eb=expression {if($eb.ctx != null) arr.add($eb.ast);} )*
	
	;





