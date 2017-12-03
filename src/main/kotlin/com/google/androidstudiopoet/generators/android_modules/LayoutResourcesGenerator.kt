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

import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class LayoutResourcesGenerator(val fileWriter: FileWriter) {

    /**
     * generates layout resources by blueprint and other resources, returns list of layout names to refer later.
     */
    fun generate(blueprint: AndroidModuleBlueprint,
                 stringResources: List<String>,
                 imageResources: List<String>): List<String> {

        var result: ArrayList<String> = ArrayList()

        val layoutsDir = blueprint.resDirPath.joinPath("layout")
        fileWriter.mkdir(layoutsDir)

        for (i in 0 until blueprint.numOfImages) {

            var layoutText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<android.support.constraint.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                    "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                    "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                    "    android:layout_width=\"match_parent\"\n" +
                    "    android:layout_height=\"match_parent\">\n" +
                    "\n" +
                    "    <TextView\n" +
                    "        android:layout_width=\"wrap_content\"\n" +
                    "        android:layout_height=\"wrap_content\"\n" +
                    "        android:text=\"Hello World!\"\n" +
                    "        app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                    "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                    "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
                    "        app:layout_constraintTop_toTopOf=\"parent\" />\n" +
                    "\n" +
                    " <ImageView\n" +
                    "         android:layout_width=\"wrap_content\"\n" +
                    "         android:layout_height=\"wrap_content\"\n" +
                    "         android:src=\"@drawable/" + imageResources[i] + "\"\n"+
            "         />\n" +
                    "</android.support.constraint.ConstraintLayout>"
            var fileName = "activity_main$i.xml"
            fileWriter.writeToFile(layoutText, layoutsDir.joinPath(fileName))
            result.add("activity_main$i")
        }

        return result
    }
}