package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.MethodBlueprint
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.google.androidstudiopoet.writers.FileWriter
import javax.lang.model.element.Modifier

class JavaGenerator constructor(fileWriter: FileWriter) : PackageGenerator(fileWriter, "Java") {

    override fun generateClass(packageName: String, classNumber: Int, methodsPerClass: Int, mainPackage: String) {
        val className = "Foo" + classNumber

        val clazz = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)

        (0 until methodsPerClass)
                .map { i ->
                    var callStatement: String? = null
                    if (i > 0) {
                        callStatement = "foo" + (i - 1) + "()"
                    } else if (classNumber > 0) {
                        callStatement = "new Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "()"
                    }
                    val statements = listOf(callStatement,
                            // adding lambda
                            "final Runnable anything = () -> System.out.println(\"anything\")")
                    MethodBlueprint(i, statements)
                }
                .map { generateMethod(it) }
                .forEach { clazz.addMethod(it) }

        val javaFile = JavaFile.builder(packageName, clazz.build()).build()

        val classPath = mainPackage + "/" + packageName +
                "/" + className + ".java"

        writeFile(classPath, javaFile.toString())
    }

    private fun generateMethod(blueprint: MethodBlueprint): MethodSpec {

        val method = MethodSpec.methodBuilder(blueprint.methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(Void.TYPE)

        blueprint.statements.forEach { statement -> statement?.let { method.addStatement(it) } }

        return method.build()
    }

}
