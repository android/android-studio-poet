package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ResourcesConfig
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.mock
import org.junit.Test

class AndroidModuleBlueprintTest {
    private val resourcesConfig0: ResourcesConfig = mock()
    private val dependency: ModuleDependency = mock()

    @Test
    fun `blueprint create proper activity names`() {
        val blueprint = getAndroidModuleBlueprint(numOfActivities = 2)

        blueprint.activityNames.assertEquals(listOf("Activity0", "Activity1"))
    }

    @Test
    fun `blueprint creates proper flavors and dimensions`() {
        val dimension1 = "dim1"
        val dimension2 = "dim2"
        val flavorName1 = "flav1"
        val flavorName2 = "flav2"
        val flavorName3 = "flav3"
        val flavorConfigs = listOf(
                FlavorConfig(flavorName1, dimension1),
                FlavorConfig(flavorName2, dimension1),
                FlavorConfig(flavorName3, dimension2))
        val blueprint = getAndroidModuleBlueprint(productFlavorConfigs = flavorConfigs)

        blueprint.flavorDimensions!!.assertEquals(setOf(dimension1, dimension2))
        blueprint.productFlavors!!.assertEquals(setOf(
                Flavor(flavorName1, dimension1),
                Flavor(flavorName2, dimension1),
                Flavor(flavorName3, dimension2)
        ))
    }


    private fun getAndroidModuleBlueprint(
            name: String = "androidAppModule1",
            numOfActivities: Int = 1,
            resourcesConfig: ResourcesConfig = resourcesConfig0,
            projectRoot: String = "root",
            hasLaunchActivity: Boolean = true,
            useKotlin: Boolean = false,
            dependencies: List<ModuleDependency> = listOf(dependency),
            productFlavorConfigs: List<FlavorConfig>? = null,
            buildTypeConfigs: List<BuildTypeConfig>? = null,
            javaPackageCount: Int = 1,
            javaClassCount: Int = 1,
            javaMethodsPerClass: Int = 1,
            kotlinPackageCount: Int = 1,
            kotlinClassCount: Int = 1,
            kotlinMethodsPerClass: Int = 1,
            extraLines: List<String>? = null,
            generateTests: Boolean = true
    ) = AndroidModuleBlueprint(name, numOfActivities, resourcesConfig, projectRoot, hasLaunchActivity, useKotlin,
            dependencies, productFlavorConfigs, buildTypeConfigs, javaPackageCount, javaClassCount, javaMethodsPerClass,
            kotlinPackageCount, kotlinClassCount, kotlinMethodsPerClass, extraLines, generateTests)
}