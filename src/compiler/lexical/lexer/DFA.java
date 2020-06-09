package compiler.lexical.lexer;

import java.util.*;

import compiler.lexical.token.InputString;
import compiler.lexical.token.Token;

/** 
 * Class to create DFA object according to states and transitions.
 * 
 * @date 2020.05.04
 * @author SimHongSub
 * @version 1.0
 */
public class DFA {
	/**
	 * type - The kind of tokens classified by DFA.
	 * start - Start state of DFA.
	 * ends - Final states of DFA.
	 * transition - Transitions of each state.
	 */
	private String type;
	private String start;
	private Set<String> ends;
	private Map<String, Map<Character,String>> transitions; // state -> (character -> next state)

	DFA(String[] ss, String[] ts, String type) {
		ends = new TreeSet<String>();
		transitions = new TreeMap<String, Map<Character,String>>();

		// States
		for (String v : ss) {
			String[] pieces = v.split(",");
			if (pieces.length>1) {
				if (pieces[1].equals("S")) start = pieces[0];
				else if (pieces[1].equals("E")) ends.add(pieces[0]);
			}
		}

		// Transitions
		for (String e : ts) {
			String[] pieces = e.split(",");
			String from = pieces[0], to = pieces[1];
			if (!transitions.containsKey(from)) transitions.put(from, new TreeMap<Character,String>());
			for (int i=2; i<pieces.length; i++) {
				if(pieces[i].equals("comma")) {
					pieces[i] = ",";
				}
				transitions.get(from).put(pieces[i].charAt(0), to);				
			}
		}
		
		this.type = type;
	}
	
	/**
	 * Returns whether or not the DFA accepts the string.
	 * 
	 * @param source - InputString object where input c source file content is stored
	 * @param tokens - Token list to classify the integer and arithmetic
	 * @return boolean
	 * @exception NullPointerException
	 */
	public boolean match(InputString source, List<Token> tokens) {
		String state = start;
		
		String s = source.getContentString();
		int tempPosition = -1;
		
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(this.type == "identifier") {
				if (!transitions.get(state).containsKey(c)) {
					
					return identifierMatch(state, s, i, c, source);
				}
				
				state = transitions.get(state).get(c);
				
			}else if(this.type == "float" || this.type == "integer") {
				try {
					if (!transitions.get(state).containsKey(c)) {
						if (tempPosition == -1) {

							return false;
						} else {
							if(s.substring(0, tempPosition).contains("-")) {
								
								return classifyArithmetic(tokens, tempPosition, source);
							}else {
								source.setNextPosition(tempPosition);

								return true;
							}
						}
					}
						
					state = transitions.get(state).get(c);
					
					if(ends.contains(state)) {
						tempPosition = i+1;
					}
				}catch(NullPointerException e) {
					source.setNextPosition(tempPosition);
					
					return true;
				}
				
			}else {
				if (!transitions.get(state).containsKey(c)) {
					source.setErrorPosition(i+1);
					source.setErrorMessage("Untokenized input c source code : \n" + source.getContentString() + "\nError Token : \" " + source.getContentString().substring(0, i+1) + " \"");
					
					return false;
				}
				state = transitions.get(state).get(c);
				
				if(ends.contains(state)) {
					source.setNextPosition(i+1);
						
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns whether or not the identifier DFA accepts the string.
	 * 
	 * @param state - Current state in DFA
	 * @param s - Source content stored in the InputString object
	 * @param position - The position of the character to check
	 * @param positionChar - Character to check
	 * @param source - InputString object where input c source file content is stored
	 * @return boolean
	 */
	private boolean identifierMatch(String state, String s, int position, char positionChar, InputString source) {
		
		if(ends.contains(state)) {
			s = s.substring(0, position);
			if(s.equals("int") || s.equals("char") || s.equals("bool") || s.equals("float") 
					|| s.equals("if") || s.equals("else") || s.equals("while") || s.equals("for") || s.equals("return") 
					|| s.equals("true") || s.equals("false")) {
				
				return false;
			}else {
				source.setNextPosition(position);
				
				return true;
			}
		}else {
			
			return false;
		}
	}
	
	/**
	 * Returns whether or not the Arithmetic.
	 * 
	 * @param tokens - Tokenized token list
	 * @param tempPosition - Current token position
	 * @param source - InputString object where input c source file content is stored
	 * @return boolean
	 */
	private boolean classifyArithmetic(List<Token> tokens, int tempPosition, InputString source) {
		Token token = tokens.get(tokens.size() - 1);
		
		if(token.getTokenName().equals("INT")||token.getTokenName().equals("FLOAT")||token.getTokenName().equals("ID")) {
			
			return false;
		}else {
			if(token.getTokenName().equals("RPAREN")) {
				int countLPAREN = 0;
				int countRPAREN = 0;
				
				for(Token temp : tokens) {
					if(temp.getTokenName().equals("LPAREN")) {
						countLPAREN++;
					}else if(temp.getTokenName().equals("RPAREN")) {
						countRPAREN++;
					}
				}
				
				if(countLPAREN == countRPAREN) {
					
					return false;
				}else {
					source.setNextPosition(tempPosition);
					
					return true;
				}
			}else {
				source.setNextPosition(tempPosition);

				return true;
			}
		}
	}
}
