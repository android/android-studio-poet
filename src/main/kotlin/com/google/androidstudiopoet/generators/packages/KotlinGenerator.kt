package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.*
import com.google.androidstudiopoet.writers.FileWriter

class KotlinGenerator constructor(fileWriter: FileWriter) : PackageGenerator(fileWriter, "Kt") {
    override fun createClassBlueprint(packageName: String, classIndex: Int, blueprint: PackageBlueprint, previousClassMethodToCall: MethodToCall?): ClassBlueprint {
        return KotlinClassBlueprint(packageName, classIndex, blueprint.methodsPerClass, blueprint.mainPackage, previousClassMethodToCall)
    }

    override fun generateClass(blueprint: ClassBlueprint): MethodToCall {
        val buff = StringBuilder()

        buff.append("package $blueprint.packageName;\n")

        val annotName = blueprint.className + "Fancy"
        buff.append("annotation class " + annotName + "\n")
        buff.append("@" + annotName + "\n")

        buff.append("public class ${blueprint.className} {\n")

        blueprint.getMethodBlueprints()
                .map { generateMethod(it) }
                .fold(buff) { acc, method -> acc.append(method) }

        buff.append("\n}")

        writeFile(blueprint.getClassPath(), buff.toString())
        return blueprint.getMethodToCallFromOutside()
    }

    private fun generateMethod(blueprint: MethodBlueprint): String {
        val buff = StringBuilder()
        buff.append("fun ${blueprint.methodName}(){\n")
        blueprint.statements.forEach { statement -> statement?.let { buff.append(it) } }
        buff.append("\n}")
        return buff.toString()
    }

}
