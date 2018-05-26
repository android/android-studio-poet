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

import com.google.androidstudiopoet.input.AndroidBuildConfig
import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.DependencyConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.models.*

object ModuleBlueprintFactory {
    // Store if a ModuleDependency was already computed
    private val tempBlueprintCache: MutableMap<String, AbstractModuleBlueprint> = mutableMapOf()
    // Used to synchronize cache elements (can be one per item since each is independent)
    private val moduleNameLock: MutableMap<String, Any> = mutableMapOf()

    fun create(moduleConfig: ModuleConfig, projectRoot: String, configMap: Map<String, ModuleConfig>): ModuleBlueprint {
        val dependencies = createDependencies(projectRoot, moduleConfig, configMap)
        return ModuleBlueprint(moduleConfig.moduleName, projectRoot, moduleConfig.useKotlin, dependencies,
                moduleConfig.javaPackageCount, moduleConfig.javaClassCount, moduleConfig.javaMethodsPerClass,
                moduleConfig.kotlinPackageCount, moduleConfig.kotlinClassCount, moduleConfig.kotlinMethodsPerClass,
                moduleConfig.extraLines, moduleConfig.generateTests, moduleConfig.plugins)
    }

    private fun createWithoutDependencies(moduleConfig: ModuleConfig, projectRoot: String): ModuleBlueprint {
        return ModuleBlueprint(moduleConfig.moduleName, projectRoot, moduleConfig.useKotlin, setOf(),
                moduleConfig.javaPackageCount, moduleConfig.javaClassCount, moduleConfig.javaMethodsPerClass,
                moduleConfig.kotlinPackageCount, moduleConfig.kotlinClassCount, moduleConfig.kotlinMethodsPerClass,
                moduleConfig.extraLines, moduleConfig.generateTests, moduleConfig.plugins)
    }

    fun createAndroidModule(projectRoot: String, androidModuleConfig: AndroidModuleConfig, configMap: Map<String, ModuleConfig>): AndroidModuleBlueprint {
        val dependencies = createDependencies(projectRoot, androidModuleConfig, configMap)
        return AndroidModuleBlueprint(androidModuleConfig.moduleName,
                androidModuleConfig.activityCount, androidModuleConfig.resourcesConfig,
                projectRoot, androidModuleConfig.hasLaunchActivity, androidModuleConfig.useKotlin,
                dependencies, androidModuleConfig.productFlavorConfigs, androidModuleConfig.buildTypes,
                androidModuleConfig.javaPackageCount, androidModuleConfig.javaClassCount,
                androidModuleConfig.javaMethodsPerClass, androidModuleConfig.kotlinPackageCount,
                androidModuleConfig.kotlinClassCount, androidModuleConfig.kotlinMethodsPerClass,
                androidModuleConfig.extraLines,
                androidModuleConfig.generateTests,
                androidModuleConfig.dataBindingConfig,
                androidModuleConfig.androidBuildConfig ?: AndroidBuildConfig(),
                androidModuleConfig.plugins)
    }

    private fun createAndroidModuleWithoutDependencies(projectRoot: String, androidModuleConfig: AndroidModuleConfig):
            AndroidModuleBlueprint = AndroidModuleBlueprint(androidModuleConfig.moduleName,
                    androidModuleConfig.activityCount, androidModuleConfig.resourcesConfig,
                    projectRoot, androidModuleConfig.hasLaunchActivity, androidModuleConfig.useKotlin,
                    setOf(), androidModuleConfig.productFlavorConfigs, androidModuleConfig.buildTypes,
                    androidModuleConfig.javaPackageCount, androidModuleConfig.javaClassCount,
                    androidModuleConfig.javaMethodsPerClass, androidModuleConfig.kotlinPackageCount,
                    androidModuleConfig.kotlinClassCount, androidModuleConfig.kotlinMethodsPerClass,
                    androidModuleConfig.extraLines,
                    androidModuleConfig.generateTests,
                    androidModuleConfig.dataBindingConfig,
                    androidModuleConfig.androidBuildConfig ?: AndroidBuildConfig(),
                    androidModuleConfig.plugins)

    private fun createDependencies(projectRoot: String, moduleConfig: ModuleConfig, configMap: Map<String, ModuleConfig>): Set<Dependency> {
        val moduleDependencies = moduleConfig.dependencies
                ?.mapNotNull { dependencyConfig ->
                    when (dependencyConfig) {
                        is DependencyConfig.ModuleDependencyConfig -> {
                            val dependencyModuleConfig = configMap[dependencyConfig.moduleName]
                            return@mapNotNull when (dependencyModuleConfig) {
                                is AndroidModuleConfig -> {
                                    val methodToCall = getMethodToCall(projectRoot, dependencyModuleConfig)
                                    val resourcesToRefer = getResourcesToRefer(projectRoot, dependencyModuleConfig)
                                    AndroidModuleDependency(dependencyConfig.moduleName, methodToCall, dependencyConfig.method.toDependencyMethod(), resourcesToRefer)
                                }
                                is ModuleConfig -> {
                                    val methodToCall = getMethodToCall(projectRoot, dependencyModuleConfig)
                                    ModuleDependency(dependencyConfig.moduleName, methodToCall, dependencyConfig.method.toDependencyMethod())
                                }
                                else -> null
                            }
                        }
                        is DependencyConfig.LibraryDependencyConfig -> LibraryDependency(dependencyConfig.method.toDependencyMethod(), dependencyConfig.library)
                    }
                } ?: listOf()
        return moduleDependencies.toHashSet()
    }

    private fun getResourcesToRefer(projectRoot: String, moduleConfig: ModuleConfig): ResourcesToRefer {
        val blueprint = getCachedBlueprint(projectRoot, moduleConfig)
        return (blueprint as AndroidModuleBlueprint).resourcesToReferFromOutside
    }


    private fun getMethodToCall(projectRoot: String, moduleConfig: ModuleConfig) = getCachedBlueprint(projectRoot, moduleConfig).methodToCallFromOutside

    private fun getCachedBlueprint(projectRoot: String, moduleConfig: ModuleConfig): AbstractModuleBlueprint {
        synchronized(moduleNameLock.getOrPut(moduleConfig.moduleName, { moduleConfig.moduleName })) {
            var cachedBlueprint = tempBlueprintCache[moduleConfig.moduleName]
            if (cachedBlueprint == null) {
                cachedBlueprint =
                        if (moduleConfig is AndroidModuleConfig)
                            createAndroidModuleWithoutDependencies(projectRoot, moduleConfig)
                        else
                            createWithoutDependencies(moduleConfig, projectRoot)
                tempBlueprintCache[moduleConfig.moduleName] = cachedBlueprint
            }
            return cachedBlueprint
        }
    }

    fun initCache() {
        tempBlueprintCache.clear()
        moduleNameLock.clear()
    }
}

private fun String?.toDependencyMethod(): String = this ?: DEFAULT_DEPENDENCY_METHOD

