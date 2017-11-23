package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.joinPath

data class ModuleBlueprint(val index: Int, val name: String, val root: String, val dependencies: List<String>,
                           val methodsToCall: List<MethodToCall>, private val config: ConfigPOJO) {

    private val javaPackageCount = config.javaPackageCount!!.toInt()
    private val javaClassCount = if (javaPackageCount > 0) config.javaClassCount!!.toInt() else 0
    private val javaMethodsPerClass = if (javaPackageCount >0 ) config.javaMethodsPerClass else 0

    private val kotlinPackageCount = config.kotlinPackageCount!!.toInt()
    private val kotlinClassCount = if (kotlinPackageCount > 0) config.kotlinClassCount!!.toInt() else 0
    private val kotlinMethodsPerClass = if (kotlinPackageCount > 0) config.kotlinMethodsPerClass else 0

    val moduleRoot = root.joinPath(name)
    val packagesBlueprint = PackagesBlueprint(javaPackageCount, javaClassCount, javaMethodsPerClass, kotlinPackageCount,
            kotlinClassCount, kotlinMethodsPerClass,moduleRoot + "/src/main/java/", name, methodsToCall)

    var methodToCallFromOutside = packagesBlueprint.methodToCallFromOutside
    val useKotlin = config.useKotlin
}