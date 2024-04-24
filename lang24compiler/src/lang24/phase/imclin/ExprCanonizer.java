package lang24.phase.imclin;

import java.util.*;

import lang24.phase.memory.*;
import lang24.data.ast.tree.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.code.stmt.*;
import lang24.data.imc.visitor.*;
import lang24.data.mem.*;


public class ExprCanonizer implements ImcVisitor<ImcExpr, Vector <ImcStmt>>{
	
	@Override
	public  ImcExpr visit(ImcBINOP bin, Vector<ImcStmt> stmts){
		ImcExpr first = bin.fstExpr.accept(this, stmts);
		ImcExpr second = bin.sndExpr.accept(this, stmts);
		ImcBINOP res = new ImcBINOP(bin.oper, first, second); 
		return res;
	}

	@Override
	public ImcExpr visit(ImcCALL call, Vector<ImcStmt> stmts){
		Vector<ImcExpr> exprs = new Vector<ImcExpr>();
	
		ImcExpr argRes = null; 
		for(ImcExpr argx : call.args){
			MemTemp temp = new MemTemp();
			argRes = argx.accept(this, stmts);
			stmts.add(new ImcMOVE(new ImcTEMP(temp), argRes));
			exprs.add(new ImcTEMP(temp));
		}

		ImcCALL res = new ImcCALL(call.label, call.offs, exprs);
		return res;
	}

	@Override
	public ImcExpr visit(ImcCONST cons, Vector<ImcStmt> stmts){
		return new ImcCONST(cons.value);
	}
	
	@Override
	public ImcExpr visit(ImcMEM mem, Vector<ImcStmt> stmts){
		ImcExpr subExpr = mem.addr.accept(this, stmts);
		return new ImcMEM(subExpr);
	}	

	@Override
	public ImcExpr visit(ImcNAME name, Vector<ImcStmt> stmts){
		return new ImcNAME(name.label);
	}

	@Override
	public ImcExpr visit(ImcTEMP tmp, Vector<ImcStmt> stmts){
		return new ImcTEMP(tmp.temp);
	}

	@Override
	public ImcExpr visit(ImcUNOP unop, Vector<ImcStmt> stmts){
		ImcExpr subExpr = unop.accept(this, stmts);
		return new ImcUNOP(unop.oper, subExpr);
	}
	
}
