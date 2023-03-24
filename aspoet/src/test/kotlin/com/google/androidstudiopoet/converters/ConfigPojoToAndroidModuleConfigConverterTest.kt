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
import com.google.androidstudiopoet.models.FromToDependencyConfig
import com.google.androidstudiopoet.testutils.*
import org.junit.Test

private const val ACTIVITY_COUNT = 4

private const val ALL_METHODS = 10

private const val JAVA_PACKAGE_COUNT = 5
private const val JAVA_CLASS_COUNT = 6
private const val JAVA_METHOD_COUNT = 7

private const val KOTLIN_PACKAGE_COUNT = 8
private const val KOTLIN_CLASS_COUNT = 9

private const val GENERATE_TESTS = true

private const val ANDROID_MODULE_COUNT = 2
private const val INDEX_1 = 1
private const val ANDROID_MODULE_NAME_0 = "androidAppModule0"
private const val ANDROID_MODULE_NAME_1 = "androidAppModule1"
private const val MODULE_NAME_0 = "module0"
private const val MODULE_NAME_1 = "module1"
private const val DEPENDENCY_METHOD = "api"
private const val LIBRARY_NAME_0 = "library0"
private const val LIBRARY_NAME_1 = "library1"

private val PURE_MODULE_LIST = listOf(MODULE_NAME_0, MODULE_NAME_1)
private val PURE_MODULE_DEPENDENCY_LIST = PURE_MODULE_LIST.map { DependencyConfig.ModuleDependencyConfig(it, DEPENDENCY_METHOD) }
private val LIBRARY_LIST = listOf(DependencyConfig.LibraryDependencyConfig(LIBRARY_NAME_0, DEPENDENCY_METHOD),
        DependencyConfig.LibraryDependencyConfig(LIBRARY_NAME_1, DEPENDENCY_METHOD))

class ConfigPojoToAndroidModuleConfigConverterTest {

    private val productFlavorConfigs: List<FlavorConfig> = mock()
    private val buildTypes: List<BuildTypeConfig> = mock()

    private val extraLinesForAndroidBuildFile: List<String> = mock()

    private val configPOJO = ConfigPOJO().apply {
        allMethods = "$ALL_METHODS"

        javaPackageCount = "$JAVA_PACKAGE_COUNT"
        javaClassCount = "$JAVA_CLASS_COUNT"
        javaMethodCount = "$JAVA_METHOD_COUNT"

        kotlinPackageCount = "$KOTLIN_PACKAGE_COUNT"
        kotlinClassCount = "$KOTLIN_CLASS_COUNT"

        numActivitiesPerAndroidModule = "$ACTIVITY_COUNT"

        extraAndroidBuildFileLines = extraLinesForAndroidBuildFile

        generateTests = GENERATE_TESTS
        androidModules = ANDROID_MODULE_COUNT
        dependencies = listOf(FromToDependencyConfig(ANDROID_MODULE_NAME_0, MODULE_NAME_0, DEPENDENCY_METHOD),
                FromToDependencyConfig(ANDROID_MODULE_NAME_0, MODULE_NAME_1, DEPENDENCY_METHOD),
                FromToDependencyConfig(ANDROID_MODULE_NAME_1, MODULE_NAME_0, DEPENDENCY_METHOD),
                FromToDependencyConfig(ANDROID_MODULE_NAME_1, MODULE_NAME_1, DEPENDENCY_METHOD),
                FromToDependencyConfig(ANDROID_MODULE_NAME_0, ANDROID_MODULE_NAME_1, DEPENDENCY_METHOD))
        libraries = LIBRARY_LIST
    }

    private val converter = ConfigPojoToAndroidModuleConfigConverter()

    @Test
    fun `convert passes correct values to result AndroidModuleConfig`() {
        val androidModuleConfig = converter.convert(configPOJO, INDEX_1, productFlavorConfigs, buildTypes)
        assertOn(androidModuleConfig) {
            moduleName.assertEquals(ANDROID_MODULE_NAME_1)
            activityCount.assertEquals(ACTIVITY_COUNT)
            extraLines!!.assertEquals(extraLinesForAndroidBuildFile)

            assertOn(java!!) {
                packages.assertEquals(JAVA_PACKAGE_COUNT)
                classesPerPackage.assertEquals(JAVA_CLASS_COUNT)
                methodsPerClass.assertEquals(configPOJO.javaMethodsPerClass)
            }

            assertOn(kotlin!!) {
                packages.assertEquals(KOTLIN_PACKAGE_COUNT)
                classesPerPackage.assertEquals(KOTLIN_CLASS_COUNT)
                methodsPerClass.assertEquals(configPOJO.kotlinMethodsPerClass)
            }

            useKotlin.assertEquals(configPOJO.useKotlin)

            generateTests.assertEquals(GENERATE_TESTS)
            hasLaunchActivity.assertFalse()
            dependencies!!.sortedBy { it.toString() }.assertEquals((PURE_MODULE_DEPENDENCY_LIST + LIBRARY_LIST).sortedBy { it.toString() })
        }
    }

    @Test
    fun `convert result AndroidModuleConfig that has launch activity when index == 0`() {
        val androidModuleConfig = converter.convert(configPOJO, 0, productFlavorConfigs, buildTypes)
        assertOn(androidModuleConfig) {
            hasLaunchActivity.assertTrue()
            dependencies!!.sortedBy { it.toString() }.assertEquals((listOf(DependencyConfig.ModuleDependencyConfig(ANDROID_MODULE_NAME_1, DEPENDENCY_METHOD)) + PURE_MODULE_DEPENDENCY_LIST + LIBRARY_LIST).sortedBy { it.toString() })
            resourcesConfig!!.assertEquals(ResourcesConfig(ACTIVITY_COUNT + 2, ACTIVITY_COUNT + 5, ACTIVITY_COUNT))
        }
    }

}