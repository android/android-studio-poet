package com.google.androidstudiopoet.input

open class DependencyConfig {

    var type: DependencyType? = null

    /**
     * Identifies ModuleConfig that this one should depend on
     */
    lateinit var moduleNamePrefix: String

}

enum class DependencyType {
    SINGLE,
    BULK
}

class BulkDependencyConfig: DependencyConfig() {
    /**
     * Amount of modules from target bulk to depend on.
     * If ModuleConfig with ModuleConfig#moduleCount >= 1 contains this BulkDependencyConfig then algorithm will try to
     * distribute dependencies evenly, and do its' best to be sure that there are dependencies on the all modules from
     * the target bulk
     */
    var moduleCount: Int = 1
}