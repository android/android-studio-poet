/*
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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