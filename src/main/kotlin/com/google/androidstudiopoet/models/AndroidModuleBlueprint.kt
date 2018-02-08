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

import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ResourcesConfig
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.utils.joinPaths

class AndroidModuleBlueprint(name: String,
                             val numOfActivities: Int,
                             private val resourcesConfig: ResourcesConfig?,
                             projectRoot: String,
                             val hasLaunchActivity: Boolean,
                             useKotlin: Boolean,
                             dependencies: List<ModuleDependency>,
                             productFlavorConfigs: List<FlavorConfig>?,
                             buildTypeConfigs: List<BuildTypeConfig>?,
                             javaPackageCount: Int, javaClassCount: Int, javaMethodsPerClass: Int,
                             kotlinPackageCount: Int, kotlinClassCount: Int, kotlinMethodsPerClass: Int,
                             extraLines: List<String>?,
                             generateTests: Boolean
) : ModuleBlueprint(name, projectRoot, useKotlin, dependencies, javaPackageCount, javaClassCount,
        javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass, extraLines, generateTests) {

    val packageName = "com.$name"
    val srcPath = moduleRoot.joinPath("src")
    val mainPath = srcPath.joinPath("main")
    val resDirPath = mainPath.joinPath("res")
    val codePath = mainPath.joinPath("java")
    val packagePath = codePath.joinPaths(packageName.split("."))

    private val resourcesToReferWithin = dependencies
            .filterIsInstance<AndroidModuleDependency>()
            .map { it.resourcesToRefer }
            .fold(ResourcesToRefer(listOf(), listOf(), listOf())) { acc, resourcesToRefer -> resourcesToRefer.combine(acc) }

    val resourcesBlueprint by lazy {
        when (resourcesConfig) {
            null -> null
            else -> ResourcesBlueprint(name, resDirPath, resourcesConfig.stringCount ?: 0,
                    resourcesConfig.imageCount ?: 0, resourcesConfig.layoutCount ?: 0, resourcesToReferWithin)
        }
    }

    private val layoutNames by lazy {
        resourcesBlueprint?.layoutNames ?: listOf()
    }
    val activityNames = 0.until(numOfActivities).map { "Activity$it" }

    val resourcesToReferFromOutside by lazy {
        resourcesBlueprint?.resourcesToReferFromOutside ?: ResourcesToRefer(listOf(), listOf(), listOf())
    }

    val productFlavors = productFlavorConfigs?.map { Flavor(it.name, it.dimension) }?.toSet()
    val flavorDimensions = productFlavors?.mapNotNull { it.dimension }?.toSet()

    val buildTypes = buildTypeConfigs?.map { BuildType(it.name, it.body) }?.toSet()
    val activityBlueprints by lazy {
        (0..numOfActivities).map { ActivityBlueprint(activityNames[it], layoutNames[it], packagePath, packageName,
                classToReferFromActivity) }
    }

    private val classToReferFromActivity: ClassBlueprint by lazy {
        (packagesBlueprint.javaPackageBlueprints.asSequence() + packagesBlueprint.kotlinPackageBlueprints.asSequence())
                .flatMap { it.classBlueprints.asSequence() }
                .first()
    }
}
