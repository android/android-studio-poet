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

import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ConfigPOJO
import com.google.androidstudiopoet.testutils.assertEmpty
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class ConfigPojoToFlavorConfigsConverterTest {
    private val configPojo: ConfigPOJO = mock()

    private val converter = ConfigPojoToFlavourConfigsConverter()

    @Test
    fun `convert returns empty list when configPojo#productFlavors is null`() {
        converter.convert(configPojo).assertEmpty()
    }

    @Test
    fun `convert returns empty list when configPojo#productFlavors is empty`() {
        whenever(configPojo.productFlavors).thenReturn(listOf())
        converter.convert(configPojo).assertEmpty()
    }

    @Test
    fun `convert returns two flavours when configPojo#productFlavors has one element equal to two`() {
        whenever(configPojo.productFlavors).thenReturn(listOf(2))
        converter.convert(configPojo).assertEquals(listOf(
                FlavorConfig("dim0flav0", "dim0"),
                FlavorConfig("dim0flav1", "dim0")
        ))
    }
}