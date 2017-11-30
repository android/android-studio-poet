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

import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.models.MethodToCall
import com.google.androidstudiopoet.models.ModuleBlueprint

object ModuleBlueprintFactory {

    // Store if a methodToCallFromOutside was already computed
    private var methodCache : MutableList<MethodToCall?> = mutableListOf()
    // Used to synchronize cache elements (can be one per item since each is independent)
    private var methodLock : List<Any> = listOf()

    fun create(index: Int, config: ConfigPOJO, projectRoot: String): ModuleBlueprint {
        val dependencies : List<Int> = config.resolvedDependencies[index]?.map { it.to } ?: listOf()

        val dependenciesNames = dependencies.map { getModuleNameByIndex(it) }
        val methodsToCallWithinModule = dependencies.map { getMethodToCallForDependency(it, config, projectRoot) }

        return ModuleBlueprint(index, getModuleNameByIndex(index), projectRoot, dependenciesNames, methodsToCallWithinModule,
                config)
    }

    private fun getMethodToCallForDependency(index: Int, config: ConfigPOJO, projectRoot: String): MethodToCall {
        /*
            Because method to call from outside doesn't depend on the dependencies, we can create ModuleBlueprint for
            dependency, return methodToCallFromOutside and forget about this module blueprint.
            WARNING: creation of ModuleBlueprint could be expensive
         */
        synchronized(methodLock[index]) {
            val cachedMethod = methodCache[index]
            if (cachedMethod != null) {
                return cachedMethod
            }
            val newMethod = ModuleBlueprint(index, getModuleNameByIndex(index), projectRoot, listOf(), listOf(), config).methodToCallFromOutside
            methodCache[index] = newMethod
            return newMethod
        }
    }

    private fun getModuleNameByIndex(index: Int) = "module$index"

    fun createAndroidModule(i: Int, configPOJO: ConfigPOJO?, projectRoot: String, dependencies: List<String>):
            AndroidModuleBlueprint {

         return AndroidModuleBlueprint(i,
                 configPOJO!!.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule!!.toInt(),
                 configPOJO.numActivitiesPerAndroidModule.toInt(), projectRoot, i == 0, configPOJO.useKotlin, dependencies, configPOJO.productFlavors)
    }

    fun initCache(size : Int) {
        methodCache = MutableList(size, {null})
        methodLock = List(size, {String()})
    }
}

