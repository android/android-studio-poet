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

package com.google.androidstudiopoet

import com.google.androidstudiopoet.models.FromToDependencyConfig
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DependencyValidatorTest {

    private val validator = DependencyValidator()

    @Test
    fun `isValid returns true when dependencies field are smaller then numModules`() {
        val moduleCount = 4
        val androidModuleCount = 0
        val dependencies = listOf(FromToDependencyConfig("module1", "module2", DEFAULT_DEPENDENCY_METHOD))
        assertTrue(validator.isValid(dependencies, moduleCount,androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#to is bigger then numModules`() {
        val moduleCount = 2
        val androidModuleCount = 0
        val dependencies = listOf(FromToDependencyConfig("module1", "module2", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#from is bigger then numModules`() {
        val moduleCount = 2
        val androidModuleCount = 0
        val dependencies = listOf(FromToDependencyConfig("module2", "module1", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns true when dependencies field are smaller then androidModules`() {
        val moduleCount = 0
        val androidModuleCount = 4
        val dependencies = listOf(FromToDependencyConfig("androidAppModule1", "androidAppModule2", DEFAULT_DEPENDENCY_METHOD))
        assertTrue(validator.isValid(dependencies, moduleCount,androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#to is bigger then androidModules`() {
        val moduleCount = 0
        val androidModuleCount = 2
        val dependencies = listOf(FromToDependencyConfig("androidAppModule1", "androidAppModule2", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#from is bigger then androidModules`() {
        val moduleCount = 2
        val androidModuleCount = 2
        val dependencies = listOf(FromToDependencyConfig("androidAppModule2", "androidAppModule1", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#from is module and to is Android`() {
        val moduleCount = 2
        val androidModuleCount = 2
        val dependencies = listOf(FromToDependencyConfig("module1", "androidAppModule1", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#to is App`() {
        val moduleCount = 2
        val androidModuleCount = 2
        val dependencies = listOf(FromToDependencyConfig("androidAppModule1", "androidAppModule0", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#from type is invalid`() {
        val moduleCount = 2
        val androidModuleCount = 2
        val dependencies = listOf(FromToDependencyConfig("invalid0", "androidAppModule0", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }

    @Test
    fun `isValid returns false when Dependency#to type is invalid`() {
        val moduleCount = 2
        val androidModuleCount = 2
        val dependencies = listOf(FromToDependencyConfig("module0", "invalid0", DEFAULT_DEPENDENCY_METHOD))
        assertFalse(validator.isValid(dependencies, moduleCount, androidModuleCount))
    }
}
