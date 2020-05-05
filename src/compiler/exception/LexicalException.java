package compiler.exception;

public class LexicalException extends Exception {
	
	private int errorPosition;
	private String message;
	
	public LexicalException(String message, int errorPosition) {
		this.errorPosition = errorPosition;
		this.message = message;
	}
	
	public int getErrorPosition() {
		return errorPosition;
	}
	
	public String getMessage() {
		return message;
	}

}
