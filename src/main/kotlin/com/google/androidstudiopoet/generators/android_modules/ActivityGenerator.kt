/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.GenerationResult
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

class ActivityGenerator(var fileWriter: FileWriter) {

    /**
     * generates activity classes by blueprint, list of layouts and methods to call.
     */
    fun generate(blueprint: AndroidModuleBlueprint, layouts: List<String>,
                 methodsToCall: List<String>): ActivityGenerationResult {

        // generate activities
        var index = 0

        File(blueprint.packagePath).mkdirs()

        while (index < blueprint.numOfActivities) {
            generateClass(index, layouts[index], blueprint.packagePath,  blueprint.packageName)
            index++
        }

        // for manifest

        return ActivityGenerationResult(0.until(blueprint.numOfActivities).map { "Activity$it" })
    }

    private fun generateClass(activityIndex: Int, layout: String, where: String, packageName: String) {

        val className = "Activity" + activityIndex

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

    data class ActivityGenerationResult(val activityNames: List<String>): GenerationResult
}

