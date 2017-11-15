package com.google.androidstudiopoet.models

import java.io.File

data class PackageBlueprint(private val packageIndex: Int, private val moduleIndex: Int, private val classesPerPackage: Int,
                            val methodsPerClass: Int, val mainPackage: String, private val moduleRoot: File,
                            val language: Language, private val methodToCallWithinClass: MethodToCall?) {

    val packageName = moduleRoot.name + moduleIndex + "package" + language.postfix + packageIndex
    val classBlueprints = ArrayList<ClassBlueprint>()

    var methodToCallFromOutside: MethodToCall?
    init {

        var previousClassMethodToCall: MethodToCall? = methodToCallWithinClass
        for (classIndex in 0 until classesPerPackage) {
            val classBlueprint = createClassBlueprint(classIndex, previousClassMethodToCall)
            classBlueprints += classBlueprint
            previousClassMethodToCall = classBlueprint.getMethodToCallFromOutside()
        }

        methodToCallFromOutside = previousClassMethodToCall
    }


    private fun createClassBlueprint(classIndex: Int, previousClassMethodToCall: MethodToCall?): ClassBlueprint =
            when(language) {
                Language.JAVA -> JavaClassBlueprint(packageName, classIndex, methodsPerClass, mainPackage, previousClassMethodToCall)
                Language.KOTLIN -> KotlinClassBlueprint(packageName, classIndex, methodsPerClass, mainPackage, previousClassMethodToCall)
            }

}
