package info.jbcs.minecraft.utilities;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;

public class Infodata {
	public String						name;
	
	public Hashtable<String, String>	fields	= new Hashtable<String, String>();
	public String[]						lines;
	
	public Infodata(String n) {
		name = n;
	}
	
	public Infodata(String n,int lineCount) {
		name = n;
		lines = new String[lineCount];
	}
	
	public String field(String key,String dflt) {
		String n = key.toLowerCase();
		
		if (fields.containsKey(n)) return fields.get(n);
		
		return dflt;
	}
	
	public String field(String key) {
		return field(key, null);
	}
	
	public int value(String key) {
		return Integer.parseInt(field(key, null));
	}
	
	public void set(String key,String value) {
		fields.put(key.length() == 1 ? key : key.toLowerCase(), value);
	}
	
	public void set(String key,int value) {
		fields.put(key.length() == 1 ? key : key.toLowerCase(), "" + value);
	}
	
	public void write(Writer writer,int linebreakEvery) throws IOException {
		writer.write(name + " {\n");
		
		Enumeration<String> keys = fields.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key.length() == 1) continue;
			
			writer.write("\t" + key + " = " + fields.get(key) + "\n");
		}
		
		writer.write("\n");
		int linesWritten = 0;
		for (int i = 0; i < lines.length; i++) {
			writer.write("\t" + lines[i] + "\n");
			linesWritten++;
			if (linebreakEvery != 0 && linesWritten % linebreakEvery == 0) writer.write("\n");
		}
		writer.write("\n");
		
		keys = fields.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key.length() != 1) continue;
			
			writer.write("\t" + key + " = " + fields.get(key) + "\n");
		}
		writer.write("}");
	}
}
