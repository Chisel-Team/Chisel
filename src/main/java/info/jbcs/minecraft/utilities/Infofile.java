package info.jbcs.minecraft.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Infofile {
	static String[] stringArray=new String[0];
	public ArrayList<Infodata> data=new ArrayList<Infodata>();

	public Infofile(File file) throws IOException {
		LineReader reader = new LineReader(file);
		String line;
		
		Infodata d=null;
		ArrayList<String> lines=new ArrayList<String>();
		
		int state = 0;
		
		try{
			while ((line = reader.readLine()) != null) {
				if((line=line.trim()).isEmpty()) continue;
				
				switch (state) {
				case 0:
					if (line.charAt(line.length() - 1) != '{') {
						reader.die("unexpected: \""+line+"\" (expecting name and opening bracket)");
					}
					
					
					line=line.substring(0,line.length()-1).trim();
					state=1;
					
					d=new Infodata(line);
					
					lines.clear();
					break;
					
				case 1:
					if (line.equals("}")) {
						state=0;
						
						d.lines=lines.toArray(stringArray);
						data.add(d);
						continue;
					}
					
					int index=line.indexOf('=');
					if(index!=-1){
						String key=line.substring(0, index).trim().toLowerCase();
						String val=line.substring(index+1,line.length()).trim();
						
						d.fields.put(key,val);
					} else{
						lines.add(line);
					}
					break;
				}
			}
		} finally{
			reader.close();
		}
	}
	
}
