package compiler.lexical.lexer;

import java.util.*;

import compiler.lexical.token.InputString;

public class DFA {
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
				transitions.get(from).put(pieces[i].charAt(0), to);				
			}
		}
		
		this.type = type;
	}

	public boolean match(InputString inputText) {
		String state = start;
		
		String s = inputText.getContentString();
		int tempPosition = -1;
		
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(this.type == "identifier") {
				if (!transitions.get(state).containsKey(c)) {
					if(ends.contains(state)) {
						s = s.substring(0, i);
						if(s.equals("int") || s.equals("char") || s.equals("bool") || s.equals("float") 
								|| s.equals("if") || s.equals("else") || s.equals("while") || s.equals("for") || s.equals("return") 
								|| s.equals("true") || s.equals("false")) {
							
							return false;
						}else {
							inputText.setNextPosition(i);
							
							return true;
						}
					}else {
						if(i >= inputText.getErrorPosition()) {
							inputText.setErrorPosition(i);
							inputText.setErrorMessage("Token : " + inputText.getContentString().substring(0, i));
						}
						
						return false;
					}
				}
				
				state = transitions.get(state).get(c);
			}else if(this.type == "float") {
				if (!transitions.get(state).containsKey(c)) {
					if(tempPosition == -1) {
						if(i+1 >= inputText.getErrorPosition()) {
							inputText.setErrorPosition(i+1);
							inputText.setErrorMessage("Token : " + inputText.getContentString().substring(0, i+1));
						}
						
						return false;
					}else {
						inputText.setNextPosition(tempPosition);
						
						return true;
					}
				}
					
				state = transitions.get(state).get(c);
				
				if(ends.contains(state)) {
					tempPosition = i+1;
				}
			}else if(this.type == "integer") {
				try {
					if (!transitions.get(state).containsKey(c)) {
						if(tempPosition == -1) {
							if(i+1 >= inputText.getErrorPosition()) {
								inputText.setErrorPosition(i+1);
								inputText.setErrorMessage("Token : " + inputText.getContentString().substring(0, i+1));
							}
							
							return false;
						}else {
							inputText.setNextPosition(tempPosition);
							
							return true;
						}
					}
					
					state = transitions.get(state).get(c);
					
					if(ends.contains(state)) {
						tempPosition = i+1;
					}
				}catch (NullPointerException e) {
					inputText.setNextPosition(tempPosition);
					
					return true;
				}
			}else {
				//try {
					if (!transitions.get(state).containsKey(c)) {
						if(i+1 >= inputText.getErrorPosition()) {
							inputText.setErrorPosition(i+1);
							inputText.setErrorMessage("Token : " + inputText.getContentString().substring(0, i+1));
						}
						
						return false;
					}
					state = transitions.get(state).get(c);
					
					if(ends.contains(state)) {
						
						inputText.setNextPosition(i+1);
						
						return true;
					}
				/*}catch (NullPointerException e) {
					
					return false;
				}*/
				
			}
		}
		
		return false;
		
		//return ends.contains(state);
	}
}
