package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.models.MethodToCall
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

        val classBlueprints = ArrayList<ClassBlueprint>()
        var previousClassMethodToCall: MethodToCall? = null
        for (classIndex in 0 until blueprint.classesPerPackage) {
            val classBlueprint = createClassBlueprint(packageName, classIndex, blueprint, previousClassMethodToCall)
            classBlueprints += classBlueprint
            previousClassMethodToCall = generateClass(classBlueprint)

        }
    }

    abstract fun createClassBlueprint(packageName: String, classIndex: Int, blueprint: PackageBlueprint,
                                      previousClassMethodToCall: MethodToCall?): ClassBlueprint

    abstract fun generateClass(blueprint: ClassBlueprint): MethodToCall

    protected fun writeFile(path: String, content: String) {
        fileWriter.writeToFile(content, path)
    }
}
