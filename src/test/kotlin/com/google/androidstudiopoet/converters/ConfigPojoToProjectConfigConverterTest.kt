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

package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.*
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import com.google.androidstudiopoet.testutils.mock
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test

private const val PROJECT_NAME = "project name"
private const val ROOT = "root"
private const val NUMBER_OF_PURE_MODULES = 2
private const val NUMBER_OF_ANDROID_MODULES = 2

class ConfigPojoToProjectConfigConverterTest {
    private val configPojoToModuleConfigConverter: ConfigPojoToModuleConfigConverter = mock()
    private val configPojoToFlavourConfigsConverter: ConfigPojoToFlavourConfigsConverter = mock()
    private val configPojoToBuildTypeConfigsConverter: ConfigPojoToBuildTypeConfigsConverter = mock()
    private val configPojoToAndroidModuleConfigConverter: ConfigPojoToAndroidModuleConfigConverter = mock()
    private val configPojoToBuildSystemConfigConverter: ConfigPojoToBuildSystemConfigConverter = mock()

    private val moduleConfig0: ModuleConfig = mock()
    private val moduleConfig1: ModuleConfig = mock()

    private val androidModuleConfig0: AndroidModuleConfig = mock()
    private val androidModuleConfig1: AndroidModuleConfig = mock()

    private val flavours: List<FlavorConfig> = mock()
    private val buildTypes: List<BuildTypeConfig> = mock()

    private val buildSystemConfig0: BuildSystemConfig = mock()

    private val configPojo = ConfigPOJO().apply {
        projectName = PROJECT_NAME
        root = ROOT
        numModules = NUMBER_OF_PURE_MODULES
        androidModules = NUMBER_OF_ANDROID_MODULES
    }

    private val configPojoToProjectConfigConverter = ConfigPojoToProjectConfigConverter(configPojoToModuleConfigConverter,
            configPojoToFlavourConfigsConverter, configPojoToBuildTypeConfigsConverter, configPojoToAndroidModuleConfigConverter,
            configPojoToBuildSystemConfigConverter)

    @Before
    fun setUp() {
        whenever(configPojoToModuleConfigConverter.convert(configPojo, 0)).thenReturn(moduleConfig0)
        whenever(configPojoToModuleConfigConverter.convert(configPojo, 1)).thenReturn(moduleConfig1)
        whenever(configPojoToAndroidModuleConfigConverter.convert(configPojo, 0, flavours, buildTypes, any())).thenReturn(androidModuleConfig0)
        whenever(configPojoToAndroidModuleConfigConverter.convert(configPojo, 1, flavours, buildTypes, any())).thenReturn(androidModuleConfig1)
        whenever(configPojoToFlavourConfigsConverter.convert(configPojo)).thenReturn(flavours)
        whenever(configPojoToBuildTypeConfigsConverter.convert(configPojo)).thenReturn(buildTypes)
        whenever(configPojoToBuildSystemConfigConverter.convert(configPojo)).thenReturn(buildSystemConfig0)
    }

    @Test
    fun `convert should pass projectName, root and create module and build system configs for ProjectConfig`() {
        val projectConfig = configPojoToProjectConfigConverter.convert(configPojo)
        assertOn(projectConfig) {
            projectName.assertEquals(PROJECT_NAME)
            root.assertEquals(ROOT)
            pureModuleConfigs.assertEquals(listOf(moduleConfig0, moduleConfig1))
            androidModuleConfigs.assertEquals(listOf(androidModuleConfig0, androidModuleConfig1))
            buildSystemConfig.assertEquals(buildSystemConfig0)
        }
    }
}