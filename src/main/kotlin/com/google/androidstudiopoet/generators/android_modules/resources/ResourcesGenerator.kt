package com.google.androidstudiopoet.generators.android_modules.resources

import com.google.androidstudiopoet.models.ResourcesBlueprint

class ResourcesGenerator(private val stringResourcesGenerator: StringResourcesGenerator,
                         private val imageResourcesGenerator: ImagesGenerator, private val layoutResourcesGenerator: LayoutResourcesGenerator) {
    fun generate(blueprint: ResourcesBlueprint) {
        stringResourcesGenerator.generate(blueprint)
        imageResourcesGenerator.generate(blueprint)
        layoutResourcesGenerator.generate(blueprint)
    }
}