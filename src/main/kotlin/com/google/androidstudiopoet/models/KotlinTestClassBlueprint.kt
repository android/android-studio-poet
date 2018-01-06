package com.google.androidstudiopoet.models

import org.junit.Test

class KotlinTestClassBlueprint(packageName: String, private val classNumber: Int, private val methodsPerClass: Int,
                               private val mainPackage: String) :
        ClassBlueprint(packageName, "Foo${classNumber}Test") {

    override fun getMethodBlueprints(): List<MethodBlueprint> {
        return (0 until methodsPerClass)
                .map { i ->
                    val statements = listOf("Foo$classNumber().foo$i()")
                    MethodBlueprint("testFoo$i", statements, listOf(Test::class.java))
                }
    }

    override fun getClassPath(): String = "$mainPackage/$packageName/$className.kt"

    override fun getMethodToCallFromOutside(): MethodToCall? = null
}