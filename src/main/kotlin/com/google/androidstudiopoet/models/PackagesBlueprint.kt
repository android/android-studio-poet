package com.google.androidstudiopoet.models

data class PackagesBlueprint(private val javaPackageCount: Int,
                             private val javaClassCount: Int,
                             private val javaMethodsPerClass: Int,
                             private val kotlinPackageCount: Int,
                             private val kotlinClassCount: Int,
                             private val kotlinMethodsPerClass: Int,
                             val where: String,
                             private val moduleName: String,
                             private val methodsToCallWithin: List<MethodToCall>) {

    val javaPackageBlueprints = ArrayList<PackageBlueprint>()
    val kotlinPackageBlueprints = ArrayList<PackageBlueprint>()
    var methodToCallFromOutside: MethodToCall

    init {
        var previousClassMethodToCall:List<MethodToCall> = methodsToCallWithin
        (0 until javaPackageCount).forEach { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, javaClassCount, javaMethodsPerClass, where, moduleName, Language.JAVA, previousClassMethodToCall)
            javaPackageBlueprints += packageBlueprint
            previousClassMethodToCall = listOf(packageBlueprint.methodToCallFromOutside)
        }

        (0 until kotlinPackageCount).map { packageIndex ->
            val packageBlueprint = PackageBlueprint(packageIndex, kotlinClassCount, kotlinMethodsPerClass, where, moduleName, Language.KOTLIN, previousClassMethodToCall)
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
