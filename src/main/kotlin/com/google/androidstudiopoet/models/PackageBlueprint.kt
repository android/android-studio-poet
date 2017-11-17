package com.google.androidstudiopoet.models

data class PackageBlueprint(private val packageIndex: Int, private val classesPerPackage: Int,
                            private val methodsPerClass: Int, val mainPackage: String, private val moduleName: String,
                            val language: Language, private val methodsToCallWithinPackage: List<MethodToCall>) {

    val packageName = moduleName + "package" + language.postfix + packageIndex
    val classBlueprints = ArrayList<ClassBlueprint>()

    var methodToCallFromOutside: MethodToCall

    init {

        var previousClassMethodToCall = methodsToCallWithinPackage
        for (classIndex in 0 until classesPerPackage) {
            val classBlueprint = createClassBlueprint(classIndex, previousClassMethodToCall)
            classBlueprints += classBlueprint
            previousClassMethodToCall = listOf(classBlueprint.getMethodToCallFromOutside())
        }

        methodToCallFromOutside = classBlueprints.last().getMethodToCallFromOutside()
    }


    private fun createClassBlueprint(classIndex: Int, methodsToCallWithinClass: List<MethodToCall>): ClassBlueprint =
            when (language) {
                Language.JAVA -> JavaClassBlueprint(packageName, classIndex, methodsPerClass, mainPackage, methodsToCallWithinClass)
                Language.KOTLIN -> KotlinClassBlueprint(packageName, classIndex, methodsPerClass, mainPackage, methodsToCallWithinClass)
            }

}
