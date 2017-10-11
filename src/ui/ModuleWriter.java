package ui;

import com.google.gson.Gson;
import java.io.File;

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


    }

    private void writeLibsFolder(ConfigPOJO configPOJO) {
        // write libs
        String libRoot = configPOJO.getRoot() + "/libs/";
        new File(libRoot).mkdir();
    }

    private void writeRootFolder(ConfigPOJO configPOJO) {
        File root = new File(configPOJO.getRoot());

        if(!root.exists()) {
            root.mkdir();
        } else {
            root.delete();
            root.mkdir();
        }
    }
}
