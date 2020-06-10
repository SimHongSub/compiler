package compiler.exception;

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

	public SyntaxException(String state, String input, String syntaxString, int index) {
		String inputCase;
		
		if(Character.isUpperCase(input.charAt(0))) {
			inputCase = "GOTO";
		}else {
			inputCase = "ACTION";
		}
		
		String temp = syntaxString.substring(0, index);
		
		syntaxString = temp + "| " + syntaxString.substring(index, syntaxString.length());
		
		message = "\nBar progress : " + syntaxString + "\nError state : " + state + "\nCase : " + inputCase + "\ninput : " + input;
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
