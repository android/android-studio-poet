package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.GenerationResult
import com.google.androidstudiopoet.Generator
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.utils.fold
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class StringResourcesGenerator(private val fileWriter: FileWriter): Generator<AndroidModuleBlueprint, StringResourceGenerationResult> {

    /**
     * generates string resources by blueprint, returns list of string names to refer later.
     * Precondition: resources package is generated
     */
    override fun generate(blueprint: AndroidModuleBlueprint): StringResourceGenerationResult {
        val valuesDirPath = blueprint.resDirPath.joinPath("values")
        fileWriter.mkdir(valuesDirPath)
        val stringsFileContent = getFileContent(blueprint.stringNames)

        fileWriter.writeToFile(stringsFileContent, valuesDirPath.joinPath("strings.xml"))
        return StringResourceGenerationResult(blueprint.stringNames)
    }

    private fun getFileContent(stringNames: List<String>): String {
        val stringsFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><resources>" +
                stringNames.map { "<string name=\"$it\">$it</string>\n" }.fold() +
                "</resources>"
        return stringsFileContent
    }
}

// TODO remove the class below after refactoring the generators

data class StringResourceGenerationResult(val stringNames: List<String>): GenerationResult