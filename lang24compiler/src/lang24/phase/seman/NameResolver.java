package lang24.phase.seman;

import lang24.common.report.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.type.*;
import lang24.data.ast.visitor.*;

/**
 * Name resolver.
 * 
 * The name resolver connects each node of a abstract syntax tree where a name
 * is used with the node where it is defined. The only exceptions are struct and
 * union component names which are connected with their definitions by type
 * resolver. The results of the name resolver are stored in
 * {@link lang24.phase.seman.SemAn#definedAt}.
 */

public class NameResolver implements AstFullVisitor<Object, Integer> {

	/** Constructs a new name resolver. */
	public NameResolver() {
	}

	/** The symbol table. */
	private SymbTable symbTable = new SymbTable();

	@Override
	public Object visit(AstTypDefn typDefn, Integer arg){
		if(arg == 0){
			try{
				symbTable.ins(typDefn.name, typDefn);
			} catch (SymbTable.CannotInsNameException e){
				System.out.println("Type redefinition at " + typDefn.location());
				System.exit(1);
			}
		}
		
		typDefn.type.accept(this, arg);
		return null;
	}
	
	@Override
	public Object visit(AstVarDefn varDefn, Integer arg){
		if(arg == 0){
			try{
				System.out.println("vardef:" + varDefn.name+ " inserted on depth: " + symbTable.currDepth() + " on loop: " + arg);
				symbTable.ins(varDefn.name, varDefn);
				symbTable.newScope();
				symbTable.fnd(varDefn.name);
			} catch (SymbTable.CannotInsNameException e){
				System.out.println("Variable redefinition of " + varDefn.name + " at " + varDefn.location());
				System.exit(1);
			} catch (Exception e) {
				System.exit(2);
			}
		}
		
		varDefn.type.accept(this, arg);
		return null;
	}

	@Override
	public Object visit(AstNameType nameType, Integer arg){
		if(arg == 1){
			try{
				AstDefn def = symbTable.fnd(nameType.name);
				SemAn.definedAt.put(nameType, def);
			}catch(SymbTable.CannotFndNameException e){
				System.out.println("No definition of type " + nameType.name + " found at " + nameType.location());
				System.exit(1);
			}
		}
		return null;	
	}

	@Override
	public Object visit(AstNameExpr nameExpr, Integer arg){
		if(arg == 2){
			try{
				AstDefn def = symbTable.fnd(nameExpr.name);
				System.out.println(def.name + " loc:" + def.location() + " found on depth: " + symbTable.currDepth() + " on loop: " + arg);
				SemAn.definedAt.put(nameExpr, def);
			}catch(SymbTable.CannotFndNameException e){
				System.out.println("No declaration of variable " + nameExpr.name + " found at " + nameExpr.location());
				System.exit(1);
			}
		}
		return null;
	}

	@Override
	public Object visit(AstFunDefn funDefn, Integer arg){
		if(arg == 0){
			symbTable.newScope();
			if(funDefn.pars != null ) 
				funDefn.pars.accept(this, arg);
			symbTable.newScope();
			if(funDefn.defns != null)
				funDefn.defns.accept(this, arg);
			symbTable.oldScope();
			symbTable.oldScope();
		}
		if(arg == 1){
			funDefn.type.accept(this, arg);
			symbTable.newScope();
			symbTable.newScope();
			if(funDefn.stmt != null) 
				funDefn.stmt.accept(this, arg);
			if(funDefn.defns != null)
				funDefn.defns.accept(this, arg);
			symbTable.oldScope();
			symbTable.oldScope();
		}
		if(arg == 2){
			symbTable.newScope();
			if(funDefn.pars != null)
				funDefn.pars.accept(this, arg);
			symbTable.newScope();
			if(funDefn.defns != null)
				funDefn.defns.accept(this, arg);
			if(funDefn.stmt != null) 
				funDefn.stmt.accept(this, arg);
			symbTable.oldScope();
			symbTable.oldScope();
		}
		if(arg == 3){
			try{
				symbTable.ins(funDefn.name, funDefn);
			}catch(SymbTable.CannotInsNameException e){
				System.out.println("Redefinition of function " + funDefn.name + " detected at " + funDefn.location());
				System.exit(1);
			}

		}

		return null;
	}

	@Override
	public Object visit(AstFunDefn.AstValParDefn valParDefn, Integer arg){
		if( arg == 1 ){
			valParDefn.type.accept(this, arg);
		}

		if( arg == 0 ){
			try{
				System.out.println("valpar inserted on depth: "+ valParDefn.name + symbTable.currDepth() + " on loop: " + arg);
				symbTable.ins(valParDefn.name, valParDefn);
			}catch(SymbTable.CannotInsNameException e){
				System.out.println("Parameter redefinition of " + valParDefn.name + " detected at " + valParDefn.location());
				System.exit(1);
			}
		}
		

		return null;
	}
	
	@Override
	public Object visit(AstFunDefn.AstRefParDefn refParDefn, Integer arg){
		if( arg == 1 ){
			refParDefn.type.accept(this, arg);
		}

		if( arg == 2 ){
			try{
				symbTable.ins(refParDefn.name, refParDefn);
			}catch(SymbTable.CannotInsNameException e){
				System.out.println("Referenca parameter redefinition of " + refParDefn.name + " detected at " + refParDefn.location());
				System.exit(1);
			}	
		}
		

		return null;
	}
}
