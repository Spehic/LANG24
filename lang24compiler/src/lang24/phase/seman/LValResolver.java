package lang24.phase.seman;

import lang24.data.ast.tree.expr.*;
import lang24.data.ast.visitor.*;

/**
 * Lvalue resolver.
 * 
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class LValResolver implements AstFullVisitor<Boolean, Object> {

	/** Constructs a new lvalue resolver. */
	public LValResolver() {
	}
	
	@Override
	public Boolean visit(AstNameExpr nameExpr, Object obj){
		SemAn.isLVal.put(nameExpr, true);
		return true;
	}

	@Override
	public Boolean visit(AstArrExpr arr, Object obj){
		if(arr.arr.accept(this, obj)){
			SemAn.isLVal.put(arr, true);
			return true;
		}
		
		else
			return false;
	}

	@Override
	public Boolean visit(AstSfxExpr sfx, Object obj){
		sfx.expr.accept(this, obj);
		SemAn.isLVal.put(sfx, true);
		return true;
	}

	@Override
	public Boolean visit(AstCmpExpr cmp, Object obj){
		if(cmp.expr.accept(this, obj)){
			SemAn.isLVal.put(cmp, true);
			return true;
		}
		else
			return false;
	}

}
