package lang24.phase.livean;

import java.util.*;
import lang24.data.mem.*;
import lang24.data.asm.*;
import lang24.phase.*;
import lang24.phase.asmgen.*;

/**
 * Liveness analysis.
 */
public class LiveAn extends Phase {

	public LiveAn() {
		super("livean");
	}

	// used to find AsmLABEL from MemLabel( instruction code generated jumps to memLabel )
	private static HashMap<String, AsmLABEL> labels = new HashMap<String, AsmLABEL>();

	public HashSet<AsmInstr> succ( Vector<AsmInstr> instrs, int index ){
		HashSet<AsmInstr> successors = new HashSet<AsmInstr>();

		AsmInstr curr = instrs.get(index);	
		
		if( curr.jumps().size() == 0 ){
			if ( index + 1 >= instrs.size()) return null;
			successors.add( instrs.get(index + 1));
		} else {
			Vector<MemLabel> labs = curr.jumps();
			for( MemLabel lab : labs){
				AsmLABEL tmp = labels.get(lab.name);
				if( tmp != null)
					successors.add( tmp );
			}
			
		}

		// for( AsmInstr i : successors){ 
		// 	System.out.println("scuc-----");
		// 	System.out.println(i);
		// 	System.out.println( i.jumps());
		// 	System.out.println("scuc-----");

		// }
		return successors;
	}

	// in(n)
	public HashSet<MemTemp> in( AsmInstr instr ){
		// use(n)
		HashSet<MemTemp> in = new HashSet<MemTemp>();
		in.addAll(instr.uses());
		
		// out(n) \ def(n)
		HashSet<MemTemp> temp = new HashSet<MemTemp>();
		temp.addAll(instr.out());
		temp.removeAll(instr.defs());
		
		// use(n) U [ out(n) \ def()]
		in.addAll(temp);
		return in;
	}

	// out(n)
	public HashSet<MemTemp> out( HashSet<AsmInstr> successors ){
		HashSet<MemTemp> out = new HashSet<MemTemp>();

		// foreach successor: out(n) = U in(n)
		for(AsmInstr instr : successors){
			out.addAll( instr.in() );
		}

		return out;
	}

	public void fncResolve(Code code){
		for(AsmInstr instr : code.instrs){
			if( instr instanceof AsmOPER ){
				AsmOPER oper = (AsmOPER) instr;
				oper.removeAllFromIn();
				oper.removeAllFromOut();
			} 
			else if ( instr instanceof AsmLABEL ){
				AsmLABEL label = (AsmLABEL) instr;
				labels.put(label.toString(), label);
			}
		}


		HashSet<MemTemp> currOut = null;
		HashSet<MemTemp> currIn = null;

		while(true){
			Vector<AsmInstr> instructions = code.instrs;
			
			boolean hasChanged = false;
			for(int i = 0; i < instructions.size(); i++){
				AsmInstr oper =  instructions.get(i);	

				currOut = new HashSet<MemTemp>(oper.out());
				currIn = new HashSet<MemTemp>(oper.in());
				
				oper.addInTemps( in( oper ) );

				HashSet<AsmInstr> succs = succ( instructions, i);
				oper.addOutTemp( out(succs) );

				if( !currOut.equals( oper.out()))
					hasChanged = true;
				if( !currIn.equals( oper.in()))
					hasChanged = true;
			}

			// if a loop didnt change anything break out
			if( !hasChanged ) break; 
		}
	}


	public void analysis() {
		for( Code code: AsmGen.codes ){
			fncResolve(code);
		}
	}


	public void log() {
		if (logger == null)
			return;
		for (Code code : AsmGen.codes) {
			logger.begElement("code");
			logger.addAttribute("prologue", code.entryLabel.name);
			logger.addAttribute("body", code.entryLabel.name);
			logger.addAttribute("epilogue", code.exitLabel.name);
			logger.addAttribute("tempsize", Long.toString(code.tempSize));
			code.frame.log(logger);
			logger.begElement("instructions");
			for (AsmInstr instr : code.instrs) {
				logger.begElement("instruction");
				logger.addAttribute("code", instr.toString());
				logger.begElement("temps");
				logger.addAttribute("name", "use");
				for (MemTemp temp : instr.uses()) {
					logger.begElement("temp");
					logger.addAttribute("name", temp.toString());
					logger.endElement();
				}
				logger.endElement();
				logger.begElement("temps");
				logger.addAttribute("name", "def");
				for (MemTemp temp : instr.defs()) {
					logger.begElement("temp");
					logger.addAttribute("name", temp.toString());
					logger.endElement();
				}
				logger.endElement();
				logger.begElement("temps");
				logger.addAttribute("name", "in");
				for (MemTemp temp : instr.in()) {
					logger.begElement("temp");
					logger.addAttribute("name", temp.toString());
					logger.endElement();
				}
				logger.endElement();
				logger.begElement("temps");
				logger.addAttribute("name", "out");
				for (MemTemp temp : instr.out()) {
					logger.begElement("temp");
					logger.addAttribute("name", temp.toString());
					logger.endElement();
				}
				logger.endElement();
				logger.endElement();
			}
			logger.endElement();
			logger.endElement();
		}
	}

}
