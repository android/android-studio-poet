package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.BuildSystemConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToBuildSystemConfigConverter {
    fun convert(configPojo: ConfigPOJO): BuildSystemConfig {
        return BuildSystemConfig(configPojo.gradleVersion, configPojo.androidGradlePluginVersion, configPojo.kotlinVersion)
    }
}