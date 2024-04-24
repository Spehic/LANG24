package lang24.phase.imclin;

import java.util.*;

import lang24.common.report.*;
import lang24.phase.memory.*;
import lang24.data.ast.tree.*;
import lang24.data.mem.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.code.stmt.*;
import lang24.data.imc.visitor.*;


public class StmtCanonizer implements ImcVisitor<Vector<ImcStmt>, Object>{
	//Statements
	
	@Override
	public Vector<ImcStmt> visit(ImcCJUMP cjmp, Object arg){
		Vector<ImcStmt> res = new Vector<ImcStmt>();
		ImcExpr condExpr = cjmp.cond.accept(new ExprCanonizer(), res);

		MemLabel neg = new MemLabel();
		res.add(new ImcCJUMP(condExpr, cjmp.posLabel, neg));

		res.add(new ImcLABEL(neg));
		res.add(new ImcJUMP(cjmp.negLabel));
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

		if( mov.dst instanceof ImcMEM){
			ImcMEM tmp = (ImcMEM) mov.dst;
			
			ImcExpr dstExpr = ((ImcMEM)tmp.accept(new ExprCanonizer(), res)).addr;
			MemTemp dstTemp = new MemTemp();
			res.add(new ImcMOVE(new ImcTEMP(dstTemp), dstExpr));

			ImcExpr srcExpr = mov.src.accept(new ExprCanonizer(), res);
			MemTemp srcTemp = new MemTemp();
			res.add(new ImcMOVE(new ImcTEMP(srcTemp), srcExpr));

			res.add(new ImcMOVE( new ImcMEM(new ImcTEMP(dstTemp)), new ImcTEMP(srcTemp)));
			return res;
		}

		else if( mov.dst instanceof ImcTEMP){
			ImcExpr srcExpr = mov.src.accept(new ExprCanonizer(), res);
			MemTemp srcTemp = new MemTemp();

			MemTemp dstTemp = ((ImcTEMP) ( mov.dst)).temp;
			
			res.add(new ImcMOVE(new ImcTEMP(srcTemp), srcExpr));
			res.add(new ImcMOVE( new ImcTEMP(dstTemp), new ImcTEMP(srcTemp)));
			return res;
		}
		
		System.out.println(mov.dst);
		throw new Report.InternalError();
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
