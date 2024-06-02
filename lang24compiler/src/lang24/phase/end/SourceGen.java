package lang24.phase.end;

import java.io.*;
import java.util.*;

import lang24.data.asm.*;
import lang24.phase.*;
import lang24.phase.asmgen.*;
import lang24.phase.imclin.*;
import lang24.phase.regall.*;
import lang24.data.lin.*;
import lang24.data.mem.*;
import lang24.data.mem.MemLabel;

import lang24.Compiler;

public class SourceGen{
	Vector<String> stringVec;

	public SourceGen(){
		stringVec = new Vector<String>();
	}
	public void createAsm(){
		//loads static variables, sets stack and heap pointers jumps to main
		addStart();
		
		//loads function code
		stringVec.add("\n");
		for ( Code code : AsmGen.codes ){
			if (code.frame.label.name.equals("_getchar")) continue;
			if (code.frame.label.name.equals("_putchar")) continue;
			if (code.frame.label.name.equals("_new")) continue;
			if (code.frame.label.name.equals("_del")) continue;
			if (code.frame.label.name.equals("_exit")) continue;

			addPrologue( code );
			addCode( code );
			addEpilogue( code );
			stringVec.add("\n");
		}

		addStdLib();
		printToFile();
		return;
	}

	//TODO: fix special chars in strings
	private void addStart(){
		// setup sp,fp,hp
		for( int i = 0; i < 3; i++ ){
			stringVec.add("\t\tGREG");
		}
		
		stringVec.add("\n");
		//add static data
		stringVec.add("\t\tLOC\tData_Segment");
		stringVec.add("\t\tGREG\t@");
		stringVec.add("buf	OCTA	0,0");
		stringVec.add("args	OCTA	buf,2");

		for ( LinDataChunk chunk : ImcLin.dataChunks()){
			// add nonstring static variable
			if( chunk.init == null){
				stringVec.add(chunk.label.name+"\t\tOCTA\t0");
				
				//if larger than 8 bytes allocate with LOC
				if ( chunk.size > 8)
					stringVec.add("\t\tLOC\t@+" + (chunk.size - 8));
			}
			//add string static varaible
			else {
				for( int i = 1; i < chunk.init.length() - 1; i++) {
					char c = chunk.init.charAt( i );
					if ( i == 1)
						stringVec.add(chunk.label.name+"\t\tOCTA\t" + (int)c);
					else
						stringVec.add("\t\tOCTA\t" + (int)c);

				}
				
				stringVec.add("\t\tOCTA\t0");


			}

		}

		stringVec.add("\n");
		stringVec.add("\t\tLOC\t#100");
		stringVec.add("Main\t\tSWYM");

		//TODO: init stack


		initStackAndHeap();
		
		stringVec.add("\t\tPUSHJ\t$" + Compiler.numOfRegs + ",_main");
		stringVec.add("\t\tLDO\t$255,$254,0");
		stringVec.add("EXIT\tTRAP\t0,Halt,0");



	}

	private void initStackAndHeap(){
		loadConst(0x7ffff0000000L,254);
		loadConst(0x500000,252);
		return;
	}

	private void addPrologue(Code code){
		stringVec.add("#Prologue of: " + code.frame.label.name);
		stringVec.add(code.frame.label.name + "\t\tSWYM");
	
		
		long offset = code.frame.locsSize + 8;
		loadConst( offset, 0 );

		stringVec.add("\t\tSUB\t$254,$254,$0");	
		stringVec.add("\t\tSTO\t$253,$254,0"); 	
		stringVec.add("\t\tSUB\t$254,$254,8"); 
		stringVec.add("\t\tGET\t$253,rJ");  	
		stringVec.add("\t\tSTO\t$253,$254,0");

		stringVec.add("\t\tADD\t$254,$254,8");	
		stringVec.add("\t\tADD\t$254,$254,$0");
		stringVec.add("\t\tOR\t$253,$254,0");

		loadConst(code.frame.size, 1);	
		stringVec.add("\t\tSUB\t$254,$254,$1");
		stringVec.add("\t\tJMP\t" + code.entryLabel.name);

	}

	private void addEpilogue(Code code){
		stringVec.add("#Epilogue of: " + code.frame.label.name);
		stringVec.add(code.exitLabel.name + "\t\tSWYM");

		stringVec.add("\t\tSTO\t$" + RegAll.tempToReg.get(code.frame.RV) + ",$253,0");
		stringVec.add("\t\tADD\t$254,$253,0");

		long prev = code.frame.locsSize + 8;
		loadConst(prev, 0);

		stringVec.add("\t\tSUB\t$0,$253,$0");
		stringVec.add("\t\tSUB\t$1,$0,8");
		stringVec.add("\t\tLDO\t$1,$1,0");
		stringVec.add("\t\tPUT\trJ,$1");


		stringVec.add("\t\tLDO\t$253,$0,0");
		stringVec.add("\t\tPOP\t0,0");

	}
	
	private void addCode(Code code){
		stringVec.add("#Body of: " + code.frame.label.name);

		for( AsmInstr inst : code.instrs ){
			if ( inst instanceof AsmLABEL ) 
				stringVec.add(inst.toString() + "\t\tSWYM");
			else
				stringVec.add("\t\t" + inst.toString(RegAll.tempToReg));
		}
	}

	
	
	private void loadConst(long num, int reg) {
		long h = (num >> 48) & 0xffff; 
		long mh = (num >> 32) & 0xffff;
		long ml = (num >> 16) & 0xffff;
		long l = (num >> 0) & 0xffff;

		stringVec.add("\t\tSETL\t$" + reg + "," + l);
		if (ml != 0) 	stringVec.add("\t\tINCML\t$" + reg + "," + ml);
		if (mh != 0) 	stringVec.add("\t\tINCMH\t$" + reg + "," + mh);
		if (h != 0) 	stringVec.add("\t\tINCH\t$" + reg + "," + h);

	}
	

	private void addStdLib(){
		stringVec.add("_getchar\t\tSWYM");
		stringVec.add("\t\tLDA\t$255,args");
		stringVec.add("\t\tTRAP\t0,Fgets,StdIn");
		stringVec.add("\t\tLDA\t$0,buf");
		stringVec.add("\t\tLDB\t$0,$0,0");
		stringVec.add("\t\tSTO\t$0,$254,0");
		stringVec.add("\t\tPOP\t0,0");

		stringVec.add("\n");

		stringVec.add("_putchar\t\tSWYM");
		stringVec.add("\t\tLDO\t$0,$254,8");	
		stringVec.add("\t\tLDA\t$255,buf");
		stringVec.add("\t\tSTB\t$0,$255,0");
		stringVec.add("\t\tTRAP\t0,Fputs,StdOut");
		stringVec.add("\t\tPOP\t0,0");
		
		stringVec.add("\n");

		stringVec.add("_new\t\tSWYM");
		stringVec.add("\t\tLDO\t$0,$254,8");
		stringVec.add("\t\tSTO\t$252,$254,0");
		stringVec.add("\t\tADD\t$252,$252,$0");
		stringVec.add("\t\tPOP\t0,0");

		stringVec.add("\n");

		stringVec.add("_del\tSWYM");
		stringVec.add("\t\tPOP\t0,0");
		
		stringVec.add("\n");

		stringVec.add("_exit\t\tSWYM");
		stringVec.add("\t\tJMP\tEXIT");
	}

	//TODO: accept user filename
	private void printToFile(){
		FileWriter out;
		try {
			out = new FileWriter("a.mmo");
			
			for(String x : this.stringVec){
				out.write( x );
				out.write("\n");
			}

			out.close();
		} 
		catch ( Exception e){
			System.out.println( e.getMessage() );
		}




	}
}
