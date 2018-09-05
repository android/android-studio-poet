package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ConfigPOJO
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import org.junit.Test

private const val KOTLIN_VERSION = "kotlin version"
private const val AGP_VERSION = "agp version"
private const val GRADLE_VERSION = "gradle version"
private val GRADLE_PROPERTIES = mapOf("property1" to "value1", "property2" to "value2")
private const val GENERATE_BAZEL_FILES = true

class ConfigPojoToBuildSystemConfigConverterTest {
    private val configPojo = ConfigPOJO().apply {
        kotlinVersion = KOTLIN_VERSION
        androidGradlePluginVersion = AGP_VERSION
        gradleVersion = GRADLE_VERSION
        gradleProperties = GRADLE_PROPERTIES
        generateBazelFiles = GENERATE_BAZEL_FILES
    }

    private val converter = ConfigPojoToBuildSystemConfigConverter()

    @Test
    fun `convert should pass gradle, AGP and kotlin versions to BuildSystemConfig`() {
        val buildSystemConfig = converter.convert(configPojo)
        assertOn(buildSystemConfig) {
            kotlinVersion!!.assertEquals(KOTLIN_VERSION)
            agpVersion!!.assertEquals(AGP_VERSION)
            buildSystemVersion!!.assertEquals(GRADLE_VERSION)
            properties!!.assertEquals(GRADLE_PROPERTIES)
            generateBazelFiles!!.assertEquals(GENERATE_BAZEL_FILES)
        }
    }

}