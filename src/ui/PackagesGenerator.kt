

package ui

import json.ConfigPOJO
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

public class PackagesGenerator {

    private fun createTestConfig(): ConfigPOJO {
        var config = ConfigPOJO()

        config.mainPackage = File(System.getProperty("user.dir") + "/src/").toString()
        config.allMethods = "4000"
        config.javaPackageCount = "4"
        config.javaClassCount = "50"
        config.javaMethodCount = "2000"
        config.kotlinPackageCount = "4"
        config.kotlinClassCount = "50"

        return config
    }

    public fun writePackages(config: ConfigPOJO) {
        println(config.javaPackageCount + " packages, " + config.javaClassCount.toInt() + " classes, " +
                config.allMethods + " methods, " + config.javaMethodsPerClass + " methods per class")

        for (i in 0 until config.javaPackageCount.toInt()) {
            generateJavaPackage(i, config.javaClassCount.toInt(), config.javaMethodsPerClass,
                    File(config.mainPackage))
        }

        val kotlinMethodsPerClass = config.kotlinMethodsPerClass

        for (i in 0 until config.kotlinPackageCount.toInt()) {
            generateKotlinPackage((i + config.javaPackageCount.toInt()),
                    config.kotlinPackageCount.toInt(), kotlinMethodsPerClass, File(config.mainPackage))
        }
    }

    private fun generateJavaPackage(packageNumber: Int, classCounter: Int,
                                    methodsPerClass: Int, mainPackage: File) {
        val packageName = "packageJava" + packageNumber
        val packageFolder = File(mainPackage.toString() + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (i in 0 until classCounter) {
            generateJavaClass(packageName, i, methodsPerClass, mainPackage)
        }
    }

    private fun generateKotlinPackage(packageNumber: Int, classCounter: Int,
                                      methodsPerClass: Int, mainPackage: File) {
        val packageName = "packageKt" + packageNumber
        val packageFolder = File(mainPackage.toString() + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (i in 0 until classCounter) {
            generateKotlinClass(packageName, i, methodsPerClass, mainPackage)
        }
    }

    private fun generateJavaClass(packageName: String, classNumber: Int,
                                  methodsPerClass: Int, mainPackage: File) {

        val className = "Foo" + classNumber
        val buff = StringBuilder()

        buff.append("package $packageName;\n")
        buff.append("public class $className {\n")


        for (i in 0 until methodsPerClass) {

            buff.append("public void foo$i(){\n")

            if (i > 0) {
                buff.append("foo" + (i - 1) + "();\n")
            } else if (classNumber > 0) {
                buff.append("new Foo" + (classNumber - 1) + "().foo" + (methodsPerClass - 1) + "();\n")
            }

            buff.append("\n}")
        }

        buff.append("\n}")

        val classPath = mainPackage.absolutePath + "/" + packageName +
                "/" + className + ".java"

        try {
            val classFile = File(classPath)
            classFile.createNewFile()
            val writer = BufferedWriter(FileWriter(classFile))
            writer.write(buff.toString())
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun generateKotlinClass(packageName: String, classNumber: Int,
                                    methodsPerClass: Int, mainPackage: File) {

        val className = "Foo" + classNumber
        val buff = StringBuilder()

        buff.append("package $packageName;\n")
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

        val classPath = mainPackage.absolutePath + "/" + packageName +
                "/" + className + ".kt"

        try {
            val classFile = File(classPath)
            classFile.createNewFile()
            val writer = BufferedWriter(FileWriter(classFile))
            writer.write(buff.toString())
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val packagesGenerator = PackagesGenerator()
            val config = packagesGenerator.createTestConfig()
            packagesGenerator.writePackages(config)
        }
    }
}

