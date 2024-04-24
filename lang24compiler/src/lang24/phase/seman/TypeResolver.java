package lang24.phase.seman;

import java.util.*;
import lang24.common.report.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.stmt.*;
import lang24.data.ast.tree.type.*;
import lang24.data.ast.visitor.*;
import lang24.data.type.*;

/**
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class TypeResolver implements AstFullVisitor<SemType, Integer> {

	//map that holds namespaces for different record types (each record type introduces a new namespace -> SymbolTable)
	final static public HashMap<SemType, SymbTable> recMap = new HashMap<SemType, SymbTable>();
	//stack used for checking type of return statements
	final static private Stack<SemType> lastFnc = new Stack<SemType>();

	/**
	 * Structural equivalence of types.
	 * 
	 * @param type1 The first type.
	 * @param type2 The second type.
	 * @return {@code true} if the types are structurally equivalent, {@code false}
	 *         otherwise.
	 */
	private boolean equiv(SemType type1, SemType type2) {
		return equiv(type1, type2, new HashMap<SemType, HashSet<SemType>>());
	}

	/**
	 * Structural equivalence of types.
	 * 
	 * @param type1  The first type.
	 * @param type2  The second type.
	 * @param equivs Type synonyms assumed structurally equivalent.
	 * @return {@code true} if the types are structurally equivalent, {@code false}
	 *         otherwise.
	 */
	private boolean equiv(SemType type1, SemType type2, HashMap<SemType, HashSet<SemType>> equivs) {

		if ((type1 instanceof SemNameType) && (type2 instanceof SemNameType)) {
			if (equivs == null)
				equivs = new HashMap<SemType, HashSet<SemType>>();

			if (equivs.get(type1) == null)
				equivs.put(type1, new HashSet<SemType>());
			if (equivs.get(type2) == null)
				equivs.put(type2, new HashSet<SemType>());
			if (equivs.get(type1).contains(type2) && equivs.get(type2).contains(type1))
				return true;
			else {
				HashSet<SemType> types;

				types = equivs.get(type1);
				types.add(type2);
				equivs.put(type1, types);

				types = equivs.get(type2);
				types.add(type1);
				equivs.put(type2, types);
			}
		}

		type1 = type1.actualType();
		type2 = type2.actualType();

		if (type1 instanceof SemVoidType)
			return (type2 instanceof SemVoidType);
		if (type1 instanceof SemBoolType)
			return (type2 instanceof SemBoolType);
		if (type1 instanceof SemCharType)
			return (type2 instanceof SemCharType);
		if (type1 instanceof SemIntType)
			return (type2 instanceof SemIntType);

		if (type1 instanceof SemArrayType) {
			if (!(type2 instanceof SemArrayType))
				return false;
			final SemArrayType arr1 = (SemArrayType) type1;
			final SemArrayType arr2 = (SemArrayType) type2;
			if (arr1.size != arr2.size)
				return false;
			return equiv(arr1.elemType, arr2.elemType, equivs);
		}

		if (type1 instanceof SemPointerType) {
			if (!(type2 instanceof SemPointerType))
				return false;
			final SemPointerType ptr1 = (SemPointerType) type1;
			final SemPointerType ptr2 = (SemPointerType) type2;
			if ((ptr1.baseType.actualType() instanceof SemVoidType)
					|| (ptr2.baseType.actualType() instanceof SemVoidType))
				return true;
			return equiv(ptr1.baseType, ptr2.baseType, equivs);
		}

		if (type1 instanceof SemStructType) {
			if (!(type2 instanceof SemStructType))
				return false;
			final SemStructType str1 = (SemStructType) type1;
			final SemStructType str2 = (SemStructType) type2;
			if (str1.cmpTypes.size() != str2.cmpTypes.size())
				return false;
			for (int c = 0; c < str1.cmpTypes.size(); c++)
				if (!(equiv(str1.cmpTypes.get(c), str2.cmpTypes.get(c), equivs)))
					return false;
			return true;
		}
		if (type1 instanceof SemUnionType) {
			if (!(type2 instanceof SemUnionType))
				return false;
			final SemUnionType uni1 = (SemUnionType) type1;
			final SemUnionType uni2 = (SemUnionType) type2;
			if (uni1.cmpTypes.size() != uni2.cmpTypes.size())
				return false;
			for (int c = 0; c < uni1.cmpTypes.size(); c++)
				if (!(equiv(uni1.cmpTypes.get(c), uni2.cmpTypes.get(c), equivs)))
					return false;
			return true;
		}

		throw new Report.InternalError();
	}

	/*** TODO ***/


	@Override
	public SemType visit(AstNodes<? extends AstNode> nodes, Integer arg){
		for(int i = 0; i < 3; i++){
			for(AstNode node: nodes){
				if(node instanceof AstTypDefn)
					node.accept(this, i);
				if(node instanceof AstVarDefn)
					node.accept(this, i);
			}
		}
		for(int i = 0; i < 3; i++){
			for(AstNode node: nodes){
				if(node instanceof AstFunDefn)
					node.accept(this, i);
			}
		}

		for(AstNode node: nodes){
			if(node instanceof AstFunDefn.AstValParDefn)
				node.accept(this, arg);
			if(node instanceof AstFunDefn.AstRefParDefn)
				node.accept(this, arg);
			if(node instanceof AstStmt)
				node.accept(this, arg);
			if(node instanceof AstExpr)
				node.accept(this, arg);
		}

		return null;
	}
	//Type Expressions

	@Override
	public SemType visit(AstAtomType atom, Integer arg){
		switch(atom.type){
			case AstAtomType.Type.VOID -> {
				SemAn.isType.put(atom, SemVoidType.type);
				return SemVoidType.type;

			}			
			case AstAtomType.Type.BOOL -> {
				SemAn.isType.put(atom, SemBoolType.type);
				return SemBoolType.type;

			}
			case AstAtomType.Type.CHAR -> {
				SemAn.isType.put(atom, SemCharType.type);
				return SemCharType.type;

			}
			case AstAtomType.Type.INT -> {
				SemAn.isType.put(atom, SemIntType.type);
				return SemIntType.type;

			}

		}
		return null;
	}

	@Override
	public SemType visit(AstNameType nameT, Integer arg){
		if( arg == 1) {
			AstDefn def = SemAn.definedAt.get(nameT);
			SemType res = SemAn.isType.get(def);
			SemAn.isType.put(nameT, res);
			return res;
		}
		return null;
	}

	@Override
	public SemType visit(AstPtrType ptrType, Integer arg){
		SemType res = ptrType.baseType.accept(this, arg);
		SemType ptr = new SemPointerType(res);
		SemAn.isType.put(ptrType, ptr);
		return ptr;
	}


	@Override
	public SemType visit(AstArrType arr, Integer arg){
		SemType typ = arr.elemType.accept(this, arg);
		
		if (typ instanceof SemVoidType){
			System.out.println("Arrays of void types not allowed: " + arr.location());
			System.exit(1);
		}

		AstAtomExpr val = (AstAtomExpr ) arr.size;
		arr.size.accept(this, arg);

		long size = 0;
		try{
			size = Long.valueOf(val.value);
		}catch(NumberFormatException e){
			System.out.println("Wrong format of int literal: " + arr.location());
			System.exit(1);
		}
		
		SemType sem = new SemArrayType(typ, size);
		SemAn.isType.put(arr, sem);
		return sem;
	}

	//recMap
	@Override
	public SemType visit(AstStrType struct, Integer arg){
		SymbTable symb = new SymbTable();
		ArrayList<SemType> list = new ArrayList<SemType>();

		for(AstRecType.AstCmpDefn node : struct.cmps){
			node.accept(this, arg);
			try{
				symb.ins(node.name, node);
			}catch (Exception e){
				System.out.println("Duplicate name in struct definition: " + struct.location());
				System.exit(1);
			}


			SemType typ = node.type.accept(this, arg);
			list.add(typ);
		}



		SemStructType res = new SemStructType(list);
		recMap.put(res, symb);
		SemAn.isType.put(struct, res);
		return res;
	}

	@Override
	public SemType visit(AstUniType union, Integer arg){
		SymbTable symb = new SymbTable();
		ArrayList<SemType> list = new ArrayList<SemType>();

		for(AstRecType.AstCmpDefn node : union.cmps){
			node.accept(this, arg);
			try{
				symb.ins(node.name, node);
			}catch (Exception e){
				System.out.println("Duplicate name in union definition: " + union.location());
				System.exit(1);
			}


			SemType typ = node.type.accept(this, arg);
			list.add(typ);
		}



		SemUnionType res = new SemUnionType(list);
		recMap.put(res, symb);
		SemAn.isType.put(union, res);
		return res;
	}
	
	//ValueExpression
	
	@Override
	public SemType visit(AstAtomExpr expr, Integer arg){
		switch(expr.type){
			case AstAtomExpr.Type.VOID ->{
				SemAn.ofType.put(expr, SemVoidType.type);
				return SemVoidType.type;
			}
			case AstAtomExpr.Type.BOOL->{
				SemAn.ofType.put(expr, SemBoolType.type);
				return SemBoolType.type;
			}
			case AstAtomExpr.Type.CHAR->{
				SemAn.ofType.put(expr, SemCharType.type);
				return SemCharType.type;
			}
			case AstAtomExpr.Type.INT->{
				SemAn.ofType.put(expr, SemIntType.type);
				return SemIntType.type;
			}
			case AstAtomExpr.Type.STR->{
				SemType sem = new SemPointerType(SemCharType.type);
				SemAn.ofType.put(expr, sem);
				return sem;
			}
			case AstAtomExpr.Type.PTR->{
				SemType sem = new SemPointerType(SemVoidType.type);
				SemAn.ofType.put(expr, sem);
				return sem;
			}
		}

		return null;
	}

	@Override
	public SemType visit(AstNameExpr expr, Integer arg){
		AstDefn def = SemAn.definedAt.get(expr);
		SemType typ = SemAn.ofType.get(def);


		if(typ == null){
			throw new Report.InternalError();
		}

		SemAn.ofType.put(expr, typ);
		return typ;
	}

	@Override
	public SemType visit(AstPfxExpr pfxExpr, Integer arg){
		SemType res = pfxExpr.expr.accept(this, arg);
		switch(pfxExpr.oper){
			case AstPfxExpr.Oper.NOT: 
				if(!(equiv(res, SemBoolType.type))){
					System.out.println("Incompatible types at: " + pfxExpr.location());
					System.exit(1);
				}
				SemAn.ofType.put(pfxExpr, SemBoolType.type);
				return SemBoolType.type;
			
			case AstPfxExpr.Oper.ADD:
			case AstPfxExpr.Oper.SUB:
				if(!(equiv(res, SemIntType.type))){
					System.out.println("Incompatible types at: " + pfxExpr.location());
					System.exit(1);
				}
				SemAn.ofType.put(pfxExpr, SemIntType.type);
				return SemIntType.type;
			
			case AstPfxExpr.Oper.PTR:
				if( SemAn.isLVal.get(pfxExpr.expr) == null) {
					System.out.println("Lval expected: " + pfxExpr.location());
					System.exit(1);
				}
				SemType typ = new SemPointerType(res);
				SemAn.ofType.put(pfxExpr, typ);
				return typ;
		}
		
		return null;
	}

	@Override
	public SemType visit(AstSfxExpr sfx, Integer arg){
		SemType res = sfx.expr.accept(this, arg).actualType();
		if(!( res instanceof SemPointerType )){
			System.out.println("Pointer type expected at: " + sfx.location());
			System.exit(1);
		}
		SemPointerType ptrRes = (SemPointerType) res;
		SemType actual = ptrRes.baseType;
		SemAn.ofType.put(sfx, actual);
		return actual;
	}

	
	@Override
	public SemType visit(AstBinExpr expr, Integer arg){
		
		SemType typ1 = expr.fstExpr.accept(this, arg).actualType();
		SemType typ2 = expr.sndExpr.accept(this, arg).actualType();

		if( !equiv(typ1, typ2) ){
			System.out.println("Type mismatch at: " + expr.location());	
			System.exit(1);
		}
		
		switch(expr.oper){
			case AstBinExpr.Oper.ADD:
			case AstBinExpr.Oper.SUB:
			case AstBinExpr.Oper.MUL:
			case AstBinExpr.Oper.DIV:
			case AstBinExpr.Oper.MOD:
				if(!( typ1 instanceof SemIntType)){
					System.out.println("Int expected at: " + expr.location());	
					System.exit(1);
				}
				if(!( typ2 instanceof SemIntType)){
					System.out.println("Int expected at: " + expr.location());	
					System.exit(1);
				}

				SemAn.ofType.put(expr, SemIntType.type);
				return SemIntType.type;
			
			case AstBinExpr.Oper.EQU:
			case AstBinExpr.Oper.NEQ:
				if( typ1 instanceof SemVoidType){
					System.out.println("Void unexpected at: " + expr.location());	
					System.exit(1);
				}
				if( typ2 instanceof SemVoidType){
					System.out.println("Void unexpected at: " + expr.location());	
					System.exit(1);
				}

				SemAn.ofType.put(expr, SemBoolType.type);
				return SemBoolType.type;
			
			case AstBinExpr.Oper.AND:
			case AstBinExpr.Oper.OR:
				if(!( typ1 instanceof SemBoolType )){
					System.out.println("Bool expected at: " + expr.location());	
					System.exit(1);
				}
				if(!( typ2 instanceof SemBoolType )){
					System.out.println("Bool expected at: " + expr.location());	
					System.exit(1);
				}

				SemAn.ofType.put(expr, SemBoolType.type);
				return SemBoolType.type;
			
			case AstBinExpr.Oper.LTH:
			case AstBinExpr.Oper.GTH:
			case AstBinExpr.Oper.LEQ:
			case AstBinExpr.Oper.GEQ:
				if( typ1 instanceof SemVoidType || typ1 instanceof SemBoolType ){
					System.out.println("Unexpected type at: " + expr.location());	
					System.exit(1);
				}
				if( typ2 instanceof SemVoidType || typ2 instanceof SemBoolType ){
					System.out.println("Unexpected type at: " + expr.location());	
					System.exit(1);
				}

				SemAn.ofType.put(expr, SemBoolType.type);
				return SemBoolType.type;
		}

		return null;
	}

//TODO check if call is defined as function
	@Override
	public SemType visit(AstCallExpr call, Integer arg){
		AstFunDefn def = (AstFunDefn) SemAn.definedAt.get(call);
		SemType resType = SemAn.ofType.get(def);
		//System.out.println(resType);

		int callSize = 0;
		int defSize = 0;

		if(def.pars != null){
			defSize = def.pars.size();
		}

		if(call.args != null){
			callSize = call.args.size();
		}

		if(defSize != callSize){
			System.out.println("Incorrect amount of arguments passed at: " + call.location());
			System.exit(1);
		}

		if(callSize != 0){
			for(int i = 0; i < def.pars.size(); i++){
		
				AstNode a = def.pars.get(i);
				AstNode b = call.args.get(i);
				SemType at = SemAn.ofType.get(a);
				SemType bt = b.accept(this, arg);
				if (!equiv(at, bt)){
					System.out.println("Type mismatch at: " + call.location());
					System.exit(1);
				}
			}
		}

		SemAn.ofType.put(call, resType);
		return resType;
	}

	//check lval
	@Override
	public SemType visit(AstArrExpr arr, Integer arg){
		SemType res1 = arr.arr.accept(this, arg).actualType();
		if(!( res1 instanceof SemArrayType)){
			System.out.println("Array expected: " + arr.location());
			System.exit(1);
		}

		SemType res2 = arr.idx.accept(this, 1);
		if(!( equiv(res2, SemIntType.type))){
			System.out.println("Array expects int index: " + arr.location());
			System.exit(1);
		}

		Boolean chk = SemAn.isLVal.get(arr.arr);
		if(!chk) {
			System.out.println("Array expects lval at: "  + arr.location());
			System.exit(1);
		}
		
		SemArrayType arrTyp = (SemArrayType) res1;
		SemAn.ofType.put(arr, arrTyp.elemType);
		return arrTyp.elemType;
	}

	// v symbol tablu rabim iz imena komponente dobit SemType 
	@Override
	public SemType visit(AstCmpExpr cmp, Integer arg){
		SemType typ = cmp.expr.accept(this, arg);
		SemType actual = typ.actualType();
		SymbTable symb = recMap.get(actual);

		AstRecType.AstCmpDefn node = null;

		try{
			 node = (AstRecType.AstCmpDefn) symb.fnd(cmp.name);
		}catch(SymbTable.CannotFndNameException e){
			System.out.println("Component name not found "  + cmp.location());
			System.exit(1);
		}

		SemType res = SemAn.ofType.get(node);
		SemAn.ofType.put(cmp, res);
		return res;
	}
	
	//TODO ADD lvalue check
	@Override
	public SemType visit(AstCastExpr cast, Integer arg){
		SemType expr = cast.expr.accept(this, arg);
		SemType typ = cast.type.accept(this, 1);

		SemAn.ofType.put(cast, typ);
		return typ;
	}

	@Override
	public SemType visit(AstSizeofExpr size, Integer arg){
		SemType res = size.type.accept(this, 1);

		if( res == null){
			throw new Report.InternalError();
		}

		SemAn.ofType.put(size, SemIntType.type);
		return SemIntType.type;
	}


	//Statements
	
	@Override
	public SemType visit(AstExprStmt stmt, Integer arg){
		SemType res = stmt.expr.accept(this, arg);
		if(!( equiv(res, SemVoidType.type) )){
			System.out.println("Statement must be of void type: "  + stmt.location());
			System.exit(1);
		}

		SemAn.ofType.put(stmt, SemVoidType.type);
		return SemVoidType.type;
	}

	@Override
	public SemType visit(AstAssignStmt stmt, Integer arg){
		SemType expr1 = stmt.dst.accept(this, arg);
		SemType expr2 = stmt.src.accept(this, arg);

		if(!( equiv(expr1, expr2) )){
			System.out.println("Type mismatch at: "  + stmt.location() + expr1 + " " + expr2);
			System.exit(1);
		}

		if( expr1 instanceof SemVoidType) {
			System.out.println("Void type not allowed in assign statement: "  + stmt.location());
			System.exit(1);
		}

		SemAn.ofType.put(stmt, SemVoidType.type);
		return SemVoidType.type;
	}

	@Override
	public SemType visit(AstIfStmt stmt, Integer arg){
		SemType typ = stmt.cond.accept(this, arg);
		SemType stms1 = stmt.thenStmt.accept(this, arg);
		SemType stms2 = null;


		if(stmt.elseStmt != null){
			stms2 = stmt.elseStmt.accept(this, arg);
		}

		if(!( typ instanceof SemBoolType)) {
			System.out.println("If condition must be of bool type: "  + stmt.location());
			System.exit(1);
		}

		if(stms1 == null || (stmt.elseStmt != null) && stms2 == null){
			throw new Report.InternalError();
		}

		SemAn.ofType.put(stmt, SemVoidType.type);
		return SemVoidType.type;
	}

	@Override
	public SemType visit(AstWhileStmt stmt, Integer arg){
		SemType typ = stmt.cond.accept(this, arg);

		if(typ == null)
			throw new Report.InternalError();

		if(!( typ instanceof SemBoolType)){
			System.out.println("While condition must be of bool type: "  + stmt.location());
			System.exit(1);
		}

		SemType stmts = stmt.stmt.accept(this, arg);
		if(stmt == null)
			throw new Report.InternalError();

		SemAn.ofType.put(stmt, SemVoidType.type);
		return SemVoidType.type;
	}

	@Override
	public SemType visit(AstBlockStmt blockStmt, Integer arg){
		if (blockStmt.stmts != null)
			blockStmt.stmts.accept(this, arg);
		SemAn.ofType.put(blockStmt, SemVoidType.type);
		return SemVoidType.type;
	}

	@Override
	public SemType visit(AstReturnStmt retStmt, Integer arg){
		SemType res = retStmt.expr.accept(this, arg);
		SemType last = lastFnc.peek();

		if(!( equiv(res, last) )){
			System.out.println("Return type does not match: "  + retStmt.location());
			System.exit(1);
		}
		SemAn.ofType.put(retStmt, SemVoidType.type);
		return SemVoidType.type;
	}

	//Declarations
	@Override
	public SemType visit(AstTypDefn typDefn, Integer arg){
		if( arg == 0 ){
			SemType newTyp = new SemNameType(typDefn.name);
			SemAn.isType.put(typDefn, newTyp);
		}else if ( arg == 1){
			SemNameType typ = (SemNameType) SemAn.isType.get(typDefn);
			SemType res = typDefn.type.accept(this, arg);
			typ.define(res);
		}
		return null;
	}

	@Override
	public SemType visit(AstVarDefn varDefn, Integer arg){
		if( arg == 1){
			SemType resType = varDefn.type.accept(this, arg);

			if(resType instanceof SemVoidType){
				System.out.println("Void variables not allowed: " + varDefn.location());
				System.exit(1);
			}

			SemAn.ofType.put(varDefn, resType);

		}

		return null;
	}

	@Override
	public SemType visit(AstFunDefn funDefn, Integer arg){
		SemType res = null;
		if(arg == 0){
			if(funDefn.pars != null)
				funDefn.pars.accept(this, 1);

			res = funDefn.type.accept(this, 1);
			SemAn.ofType.put(funDefn, res);

			if(funDefn.defns != null)
				funDefn.defns.accept(this, arg);
		}
		if(arg == 1){
			SemType typ = SemAn.ofType.get(funDefn);
			lastFnc.push(typ);
			if(funDefn.stmt != null)
				funDefn.stmt.accept(this, arg);
			lastFnc.pop();
		}
		
		return res;
	}

	@Override
	public SemType visit(AstFunDefn.AstValParDefn valParDefn, Integer arg){
		if (arg == 1) {
			SemType resType = valParDefn.type.accept(this, arg);

			if(resType instanceof SemVoidType){
				System.out.println("Void parameters not allowed: " + valParDefn.location());
				System.exit(1);
			}

			SemAn.ofType.put(valParDefn, resType);
		}

		return null;
	}

	@Override
	public SemType visit(AstFunDefn.AstRefParDefn valRefDefn, Integer arg){
		if (arg == 1) {
			SemType resType = valRefDefn.type.accept(this, arg);

			if(resType instanceof SemVoidType){
				System.out.println("Void parameters not allowed: " + valRefDefn.location());
				System.exit(1);
			}

			SemAn.ofType.put(valRefDefn, resType);
		}

		return null;
	}

	@Override
	public SemType visit(AstRecType.AstCmpDefn cmpDefn, Integer arg) {
		SemType typ = cmpDefn.type.accept(this, arg);
		SemAn.ofType.put(cmpDefn, typ);
		return typ;
	}

}
