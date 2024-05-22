package lang24.phase.regall;

import java.util.*;

import lang24.data.mem.*;

public class Node {
	MemTemp temp;
	HashSet<MemTemp> neighbors;

	public Node( MemTemp temp ){
		this.temp = temp;
		this.neighbors = new HashSet<MemTemp>();
		this.potSpill = false;
	}

	public addNeighbor( MemTemp temp ){
		neighbors.add( temp );
	}

	public addNeighbors( HashSet<MemTemp> temps ){
		neighbors.addAll( temps );
	}
}
