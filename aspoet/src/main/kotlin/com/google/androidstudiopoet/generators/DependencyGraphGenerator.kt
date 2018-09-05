/*
 *  Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */


package com.google.androidstudiopoet.generators

import com.google.androidstudiopoet.models.ModuleDependency
import com.google.androidstudiopoet.models.ProjectBlueprint
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