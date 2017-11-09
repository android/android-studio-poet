package main.generators.project

import main.models.AndroidModuleBlueprint
import main.writers.FileWriter
import main.models.ModuleBlueprint
import main.utils.joinPath

private const val INCLUDE_LENGTH_LIMIT = 250
class GradleSettingsGenerator(private val fileWriter: FileWriter) {

    fun generate(projectName: String, moduleBlueprints: List<ModuleBlueprint>, androidModuleBlueprints: List<AndroidModuleBlueprint>, projectRoot: String) {
        val buff = StringBuilder()
        buff.append("rootProject.name = \'$projectName\'\n")
        buff.append(generateIncludeStatements(moduleBlueprints.map { it.name }))
        buff.append(generateIncludeStatements(androidModuleBlueprints.map { it.name }))

        fileWriter.writeToFile(buff.toString(), projectRoot.joinPath("settings.gradle"))
    }

    private fun generateIncludeStatements(moduleBlueprints: List<String>) =
            moduleBlueprints.asSequence()
                    .map { "\'$it\'" }
                    .foldIndexed("", { index, acc, next ->
                        acc + getIncludeStatementOrComma(index) + next
                    })

    private fun getIncludeStatementOrComma(index: Int): String {
        return if (index.rem(INCLUDE_LENGTH_LIMIT) == 0) {
            "\ninclude "
        } else ","
    }
}
