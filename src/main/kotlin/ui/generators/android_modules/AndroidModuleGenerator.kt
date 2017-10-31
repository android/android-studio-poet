package ui.generators.android_modules

import ui.generators.packages.JavaGenerator
import ui.generators.packages.KotlinGenerator
import ui.models.AndroidModuleBlueprint

class AndroidModuleGenerator(private val stringResourcesGenerator: StringResourcesGenerator,
                             private val imageResourcesGenerator: ImageResourcesGenerator,
                             private val layoutResourcesGenerator: LayoutResourcesGenerator,
                             private val javaGenerator: JavaGenerator,
                             private val kotlinGenerator: KotlinGenerator,
                             private val activityGenerator: ActivityGenerator,
                             private val manifestGenerator: ManifestGenerator) {
    fun generate(blueprint: AndroidModuleBlueprint) {
        val stringResources = stringResourcesGenerator.generate(blueprint)
        val imageResources = imageResourcesGenerator.generate(blueprint)
        val layouts = layoutResourcesGenerator.generate(blueprint, stringResources, imageResources)
//        val javaMethodsToCall = javaGenerator.generatePackage() after packageBlueprint is created use it here
//        val kotlinMethodsToCall = kotlinGenerator.generatePackage() after packageBlueprint is created use it here
        val methodsToCall: List<String> = listOf()
        val activities = activityGenerator.generate(blueprint, layouts, methodsToCall)
        manifestGenerator.generate(blueprint, activities)

    }
}
