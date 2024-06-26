package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;
import lang24.common.report.*;

public class Node {
	public int color;
	MemTemp temp;
	HashSet<Node> neighbors;
	public boolean potSpill;

	public Node( MemTemp temp ){
		this.temp = temp;
		this.neighbors = new HashSet<Node>();
		this.color = -1;
		this.potSpill = false;
	}

	public void addNeighbor( Node node ){
		if ( this == node ) return;
		if ( this.neighbors.contains( node)) return;
		neighbors.add( node );
	}

	public void removeNeighbor( Node node ){
		if ( this == node ) return;
		neighbors.remove( node );
	}

	public void addNeighbors( HashSet<Node> temps ){
		neighbors.addAll( temps );
		neighbors.remove( this );
	}

	public boolean color( int max ){
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
				return true;
			}
		}
		
		return false;
		//throw new Report.InternalError();
	}

	@Override
	public String toString(){
		String ret = " ";
		ret += this.temp.toString();
		if( potSpill ) ret += " spill";
		return ret;
	}
}
