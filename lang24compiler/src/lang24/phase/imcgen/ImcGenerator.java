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
import lang24.data.type.*;

import java.util.Stack;
import java.util.Vector;

public class ImcGenerator implements AstFullVisitor<ImcInstr, Integer> {
	
	private Stack<MemLabel> lastFncLabel = new Stack<MemLabel>();
	private Stack<MemFrame> lastFncFrame = new Stack<MemFrame>();

	public ImcGenerator() {
	}

	private static int calcSize(SemType typ){
		int total = 0;
		if( typ instanceof SemIntType)
			return 8;
		if( typ instanceof SemPointerType)
			return 8;
		if( typ instanceof SemBoolType)
			return 8;
		if( typ instanceof SemCharType)
			return 8;
		if( typ instanceof SemVoidType)
			return 0;
		if( typ instanceof SemNameType){
			SemNameType name = (SemNameType) typ;
			return calcSize(name.type());
		}
		if( typ instanceof SemArrayType){
			SemArrayType arr = (SemArrayType) typ;
			return (int )arr.size * calcSize(arr.elemType);
		}
		if (typ instanceof SemStructType){
			SemStructType str = (SemStructType) typ;
			for( SemType a : str.cmpTypes )
				total += calcSize(a);
			return total;
		}
		if (typ instanceof SemUnionType){
			SemUnionType uni = (SemUnionType) typ;
			for( SemType a : uni.cmpTypes ){
				int x = calcSize(a);
				if( x > total)
					total = x;
			}
			return total;
		}

		throw new Report.InternalError();
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
		AstDefn def = SemAn.definedAt.get(expr);			
		boolean isRef = false;
		
		MemAccess mem = null;
		if( def instanceof AstVarDefn)
			mem = Memory.varAccesses.get((AstVarDefn)def);
	
		if( def instanceof AstFunDefn.AstValParDefn)
			mem = Memory.parAccesses.get((AstFunDefn.AstValParDefn)def);

		if( def instanceof AstFunDefn.AstRefParDefn){
			mem = Memory.parAccesses.get((AstFunDefn.AstRefParDefn)def);
			isRef = true;
		}

		if(mem == null)
			throw new Report.InternalError();

		if(mem instanceof MemAbsAccess){
			MemAbsAccess actMem = (MemAbsAccess) mem;	
			ImcNAME name = new ImcNAME(actMem.label);
			ImcMEM newMem = new ImcMEM(name);
			ImcGen.exprImc.put(expr, newMem);
			if(isRef)
				newMem = new ImcMEM(newMem);
			return newMem;

		}
		
		if(mem instanceof MemRelAccess){
			MemRelAccess relMem = (MemRelAccess) mem;
			MemFrame lastFnc = lastFncFrame.peek();
			long staticDistance = lastFnc.depth - relMem.depth + 1 ;

			ImcTEMP tmp = new ImcTEMP(lastFnc.FP);

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

			if(isRef)
				res = new ImcMEM(res);

			ImcGen.exprImc.put(expr, res);
			return res;
		}
		
		throw new Report.InternalError();
	}

	@Override
	public ImcInstr visit(AstArrExpr arr, Integer arg){
		ImcMEM mem = (ImcMEM) arr.arr.accept(this, arg);
		ImcExpr id = (ImcExpr) arr.idx.accept(this, arg);

		ImcExpr addr = mem.addr;
		
		SemArrayType typ = (SemArrayType) SemAn.ofType.get(arr.arr);
		int size = calcSize(typ.elemType);
		ImcBINOP mul = new ImcBINOP(ImcBINOP.Oper.MUL, id, new ImcCONST(size));

		ImcBINOP bin = new ImcBINOP(ImcBINOP.Oper.ADD, addr, mul);
		ImcGen.exprImc.put(arr, bin);

		return bin;
	}
	
	@Override
	public ImcInstr visit(AstCmpExpr cmp, Integer arg){
		ImcExpr res = (ImcExpr) cmp.expr.accept(this, arg);
		SemType actual = SemAn.ofType.get(cmp.expr).actualType();
		SymbTable symb = TypeResolver.recMap.get(actual);
		
		AstRecType.AstCmpDefn node = null;
		try{
			node = (AstRecType.AstCmpDefn) symb.fnd(cmp.name);
		}catch(SymbTable.CannotFndNameException e){
			System.out.println("Cannot find component - internal error");
			System.exit(1);
		}

		MemRelAccess mem = (MemRelAccess) Memory.cmpAccesses.get(node);

		ImcCONST off = new ImcCONST(mem.offset);	
		ImcMEM newMem = (ImcMEM) res;
		ImcBINOP bin = new ImcBINOP(ImcBINOP.Oper.ADD, newMem.addr, off);
		ImcMEM finRes = new ImcMEM(bin); 

		ImcGen.exprImc.put(cmp, finRes);

		return finRes;
	}

	//TODO add mem?
	@Override
	public ImcInstr visit(AstCallExpr call, Integer arg){
		AstFunDefn def = (AstFunDefn) SemAn.definedAt.get(call);		
		MemFrame frame = Memory.frames.get(def);

		Vector<Long> offsets = new Vector<Long>();
		Vector<ImcExpr> args = new Vector<ImcExpr>();

		long offset = 0;
		if(call.args != null){
			int index = 0;
			for(AstExpr expr : call.args){
				offsets.add(offset);
				SemType typ = SemAn.ofType.get(expr);
				int size = calcSize(typ);
				offset += size;
				
				ImcExpr argEx = (ImcExpr) expr.accept(this, arg);

				AstFunDefn.AstParDefn parDefn = def.pars.get(index);
				if(parDefn instanceof AstFunDefn.AstRefParDefn){
					ImcMEM tmp = (ImcMEM) argEx;
					argEx = tmp.addr;
				}
				
				args.add(argEx);
				index++;
			}
		}

		ImcCALL res = new ImcCALL(frame.label, offsets, args);
		ImcGen.exprImc.put(call, res);
		return res;
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
			case AstAtomExpr.Type.STR -> {
				MemAbsAccess acc = Memory.strings.get(expr);
				ImcNAME name = new ImcNAME(acc.label);
				ImcGen.exprImc.put(expr, name);
				return name;
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


	@Override
	public ImcInstr visit(AstCastExpr cast, Integer arg){
		SemType typ = SemAn.isType.get(cast.type);
		SemType actual = typ.actualType();
		
		ImcInstr castExpr = cast.expr.accept(this, arg);

		if(actual instanceof SemCharType){
			ImcCONST by = new ImcCONST(256);
			ImcBINOP bin = new ImcBINOP(ImcBINOP.Oper.MOD, (ImcExpr)castExpr, by);
			ImcGen.exprImc.put(cast, bin);
			return bin;
		}
		else{
			ImcGen.exprImc.put(cast, (ImcExpr)castExpr);
			return castExpr;
		}
	}

	@Override
	public ImcInstr visit(AstSizeofExpr sizeExpr, Integer arg){
		long size = 0;
		SemType typ = SemAn.isType.get(sizeExpr.type).actualType();
		size = calcSize(typ);
		
		ImcCONST res = new ImcCONST(size);

		ImcGen.exprImc.put(sizeExpr, res);
		return res;
	}

	//Statements
	
	@Override
	public ImcInstr visit(AstBlockStmt stm, Integer arg){
		Vector<ImcStmt> vec = new Vector<ImcStmt>();
		if(stm.stmts != null){
			for(AstStmt statement : stm.stmts){
				ImcStmt res = (ImcStmt) statement.accept(this, arg);
				vec.add(res);
			}
		}

		ImcSTMTS res = new ImcSTMTS(vec);
		ImcGen.stmtImc.put(stm, res);
		return res;
	}
	
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

	@Override
	public ImcInstr visit(AstExprStmt stm, Integer arg){
		return new ImcESTMT((ImcExpr) stm.expr.accept(this, arg));
	}


}
