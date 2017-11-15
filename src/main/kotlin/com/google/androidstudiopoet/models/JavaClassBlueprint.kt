package com.google.androidstudiopoet.models

class JavaClassBlueprint(packageName: String, classNumber: Int, private val methodsPerClass: Int,
                         private val mainPackage: String, private val methodsToCallWithinClass: List<MethodToCall>) :
        ClassBlueprint(packageName, "Foo" + classNumber) {

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    val statements = ArrayList<String>()
                    // adding lambda
                    statements += "final Runnable anything = () -> System.out.println(\"anything\")"
                    if (i > 0) {
                        statements += "foo" + (i - 1) + "()"
                    } else if (!methodsToCallWithinClass.isEmpty()) {
                        methodsToCallWithinClass.forEach({ statements += "new ${it.className}().${it.methodName}()" })

                    }

                    MethodBlueprint(i, statements)
                }
    }

    override fun getClassPath(): String {
        return "$mainPackage/$packageName/$className.java"
    }

    override fun getMethodToCallFromOutside(): MethodToCall {
        return MethodToCall(getMethodBlueprints().last().methodName, fullClassName)
    }
}