package compiler.lexical.filesystem;

import java.io.FileInputStream;

/* Developer : SimHongSub
 * Date : 2020-04-21
 * Contents : Class responsible for file I/O 
 * */
public class FileProcessing {
	
	private String filePath = null;
	private FileInputStream fileStream = null;
	
	public FileProcessing(String filePath) {
		this.filePath = filePath;
	}
	
	public String readFile() {
		String fileContents = "";
		
		try {
			this.fileStream = new FileInputStream(this.filePath);
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
}
