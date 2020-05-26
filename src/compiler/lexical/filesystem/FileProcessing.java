package compiler.lexical.filesystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import compiler.exception.LexicalException;
import compiler.lexical.lexer.Lexer;
import compiler.lexical.token.Token;

/** 
 * Class responsible for file I/O.
 * 
 * @date 2020.05.06
 * @author SimHongSub
 * @version 2.0
 */
public class FileProcessing {
	/**
	 * filePath - String variable to save c source file path.
	 * fileStream - FileInputStream to read file.
	 */
	private String filePath = null;
	private FileInputStream fileStream = null;
	
	public FileProcessing(String filePath) {
		this.filePath = filePath;
		try {
			this.fileStream = new FileInputStream(this.filePath);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Return fileContents after read c source file and save the content.
	 * 
	 * @return fileContents
	 * @throws IOException 
	 * @exception FileInputStream Exception.
	 */
	public String readFile() {
		String fileContents = "";
		
		try {
			byte[] readBuffer = new byte[fileStream.available()];
			
			while(fileStream.read(readBuffer) != -1) {
				fileContents += new String(readBuffer);
			}
			
			fileStream.close();
			
			return fileContents;
		}catch(Exception e) {
			e.getStackTrace();
			
			return e.getMessage();
		}
	}
	
	/**
	 * The method to write lexer tokenization result to file.
	 * 
	 * @param lexer - Current Lexer object
	 * @param fileName - Output file name
	 * @exception IOException.
	 */
	public void writeFile(Lexer lexer, String fileName) {
		File outFile = new File(fileName);
		
		List<Token> tokens = lexer.getTokens();
		
		if(tokens.size() != 0) {
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
				
				bw.write("Token name : Token value\n");
				
				for(int i=0;i<tokens.size();i++) {
					Token token = tokens.get(i);
					
					bw.write(token.getTokenName() + " : " + token.getTokenValue() + "\n");
				}
				
				bw.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}else {
			
		}
	}
	
	/**
	 * The method to write error message to file.
	 * Method overloading
	 * 
	 * @param lexicalE - Current LexicalException object
	 * @param fileName - Output file name
	 * @exception IOException.
	 */
	public void writeFile(LexicalException lexicalE) {
		File outFile = new File("error_output.txt");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			
			StackTraceElement[] stackElement = lexicalE.getStackTrace();
			
			bw.write(lexicalE.getMessage() + "\n");
			
			for(int i=0; i< stackElement.length; i++) {
				bw.write(stackElement[i].toString() + "\n");
			}
			
			bw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
