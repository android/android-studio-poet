package com.google.androidstudiopoet.converters

import com.google.androidstudiopoet.input.ProjectConfig
import com.google.androidstudiopoet.models.ConfigPOJO

class ConfigPojoToProjectConfigConverter {
    fun convert(configPojo: ConfigPOJO): ProjectConfig {
        return ProjectConfig()
    }
}
