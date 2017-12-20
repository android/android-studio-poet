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

import com.google.androidstudiopoet.utils.joinPath

data class ModuleBlueprint(val name: String,
                           private val root: String,
                           val useKotlin: Boolean,
                           val dependencies: List<ModuleDependency>,
                           private val javaPackageCount: Int, private val javaClassCount: Int, private val javaMethodsPerClass: Int,
                           private val kotlinPackageCount: Int, private val kotlinClassCount: Int, private val kotlinMethodsPerClass: Int,
                           val extraLines: List<String>?) {

    val moduleRoot = root.joinPath(name)
    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass, moduleRoot + "/src/main/java/", name, convertDependenciesToMethodsToCall(dependencies))

    var methodToCallFromOutside = packagesBlueprint.methodToCallFromOutside

    private fun convertDependenciesToMethodsToCall(dependencies: List<ModuleDependency>): List<MethodToCall> = dependencies.map { it.methodToCall }

}