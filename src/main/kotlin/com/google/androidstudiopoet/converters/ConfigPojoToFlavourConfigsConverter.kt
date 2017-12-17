package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.FlavourConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToFlavourConfigsConverter {
    fun convert(configPOJO: ConfigPOJO): List<FlavourConfig> {
        val productFlavors = configPOJO.productFlavors
        if (productFlavors == null || productFlavors.isEmpty()) {
            return listOf()
        }

        return productFlavors.withIndex().flatMap { (dimension, size) ->
            (0 until size)
                    .map { flavourIndex -> "dim${dimension}flav$flavourIndex" }
                    .map { FlavourConfig(it, "dim$dimension") }
        }
    }
}