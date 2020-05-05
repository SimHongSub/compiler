package compiler.lexical.token;

public class InputString {
	private int nextPosition;
	private String contentString;
	private int errorPosition;
	private String errorMessage;
	
	public InputString(String text) {
		this.nextPosition = 0;
		this.contentString = text;
		this.errorPosition = 0;
	}
	
	public int getNextPosition() {
		return nextPosition;
	}

	public void setNextPosition(int nextPosition) {
		this.nextPosition = nextPosition;
	}

	public String getContentString() {
		return contentString;
	}

	public void setContentString(String contentString) {
		this.contentString = contentString;
	}

	public int getErrorPosition() {
		return errorPosition;
	}

	public void setErrorPosition(int errorPosition) {
		this.errorPosition = errorPosition;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
