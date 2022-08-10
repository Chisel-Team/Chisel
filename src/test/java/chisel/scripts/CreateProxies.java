package chisel.scripts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class CreateProxies {
    
    private static final Path TEXTURES_FOLDER = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "block", "planks");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        for (Path block : Files.walk(TEXTURES_FOLDER).filter(Files::isDirectory).toArray(Path[]::new)) {
//            crop(block, "array", 2);
//            crop(block, "chaotic_bricks", 3);
//            crop(block, "cuts", 4);
//            crop(block, "jellybean", 2);
//            crop(block, "slanted", 2);
//            crop(block, "zag", "ctm");
//            crop(block, "abandoned", "ctmh");
//            crop(block, "brim", "ctmh");
//            crop(block, "cans", "ctmh");
//            crop(block, "historician", "ctmh");
//            crop(block, "hoarder", "ctmh");
//            crop(block, "necromancer-novice", "ctmh");
//            crop(block, "necromancer", "ctmh");
//            crop(block, "papers", "ctmh");
//            crop(block, "rainbow", "ctmh");
//            crop(block, "redtomes", "ctmh");
            crop(block, "crude_horizontal_planks", 3);
            crop(block, "crude_vertical_planks", 3);
        }
    }

    private static void crop(Path folder, String name, int size) throws IOException {
        crop(folder, name, size + "x" + size);
    }
    
    private static void crop(Path folder, String name, String suffix) throws IOException {
        Path texture = folder.resolve(name + ".png");
        String newName = name + "-" + suffix;
        Path newTexture = folder.resolve(newName + ".png");
        
        Path meta = folder.resolve(name + ".png.mcmeta");
        Path newMeta = folder.resolve(newName + ".png.mcmeta");
        
        if (!Files.exists(texture) || Files.exists(newTexture)) {
//            if (Files.exists(meta)) {
//                Files.delete(meta);
//                generateProxy(meta, newTexture);
//            }
            return; // Not a texture or already processed
        }
        
        Files.move(texture, newTexture);
        Files.move(meta, newMeta);
        
        BufferedImage img = ImageIO.read(newTexture.toFile());
        img = img.getSubimage(0, 0, 16, 16);
        ImageIO.write(img, "png", texture.toFile());

        generateProxy(meta, newTexture);
    }
    
    private static void generateProxy(Path meta, Path newTexture) throws IOException {
        JsonObject metadata = new JsonObject();
        JsonObject ctm = new JsonObject();
        ctm.addProperty("ctm_version", 1);
        ctm.addProperty("proxy", "chisel:" + TEXTURES_FOLDER.getParent().relativize(newTexture).toString().replace('\\', '/').replace(".png", ""));
        metadata.add("ctm", ctm);
        Files.write(meta, Collections.singleton(GSON.toJson(metadata)));
    }
}
