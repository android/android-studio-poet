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
import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.utils.decrease
import com.google.androidstudiopoet.utils.increase
import com.google.androidstudiopoet.utils.joinPath
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

class ProjectBlueprint(configPOJO: ConfigPOJO,
                       private val projectConfig: ProjectConfig) {

    val projectName = projectConfig.projectName

    val projectRoot = projectConfig.root.joinPath(projectName)

    val androidGradlePluginVersion = projectConfig.buildSystemConfig.agpVersion
    val kotlinVersion = projectConfig.buildSystemConfig.kotlinVersion
    val useKotlin = (projectConfig.pureModuleConfigs + projectConfig.androidModuleConfigs)
            .map { it.useKotlin }.find { it } ?: false
    val gradleVersion = projectConfig.buildSystemConfig.version!!

    val moduleBlueprints : List<ModuleBlueprint>
    val androidModuleBlueprints : List<AndroidModuleBlueprint>
    val allModulesNames : List<String>
    val generateTests = configPOJO.generateTests
    private val allDependencies = configPOJO.resolvedDependencies

    init  {
        var temporaryModuleBlueprints : List<ModuleBlueprint> = listOf()
        val timeModels = measureTimeMillis {
            ModuleBlueprintFactory.initCache()
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
            temporaryAndroidBlueprints = (0 until configPOJO.androidModules).map { i ->
                ModuleBlueprintFactory.createAndroidModule(projectRoot, projectConfig.androidModuleConfigs[i])
            }
        }
        androidModuleBlueprints = temporaryAndroidBlueprints
        println("Time to create Android model blueprints: $timeAndroidModels")

        allModulesNames = moduleBlueprints.map { it.name } + androidModuleBlueprints.map { it.name }
    }

    fun printDependencies() {
        println("digraph $projectName {")
        for (module in allModulesNames.sorted()) {
            var list = ""
	    val dependencies = allDependencies[module]
	    if ((dependencies != null) && (dependencies.isNotEmpty())) {
	        list = " -> ${dependencies.map { dep -> dep.to }.sorted().joinToString()}"
	    }
            println("  $module$list;")
        }
        println("}")
    }

    fun hasCircularDependencies() : Boolean {
        // Try to find a topological order (it will be stored in topologicalOrder)
        val dependencyCounter : MutableMap<String, Int> = mutableMapOf()
        for (moduleName in allModulesNames) {
            dependencyCounter.put(moduleName, 0)
        }
        // Count how many modules depend on each
        for ((_, dependencies) in allDependencies) {
            for (dependency in dependencies) {
                dependencyCounter.increase(dependency.to)
            }
        }
        // Try to generate a topological order
        val topologicalOrder = dependencyCounter.filter { (_, counter) -> counter == 0 }.map { it -> it.key }.toMutableList()
        var index = 0
        while (index < topologicalOrder.size && topologicalOrder.size < allModulesNames.size) {
            val from = topologicalOrder[index]
            for (dependency in allDependencies[from]!!) {
                val count = dependencyCounter.decrease(dependency.to)
                if (count == 0) {
                    topologicalOrder.add(dependency.to)
                }
            }
            index++
        }
        // No circular dependencies if and only if all modules can be sorted
        return topologicalOrder.size != allModulesNames.size
    }
}
