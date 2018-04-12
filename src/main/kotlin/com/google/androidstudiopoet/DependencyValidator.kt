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

import com.google.androidstudiopoet.models.FromToDependencyConfig

private const val ANDROID_TYPE: String = "androidAppModule"
private const val MODULE_TYPE = "module"

class DependencyValidator {
    fun isValid(dependencies: List<FromToDependencyConfig>, moduleCount: Int, androidModuleCount: Int): Boolean {
        for (dependency in dependencies) {
            if (badDependency(dependency, moduleCount, androidModuleCount)) {
                return false
            }
        }
        //TODO Add check for cycle dependencies and proper reporting
        return true
    }

    private fun badDependency(dependency: FromToDependencyConfig, moduleCount: Int, androidModuleCount: Int): Boolean {
        val from = ModuleSplit(dependency.from)
        if (badIndexByType(from, moduleCount, androidModuleCount)) {
            return true
        }
        val to = ModuleSplit(dependency.to)
        if (badIndexByType(to, moduleCount, androidModuleCount)) {
            return true
        }
        if (javaDependsOnAndroid(from, to)) {
            println("Java module ${dependency.from} cannot depend on Android module ${dependency.to}")
            return true
        }
        if (isApp(to)) {
            println("Module ${dependency.from} cannot depend on Android app module ${dependency.to}")
            return true
        }
        return false
    }

    private fun badIndexByType(moduleSplit: ModuleSplit, moduleCount: Int, androidModuleCount: Int): Boolean =
            when(moduleSplit.type) {
                MODULE_TYPE -> badIndex(moduleSplit, moduleCount)
                ANDROID_TYPE -> badIndex(moduleSplit, androidModuleCount)
                else -> true
            }

    private fun badIndex(split: ModuleSplit, count: Int) : Boolean {
        if ((split.index < 0) || (split.index >= count)) {
            println("Incorrect index for ${split.name()} in dependencies, should be in [0, $count)")
            return true
        }
        return false
    }

    private fun javaDependsOnAndroid(from: ModuleSplit, to: ModuleSplit): Boolean =
            from.type.equals(MODULE_TYPE) && to.type.equals(ANDROID_TYPE)

    private fun isApp(to: ModuleSplit): Boolean = to.index == 0 && to.type.equals(ANDROID_TYPE)

    class ModuleSplit(moduleName : String){
        val type: String
        val index: Int

        init {
            var firstChar = moduleName.length - 1
            while ((firstChar >= 0) && moduleName[firstChar].isDigit()) {
                firstChar--
            }
            type = moduleName.substring(0, firstChar + 1)
            index = moduleName.substring(firstChar + 1).toInt()
        }

        fun name(): String {
            return "$type$index"
        }
    }
}
