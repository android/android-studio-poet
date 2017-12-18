package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ResourcesConfig
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.mock
import org.junit.Test

class AndroidModuleBlueprintTest {
    private val resourcesConfig: ResourcesConfig = mock()
    private val dependency: ModuleDependency = mock()
    private val originalAndroidModuleBlueprint = AndroidModuleBlueprint(1, 1, resourcesConfig,
            "root", true, false, listOf(dependency), null,
            1, 1, 1, 1, 1, 1)

    @Test
    fun `blueprint create proper activity names`() {
        val blueprint = originalAndroidModuleBlueprint.copy(numOfActivities = 2)

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
        val blueprint = originalAndroidModuleBlueprint.copy(productFlavorConfigs = flavorConfigs)

        blueprint.flavorDimensions!!.assertEquals(setOf(dimension1, dimension2))
        blueprint.productFlavors!!.assertEquals(setOf(
                Flavor(flavorName1, dimension1),
                Flavor(flavorName2, dimension1),
                Flavor(flavorName3, dimension2)
        ))
    }
}