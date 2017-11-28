package com.google.androidstudiopoet.generators.android_modules.resources

import com.google.androidstudiopoet.models.ResourcesBlueprint
import com.google.androidstudiopoet.writers.FileWriter

class ResourcesGenerator(private val stringResourcesGenerator: StringResourcesGenerator,
                         private val imageResourcesGenerator: ImagesGenerator,
                         private val layoutResourcesGenerator: LayoutResourcesGenerator,
                         private val fileWriter: FileWriter) {
    fun generate(blueprint: ResourcesBlueprint) {
        stringResourcesGenerator.generate(blueprint)
        imageResourcesGenerator.generate(blueprint)
        fileWriter.mkdir(blueprint.layoutsDir)
        blueprint.layoutBlueprints.forEach { layoutResourcesGenerator.generate(it) }
    }
}