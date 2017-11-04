package ui.generators.android_modules

import ui.FileWriter
import ui.models.AndroidModuleBlueprint
import utils.joinPath

class StringResourcesGenerator(private val fileWriter: FileWriter) {

    /**
     * generates string resources by blueprint, returns list of string names to refer later.
     * Precondition: resources package is generated
     */
    fun generate(blueprint: AndroidModuleBlueprint): List<String> {
        val valuesDirPath = blueprint.resDirPath.joinPath("values")
        fileWriter.mkdir(valuesDirPath)

        val stringNames = (0..blueprint.numOfStrings).map { "${blueprint.name}string{$it}" }
        val stringsFileContent = getFileContent(stringNames)

        fileWriter.writeToFile(stringsFileContent, valuesDirPath.joinPath("strings.xml"))
        return stringNames
    }

    private fun getFileContent(stringNames: List<String>): String {
        val stringsFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resources>" +
                stringNames.map { "<string name=\"$it\">$it</string>\n" }.fold("") { acc, next -> acc + next } +
                "</resources>"
        return stringsFileContent
    }
}
