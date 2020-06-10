package compiler.exception;

import java.util.ArrayList;

/** 
 * Class responsible for syntax analyzer exception.
 * 
 * @date 2020.06.10
 * @author SimHongSub
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SyntaxException extends Exception {
	/**
	 * message - String variable to stores the error message to be displayed in the console.
	 */
	private String message;

	public SyntaxException(String state, String input, ArrayList<String> keywords, int index) {
		String inputCase;
		String syntax = "|";
		
		if(Character.isUpperCase(input.charAt(0))) {
			inputCase = "GOTO";
		}else {
			inputCase = "ACTION";
		}
		
		for(int i=index; i<keywords.size(); i++) {
			syntax += " " + keywords.get(i);
		}
		
		message = "\nBar progress : " + syntax + "\nError state : " + state + "\nCase : " + inputCase + "\ninput : " + input;
	}
	
	/**
	 * The get method to returns error message.
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
}
