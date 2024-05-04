package lang24.phase.asmgen;

import java.util.*;

import lang24.data.imc.code.stmt.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.visitor.*;
import lang24.data.lin.*;
import lang24.data.mem.MemLabel;
import lang24.data.mem.MemTemp;
import lang24.data.asm.*;
import lang24.common.report.*;


public class StatementGen implements ImcVisitor<Vector<AsmInstr>, Object>{
	//Statements
	
	@Override
	public Vector<AsmInstr> visit(ImcJUMP jmp, Object arg){
		Vector<AsmInstr> res = new Vector<AsmInstr>();

		String mnemonic = "JMP " + jmp.label.name;

		Vector<MemLabel> jmps = new Vector<MemLabel>();
		jmps.add(jmp.label);

		AsmOPER oper = new AsmOPER(mnemonic, null, null, jmps);
		res.add(oper);

		return res;
	}
	
	@Override
	public Vector<AsmInstr> visit(ImcCJUMP cjmp, Object arg){
		Vector<AsmInstr> res = new Vector<AsmInstr>();

		String mnemonic = "BNZ  `s0," + cjmp.posLabel.name;

		Vector<MemTemp> uses = new Vector<MemTemp>();
		uses.add( cjmp.cond.accept( new ExpressionGen(), res ));

		Vector<MemLabel> jmps = new Vector<MemLabel>();
		jmps.add(cjmp.posLabel);
		jmps.add(cjmp.negLabel);

		AsmOPER oper = new AsmOPER(mnemonic, uses, null, jmps);
		res.add(oper);
		return res;
	}

	@Override
	public Vector<AsmInstr> visit(ImcESTMT estm, Object arg){	
		Vector<AsmInstr> res = new Vector<AsmInstr>();

		estm.expr.accept( new ExpressionGen(), res);

		return res;
	}

	@Override
	public Vector<AsmInstr> visit(ImcLABEL lab, Object arg){
		Vector<AsmInstr> res = new Vector<AsmInstr>();
		
		AsmLABEL label = new AsmLABEL(lab.label);
		res.add(label);

		return res;
	}

	@Override
	public Vector<AsmInstr> visit(ImcMOVE mov, Object arg){
		Vector<AsmInstr> res = new Vector<AsmInstr>();

		String mnemonic = null;
		if( mov.dst instanceof ImcMEM ){
			mnemonic = "STO  `s0,`s1,0";

			Vector<MemTemp> uses = new Vector<MemTemp>();
			uses.add( mov.src.accept( new ExpressionGen(), res));
			uses.add( mov.dst.accept( new ExpressionGen(), res) );
			
			AsmOPER oper = new AsmOPER(mnemonic, uses, null, null);
			res.add(oper);

			return res;
		}

		if( mov.dst instanceof ImcTEMP ){
			mnemonic = "ADD `d0,`s0,0";

			Vector<MemTemp> uses = new Vector<MemTemp>();
			uses.add( mov.src.accept( new ExpressionGen(), res));
			
			Vector<MemTemp> defs = new Vector<MemTemp>();
			defs.add( mov.dst.accept( new ExpressionGen(), res) );
			
			AsmMOVE oper = new AsmMOVE(mnemonic, uses, defs);
			res.add(oper);
			return res;
		}

		throw new Report.InternalError();
	}


	@Override
	public Vector<AsmInstr> visit(ImcSTMTS stmts, Object arg){
		throw new Report.InternalError();
	}
	
}
