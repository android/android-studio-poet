package com.google.androidstudiopoet.input

/**
 * Configuration of the whole project that should be generated
 */
class ProjectConfig {


    /**
     * Name of the new project
     */
    lateinit var projectName: String

    /**
     * Directory where generator should put generated project
     */
    lateinit var root: String

    /**
     * Name of the buildSystemConfig
     */
    var buildSystemConfig: BuildSystemConfig? = null

    /**
     * Module Configurations
     */
    lateinit var moduleConfigs: List<ModuleConfig>
}
