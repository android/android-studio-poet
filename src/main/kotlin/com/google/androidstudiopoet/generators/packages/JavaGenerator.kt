package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.*
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import com.google.androidstudiopoet.writers.FileWriter
import javax.lang.model.element.Modifier

class JavaGenerator constructor(fileWriter: FileWriter) : PackageGenerator(fileWriter, "Java") {
    override fun createClassBlueprint(packageName: String, classIndex: Int, blueprint: PackageBlueprint, previousClassMethodToCall: MethodToCall?): ClassBlueprint {
        return JavaClassBlueprint(packageName, classIndex, blueprint.methodsPerClass, blueprint.mainPackage, previousClassMethodToCall)
    }

    override fun generateClass(blueprint: ClassBlueprint): MethodToCall {

        val classSpec = blueprint.getMethodBlueprints()
                .map { generateMethod(it) }
                .fold(getClazz(blueprint)) { acc, methodSpec -> acc.addMethod(methodSpec) }
                .build()

        val javaFile = JavaFile.builder(blueprint.packageName, classSpec).build()

        writeFile(blueprint.getClassPath(), javaFile.toString())
        return blueprint.getMethodToCallFromOutside()
    }

    private fun getClazz(blueprint: ClassBlueprint) =
            TypeSpec.classBuilder(blueprint.className)
                    .addModifiers(Modifier.PUBLIC)

    private fun generateMethod(blueprint: MethodBlueprint): MethodSpec {

        val method = MethodSpec.methodBuilder(blueprint.methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(Void.TYPE)

        blueprint.statements.forEach { statement -> statement?.let { method.addStatement(it) } }

        return method.build()
    }

}
