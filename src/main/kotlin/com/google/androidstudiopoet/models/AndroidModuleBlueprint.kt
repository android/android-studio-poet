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

import com.google.androidstudiopoet.Blueprint
import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ResourcesConfig
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.utils.joinPaths

data class AndroidModuleBlueprint(val index: Int,
                                  val numOfActivities: Int,
                                  private val resourcesConfig: ResourcesConfig,
                                  private val projectRoot: String,
                                  val hasLaunchActivity: Boolean,
                                  val useKotlin: Boolean,
                                  val dependencies: List<ModuleDependency>,
                                  private val productFlavorConfigs: List<FlavorConfig>?,
                                  private val buildTypeConfigs: List<BuildTypeConfig>?,
                                  private val javaPackageCount: Int, private val javaClassCount: Int, private val javaMethodsPerClass: Int,
                                  private val kotlinPackageCount: Int, private val kotlinClassCount: Int, private val kotlinMethodsPerClass: Int
) : Blueprint {

    val name = "androidAppModule" + index
    val packageName = "com.$name"
    val moduleRoot = projectRoot.joinPath(name)
    val srcPath = moduleRoot.joinPath("src")
    val mainPath = srcPath.joinPath("main")
    val resDirPath = mainPath.joinPath("res")
    val codePath = mainPath.joinPath("java")
    val packagePath = codePath.joinPaths(packageName.split("."))

    private val methodsToCallWithIn = dependencies.map { it.methodToCall }

    val packagesBlueprint by lazy {
        PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
                kotlinClassCount, kotlinMethodsPerClass, moduleRoot + "/src/main/java/", name, methodsToCallWithIn)
    }

    private val resourcesToReferWithin = dependencies
            .filterIsInstance<AndroidModuleDependency>()
            .map { it.resourcesToRefer }
            .fold(ResourcesToRefer(listOf(), listOf(), listOf())) { acc, resourcesToRefer -> resourcesToRefer.combine(acc) }

    val resourcesBlueprint by lazy {
        ResourcesBlueprint(name, resDirPath, resourcesConfig.stringCount ?: 0,
                resourcesConfig.imageCount ?: 0, resourcesConfig.layoutCount ?: 0, resourcesToReferWithin)
    }

    val layoutNames by lazy {
        resourcesBlueprint.layoutNames
    }
    val activityNames = 0.until(numOfActivities).map { "Activity$it" }

    val methodToCallFromOutside by lazy {
        packagesBlueprint.methodToCallFromOutside
    }
    val resourcesToReferFromOutside by lazy {
        resourcesBlueprint.resourcesToReferFromOutside
    }

    val productFlavors = productFlavorConfigs?.map { Flavor(it.name, it.dimension) }?.toSet()
    val flavorDimensions = productFlavors?.mapNotNull { it.dimension }?.toSet()

    val buildTypes = buildTypeConfigs?.map {BuildType(it.name, it.body)}?.toSet()
}
