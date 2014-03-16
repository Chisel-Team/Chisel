package info.jbcs.minecraft.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LineReader extends BufferedReader {
	String	filename;
	int		line	= 0;
	
	public LineReader(File file) throws FileNotFoundException {
		super(new FileReader(file));
		
		filename = file.getName();
	}
	
	@Override public String readLine() {
		line++;
		
		try {
			return super.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	String where(){
		return filename+", line "+line;
	}
	
	public void die(String cause) throws IOException{
		throw new IOException(where()+": "+cause);
	}
}
