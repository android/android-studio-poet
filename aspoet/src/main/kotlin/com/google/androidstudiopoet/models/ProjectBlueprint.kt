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
import com.google.androidstudiopoet.DEFAULT_GRADLE_VERSION
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

    private val androidGradlePluginVersion = projectConfig.buildSystemConfig?.agpVersion ?: DEFAULT_AGP_VERSION
    private val kotlinVersion = projectConfig.buildSystemConfig?.kotlinVersion ?: DEFAULT_KOTLIN_VERSION
    val useKotlin = projectConfig.moduleConfigs.map { it.useKotlin }.find { it } ?: false
    val gradleVersion = projectConfig.buildSystemConfig?.buildSystemVersion ?: DEFAULT_GRADLE_VERSION

    val generateBazelFiles = projectConfig.buildSystemConfig?.generateBazelFiles

    val moduleBlueprints: List<ModuleBlueprint>
    val androidModuleBlueprints: List<AndroidModuleBlueprint>
    val allModuleBlueprints: List<AbstractModuleBlueprint>
    val allModulesNames: List<String>
    val allDependencies: Map<String, List<ModuleDependency>>

    val buildGradleBlueprint = ProjectBuildGradleBlueprint(projectRoot, useKotlin, androidGradlePluginVersion,
            kotlinVersion, projectConfig.repositories, projectConfig.classpathDependencies)

    val gradlePropertiesBlueprint = GradlePropertiesBlueprint(projectRoot, projectConfig.buildSystemConfig?.properties)

    val jsonText = projectConfig.jsonText

    init {
        var temporaryModuleBlueprints: List<ModuleBlueprint> = listOf()
        var temporaryAndroidBlueprints: List<AndroidModuleBlueprint> = listOf()
        print("Generating blueprints... ")
        val timeModels = measureTimeMillis {
            val configMap = projectConfig.moduleConfigs.associateBy { it.moduleName }
            ModuleBlueprintFactory.initCache()
            runBlocking {
                val deferredModules = projectConfig.pureModuleConfigs.map {
                    async {
                        ModuleBlueprintFactory.create(it, projectRoot, configMap, projectConfig.buildSystemConfig)
                    }
                }
                val deferredAndroid = projectConfig.androidModuleConfigs.map {
                    async {
                        ModuleBlueprintFactory.createAndroidModule(projectRoot, it, configMap, projectConfig.buildSystemConfig)
                    }
                }
                temporaryModuleBlueprints = deferredModules.map { it.await() }
                temporaryAndroidBlueprints = deferredAndroid.map { it.await() }
            }
        }
        moduleBlueprints = temporaryModuleBlueprints
        androidModuleBlueprints = temporaryAndroidBlueprints
        allModuleBlueprints = androidModuleBlueprints + moduleBlueprints
        allModulesNames = allModuleBlueprints.map { it.name }
        allDependencies = allModuleBlueprints.associate { it.name to it.moduleDependencies }
        println("\rGenerating blueprints... done in $timeModels ms")
    }

    fun hasCircularDependencies(): Boolean {
        // Try to find a topological order (it will be stored in topologicalOrder)
        val dependencyCounter: MutableMap<String, Int> = mutableMapOf()
        for (moduleName in allModulesNames) {
            dependencyCounter[moduleName] = 0
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
        if (topologicalOrder.size != allModulesNames.size) {
            println("Found circular dependencies that affect modules ${dependencyCounter.filter { counter -> counter.value > 0 }.keys}")
            return true
        }
        return false
    }
}
