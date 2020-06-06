package compiler.main;

import compiler.exception.LexicalException;
import compiler.filesystem.FileProcessing;
import compiler.lexical.lexer.Lexer;
import compiler.lexical.token.InputString;

/** 
 * Main class for lexer testing.
 * 
 * @date 2020.05.05
 * @author SimHongSub
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
		
		/**
		 * lexer - Lexer object to test.
		 * fileProcessing - FileProcessing object to read .c source file.
		 * (*)SourceCode - InputString object to save input c source file content.
		 */
		Lexer lexer = new Lexer();
		
		FileProcessing fileProcessing = new FileProcessing("correct_source.c");
		InputString correctSourceCode = new InputString(fileProcessing.readFile());
		
		try {
			lexer.tokenize(correctSourceCode);
		} catch (LexicalException e) {
			fileProcessing.writeFile(e);
			e.printStackTrace();
		}
		
		fileProcessing.writeFile(lexer, "correct_output.txt");
		
		System.out.println("correct_output.txt created!!!\n");
		
		fileProcessing = new FileProcessing("error_source.c");
		InputString errorSourceCode = new InputString(fileProcessing.readFile());
		
		try {
			lexer.tokenize(errorSourceCode);
		} catch (LexicalException e) {
			fileProcessing.writeFile(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * The method to print lexer token list.
	 * 
	 * @param lexer - Current lexer object
	 */
	/*private static void printTokens(Lexer lexer) {
		
		System.out.println("-------Print tokens-------");
		
		List<Token> tokens = lexer.getTokens();
		
		if(tokens.size() != 0) {
			System.out.println("Token name : Token value");
			
			for(int i=0;i<tokens.size();i++) {
				Token token = tokens.get(i);
				System.out.println(token.getTokenName() + " : " + token.getTokenValue());
			}
		}
	}*/
}



