package com.google.androidstudiopoet.models

import java.io.File

data class PackagesBlueprint(private val config: ConfigPOJO, private val moduleIndex: Int,
                             val where: String, private val moduleRoot: File) {

    private val javaPackageCount = config.javaPackageCount!!.toInt()
    private val javaClassCount = config.javaClassCount!!.toInt()
    private val javaMethodsPerClass = config.javaMethodsPerClass

    private val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
    private val kotlinClassCount = config.kotlinClassCount!!.toInt()
    private val kotlinMethodsPerClass = config.kotlinMethodsPerClass

    val javaPackageBlueprints = (0 until javaPackageCount).map { packageIndex ->
        PackageBlueprint(packageIndex, moduleIndex, javaClassCount, javaMethodsPerClass, where, moduleRoot)
    }

    val kotlinPackageBlueprints = (0 until kotlinPackageCount).map { packageIndex ->
        PackageBlueprint(packageIndex, moduleIndex, kotlinClassCount, kotlinMethodsPerClass, where, moduleRoot)
    }


}
