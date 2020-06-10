package compiler.main;

import compiler.exception.LexicalException;
import compiler.exception.SyntaxException;
import compiler.filesystem.FileProcessing;
import compiler.lexical.lexer.Lexer;
import compiler.lexical.token.InputString;
import compiler.syntax.syntaxer.Syntaxer;

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
		 * syntaxer - Syntaxer object to test.
		 * fileProcessing - FileProcessing object to read .c source file.
		 * sourceCode - InputString object to save input c source file content.
		 */
		Lexer lexer = new Lexer();
		Syntaxer syntaxer = new Syntaxer();
		
		FileProcessing fileProcessing = new FileProcessing("source.c");
		InputString sourceCode = new InputString(fileProcessing.readFile());
		
		try {
			lexer.tokenize(sourceCode);
			
			fileProcessing.writeFile(lexer, "lexer_correct_output.txt");
			
			System.out.println("correct_output.txt created.\n");
			
			try {
				syntaxer.analysis(lexer.getTokens());
			}catch (SyntaxException e) {
				e.printStackTrace();
			}
		} catch (LexicalException e) {
			fileProcessing.writeFile(e);
			
			System.out.println("lexer_error_output.txt created.\n");
			
			e.printStackTrace();
			
		}
	}
}



