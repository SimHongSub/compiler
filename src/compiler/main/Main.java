package compiler.main;

import java.util.List;

import compiler.exception.LexicalException;
import compiler.lexical.filesystem.FileProcessing;
import compiler.lexical.lexer.Lexer;
import compiler.lexical.token.InputString;
import compiler.lexical.token.Token;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lexer lexer = new Lexer();
		
		FileProcessing fileProcessing = new FileProcessing("test.c");
		
		InputString sourceCode = new InputString(fileProcessing.readFile());
		
		try {
			
			lexer.tokenize(sourceCode);
		} catch (LexicalException e) {
			
			e.printStackTrace();
		}
		
		List<Token> tokens = lexer.getTokens();
		
		if(tokens.size() != 0) {
			System.out.println("Token name : Token value");
			
			for(int i=0;i<tokens.size();i++) {
				Token token = tokens.get(i);
				System.out.println(token.getTokenName() + " : " + token.getTokenValue());
			}
		}
	}
}



