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

import com.google.androidstudiopoet.models.ActivityBlueprint
import com.google.androidstudiopoet.writers.FileWriter

class ActivityGenerator(var fileWriter: FileWriter) {

    fun generate(blueprint: ActivityBlueprint) {
        val classText =
                "package ${blueprint.packageName};\n" +
                        "import android.app.Activity;\n" +
                        "import android.os.Bundle;\n" +
                        "import ${blueprint.packageName}.R;\n" +
                        "\n" +
                        "\n" +
                        "public class " + blueprint.className + " extends Activity {\n" +
                        "    public ${blueprint.className}() {\n" +
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
                        "        setContentView(R.layout." + blueprint.layout + ");\n" +
                        "    }\n" +
                        "}\n"
        println("${blueprint.where}/${blueprint.className}.java")
        fileWriter.writeToFile(classText, "${blueprint.where}/${blueprint.className}.java")

    }

}

