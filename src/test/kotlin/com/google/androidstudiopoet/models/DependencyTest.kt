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

package com.google.androidstudiopoet.models

import com.google.gson.Gson

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class DependencyTest {
    @Test
    fun `Dependency deserialized correctly`() {
        @Language("JSON") val dependencyString = "{\n  \"from\": \"module2\",\n  \"to\": \"module1\",\n  \"method\": \"api\"\n}"
        val dependency = Gson().fromJson(dependencyString, FromToDependencyConfig::class.java)
        assertEquals("module2", dependency.from)
        assertEquals("module1", dependency.to)
        assertEquals("api", dependency.method)
    }
}
