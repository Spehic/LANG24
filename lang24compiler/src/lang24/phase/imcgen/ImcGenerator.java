package lang24.phase.imcgen;

import lang24.common.report.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.type.*;
import lang24.data.ast.tree.stmt.*;
import lang24.data.ast.visitor.*;
import lang24.data.imc.code.*;
import lang24.data.imc.code.expr.*;



public class ImcGenerator implements AstFullVisitor<ImcInstr, Integer> {
	
	public ImcGenerator() {
	}

	//Expressions
	
	@Override
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
	public ImcInstr visit(AstPfxExpr pfxExpr, Integer arg){
		ImcExpr res = (ImcExpr) pfxExpr.expr.accept(this, arg);
		ImcUNOP imc = null;
		switch(pfxExpr.oper){
			case AstPfxExpr.Oper.NOT:
				imc = new ImcUNOP(ImcUNOP.Oper.NOT, res);
				ImcGen.exprImc.put(pfxExpr, imc);
				return imc;
			case AstPfxExpr.Oper.ADD:
				ImcGen.exprImc.put(pfxExpr, res);
				return res;
			case AstPfxExpr.Oper.SUB:
				imc = new ImcUNOP(ImcUNOP.Oper.NEG, res);
				ImcGen.exprImc.put(pfxExpr, imc);
				return imc;
			case AstPfxExpr.Oper.PTR:
				ImcGen.exprImc.put(pfxExpr, res);
				return res;
		}

		throw new Report.InternalError();
	}

	@Override
	public ImcInstr visit(AstBinExpr binExpr, Integer arg){
		ImcExpr expr1 = (ImcExpr) binExpr.fstExpr.accept(this, arg);
		ImcExpr expr2 = (ImcExpr) binExpr.sndExpr.accept(this, arg);

		if(expr1 == null)
			throw new Report.InternalError();

		if(expr2 == null)
			throw new Report.InternalError();
		
		int enumVal = binExpr.oper.ordinal();
		ImcBINOP.Oper operand = ImcBINOP.Oper.values()[enumVal]; 
		ImcBINOP imc = new ImcBINOP(operand, expr1, expr2);

		ImcGen.exprImc.put(binExpr, imc);
		return imc;
	}

	@Override
	public ImcInstr visit(AstSfxExpr sfxExpr, Integer arg){	
		ImcExpr res = (ImcExpr) sfxExpr.expr.accept(this, arg);

		if(res == null)
			throw new Report.InternalError();

		ImcMEM mem = new ImcMEM(res);
		ImcGen.exprImc.put(sfxExpr, mem);
		return mem;
	}



}
