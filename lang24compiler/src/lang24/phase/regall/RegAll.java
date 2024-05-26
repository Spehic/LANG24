package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;
import lang24.data.asm.*;
import lang24.phase.*;
import lang24.phase.asmgen.*;
import lang24.phase.livean.*;

/**
 * Register allocation.
 */
public class RegAll extends Phase {

	/** Mapping of temporary variables to registers. */
	public static final HashMap<MemTemp, Integer> tempToReg = new HashMap<MemTemp, Integer>();

	public RegAll() {
		super("regall");
	}

	public static void allocateFnc (Code code) {
		// build
		Graph graph = new Graph();
		graph.build( code.instrs );

		// build graph used for select later
		Graph selectGraph = new Graph();
		selectGraph.build( code.instrs );

		graph.fp = code.frame.FP;
		//System.out.println("---build---");
		//System.out.println( graph );
		//System.out.println("---build---");
		//System.out.println( selectGraph );

		//try { Thread.sleep( 2000 ); }
		//catch (Exception e) {}

		//simplfy
		graph.simplfy( );
		//System.out.println("---simplfy---");
		//System.out.println( graph );
		//graph.printStack();

		//select
		// used for altering code in case of spill
		selectGraph.code = code;
		//fp uses special register ($253 )
		selectGraph.fp = code.frame.FP;
		selectGraph.select( graph.stk );
	}

	public void allocate() {
		for ( Code code : AsmGen.codes ) {
			allocateFnc( code );	
		}
	}

	public void log() {
		if (logger == null)
			return;
		for (Code code : AsmGen.codes) {
			logger.begElement("code");
			logger.addAttribute("body", code.entryLabel.name);
			logger.addAttribute("epilogue", code.exitLabel.name);
			logger.addAttribute("tempsize", Long.toString(code.tempSize));
			code.frame.log(logger);
			logger.begElement("instructions");
			for (AsmInstr instr : code.instrs) {
				logger.begElement("instruction");
				logger.addAttribute("code", instr.toString(tempToReg));
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
