package compiler.lexical.token;

public class Token {
	
	private String tokenName;
	private String tokenValue;
	
	public Token(String tokenName, String tokenValue) {
		this.tokenName = tokenName;
		this.tokenValue = tokenValue;
	}
	
	public String getTokenName() {
		
		return tokenName;
	}
	
	public String getTokenValue() {
		
		return tokenValue;
	}

}
