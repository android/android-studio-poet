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

data class ModuleBlueprint(val index: Int, val name: String, val root: String, val dependencies: List<String>,
                           val methodsToCall: List<MethodToCall>, private val config: ConfigPOJO) {

    private val javaPackageCount = config.javaPackageCount!!.toInt()
    private val javaClassCount = if (javaPackageCount > 0) config.javaClassCount!!.toInt() else 0
    private val javaMethodsPerClass = if (javaPackageCount >0 ) config.javaMethodsPerClass else 0

    private val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
    private val kotlinClassCount = if (kotlinPackageCount > 0) config.kotlinClassCount!!.toInt() else 0
    private val kotlinMethodsPerClass = if (kotlinPackageCount > 0) config.kotlinMethodsPerClass else 0

    val moduleRoot = root.joinPath(name)
    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass,moduleRoot + "/src/main/java/", name, methodsToCall)

    var methodToCallFromOutside = packagesBlueprint.methodToCallFromOutside
    val useKotlin = config.useKotlin
}