
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

public class ExpressionGen implements ImcVisitor<MemTemp, Vector<AsmInstr>> {
    
    @Override
    public MemTemp visit(ImcBINOP bin, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        MemTemp first = bin.fstExpr.accept(this, instrs);
        MemTemp second = bin.sndExpr.accept(this, instrs);

        Vector<MemTemp> uses = new Vector<MemTemp>();
        uses.add( first );
        uses.add( second );


    }

    @Override
	public MemTemp visit(ImcMEM mem, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        Vector<MemTemp> uses = new Vector<MemTemp>();
        uses.add( mem.addr.accept(this, instrs) );
        
        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add( temporary );

        return temporary;
	}

    @Override
    public MemTemp visit(ImcTEMP temp, Vector<AsmInstr> instrs){
        return temp;
    }

}
