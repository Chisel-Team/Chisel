package chisel.scripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

public class RenamePlanks {
    private static final Path TEXTURES_FOLDER = Paths.get("src", "main", "resources", "assets", "chisel", "textures", "block", "planks");
    
    private static final ImmutableMap<String, String> RENAMES = ImmutableMap.<String, String>builder()
//            .put("blinds", "stacked")
//            .put("chaotic", "crude_horizontal")
//            .put("chaotic-hor", "crude_vertical")
//            .put("clean", "smooth")
//            .put("crate", "encased_planks")
//            .put("crate-fancy", "encased_large_planks")
//            .put("crateex", "crate")
//            .put("double", "braced")
//            .put("fancy", "paneling")
//            .put("large", "large_planks")
//            .put("panel-nails", "encased_smooth")
//            .put("parquet", "braid")
//            .put("short", "log_cabin")
//            .put("vertical", "vertical_planks")
//            .put("vertical-uneven", "crude_paneling")
///            .put("crate", "shipping_crate")
///            .put("braced", "facade")
////            .put("facade", "braced_planks")
            .put("crude_horizontal", "crude_horizontal_planks")
            .put("crude_vertical", "crude_vertical_planks")
            .build();
    
    private static final Pattern NAME_PATTERN = Pattern.compile(RENAMES.keySet().stream().collect(Collectors.joining("|", "(", ")")));
    
    public static void main(String[] args) throws IOException {
        Files.walk(TEXTURES_FOLDER)
            .filter(Files::isRegularFile)
            .filter(p -> NAME_PATTERN.matcher(p.getFileName().toString()).find()).forEach(p -> {
                for (Entry<String, String> e : RENAMES.entrySet()) {
                    if (p.getFileName().toString().matches(e.getKey() + "(-(top|side|ew|ns|tb))?(-ctm)?\\.png(\\.mcmeta)?")) {
                        try {
                            p = Files.move(p, p.resolveSibling(p.getFileName().toString().replace(e.getKey(), e.getValue())));
                            if (p.getFileName().toString().endsWith(".mcmeta")) {
                                Files.write(p, Files.readAllLines(p).stream()
                                        .map(s -> s.replace(e.getKey(), e.getValue()))
                                        .collect(Collectors.toList()), StandardOpenOption.TRUNCATE_EXISTING);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            });
    }

}
