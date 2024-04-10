package lang24.phase.seman;

import lang24.common.report.*;
import lang24.data.ast.tree.*;
import lang24.data.ast.tree.defn.*;
import lang24.data.ast.tree.expr.*;
import lang24.data.ast.tree.type.*;
import lang24.data.ast.tree.stmt.*;
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
	public Object visit(AstNodes<? extends AstNode> nodes, Integer arg){
		for(int i = 0; i < 2; i++){
			for(final AstNode node : nodes){
				if(node instanceof AstTypDefn)
					node.accept(this, i);
			}
			for(final AstNode node : nodes){
				if(node instanceof AstVarDefn)
					node.accept(this, i);
			}
			
		}

		for(int i = 0; i < 2; i++){
			for(final AstNode node : nodes){
				if(node instanceof AstFunDefn)
					node.accept(this, i);
			}
		}
		
		if(arg == null) return null;
		for(final AstNode node : nodes){
			if(node instanceof AstStmt)
				node.accept(this, arg);
		}

		for(final AstNode node : nodes){
			if(node instanceof AstFunDefn.AstValParDefn || node instanceof AstFunDefn.AstRefParDefn)
				node.accept(this, arg);	
		}

		for(final AstNode node : nodes){
			if(node instanceof AstExpr)
				node.accept(this, arg);	
			if(node instanceof AstRecType.AstCmpDefn)
				node.accept(this, arg);	
		}

		return null;
	}

	@Override
	public Object visit(AstTypDefn typDefn, Integer arg){
		if(arg == 0){
			try{
				symbTable.ins(typDefn.name, typDefn);
			}catch(Exception e){
				System.out.println("Duplicate definition at: " + typDefn.location());
				System.exit(1);
			}	
		}
		
		if(arg == 1)
			typDefn.type.accept(this, arg);
		return null;
	}
	
	@Override
	public Object visit(AstVarDefn varDefn, Integer arg){
		if(arg == 0){
			try{
				symbTable.ins(varDefn.name, varDefn);
			}catch(Exception e){
				System.out.println("Duplicate definition at: " + varDefn.location());
				System.exit(1);
			}	
		}

		if(arg == 1)
			varDefn.type.accept(this, arg);
		return null;
	}

	@Override
	public Object visit(AstNameType nameType, Integer arg){
		if(arg == 1){
			try{
				AstDefn def = symbTable.fnd(nameType.name);
				SemAn.definedAt.put(nameType, def);
			}catch(Exception e){
				System.out.println("No definition found for: " + nameType.location());
				System.exit(1);
			}
		}
		return null;	
	}

	@Override
	public Object visit(AstNameExpr nameExpr, Integer arg){
		if(arg == 1){
			try{
				AstDefn def = symbTable.fnd(nameExpr.name);
				SemAn.definedAt.put(nameExpr, def);
			}catch(Exception e){
				System.out.println("No definition found for name: " + nameExpr.location());
				System.exit(1);
			}
		}
		return null;
	}

	@Override
	public Object visit(AstFunDefn funDefn, Integer arg){
		if(arg == 0){
			try{
				symbTable.ins(funDefn.name, funDefn);
			}catch(Exception e){
				System.out.println("Duplicate definition at: " + funDefn.location());
				System.exit(1);
			}

			funDefn.type.accept(this, 1);
			if(funDefn.pars != null)
				funDefn.pars.accept(this, arg);
		}
		if(arg == 1){
			symbTable.newScope();
			if(funDefn.pars != null)
				funDefn.pars.accept(this, arg);
			symbTable.newScope();
			if(funDefn.defns != null)
				funDefn.defns.accept(this, arg);

			if(funDefn.stmt!= null)
				funDefn.stmt.accept(this, arg);
			symbTable.oldScope();
			symbTable.oldScope();
		}

		return null;
	}

	public Object visit(AstFunDefn.AstValParDefn valParDefn, Integer arg){
		if(arg == 0){
			valParDefn.type.accept(this, 1);
		}
		if(arg == 1){
			try{
				symbTable.ins(valParDefn.name, valParDefn);
			}catch(Exception e){
				System.out.println("Duplicate definition at: " + valParDefn.location());
				System.exit(1);
			}
		}

		return null;
	}
	
	@Override
	public Object visit(AstFunDefn.AstRefParDefn refParDefn, Integer arg){
		if(arg == 0){
			refParDefn.type.accept(this, 1);
		}
		if(arg == 1){
			try{
				symbTable.ins(refParDefn.name, refParDefn);
			}catch(Exception e){
				System.out.println("Duplicate definition at: " + refParDefn.location());
				System.exit(1);
			}
		}

		return null;
	}
	
	@Override
	public Object visit(AstCallExpr callExpr, Integer arg){
		if(arg == 1){
			if(callExpr.args != null)
				callExpr.args.accept(this, arg);
			try{
				AstDefn def = symbTable.fnd(callExpr.name);
				SemAn.definedAt.put(callExpr, def);
			}catch(Exception e){
				System.out.println("No definition found for function: " + callExpr.name + " " + callExpr.location() + " at depth " + symbTable.currDepth());
				System.exit(1);
			}	
		}

		return null;
	}
}
