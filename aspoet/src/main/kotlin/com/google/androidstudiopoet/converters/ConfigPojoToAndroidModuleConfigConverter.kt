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

package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.*
import com.google.androidstudiopoet.input.ConfigPOJO

class ConfigPojoToAndroidModuleConfigConverter {
    fun convert(config: ConfigPOJO, index: Int, productFlavorConfigs: List<FlavorConfig>,
                buildTypes: List<BuildTypeConfig>): AndroidModuleConfig {
        return AndroidModuleConfig().apply {
            moduleName = config.getAndroidModuleName(index)

            java = CodeConfig().apply {
                packages = config.javaPackageCount?.toInt() ?: 0
                classesPerPackage = config.javaClassCount?.toInt() ?: 0
                methodsPerClass = config.javaMethodsPerClass
            }

            kotlin = CodeConfig().apply {
                packages = config.kotlinPackageCount?.toInt() ?: 0
                classesPerPackage = config.kotlinClassCount?.toInt() ?: 0
                methodsPerClass = config.kotlinMethodsPerClass
            }

            useKotlin = config.useKotlin

            activityCount = config.numActivitiesPerAndroidModule?.toInt() ?: 0

            generateTests = config.generateTests
            hasLaunchActivity = index == 0

            val resolvedDependencies = config.resolvedDependencies[moduleName]?.sortedBy { it.to }
                    ?.map { dependency -> DependencyConfig.ModuleDependencyConfig(dependency.to, dependency.method) } ?: emptyList()
            dependencies = config.libraries?.let { resolvedDependencies + it } ?: resolvedDependencies

            this.buildTypes = buildTypes
            this.productFlavorConfigs = productFlavorConfigs
            extraLines = config.extraAndroidBuildFileLines
            resourcesConfig = ResourcesConfig(activityCount + 2, activityCount + 5, activityCount)
            dataBindingConfig = config.dataBindingConfig
        }
    }
}