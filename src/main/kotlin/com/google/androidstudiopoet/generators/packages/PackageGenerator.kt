package com.google.androidstudiopoet.generators.packages

import com.google.androidstudiopoet.models.ClassBlueprint
import com.google.androidstudiopoet.models.MethodToCall
import com.google.androidstudiopoet.models.PackageBlueprint
import com.google.androidstudiopoet.writers.FileWriter
import java.io.File

abstract class PackageGenerator(private val fileWriter: FileWriter) {

    fun generatePackage(blueprint: PackageBlueprint): MethodToCall? {

        val packageFolder = File(blueprint.mainPackage, blueprint.packageName)
        if (packageFolder.exists()) {
            packageFolder.delete()
        }

        packageFolder.mkdir()

        blueprint.classBlueprints.forEach({ generateClass(it) })

        return blueprint.methodToCallFromOutside
    }

    abstract fun generateClass(blueprint: ClassBlueprint): MethodToCall

    protected fun writeFile(path: String, content: String) {
        fileWriter.writeToFile(content, path)
    }
}
