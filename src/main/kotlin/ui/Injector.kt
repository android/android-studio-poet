package ui

import ui.generators.BuildGradleGenerator
import ui.generators.PackagesGenerator
import ui.generators.android_modules.*
import ui.generators.packages.JavaGenerator
import ui.generators.packages.KotlinGenerator
import ui.generators.project.GradleSettingsGenerator
import ui.generators.project.ProjectBuildGradleGenerator

object Injector {

    private val fileWriter = FileWriter()
    private val dependencyValidator = DependencyValidator()
    private val moduleBlueprintFactory = ModuleBlueprintFactory()
    private val buildGradleGenerator = BuildGradleGenerator()
    private val gradleSettingsGenerator = GradleSettingsGenerator(fileWriter)
    private val projectBuildGradleGenerator = ProjectBuildGradleGenerator()
    private val stringResourcesGenerator: StringResourcesGenerator = StringResourcesGenerator()
    private val imageResourcesGenerator: ImageResourcesGenerator = ImageResourcesGenerator()
    private val layoutResourcesGenerator: LayoutResourcesGenerator = LayoutResourcesGenerator()
    private val javaGenerator: JavaGenerator = JavaGenerator(fileWriter)
    private val kotlinGenerator: KotlinGenerator = KotlinGenerator(fileWriter)
    private val activityGenerator: ActivityGenerator = ActivityGenerator()
    private val manifestGenerator: ManifestGenerator = ManifestGenerator()

    private val androidModuleGenerator = AndroidModuleGenerator(stringResourcesGenerator, imageResourcesGenerator,
            layoutResourcesGenerator, javaGenerator, kotlinGenerator, activityGenerator, manifestGenerator)

    private val packagesGenerator = PackagesGenerator(javaGenerator, kotlinGenerator)

    val modulesWriter = ModulesWriter(dependencyValidator, moduleBlueprintFactory, buildGradleGenerator,
            gradleSettingsGenerator, projectBuildGradleGenerator, androidModuleGenerator, packagesGenerator, fileWriter)
}
