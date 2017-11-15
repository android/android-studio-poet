package com.google.androidstudiopoet.models

import java.io.File

data class PackagesBlueprint(private val config: ConfigPOJO, private val moduleIndex: Int,
                             val where: String, private val moduleRoot: File, private val methodsToCallWithinPackages: List<MethodToCall>) {

    private val javaPackageCount = config.javaPackageCount!!.toInt()
    private val javaClassCount = config.javaClassCount!!.toInt()
    private val javaMethodsPerClass = config.javaMethodsPerClass

    private val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
    private val kotlinClassCount = config.kotlinClassCount!!.toInt()
    private val kotlinMethodsPerClass = config.kotlinMethodsPerClass

    val javaPackageBlueprints = ArrayList<PackageBlueprint>()
    val kotlinPackageBlueprints = ArrayList<PackageBlueprint>()
    var methodToCallFromOutside: MethodToCall

    init {
        var previousClassMethodToCall = methodsToCallWithinPackages
        (0 until javaPackageCount).forEach { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, moduleIndex, javaClassCount, javaMethodsPerClass, where, moduleRoot, Language.JAVA, previousClassMethodToCall)
            javaPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        (0 until kotlinPackageCount).map { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, moduleIndex, kotlinClassCount, kotlinMethodsPerClass, where, moduleRoot, Language.KOTLIN, previousClassMethodToCall)
            kotlinPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        methodToCallFromOutside = if (kotlinPackageCount != 0) {
            kotlinPackageBlueprints.last().methodToCallFromOutside
        } else {
            javaPackageBlueprints.last().methodToCallFromOutside
        }
    }

}
