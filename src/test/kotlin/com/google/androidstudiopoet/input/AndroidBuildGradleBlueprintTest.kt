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

package com.google.androidstudiopoet.input

import com.google.androidstudiopoet.testutils.assertContains
import com.google.androidstudiopoet.testutils.assertNotContains
import com.google.androidstudiopoet.testutils.assertOn
import org.junit.Test

class AndroidBuildGradleBlueprintTest {

    @Test
    fun `plugins contain android library when is not an application module`() {
        val blueprint = createAndroidBuildGradleBlueprint(isApplication = false)

        assertOn(blueprint) {
            plugins.assertContains("com.android.library")
        }
    }

    @Test
    fun `plugins contain android application when is an application module`() {
        val blueprint = createAndroidBuildGradleBlueprint(isApplication = true)

        assertOn(blueprint) {
            plugins.assertContains("com.android.application")
        }

    }

    @Test
    fun `plugins contain kotlin-android and kotlin-android-extensions when Kotlin is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin-android")
            plugins.assertContains("kotlin-android-extensions")
        }
    }

    @Test
    fun `plugins do not contain kotlin-kapt when Kotlin is disabled and data binding is enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = false, enableDataBinding = true)

        assertOn(blueprint) {
            plugins.assertNotContains("kotlin-kapt")
        }
    }

    @Test
    fun `plugins do not contain kotlin-kapt when Kotlin is enabled and data binding is disabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = false)

        assertOn(blueprint) {
            plugins.assertNotContains("kotlin-kapt")
        }
    }

    @Test
    fun `plugins contain kotlin-kapt when Kotlin and data binding are enabled`() {
        val blueprint = createAndroidBuildGradleBlueprint(enableKotlin = true, enableDataBinding = true)

        assertOn(blueprint) {
            plugins.assertContains("kotlin-kapt")
        }
    }

    private fun createAndroidBuildGradleBlueprint(isApplication: Boolean = false,
                                                  enableKotlin: Boolean = false,
                                                  enableDataBinding: Boolean = false
    ) = AndroidBuildGradleBlueprint(isApplication, enableKotlin, enableDataBinding)
}