package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import org.junit.Test

private const val KOTLIN_VERSION = "kotlin version"
private const val AGP_VERSION = "agp version"
private const val GRADLE_VERSION = "gradle version"

class ConfigPojoToBuildSystemConfigConverterTest {
    private val configPojo = ConfigPOJO().apply {
        kotlinVersion = KOTLIN_VERSION
        androidGradlePluginVersion = AGP_VERSION
        gradleVersion = GRADLE_VERSION
    }

    private val converter = ConfigPojoToBuildSystemConfigConverter()

    @Test
    fun `convert should pass gradle, AGP and kotlin versions to BuildSystemConfig`() {
        val buildSystemConfig = converter.convert(configPojo)
        assertOn(buildSystemConfig) {
            kotlinVersion!!.assertEquals(KOTLIN_VERSION)
            agpVersion!!.assertEquals(AGP_VERSION)
            version!!.assertEquals(GRADLE_VERSION)
        }
    }

}