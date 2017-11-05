package main.generators.project

import main.writers.FileWriter
import main.models.ModuleBlueprint
import main.utils.joinPath

class GradleSettingsGenerator(private val fileWriter: FileWriter) {
    fun generate(projectName: String, moduleBlueprints: List<ModuleBlueprint>, projectRoot: String) {
        val buff = StringBuilder()
        buff.append("rootProject.name = \'$projectName\'\n")
        buff.append(moduleBlueprints.map { it.name }.map{"\'$it\'"}.joinToString(",", "include "))

        fileWriter.writeToFile(buff.toString(), projectRoot.joinPath("settings.gradle"))
    }
}
