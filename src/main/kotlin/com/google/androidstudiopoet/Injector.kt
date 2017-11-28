package com.google.androidstudiopoet

import com.google.androidstudiopoet.generators.BuildGradleGenerator
import com.google.androidstudiopoet.generators.PackagesGenerator
import com.google.androidstudiopoet.generators.android_modules.*
import com.google.androidstudiopoet.generators.packages.JavaGenerator
import com.google.androidstudiopoet.generators.packages.KotlinGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.generators.android_modules.AndroidModuleGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.ImagesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.LayoutResourcesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.ResourcesGenerator
import com.google.androidstudiopoet.generators.android_modules.resources.StringResourcesGenerator
import com.google.androidstudiopoet.writers.FileWriter
import com.google.androidstudiopoet.writers.SourceModuleWriter

object Injector {

    private val fileWriter = FileWriter()
    private val dependencyValidator = DependencyValidator()
    private val amBuildGradleGenerator = AndroidModuleBuildGradleGenerator(fileWriter)
    private val buildGradleGenerator = BuildGradleGenerator()
    private val gradleSettingsGenerator = GradleSettingsGenerator(fileWriter)
    private val projectBuildGradleGenerator = ProjectBuildGradleGenerator()
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

    private val androidModuleGenerator =
            AndroidModuleGenerator(
                    stringResourcesGenerator,
                    imageResourcesGenerator,
                    layoutResourcesGenerator,
                    resourcesGenerator,
                    packagesGenerator,
                    activityGenerator,
                    manifestGenerator, proguardGenerator,
                    amBuildGradleGenerator,
                    fileWriter)

    val modulesWriter =
            SourceModuleWriter(dependencyValidator, buildGradleGenerator, gradleSettingsGenerator,
                    projectBuildGradleGenerator, androidModuleGenerator, packagesGenerator, fileWriter)
}
