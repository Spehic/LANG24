package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;
import lang24.data.asm.*;
import lang24.data.imc.code.expr.*;
import lang24.data.imc.code.stmt.*;

import lang24.phase.asmgen.*;

import lang24.phase.regall.*;
import lang24.phase.livean.*;
import lang24.*;

public class Graph {
	
	public static HashSet<MemTemp> alreadySpilled;

	HashSet<Node> nodes;
	HashMap<MemTemp, Node> tmpToNode;
	Stack<Node> stk;
	public MemTemp fp;
	public Code code;

	public Graph(){
		nodes = new HashSet<Node>();
		alreadySpilled = new HashSet<MemTemp>();
		tmpToNode = new HashMap<MemTemp, Node>();
		stk = new Stack<Node>();
	}

	public void addNode( Node node ) {
		nodes.add( node );
	}

	@Override
	public String toString(){
		String ret = "";
		for ( Node node : nodes ){
			ret += "Node : ";
			ret += node.toString();
			
			ret+= "\tNeighbors : ";
			for (Node neigh : node.neighbors )
				ret += neigh.toString();

			ret += "\n";

		}

		return ret;
	}

	public void printStack(){
		System.out.println("------- stack -------");
		for( Node n : stk){
			System.out.println(n);
		}
		System.out.println("------- stack -------");
	}


	public void build( Vector<AsmInstr> instrs){
		for( AsmInstr ins : instrs) {
			HashSet<MemTemp> uniqTemps = ins.in();
			uniqTemps.addAll( ins.out() );
			uniqTemps.addAll( ins.defs() );
			
			for ( MemTemp reg : uniqTemps ) {
				// create node if no node exists for temp
				Node regNode = tmpToNode.get(reg);
				if ( regNode == null ){
					regNode = new Node( reg );
					tmpToNode.put(reg, regNode );
				}
				
				//add node to graph
				if ( !nodes.contains ( regNode ))
					nodes.add( regNode );

				//add all neighbors
				Node neighNode = null;
				for( MemTemp im : uniqTemps ) {
					//create node if no node exists for neighbors
					neighNode = tmpToNode.get( im );
					if( neighNode == null ) {
						neighNode = new Node( im );
						tmpToNode.put(im, neighNode );
					}
					
					//dont add yourself as neighbor
					if ( neighNode == regNode ) continue;
					regNode.addNeighbor ( neighNode );
				}
			}


		}

		return;
	}

	public boolean simplfy(){
		boolean doable = true;
		while( doable ){
			doable = false;
			for( Node n : nodes ){
				if( n.neighbors.size() < Compiler.numOfRegs ){
					//push on stack
					stk.push(n);
					
					//remove from neighbors
					for (Node neigh : n.neighbors) {
						neigh.removeNeighbor( n );
					}

					//remove from graph
					nodes.remove(n);
					doable = true;
					break;
				}
			}
			
			//if it is not simplyable anymore spill a reg and continue
			if( !doable ) doable = spill();
		}
		
		if ( nodes.size() == 0) return true;
		// throw new Report.InternalError();
		return false;
	}

	public boolean spill(){
		for( Node n : nodes ) {
			if ( n.neighbors.size() >= Compiler.numOfRegs ){
				// if this temp has already been spilled do not spill again
				if ( alreadySpilled.contains( n.temp )) continue;
				
				//mark as potential spill and push on stack
				n.potSpill = true;
				alreadySpilled.add( n.temp );
				stk.push(n);

				//remove node from neighbors
				for(Node neigh : n.neighbors){
					neigh.removeNeighbor(n);
				}

				//remove node from graph
				nodes.remove(n);
				return true;
			}
		}
		
		// nothing more to spill
		return false;
	}

	public void select( Stack<Node> stack ){
		Node curNode = null;
		while( stack.size() != 0 ){
			curNode = tmpToNode.get(stack.pop().temp);
			
			if( curNode.temp == fp ){
				RegAll.tempToReg.put( curNode.temp, 253);
				continue;
			}

			if( curNode.color( Compiler.numOfRegs ) ){
				RegAll.tempToReg.put( curNode.temp, curNode.color );
			} else{
				handleSpill( curNode );
			}


		}	
	}

	public void handleSpill( Node n ){
		//TODO: empty the stack
		
		MemTemp curTemp = n.temp;
		code.tempSize += 8;
		//long locsSize = code.frame.locsSize;
		long off = - code.frame.locsSize - 16 - code.tempSize;
		
		ImcCONST offset = new ImcCONST(off);

		for( int i = 0; i < code.instrs.size(); i++ ) {
			AsmInstr instr = code.instrs.get(i);

			boolean uses = instr.uses().contains( curTemp );
			boolean defines = instr.defs().contains( curTemp );
			
			MemTemp newTemp = new MemTemp();

			if ( uses ) {
				Vector<AsmInstr> temp = new Vector<AsmInstr>();
				MemTemp reg = offset.accept( new ExpressionGen(), temp);
	
				String instrString = "	LDO `d0,`s0,`s1";
				Vector<MemTemp> use = new Vector<MemTemp>();
				Vector<MemTemp> defs = new Vector<MemTemp>();
				Vector<MemLabel> jmps = new Vector<MemLabel>();

				use.add( code.frame.FP );
				use.add( reg );
				defs.add( newTemp );
				
				Vector<MemTemp> newUses = instr.uses();
				newUses.remove( curTemp );
				newUses.add( newTemp );
				//modify instruction
				AsmOPER modified = new AsmOPER(((AsmOPER) instr).instr(), newUses, instr.defs(), instr.jumps());
				//code.instrs.insertElementAt(modified, i);
				//create new instruction
				AsmOPER load = new AsmOPER(instrString, use , defs, jmps);
					
				temp.add(load);
				temp.add(modified);
				code.instrs.addAll(i, temp);
				code.instrs.remove(i + temp.size());
				i += temp.size() - 1;
			}

			if ( defines ) {
				Vector<AsmInstr> temp = new Vector<AsmInstr>();
				MemTemp reg = offset.accept( new ExpressionGen(), temp);
	
				Vector<MemTemp> modifiedDefs = new Vector<MemTemp>();
				modifiedDefs.add( newTemp );
				AsmOPER modified = new AsmOPER(((AsmOPER)instr).instr(), instr.uses(), modifiedDefs, instr.jumps());
				
				String instrString = "	STO `s0,`s1,`s2";
				Vector<MemTemp> use = new Vector<MemTemp>();
				Vector<MemTemp> defs = new Vector<MemTemp>();
				Vector<MemLabel> jmps = new Vector<MemLabel>();
				
				use.add ( newTemp );
				use.add( code.frame.FP );
				use.add( reg );

				AsmOPER store = new AsmOPER(instrString, use , defs, jmps);
				
				code.instrs.remove(i);
				temp.add(modified);
				temp.add(store);
				code.instrs.addAll(i, temp);
				i += temp.size();
			}


		}

		LiveAn liv = new LiveAn();
		liv.analysis();
		liv.log();
		//System.out.println("Spilled " + curTemp);
		//for ( AsmInstr c : code.instrs ) System.out.println( c );
		RegAll.allocateFnc( code );
	}

}
