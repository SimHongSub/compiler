package compiler.lexical.token;

/** 
 * Class to create input string(c source code) object.
 * 
 * @date 2020.05.05
 * @author SimHongSub
 * @version 1.0
 */
public class InputString {
	/**
	 * nextPosition - Variable to save the token position used to cut the token in the entire content.
	 * contentString - String variable to save the entire content(c source code).
	 * errorPosition - Variable to save the error token position not classified in any DFA.
	 * errorMessage - String variable to save the error message.
	 */
	private int nextPosition;
	private String contentString;
	private int errorPosition;
	private String errorMessage;
	
	public InputString(String text) {
		this.nextPosition = 0;
		this.contentString = text;
		this.errorPosition = 0;
	}
	
	/**
	 * The get, set method to returns next character position and set nextPosition value.
	 * 
	 * @param nextPosition
	 * @return tokenName
	 */
	public int getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(int nextPosition) {
		this.nextPosition = nextPosition;
	}
	
	/**
	 * The get, set method to returns source content and set source content value.
	 * 
	 * @param contentString
	 * @return tokenName
	 */
	public String getContentString() {
		return contentString;
	}

	public void setContentString(String contentString) {
		this.contentString = contentString;
	}
	
	/**
	 * The get, set method to returns error token position and set position value.
	 * 
	 * @param errorPosition
	 * @return tokenName
	 */
	public int getErrorPosition() {
		return errorPosition;
	}
	
	public void setErrorPosition(int errorPosition) {
		this.errorPosition = errorPosition;
	}
	
	/**
	 * The get, set method to returns error message and set message value.
	 * 
	 * @return tokenName
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
