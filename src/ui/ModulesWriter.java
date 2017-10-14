package ui;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModulesWriter {

    public ModulesWriter(PackagesGeneratorUI packagesGeneratorUI) {
    }

    public void generate(String configStr) {

        Gson gson = new Gson();
        ConfigPOJO configPOJO = gson.fromJson(configStr, ConfigPOJO.class);

        writeRootFolder(configPOJO);

        for (int i = 0; i < Integer.parseInt(configPOJO.getNumModules()); i++) {
            writeModule(i, configPOJO);
        }
    }

    private void writeModule(int index, ConfigPOJO configPOJO) {
        String moduleRoot = configPOJO.getRoot() + "/module" + index +"/";
        File moduleRootFile = new File(moduleRoot);
        moduleRootFile.mkdir();

        writeLibsFolder(moduleRootFile, configPOJO);
        writeBuildGradle(moduleRootFile,configPOJO);

        PackagesWriter packagesWriter = new PackagesWriter();
        packagesWriter.writePackages(configPOJO,
                moduleRoot + "/src/main/java/");

    }

    private void writeBuildGradle(File moduleRootFile, ConfigPOJO configPOJO) {
        String libRoot = moduleRootFile + "/build.gradle/";
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

    private void writeLibsFolder(File moduleRootFile, ConfigPOJO configPOJO) {
        // write libs
        String libRoot = moduleRootFile + "/libs/";
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
