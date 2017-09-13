package chisel.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class CtxToMcmeta {
    
    private static final Map<String, String> typeRenames = ImmutableMap.<String, String>builder()
            .put("R", "random")
            .put("V", "pattern")
            .put("CTMV", "pillar")
            .put("CTMH", "ctm_horizontal")
            .build();
    
    private static final String toAbsoluteTexture(Path root, Path cwd, String texture) {
        String ret;
        if (texture.startsWith("./")) {
            ret = root.relativize(cwd.getParent()).toString().replace('\\', '/') + "/" + texture.substring(2);
        } else {
            ret = texture;
        }
        return ret;
    }
    
    private static final File toAbsolute(Path root, Path cwd, String texture) {
        return new File(root.toFile(), toAbsoluteTexture(root, cwd, texture));
    }
    
    public static void main(String[] args) throws IOException {
        Path texturesFolder = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "blocks");
        System.out.println(texturesFolder.toAbsolutePath());
        Files.walk(texturesFolder)
            .filter(path -> path.toString().endsWith(".ctx"))
            .forEach(path -> {
                FileReader fr = null;
                try {
                    fr = new FileReader(path.toFile());
                    JsonObject data = new JsonParser().parse(fr).getAsJsonObject();
                    
                    String type = data.get("type").getAsString();
                    String[] textures = Optional.ofNullable(data.get("textures"))
                            .map(JsonElement::getAsJsonArray)
                            .map(arr -> StreamSupport.stream(arr.spliterator(), false).map(JsonElement::getAsString).toArray(String[]::new))
                            .orElse(new String[] { "./" + path.getFileName().toString().replace(".ctx", "") });
                    
                    Optional<String> layer = Optional.ofNullable(data.get("layer")).map(JsonElement::getAsString);
                    Optional<JsonObject> info = Optional.ofNullable(data.getAsJsonObject("info"));
                    
                    JsonObject output = new JsonObject();

                    JsonObject ctm = new JsonObject();
                    ctm.add("ctm_version", new JsonPrimitive(1));
                    
                    if (!type.equalsIgnoreCase("normal")) {
                        ctm.add("type", new JsonPrimitive(typeRenames.getOrDefault(type, type.toLowerCase())));
                    }
                    
                    layer.ifPresent(s -> ctm.add("layer", new JsonPrimitive(s)));
                    
                    if (textures.length > 1) {
                        JsonArray arr = new JsonArray();
                        Arrays.stream(ArrayUtils.remove(textures, 0))
                                .map(s -> toAbsoluteTexture(texturesFolder, path, s))
                                .forEach(s -> arr.add(new JsonPrimitive(s)));
                        ctm.add("textures", arr);
                    }
                    
                    info.ifPresent(obj -> ctm.add("extra", obj));
                    
                    output.add("ctm", ctm);
                    
                    FileWriter fw = null;
                    try {
                        File texture = toAbsolute(texturesFolder, path, textures[0]);
                        File outputFile = new File(texture.getParentFile(), texture.getName() + ".png.mcmeta");
                        outputFile.getParentFile().mkdirs();
                        fw = new FileWriter(outputFile);
                        fw.write(new GsonBuilder().setPrettyPrinting().create().toJson(output).replace("  ", "    "));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(fw);
                    }
                    
                } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(fr);
                }
            });
    }
}
