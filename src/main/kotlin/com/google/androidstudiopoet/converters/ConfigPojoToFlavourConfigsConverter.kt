package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.FlavorConfig
import com.google.androidstudiopoet.input.ConfigPOJO

class ConfigPojoToFlavourConfigsConverter {
    fun convert(configPOJO: ConfigPOJO): List<FlavorConfig> {
        val productFlavors = configPOJO.productFlavors
        if (productFlavors == null || productFlavors.isEmpty()) {
            return listOf()
        }

        return productFlavors.withIndex().flatMap { (dimension, size) ->
            (0 until size)
                    .map { flavourIndex -> "dim${dimension}flav$flavourIndex" }
                    .map { FlavorConfig(it, "dim$dimension") }
        }
    }
}