package com.google.androidstudiopoet.deserializers

import com.google.androidstudiopoet.input.DependencyConfig
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val MODULE_NAME = "moduleName"

class DependencyConfigDeserializer: JsonDeserializer<DependencyConfig> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DependencyConfig =
            when (json.asJsonObject?.get(MODULE_NAME)?.asString) {
                null -> context.deserialize<DependencyConfig.LibraryDependencyConfig>(json,
                        DependencyConfig.LibraryDependencyConfig::class.java)
                else -> context.deserialize<DependencyConfig.ModuleDependencyConfig>(json,
                        DependencyConfig.ModuleDependencyConfig::class.java)
            }
}