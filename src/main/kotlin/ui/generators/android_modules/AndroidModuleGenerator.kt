package ui.generators.android_modules

import ui.generators.packages.JavaGenerator
import ui.generators.packages.KotlinGenerator
import ui.models.AndroidModuleBlueprint
import java.io.File

class AndroidModuleGenerator(private val stringResourcesGenerator: StringResourcesGenerator,
                             private val imageResourcesGenerator: ImageResourcesGenerator,
                             private val layoutResourcesGenerator: LayoutResourcesGenerator,
                             private val javaGenerator: JavaGenerator,
                             private val kotlinGenerator: KotlinGenerator,
                             private val activityGenerator: ActivityGenerator,
                             private val manifestGenerator: ManifestGenerator) {

    /**
     *  Generate android module, including module folder
     */
    fun generate(blueprint: AndroidModuleBlueprint) {

        val moduleRootFile = File(blueprint.moduleRoot)
        moduleRootFile.mkdir()

        writeLibsFolder(moduleRootFile)

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

    private fun writeLibsFolder(moduleRootFile: File) {
        // write libs
        val libRoot = moduleRootFile.toString() + "/libs/"
        File(libRoot).mkdir()
    }

    //TODO Write a generator for it and build.gradle, to keep this class concise
    private fun writeProguard() {

    }
}
