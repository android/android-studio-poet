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
import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.utils.joinPath

class ProjectBlueprint(val configPOJO: ConfigPOJO) {
    val projectName = configPOJO.projectName

    val projectRoot = configPOJO.root.joinPath(projectName)

    private val pureModulesConfigs = (0 until configPOJO.numModules).map { ModuleConfig(it, configPOJO) }
    val moduleBlueprints = pureModulesConfigs.map { ModuleBlueprintFactory.create(it, projectRoot) }

    val androidModuleBlueprints = (0 until configPOJO.androidModules!!.toInt()).map { i ->
        ModuleBlueprintFactory.createAndroidModule(projectRoot, pureModulesConfigs.map { it.index },
                AndroidModuleConfig(i, configPOJO),
                (i + 1 until configPOJO.androidModules.toInt()).toList().map { AndroidModuleConfig(it, configPOJO) })
    }

    val allModulesNames = moduleBlueprints.map { it.name } + androidModuleBlueprints.map { it.name }

    val androidGradlePluginVersion = configPOJO.androidGradlePluginVersion
    val kotlinVersion = configPOJO.kotlinVersion
    val useKotlin = configPOJO.useKotlin
    val gradleVersion = configPOJO.gradleVersion!!

    val dependencies = configPOJO.dependencies ?: listOf()
    val moduleCount = configPOJO.numModules
}
