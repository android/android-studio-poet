/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.ModuleBlueprintFactory
import com.google.androidstudiopoet.converters.ConfigPojoToAndroidModuleConfigConverter
import com.google.androidstudiopoet.converters.ConfigPojoToBuildTypeConfigsConverter
import com.google.androidstudiopoet.converters.ConfigPojoToFlavourConfigsConverter
import com.google.androidstudiopoet.converters.ConfigPojoToModuleConfigConverter
import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.utils.joinPath
import kotlinx.coroutines.experimental.*
import kotlin.system.measureTimeMillis

class ProjectBlueprint(configPOJO: ConfigPOJO,
                       private val projectConfig: ProjectConfig) {

    val projectName = projectConfig.projectName

    val projectRoot = projectConfig.root.joinPath(projectName)

    val androidGradlePluginVersion = configPOJO.androidGradlePluginVersion
    val kotlinVersion = configPOJO.kotlinVersion
    val useKotlin = (projectConfig.pureModuleConfigs + projectConfig.androidModuleConfigs)
            .map { it.useKotlin }.find { it } ?: false
    val gradleVersion = configPOJO.gradleVersion!!

    val dependencies = configPOJO.dependencies ?: listOf()

    val moduleCount = projectConfig.pureModuleConfigs.size

    val moduleBlueprints : List<ModuleBlueprint>
    val androidModuleBlueprints : List<AndroidModuleBlueprint>
    val allModulesNames : List<String>

    init  {
        var temporaryModuleBlueprints : List<ModuleBlueprint> = listOf()
        val timeModels = measureTimeMillis {
            ModuleBlueprintFactory.initCache(moduleCount)
            runBlocking {
                val deferred = projectConfig.pureModuleConfigs.map {
                    async {
                        ModuleBlueprintFactory.create(it, projectRoot)
                    }
                }
                temporaryModuleBlueprints = deferred.map {it.await()}
            }
        }
        moduleBlueprints = temporaryModuleBlueprints
        println("Time to create model blueprints: $timeModels")

        var temporaryAndroidBlueprints : List<AndroidModuleBlueprint> = listOf()
        val timeAndroidModels = measureTimeMillis {
            temporaryAndroidBlueprints = (0 until projectConfig.androidModuleConfigs.size).map { i ->
                ModuleBlueprintFactory.createAndroidModule(projectRoot, projectConfig.pureModuleConfigs.map { it.index },
                        projectConfig.androidModuleConfigs[i],
                        (i + 1 until projectConfig.androidModuleConfigs.size).map { projectConfig.androidModuleConfigs[i] })
            }
        }
        androidModuleBlueprints = temporaryAndroidBlueprints
        println("Time to create Android model blueprints: $timeAndroidModels")

        allModulesNames = moduleBlueprints.map { it.name } + androidModuleBlueprints.map { it.name }
    }

    fun printDependencies() {
        println("digraph $projectName {")
        for (module in projectConfig.pureModuleConfigs) {
            val list = if (module.dependencies.isNotEmpty()) " -> ${module.dependencies.sorted().joinToString()}" else ""
            println("  ${module.index}$list;")
        }
        println("}")
    }

    fun hasCircularDependencies() : Boolean {
        // Try to find a topological order (it will be stored in withZero)
        val dependencyCounter : MutableList<Int> = mutableListOf()
        projectConfig.pureModuleConfigs.mapTo(dependencyCounter, {_ -> 0})
        // Count how many modules depend on each
        projectConfig.pureModuleConfigs.flatMap { it.dependencies }.forEach { dependencyCounter[it]++ }
        // Try to generate a topological order
        val topologicalOrder = dependencyCounter.withIndex().filter { pair -> pair.value == 0 }.map { it -> it.index }.toMutableList()
        var index = 0
        while (index < topologicalOrder.size && topologicalOrder.size < projectConfig.pureModuleConfigs.size) {
            val from = topologicalOrder[index]
            for (to in projectConfig.pureModuleConfigs[from].dependencies) {
                dependencyCounter[to]--
                if (dependencyCounter[to] == 0) {
                    topologicalOrder.add(to)
                }
            }
            index++
        }
        // No circular dependencies if and only if all modules can be sorted
        return topologicalOrder.size != projectConfig.pureModuleConfigs.size
    }
}
