package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.GenerationResult
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

class ActivityGenerator(var fileWriter: FileWriter) {

    /**
     * generates activity classes by blueprint, list of layouts and methods to call.
     */
    fun generate(blueprint: AndroidModuleBlueprint, methodsToCall: List<String>) {

        // generate activities
        var index = 0

        File(blueprint.packagePath).mkdirs()

        while (index < blueprint.numOfActivities) {
            generateClass(blueprint.activityNames[index], blueprint.layoutNames[index], blueprint.packagePath, blueprint.packageName)
            index++
        }
    }

    private fun generateClass(className: String, layout: String, where: String, packageName: String) {

        // TODO add methods
        // TODO move to java poet
        val classText =
                "package $packageName;\n" +
                        "import android.app.Activity;\n" +
                        "import android.os.Bundle;\n" +
                        "import $packageName.R;\n" +
                        "\n" +
                        "\n" +
                        "public class " + className + " extends Activity {\n" +
                        "    public $className() {\n" +
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

    data class ActivityGenerationResult(val activityNames: List<String>) : GenerationResult
}

