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

package com.google.androidstudiopoet

import com.google.androidstudiopoet.converters.*
import com.google.androidstudiopoet.deserializers.DependencyConfigDeserializer
import com.google.androidstudiopoet.deserializers.ModuleConfigDeserializer
import com.google.androidstudiopoet.generators.*
import com.google.androidstudiopoet.generators.android_modules.*
import com.google.androidstudiopoet.generators.android_modules.resources.ImagesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.LayoutResourcesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.ResourcesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.StringResourcesGenerator
import com.google.androidstudiopoet.generators.packages.JavaGenerator
import com.google.androidstudiopoet.generators.packages.KotlinGenerator
import com.google.androidstudiopoet.generators.project.GradlePropertiesGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.input.DependencyConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.writers.FileWriter
import com.google.androidstudiopoet.writers.ImageWriter
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Injector {

    private val fileWriter = FileWriter()
    private val imageWriter = ImageWriter()
    val dependencyValidator = DependencyValidator()
    private val amBuildGradleGenerator = AndroidModuleBuildGradleGenerator(fileWriter)
    private val amBazelBuildGenerator = AndroidModuleBuildBazelGenerator(fileWriter)
    private val buildGradleGenerator = ModuleBuildGradleGenerator(fileWriter)
    private val buildBazelGenerator = ModuleBuildBazelGenerator(fileWriter)
    private val bazelWorkspaceGenerator = BazelWorkspaceGenerator(fileWriter)
    private val gradleSettingsGenerator = GradleSettingsGenerator(fileWriter)
    private val projectBuildGradleGenerator = ProjectBuildGradleGenerator(fileWriter)
    private val gradlePropertiesGenerator = GradlePropertiesGenerator(fileWriter)
    private val stringResourcesGenerator = StringResourcesGenerator(fileWriter)
    private val imageResourcesGenerator = ImagesGenerator(fileWriter)
    private val layoutResourcesGenerator = LayoutResourcesGenerator(fileWriter)
    private val resourcesGenerator = ResourcesGenerator(stringResourcesGenerator, imageResourcesGenerator,
            layoutResourcesGenerator, fileWriter)
    private val javaGenerator: JavaGenerator = JavaGenerator(fileWriter)
    private val kotlinGenerator: KotlinGenerator = KotlinGenerator(fileWriter)
    private val activityGenerator: ActivityGenerator = ActivityGenerator(fileWriter)
    private val manifestGenerator: ManifestGenerator = ManifestGenerator(fileWriter)
    private val proguardGenerator: ProguardGenerator = ProguardGenerator(fileWriter)
    private val packagesGenerator = PackagesGenerator(javaGenerator, kotlinGenerator)
    private val dependencyImageGenerator = DependencyImageGenerator(imageWriter)
    private val dependencyGraphGenerator = DependencyGraphGenerator(fileWriter, dependencyImageGenerator)
    private val jsonConfigGenerator = JsonConfigGenerator(fileWriter)

    private val configPojoToFlavourConfigsConverter = ConfigPojoToFlavourConfigsConverter()
    private val configPojoToBuildTypeConfigsConverter = ConfigPojoToBuildTypeConfigsConverter()
    private val configPojoToAndroidModuleConfigConverter = ConfigPojoToAndroidModuleConfigConverter()
    private val configPojoToModuleConfigConverter = ConfigPojoToModuleConfigConverter()
    private val configPojoToBuildSystemConfigConverter = ConfigPojoToBuildSystemConfigConverter()
    val configPojoToProjectConfigConverter = ConfigPojoToProjectConfigConverter(configPojoToModuleConfigConverter,
            configPojoToFlavourConfigsConverter, configPojoToBuildTypeConfigsConverter,
            configPojoToAndroidModuleConfigConverter, configPojoToBuildSystemConfigConverter)

    private val androidModuleGenerator =
            AndroidModuleGenerator(
                    resourcesGenerator,
                    packagesGenerator,
                    activityGenerator,
                    manifestGenerator, proguardGenerator,
                    amBuildGradleGenerator,
                    amBazelBuildGenerator,
                    fileWriter)

    val modulesWriter =
            SourceModuleGenerator(
                buildGradleGenerator,
                buildBazelGenerator,
                bazelWorkspaceGenerator,
                gradleSettingsGenerator,
                gradlePropertiesGenerator,
                projectBuildGradleGenerator,
                androidModuleGenerator,
                packagesGenerator,
                dependencyGraphGenerator,
                jsonConfigGenerator,
                fileWriter)


    private val moduleConfigDeserializer = ModuleConfigDeserializer()
    private val dependencyConfigDeserializer = DependencyConfigDeserializer()

    val gson: Gson by lazy {
        GsonBuilder()
                .registerTypeAdapter(ModuleConfig::class.java, moduleConfigDeserializer)
                .registerTypeAdapter(DependencyConfig::class.java, dependencyConfigDeserializer)
                .create()
    }
}
