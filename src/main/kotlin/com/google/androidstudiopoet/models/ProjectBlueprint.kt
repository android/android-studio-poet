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
import com.google.androidstudiopoet.utils.joinPath
import kotlinx.coroutines.experimental.*
import kotlin.system.measureTimeMillis

class ProjectBlueprint(val configPOJO: ConfigPOJO) {
    val projectRoot = configPOJO.root.joinPath(configPOJO.projectName)
    val moduleBlueprints : List<ModuleBlueprint>
    val androidModuleBlueprints : List<AndroidModuleBlueprint>
    val allModulesNames : List<String>

    init  {
        var temporaryModuleBlueprints : List<ModuleBlueprint> = listOf()
        val timeModels = measureTimeMillis {
            ModuleBlueprintFactory.initCache(configPOJO.numModules)
            runBlocking {
                val deferred = (0 until configPOJO.numModules).map {
                    async {
                        ModuleBlueprintFactory.create(it, configPOJO, projectRoot)
                    }
                }
                temporaryModuleBlueprints = deferred.map {it.await()}
            }
        }
        moduleBlueprints = temporaryModuleBlueprints
        println("Time to create model blueprints: $timeModels")

        var temporayAndroidBlueprints : List<AndroidModuleBlueprint> = listOf()
        val timeAndroidModels = measureTimeMillis {
            temporayAndroidBlueprints = (0 until configPOJO.androidModules!!.toInt()).map { i ->
                ModuleBlueprintFactory.createAndroidModule(i, configPOJO, projectRoot, moduleBlueprints.map { it.name })
            }
        }
        androidModuleBlueprints = temporayAndroidBlueprints
        println("Time to create Android model blueprints: $timeAndroidModels")

        allModulesNames = moduleBlueprints.map { it.name } + androidModuleBlueprints.map { it.name }
    }
}
