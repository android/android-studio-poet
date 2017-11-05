package ui.writers

import ui.generators.android_modules.*
import ui.generators.packages.JavaGenerator
import ui.generators.packages.KotlinGenerator
import ui.models.AndroidModuleBlueprint
import utils.joinPath

class AndroidModuleWriter(private val stringResourcesGenerator: StringResourcesGenerator,
                          private val imageResourcesGenerator: ImagesGenerator,
                          private val layoutResourcesGenerator: LayoutResourcesGenerator,
                          private val javaGenerator: JavaGenerator,
                          private val kotlinGenerator: KotlinGenerator,
                          private val activityGenerator: ActivityGenerator,
                          private val manifestGenerator: ManifestGenerator,
                          private val proguardGenerator: ProguardGenerator,
                          private val buildGradleGenerator: AndroidModuleBuildGradleGenerator,
                          private val fileWriter: FileWriter) {

    /**
     *  Generate android module, including module folder
     */
    fun generate(blueprint: AndroidModuleBlueprint) {

        generateMainFolders(blueprint)

        proguardGenerator.generate(blueprint)
        buildGradleGenerator.generate(blueprint)

        val stringResources = stringResourcesGenerator.generate(blueprint)
        val imageResources = imageResourcesGenerator.generate(blueprint)
        val layouts = layoutResourcesGenerator.generate(blueprint, stringResources, imageResources)
//        val javaMethodsToCall = javaGenerator.generatePackage() after packageBlueprint is created use it here
//        val kotlinMethodsToCall = kotlinGenerator.generatePackage() after packageBlueprint is created use it here
        val methodsToCall: List<String> = listOf()
        val activities = activityGenerator.generate(blueprint, layouts, methodsToCall)
        manifestGenerator.generate(blueprint, activities)
    }

    private fun generateMainFolders(blueprint: AndroidModuleBlueprint) {
        val moduleRoot = blueprint.moduleRoot
        fileWriter.mkdir(moduleRoot)
        fileWriter.mkdir(moduleRoot.joinPath("libs"))
        fileWriter.mkdir(blueprint.srcPath)
        fileWriter.mkdir(blueprint.mainPath)
        fileWriter.mkdir(blueprint.codePath)
        fileWriter.mkdir(blueprint.resDirPath)
    }
}
