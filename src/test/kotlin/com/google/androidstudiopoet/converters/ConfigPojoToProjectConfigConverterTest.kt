package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import com.google.androidstudiopoet.testutils.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test

private const val PROJECT_NAME = "project name"
private const val ROOT = "root"
private const val NUMBER_OF_PURE_MODULES = 2

class ConfigPojoToProjectConfigConverterTest {
    private val configPojoToModuleConfigConverter: ConfigPojoToModuleConfigConverter = mock()
    private val moduleConfig0: ModuleConfig = mock()
    private val moduleConfig1: ModuleConfig = mock()

    private val configPojo = ConfigPOJO().apply {
        projectName = PROJECT_NAME
        root = ROOT
        numModules = NUMBER_OF_PURE_MODULES
    }

    private val configPojoToProjectConfigConverter = ConfigPojoToProjectConfigConverter(configPojoToModuleConfigConverter)

    @Before
    fun setUp() {
        whenever(configPojoToModuleConfigConverter.convert(configPojo, 0)).thenReturn(moduleConfig0)
        whenever(configPojoToModuleConfigConverter.convert(configPojo, 1)).thenReturn(moduleConfig1)
    }

    @Test
    fun `convert`() {
        val projectConfig = configPojoToProjectConfigConverter.convert(configPojo)
        assertOn(projectConfig) {
            projectName.assertEquals(PROJECT_NAME)
            root.assertEquals(ROOT)
            pureModuleConfigs.assertEquals(listOf(moduleConfig0, moduleConfig1))
        }
    }
}