package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.models.*
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

class DependencyGraphGenerator(private val fileWriter: FileWriter, private val dependencyImageGenerator: DependencyImageGenerator) {

    fun generate(projectBlueprint: ProjectBlueprint) {
        val dependenciesGraphFileName = projectBlueprint.projectRoot.joinPath("dependencies.dot")
        fileWriter.writeToFile(dependenciesGraphString(projectBlueprint), dependenciesGraphFileName)
        println("Dependency graph written to $dependenciesGraphFileName")
        dependencyImageGenerator.generate(projectBlueprint)
    }

    private fun dependenciesGraphString(projectBlueprint: ProjectBlueprint) = projectBlueprint.allModuleBlueprints.joinToString("\n", "digraph ${projectBlueprint.projectName} {\n", "\n}") {
        getDependencyForModuleAsString(it.name, projectBlueprint.allDependencies[it.name]?: listOf())
    }

    private fun getDependencyForModuleAsString(name: String, dependencies: List<ModuleDependency>): String {
        var list = ""
        if (dependencies.isNotEmpty()) {
            list = " -> ${dependencies.map { it.name }.sorted().joinToString()}"
        }

        return "  $name$list;"
    }

}