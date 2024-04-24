package lang24.phase.imclin;

import java.util.*;

import lang24.phase.memory.*;
import lang24.data.ast.tree.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.code.stmt.*;
import lang24.data.imc.visitor.*;


public class StmtCanonizer implements ImcVisitor<Vector<ImcStmt>, Object>{
	//Statements
	
	//TODO
	@Override
	public Vector<ImcStmt> visit(ImcCJUMP cjmp, Object arg){
		Vector<ImcStmt> res = new Vector<ImcStmt>();
		ImcExpr condExpr = cjmp.cond.accept(new ExprCanonizer(), res);

		res.add(new ImcCJUMP(condExpr, cjmp.posLabel, cjmp.negLabel));
		return res;
	}

	@Override
	public Vector<ImcStmt> visit(ImcESTMT estm, Object arg){	
		Vector<ImcStmt> res = new Vector<ImcStmt>();
		ImcExpr exprRes = estm.expr.accept(new ExprCanonizer(), res);
		res.add( new ImcESTMT(exprRes));
		return res;
	}

	@Override
	public Vector<ImcStmt> visit(ImcJUMP jmp, Object arg){
		Vector<ImcStmt> res = new Vector<ImcStmt>();
		res.add(new ImcJUMP(jmp.label));
		return res;
	}
	
	@Override
	public Vector<ImcStmt> visit(ImcLABEL lab, Object arg){
		Vector<ImcStmt> res = new Vector<ImcStmt>();
		res.add(new ImcLABEL(lab.label));
		return res;
	}

	//TODO
	@Override
	public Vector<ImcStmt> visit(ImcMOVE mov, Object arg){
		Vector<ImcStmt> res = new Vector<ImcStmt>();
		ImcExpr dest = mov.dst.accept(new ExprCanonizer(), res);
		ImcExpr source = mov.src.accept(new ExprCanonizer(), res);
		res.add( new ImcMOVE(dest, source));
		return res;
	}


	@Override
	public Vector<ImcStmt> visit(ImcSTMTS stmts, Object arg){
		Vector<ImcStmt> res = new Vector<ImcStmt>();

		for(ImcStmt statement: stmts.stmts){
			res.addAll(statement.accept(this, arg));
		}

		return res;
	}
	
}
