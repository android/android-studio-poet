package com.google.androidstudiopoet.models

class JavaClassBlueprint(packageName: String, classNumber: Int, private val methodsPerClass: Int,
                         private val mainPackage: String, private val methodToCallWithinClass: MethodToCall?):
        ClassBlueprint(packageName, "Foo" + classNumber) {

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    var callStatement: String? = null
                    if (i > 0) {
                        callStatement = "foo" + (i - 1) + "()"
                    } else if (methodToCallWithinClass != null) {
                        callStatement = "new ${methodToCallWithinClass.className}().${methodToCallWithinClass.methodName}()"
                    }
                    val statements = listOf(callStatement,
                            // adding lambda
                            "final Runnable anything = () -> System.out.println(\"anything\")")
                    MethodBlueprint(i, statements)
                }
    }

    override fun getClassPath(): String {
        return "$mainPackage/$packageName/$className.java"
    }

    override fun getMethodToCallFromOutside(): MethodToCall {
        return MethodToCall(getMethodBlueprints().last().methodName, className)
    }
}