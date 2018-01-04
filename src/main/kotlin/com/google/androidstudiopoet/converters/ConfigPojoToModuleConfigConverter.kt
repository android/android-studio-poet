package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ModuleConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToModuleConfigConverter {
    fun convert(config: ConfigPOJO, index: Int): ModuleConfig {
        return ModuleConfig(index, config)
    }
}