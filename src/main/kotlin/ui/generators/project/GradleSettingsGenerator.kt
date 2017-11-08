package ui.generators.project

import ui.FileWriter
import ui.models.ModuleBlueprint
import utils.joinPath

private const val INCLUDE_LENGTH_LIMIT = 250
class GradleSettingsGenerator(private val fileWriter: FileWriter) {

    fun generate(projectName: String, moduleBlueprints: List<ModuleBlueprint>, projectRoot: String) {
        val buff = StringBuilder()
        buff.append("rootProject.name = \'$projectName\'\n")
        buff.append(generateIncludeStatements(moduleBlueprints))

        fileWriter.writeToFile(buff.toString(), projectRoot.joinPath("settings.gradle"))
    }

    private fun generateIncludeStatements(moduleBlueprints: List<ModuleBlueprint>) =
            moduleBlueprints.asSequence()
                    .map { it.name }
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
