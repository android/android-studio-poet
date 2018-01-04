package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.BuildTypeConfig
import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import com.google.androidstudiopoet.testutils.mock
import org.junit.Test

private const val ACTIVITY_COUNT = 4

private const val ALL_METHODS = 10

private const val JAVA_PACKAGE_COUNT = 5
private const val JAVA_CLASS_COUNT = 6
private const val JAVA_METHOD_COUNT = 7

private const val KOTLIN_PACKAGE_COUNT = 8
private const val KOTLIN_CLASS_COUNT = 9



class ConfigPojoToModuleConfigConverterTest {
    private val index = 3

    private val extraLinesForBuildFile: List<String> = mock()

    private val configPOJO = ConfigPOJO().apply {
        allMethods = "$ALL_METHODS"

        javaPackageCount = "$JAVA_PACKAGE_COUNT"
        javaClassCount = "$JAVA_CLASS_COUNT"
        javaMethodCount = "$JAVA_METHOD_COUNT"

        kotlinPackageCount = "$KOTLIN_PACKAGE_COUNT"
        kotlinClassCount = "$KOTLIN_CLASS_COUNT"

        numActivitiesPerAndroidModule = "$ACTIVITY_COUNT"

        extraBuildFileLines = extraLinesForBuildFile

    }

    private val converter = ConfigPojoToModuleConfigConverter()

    @Test
    fun `convert passes correct values to result ModuleConfig`() {
        val androidModuleConfig = converter.convert(configPOJO, index)
        assertOn(androidModuleConfig) {
            javaClassCount.assertEquals(JAVA_CLASS_COUNT)
            javaPackageCount.assertEquals(JAVA_PACKAGE_COUNT)
            javaMethodsPerClass.assertEquals(configPOJO.javaMethodsPerClass)

            kotlinPackageCount.assertEquals(KOTLIN_PACKAGE_COUNT)
            kotlinClassCount.assertEquals(KOTLIN_CLASS_COUNT)
            kotlinMethodsPerClass.assertEquals(configPOJO.kotlinMethodsPerClass)

            useKotlin.assertEquals(configPOJO.useKotlin)

            extraLines!!.assertEquals(extraLinesForBuildFile)
        }
    }

}