package lang24.phase.asmgen;

import java.util.*;

import lang24.data.imc.code.stmt.*;
import lang24.data.imc.code.expr.*;
import lang24.data.lin.*;
import lang24.data.mem.MemLabel;
import lang24.data.mem.MemTemp;
import lang24.data.asm.*;

public class MaximalMunch{
	private static Vector<AsmInstr> instr = new Vector<AsmInstr>();


	//statements
	public static Vector<AsmInstr> munchStmt(ImcStmt stmt){
		instr = new Vector<AsmInstr>();

		if( stmt instanceof ImcMOVE){
			munchMOVE( (ImcMOVE) stmt );
		}
		else if( stmt instanceof ImcJUMP){
			munchJUMP( (ImcJUMP) stmt );
		}
		else if( stmt instanceof ImcCJUMP){
			munchCJUMP( (ImcCJUMP) stmt );
		}
		else if( stmt instanceof ImcESTMT){}
		else if( stmt instanceof ImcLABEL){
			munchLabel( (ImcLABEL) stmt );
		}
		
		return instr;
	}

	public static void munchMOVE(ImcMOVE mov){
		if( mov.dst instanceof ImcTEMP) munchMOVE( (ImcTEMP) mov.dst, mov.src);


		return;
	}

	public static void munchMOVE(ImcTEMP dstTemp, ImcExpr src){
		if( src instanceof ImcTEMP) munchMOVE(dstTemp, (ImcTEMP) src);
		if( src instanceof ImcCONST) munchMOVE(dstTemp, (ImcCONST) src);
		if (src instanceof ImcMEM) munchMOVE(dstTemp, (ImcMEM) src);

		return;
	}

	public static void munchMOVE(ImcTEMP dst, ImcTEMP src){
		String mnemonic = "ADD d0,s0,0";

		Vector<MemTemp> uses = new Vector<MemTemp>();
		uses.add(src.temp);
		Vector<MemTemp> defs = new Vector<MemTemp>();
		defs.add(dst.temp);

		AsmMOVE res = new AsmMOVE(mnemonic, uses, defs);
		instr.add(res);
		return;
	}

	//TODO: ADD arbitrary size
	public static void munchMOVE(ImcTEMP dst, ImcCONST src){
		String mnemonic = "SETL d0," + src.value;

		Vector<MemTemp> defs = new Vector<MemTemp>();
		defs.add(dst.temp);


		if(src.value < 0){
		}
			
		AsmOPER res = new AsmOPER(mnemonic, null, defs, null);
		instr.add(res);
		return;
	}

	public static void munchJUMP(ImcJUMP jmp){
		String mnemonic = "JMP " + jmp.label.name;

		Vector<MemLabel> jmps = new Vector<MemLabel>();
		jmps.add(jmp.label);

		AsmOPER oper = new AsmOPER(mnemonic, null, null, jmps);
		instr.add(oper);
		return;
	}

	public static void munchCJUMP(ImcCJUMP cjmp){
		//String mnemonic = "JMP " + cjmp.label.name;

		Vector<MemLabel> jmps = new Vector<MemLabel>();
		//jmps.add(cjmp.label);


		//AsmOPER oper = new AsmOPER(mnemonic, null, null, jmps);
		//instr.add(oper);
		return;
	}

	public static void munchLabel(ImcLABEL lab){
		AsmLABEL label = new AsmLABEL(lab.label);
		instr.add(label);
		return;
	}



	//Expressions
	public static Vector<MemTemp> munchExpression(ImcExpr expr){
		Vector<MemTemp> res = new Vector<MemTemp>();	

		if( expr instanceof ImcMEM) munchMem( (ImcMEM) expr );
		return res;
	}
	
	private static Vector<MemTemp> munchMem(ImcMEM mem){
		return null;
	}

}
