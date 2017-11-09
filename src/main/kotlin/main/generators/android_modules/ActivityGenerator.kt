package main.generators.android_modules

import main.GenerationResult
import main.models.AndroidModuleBlueprint
import main.writers.FileWriter
import java.io.File

class ActivityGenerator(var fileWriter: FileWriter) {

    /**
     * generates activity classes by blueprint, list of layouts and methods to call.
     */
    fun generate(blueprint: AndroidModuleBlueprint, layouts: List<String>,
                 methodsToCall: List<String>): ActivityGenerationResult {

        // generate activities
        var index = 0

        // TODO convert package names from . to /
        var where = blueprint.moduleRoot + "/src/main/java/" +
                "com/" + blueprint.packageName

        File(where).mkdirs()

        while (index < blueprint.numOfActivities) {
            generateClass(index, layouts[index], where,  "com." + blueprint.packageName)
            index++
        }

        // for manifest

        return ActivityGenerationResult(0.until(blueprint.numOfActivities).map { "Activity$it" })
    }

    private fun generateClass(activityIndex: Int, layout: String, where: String, packageName: String) {

        val className = "Activity" + activityIndex

        // TODO add methods
        // TODO move to java poet
        var classText = "package com.example.android.helloactivity;\n" +
                "\n" +
                "package " + packageName +";\n"
                "import android.app.Activity;\n" +
                "import android.os.Bundle;\n" +
                "\n" +
                "\n" +
                "public class " + className + " extends Activity {\n" +
                "    public HelloActivity() {\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Called with the activity is first created.\n" +
                "     */\n" +
                "    @Override\n" +
                "    public void onCreate(Bundle savedInstanceState) {\n" +
                "        super.onCreate(savedInstanceState);\n" +
                "\n" +
                "        // Set the layout for this activity.  You can find it\n" +
                "        // in res/layout/hello_activity.xml\n" +
                "        setContentView(R.layout." + layout + ");\n" +
                "    }\n" +
                "}\n"

        println("$where/$className.java")

        fileWriter.writeToFile(classText, "$where/$className.java")

    }

    data class ActivityGenerationResult(val activityNames: List<String>): GenerationResult
}

