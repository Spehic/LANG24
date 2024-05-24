package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;
import lang24.data.asm.*;

public class Graph {
	
	HashSet<Node> nodes;
	HashMap<MemTemp, Node> tmpToNode;
	Stack<Node> stk;

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
					//create node if no node exists for neigbors
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
		


		return false;
	}

}
