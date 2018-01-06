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

package com.google.androidstudiopoet

import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.models.ModuleBlueprint
import com.google.androidstudiopoet.models.ModuleDependency

object ModuleBlueprintFactory {

    // Store if a ModuleDependency was already computed
    private var moduleDependencyCache: MutableMap<String, ModuleDependency?> = mutableMapOf()
    // Used to synchronize cache elements (can be one per item since each is independent)
    private var moduleDependencyLock: MutableMap<String, Any> = mutableMapOf()

    fun create(moduleConfig: ModuleConfig, projectRoot: String): ModuleBlueprint {
        val moduleDependencies = moduleConfig.dependencies
                .map {
                    getModuleDependency(it, projectRoot, moduleConfig.javaPackageCount, moduleConfig.javaClassCount,
                            moduleConfig.javaMethodsPerClass, moduleConfig.kotlinPackageCount,
                            moduleConfig.kotlinClassCount, moduleConfig.kotlinMethodsPerClass, moduleConfig.useKotlin)
                }
        val result = ModuleBlueprint(moduleConfig.moduleName, projectRoot, moduleConfig.useKotlin, moduleDependencies,
                moduleConfig.javaPackageCount, moduleConfig.javaClassCount, moduleConfig.javaMethodsPerClass,
                moduleConfig.kotlinPackageCount, moduleConfig.kotlinClassCount, moduleConfig.kotlinMethodsPerClass, moduleConfig.extraLines, moduleConfig.generateTests)
        synchronized(moduleDependencyLock.getOrPut(moduleConfig.moduleName, {moduleConfig.moduleName} )) {
            if (moduleDependencyCache[moduleConfig.moduleName] == null) {
                moduleDependencyCache[moduleConfig.moduleName] = ModuleDependency(result.name, result.methodToCallFromOutside)
            }
        }
        return result
    }

    private fun getModuleDependency(moduleName: String, projectRoot: String, javaPackageCount: Int, javaClassCount: Int, javaMethodsPerClass: Int,
                                    kotlinPackageCount: Int, kotlinClassCount: Int, kotlinMethodsPerClass: Int, useKotlin: Boolean): ModuleDependency {
        synchronized(moduleDependencyLock.getOrPut(moduleName, {moduleName})) {
            val cachedMethod = moduleDependencyCache[moduleName]
            if (cachedMethod != null) {
                return cachedMethod
            }
            val newMethodDependency = getDependencyForModule(moduleName, projectRoot, javaPackageCount, javaClassCount,
                    javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass, useKotlin)
            moduleDependencyCache[moduleName] = newMethodDependency
            return newMethodDependency
        }
    }

    private fun getDependencyForModule(moduleName: String, projectRoot: String, javaPackageCount: Int, javaClassCount: Int,
                                         javaMethodsPerClass: Int, kotlinPackageCount: Int, kotlinClassCount: Int,
                                         kotlinMethodsPerClass: Int, useKotlin: Boolean): ModuleDependency {
        /*
            Because method to call from outside doesn't depend on the dependencies, we can create ModuleBlueprint for
            dependency, return methodToCallFromOutside and forget about this module blueprint.
            WARNING: creation of ModuleBlueprint could be expensive
         */
        val tempModuleBlueprint = ModuleBlueprint(moduleName, projectRoot, useKotlin, listOf(),
                javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,kotlinClassCount,
                kotlinMethodsPerClass, null, false)
        return ModuleDependency(tempModuleBlueprint.name, tempModuleBlueprint.methodToCallFromOutside)

    }

    fun createAndroidModule(projectRoot: String, androidModuleConfig: AndroidModuleConfig):
            AndroidModuleBlueprint {

        val moduleDependencies = androidModuleConfig.dependencies
                .map {
                    getModuleDependency(it, projectRoot, androidModuleConfig.javaPackageCount, androidModuleConfig.javaClassCount,
                            androidModuleConfig.javaMethodsPerClass, androidModuleConfig.kotlinPackageCount,
                            androidModuleConfig.kotlinClassCount, androidModuleConfig.kotlinMethodsPerClass, androidModuleConfig.useKotlin)
                }

        return AndroidModuleBlueprint(androidModuleConfig.moduleName,
                androidModuleConfig.activityCount, androidModuleConfig.resourcesConfig,
                projectRoot, androidModuleConfig.hasLaunchActivity, androidModuleConfig.useKotlin,
                moduleDependencies, androidModuleConfig.productFlavorConfigs, androidModuleConfig.buildTypes,
                androidModuleConfig.javaPackageCount, androidModuleConfig.javaClassCount,
                androidModuleConfig.javaMethodsPerClass, androidModuleConfig.kotlinPackageCount,
                androidModuleConfig.kotlinClassCount, androidModuleConfig.kotlinMethodsPerClass,
                androidModuleConfig.extraLines,
                androidModuleConfig.generateTests)
    }

    fun initCache() {
        moduleDependencyCache = mutableMapOf()
        moduleDependencyLock = mutableMapOf()
    }
}

