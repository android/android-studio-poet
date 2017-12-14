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
import com.google.androidstudiopoet.models.*

object ModuleBlueprintFactory {

    // Store if a ModuleDependency was already computed
    private var moduleDepencencyCache : MutableList<ModuleDependency?> = mutableListOf()
    // Used to synchronize cache elements (can be one per item since each is independent)
    private var moduleDepencencyLock : List<Any> = listOf()

    fun create(moduleConfig: ModuleConfig, projectRoot: String): ModuleBlueprint {
        val moduleDependencies = moduleConfig.dependencies
                .map {
                    getModuleDependency(it, projectRoot, moduleConfig.javaPackageCount, moduleConfig.javaClassCount,
                            moduleConfig.javaMethodsPerClass, moduleConfig.kotlinPackageCount,
                            moduleConfig.kotlinClassCount, moduleConfig.kotlinMethodsPerClass, moduleConfig.useKotlin)
                }

        return ModuleBlueprint(getModuleNameByIndex(moduleConfig.index), projectRoot, moduleConfig.useKotlin, moduleDependencies,
                moduleConfig.javaPackageCount, moduleConfig.javaClassCount, moduleConfig.javaMethodsPerClass,
                moduleConfig.kotlinPackageCount, moduleConfig.kotlinClassCount, moduleConfig.kotlinMethodsPerClass)
    }

    private fun getModuleDependency(index: Int, projectRoot: String, javaPackageCount: Int, javaClassCount: Int, javaMethodsPerClass: Int,
                                    kotlinPackageCount: Int, kotlinClassCount: Int, kotlinMethodsPerClass: Int, useKotlin: Boolean): ModuleDependency {
        synchronized(moduleDepencencyLock[index]) {
            val cachedMethod = moduleDepencencyCache[index]
            if (cachedMethod != null) {
                return cachedMethod
            }
            val newMethodDependency = getDependencyForModule(index, projectRoot, javaPackageCount, javaClassCount,
                    javaMethodsPerClass, kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass, useKotlin)
            moduleDepencencyCache[index] = newMethodDependency
            return newMethodDependency
        }
    }

    private fun getDependencyForModule(index: Int, projectRoot: String, javaPackageCount: Int, javaClassCount: Int,
                                         javaMethodsPerClass: Int, kotlinPackageCount: Int, kotlinClassCount: Int,
                                         kotlinMethodsPerClass: Int, useKotlin: Boolean): ModuleDependency {
        /*
            Because method to call from outside doesn't depend on the dependencies, we can create ModuleBlueprint for
            dependency, return methodToCallFromOutside and forget about this module blueprint.
            WARNING: creation of ModuleBlueprint could be expensive
         */
        val tempModuleBlueprint = ModuleBlueprint(getModuleNameByIndex(index), projectRoot, useKotlin, listOf(),
                javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,kotlinClassCount,
                kotlinMethodsPerClass)
        return ModuleDependency(tempModuleBlueprint.name, tempModuleBlueprint.methodToCallFromOutside)

    }

    private fun getAndroidModuleDependency(projectRoot: String, androidModuleConfig: AndroidModuleConfig): AndroidModuleDependency {
        /*
            Because method to call from outside and resources to refer don't depend on the dependencies, we can create AndroidModuleBlueprint for
            dependency, return methodToCallFromOutside with resourcesToRefer and forget about this module blueprint.
            WARNING: creation of AndroidModuleBlueprint could be expensive
         */

        val moduleBlueprint = createAndroidModule(projectRoot, listOf(), androidModuleConfig, listOf())
        return AndroidModuleDependency(moduleBlueprint.name, moduleBlueprint.methodToCallFromOutside, moduleBlueprint.resourcesToReferFromOutside)
    }

    fun createAndroidModule(projectRoot: String, codeModuleDependencyIndexes: List<Int>,
                            androidModuleConfig: AndroidModuleConfig, androidDependencies: List<AndroidModuleConfig>):
            AndroidModuleBlueprint {

        val moduleDependencies = codeModuleDependencyIndexes
                .map {
                    getModuleDependency(it, projectRoot, androidModuleConfig.javaPackageCount, androidModuleConfig.javaClassCount,
                            androidModuleConfig.javaMethodsPerClass, androidModuleConfig.kotlinPackageCount,
                            androidModuleConfig.kotlinClassCount, androidModuleConfig.kotlinMethodsPerClass, androidModuleConfig.useKotlin)
                } + androidDependencies.map { getAndroidModuleDependency(projectRoot, it) }

        return AndroidModuleBlueprint(androidModuleConfig.index,
                androidModuleConfig.activityCount, androidModuleConfig.resourcesConfig,
                projectRoot, androidModuleConfig.hasLaunchActivity, androidModuleConfig.useKotlin,
                moduleDependencies, androidModuleConfig.productFlavors,
                androidModuleConfig.javaPackageCount, androidModuleConfig.javaClassCount,
                androidModuleConfig.javaMethodsPerClass, androidModuleConfig.kotlinPackageCount,
                androidModuleConfig.kotlinClassCount, androidModuleConfig.kotlinMethodsPerClass)
    }

    private fun getModuleNameByIndex(index: Int) = "module$index"

    fun initCache(size : Int) {
        moduleDepencencyCache = MutableList(size, {null})
        moduleDepencencyLock = List(size, {String()})
    }
}

