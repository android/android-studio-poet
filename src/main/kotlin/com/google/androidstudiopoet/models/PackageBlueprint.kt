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

import com.google.androidstudiopoet.utils.joinPaths

data class PackageBlueprint(private val packageIndex: Int, private val classesPerPackage: Int,
                            private val methodsPerClass: Int, private val fieldsPerClass: Int,
                            private val where: String, private val moduleName: String,
                            val language: Language, private val methodsToCallWithinPackage: List<MethodToCall>,
                            val generateTests : Boolean, private val classComplexity: ClassComplexity) {

    val packageName = moduleName + "package" + language.postfix + packageIndex
    val classBlueprints = ArrayList<ClassBlueprint>()
    val srcFolder = where.joinPaths(listOf("src", "main", "java"))
    val testFolder = where.joinPaths(listOf("src", "test", "java"))

    var methodToCallFromOutside: MethodToCall

    init {

        var previousClassMethodToCall = methodsToCallWithinPackage
        for (classIndex in 0 until classesPerPackage) {
            val classBlueprint = createClassBlueprint(classIndex, previousClassMethodToCall)
            classBlueprints += classBlueprint
            previousClassMethodToCall = listOf(classBlueprint.getMethodToCallFromOutside()!!)
        }

        methodToCallFromOutside = classBlueprints.first().getMethodToCallFromOutside()!!

        if (generateTests) {
            for (classIndex in 0 until classesPerPackage) {
                classBlueprints += createTestClassBlueprint(classIndex)
            }
        }
    }

    private fun createClassBlueprint(classIndex: Int, methodsToCallWithinClass: List<MethodToCall>): ClassBlueprint =
            when (language) {
                Language.JAVA -> JavaClassBlueprint(packageName, classIndex, methodsPerClass, fieldsPerClass, srcFolder,
                        methodsToCallWithinClass, classComplexity)
                Language.KOTLIN -> KotlinClassBlueprint(packageName, classIndex, methodsPerClass, fieldsPerClass, srcFolder,
                        methodsToCallWithinClass, classComplexity)
            }

    private fun createTestClassBlueprint(classIndex: Int): ClassBlueprint =
            when (language) {
                Language.JAVA -> JavaTestClassBlueprint(packageName, classIndex, methodsPerClass, testFolder)
                Language.KOTLIN -> KotlinTestClassBlueprint(packageName, classIndex, methodsPerClass, testFolder)
            }
}
