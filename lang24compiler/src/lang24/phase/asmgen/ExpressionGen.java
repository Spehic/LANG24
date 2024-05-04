
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
    
    //TODO: fix MOD
    @Override
    public MemTemp visit(ImcBINOP bin, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        MemTemp first = bin.fstExpr.accept(this, instrs);
        MemTemp second = bin.sndExpr.accept(this, instrs);

        Vector<MemTemp> uses = new Vector<MemTemp>();
        uses.add( first );
        uses.add( second );

        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add(temporary);

        String mnemonic = null;

        switch (bin.oper) {
            case ImcBINOP.Oper.OR -> mnemonic = "OR";
            case ImcBINOP.Oper.AND -> mnemonic = "AND";
            case ImcBINOP.Oper.EQU -> mnemonic = "EQU";
            case ImcBINOP.Oper.NEQ -> mnemonic = "NEQ";
            case ImcBINOP.Oper.LTH -> mnemonic = "LTH";
            case ImcBINOP.Oper.GTH -> mnemonic = "GTH";
            case ImcBINOP.Oper.LEQ -> mnemonic = "LEQ";
            case ImcBINOP.Oper.GEQ -> mnemonic = "GEQ";
            case ImcBINOP.Oper.ADD -> mnemonic = "ADD";
            case ImcBINOP.Oper.SUB -> mnemonic = "SUB";
            case ImcBINOP.Oper.MUL -> mnemonic = "MUL";
            case ImcBINOP.Oper.DIV -> mnemonic = "DIV";
            case ImcBINOP.Oper.MOD -> mnemonic = "MOD";                
            default -> throw new Report.InternalError();
        }

        String fullInstr = mnemonic + " " + "`d0,`s0,`s0";

        AsmOPER oper = new AsmOPER(fullInstr, uses, defs, null);
        instrs.add(oper);
        return temporary;
    }

    @Override
    public MemTemp visit(ImcCALL call, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        return temporary;
    }

    @Override
    public MemTemp visit(ImcCONST imconst, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        Vector<MemTemp> defs = new Vector<MemTemp>();
        return temporary;
    }

    @Override
	public MemTemp visit(ImcMEM mem, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        String fullInstr = "LDO `d0,`s0,0";

        Vector<MemTemp> uses = new Vector<MemTemp>();
        uses.add( mem.addr.accept(this, instrs) );
        
        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add( temporary );

        AsmOPER oper = new AsmOPER(fullInstr, uses, defs, null);
        instrs.add(oper);
        return temporary;
	}

    //TODO
    @Override
    public MemTemp visit(ImcNAME name, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();
        
        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add( temporary );

        String fullInstr = " LDA d0," + name.label;
        AsmOPER oper = new AsmOPER(fullInstr, null, defs, null);
        instrs.add(oper);

        return temporary;
    }

    @Override
    public MemTemp visit(ImcTEMP temp, Vector<AsmInstr> instrs){
        return temp.temp;
    }

    @Override
    public MemTemp visit(ImcUNOP unop, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        Vector<MemTemp> uses = new Vector<MemTemp>();
        uses.add( unop.subExpr.accept(this, instrs) );
        
        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add( temporary );

        String fullInstr = null;
        
        switch(unop.oper) {
            case ImcUNOP.Oper.NEG -> fullInstr = "NEG `d0,`s0";
            case ImcUNOP.Oper.NOT -> fullInstr = "XOR `d0,`s0,1";
            default -> throw new Report.InternalError();
        }

        AsmOPER oper = new AsmOPER(fullInstr, uses, defs, null);
        instrs.add(oper);

        return temporary;
    }

}
