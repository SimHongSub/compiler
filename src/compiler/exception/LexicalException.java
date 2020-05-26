package compiler.exception;

/** 
 * Class responsible for lexical analyzer exception.
 * 
 * @date 2020.05.05
 * @author SimHongSub
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LexicalException extends Exception {
	/**
	 * errorPosition - Variable that stores the location of error to be displayed in the console.
	 * message - String variable to stores the error message to be displayed in the console.
	 */
	private int errorPosition;
	private String message;
	
	public LexicalException(String message, int errorPosition) {
		this.errorPosition = errorPosition;
		this.message = message;
	}
	
	/**
	 * The get method to returns error token position.
	 * 
	 * @return tokenName
	 */
	public int getErrorPosition() {
		return errorPosition;
	}
	
	/**
	 * The get method to returns error message.
	 * 
	 * @return tokenName
	 */
	public String getMessage() {
		return "\n" + message;
	}

}
