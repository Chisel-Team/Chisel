package chisel.scripts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class CreateProxies {
    
    private static final Path TEXTURES_FOLDER = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "block");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        for (Path block : Files.list(TEXTURES_FOLDER).toArray(Path[]::new)) {
            crop(block, "array", 2);
            crop(block, "chaotic_bricks", 3);
            crop(block, "cuts", 4);
            crop(block, "jellybean", 2);
            crop(block, "slanted", 2);
            crop(block, "zag", "zag-ctm");
        }
    }

    private static void crop(Path folder, String name, int size) throws IOException {
        crop(folder, name, name + "-" + size + "x" + size);
    }s
    
    private static void crop(Path folder, String name, String newName) throws IOException {
        Path texture = folder.resolve(name + ".png");
        Path newTexture = folder.resolve(newName + ".png");
        
        Path meta = folder.resolve(name + ".png.mcmeta");
        Path newMeta = folder.resolve(newName + ".png.mcmeta");
        
        if (!Files.exists(texture) || Files.exists(newTexture)) {
            return; // Not a texture or already processed
        }
        
        Files.move(texture, newTexture);
        Files.move(meta, newMeta);
        
        BufferedImage img = ImageIO.read(newTexture.toFile());
        img = img.getSubimage(0, 0, 16, 16);
        ImageIO.write(img, "png", texture.toFile());
        
        JsonObject metadata = new JsonObject();
        JsonObject ctm = new JsonObject();
        ctm.addProperty("ctm_version", 1);
        ctm.addProperty("proxy", "chisel:" + TEXTURES_FOLDER.getParent().relativize(newTexture).toString().replace('\\', '/'));
        metadata.add("ctm", ctm);
        Files.write(meta, Collections.singleton(GSON.toJson(metadata)));
    }
}
