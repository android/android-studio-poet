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

import com.google.androidstudiopoet.input.CodeConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.input.ConfigPOJO
import com.google.androidstudiopoet.input.DependencyConfig

class ConfigPojoToModuleConfigConverter {
    fun convert(config: ConfigPOJO, index: Int): ModuleConfig {
        return ModuleConfig().apply {
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

            extraLines = config.extraBuildFileLines

            generateTests = config.generateTests

            moduleName = config.getModuleName(index)

            val resolvedDummyFileTreeDependency = config.resolvedDummyLocalJarLibsDependencies[moduleName]?.let { listOf(it) } ?: emptyList()
            dependencies = (config.resolvedDependencies[moduleName]?.map { DependencyConfig.ModuleDependencyConfig(it.to, it.method) } ?: listOf()) + resolvedDummyFileTreeDependency
        }
    }
}