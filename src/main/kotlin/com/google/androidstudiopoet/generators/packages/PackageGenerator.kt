package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.PackageBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

abstract class PackageGenerator(private val fileWriter: FileWriter, private val postfix: String) {

    fun generatePackage(blueprint: PackageBlueprint) {
        val packageName = blueprint.moduleRoot.name + blueprint.moduleIndex +"package" + postfix + blueprint.packageIndex
        val packageFolder = File(blueprint.mainPackage + "/" + packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        for (classIndex in 0 until blueprint.classesPerPackage) {
            generateClass(packageName, classIndex, blueprint.methodsPerClass, blueprint.mainPackage)
        }
    }

    abstract fun generateClass(packageName: String, classNumber: Int,
                                         methodsPerClass: Int, mainPackage: String)

    protected fun writeFile(path: String, content: String) {
        fileWriter.writeToFile(content, path)
    }
}
