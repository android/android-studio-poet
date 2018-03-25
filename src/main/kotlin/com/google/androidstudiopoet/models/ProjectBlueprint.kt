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

import com.google.androidstudiopoet.DEFAULT_AGP_VERSION
import com.google.androidstudiopoet.DEFAULT_KOTLIN_VERSION
import com.google.androidstudiopoet.ModuleBlueprintFactory
import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.utils.decrease
import com.google.androidstudiopoet.utils.increase
import com.google.androidstudiopoet.utils.joinPath
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

class ProjectBlueprint(private val projectConfig: ProjectConfig) {

    val projectName = projectConfig.projectName

    val projectRoot = projectConfig.root.joinPath(projectName)

    val androidGradlePluginVersion = projectConfig.buildSystemConfig.agpVersion ?: DEFAULT_AGP_VERSION
    val kotlinVersion = projectConfig.buildSystemConfig.kotlinVersion ?: DEFAULT_KOTLIN_VERSION
    val useKotlin = projectConfig.moduleConfigs.map { it.useKotlin }.find { it } ?: false
    val gradleVersion = projectConfig.buildSystemConfig.buildSystemVersion!!

    val moduleBlueprints: List<ModuleBlueprint>
    val androidModuleBlueprints: List<AndroidModuleBlueprint>
    private val allModuleBlueprints: List<AbstractModuleBlueprint>
    val allModulesNames: List<String>
    private val allDependencies: Map<String, List<ModuleDependency>>

    val buildGradleBlueprint = ProjectBuildGradleBlueprint(projectRoot)

    init {
        var temporaryModuleBlueprints: List<ModuleBlueprint> = listOf()
        val timeModels = measureTimeMillis {
            ModuleBlueprintFactory.initCache()
            runBlocking {
                val deferred = projectConfig.pureModuleConfigs.map {
                    async {
                        ModuleBlueprintFactory.create(it, projectRoot)
                    }
                }
                temporaryModuleBlueprints = deferred.map { it.await() }
            }
        }
        moduleBlueprints = temporaryModuleBlueprints
        println("Time to create model blueprints: $timeModels")

        var temporaryAndroidBlueprints: List<AndroidModuleBlueprint> = listOf()
        val timeAndroidModels = measureTimeMillis {
            temporaryAndroidBlueprints = projectConfig.androidModuleConfigs.map {
                ModuleBlueprintFactory.createAndroidModule(projectRoot, it, projectConfig.moduleConfigs)
            }
        }
        androidModuleBlueprints = temporaryAndroidBlueprints
        println("Time to create Android model blueprints: $timeAndroidModels")
        allModuleBlueprints = androidModuleBlueprints + moduleBlueprints
        allModulesNames = allModuleBlueprints.map { it.name }
        allDependencies = allModuleBlueprints.associate { it -> Pair(it.name, it.moduleDependencies) }
    }

    fun printDependencies() {
        println(allModuleBlueprints.joinToString("\n", "digraph $projectName {\n", "\n}") {
            getDependencyForModuleAsString(it.name, it.moduleDependencies)
        })
    }

    private fun getDependencyForModuleAsString(name: String, dependencies: List<ModuleDependency>): String {
        var list = ""
        if (dependencies.isNotEmpty()) {
            list = " -> ${dependencies.map { it.name }.sorted().joinToString()}"
        }

        return "  $name$list;"
    }

    fun hasCircularDependencies(): Boolean {
        // Try to find a topological order (it will be stored in topologicalOrder)
        val dependencyCounter: MutableMap<String, Int> = mutableMapOf()
        for (moduleName in allModulesNames) {
            dependencyCounter.put(moduleName, 0)
        }
        // Count how many modules depend on each
        for ((_, dependencies) in allDependencies) {
            for (dependency in dependencies) {
                dependencyCounter.increase(dependency.name)
            }
        }
        // Try to generate a topological order
        val topologicalOrder = dependencyCounter.filter { (_, counter) -> counter == 0 }.map { it -> it.key }.toMutableList()
        var index = 0
        while (index < topologicalOrder.size && topologicalOrder.size < allModulesNames.size) {
            val from = topologicalOrder[index]
            for (dependency in allDependencies[from]!!) {
                val count = dependencyCounter.decrease(dependency.name)
                if (count == 0) {
                    topologicalOrder.add(dependency.name)
                }
            }
            index++
        }
        // No circular dependencies if and only if all modules can be sorted
        return topologicalOrder.size != allModulesNames.size
    }
}
