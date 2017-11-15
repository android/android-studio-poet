package com.google.androidstudiopoet.models

class KotlinClassBlueprint(packageName: String, classNumber: Int, private val methodsPerClass: Int,
                           private val mainPackage: String, private val methodToCallWithinClass: MethodToCall?):
        ClassBlueprint(packageName, "Foo" + classNumber) {

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    var callStatement: String? = null
                    if (i > 0) {
                        callStatement = "foo" + (i - 1) + "()\n"
                    } else if (methodToCallWithinClass != null) {
                        callStatement = "${methodToCallWithinClass.className}().${methodToCallWithinClass.methodName}()\n"
                    }
                    MethodBlueprint(i, listOf(callStatement))
                }
    }

    override fun getClassPath(): String {
        return "$mainPackage/$packageName/$className.kt"
    }

    override fun getMethodToCallFromOutside(): MethodToCall {
        return MethodToCall(getMethodBlueprints().last().methodName, className)
    }
}