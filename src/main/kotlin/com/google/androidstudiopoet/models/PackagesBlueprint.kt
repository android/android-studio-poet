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

data class PackagesBlueprint(private val javaPackageCount: Int,
                             private val javaClassCount: Int,
                             private val javaMethodsPerClass: Int,
                             private val kotlinPackageCount: Int,
                             private val kotlinClassCount: Int,
                             private val kotlinMethodsPerClass: Int,
                             val where: String,
                             private val moduleName: String,
                             private val methodsToCallWithin: List<MethodToCall>) {

    val javaPackageBlueprints = ArrayList<PackageBlueprint>()
    val kotlinPackageBlueprints = ArrayList<PackageBlueprint>()
    var methodToCallFromOutside: MethodToCall

    init {
        var previousClassMethodToCall:List<MethodToCall> = methodsToCallWithin
        (0 until javaPackageCount).forEach { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, javaClassCount, javaMethodsPerClass, where, moduleName, Language.JAVA, previousClassMethodToCall)
            javaPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        (0 until kotlinPackageCount).map { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, kotlinClassCount, kotlinMethodsPerClass, where, moduleName, Language.KOTLIN, previousClassMethodToCall)
            kotlinPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        methodToCallFromOutside = if (kotlinPackageCount != 0) {
            kotlinPackageBlueprints.last().methodToCallFromOutside
        } else {
            javaPackageBlueprints.last().methodToCallFromOutside
        }
    }
}
