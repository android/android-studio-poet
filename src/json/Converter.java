package json;

import com.google.gson.Gson;

public class Converter {

    public static final String SAMPLE_CONFIG = "{\n" +
            "  \"mainPackage\": \"/Users/bfarber/Development/java-generator/src\",\n" +
            "  \"allMethods\": \"4000\",\n" +
            "  \"javaPackageCount\": \"20\",\n" +
            "  \"javaClassCount\": \"8\",\n" +
            "  \"javaMethodCount\": \"2000\",\n" +
            "  \"kotlinPackageCount\": \"20\",\n" +
            "  \"kotlinClassCount\": \"8\"\n" +
            "}";


    public static void main(String[] args) {






        Gson gson = new Gson();
        ConfigPOJO obj2 = gson.fromJson(SAMPLE_CONFIG, ConfigPOJO.class);


        System.out.println(obj2);

        System.out.println(obj2.toJson());


    }
}
