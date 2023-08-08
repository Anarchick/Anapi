package fr.anarchick.anapi;

import fr.anarchick.anapi.java.FileUtils;
import fr.anarchick.anapi.java.Pair;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AssetsToJson {

    private final File folder;
    private String TEMPLATE = "{\n" +
            "\t\"parent\": \"PARENT\",\n" +
            "\t\"textures\": {\n" +
            "\t\t\"0\": \"ASSET##\"\n" +
            "\t}\n" +
            "}";

    public AssetsToJson(File folder) {
        this.folder = folder;
    }

    public void setTemplate(String json) {
        this.TEMPLATE = json;
    }

    public List<File> getAssets() {
        if (!folder.isDirectory()) throw new IllegalArgumentException("Folder must be a directory");
        LinkedList<File> assets = new LinkedList();
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                try {
                    if (ImageIO.read(file) != null) {
                        assets.add(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(assets, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        return assets;
    }

    public String getJson(List<Pair<String, String>> pairs) {
        String json = new String(TEMPLATE);
        for (Pair<String, String> pair : pairs) {
            json = json.replace(pair.first(), pair.second());
        }
        return json;
    }

    public void generate(@Nonnull File outputFolder, boolean erase, List<Pair<String, String>> pairs) throws IOException {
        if (outputFolder.exists() && !erase) {
            throw new IOException("Cannot replace existing output file");
        }
        final String originalJson = getJson(pairs);
        for (File assetFile : getAssets()) {
            String fileName = FileUtils.getFileNameWithoutExtension(assetFile);
            String json = new String(originalJson);
            json = json.replace("##", fileName);
            File outputFile = new File(outputFolder, fileName+".json");
            FileUtils.save(outputFile, json);
        }
    }

    public static void test() {
        final File outputFolder = new File("C:\\Users\\aeim\\Documents\\minecraft\\avatar\\resourcepack\\avatar\\assets\\minecraft\\models\\custom\\spellcast\\fire\\circle\\");
        final File assetsFolder = new File("C:\\Users\\aeim\\Documents\\minecraft\\avatar\\resourcepack\\avatar\\assets\\minecraft\\textures\\custom\\spellcast\\fire\\circle\\");
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<String, String>("PARENT", "custom/spellcast/fire/circle/fire_circle"));
        pairs.add(new Pair<String, String>("ASSET", "custom/spellcast/fire/circle/"));
        AssetsToJson atj = new AssetsToJson(assetsFolder);
        try {
            atj.generate(outputFolder, true, pairs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
