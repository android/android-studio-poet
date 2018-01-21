package com.google.androidstudiopoet.deserializers

import com.google.androidstudiopoet.input.AndroidModuleConfig
import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.input.PureModuleConfig
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

private const val MODULE_TYPE = "moduleType"
private const val ANDROID = "android"

class ModuleConfigDeserializer: JsonDeserializer<ModuleConfig> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ModuleConfig =
            when (json.asJsonObject?.get(MODULE_TYPE)?.asString) {
                ANDROID -> context.deserialize<AndroidModuleConfig>(json, AndroidModuleConfig::class.java)
                else -> context.deserialize<AndroidModuleConfig>(json, PureModuleConfig::class.java)
            }
}