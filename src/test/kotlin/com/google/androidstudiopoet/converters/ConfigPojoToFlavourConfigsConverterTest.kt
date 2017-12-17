package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.FlavourConfig
import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.test_utils.assertEmpty
import com.google.androidstudiopoet.test_utils.assertEquals
import com.google.androidstudiopoet.test_utils.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class ConfigPojoToFlavourConfigsConverterTest {
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
                FlavourConfig("dim0flav0", "dim0"),
                FlavourConfig("dim0flav1", "dim0")
        ))
    }
}