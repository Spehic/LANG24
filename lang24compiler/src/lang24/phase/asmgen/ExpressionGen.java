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

        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add(temporary);

        switch (bin.oper) {
            //CONDITIONS 
            case ImcBINOP.Oper.EQU -> {
                instrs.add(new AsmOPER("CMP `d0,`s0,`s1", uses, defs, null)); 
                instrs.add(new AsmOPER("ZSZ `d0,`s0,1", defs, defs, null));
            }
            case ImcBINOP.Oper.NEQ -> {
                instrs.add(new AsmOPER("CMP `d0,`s0,`s1", uses, defs, null)); 
                instrs.add(new AsmOPER("ZSNZ `d0,`s0,1", defs, defs, null));
            }
            case LTH -> { 
                instrs.add(new AsmOPER("CMP `d0,`s0,`s1", uses, defs, null)); 
                instrs.add(new AsmOPER("ZSN `d0,`s0,1", defs, defs, null));
            }
            case LEQ -> { 
                instrs.add(new AsmOPER("CMP `d0,`s0,`s1", uses, defs, null)); 
                instrs.add(new AsmOPER("ZSNP `d0,`s0,1", defs, defs, null));
            }
            case GTH -> { 
                instrs.add(new AsmOPER("CMP `d0,`s0,`s1", uses, defs, null)); 
                instrs.add(new AsmOPER("ZSP `d0,`s0,1", defs, defs, null));
            }
            case GEQ -> { 
                instrs.add(new AsmOPER("CMP `d0,`s0,`s1", uses, defs, null)); 
                instrs.add(new AsmOPER("ZSNN `d0,`s0,1", defs, defs, null));
            }
            
            //ARITHMETHIC
            case ImcBINOP.Oper.ADD -> 
                instrs.add(new AsmOPER("ADD `d0,`s0,`s1", uses, defs, null));
            case ImcBINOP.Oper.SUB -> 
                instrs.add(new AsmOPER("SUB `d0,`s0,`s1", uses, defs, null)); 
            case ImcBINOP.Oper.MUL -> 
                instrs.add(new AsmOPER("MUL `d0,`s0,`s1", uses, defs, null)); 
            case ImcBINOP.Oper.DIV -> 
                instrs.add(new AsmOPER("DIV `d0,`s0,`s1", uses, defs, null)); 
            case ImcBINOP.Oper.MOD -> { 
                    instrs.add(new AsmOPER("DIV `d0,`s0,`s1", uses, defs, null)); 
                    instrs.add(new AsmOPER("GET `d0,rR", null, defs, null));
                }
            
            case ImcBINOP.Oper.OR -> 
                instrs.add(new AsmOPER("OR `d0,`s0,`s1", uses, defs, null)); 
            case ImcBINOP.Oper.AND ->
                instrs.add(new AsmOPER("AND `d0,`s0,`s1", uses, defs, null));               
            default -> throw new Report.InternalError();
        }

        return temporary;
    }

    // TODO: ADD arbitrary distance away from stack pointer
    @Override
    public MemTemp visit(ImcCALL call, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        String fullInstr = null;
        for( int i = 0; i < call.args.size(); i++){
            MemTemp argRes = call.args.get(i).accept(this, instrs);
            
            Vector<MemTemp> uses = new Vector<MemTemp>();
            uses.add(argRes);

            long offset = call.offs.get(i);
            if( offset <= 0xFF)
                fullInstr = "STO $254,`s0,"+ offset;
            else{
                uses.add( new ImcCONST(offset).accept(this, instrs) );
                fullInstr = "STO $254,`s0,`s1";
            }
            AsmOPER oper = new AsmOPER(fullInstr, uses, null, null);
            instrs.add(oper);
        }

        // call function
        // $8 here is a magic number
        fullInstr = "PUSHJ $8," + call.label.name;

        Vector<MemLabel> jmps = new Vector<MemLabel >();
        jmps.add(call.label);

        AsmOPER oper = new AsmOPER(fullInstr, null, null, jmps);
        instrs.add(oper);

        // save return value
        fullInstr = "LDO `d0,$254,0";
        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add(temporary);

        oper = new AsmOPER(fullInstr, null, defs, null);
        instrs.add(oper);

        return temporary;
    }

    @Override
    public MemTemp visit(ImcCONST imconst, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();

        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add(temporary);

        long lo = imconst.value & 0xFFFF;
        long ml = imconst.value >> 16 & 0xFFFF;
        long mh = imconst.value >> 32 & 0xFFFF;
        long hi = imconst.value >> 48 & 0xFFFF;

        String fullInstr = "SETL `d0," + lo;
        AsmOPER oper = new AsmOPER(fullInstr, null, defs, null);
        instrs.add(oper);

        if( ml != 0 ) {
            fullInstr = "INCML `d0," + ml;
            oper = new AsmOPER(fullInstr, null, defs, null);
            instrs.add(oper);
        }

        if( mh != 0 ) {
            fullInstr = "INCMH `d0," + mh;
            oper = new AsmOPER(fullInstr, null, defs, null);
            instrs.add(oper);
        }

        if( mh != 0 ) {
            fullInstr = "INCH `d0," + hi;
            oper = new AsmOPER(fullInstr, null, defs, null);
            instrs.add(oper);
        }

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

    @Override
    public MemTemp visit(ImcNAME name, Vector<AsmInstr> instrs){
        MemTemp temporary = new MemTemp();
        
        Vector<MemTemp> defs = new Vector<MemTemp>();
        defs.add( temporary );

        String fullInstr = " LDA `d0," + name.label.name;
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
            case ImcUNOP.Oper.NOT -> {
                uses.add( new ImcCONST(-1).accept(this, instrs) );
                fullInstr = "XOR `d0,`s0,`s1";
            }
            default -> throw new Report.InternalError();
        }

        AsmOPER oper = new AsmOPER(fullInstr, uses, defs, null);
        instrs.add(oper);

        return temporary;
    }

}
