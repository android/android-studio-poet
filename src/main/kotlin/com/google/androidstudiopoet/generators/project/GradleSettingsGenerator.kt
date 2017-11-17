package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import com.google.androidstudiopoet.models.ModuleBlueprint
import com.google.androidstudiopoet.utils.joinPath

private const val INCLUDE_LENGTH_LIMIT = 250
class GradleSettingsGenerator(private val fileWriter: FileWriter) {

    fun generate(projectName: String, allModulesNames: List<String>, projectRoot: String) {
        val buff = StringBuilder()
        buff.append("rootProject.name = \'$projectName\'\n")
        buff.append(generateIncludeStatements(allModulesNames))

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
