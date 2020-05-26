package compiler.lexical.token;

/** 
 * Class containing one lexeme information(Token name, Token value).
 * 
 * @date 2020.04.21
 * @author SimHongSub
 * @version 1.0
 */
public class Token {
	/**
	 * tokenName - String variable to save the token name.
	 * tokenValue - String variable to save the token value.
	 */
	private String tokenName;
	private String tokenValue;
	
	public Token(String tokenName, String tokenValue) {
		this.tokenName = tokenName;
		this.tokenValue = tokenValue;
	}
	
	/**
	 * The get method to returns token name.
	 * 
	 * @return tokenName
	 */
	public String getTokenName() {
		
		return tokenName;
	}
	
	/**
	 * The get method to returns token value.
	 * 
	 * @return tokenValue
	 */
	public String getTokenValue() {
		
		return tokenValue;
	}

}
