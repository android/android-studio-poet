package ui;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModuleWriter {

    public ModuleWriter(PackagesGeneratorUI packagesGeneratorUI) {
    }

    public void generate(String configStr) {

        Gson gson = new Gson();
        ConfigPOJO configPOJO = gson.fromJson(configStr, ConfigPOJO.class);

        writeRootFolder(configPOJO);
        writeLibsFolder(configPOJO);
        writeBuildGradle(configPOJO);


        PackagesWriter packagesWriter = new PackagesWriter();
        packagesWriter.writePackages(configPOJO,
                configPOJO.getRoot() + "/src/main/java/");
    }

    private void writeBuildGradle(ConfigPOJO configPOJO) {
        String libRoot = configPOJO.getRoot() + "/build.gradle/";
        BufferedWriter writer = null;
        try {
            File buildGradle = new File(libRoot);
            buildGradle.createNewFile();

            writer = new BufferedWriter(new FileWriter(buildGradle));
            writer.write(BuildGradle.TEXT);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeLibsFolder(ConfigPOJO configPOJO) {
        // write libs
        String libRoot = configPOJO.getRoot() + "/libs/";
        new File(libRoot).mkdir();
    }

    private void writeRootFolder(ConfigPOJO configPOJO) {
        File root = new File(configPOJO.getRoot());

        if (!root.exists()) {
            root.mkdir();
        } else {
            root.delete();
            root.mkdir();
        }
    }
}
