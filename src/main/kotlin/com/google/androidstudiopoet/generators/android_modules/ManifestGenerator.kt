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
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class ManifestGenerator(private val fileWriter: FileWriter) {

    /**
     * generates manifest by blueprint and list of activity names
     */
    fun generate(blueprint: AndroidModuleBlueprint) {
        val manifestContent = generateManifestTag(blueprint.packageName, generateApplicationTag(blueprint.activityNames, blueprint.hasLaunchActivity))
        fileWriter.writeToFile(manifestContent, blueprint.mainPath.joinPath("AndroidManifest.xml"))
    }

    private fun generateManifestTag(packageName: String, applicationTag: String): String {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    package=\"$packageName\">\n" +
                "\n" +
                "    <uses-permission android:name=\"android.permission.INTERNET\" />\n" +
                "\n" +
                applicationTag +
                "\n" +
                "</manifest>"
    }

    private fun generateApplicationTag(activityNames: List<String>, hasLaunchActivity: Boolean): String {
        return "<application>\n ${generateActivityTags(activityNames, hasLaunchActivity)} </application>\n"
    }

    private fun generateActivityTags(activityNames: List<String>, hasLaunchActivity: Boolean): String {
        return activityNames.mapIndexed({ index, activityName ->
                    if (index == 0 && hasLaunchActivity) generateLaunchActivityTag(activityName)
                    else generateActivityTag(activityName)
                }).fold()
    }

    private fun generateLaunchActivityTag(activityName: String): String {
        return "        <activity android:name=\".$activityName\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.MAIN\" />\n" +
                "\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\" />\n" +
                "            </intent-filter>\n" +
                "        </activity>\n"
    }

    private fun generateActivityTag(activityName: String): String {
        return "        <activity android:name=\".$activityName\">\n" +
                "        </activity>\n"
    }

}