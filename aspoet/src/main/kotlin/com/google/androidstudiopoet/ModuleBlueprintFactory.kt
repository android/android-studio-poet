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

import com.google.androidstudiopoet.input.*
import com.google.androidstudiopoet.models.*

object ModuleBlueprintFactory {
    // Store if a ModuleDependency was already computed
    private val tempBlueprintCache: MutableMap<String, AbstractModuleBlueprint> = mutableMapOf()
    // Used to synchronize cache elements (can be one per item since each is independent)
    private val moduleNameLock: MutableMap<String, Any> = mutableMapOf()

    fun create(
        moduleConfig: ModuleConfig,
        projectRoot: String,
        configMap: Map<String, ModuleConfig>,
        buildSystemConfig: BuildSystemConfig?
    ): ModuleBlueprint = createModule(
            moduleConfig,
            projectRoot,
            createDependencies(projectRoot, moduleConfig, configMap, buildSystemConfig),
            buildSystemConfig
    )

    private fun createModule(
            moduleConfig: ModuleConfig,
            projectRoot: String,
            dependencies: Set<Dependency>,
            buildSystemConfig: BuildSystemConfig?
    ): ModuleBlueprint {
        return ModuleBlueprint(
                moduleConfig.moduleName,
                projectRoot,
                moduleConfig.useKotlin,
                dependencies,
                moduleConfig.java,
                moduleConfig.kotlin,
                moduleConfig.extraLines,
                moduleConfig.generateTests,
                moduleConfig.plugins,
                buildSystemConfig?.generateBazelFiles
        )
    }

    fun createAndroidModule(
        projectRoot: String,
        androidModuleConfig: AndroidModuleConfig,
        configMap: Map<String, ModuleConfig>,
        buildSystemConfig: BuildSystemConfig?
    ): AndroidModuleBlueprint = createAndroidModule(
            projectRoot,
            createDependencies(projectRoot, androidModuleConfig, configMap, buildSystemConfig),
            androidModuleConfig,
            buildSystemConfig
    )

    private fun createAndroidModule(
            projectRoot: String,
            dependencies: Set<Dependency>,
            androidModuleConfig: AndroidModuleConfig,
            buildSystemConfig: BuildSystemConfig?,
    ): AndroidModuleBlueprint = AndroidModuleBlueprint(
            androidModuleConfig.moduleName,
            androidModuleConfig.activityCount,
            androidModuleConfig.resourcesConfig,
            projectRoot,
            androidModuleConfig.hasLaunchActivity,
            androidModuleConfig.useKotlin,
            dependencies,
            androidModuleConfig.productFlavorConfigs,
            androidModuleConfig.buildTypes,
            androidModuleConfig.java,
            androidModuleConfig.kotlin,
            androidModuleConfig.extraLines,
            androidModuleConfig.generateTests,
            androidModuleConfig.dataBindingConfig,
            androidModuleConfig.composeConfig,
            androidModuleConfig.viewBinding,
            androidModuleConfig.androidBuildConfig,
            androidModuleConfig.plugins,
            buildSystemConfig?.generateBazelFiles
    )

    private fun createDependencies(projectRoot: String, moduleConfig: ModuleConfig, configMap: Map<String, ModuleConfig>, buildSystemConfig: BuildSystemConfig?): Set<Dependency> {
        val moduleDependencies = moduleConfig.dependencies
                ?.mapNotNull { dependencyConfig ->
                    when (dependencyConfig) {
                        is DependencyConfig.ModuleDependencyConfig -> {
                            val dependencyModuleConfig = configMap[dependencyConfig.moduleName]
                            when (dependencyModuleConfig) {
                                is AndroidModuleConfig -> {
                                    val methodToCall = getMethodToCall(projectRoot, dependencyModuleConfig, buildSystemConfig)
                                    val resourcesToRefer = getResourcesToRefer(projectRoot, dependencyModuleConfig, buildSystemConfig)
                                    AndroidModuleDependency(dependencyConfig.moduleName, methodToCall, dependencyConfig.method.toDependencyMethod(), resourcesToRefer)
                                }
                                is ModuleConfig -> {
                                    val methodToCall = getMethodToCall(projectRoot, dependencyModuleConfig, buildSystemConfig)
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

    private fun getResourcesToRefer(projectRoot: String, moduleConfig: ModuleConfig, buildSystemConfig: BuildSystemConfig?): ResourcesToRefer {
        val blueprint = getCachedBlueprint(projectRoot, moduleConfig, buildSystemConfig)
        return (blueprint as AndroidModuleBlueprint).resourcesToReferFromOutside
    }

    private fun getMethodToCall(projectRoot: String, moduleConfig: ModuleConfig, buildSystemConfig: BuildSystemConfig?
    ) = getCachedBlueprint(projectRoot, moduleConfig, buildSystemConfig).methodToCallFromOutside

    private fun getCachedBlueprint(projectRoot: String, moduleConfig: ModuleConfig, buildSystemConfig: BuildSystemConfig?): AbstractModuleBlueprint {
        synchronized(moduleNameLock.getOrPut(moduleConfig.moduleName, { moduleConfig.moduleName })) {
            var cachedBlueprint = tempBlueprintCache[moduleConfig.moduleName]
            if (cachedBlueprint == null) {
                cachedBlueprint =
                        if (moduleConfig is AndroidModuleConfig)
                            createAndroidModule(projectRoot, emptySet(), moduleConfig, buildSystemConfig)
                        else
                            createModule(moduleConfig, projectRoot, emptySet(), buildSystemConfig)
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

