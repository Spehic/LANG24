package lang24.phase.asmgen;

import java.util.*;

import lang24.data.imc.code.stmt.*;
import lang24.data.lin.*;
import lang24.data.asm.*;
import lang24.phase.*;
import lang24.phase.imclin.*;
import lang24.phase.asmgen.*;

/**
 * Machine code generator.
 */
public class AsmGen extends Phase {

	public static Vector<Code> codes = new Vector<Code>();

	public AsmGen() {
		super("asmgen");
	}

	public void genAsmCodes() {
		for (LinCodeChunk codeChunk : ImcLin.codeChunks()) {
			Vector<AsmInstr> instr = new Vector<AsmInstr>();

			for(ImcStmt statement : codeChunk.stmts()){
				instr.addAll(statement.accept(new StatementGen(), null));
			}

			Code code = new Code(codeChunk.frame, codeChunk.entryLabel, codeChunk.exitLabel, instr);
			codes.add(code);
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
				logger.endElement();
			}
			logger.endElement();
			logger.endElement();
		}
	}

}
