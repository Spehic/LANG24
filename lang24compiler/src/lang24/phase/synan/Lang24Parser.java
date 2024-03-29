// Generated from Lang24Parser.g4 by ANTLR 4.13.1


	package lang24.phase.synan;
	
	import java.util.*;
	import lang24.common.report.*;
	import lang24.data.token.*;
	import lang24.data.ast.tree.*;
	import lang24.data.ast.tree.defn.*;
	import lang24.data.ast.tree.expr.*;
	import lang24.data.ast.tree.stmt.*;
	import lang24.data.ast.tree.type.*;
	import lang24.phase.lexan.*;


import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"doclint:missing", "all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class Lang24Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		AND=1, BOOL=2, CHAR=3, ELSE=4, FALSE=5, IF=6, INT=7, NIL=8, NONE=9, NOT=10, 
		OR=11, SIZEOF=12, THEN=13, RETURN=14, TRUE=15, VOID=16, WHILE=17, LBRACKET=18, 
		RBRACKET=19, LCURLY=20, RCURLY=21, LSQUARE=22, RSQUARE=23, DOT=24, COMMA=25, 
		COL=26, SEMICOL=27, EQUAL=28, NOTEQUAL=29, LESSTHAN=30, GREATERTHAN=31, 
		LESSEREQ=32, GREATERQ=33, MUL=34, DIV=35, MOD=36, PLUS=37, MINUS=38, POWER=39, 
		ASSIGN=40, IDENTIFIER=41, INT_CONST=42, CHAR_LIT=43, STRING_LIT=44, WHITESPACE=45, 
		TAB=46, COMMENT=47, ERR=48, GERR=49;
	public static final int
		RULE_source = 0, RULE_definitions = 1, RULE_type_definition = 2, RULE_variable_definition = 3, 
		RULE_function_definition = 4, RULE_parameters = 5, RULE_other_params = 6, 
		RULE_tick = 7, RULE_statement = 8, RULE_type = 9, RULE_components = 10, 
		RULE_other_components = 11, RULE_expression = 12, RULE_fnc_call_expr = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"source", "definitions", "type_definition", "variable_definition", "function_definition", 
			"parameters", "other_params", "tick", "statement", "type", "components", 
			"other_components", "expression", "fnc_call_expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'and'", "'bool'", "'char'", "'else'", "'false'", "'if'", "'int'", 
			"'nil'", "'none'", "'not'", "'or'", "'sizeof'", "'then'", "'return'", 
			"'true'", "'void'", "'while'", "'('", "')'", "'{'", "'}'", "'['", "']'", 
			"'.'", "','", "':'", "';'", "'=='", "'!='", "'<'", "'>'", "'<='", "'>='", 
			"'*'", "'/'", "'%'", "'+'", "'-'", "'^'", "'='", null, null, null, null, 
			null, "'\\t'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "AND", "BOOL", "CHAR", "ELSE", "FALSE", "IF", "INT", "NIL", "NONE", 
			"NOT", "OR", "SIZEOF", "THEN", "RETURN", "TRUE", "VOID", "WHILE", "LBRACKET", 
			"RBRACKET", "LCURLY", "RCURLY", "LSQUARE", "RSQUARE", "DOT", "COMMA", 
			"COL", "SEMICOL", "EQUAL", "NOTEQUAL", "LESSTHAN", "GREATERTHAN", "LESSEREQ", 
			"GREATERQ", "MUL", "DIV", "MOD", "PLUS", "MINUS", "POWER", "ASSIGN", 
			"IDENTIFIER", "INT_CONST", "CHAR_LIT", "STRING_LIT", "WHITESPACE", "TAB", 
			"COMMENT", "ERR", "GERR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Lang24Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }



		private Location loc(Token tok) { return new Location((LocLogToken)tok); }
		private Location loc(Token     tok1, Token     tok2) { return new Location((LocLogToken)tok1, (LocLogToken)tok2); }
		private Location loc(Token     tok1, Locatable loc2) { return new Location((LocLogToken)tok1, loc2); }
		private Location loc(Locatable loc1, Token     tok2) { return new Location(loc1, (LocLogToken)tok2); }
		private Location loc(Locatable loc1, Locatable loc2) { return new Location(loc1, loc2); }
		
		private AstBinExpr.Oper getBinOp(String str){
			return switch(str){
				case "and" -> AstBinExpr.Oper.AND;
				case "or" -> AstBinExpr.Oper.OR;
				case "==" -> AstBinExpr.Oper.EQU;
				case "!=" -> AstBinExpr.Oper.NEQ;
				case "<" -> AstBinExpr.Oper.LTH;
				case ">" -> AstBinExpr.Oper.GTH;
				case ">=" -> AstBinExpr.Oper.GEQ;
				case "<=" -> AstBinExpr.Oper.LEQ;
				case "+" -> AstBinExpr.Oper.ADD;
				case "-" -> AstBinExpr.Oper.SUB;
				case "*" -> AstBinExpr.Oper.MUL;
				case "/" -> AstBinExpr.Oper.DIV;
				case "%" -> AstBinExpr.Oper.MOD;
				default -> null;
			};
		}

	public Lang24Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SourceContext extends ParserRuleContext {
		public AstNodes ast;
		public DefinitionsContext d;
		public DefinitionsContext definitions() {
			return getRuleContext(DefinitionsContext.class,0);
		}
		public SourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_source; }
	}

	public final SourceContext source() throws RecognitionException {
		SourceContext _localctx = new SourceContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_source);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			((SourceContext)_localctx).d = definitions();
			((SourceContext)_localctx).ast =  ((SourceContext)_localctx).d.ast;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefinitionsContext extends ParserRuleContext {
		public AstNodes<AstDefn> ast;
		public Type_definitionContext t;
		public Variable_definitionContext v;
		public Function_definitionContext f;
		public List<Type_definitionContext> type_definition() {
			return getRuleContexts(Type_definitionContext.class);
		}
		public Type_definitionContext type_definition(int i) {
			return getRuleContext(Type_definitionContext.class,i);
		}
		public List<Variable_definitionContext> variable_definition() {
			return getRuleContexts(Variable_definitionContext.class);
		}
		public Variable_definitionContext variable_definition(int i) {
			return getRuleContext(Variable_definitionContext.class,i);
		}
		public List<Function_definitionContext> function_definition() {
			return getRuleContexts(Function_definitionContext.class);
		}
		public Function_definitionContext function_definition(int i) {
			return getRuleContext(Function_definitionContext.class,i);
		}
		public DefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definitions; }
	}

	public final DefinitionsContext definitions() throws RecognitionException {
		DefinitionsContext _localctx = new DefinitionsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_definitions);

			ArrayList<AstDefn> arr = new ArrayList<AstDefn>();

		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(40);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(31);
					((DefinitionsContext)_localctx).t = type_definition();
					arr.add(((DefinitionsContext)_localctx).t.ast);
					}
					break;
				case 2:
					{
					setState(34);
					((DefinitionsContext)_localctx).v = variable_definition();
					arr.add(((DefinitionsContext)_localctx).v.ast);
					}
					break;
				case 3:
					{
					setState(37);
					((DefinitionsContext)_localctx).f = function_definition();
					arr.add(((DefinitionsContext)_localctx).f.ast);
					}
					break;
				}
				}
				setState(42); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			}
			_ctx.stop = _input.LT(-1);

				((DefinitionsContext)_localctx).ast =  new AstNodes<AstDefn>(arr);

		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Type_definitionContext extends ParserRuleContext {
		public AstTypDefn ast;
		public Token i;
		public TypeContext t;
		public TerminalNode ASSIGN() { return getToken(Lang24Parser.ASSIGN, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public Type_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_definition; }
	}

	public final Type_definitionContext type_definition() throws RecognitionException {
		Type_definitionContext _localctx = new Type_definitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_type_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			((Type_definitionContext)_localctx).i = match(IDENTIFIER);
			setState(45);
			match(ASSIGN);
			setState(46);
			((Type_definitionContext)_localctx).t = type();
			 ((Type_definitionContext)_localctx).ast =  new AstTypDefn( loc(((Type_definitionContext)_localctx).i, ((Type_definitionContext)_localctx).t.l), (((Type_definitionContext)_localctx).i!=null?((Type_definitionContext)_localctx).i.getText():null), ((Type_definitionContext)_localctx).t.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Variable_definitionContext extends ParserRuleContext {
		public AstVarDefn ast;
		public Token i;
		public TypeContext t;
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public Variable_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable_definition; }
	}

	public final Variable_definitionContext variable_definition() throws RecognitionException {
		Variable_definitionContext _localctx = new Variable_definitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_variable_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			((Variable_definitionContext)_localctx).i = match(IDENTIFIER);
			setState(50);
			match(COL);
			setState(51);
			((Variable_definitionContext)_localctx).t = type();
			 ((Variable_definitionContext)_localctx).ast =  new AstVarDefn( loc(((Variable_definitionContext)_localctx).i, ((Variable_definitionContext)_localctx).t.l) , (((Variable_definitionContext)_localctx).i!=null?((Variable_definitionContext)_localctx).i.getText():null), ((Variable_definitionContext)_localctx).t.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Function_definitionContext extends ParserRuleContext {
		public AstFunDefn ast;
		public Location l;
		public Token i;
		public ParametersContext p;
		public TypeContext t;
		public StatementContext s;
		public DefinitionsContext d;
		public Token endd;
		public TerminalNode LBRACKET() { return getToken(Lang24Parser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(Lang24Parser.RBRACKET, 0); }
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(Lang24Parser.ASSIGN, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode LCURLY() { return getToken(Lang24Parser.LCURLY, 0); }
		public DefinitionsContext definitions() {
			return getRuleContext(DefinitionsContext.class,0);
		}
		public TerminalNode RCURLY() { return getToken(Lang24Parser.RCURLY, 0); }
		public Function_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_definition; }
	}

	public final Function_definitionContext function_definition() throws RecognitionException {
		Function_definitionContext _localctx = new Function_definitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_function_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			((Function_definitionContext)_localctx).i = match(IDENTIFIER);
			setState(55);
			match(LBRACKET);
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==POWER || _la==IDENTIFIER) {
				{
				setState(56);
				((Function_definitionContext)_localctx).p = parameters();
				}
			}

			setState(59);
			match(RBRACKET);
			setState(60);
			match(COL);
			setState(61);
			((Function_definitionContext)_localctx).t = type();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(62);
				match(ASSIGN);
				setState(63);
				((Function_definitionContext)_localctx).s = statement();
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LCURLY) {
					{
					setState(64);
					match(LCURLY);
					setState(65);
					((Function_definitionContext)_localctx).d = definitions();
					setState(66);
					((Function_definitionContext)_localctx).endd = match(RCURLY);
					}
				}

				}
			}

			 	
						if(((Function_definitionContext)_localctx).d != null)
							((Function_definitionContext)_localctx).l = loc(((Function_definitionContext)_localctx).i, ((Function_definitionContext)_localctx).endd);
						else if(((Function_definitionContext)_localctx).s != null)
							((Function_definitionContext)_localctx).l = loc(((Function_definitionContext)_localctx).i, ((Function_definitionContext)_localctx).s.l);
						else 
							((Function_definitionContext)_localctx).l =  loc(((Function_definitionContext)_localctx).i, ((Function_definitionContext)_localctx).t.l);
						AstNodes<AstFunDefn.AstParDefn> params = null;
						AstNodes<AstDefn> defns = null;
						AstStmt stmts = null;
						if(((Function_definitionContext)_localctx).p != null) params = ((Function_definitionContext)_localctx).p.pars;
						if(((Function_definitionContext)_localctx).d != null) defns = ((Function_definitionContext)_localctx).d.ast;
						if(((Function_definitionContext)_localctx).s != null) stmts = ((Function_definitionContext)_localctx).s.ast;
						((Function_definitionContext)_localctx).ast =  new AstFunDefn(_localctx.l,(((Function_definitionContext)_localctx).i!=null?((Function_definitionContext)_localctx).i.getText():null),params,((Function_definitionContext)_localctx).t.ast,stmts,defns);	
					
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParametersContext extends ParserRuleContext {
		public AstNodes<AstFunDefn.AstParDefn> pars;
		public TickContext ta;
		public Token ia;
		public TypeContext t;
		public Other_paramsContext o;
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TickContext tick() {
			return getRuleContext(TickContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<Other_paramsContext> other_params() {
			return getRuleContexts(Other_paramsContext.class);
		}
		public Other_paramsContext other_params(int i) {
			return getRuleContext(Other_paramsContext.class,i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parameters);

			ArrayList<AstFunDefn.AstParDefn> arr = new ArrayList<>();
			Location l;
			boolean flag;

		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			flag = true;
			setState(75);
			((ParametersContext)_localctx).ta = tick();
			setState(76);
			((ParametersContext)_localctx).ia = match(IDENTIFIER);
			setState(77);
			match(COL);
			setState(78);
			((ParametersContext)_localctx).t = type();
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(79);
				((ParametersContext)_localctx).o = other_params();
				  if(((ParametersContext)_localctx).o != null) arr.add(((ParametersContext)_localctx).o.othrs);
				}
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}

					flag = false;
					if(((ParametersContext)_localctx).ta.b){
						l = loc(((ParametersContext)_localctx).ta.l, ((ParametersContext)_localctx).t.l);		
						arr.add(new AstFunDefn.AstRefParDefn(l, (((ParametersContext)_localctx).ia!=null?((ParametersContext)_localctx).ia.getText():null), ((ParametersContext)_localctx).t.ast));
					} else{
						l = loc(((ParametersContext)_localctx).ia, ((ParametersContext)_localctx).t.l);		
						arr.add(new AstFunDefn.AstValParDefn(l, (((ParametersContext)_localctx).ia!=null?((ParametersContext)_localctx).ia.getText():null), ((ParametersContext)_localctx).t.ast));
					}
					
			}
			_ctx.stop = _input.LT(-1);

				if(flag) ((ParametersContext)_localctx).pars =  null;
				else ((ParametersContext)_localctx).pars =  new AstNodes(arr);

		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Other_paramsContext extends ParserRuleContext {
		public AstFunDefn.AstParDefn othrs;
		public TickContext tb;
		public Token i;
		public TypeContext t;
		public TerminalNode COMMA() { return getToken(Lang24Parser.COMMA, 0); }
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TickContext tick() {
			return getRuleContext(TickContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public Other_paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_other_params; }
	}

	public final Other_paramsContext other_params() throws RecognitionException {
		Other_paramsContext _localctx = new Other_paramsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_other_params);

			Location l;

		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(COMMA);
			setState(90);
			((Other_paramsContext)_localctx).tb = tick();
			setState(91);
			((Other_paramsContext)_localctx).i = match(IDENTIFIER);
			setState(92);
			match(COL);
			setState(93);
			((Other_paramsContext)_localctx).t = type();

						if(((Other_paramsContext)_localctx).tb.b){
							l = loc(((Other_paramsContext)_localctx).tb.l, ((Other_paramsContext)_localctx).t.l);		
							((Other_paramsContext)_localctx).othrs =  new AstFunDefn.AstRefParDefn(l, (((Other_paramsContext)_localctx).i!=null?((Other_paramsContext)_localctx).i.getText():null), ((Other_paramsContext)_localctx).t.ast);
						} else{
							l = loc(((Other_paramsContext)_localctx).i, ((Other_paramsContext)_localctx).t.l);		
							((Other_paramsContext)_localctx).othrs =  new AstFunDefn.AstValParDefn(l, (((Other_paramsContext)_localctx).i!=null?((Other_paramsContext)_localctx).i.getText():null), ((Other_paramsContext)_localctx).t.ast);
						}
					
			}
			_ctx.stop = _input.LT(-1);


		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TickContext extends ParserRuleContext {
		public boolean b;
		public Location l;
		public Token a;
		public TerminalNode POWER() { return getToken(Lang24Parser.POWER, 0); }
		public TickContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tick; }
	}

	public final TickContext tick() throws RecognitionException {
		TickContext _localctx = new TickContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_tick);
		try {
			setState(99);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				((TickContext)_localctx).b =  false;
				}
				break;
			case POWER:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				((TickContext)_localctx).a = match(POWER);
				((TickContext)_localctx).b =  true; ((TickContext)_localctx).l =  loc(((TickContext)_localctx).a);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public AstStmt ast;
		public Location l;
		public ExpressionContext e;
		public Token r;
		public ExpressionContext ls;
		public Token w;
		public StatementContext s;
		public Token k;
		public Token ll;
		public Token i;
		public StatementContext ss;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SEMICOL() { return getToken(Lang24Parser.SEMICOL, 0); }
		public TerminalNode ASSIGN() { return getToken(Lang24Parser.ASSIGN, 0); }
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TerminalNode WHILE() { return getToken(Lang24Parser.WHILE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode RETURN() { return getToken(Lang24Parser.RETURN, 0); }
		public TerminalNode LCURLY() { return getToken(Lang24Parser.LCURLY, 0); }
		public TerminalNode RCURLY() { return getToken(Lang24Parser.RCURLY, 0); }
		public TerminalNode THEN() { return getToken(Lang24Parser.THEN, 0); }
		public TerminalNode IF() { return getToken(Lang24Parser.IF, 0); }
		public TerminalNode ELSE() { return getToken(Lang24Parser.ELSE, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_statement);

			ArrayList<AstStmt> arr = new ArrayList<AstStmt>();

		int _la;
		try {
			setState(143);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(101);
				((StatementContext)_localctx).e = expression(0);
				setState(102);
				((StatementContext)_localctx).r = match(SEMICOL);
				((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).e.l, ((StatementContext)_localctx).r); ((StatementContext)_localctx).ast =  new AstExprStmt(_localctx.l, ((StatementContext)_localctx).e.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(105);
				((StatementContext)_localctx).ls = expression(0);
				setState(106);
				match(ASSIGN);
				setState(107);
				((StatementContext)_localctx).e = expression(0);
				setState(108);
				((StatementContext)_localctx).r = match(SEMICOL);
				((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).ls.l, ((StatementContext)_localctx).r); ((StatementContext)_localctx).ast =  new AstAssignStmt(_localctx.l, ((StatementContext)_localctx).ls.ast, ((StatementContext)_localctx).e.ast);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(111);
				((StatementContext)_localctx).w = match(WHILE);
				setState(112);
				((StatementContext)_localctx).e = expression(0);
				setState(113);
				match(COL);
				setState(114);
				((StatementContext)_localctx).s = statement();
				 ((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).w, ((StatementContext)_localctx).s.l); ((StatementContext)_localctx).ast =  new AstWhileStmt(_localctx.l, ((StatementContext)_localctx).e.ast, ((StatementContext)_localctx).s.ast);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(117);
				((StatementContext)_localctx).r = match(RETURN);
				setState(118);
				((StatementContext)_localctx).e = expression(0);
				setState(119);
				((StatementContext)_localctx).k = match(SEMICOL);
				 ((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).r, ((StatementContext)_localctx).k); ((StatementContext)_localctx).ast =  new AstReturnStmt(_localctx.l, ((StatementContext)_localctx).e.ast);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(122);
				((StatementContext)_localctx).ll = match(LCURLY);
				setState(126); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(123);
					((StatementContext)_localctx).s = statement();
					arr.add(((StatementContext)_localctx).s.ast);
					}
					}
					setState(128); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 33948496746336L) != 0) );
				setState(130);
				((StatementContext)_localctx).r = match(RCURLY);
				((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).ll, ((StatementContext)_localctx).r);  ((StatementContext)_localctx).ast =  new AstBlockStmt(_localctx.l, arr);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(133);
				((StatementContext)_localctx).i = match(IF);
				setState(134);
				((StatementContext)_localctx).e = expression(0);
				setState(135);
				match(THEN);
				setState(136);
				((StatementContext)_localctx).s = statement();
				setState(139);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
				case 1:
					{
					setState(137);
					match(ELSE);
					setState(138);
					((StatementContext)_localctx).ss = statement();
					}
					break;
				}

							((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).i, ((StatementContext)_localctx).s.l);
							if(((StatementContext)_localctx).ss != null){
								((StatementContext)_localctx).l =  loc(((StatementContext)_localctx).i, ((StatementContext)_localctx).ss.l); 
								((StatementContext)_localctx).ast =  new AstIfStmt(_localctx.l, ((StatementContext)_localctx).e.ast, ((StatementContext)_localctx).s.ast, ((StatementContext)_localctx).ss.ast);
							}
							else{
								((StatementContext)_localctx).ast =  new AstIfStmt(_localctx.l, ((StatementContext)_localctx).e.ast, ((StatementContext)_localctx).s.ast, null);
							}	
						
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public AstType ast;
		public Location l;
		public Token v;
		public Token b;
		public Token c;
		public Token i;
		public Token s;
		public TypeContext t;
		public Token a;
		public Token lb;
		public ComponentsContext cm;
		public Token r;
		public TerminalNode VOID() { return getToken(Lang24Parser.VOID, 0); }
		public TerminalNode BOOL() { return getToken(Lang24Parser.BOOL, 0); }
		public TerminalNode CHAR() { return getToken(Lang24Parser.CHAR, 0); }
		public TerminalNode INT() { return getToken(Lang24Parser.INT, 0); }
		public TerminalNode RSQUARE() { return getToken(Lang24Parser.RSQUARE, 0); }
		public TerminalNode LSQUARE() { return getToken(Lang24Parser.LSQUARE, 0); }
		public TerminalNode INT_CONST() { return getToken(Lang24Parser.INT_CONST, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode POWER() { return getToken(Lang24Parser.POWER, 0); }
		public TerminalNode LBRACKET() { return getToken(Lang24Parser.LBRACKET, 0); }
		public ComponentsContext components() {
			return getRuleContext(ComponentsContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(Lang24Parser.RBRACKET, 0); }
		public TerminalNode LCURLY() { return getToken(Lang24Parser.LCURLY, 0); }
		public TerminalNode RCURLY() { return getToken(Lang24Parser.RCURLY, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_type);
		try {
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(145);
				((TypeContext)_localctx).v = match(VOID);
				((TypeContext)_localctx).ast =  new AstAtomType(loc(((TypeContext)_localctx).v), AstAtomType.Type.VOID); ((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).v);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(147);
				((TypeContext)_localctx).b = match(BOOL);
				((TypeContext)_localctx).ast =  new AstAtomType(loc(((TypeContext)_localctx).b), AstAtomType.Type.BOOL); ((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).b);
				}
				break;
			case CHAR:
				enterOuterAlt(_localctx, 3);
				{
				setState(149);
				((TypeContext)_localctx).c = match(CHAR);
				((TypeContext)_localctx).ast =  new AstAtomType(loc(((TypeContext)_localctx).c), AstAtomType.Type.CHAR); ((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).c);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 4);
				{
				setState(151);
				((TypeContext)_localctx).i = match(INT);
				((TypeContext)_localctx).ast =  new AstAtomType(loc(((TypeContext)_localctx).i), AstAtomType.Type.INT); ((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).i);
				}
				break;
			case LSQUARE:
				enterOuterAlt(_localctx, 5);
				{
				setState(153);
				((TypeContext)_localctx).s = match(LSQUARE);
				setState(154);
				((TypeContext)_localctx).i = match(INT_CONST);
				setState(155);
				match(RSQUARE);
				setState(156);
				((TypeContext)_localctx).t = type();
				((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).s, ((TypeContext)_localctx).t.l); ((TypeContext)_localctx).ast =  new AstArrType(_localctx.l, ((TypeContext)_localctx).t.ast, new AstAtomExpr(loc(((TypeContext)_localctx).i), AstAtomExpr.Type.INT, (((TypeContext)_localctx).i!=null?((TypeContext)_localctx).i.getText():null)));
				}
				break;
			case POWER:
				enterOuterAlt(_localctx, 6);
				{
				setState(159);
				((TypeContext)_localctx).a = match(POWER);
				setState(160);
				((TypeContext)_localctx).t = type();
				((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).a, ((TypeContext)_localctx).t.l); ((TypeContext)_localctx).ast =  new AstPtrType(_localctx.l, ((TypeContext)_localctx).t.ast);
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 7);
				{
				setState(163);
				((TypeContext)_localctx).lb = match(LBRACKET);
				setState(164);
				((TypeContext)_localctx).cm = components();
				setState(165);
				((TypeContext)_localctx).r = match(RBRACKET);
				((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).lb, ((TypeContext)_localctx).r); ((TypeContext)_localctx).ast =  new AstStrType(_localctx.l, ((TypeContext)_localctx).cm.ast);
				}
				break;
			case LCURLY:
				enterOuterAlt(_localctx, 8);
				{
				setState(168);
				((TypeContext)_localctx).lb = match(LCURLY);
				setState(169);
				((TypeContext)_localctx).cm = components();
				setState(170);
				((TypeContext)_localctx).r = match(RCURLY);
				((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).lb, ((TypeContext)_localctx).r); ((TypeContext)_localctx).ast =  new AstUniType(_localctx.l, ((TypeContext)_localctx).cm.ast);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 9);
				{
				setState(173);
				((TypeContext)_localctx).i = match(IDENTIFIER);
				((TypeContext)_localctx).l =  loc(((TypeContext)_localctx).i); ((TypeContext)_localctx).ast =  new AstNameType(_localctx.l, (((TypeContext)_localctx).i!=null?((TypeContext)_localctx).i.getText():null));
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComponentsContext extends ParserRuleContext {
		public AstNodes<AstRecType.AstCmpDefn> ast;
		public Token i;
		public TypeContext t;
		public Other_componentsContext o;
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<Other_componentsContext> other_components() {
			return getRuleContexts(Other_componentsContext.class);
		}
		public Other_componentsContext other_components(int i) {
			return getRuleContext(Other_componentsContext.class,i);
		}
		public ComponentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_components; }
	}

	public final ComponentsContext components() throws RecognitionException {
		ComponentsContext _localctx = new ComponentsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_components);

			ArrayList<AstRecType.AstCmpDefn> arr = new ArrayList<>();
			Location l;

		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			((ComponentsContext)_localctx).i = match(IDENTIFIER);
			setState(178);
			match(COL);
			setState(179);
			((ComponentsContext)_localctx).t = type();
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(180);
				((ComponentsContext)_localctx).o = other_components();
				 if(((ComponentsContext)_localctx).o != null) arr.add(((ComponentsContext)_localctx).o.ast);
				}
				}
				setState(187);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
				
					l = loc(((ComponentsContext)_localctx).i, ((ComponentsContext)_localctx).t.l); 
					arr.add(new AstRecType.AstCmpDefn(l, (((ComponentsContext)_localctx).i!=null?((ComponentsContext)_localctx).i.getText():null), ((ComponentsContext)_localctx).t.ast)); 
				
			}
			_ctx.stop = _input.LT(-1);

				((ComponentsContext)_localctx).ast =  new AstNodes<AstRecType.AstCmpDefn>(arr);

		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Other_componentsContext extends ParserRuleContext {
		public AstRecType.AstCmpDefn ast;
		public Token ll;
		public Token i;
		public TypeContext t;
		public TerminalNode COL() { return getToken(Lang24Parser.COL, 0); }
		public TerminalNode COMMA() { return getToken(Lang24Parser.COMMA, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public Other_componentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_other_components; }
	}

	public final Other_componentsContext other_components() throws RecognitionException {
		Other_componentsContext _localctx = new Other_componentsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_other_components);

			Location l;

		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			((Other_componentsContext)_localctx).ll = match(COMMA);
			setState(191);
			((Other_componentsContext)_localctx).i = match(IDENTIFIER);
			setState(192);
			match(COL);
			setState(193);
			((Other_componentsContext)_localctx).t = type();
			l = loc(((Other_componentsContext)_localctx).ll, ((Other_componentsContext)_localctx).t.l); ((Other_componentsContext)_localctx).ast =  new AstRecType.AstCmpDefn(l, (((Other_componentsContext)_localctx).i!=null?((Other_componentsContext)_localctx).i.getText():null), ((Other_componentsContext)_localctx).t.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public AstExpr ast;
		public Location l;
		public ExpressionContext e;
		public ExpressionContext as;
		public Token n;
		public Token t;
		public Token f;
		public Token i;
		public Token c;
		public Token s;
		public Token ae;
		public Token aa;
		public Token ab;
		public Token ac;
		public Token ad;
		public TypeContext tb;
		public TypeContext ta;
		public Token r;
		public Token ls;
		public Token ll;
		public Fnc_call_exprContext fnc;
		public Token rr;
		public Token op;
		public ExpressionContext ba;
		public ExpressionContext eb;
		public Token b;
		public TerminalNode NONE() { return getToken(Lang24Parser.NONE, 0); }
		public TerminalNode TRUE() { return getToken(Lang24Parser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(Lang24Parser.FALSE, 0); }
		public TerminalNode INT_CONST() { return getToken(Lang24Parser.INT_CONST, 0); }
		public TerminalNode CHAR_LIT() { return getToken(Lang24Parser.CHAR_LIT, 0); }
		public TerminalNode STRING_LIT() { return getToken(Lang24Parser.STRING_LIT, 0); }
		public TerminalNode NIL() { return getToken(Lang24Parser.NIL, 0); }
		public TerminalNode NOT() { return getToken(Lang24Parser.NOT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(Lang24Parser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(Lang24Parser.MINUS, 0); }
		public TerminalNode POWER() { return getToken(Lang24Parser.POWER, 0); }
		public TerminalNode GREATERTHAN() { return getToken(Lang24Parser.GREATERTHAN, 0); }
		public TerminalNode LESSTHAN() { return getToken(Lang24Parser.LESSTHAN, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode LBRACKET() { return getToken(Lang24Parser.LBRACKET, 0); }
		public TerminalNode SIZEOF() { return getToken(Lang24Parser.SIZEOF, 0); }
		public TerminalNode RBRACKET() { return getToken(Lang24Parser.RBRACKET, 0); }
		public TerminalNode IDENTIFIER() { return getToken(Lang24Parser.IDENTIFIER, 0); }
		public Fnc_call_exprContext fnc_call_expr() {
			return getRuleContext(Fnc_call_exprContext.class,0);
		}
		public TerminalNode MUL() { return getToken(Lang24Parser.MUL, 0); }
		public TerminalNode DIV() { return getToken(Lang24Parser.DIV, 0); }
		public TerminalNode MOD() { return getToken(Lang24Parser.MOD, 0); }
		public TerminalNode EQUAL() { return getToken(Lang24Parser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(Lang24Parser.NOTEQUAL, 0); }
		public TerminalNode LESSEREQ() { return getToken(Lang24Parser.LESSEREQ, 0); }
		public TerminalNode GREATERQ() { return getToken(Lang24Parser.GREATERQ, 0); }
		public TerminalNode AND() { return getToken(Lang24Parser.AND, 0); }
		public TerminalNode OR() { return getToken(Lang24Parser.OR, 0); }
		public TerminalNode LSQUARE() { return getToken(Lang24Parser.LSQUARE, 0); }
		public TerminalNode RSQUARE() { return getToken(Lang24Parser.RSQUARE, 0); }
		public TerminalNode DOT() { return getToken(Lang24Parser.DOT, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NONE:
				{
				setState(197);
				((ExpressionContext)_localctx).n = match(NONE);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).n), AstAtomExpr.Type.VOID, (((ExpressionContext)_localctx).n!=null?((ExpressionContext)_localctx).n.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).n);
				}
				break;
			case TRUE:
				{
				setState(199);
				((ExpressionContext)_localctx).t = match(TRUE);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).t), AstAtomExpr.Type.BOOL, (((ExpressionContext)_localctx).t!=null?((ExpressionContext)_localctx).t.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).t);
				}
				break;
			case FALSE:
				{
				setState(201);
				((ExpressionContext)_localctx).f = match(FALSE);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).f), AstAtomExpr.Type.BOOL, (((ExpressionContext)_localctx).f!=null?((ExpressionContext)_localctx).f.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).f);
				}
				break;
			case INT_CONST:
				{
				setState(203);
				((ExpressionContext)_localctx).i = match(INT_CONST);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).i), AstAtomExpr.Type.INT, (((ExpressionContext)_localctx).i!=null?((ExpressionContext)_localctx).i.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).i);
				}
				break;
			case CHAR_LIT:
				{
				setState(205);
				((ExpressionContext)_localctx).c = match(CHAR_LIT);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).c), AstAtomExpr.Type.CHAR, (((ExpressionContext)_localctx).c!=null?((ExpressionContext)_localctx).c.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).c);
				}
				break;
			case STRING_LIT:
				{
				setState(207);
				((ExpressionContext)_localctx).s = match(STRING_LIT);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).s), AstAtomExpr.Type.STR, (((ExpressionContext)_localctx).s!=null?((ExpressionContext)_localctx).s.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).s);
				}
				break;
			case NIL:
				{
				setState(209);
				((ExpressionContext)_localctx).n = match(NIL);
				((ExpressionContext)_localctx).ast =  new AstAtomExpr(loc(((ExpressionContext)_localctx).n), AstAtomExpr.Type.PTR, (((ExpressionContext)_localctx).n!=null?((ExpressionContext)_localctx).n.getText():null)); ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).n);
				}
				break;
			case NOT:
				{
				setState(211);
				((ExpressionContext)_localctx).ae = match(NOT);
				setState(212);
				((ExpressionContext)_localctx).e = expression(13);
				 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).ae, ((ExpressionContext)_localctx).e.l); ((ExpressionContext)_localctx).ast =  new AstPfxExpr(_localctx.l, AstPfxExpr.Oper.NOT, ((ExpressionContext)_localctx).e.ast);
				}
				break;
			case PLUS:
				{
				setState(215);
				((ExpressionContext)_localctx).aa = match(PLUS);
				setState(216);
				((ExpressionContext)_localctx).e = expression(12);
				 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).aa, ((ExpressionContext)_localctx).e.l); ((ExpressionContext)_localctx).ast =  new AstPfxExpr(_localctx.l, AstPfxExpr.Oper.ADD, ((ExpressionContext)_localctx).e.ast);
				}
				break;
			case MINUS:
				{
				setState(219);
				((ExpressionContext)_localctx).ab = match(MINUS);
				setState(220);
				((ExpressionContext)_localctx).e = expression(11);
				 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).ab, ((ExpressionContext)_localctx).e.l); ((ExpressionContext)_localctx).ast =  new AstPfxExpr(_localctx.l, AstPfxExpr.Oper.SUB, ((ExpressionContext)_localctx).e.ast);
				}
				break;
			case POWER:
				{
				setState(223);
				((ExpressionContext)_localctx).ac = match(POWER);
				setState(224);
				((ExpressionContext)_localctx).e = expression(10);
				 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).ac, ((ExpressionContext)_localctx).e.l); ((ExpressionContext)_localctx).ast =  new AstPfxExpr(_localctx.l, AstPfxExpr.Oper.PTR, ((ExpressionContext)_localctx).e.ast);
				}
				break;
			case LESSTHAN:
				{
				setState(227);
				((ExpressionContext)_localctx).ad = match(LESSTHAN);
				setState(228);
				((ExpressionContext)_localctx).tb = type();
				setState(229);
				match(GREATERTHAN);
				setState(230);
				((ExpressionContext)_localctx).e = expression(9);
				 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).ad, ((ExpressionContext)_localctx).e.l); ((ExpressionContext)_localctx).ast =  new AstCastExpr(_localctx.l, ((ExpressionContext)_localctx).tb.ast, ((ExpressionContext)_localctx).e.ast); 
				}
				break;
			case SIZEOF:
				{
				setState(233);
				((ExpressionContext)_localctx).s = match(SIZEOF);
				setState(234);
				match(LBRACKET);
				setState(235);
				((ExpressionContext)_localctx).ta = type();
				setState(236);
				((ExpressionContext)_localctx).r = match(RBRACKET);
				((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).s, ((ExpressionContext)_localctx).r); ((ExpressionContext)_localctx).ast =  new AstSizeofExpr(_localctx.l, ((ExpressionContext)_localctx).ta.ast);
				}
				break;
			case LBRACKET:
				{
				setState(239);
				((ExpressionContext)_localctx).ls = match(LBRACKET);
				setState(240);
				((ExpressionContext)_localctx).e = expression(0);
				setState(241);
				((ExpressionContext)_localctx).r = match(RBRACKET);
				((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).ls, ((ExpressionContext)_localctx).r); ((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).e.ast;
				}
				break;
			case IDENTIFIER:
				{
				setState(244);
				((ExpressionContext)_localctx).i = match(IDENTIFIER);
				setState(250);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(245);
					((ExpressionContext)_localctx).ll = match(LBRACKET);
					setState(247);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 33948495550240L) != 0)) {
						{
						setState(246);
						((ExpressionContext)_localctx).fnc = fnc_call_expr();
						}
					}

					setState(249);
					((ExpressionContext)_localctx).rr = match(RBRACKET);
					}
					break;
				}

							((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).i);
							if(_localctx.ll == null) ((ExpressionContext)_localctx).ast =  new AstNameExpr(loc(((ExpressionContext)_localctx).i), (((ExpressionContext)_localctx).i!=null?((ExpressionContext)_localctx).i.getText():null));
							else{
								AstNodes<AstExpr> tmp = null;
								if(((ExpressionContext)_localctx).fnc != null) tmp = ((ExpressionContext)_localctx).fnc.ast;
								((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).i, ((ExpressionContext)_localctx).rr);
							 	((ExpressionContext)_localctx).ast =  new AstCallExpr(_localctx.l, (((ExpressionContext)_localctx).i!=null?((ExpressionContext)_localctx).i.getText():null), tmp);
							}
						
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(295);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(293);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.as = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(255);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(256);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 120259084288L) != 0)) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(257);
						((ExpressionContext)_localctx).ba = expression(9);
						 ((ExpressionContext)_localctx).l = loc(((ExpressionContext)_localctx).as.l, ((ExpressionContext)_localctx).ba.l); ((ExpressionContext)_localctx).ast = new AstBinExpr(_localctx.l, getBinOp((((ExpressionContext)_localctx).op!=null?((ExpressionContext)_localctx).op.getText():null)), ((ExpressionContext)_localctx).as.ast, ((ExpressionContext)_localctx).ba.ast);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.as = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(260);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(261);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(262);
						((ExpressionContext)_localctx).ba = expression(8);
						 ((ExpressionContext)_localctx).l = loc(((ExpressionContext)_localctx).as.l, ((ExpressionContext)_localctx).ba.l); ((ExpressionContext)_localctx).ast =  new AstBinExpr(_localctx.l, getBinOp((((ExpressionContext)_localctx).op!=null?((ExpressionContext)_localctx).op.getText():null)), ((ExpressionContext)_localctx).as.ast, ((ExpressionContext)_localctx).ba.ast);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.as = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(265);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(266);
						((ExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16911433728L) != 0)) ) {
							((ExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(267);
						((ExpressionContext)_localctx).ba = expression(7);
						 ((ExpressionContext)_localctx).l = loc(((ExpressionContext)_localctx).as.l, ((ExpressionContext)_localctx).ba.l); ((ExpressionContext)_localctx).ast = new AstBinExpr(_localctx.l, getBinOp((((ExpressionContext)_localctx).op!=null?((ExpressionContext)_localctx).op.getText():null)), ((ExpressionContext)_localctx).as.ast, ((ExpressionContext)_localctx).ba.ast);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.as = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(270);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(271);
						match(AND);
						setState(272);
						((ExpressionContext)_localctx).ba = expression(6);
						 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).as.l, ((ExpressionContext)_localctx).ba.l); ((ExpressionContext)_localctx).ast = new AstBinExpr(_localctx.l, AstBinExpr.Oper.AND, ((ExpressionContext)_localctx).as.ast, ((ExpressionContext)_localctx).ba.ast);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.as = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(275);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(276);
						match(OR);
						setState(277);
						((ExpressionContext)_localctx).ba = expression(5);
						 ((ExpressionContext)_localctx).l = loc(((ExpressionContext)_localctx).as.l, ((ExpressionContext)_localctx).ba.l); ((ExpressionContext)_localctx).ast =  new AstBinExpr(_localctx.l, AstBinExpr.Oper.OR, ((ExpressionContext)_localctx).as.ast, ((ExpressionContext)_localctx).ba.ast);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.e = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(280);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(281);
						match(LSQUARE);
						setState(282);
						((ExpressionContext)_localctx).eb = expression(0);
						setState(283);
						((ExpressionContext)_localctx).b = match(RSQUARE);
						((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).e.l, ((ExpressionContext)_localctx).b); ((ExpressionContext)_localctx).ast =  new AstArrExpr(_localctx.l, ((ExpressionContext)_localctx).e.ast, ((ExpressionContext)_localctx).eb.ast);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.e = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(286);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(287);
						((ExpressionContext)_localctx).b = match(POWER);
						 ((ExpressionContext)_localctx).l =  loc(((ExpressionContext)_localctx).e.l, ((ExpressionContext)_localctx).b); ((ExpressionContext)_localctx).ast =  new AstSfxExpr( _localctx.l, AstSfxExpr.Oper.PTR, ((ExpressionContext)_localctx).e.ast);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.e = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(289);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(290);
						match(DOT);
						setState(291);
						((ExpressionContext)_localctx).i = match(IDENTIFIER);
						((ExpressionContext)_localctx).l = loc(((ExpressionContext)_localctx).e.l, ((ExpressionContext)_localctx).i); ((ExpressionContext)_localctx).ast =  new AstCmpExpr(_localctx.l, ((ExpressionContext)_localctx).e.ast, (((ExpressionContext)_localctx).i!=null?((ExpressionContext)_localctx).i.getText():null)); 
						}
						break;
					}
					} 
				}
				setState(297);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Fnc_call_exprContext extends ParserRuleContext {
		public AstNodes<AstExpr> ast;
		public ExpressionContext ea;
		public ExpressionContext eb;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Lang24Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Lang24Parser.COMMA, i);
		}
		public Fnc_call_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fnc_call_expr; }
	}

	public final Fnc_call_exprContext fnc_call_expr() throws RecognitionException {
		Fnc_call_exprContext _localctx = new Fnc_call_exprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_fnc_call_expr);

				ArrayList<AstExpr> arr = new ArrayList<AstExpr>();
		 
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(298);
			((Fnc_call_exprContext)_localctx).ea = expression(0);
			setState(305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(299);
				match(COMMA);
				setState(300);
				((Fnc_call_exprContext)_localctx).eb = expression(0);
				if(((Fnc_call_exprContext)_localctx).eb != null) arr.add(((Fnc_call_exprContext)_localctx).eb.ast);
				}
				}
				setState(307);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			arr.add(((Fnc_call_exprContext)_localctx).ea.ast);
			}
			_ctx.stop = _input.LT(-1);

					((Fnc_call_exprContext)_localctx).ast =  new AstNodes(arr);
			 
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 16);
		case 6:
			return precpred(_ctx, 15);
		case 7:
			return precpred(_ctx, 14);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00011\u0137\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0004\u0001)\b\u0001\u000b\u0001\f\u0001*\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004:\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004E\b\u0004\u0003\u0004G\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0005\u0005S\b\u0005\n\u0005\f\u0005V\t\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007"+
		"d\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0004"+
		"\b\u007f\b\b\u000b\b\f\b\u0080\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u008c\b\b\u0001\b\u0001\b\u0003"+
		"\b\u0090\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u00b0\b\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0005\n\u00b8\b\n\n\n\f\n\u00bb\t\n\u0001\n"+
		"\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00f8"+
		"\b\f\u0001\f\u0003\f\u00fb\b\f\u0001\f\u0003\f\u00fe\b\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0005\f\u0126\b\f\n\f\f\f\u0129\t\f\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0005\r\u0130\b\r\n\r\f\r\u0133\t\r\u0001\r\u0001\r\u0001\r\u0000"+
		"\u0001\u0018\u000e\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014"+
		"\u0016\u0018\u001a\u0000\u0003\u0001\u0000\"$\u0001\u0000%&\u0001\u0000"+
		"\u001c!\u0159\u0000\u001c\u0001\u0000\u0000\u0000\u0002(\u0001\u0000\u0000"+
		"\u0000\u0004,\u0001\u0000\u0000\u0000\u00061\u0001\u0000\u0000\u0000\b"+
		"6\u0001\u0000\u0000\u0000\nJ\u0001\u0000\u0000\u0000\fY\u0001\u0000\u0000"+
		"\u0000\u000ec\u0001\u0000\u0000\u0000\u0010\u008f\u0001\u0000\u0000\u0000"+
		"\u0012\u00af\u0001\u0000\u0000\u0000\u0014\u00b1\u0001\u0000\u0000\u0000"+
		"\u0016\u00be\u0001\u0000\u0000\u0000\u0018\u00fd\u0001\u0000\u0000\u0000"+
		"\u001a\u012a\u0001\u0000\u0000\u0000\u001c\u001d\u0003\u0002\u0001\u0000"+
		"\u001d\u001e\u0006\u0000\uffff\uffff\u0000\u001e\u0001\u0001\u0000\u0000"+
		"\u0000\u001f \u0003\u0004\u0002\u0000 !\u0006\u0001\uffff\uffff\u0000"+
		"!)\u0001\u0000\u0000\u0000\"#\u0003\u0006\u0003\u0000#$\u0006\u0001\uffff"+
		"\uffff\u0000$)\u0001\u0000\u0000\u0000%&\u0003\b\u0004\u0000&\'\u0006"+
		"\u0001\uffff\uffff\u0000\')\u0001\u0000\u0000\u0000(\u001f\u0001\u0000"+
		"\u0000\u0000(\"\u0001\u0000\u0000\u0000(%\u0001\u0000\u0000\u0000)*\u0001"+
		"\u0000\u0000\u0000*(\u0001\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000"+
		"+\u0003\u0001\u0000\u0000\u0000,-\u0005)\u0000\u0000-.\u0005(\u0000\u0000"+
		"./\u0003\u0012\t\u0000/0\u0006\u0002\uffff\uffff\u00000\u0005\u0001\u0000"+
		"\u0000\u000012\u0005)\u0000\u000023\u0005\u001a\u0000\u000034\u0003\u0012"+
		"\t\u000045\u0006\u0003\uffff\uffff\u00005\u0007\u0001\u0000\u0000\u0000"+
		"67\u0005)\u0000\u000079\u0005\u0012\u0000\u00008:\u0003\n\u0005\u0000"+
		"98\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000"+
		"\u0000;<\u0005\u0013\u0000\u0000<=\u0005\u001a\u0000\u0000=F\u0003\u0012"+
		"\t\u0000>?\u0005(\u0000\u0000?D\u0003\u0010\b\u0000@A\u0005\u0014\u0000"+
		"\u0000AB\u0003\u0002\u0001\u0000BC\u0005\u0015\u0000\u0000CE\u0001\u0000"+
		"\u0000\u0000D@\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000EG\u0001"+
		"\u0000\u0000\u0000F>\u0001\u0000\u0000\u0000FG\u0001\u0000\u0000\u0000"+
		"GH\u0001\u0000\u0000\u0000HI\u0006\u0004\uffff\uffff\u0000I\t\u0001\u0000"+
		"\u0000\u0000JK\u0006\u0005\uffff\uffff\u0000KL\u0003\u000e\u0007\u0000"+
		"LM\u0005)\u0000\u0000MN\u0005\u001a\u0000\u0000NT\u0003\u0012\t\u0000"+
		"OP\u0003\f\u0006\u0000PQ\u0006\u0005\uffff\uffff\u0000QS\u0001\u0000\u0000"+
		"\u0000RO\u0001\u0000\u0000\u0000SV\u0001\u0000\u0000\u0000TR\u0001\u0000"+
		"\u0000\u0000TU\u0001\u0000\u0000\u0000UW\u0001\u0000\u0000\u0000VT\u0001"+
		"\u0000\u0000\u0000WX\u0006\u0005\uffff\uffff\u0000X\u000b\u0001\u0000"+
		"\u0000\u0000YZ\u0005\u0019\u0000\u0000Z[\u0003\u000e\u0007\u0000[\\\u0005"+
		")\u0000\u0000\\]\u0005\u001a\u0000\u0000]^\u0003\u0012\t\u0000^_\u0006"+
		"\u0006\uffff\uffff\u0000_\r\u0001\u0000\u0000\u0000`d\u0006\u0007\uffff"+
		"\uffff\u0000ab\u0005\'\u0000\u0000bd\u0006\u0007\uffff\uffff\u0000c`\u0001"+
		"\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000d\u000f\u0001\u0000\u0000"+
		"\u0000ef\u0003\u0018\f\u0000fg\u0005\u001b\u0000\u0000gh\u0006\b\uffff"+
		"\uffff\u0000h\u0090\u0001\u0000\u0000\u0000ij\u0003\u0018\f\u0000jk\u0005"+
		"(\u0000\u0000kl\u0003\u0018\f\u0000lm\u0005\u001b\u0000\u0000mn\u0006"+
		"\b\uffff\uffff\u0000n\u0090\u0001\u0000\u0000\u0000op\u0005\u0011\u0000"+
		"\u0000pq\u0003\u0018\f\u0000qr\u0005\u001a\u0000\u0000rs\u0003\u0010\b"+
		"\u0000st\u0006\b\uffff\uffff\u0000t\u0090\u0001\u0000\u0000\u0000uv\u0005"+
		"\u000e\u0000\u0000vw\u0003\u0018\f\u0000wx\u0005\u001b\u0000\u0000xy\u0006"+
		"\b\uffff\uffff\u0000y\u0090\u0001\u0000\u0000\u0000z~\u0005\u0014\u0000"+
		"\u0000{|\u0003\u0010\b\u0000|}\u0006\b\uffff\uffff\u0000}\u007f\u0001"+
		"\u0000\u0000\u0000~{\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000"+
		"\u0000\u0080~\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000\u0000"+
		"\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0083\u0005\u0015\u0000\u0000"+
		"\u0083\u0084\u0006\b\uffff\uffff\u0000\u0084\u0090\u0001\u0000\u0000\u0000"+
		"\u0085\u0086\u0005\u0006\u0000\u0000\u0086\u0087\u0003\u0018\f\u0000\u0087"+
		"\u0088\u0005\r\u0000\u0000\u0088\u008b\u0003\u0010\b\u0000\u0089\u008a"+
		"\u0005\u0004\u0000\u0000\u008a\u008c\u0003\u0010\b\u0000\u008b\u0089\u0001"+
		"\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001"+
		"\u0000\u0000\u0000\u008d\u008e\u0006\b\uffff\uffff\u0000\u008e\u0090\u0001"+
		"\u0000\u0000\u0000\u008fe\u0001\u0000\u0000\u0000\u008fi\u0001\u0000\u0000"+
		"\u0000\u008fo\u0001\u0000\u0000\u0000\u008fu\u0001\u0000\u0000\u0000\u008f"+
		"z\u0001\u0000\u0000\u0000\u008f\u0085\u0001\u0000\u0000\u0000\u0090\u0011"+
		"\u0001\u0000\u0000\u0000\u0091\u0092\u0005\u0010\u0000\u0000\u0092\u00b0"+
		"\u0006\t\uffff\uffff\u0000\u0093\u0094\u0005\u0002\u0000\u0000\u0094\u00b0"+
		"\u0006\t\uffff\uffff\u0000\u0095\u0096\u0005\u0003\u0000\u0000\u0096\u00b0"+
		"\u0006\t\uffff\uffff\u0000\u0097\u0098\u0005\u0007\u0000\u0000\u0098\u00b0"+
		"\u0006\t\uffff\uffff\u0000\u0099\u009a\u0005\u0016\u0000\u0000\u009a\u009b"+
		"\u0005*\u0000\u0000\u009b\u009c\u0005\u0017\u0000\u0000\u009c\u009d\u0003"+
		"\u0012\t\u0000\u009d\u009e\u0006\t\uffff\uffff\u0000\u009e\u00b0\u0001"+
		"\u0000\u0000\u0000\u009f\u00a0\u0005\'\u0000\u0000\u00a0\u00a1\u0003\u0012"+
		"\t\u0000\u00a1\u00a2\u0006\t\uffff\uffff\u0000\u00a2\u00b0\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a4\u0005\u0012\u0000\u0000\u00a4\u00a5\u0003\u0014"+
		"\n\u0000\u00a5\u00a6\u0005\u0013\u0000\u0000\u00a6\u00a7\u0006\t\uffff"+
		"\uffff\u0000\u00a7\u00b0\u0001\u0000\u0000\u0000\u00a8\u00a9\u0005\u0014"+
		"\u0000\u0000\u00a9\u00aa\u0003\u0014\n\u0000\u00aa\u00ab\u0005\u0015\u0000"+
		"\u0000\u00ab\u00ac\u0006\t\uffff\uffff\u0000\u00ac\u00b0\u0001\u0000\u0000"+
		"\u0000\u00ad\u00ae\u0005)\u0000\u0000\u00ae\u00b0\u0006\t\uffff\uffff"+
		"\u0000\u00af\u0091\u0001\u0000\u0000\u0000\u00af\u0093\u0001\u0000\u0000"+
		"\u0000\u00af\u0095\u0001\u0000\u0000\u0000\u00af\u0097\u0001\u0000\u0000"+
		"\u0000\u00af\u0099\u0001\u0000\u0000\u0000\u00af\u009f\u0001\u0000\u0000"+
		"\u0000\u00af\u00a3\u0001\u0000\u0000\u0000\u00af\u00a8\u0001\u0000\u0000"+
		"\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00b0\u0013\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b2\u0005)\u0000\u0000\u00b2\u00b3\u0005\u001a\u0000\u0000"+
		"\u00b3\u00b9\u0003\u0012\t\u0000\u00b4\u00b5\u0003\u0016\u000b\u0000\u00b5"+
		"\u00b6\u0006\n\uffff\uffff\u0000\u00b6\u00b8\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b4\u0001\u0000\u0000\u0000\u00b8\u00bb\u0001\u0000\u0000\u0000\u00b9"+
		"\u00b7\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba"+
		"\u00bc\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc"+
		"\u00bd\u0006\n\uffff\uffff\u0000\u00bd\u0015\u0001\u0000\u0000\u0000\u00be"+
		"\u00bf\u0005\u0019\u0000\u0000\u00bf\u00c0\u0005)\u0000\u0000\u00c0\u00c1"+
		"\u0005\u001a\u0000\u0000\u00c1\u00c2\u0003\u0012\t\u0000\u00c2\u00c3\u0006"+
		"\u000b\uffff\uffff\u0000\u00c3\u0017\u0001\u0000\u0000\u0000\u00c4\u00c5"+
		"\u0006\f\uffff\uffff\u0000\u00c5\u00c6\u0005\t\u0000\u0000\u00c6\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00c7\u00c8\u0005\u000f\u0000\u0000\u00c8\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00c9\u00ca\u0005\u0005\u0000\u0000\u00ca\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00cb\u00cc\u0005*\u0000\u0000\u00cc\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00cd\u00ce\u0005+\u0000\u0000\u00ce\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00cf\u00d0\u0005,\u0000\u0000\u00d0\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00d1\u00d2\u0005\b\u0000\u0000\u00d2\u00fe"+
		"\u0006\f\uffff\uffff\u0000\u00d3\u00d4\u0005\n\u0000\u0000\u00d4\u00d5"+
		"\u0003\u0018\f\r\u00d5\u00d6\u0006\f\uffff\uffff\u0000\u00d6\u00fe\u0001"+
		"\u0000\u0000\u0000\u00d7\u00d8\u0005%\u0000\u0000\u00d8\u00d9\u0003\u0018"+
		"\f\f\u00d9\u00da\u0006\f\uffff\uffff\u0000\u00da\u00fe\u0001\u0000\u0000"+
		"\u0000\u00db\u00dc\u0005&\u0000\u0000\u00dc\u00dd\u0003\u0018\f\u000b"+
		"\u00dd\u00de\u0006\f\uffff\uffff\u0000\u00de\u00fe\u0001\u0000\u0000\u0000"+
		"\u00df\u00e0\u0005\'\u0000\u0000\u00e0\u00e1\u0003\u0018\f\n\u00e1\u00e2"+
		"\u0006\f\uffff\uffff\u0000\u00e2\u00fe\u0001\u0000\u0000\u0000\u00e3\u00e4"+
		"\u0005\u001e\u0000\u0000\u00e4\u00e5\u0003\u0012\t\u0000\u00e5\u00e6\u0005"+
		"\u001f\u0000\u0000\u00e6\u00e7\u0003\u0018\f\t\u00e7\u00e8\u0006\f\uffff"+
		"\uffff\u0000\u00e8\u00fe\u0001\u0000\u0000\u0000\u00e9\u00ea\u0005\f\u0000"+
		"\u0000\u00ea\u00eb\u0005\u0012\u0000\u0000\u00eb\u00ec\u0003\u0012\t\u0000"+
		"\u00ec\u00ed\u0005\u0013\u0000\u0000\u00ed\u00ee\u0006\f\uffff\uffff\u0000"+
		"\u00ee\u00fe\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005\u0012\u0000\u0000"+
		"\u00f0\u00f1\u0003\u0018\f\u0000\u00f1\u00f2\u0005\u0013\u0000\u0000\u00f2"+
		"\u00f3\u0006\f\uffff\uffff\u0000\u00f3\u00fe\u0001\u0000\u0000\u0000\u00f4"+
		"\u00fa\u0005)\u0000\u0000\u00f5\u00f7\u0005\u0012\u0000\u0000\u00f6\u00f8"+
		"\u0003\u001a\r\u0000\u00f7\u00f6\u0001\u0000\u0000\u0000\u00f7\u00f8\u0001"+
		"\u0000\u0000\u0000\u00f8\u00f9\u0001\u0000\u0000\u0000\u00f9\u00fb\u0005"+
		"\u0013\u0000\u0000\u00fa\u00f5\u0001\u0000\u0000\u0000\u00fa\u00fb\u0001"+
		"\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000\u00fc\u00fe\u0006"+
		"\f\uffff\uffff\u0000\u00fd\u00c4\u0001\u0000\u0000\u0000\u00fd\u00c7\u0001"+
		"\u0000\u0000\u0000\u00fd\u00c9\u0001\u0000\u0000\u0000\u00fd\u00cb\u0001"+
		"\u0000\u0000\u0000\u00fd\u00cd\u0001\u0000\u0000\u0000\u00fd\u00cf\u0001"+
		"\u0000\u0000\u0000\u00fd\u00d1\u0001\u0000\u0000\u0000\u00fd\u00d3\u0001"+
		"\u0000\u0000\u0000\u00fd\u00d7\u0001\u0000\u0000\u0000\u00fd\u00db\u0001"+
		"\u0000\u0000\u0000\u00fd\u00df\u0001\u0000\u0000\u0000\u00fd\u00e3\u0001"+
		"\u0000\u0000\u0000\u00fd\u00e9\u0001\u0000\u0000\u0000\u00fd\u00ef\u0001"+
		"\u0000\u0000\u0000\u00fd\u00f4\u0001\u0000\u0000\u0000\u00fe\u0127\u0001"+
		"\u0000\u0000\u0000\u00ff\u0100\n\b\u0000\u0000\u0100\u0101\u0007\u0000"+
		"\u0000\u0000\u0101\u0102\u0003\u0018\f\t\u0102\u0103\u0006\f\uffff\uffff"+
		"\u0000\u0103\u0126\u0001\u0000\u0000\u0000\u0104\u0105\n\u0007\u0000\u0000"+
		"\u0105\u0106\u0007\u0001\u0000\u0000\u0106\u0107\u0003\u0018\f\b\u0107"+
		"\u0108\u0006\f\uffff\uffff\u0000\u0108\u0126\u0001\u0000\u0000\u0000\u0109"+
		"\u010a\n\u0006\u0000\u0000\u010a\u010b\u0007\u0002\u0000\u0000\u010b\u010c"+
		"\u0003\u0018\f\u0007\u010c\u010d\u0006\f\uffff\uffff\u0000\u010d\u0126"+
		"\u0001\u0000\u0000\u0000\u010e\u010f\n\u0005\u0000\u0000\u010f\u0110\u0005"+
		"\u0001\u0000\u0000\u0110\u0111\u0003\u0018\f\u0006\u0111\u0112\u0006\f"+
		"\uffff\uffff\u0000\u0112\u0126\u0001\u0000\u0000\u0000\u0113\u0114\n\u0004"+
		"\u0000\u0000\u0114\u0115\u0005\u000b\u0000\u0000\u0115\u0116\u0003\u0018"+
		"\f\u0005\u0116\u0117\u0006\f\uffff\uffff\u0000\u0117\u0126\u0001\u0000"+
		"\u0000\u0000\u0118\u0119\n\u0010\u0000\u0000\u0119\u011a\u0005\u0016\u0000"+
		"\u0000\u011a\u011b\u0003\u0018\f\u0000\u011b\u011c\u0005\u0017\u0000\u0000"+
		"\u011c\u011d\u0006\f\uffff\uffff\u0000\u011d\u0126\u0001\u0000\u0000\u0000"+
		"\u011e\u011f\n\u000f\u0000\u0000\u011f\u0120\u0005\'\u0000\u0000\u0120"+
		"\u0126\u0006\f\uffff\uffff\u0000\u0121\u0122\n\u000e\u0000\u0000\u0122"+
		"\u0123\u0005\u0018\u0000\u0000\u0123\u0124\u0005)\u0000\u0000\u0124\u0126"+
		"\u0006\f\uffff\uffff\u0000\u0125\u00ff\u0001\u0000\u0000\u0000\u0125\u0104"+
		"\u0001\u0000\u0000\u0000\u0125\u0109\u0001\u0000\u0000\u0000\u0125\u010e"+
		"\u0001\u0000\u0000\u0000\u0125\u0113\u0001\u0000\u0000\u0000\u0125\u0118"+
		"\u0001\u0000\u0000\u0000\u0125\u011e\u0001\u0000\u0000\u0000\u0125\u0121"+
		"\u0001\u0000\u0000\u0000\u0126\u0129\u0001\u0000\u0000\u0000\u0127\u0125"+
		"\u0001\u0000\u0000\u0000\u0127\u0128\u0001\u0000\u0000\u0000\u0128\u0019"+
		"\u0001\u0000\u0000\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u012a\u0131"+
		"\u0003\u0018\f\u0000\u012b\u012c\u0005\u0019\u0000\u0000\u012c\u012d\u0003"+
		"\u0018\f\u0000\u012d\u012e\u0006\r\uffff\uffff\u0000\u012e\u0130\u0001"+
		"\u0000\u0000\u0000\u012f\u012b\u0001\u0000\u0000\u0000\u0130\u0133\u0001"+
		"\u0000\u0000\u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0131\u0132\u0001"+
		"\u0000\u0000\u0000\u0132\u0134\u0001\u0000\u0000\u0000\u0133\u0131\u0001"+
		"\u0000\u0000\u0000\u0134\u0135\u0006\r\uffff\uffff\u0000\u0135\u001b\u0001"+
		"\u0000\u0000\u0000\u0012(*9DFTc\u0080\u008b\u008f\u00af\u00b9\u00f7\u00fa"+
		"\u00fd\u0125\u0127\u0131";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}