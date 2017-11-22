package com.google.androidstudiopoet

import com.google.androidstudiopoet.generators.BuildGradleGenerator
import com.google.androidstudiopoet.generators.PackagesGenerator
import com.google.androidstudiopoet.generators.android_modules.*
import com.google.androidstudiopoet.generators.packages.JavaGenerator
import com.google.androidstudiopoet.generators.packages.KotlinGenerator
import com.google.androidstudiopoet.generators.project.GradleSettingsGenerator
import com.google.androidstudiopoet.generators.project.ProjectBuildGradleGenerator
import com.google.androidstudiopoet.generators.AndroidModuleGenerator
import com.google.androidstudiopoet.writers.FileWriter
import com.google.androidstudiopoet.writers.SourceModuleWriter

object Injector {

    private val fileWriter = FileWriter()
    private val dependencyValidator = DependencyValidator()
    private val amBuildGradleGenerator = AndroidModuleBuildGradleGenerator(fileWriter)
    private val buildGradleGenerator = BuildGradleGenerator()
    private val gradleSettingsGenerator = GradleSettingsGenerator(fileWriter)
    private val projectBuildGradleGenerator = ProjectBuildGradleGenerator()
    private val stringResourcesGenerator: StringResourcesGenerator = StringResourcesGenerator(fileWriter)
    private val imageResourcesGenerator: ImagesGenerator = ImagesGenerator(fileWriter)
    private val layoutResourcesGenerator: LayoutResourcesGenerator = LayoutResourcesGenerator(fileWriter)
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
                    packagesGenerator,
                    activityGenerator,
                    manifestGenerator, proguardGenerator,
                    amBuildGradleGenerator,
                    fileWriter)

    val modulesWriter =
            SourceModuleWriter(dependencyValidator, buildGradleGenerator, gradleSettingsGenerator,
                    projectBuildGradleGenerator, androidModuleGenerator, packagesGenerator, fileWriter)
}
