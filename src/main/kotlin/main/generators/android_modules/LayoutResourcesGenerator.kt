package main.generators.android_modules

import main.models.AndroidModuleBlueprint
import main.utils.joinPath
import main.writers.FileWriter

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