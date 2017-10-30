package ui.generators.project

import ui.FileWriter
import ui.models.ModuleBlueprint
import utils.joinPath

class GradleSettingsGenerator(private val fileWriter: FileWriter) {
    fun generate(projectName: String, moduleBlueprints: List<ModuleBlueprint>, projectRoot: String) {
        val buff = StringBuilder()
        buff.append("rootProject.name = \'$projectName\'\n")
        buff.append(moduleBlueprints.map { it.name }.map{"\'$it\'"}.joinToString(",", "include "))

        fileWriter.writeToFile(buff.toString(), projectRoot.joinPath("settings.gradle"))
    }
}
