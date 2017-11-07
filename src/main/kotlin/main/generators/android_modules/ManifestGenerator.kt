package main.generators.android_modules

import main.models.AndroidModuleBlueprint
import main.utils.fold
import main.utils.joinPath
import main.writers.FileWriter

class ManifestGenerator(private val fileWriter: FileWriter) {

    /**
     * generates manifest by blueprint and list of activity names
     */
    fun generate(blueprint: AndroidModuleBlueprint, activityGenerationResult: ActivityGenerationResult) {
        val manifestContent = generateManifestTag(blueprint.packageName, generateApplicationTag(activityGenerationResult, blueprint.hasLaunchActivity))
        fileWriter.writeToFile(manifestContent, blueprint.mainPath.joinPath("Manifest.xml"))
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

    private fun generateApplicationTag(activityGenerationResult: ActivityGenerationResult, hasLaunchActivity: Boolean): String {
        return "<application>\n ${generateActivityTags(activityGenerationResult.activityNames, hasLaunchActivity)} </application>\n"
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