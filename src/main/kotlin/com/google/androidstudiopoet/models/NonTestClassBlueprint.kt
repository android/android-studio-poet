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

abstract class NonTestClassBlueprint(packageName: String, className: String, val methodsPerClass: Int,
                                     private val classComplexity: ClassComplexity) : ClassBlueprint(packageName, className) {

    override fun getMethodToCallFromOutside(): MethodToCall? =
            MethodToCall(getMethodBlueprints().last().methodName, fullClassName)

    fun lambdaCountInMethod(methodNumber: Int) = classComplexity.lambdaCount / methodsPerClass +
                if (((classComplexity.lambdaCount % methodsPerClass) - methodNumber) > 0) 1 else 0
}