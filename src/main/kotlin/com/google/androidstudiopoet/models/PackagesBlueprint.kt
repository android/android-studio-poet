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

import com.google.androidstudiopoet.input.CodeConfig

data class PackagesBlueprint(private val javaConfig: CodeConfig?,
                             private val kotlinConfig: CodeConfig?,
                             val where: String,
                             private val moduleName: String,
                             private val methodsToCallWithin: List<MethodToCall>,
                             val generateTests: Boolean) {

    private val javaPackageCount: Int = javaConfig?.packages ?: 0
    private val javaClassCount: Int = javaConfig?.classesPerPackage ?: 0
    private val javaMethodsPerClass: Int = javaConfig?.methodsPerClass ?: 0
    private val kotlinPackageCount: Int = kotlinConfig?.packages ?: 0
    private val kotlinClassCount: Int = kotlinConfig?.classesPerPackage ?: 0
    private val kotlinMethodsPerClass: Int = kotlinConfig?.methodsPerClass ?: 0

    private val javaCodeComplexity = javaConfig.getCodeComplexity()
    private val kotlinCodeComplexity = kotlinConfig.getCodeComplexity()

    val javaPackageBlueprints = ArrayList<PackageBlueprint>()
    val kotlinPackageBlueprints = ArrayList<PackageBlueprint>()
    var methodToCallFromOutside: MethodToCall

    init {
        var previousClassMethodToCall: List<MethodToCall> = methodsToCallWithin
        (0 until javaPackageCount).forEach { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, javaClassCount, javaMethodsPerClass, where, moduleName,
                    Language.JAVA, previousClassMethodToCall, generateTests, javaCodeComplexity)
            javaPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        (0 until kotlinPackageCount).map { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, kotlinClassCount, kotlinMethodsPerClass, where,
                    moduleName, Language.KOTLIN, previousClassMethodToCall, generateTests, kotlinCodeComplexity)
            kotlinPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        methodToCallFromOutside = MethodToCall("", "")

        if (kotlinPackageCount != 0) {
            if (!kotlinPackageBlueprints.isEmpty()) {
                methodToCallFromOutside = kotlinPackageBlueprints.last().methodToCallFromOutside
            }

        } else {
            if (!javaPackageBlueprints.isEmpty()) {
                methodToCallFromOutside = javaPackageBlueprints.last().methodToCallFromOutside
            }
        }
    }

    private fun CodeConfig?.getCodeComplexity() = ClassComplexity(this?.complexity?.lambdaCount ?: 0)
}
