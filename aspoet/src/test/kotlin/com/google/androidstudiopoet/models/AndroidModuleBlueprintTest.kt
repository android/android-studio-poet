package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.input.*
import com.google.androidstudiopoet.testutils.*
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

class AndroidModuleBlueprintTest {
    private val resourcesConfig0: ResourcesConfig = mock()

    @Test
    fun `blueprint create proper activity names`() {
        val blueprint = getAndroidModuleBlueprint(numOfActivities = 2)

        blueprint.activityNames.assertEquals(listOf("Activity0", "Activity1"))
    }

    @Test
    fun `blueprint creates activity blueprint with java class when java code exists`() {
        whenever(resourcesConfig0.layoutCount).thenReturn(1)

        val androidModuleBlueprint = getAndroidModuleBlueprint()

        assertThat(androidModuleBlueprint.activityBlueprints).isEqualTo(listOf(ActivityBlueprint(
                "Activity0",
                false,
                false,
                androidModuleBlueprint.resourcesBlueprint!!.layoutBlueprints[0],
                androidModuleBlueprint.packagePath,
                androidModuleBlueprint.packageName,
                androidModuleBlueprint.packagesBlueprint.javaPackageBlueprints[0].classBlueprints[0],
                listOf(), false)))
    }

    @Test
    fun `blueprint creates activity blueprint with koltin class when java code doesn't exist`() {
        whenever(resourcesConfig0.layoutCount).thenReturn(1)

        val androidModuleBlueprint = getAndroidModuleBlueprint(
                javaConfig = CodeConfig().apply {
                    packages = 0
                    classesPerPackage = 0
                    methodsPerClass = 0
                }
        )

        assertOn(androidModuleBlueprint) {
            activityBlueprints[0].classToReferFromActivity.assertEquals(
                    androidModuleBlueprint.packagesBlueprint.kotlinPackageBlueprints[0].classBlueprints[0])
        }
    }

    @Test
    fun `enableDataBinding is false when data binding config is null`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = null)

        androidModuleBlueprint.enableDataBinding.assertFalse()
    }

    @Test
    fun `enableDataBinding is false when data binding config has zero listener count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = 0))

        androidModuleBlueprint.enableDataBinding.assertFalse()
    }

    @Test
    fun `enableDataBinding is true when data binding config has positive listener count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = 2))

        androidModuleBlueprint.enableDataBinding.assertTrue()
    }

    @Test
    fun `enableDataBinding is false when data binding config has negative listener count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = -1))

        androidModuleBlueprint.enableDataBinding.assertFalse()
    }

    @Test
    fun `enableKapt is true when data binding config specifies to be true`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = 1, kapt = true))

        androidModuleBlueprint.enableDataBinding.assertTrue()
        androidModuleBlueprint.enableKapt.assertTrue()
    }

    @Test
    fun `enableKapt is false when data binding config does not specify`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = DataBindingConfig(listenerCount = 1))

        androidModuleBlueprint.enableDataBinding.assertTrue()
        androidModuleBlueprint.enableKapt.assertFalse()
    }

    @Test
    fun `enableKapt is false when data binding config is null`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(dataBindingConfig = null)

        androidModuleBlueprint.enableDataBinding.assertFalse()
        androidModuleBlueprint.enableKapt.assertFalse()
    }

    @Test
    fun `enableCompose is false when compose config is null`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(composeConfig = null)

        androidModuleBlueprint.enableCompose.assertFalse()
    }

    @Test
    fun `enableCompose is false when compose config has zero action count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(composeConfig = ComposeConfig(actionCount = 0))

        androidModuleBlueprint.enableCompose.assertFalse()
    }

    @Test
    fun `enableCompose is true when compose config has positive action count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(composeConfig = ComposeConfig(actionCount = 2))

        androidModuleBlueprint.enableCompose.assertTrue()
    }

    @Test
    fun `enableCompose is false when compose config has negative action count`() {
        val androidModuleBlueprint = getAndroidModuleBlueprint(composeConfig = ComposeConfig(actionCount = -1))

        androidModuleBlueprint.enableCompose.assertFalse()
    }

    @Test
    fun `empty resource config generates null resources blueprint`() {
      val androidModuleBlueprint = getAndroidModuleBlueprint(resourcesConfig = ResourcesConfig(0, 0, 0))

      androidModuleBlueprint.resourcesBlueprint.assertNull()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cannot have both compose config and data binding config`() {
        getAndroidModuleBlueprint(
                composeConfig = ComposeConfig(),
                dataBindingConfig = DataBindingConfig()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cannot have both compose config and view binding`() {
        getAndroidModuleBlueprint(
                composeConfig = ComposeConfig(),
                viewBinding = true
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cannot have both data binding and view binding`() {
        getAndroidModuleBlueprint(
                dataBindingConfig = DataBindingConfig(),
                viewBinding = true
        )
    }

    private fun getAndroidModuleBlueprint(
            name: String = "androidAppModule1",
            numOfActivities: Int = 1,
            resourcesConfig: ResourcesConfig = resourcesConfig0,
            projectRoot: String = "root",
            hasLaunchActivity: Boolean = true,
            useKotlin: Boolean = false,
            dependencies: Set<Dependency> = setOf(),
            productFlavorConfigs: List<FlavorConfig>? = null,
            buildTypeConfigs: List<BuildTypeConfig>? = null,
            javaConfig: CodeConfig? = defaultCodeConfig(),
            kotlinConfig: CodeConfig? = defaultCodeConfig(),
            extraLines: List<String>? = null,
            generateTests: Boolean = true,
            dataBindingConfig: DataBindingConfig? = null,
            composeConfig: ComposeConfig? = null,
            viewBinding: Boolean = false,
            androidBuildConfig: AndroidBuildConfig = AndroidBuildConfig(),
            pluginConfigs: List<PluginConfig>? = null,
            generateBazelFiles: Boolean? = false
    ) = AndroidModuleBlueprint(name, numOfActivities, resourcesConfig, projectRoot, hasLaunchActivity, useKotlin,
            dependencies, productFlavorConfigs, buildTypeConfigs, javaConfig, kotlinConfig,
            extraLines, generateTests, dataBindingConfig, composeConfig, viewBinding, androidBuildConfig, pluginConfigs, generateBazelFiles)

    private fun defaultCodeConfig() = CodeConfig().apply {
        packages = 1
        classesPerPackage = 1
        methodsPerClass = 1
    }
}