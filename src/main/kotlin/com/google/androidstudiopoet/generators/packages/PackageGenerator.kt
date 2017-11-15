package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

abstract class PackageGenerator(private val fileWriter: FileWriter, private val postfix: String) {

    fun generatePackage(packageIndex: Int, moduleIndex: Int, classesPerPackage: Int,
                              methodsPerClass: Int, mainPackage: File, moduleRoot: File) {
        val packageName = moduleRoot.name + moduleIndex +"package" + postfix + packageIndex
        val packageFolder = File(mainPackage.toString() + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (classIndex in 0 until classesPerPackage) {
            generateClass(packageName, classIndex, methodsPerClass, mainPackage)
        }
    }

    abstract fun generateClass(packageName: String, classNumber: Int,
                                         methodsPerClass: Int, mainPackage: File)

    protected fun writeFile(path: String, content: String) {
        fileWriter.writeToFile(content, path)
    }
}
