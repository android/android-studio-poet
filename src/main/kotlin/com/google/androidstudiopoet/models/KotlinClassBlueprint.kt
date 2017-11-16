package com.google.androidstudiopoet.models

class KotlinClassBlueprint(packageName: String, classNumber: Int, private val methodsPerClass: Int,
                           private val mainPackage: String, private val methodsToCallWithinClass: List<MethodToCall>) :
        ClassBlueprint(packageName, "Foo" + classNumber) {

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    val statements = ArrayList<String>()
                    if (i > 0) {
                        statements += "foo" + (i - 1) + "()\n"
                    } else if (!methodsToCallWithinClass.isEmpty()) {
                        methodsToCallWithinClass.forEach { statements += "${it.className}().${it.methodName}()\n" }

                    }
                    MethodBlueprint(i, statements)
                }
    }

    override fun getClassPath(): String {
        return "$mainPackage/$packageName/$className.kt"
    }

    override fun getMethodToCallFromOutside(): MethodToCall {
        return MethodToCall(getMethodBlueprints().last().methodName, fullClassName)
    }
}