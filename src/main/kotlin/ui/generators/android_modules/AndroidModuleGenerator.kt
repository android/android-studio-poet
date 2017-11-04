package ui.generators.android_modules

import ui.FileWriter
import ui.generators.packages.JavaGenerator
import ui.generators.packages.KotlinGenerator
import ui.models.AndroidModuleBlueprint
import utils.joinPath
import java.io.File

class AndroidModuleGenerator(private val stringResourcesGenerator: StringResourcesGenerator,
                             private val imageResourcesGenerator: ImageResourcesGenerator,
                             private val layoutResourcesGenerator: LayoutResourcesGenerator,
                             private val javaGenerator: JavaGenerator,
                             private val kotlinGenerator: KotlinGenerator,
                             private val activityGenerator: ActivityGenerator,
                             private val manifestGenerator: ManifestGenerator,
                             private val fileWriter: FileWriter) {

    /**
     *  Generate android module, including module folder
     */
    fun generate(blueprint: AndroidModuleBlueprint) {

        generateMainFolders(blueprint)

        // TODO add one for Android package,
        //writeBuildGradle(moduleRootFile, androidModuleBlueprint)
        writeProguard()

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

    //TODO Write a generator for it and build.gradle, to keep this class concise
    private fun writeProguard() {

    }
}
