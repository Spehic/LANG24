package lang24.phase.imclin;

import java.util.*;


import lang24.phase.memory.*;
import lang24.phase.imcgen.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.visitor.*;
import lang24.data.mem.*;
import lang24.data.lin.*;
import lang24.data.imc.code.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.code.stmt.*;

import lang24.data.mem.*;


public class ChunkGenerator implements AstFullVisitor<Object, Object>{

	@Override
	public Object visit(AstFunDefn funDefn, Object arg){
		MemFrame frame = Memory.frames.get(funDefn);

		MemLabel entry = ImcGen.entryLabel.get(funDefn);
		MemLabel exit = ImcGen.exitLabel.get(funDefn);
		
		Vector<ImcStmt> statements = new Vector<ImcStmt>();
		
		statements.add(new ImcLABEL(entry));

		if(funDefn.stmt != null){
			ImcStmt imcRes = ImcGen.stmtImc.get(funDefn.stmt);
			statements.addAll(imcRes.accept(new StmtCanonizer(), null));
		}
		

		statements.add(new ImcMOVE(new ImcTEMP(frame.RV), new ImcCONST(0)));
		statements.add(new ImcJUMP(exit));

		LinCodeChunk res = new LinCodeChunk(frame, statements, entry, exit);
		ImcLin.addCodeChunk(res);
		
		if(funDefn.defns != null)
			funDefn.defns.accept(this, arg);

		if(funDefn.stmt!= null)
			funDefn.stmt.accept(this, arg);

		return null;
	}

	@Override
	public Object visit(AstVarDefn varDefn, Object arg){
		MemAccess acc = Memory.varAccesses.get(varDefn);

		if(acc instanceof MemAbsAccess){
			MemAbsAccess abs = (MemAbsAccess) acc;
			ImcLin.addDataChunk(new LinDataChunk(abs));
		}

		return null;
	}

	@Override
	public Object visit(AstAtomExpr atom, Object arg){
		if(atom.type == AstAtomExpr.Type.STR){
			MemAbsAccess abs = Memory.strings.get(atom);	
			ImcLin.addDataChunk(new LinDataChunk(abs));
		}
		return null;
	}


}
