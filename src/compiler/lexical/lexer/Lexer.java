package compiler.lexical.lexer;

import java.util.ArrayList;
import java.util.List;

import compiler.exception.LexicalException;
import compiler.lexical.token.InputString;
import compiler.lexical.token.Token;

/** 
 * Class that tokenize c source code using DFA.
 * 
 * @date 2020.05.05
 * @author SimHongSub
 * @version 1.0
 */
public class Lexer {
	/**
	 * result - List variable to store tokenized tokens.
	 * (*)DFA - DFA object for each type(*)
	 */
	private List<Token> result;
	private DFA variableDFA;
	private DFA integerDFA;
	private DFA stringDFA;
	private DFA booleanDFA;
	private DFA floatDFA;
	private DFA identifierDFA;
	private DFA statementDFA;
	private DFA arithmeticDFA;
	private DFA bitwiseDFA;
	private DFA assignmentDFA;
	private DFA comparisonDFA;
	private DFA terminateDFA;
	private DFA blockDFA;
	private DFA parenDFA;
	private DFA separateDFA;
	private DFA whitespaceDFA;
	
	public Lexer() {
		initDFA();
		result = new ArrayList<Token>();
	}
	
	/**
	 * The get method to returns classified token list.
	 * 
	 * @return tokenName
	 */
	public List<Token> getTokens(){
		return result;
	}
	
	/**
	 * Method to tokenize each token in source using DFA.
	 * 
	 * @param source - InputString object where input c source file content is stored
	 * @exception LexicalException.
	 */
	public void tokenize(InputString source) throws LexicalException {
		while(source.getContentString().length() > 0) {
			if(whitespaceDFA.match(source, result)) {
				/* whitespace check */
				tokenProcessing("whitespace", source);
				
			}else if(identifierDFA.match(source, result)) {
				/* identifier check */
				tokenProcessing("id", source);
				
			}else if(variableDFA.match(source, result)) {
				/* variable check */
				tokenProcessing("variable", source);
				
			}else if(statementDFA.match(source, result)) {
				/* statement check */
				tokenProcessing("statement", source);
				
			}else if(parenDFA.match(source, result)) {
				/* paren check */
				tokenProcessing("paren", source);
				
			}else if(separateDFA.match(source, result)) {
				/* separate check */
				tokenProcessing("comma", source);
				
			}else if(blockDFA.match(source, result)) {
				/* block check */
				tokenProcessing("brace", source);
				
			}/*else if(assignmentDFA.match(source, result)) {
				assignment check
				tokenProcessing("assign", source);
				
			}*/else if(stringDFA.match(source, result)) {
				/* string check */
				tokenProcessing("literal", source);
				
			}else if(floatDFA.match(source, result)) {
				/* float check */
				tokenProcessing("float", source);
				
			}else if(integerDFA.match(source, result)) {
				/* integer check */
				tokenProcessing("num", source);
				
			}else if(booleanDFA.match(source, result)) {
				/* boolean check */
				tokenProcessing("bool", source);
				
			}else if(terminateDFA.match(source, result)) {
				/* terminate check */
				tokenProcessing("semi", source);
				
			}else if(arithmeticDFA.match(source, result)) {
				/* arithmetic check */
				tokenProcessing("arithmetic", source);
				
			}else if(comparisonDFA.match(source, result)) {
				/* comparison check */
				tokenProcessing("comp", source);
				
			}else if(bitwiseDFA.match(source, result)) {
				/* whitespace check */
				tokenProcessing("bitwise", source);
				
			}else if(assignmentDFA.match(source, result)) {
				/* assignment check */
				tokenProcessing("assign", source);
				
			}else {
				/* not belong to any DFA */
				result.clear();
				
				throw new LexicalException(source.getErrorMessage(), source.getErrorPosition());
			}
		}
	}
	
	/**
	 * Token processing method for storing the token object in the correct form.
	 * 
	 * @param tokenName -Token name to which each token value corresponds
	 * @param source - InputString object where input c source file content is stored
	 */
	private void tokenProcessing(String tokenName, InputString source) {
		String s = source.getContentString();
		Token token;
		
		if(tokenName.equals("variable")) {
			
			token = new Token("vtype", s.substring(0, source.getNextPosition()));
			
			result.add(token);
		}else if(tokenName.equals("statement")){
			
			token = new Token(s.substring(0, source.getNextPosition()), s.substring(0, source.getNextPosition()));
			
			result.add(token);
			
		}else if(tokenName.equals("paren")) {
			
			if(s.substring(0, source.getNextPosition()).equals("(")) {
				
				token = new Token("lparen", s.substring(0, source.getNextPosition()));
			}else {
				
				token = new Token("rparen", s.substring(0, source.getNextPosition()));
			}
			
			result.add(token);
		}else if(tokenName.equals("brace")) {
			
			if(s.substring(0, source.getNextPosition()).equals("{")) {
				
				token = new Token("lbrace", s.substring(0, source.getNextPosition()));
			}else {
				
				token = new Token("rbrace", s.substring(0, source.getNextPosition()));
			}
			
			result.add(token);
		}else if(tokenName.equals("arithmetic")) {
			
			if(s.substring(0, source.getNextPosition()).equals("+") || s.substring(0, source.getNextPosition()).equals("-")) {
				
				token = new Token("addsub", s.substring(0, source.getNextPosition()));
			}/*else if(s.substring(0, source.getNextPosition()).equals("-")) {
				
				token = new Token("addsub", s.substring(0, source.getNextPosition()));
			}else if(s.substring(0, source.getNextPosition()).equals("*")) {
				
				token = new Token("multdiv", s.substring(0, source.getNextPosition()));
			}*/else{
				
				token = new Token("multdiv", s.substring(0, source.getNextPosition()));
			}
			
			result.add(token);
		}else {
			
			if(!tokenName.equals("whitespace")) {
				
				token = new Token(tokenName, s.substring(0, source.getNextPosition()));
				
				result.add(token);
			}
		}
		
		s = s.substring(source.getNextPosition());
		
		source.setContentString(s);
	}
	
	/**
	 * Method to initialize each declared DFA.
	 * 
	 * @see compiler.lexical.lexer.DFA
	 */
	private void initDFA() {
		
		/* Variable type DFA initialize */
		String[] variableState = { "A,S", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N,E" };
		String[] variableTransition = { "A,B,i","A,C,c","A,D,b","A,E,f",
										"B,F,n",
										"C,G,h",
										"D,H,o",
										"E,I,l",
										"F,N,t",
										"G,J,a",
										"H,K,o",
										"I,L,o",
										"J,N,r",
										"K,N,l",
										"L,M,a",
										"M,N,t" };
		
		variableDFA = new DFA(variableState, variableTransition, "avariable");
		
		/* Signed integer DFA initialize */
		String[] integerState = { "A,S", "B,E", "C,E", "D" };
		String[] integerTransition = { "A,B,1,2,3,4,5,6,7,8,9","A,C,0","A,D,-",
										"B,B,0,1,2,3,4,5,6,7,8,9",
										"D,B,1,2,3,4,5,6,7,8,9" };
		
		integerDFA = new DFA(integerState, integerTransition, "integer");
		
		/* Literal string DFA initialize */
		String[] stringState = { "A,S", "B", "C,E" };
		String[] stringTransition = { "A,B,\"",
										"B,B,0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z, ",
										"B,C,\"" };
		
		stringDFA = new DFA(stringState, stringTransition, "string");
		
		/* Boolean string DFA initialize */
		String[] booleanState = { "A,S", "B", "C", "D", "E,E", "F", "G", "H", "I", "J,E" };
		String[] booleanTransition = { "A,B,t","A,F,f",
										"B,C,r",
										"C,D,u",
										"D,E,e",
										"F,G,a",
										"G,H,l",
										"H,I,s",
										"I,J,e" };
		
		booleanDFA = new DFA(booleanState, booleanTransition, "boolean");
		
		/* Floating-point number DFA initialize */
		String[] floatState = { "A,S", "B", "C", "D", "E", "F", "G", "H,E", "I" };
		String[] floatTransition = { "A,B,0","A,C,1,2,3,4,5,6,7,8,9","A,D,-",
										"B,G,.",
										"C,C,0,1,2,3,4,5,6,7,8,9", "C,G,.",
										"D,E,1,2,3,4,5,6,7,8,9", "D,F,0",
										"E,E,0,1,2,3,4,5,6,7,8,9", "E,G,.",
										"F,G,.",
										"G,H,0,1,2,3,4,5,6,7,8,9",
										"H,I,0", "H,H,1,2,3,4,5,6,7,8,9",
										"I,I,0", "I,H,1,2,3,4,5,6,7,8,9" };
		
		floatDFA = new DFA(floatState, floatTransition, "float");
		
		/* Identifier DFA initialize */
		String[] identifierState = { "A,S", "B,E" };
		String[] identifierTransition = { "A,B,_,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z",
										"B,B,_,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,0,1,2,3,4,5,6,7,8,9" };
		
		
		identifierDFA = new DFA(identifierState, identifierTransition, "identifier");
		
		/* Special statements DFA initialize */
		String[] statementState = { "A,S", "B", "C,E", "D", "E", "F", "G,E", "H", "I", "J", "K", "L,E", "M", "N", "O,E", "P", "Q", "R", "S", "T", "U,E" };
		String[] statementTransition = { "A,B,i", "A,D,e", "A,H,w", "A,M,f", "A,P,r",
										"B,C,f",
										"D,E,l",
										"E,F,s",
										"F,G,e",
										"H,I,h",
										"I,J,i",
										"J,K,l",
										"K,L,e",
										"M,N,o",
										"N,O,r",
										"P,Q,e",
										"Q,R,t",
										"R,S,u",
										"S,T,r",
										"T,U,n" };
		
		statementDFA = new DFA(statementState, statementTransition, "statement");
		
		/* Arithmetic operators DFA initialize */
		String[] arithmeticState = { "A,S", "B,E" };
		String[] arithmeticTransition = { "A,B,+,-,*,/" };
		
		arithmeticDFA = new DFA(arithmeticState, arithmeticTransition, "arithmetic");
		
		/* Bitwise operators DFA initialize */
		String[] bitwiseState = { "A,S", "B", "C,E", "D" };
		String[] bitwiseTransition = { "A,B,<","A,D,>", "A,C,&,|",
										"B,C,<",
										"D,C,>" };
		
		bitwiseDFA = new DFA(bitwiseState, bitwiseTransition, "bitwise");
		
		/* Assignment operator DFA initialize */
		String[] assignmentState = { "A,S", "B,E" };
		String[] assignmentTransition = { "A,B,=" };
		
		assignmentDFA = new DFA(assignmentState, assignmentTransition, "assignment");
		
		/* Comparison operators DFA initialize */
		String[] comparisonState = { "A,S", "B,E", "C,E", "D", "E,E", "F", "G,E" };
		String[] comparisonTransition = { "A,B,<","A,C,>","A,D,=", "A,F,!",
										"B,B,=",
										"C,C,=",
										"D,E,=",
										"F,G,="};
		
		comparisonDFA = new DFA(comparisonState, comparisonTransition, "comparison");
		
		/* Terminating symbol DFA initialize */
		String[] terminateState = { "A,S", "B,E" };
		String[] terminateTransition = { "A,B,;" };
		
		terminateDFA = new DFA(terminateState, terminateTransition, "terminate");
		
		/* A pair of symbols for area/scope of variables and functions DFA initialize */
		String[] blockState = { "A,S", "B,E" };
		String[] blockTransition = { "A,B,{,}" };
		
		blockDFA = new DFA(blockState, blockTransition, "block");
		
		/* A pair of symbols for function/statement  DFA initialize */
		String[] parenState = { "A,S", "B,E" };
		String[] parenTransition = { "A,B,(,)" };
		
		parenDFA = new DFA(parenState, parenTransition, "paren");
		
		/* Separating symbol DFA initialize */
		String[] separateState = { "A,S", "B,E" };
		String[] separateTransition = { "A,B,comma" };
		
		separateDFA = new DFA(separateState, separateTransition, "separate");
		
		/* Whitespaces DFA initialize */
		String[] whitespaceState = { "A,S", "B,E" };
		String[] whitespaceTransition = { "A,B, ,\n,\t" };
		
		whitespaceDFA = new DFA(whitespaceState, whitespaceTransition, "whitespace");
		
	}
}
