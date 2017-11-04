package ui.generators.android_modules

import ui.writers.FileWriter
import ui.models.AndroidModuleBlueprint
import utils.joinPath

class ProguardGenerator(val fileWriter: FileWriter) {
    fun generate(blueprint: AndroidModuleBlueprint) {
        val moduleRoot = blueprint.moduleRoot

        val proguardText = "# Add project specific ProGuard rules here.\n" +
                "# You can control the set of applied configuration files using the\n" +
                "# proguardFiles setting in build.gradle.\n" +
                "#\n" +
                "# For more details, see\n" +
                "#   http://developer.android.com/guide/developing/tools/proguard.html\n" +
                "\n" +
                "# If your project uses WebView with JS, uncomment the following\n" +
                "# and specify the fully qualified class name to the JavaScript interface\n" +
                "# class:\n" +
                "#-keepclassmembers class fqcn.of.javascript.interface.for.webview {\n" +
                "#   public *;\n" +
                "#}\n" +
                "\n" +
                "# Uncomment this to preserve the line number information for\n" +
                "# debugging stack traces.\n" +
                "#-keepattributes SourceFile,LineNumberTable\n" +
                "\n" +
                "# If you keep the line number information, uncomment this to\n" +
                "# hide the original source file name.\n" +
                "#-renamesourcefileattribute SourceFile"


        fileWriter.writeToFile(proguardText, moduleRoot.joinPath("proguard-rules.pro"))
    }
}
