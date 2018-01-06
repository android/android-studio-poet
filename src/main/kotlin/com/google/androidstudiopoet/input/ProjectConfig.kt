package com.google.androidstudiopoet.input

data class ProjectConfig(val projectName: String, val root: String, val pureModuleConfigs: List<ModuleConfig>)