package lang24.phase.imcgen;

import lang24.phase.seman.*;
import lang24.phase.memory.*;
import lang24.data.mem.*;

import lang24.common.report.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.type.*;
import lang24.data.ast.tree.stmt.*;
import lang24.data.ast.visitor.*;
import lang24.data.imc.code.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.code.stmt.*;

import java.util.Stack;
import java.util.Vector;

public class ImcGenerator implements AstFullVisitor<ImcInstr, Integer> {
	
	private Stack<MemLabel> lastFncLabel = new Stack<MemLabel>();
	private Stack<MemFrame> lastFncFrame = new Stack<MemFrame>();

	public ImcGenerator() {
	}

	@Override
	public ImcInstr visit(AstFunDefn funDefn, Integer arg){
		MemFrame frame = Memory.frames.get(funDefn);
		lastFncFrame.push(frame);

		MemLabel entry = new MemLabel();
		MemLabel exit = new MemLabel();
		lastFncLabel.push(exit);

		ImcGen.entryLabel.put(funDefn, entry);
		ImcGen.exitLabel.put(funDefn, exit);
	
		if(funDefn.stmt != null)
			funDefn.stmt.accept(this, arg);
		
		if(funDefn.defns != null)
			funDefn.defns.accept(this, arg);

		lastFncFrame.pop();
		lastFncLabel.pop();

		return null;
	}
	//Addresses
	
	@Override
	public ImcInstr visit(AstNameExpr expr, Integer arg){
		AstVarDefn def = (AstVarDefn) SemAn.definedAt.get(expr);			
		MemAccess mem = Memory.varAccesses.get(def);
	
		if(mem instanceof MemAbsAccess){
			MemAbsAccess actMem = (MemAbsAccess) mem;	
			ImcNAME name = new ImcNAME(actMem.label);
			ImcMEM newMem = new ImcMEM(name);
			ImcGen.exprImc.put(expr, newMem);
			return newMem;

		}
		
		if(mem instanceof MemRelAccess){
			MemRelAccess relMem = (MemRelAccess) mem;
			MemFrame lastFnc = lastFncFrame.peek();
			long staticDistance = lastFnc.depth - relMem.depth + 1 ;

			ImcTEMP tmp = new ImcTEMP(lastFnc.FP);
			
			//System.out.println(staticDistance + " " + expr.name);

			ImcMEM mems = null; 
			for(long i = 0; i < staticDistance ; i++){
				if( i == 0) 
					mems = new ImcMEM(tmp);
				else 
					mems = new ImcMEM(mems);
			}
			
			
			ImcCONST off = new ImcCONST(relMem.offset);
			ImcBINOP bin = null;
			if(mems != null)
				bin = new ImcBINOP(ImcBINOP.Oper.ADD, mems, off);
			else
				bin = new ImcBINOP(ImcBINOP.Oper.ADD, tmp, off);

			

			ImcMEM res = new ImcMEM(bin);
			ImcGen.exprImc.put(expr, res);
			return res;
		}
		
		throw new Report.InternalError();
	}

	//TODO calclate sizeof when mulitplying
	@Override
	public ImcInstr visit(AstArrExpr arr, Integer arg){
		ImcMEM mem = (ImcMEM) arr.arr.accept(this, arg);
		ImcExpr id = (ImcExpr) arr.idx.accept(this, arg);

		ImcExpr addr = mem.addr;
		ImcBINOP mul = new ImcBINOP(ImcBINOP.Oper.MUL, id, new ImcCONST(0));

		ImcBINOP bin = new ImcBINOP(ImcBINOP.Oper.ADD, addr, mul);
		ImcGen.exprImc.put(arr, bin);

		return bin;
	}

	@Override
	public ImcInstr visit(AstCmpExpr cmp, Integer arg){
		ImcInstr res = cmp.expr.accept(this, arg);
		

		return null;
	}
	//Expressions
	
	@Override
	public ImcInstr visit(AstAtomExpr expr, Integer arg) {
		ImcCONST imc = null;
		switch(expr.type){
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

	//Statements
	
	@Override
	public ImcInstr visit(AstAssignStmt stm, Integer arg){
		ImcExpr dst = (ImcExpr) stm.dst.accept(this, arg);
		ImcExpr src = (ImcExpr) stm.src.accept(this, arg);

		ImcMOVE mov = new ImcMOVE(dst, src);
		ImcGen.stmtImc.put(stm, mov);
		return mov;
	}
	
	

	@Override
	public ImcInstr visit(AstIfStmt stm, Integer arg){
		Vector<ImcStmt> vec = new Vector<ImcStmt>();
		
		MemLabel first = new MemLabel();
		MemLabel second = new MemLabel();
		MemLabel end = new MemLabel();

		ImcExpr cond = (ImcExpr) stm.cond.accept(this, arg);
		ImcCJUMP cjmp = new ImcCJUMP(cond, first, second);

		vec.add(cjmp);
		vec.add(new ImcLABEL(first));
		
		ImcStmt statement = (ImcStmt) stm.thenStmt.accept(this, arg);
		vec.add(statement);
		
		vec.add(new ImcJUMP(end));
		
		if(stm.elseStmt != null){
			ImcStmt elseStatement = (ImcStmt) stm.elseStmt.accept(this, arg);
			vec.add(elseStatement);
		}

		vec.add(new ImcLABEL(end));

		ImcSTMTS res = new ImcSTMTS(vec);
		ImcGen.stmtImc.put(stm, res);
		return res;
	}

	@Override
	public ImcInstr visit(AstWhileStmt stm, Integer arg){
		Vector<ImcStmt> vec = new Vector<ImcStmt>();
		
		MemLabel start = new MemLabel();
		MemLabel mid = new MemLabel();
		MemLabel end = new MemLabel();

		vec.add(new ImcLABEL(start));
		
		ImcExpr cond = (ImcExpr) stm.cond.accept(this, arg);
		ImcCJUMP cjmp = new ImcCJUMP(cond, mid, end);

		vec.add(cjmp);
		vec.add(new ImcLABEL(mid));
		
		ImcStmt statement = (ImcStmt) stm.stmt.accept(this, arg);
		vec.add(statement);

		ImcJUMP jmp = new ImcJUMP(start);
		vec.add(jmp);

		vec.add(new ImcLABEL(end));

		ImcSTMTS res = new ImcSTMTS(vec);
		ImcGen.stmtImc.put(stm, res);
		return res;
	}

	@Override
	public ImcInstr visit(AstReturnStmt ret, Integer arg){
		Vector<ImcStmt> vec = new Vector<ImcStmt>();
		

		MemFrame last = lastFncFrame.peek();
		ImcTEMP retVal = new ImcTEMP(last.RV);
		
		ImcExpr retExpr = (ImcExpr) ret.expr.accept(this, arg);

		ImcMOVE mov = new ImcMOVE(retVal, retExpr);
		vec.add(mov);

		ImcJUMP end = new ImcJUMP(lastFncLabel.peek());
		vec.add(end);

		ImcSTMTS res = new ImcSTMTS(vec);
		ImcGen.stmtImc.put(ret, res);
		return res;
	}


}
