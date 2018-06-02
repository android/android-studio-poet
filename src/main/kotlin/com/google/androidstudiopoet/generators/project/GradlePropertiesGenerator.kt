package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.models.GradlePropertiesBlueprint
import com.google.androidstudiopoet.writers.FileWriter

class GradlePropertiesGenerator(private val fileWriter: FileWriter) {
    fun generate(blueprint: GradlePropertiesBlueprint) {
        val fileBody = blueprint.properties?.entries
                ?.joinToString("\n") { it.key + "=" + it.value }

        fileBody?.let { fileWriter.writeToFile(fileBody, blueprint.path) }
    }
}