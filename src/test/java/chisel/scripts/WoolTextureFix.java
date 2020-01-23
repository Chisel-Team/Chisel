package chisel.scripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraft.item.DyeColor;

public class WoolTextureFix {

    public static void main(String[] args) throws IOException {
        Path texturesFolder = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "block", "wool");
        
        for (DyeColor color : DyeColor.values()) {
            Path outputFolder = texturesFolder.resolve(color.getName());
            outputFolder.toFile().mkdirs();
            Files.move(texturesFolder.resolve("legacy").resolve(color.getName().replace("_", "") + ".png"), outputFolder.resolve("legacy.png"));
            Files.move(texturesFolder.resolve("legacy").resolve(color.getName().replace("_", "") + "-ctm.png"), outputFolder.resolve("legacy-ctm.png"));
            Files.move(texturesFolder.resolve("llama").resolve(color.getName().replace("_", "") + ".png"), outputFolder.resolve("llama.png"));
            Files.move(texturesFolder.resolve("llama").resolve(color.getName().replace("_", "") + "-ctm.png"), outputFolder.resolve("llama-ctm.png"));
        }
    }
}
