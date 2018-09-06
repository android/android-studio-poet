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

package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.generators.PackagesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.ResourcesGenerator
import com.google.androidstudiopoet.models.AndroidModuleBlueprint
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter
import java.util.*

class AndroidModuleGenerator(private val resourcesGenerator: ResourcesGenerator,
                             private val packagesGenerator: PackagesGenerator,
                             private val activityGenerator: ActivityGenerator,
                             private val manifestGenerator: ManifestGenerator,
                             private val proguardGenerator: ProguardGenerator,
                             private val buildGradleGenerator: AndroidModuleBuildGradleGenerator,
                             private val buildBazelGenerator: AndroidModuleBuildBazelGenerator,
                             private val fileWriter: FileWriter) {

    /**
     *  Generate android module, including module folder
     */
    fun generate(blueprint: AndroidModuleBlueprint, random: Random, generateBazelFiles: Boolean) {
        generateMainFolders(blueprint)

        proguardGenerator.generate(blueprint)
        buildGradleGenerator.generate(blueprint.buildGradleBlueprint)
        blueprint.resourcesBlueprint?.let { resourcesGenerator.generate(it, random) }
        packagesGenerator.writePackages(blueprint.packagesBlueprint)
        blueprint.activityBlueprints.forEach({ activityGenerator.generate(it) })
        manifestGenerator.generate(blueprint)

        if (generateBazelFiles) {
            buildBazelGenerator.generate(blueprint.buildBazelBlueprint)
        }
    }

    private fun generateMainFolders(blueprint: AndroidModuleBlueprint) {
        val moduleRoot = blueprint.moduleRoot
        fileWriter.mkdir(moduleRoot)
        fileWriter.mkdir(moduleRoot.joinPath("libs"))
        fileWriter.mkdir(blueprint.srcPath)
        fileWriter.mkdir(blueprint.mainPath)
        fileWriter.mkdir(blueprint.codePath)
        fileWriter.mkdir(blueprint.resDirPath)
        fileWriter.mkdir(blueprint.packagePath)
    }
}
