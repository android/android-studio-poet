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

import com.google.androidstudiopoet.models.DependencyConfig
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DependencyValidatorTest {

    private val validator = DependencyValidator()

    @Test
    fun `isValid returns true when dependencies field are smaller then numModules`() {
        val moduleCount = 4
        val dependencies = listOf(DependencyConfig(1, 2))
        assertTrue(validator.isValid(dependencies, moduleCount))
    }

    @Test
    fun `isValid returns false when Dependency#to is bigger then numModules`() {
        val moduleCount = 1
        val dependencies = listOf(DependencyConfig(0, 2))
        assertFalse(validator.isValid(dependencies, moduleCount))
    }

    @Test
    fun `isValid returns false when Dependency#from is bigger then numModules`() {
        val moduleCount = 1
        val dependencies = listOf(DependencyConfig(2, 0))
        assertFalse(validator.isValid(dependencies, moduleCount))
    }
}