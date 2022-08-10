package chisel.scripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class TextureRenamer {

    public static void main(String[] args) throws IOException {
        Path texturesFolder = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "block");

        Files.walk(texturesFolder)
            .filter(p -> p.toString().endsWith(".png") || p.toString().endsWith(".png.mcmeta"))
            .filter(p -> p.getFileName().toString().contains("-"))
            .forEach(p -> {
                Path output = p.getParent().resolve(migrate(p.getFileName().toString()));
                if (output.toString().endsWith(".mcmeta")) {
                    try {
                        List<String> lines = Files.readAllLines(p)
                                .stream()
                                .map(TextureRenamer::migrate)
                                .collect(Collectors.toList());
                        Files.write(output, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        p.toFile().delete();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    p.toFile().renameTo(output.toFile());
                }
            });
    }
    
    private static String migrate(String name) {
        return name.replaceAll("-(?!ctm|side|top|bottom)", "_").replaceAll("bricks_([a-z]+)", "$1_bricks");
    }
}
