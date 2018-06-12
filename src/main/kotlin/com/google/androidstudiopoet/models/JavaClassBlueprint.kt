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

class JavaClassBlueprint(packageName: String, classNumber: Int, methodsPerClass: Int, fieldsPerClass: Int,
                         private val where: String, private val methodsToCallWithinClass: List<MethodToCall>, classComplexity: ClassComplexity) :
        NonTestClassBlueprint(packageName, "Foo" + classNumber, methodsPerClass, classComplexity) {
    override fun getFieldBlueprints(): List<FieldBlueprint> {
        return listOf()
    }

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    val statements = ArrayList<String>()

                    // adding lambdas
                    for (j in 0 until lambdaCountInMethod(i)) {
                        statements += getLambda(j)
                    }
                    if (i > 0) {
                        statements += "foo" + (i - 1) + "()"
                    } else if (!methodsToCallWithinClass.isEmpty()) {
                        methodsToCallWithinClass.forEach({ statements += "new ${it.className}().${it.methodName}()" })

                    }

                    MethodBlueprint("foo$i", statements)
                }
    }

    override fun getClassPath(): String = "$where/$packageName/$className.java"

    private fun getLambda(lambdaNumber: Int) = "final Runnable anything$lambdaNumber = () -> System.out.println(\"anything\")"
}