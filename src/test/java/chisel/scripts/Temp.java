package chisel.scripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Joiner;

public class Temp {
    
    public static void main(String[] args) throws IOException {
        
        Path assets = Paths.get("src", "main", "resources", "assets", "chisel");
        Path target = assets.resolve("blockstates").resolve("glasspane");
        Path textures = assets.resolve("textures").resolve("blocks").resolve("glasspane");
        
        Set<String> processedNames = new HashSet<>();

        String templateBlockstate = Joiner.on('\n').join(Files.readAllLines(Paths.get("template_blockstate.json")));
        String templateNoside = Joiner.on('\n').join(Files.readAllLines(Paths.get("template_pane_noside.json")));
        String templateSide = Joiner.on('\n').join(Files.readAllLines(Paths.get("template_pane_side.json")));
        String templatePost = Joiner.on('\n').join(Files.readAllLines(Paths.get("template_pane_post.json")));

        Files.walk(textures).forEach(p -> {
            String fname = p.toFile().getName();
            int lastdash = fname.lastIndexOf('-');
            String name = fname.substring(0, lastdash);
            String type = fname.substring(lastdash + 1, name.length());
            
            if (processedNames.contains(name)) {
                return;
            }
            processedNames.add(name);
            
            File[] textureFiles = new File[2];
            if (type.equals("side")) {
                textureFiles[0] = p.toFile();
                textureFiles[1] = p.getParent().resolve(name + "-top").toFile();
            } else {
                textureFiles[1] = p.toFile();
                textureFiles[0] = p.getParent().resolve(name + "-side").toFile();
            }
            
            

            File folder = p.getParent().resolve(color).toFile();
            if (!folder.exists()) {
                folder.mkdir();
            }
            File newLoc = p.getParent().resolve(color).resolve(name.replace(color + "-", "")).toFile();
            try {
                FileUtils.copyFile(p.toFile(), newLoc);
                p.toFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
    }

}
