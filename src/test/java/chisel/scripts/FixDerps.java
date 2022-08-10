package chisel.scripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FixDerps {
    private static final Path TEXTURES_FOLDER = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "block");
    
    public static void main(String[] args) throws IOException {
        Files.walk(TEXTURES_FOLDER)
            .filter(Files::isRegularFile)
            .filter(p -> p.getFileName().toString().matches("(\\w+)-\\1.*\\.png(?:\\.mcmeta)?"))
            .forEach(t -> {
                try {
                    System.out.println(t);
                    Files.delete(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

}
