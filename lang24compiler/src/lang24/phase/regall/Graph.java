package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;
import lang24.data.asm.*;

import lang24.phase.regall.*;
import lang24.*;

public class Graph {
	
	HashSet<Node> nodes;
	HashMap<MemTemp, Node> tmpToNode;
	Stack<Node> stk;
	public MemTemp fp;

	public Graph(){
		nodes = new HashSet<Node>();
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
				//mark as potential spill and push on stack
				n.potSpill = true;
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

	public void select(){
		Node curNode = null;
		while( stk.size() != 0 ){
			curNode = stk.pop();
			
			if( curNode.temp == fp )
				RegAll.tempToReg.put( curNode.temp, 253);


		}	
	}

}
