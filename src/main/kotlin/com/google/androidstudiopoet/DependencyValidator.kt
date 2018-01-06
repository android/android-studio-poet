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

import com.google.androidstudiopoet.models.DependencyConfig

class DependencyValidator {

    private val ANDROID_TYPE = "androidAppModule"
    private val MODULE_TYPE = "module"

    fun isValid(dependencies: List<DependencyConfig>, moduleCount: Int, androidModuleCount: Int): Boolean {
        for (dependency in dependencies) {
            if (badDependency(dependency, moduleCount, androidModuleCount)) {
                return false
            }
        }
        //TODO Add check for cycle dependencies and proper reporting
        return true
    }

    private class ModuleSplit(moduleName : String){
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
    }

    private fun badDependency(dependency: DependencyConfig, moduleCount: Int, androidModuleCount: Int): Boolean {
        val from = ModuleSplit(dependency.from)
        // Check types and indexes
        if (badIndexByType(from, moduleCount, androidModuleCount)) {
            return true
        }
        val to = ModuleSplit(dependency.to)
        if (badIndexByType(to, moduleCount, androidModuleCount)) {
            return true
        }
        // check that a java module does not depend on an Android module
        if (from.type.equals(MODULE_TYPE) && to.type.equals(ANDROID_TYPE)) {
            return true
        }
        // Check that an Android module does not depend on an app
        if (from.type.equals(ANDROID_TYPE) && to.type.equals(ANDROID_TYPE) && (to.index == 0)) {
            return true
        }
        return false
    }

    private fun badIndexByType(moduleSplit: ModuleSplit, moduleCount: Int, androidModuleCount: Int): Boolean {
        if (moduleSplit.type.equals(MODULE_TYPE)) {
            return badIndex(moduleSplit.index, moduleCount)
        }
        if (moduleSplit.type.equals(ANDROID_TYPE)) {
            return badIndex(moduleSplit.index, androidModuleCount)
        }
        return true
    }

    private fun badIndex(index: Int, count: Int) = (index < 0) || (index >= count)
}
