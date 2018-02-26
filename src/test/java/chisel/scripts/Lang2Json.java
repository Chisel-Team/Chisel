package chisel.scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.gson.stream.JsonWriter;

public class Lang2Json {
    
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        File outfile = new File(args[0].replace(".lang", ".json"));
        
        Pattern MAPPING_PATTERN = Pattern.compile("^(\\S+)=(.+)$");

        try (JsonWriter out = new JsonWriter(new FileWriter(outfile))) {
            out.setIndent("  ");
            out.beginObject();
            for (String s : FileUtils.readLines(file, Charsets.UTF_8)) {
                s = s.trim();
                if (s.startsWith("#")) {
                    out.name("__comment").value(s.substring(1).trim());
                } else {
                    Matcher m = MAPPING_PATTERN.matcher(s);
                    if (m.matches()) {
                        out.name(m.group(1)).value(m.group(2));
                    }
                }
            }
            out.endObject();
        }
    }

}
