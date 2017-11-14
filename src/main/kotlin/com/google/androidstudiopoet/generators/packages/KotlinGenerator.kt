package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

class KotlinGenerator constructor(fileWriter: FileWriter): PackageGenerator(fileWriter, "Kt") {

    override fun generateClass(packageName: String, classNumber: Int, methodsPerClass: Int, mainPackage: String) {
        val className = "Foo" + classNumber
        val buff = StringBuilder()

        buff.append("package $packageName;\n")

        var annotName = className + "Fancy"
        buff.append("annotation class " + annotName + "\n")
        buff.append("@" + annotName + "\n")

        buff.append("public class $className {\n")

        for (i in 0 until methodsPerClass) {

            buff.append("fun foo$i(){\n")

            if (i > 0) {
                buff.append("foo" + (i - 1) + "()\n")
            } else if (classNumber > 0) {
                buff.append("Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "()\n")
            }

            buff.append("\n}")
        }

        buff.append("\n}")

        val classPath = mainPackage + "/" + packageName +
                "/" + className + ".kt"

        writeFile(classPath, buff.toString())
    }

}
