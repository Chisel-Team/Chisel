import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class Script {

    public static void main(String[] args) {
        File assets = Paths.get("src", "main", "resources", "assets", "chisel").toFile();
        System.out.println(assets.getAbsolutePath());
        Iterator<File> allFiles = FileUtils.iterateFiles(assets, new String[]{ "png" }, true);
        while (allFiles.hasNext()) {
            File f = allFiles.next();
            f.renameTo(new File(f.getParentFile().getAbsolutePath(), f.getName().toLowerCase()));
        }
    }

}
