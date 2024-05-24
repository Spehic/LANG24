package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;
import lang24.common.report.*;

public class Node {
	int color;
	MemTemp temp;
	HashSet<Node> neighbors;

	public Node( MemTemp temp ){
		this.temp = temp;
		this.neighbors = new HashSet<Node>();
		this.color = -1;
		//this.potSpill = false;
	}

	public void addNeighbor( Node node ){
		if ( this == node ) return;
		if ( this.neighbors.contains( node)) return;
		neighbors.add( node );
	}

	public void addNeighbors( HashSet<Node> temps ){
		neighbors.addAll( temps );
		neighbors.remove( this );
	}

	public void color( int max ){
		for( int i = 0; i < max; i++ ){
			boolean available = true;

			for ( Node x : this.neighbors ){
				if ( x.color == i ){
					available = false;
					break;
				} 		
			}	

			if ( available ) {
				this.color = i;
				return;
			}
		}
		
		throw new Report.InternalError();
	}

	@Override
	public String toString(){
		String ret = " ";
		ret += this.temp.toString();

		return ret;
	}
}
