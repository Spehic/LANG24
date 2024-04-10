package lang24.phase.imcgen;

import lang24.common.report.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.type.*;
import lang24.data.ast.tree.stmt.*;
import lang24.data.ast.visitor.*;
import lang24.data.imc.code.*;
import lang24.data.imc.code.expr.ImcCONST;



public class ImcGenerator implements AstFullVisitor<ImcInstr, Integer> {
	
	public ImcGenerator() {
	}

	//Expressions
	@
	Override
	public ImcInstr visit(AstAtomExpr expr, Integer arg) {
		ImcCONST imc = null;
		switch(expr.type){
			//TODO: must be undefined dk how yet
			case AstAtomExpr.Type.VOID -> {
				imc = new ImcCONST(0);
			}			
			case AstAtomExpr.Type.BOOL -> {
				if( expr.value.equals("true") )
					imc = new ImcCONST(1);
				else
					imc = new ImcCONST(0);		
			}
			case AstAtomExpr.Type.CHAR -> {
				imc = new ImcCONST(Character.valueOf(expr.value.charAt(1)));
			}
			case AstAtomExpr.Type.INT -> {
				imc = new ImcCONST(Long.parseLong(expr.value));
			}
			case AstAtomExpr.Type.PTR -> {
				imc = new ImcCONST(0);
			}
			//TODO: not correct yet
			case AstAtomExpr.Type.STR -> {
				imc = new ImcCONST(0);
			}
		}

		ImcGen.exprImc.put(expr, imc);
		return imc;
	}

	@Override
	public ImcCONST visit(AstPfxExpr pfxExpr, Integer arg){
		switch(pfxExpr.oper){
			case AstPfxExpr.Oper.NOT:
			case AstPfxExpr.Oper.ADD:
			case AstPfxExpr.Oper.SUB:	
		}
	}

}
