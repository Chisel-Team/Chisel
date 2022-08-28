package chisel.scripts;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Joiner;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;

import lombok.val;

public class BlockstateSplitter {

    public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException {
        File blockstatef = Paths.get("src", "main", "resources", "assets", "chisel", "blockstates", "default.json").toFile();
        JsonObject json = JsonParser.parseReader(new FileReader(blockstatef)).getAsJsonObject();
       
        JsonObject variants = json.getAsJsonObject("variants");
        
        Map<String, Map<String, String>> models = new HashMap<>();

        for (val e : variants.entrySet()) {
            String key = e.getKey();
            String[] split = key.split("\\/");
            String file = Joiner.on(File.separator).join(ArrayUtils.remove(split, split.length - 1));
            String variant = split[split.length - 1];
            Map<String, String> col = models.computeIfAbsent(file, s -> new HashMap<>());

            String model = e.getValue().getAsJsonObject().get("model").getAsString();
            col.put(variant, model);
        }
        
        for (val e : models.entrySet()) {
            File output = new File(blockstatef.getParentFile(), e.getKey() + ".json");
            if (output.exists()) {
                output.delete();
            }
            output.getParentFile().mkdirs();
            output.createNewFile();
            JsonWriter jw = new JsonWriter(new FileWriter(output));
            final String indent = "    ";
            jw.setIndent(indent);
            try {
                jw.beginObject();
                {
                    jw.name("forge_marker");
                    jw.value(1);

                    jw.name("defaults");
                    jw.beginObject();
                    {
                        jw.setIndent("");
                        jw.name("model");
                        jw.value("cube_all");
                    }
                    jw.endObject();
                    jw.setIndent(indent);
                    
                    jw.name("variants");
                    jw.beginObject();
                    {
                        for (val e2 : e.getValue().entrySet()) {
                            jw.name(e2.getKey());
                            jw.beginArray();
                            jw.setIndent("");
                            jw.beginObject();
                            {
                                jw.name("textures");
                                jw.beginObject();
                                {
                                    jw.name("all");
                                    jw.value(e2.getValue().replaceAll("^ctm:(chisel:)", "$1blocks/"));
                                }
                                jw.endObject();
                            }
                            jw.endObject();
                            jw.endArray();
                            jw.setIndent("    ");
                        }
                    }
                    jw.endObject();
                }
                jw.endObject();
            } finally {
                jw.flush();
                jw.close();
            }
        }
    }

}
