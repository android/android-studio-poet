package main

import main.generators.BuildGradleGenerator
import main.generators.PackagesGenerator
import main.generators.android_modules.*
import main.generators.packages.JavaGenerator
import main.generators.packages.KotlinGenerator
import main.generators.project.GradleSettingsGenerator
import main.generators.project.ProjectBuildGradleGenerator
import main.writers.AndroidModuleWriter
import main.writers.FileWriter
import main.writers.SourceModuleWriter

object Injector {

    private val fileWriter = FileWriter()
    private val dependencyValidator = DependencyValidator()
    private val moduleBlueprintFactory = ModuleBlueprintFactory()
    private val amBuildGradleGenerator = AndroidModuleBuildGradleGenerator(fileWriter)
    private val buildGradleGenerator = BuildGradleGenerator()
    private val gradleSettingsGenerator = GradleSettingsGenerator(fileWriter)
    private val projectBuildGradleGenerator = ProjectBuildGradleGenerator()
    private val stringResourcesGenerator: StringResourcesGenerator = StringResourcesGenerator(fileWriter)
    private val imageResourcesGenerator: ImagesGenerator = ImagesGenerator(fileWriter)
    private val layoutResourcesGenerator: LayoutResourcesGenerator = LayoutResourcesGenerator()
    private val javaGenerator: JavaGenerator = JavaGenerator(fileWriter)
    private val kotlinGenerator: KotlinGenerator = KotlinGenerator(fileWriter)
    private val activityGenerator: ActivityGenerator = ActivityGenerator()
    private val manifestGenerator: ManifestGenerator = ManifestGenerator()
    private val proguardGenerator: ProguardGenerator = ProguardGenerator(fileWriter)

    private val androidModuleGenerator =
            AndroidModuleWriter(
                    stringResourcesGenerator,
                    imageResourcesGenerator,
                    layoutResourcesGenerator,
                    javaGenerator,
                    kotlinGenerator,
                    activityGenerator,
                    manifestGenerator, proguardGenerator,
                    amBuildGradleGenerator,
                    fileWriter)

    private val packagesGenerator =
            PackagesGenerator(javaGenerator, kotlinGenerator)

    val modulesWriter =
            SourceModuleWriter(dependencyValidator, moduleBlueprintFactory, buildGradleGenerator,
            gradleSettingsGenerator, projectBuildGradleGenerator, androidModuleGenerator, packagesGenerator, fileWriter)
}
