package main.generators.android_modules

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import main.GenerationResult
import main.models.AndroidModuleBlueprint
import java.io.File
import javax.lang.model.element.Modifier

class ActivityGenerator {

    /**
     * generates activity classes by blueprint, list of layouts and methods to call.
     */
    fun generate(blueprint: AndroidModuleBlueprint, layouts: List<String>, methodsToCall: List<String>): ActivityGenerationResult {
        return ActivityGenerationResult(0.until(blueprint.numOfActivities).map { "Activity$it" })
    }

    fun generateClass(activityNumber: Int) {

        val className = "Activity" + activityNumber

        //TODO move to java poet
        var classText = "package com.example.android.helloactivity;\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "import android.os.Bundle;\n" +
                "\n" +
                "\n" +
                "public class HelloActivity extends Activity {\n" +
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
                "        setContentView(R.layout.hello_activity);\n" +
                "    }\n" +
                "}\n"






    }



}

    data class ActivityGenerationResult(val activityNames: List<String>): GenerationResult
}

