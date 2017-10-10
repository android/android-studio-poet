package ui;

import com.google.gson.Gson;
import json.ConfigPOJO;

public class Controller {

    public static final String SAMPLE_CONFIG = "{\n" +
            "  \"mainPackage\": \"/Users/bfarber/Development/java-generator/src\",\n" +
            "  \"allMethods\": \"4000\",\n" +
            "  \"javaPackageCount\": \"20\",\n" +
            "  \"javaClassCount\": \"8\",\n" +
            "  \"javaMethodCount\": \"2000\",\n" +
            "  \"kotlinPackageCount\": \"20\",\n" +
            "  \"kotlinClassCount\": \"8\"\n" +
            "}";

    public Controller(PackagesGeneratorUI packagesGeneratorUI) {

    }

    public void generate(String configStr) {

        Gson gson = new Gson();
        ConfigPOJO obj2 = gson.fromJson(configStr, ConfigPOJO.class);

        PackagesGenerator packagesGenerator = new PackagesGenerator();
        packagesGenerator.writePackages(obj2);

    }
}
