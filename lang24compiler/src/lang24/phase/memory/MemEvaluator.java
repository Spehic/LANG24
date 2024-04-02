package lang24.phase.memory;

import java.util.*;

import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.stmt.*;
import lang24.data.ast.tree.expr.AstCallExpr;
import lang24.data.ast.tree.type.AstRecType;
import lang24.data.ast.tree.type.AstStrType;
import lang24.data.ast.tree.type.AstUniType;
import lang24.data.ast.tree.type.AstType;
import lang24.data.ast.visitor.*;
import lang24.data.mem.*;
import lang24.data.type.*;
import lang24.data.type.visitor.*;
import lang24.phase.seman.SemAn;

/**
 * Computing memory layout: stack frames and variable accesses.
 * 
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class MemEvaluator implements AstFullVisitor<Integer, Integer>{

	private static int currDepth = 0;
	static int currentParamOffset = 0;
	static int currentComponentOffset = 0;
	static int currentLocalOffset = 0;
	static long currentMaxArg = 0;

	private static int calcSize(SemType typ){
		int total = 0;
		if( typ instanceof SemIntType)
			return 8;
		if( typ instanceof SemPointerType)
			return 8;
		if( typ instanceof SemBoolType)
			return 8;
		if( typ instanceof SemCharType)
			return 8;
		if( typ instanceof SemVoidType)
			return 0;
		if( typ instanceof SemNameType){
			SemNameType name = (SemNameType) typ;
			return calcSize(name.type());
		}
		if( typ instanceof SemArrayType){
			SemArrayType arr = (SemArrayType) typ;
			return (int )arr.size * calcSize(arr.elemType);
		}
		if (typ instanceof SemStructType){
			SemStructType str = (SemStructType) typ;
			for( SemType a : str.cmpTypes )
				total += calcSize(a);
			return total;
		}
		if (typ instanceof SemUnionType){
			SemUnionType uni = (SemUnionType) typ;
			for( SemType a : uni.cmpTypes ){
				int x = calcSize(a);
				if( x > total)
					total = x;
			}
			return total;
		}

		return -1;
	}

	@Override
	public Integer visit(AstNodes<? extends AstNode> nodes, Integer arg){
		int total = 0;
		for(AstNode node : nodes){
			if(node instanceof AstStmt){
				node.accept(this, arg);
				continue;
			}
			if(node instanceof AstExpr){
				node.accept(this, arg);
				continue;
			}

			Integer res = node.accept(this, arg);
			if(res != null)
				total += res;
		}

		return total;
	}

	@Override
	public Integer visit(AstVarDefn var, Integer arg){
		int size = calcSize(SemAn.ofType.get(var));
		MemLabel label = new MemLabel(var.name);
		if( currDepth == 0) {
			MemAbsAccess mem = new MemAbsAccess(size, label, null);
			Memory.varAccesses.put(var, mem);
			return size;
		} else {
			MemRelAccess mem = new MemRelAccess(size, currentLocalOffset, currDepth);
			currentLocalOffset -= size;
			Memory.varAccesses.put(var, mem);
			return size;
		}	
	}

	@Override
	public Integer visit(AstFunDefn funDefn, Integer arg){
		
		long localSize = 0;
		currentParamOffset = 0;
		currentLocalOffset = -8;

		currDepth += 1;
		if (funDefn.pars != null)
			funDefn.pars.accept(this, arg);

		if (funDefn.defns != null)
			localSize = funDefn.defns.accept(this, arg);
		
		currentMaxArg = 0;
		if(funDefn.stmt != null)
			funDefn.stmt.accept(this, arg);

		long totalSize = localSize + 8 + 8 + currentMaxArg;
		MemLabel label = new MemLabel(funDefn.name);
		MemFrame frame = new MemFrame(label, (long)currDepth, localSize, currentMaxArg, totalSize);
		Memory.frames.put(funDefn, frame);

		currDepth -= 1;
		return 0;
	}

	@Override
	public Integer visit(AstFunDefn.AstValParDefn valPar, Integer arg){
		int size = calcSize(SemAn.ofType.get(valPar));
		MemRelAccess mem = new MemRelAccess(size, currentParamOffset, currDepth);
		currentParamOffset += size;
		Memory.parAccesses.put(valPar, mem);

		return size;
	}

	@Override
	public Integer visit(AstFunDefn.AstRefParDefn refPar, Integer arg){
		int size = calcSize(SemAn.ofType.get(refPar));
		MemRelAccess mem = new MemRelAccess(size, currentParamOffset, currDepth);
		currentParamOffset += size;
		Memory.parAccesses.put(refPar, mem);

		return size;
	}

	@Override
	public Integer visit(AstStrType typ, Integer arg){
		int total = 0;
		currentComponentOffset = 0;

		for( AstRecType.AstCmpDefn cmp : typ.cmps ){
			total += cmp.accept(this, 0);
		}

		return total;
	}

	@Override
	public Integer visit(AstUniType typ, Integer arg){
		int total = 0;

		for( AstRecType.AstCmpDefn cmp : typ.cmps ){
			Integer res = cmp.accept(this, 1);
			if(res > total)
				total = res;
		}

		return total;
	}

	@Override
	public Integer visit(AstRecType.AstCmpDefn cmp, Integer arg){
		int size = calcSize(SemAn.ofType.get(cmp));
		
		//struct type
		if( arg == 0 ){
			MemRelAccess mem = new MemRelAccess(size, currentComponentOffset, currDepth);
			currentComponentOffset += size;
			Memory.cmpAccesses.put(cmp, mem);
			return size;
		} else {
			MemRelAccess mem = new MemRelAccess(size, 0, currDepth);
			Memory.cmpAccesses.put(cmp, mem);
			return size;
		}	
	}


	@Override
	public Integer visit(AstAtomExpr atom, Integer arg){
		
		if(atom.type == AstAtomExpr.Type.STR){
			MemLabel label = new MemLabel();
			MemAbsAccess mem = new MemAbsAccess(atom.value.length() + 1, label,atom.value);
			Memory.strings.put(atom, mem);
		}


		return null;
	}

	@Override
	public Integer visit(AstCallExpr call, Integer arg){
		int total = 0;
		
		if(call.args == null) 
			return null;

		for(AstExpr ex : call.args){
			total += calcSize(SemAn.ofType.get(ex));
		}
		
		if(total > currentMaxArg)
			currentMaxArg = total;

		return null;
	}
}
