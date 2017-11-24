package com.google.androidstudiopoet.input

enum class ModuleType {
    ANDROID,
    PURE
}

open class ModuleConfig {

    lateinit var moduleType: ModuleType

    /**
     * How many modules should be created according to this configuration
     */
    var moduleCount: Int? = null

    /**
     * Module Name prefix used to create an actual modules names according to formula "$moduleNamePrefix$moduleNumber",
     * So if moduleCount = 2 and moduleNamePrefix = "module" then there would be generated two modules with names "module0" and "module1"
     * All the module names in the project must be unique
     */
    lateinit var moduleNamePrefix: String

    /**
     *  how many java packages should be generated
     */
    var javaPackageCount: Int = 1

    /**
     *  how many kotlin packages should be generated
     */
    var kotlinPackageCount: Int = 1

    /**
     *  how many classes should be generated in each Java package
     */
    var javaClassCount: Int = 1

    /**
     *  how many classes should be generated in each Kotlin package
     */
    var kotlinClassCount: Int = 1

    /**
     *  how many methods should be generated in each Java class
     */
    var javaMethodCount: Int = 1

    /**
     *  how many methods should be generated in each Kotlin class
     */
    var kotlinMethodCount: Int = 1

    var dependencies: List<DependencyConfig>? = listOf()
}

class AndroidModuleConfig: ModuleConfig() {
    /**
     * If true then android application plugin is applied.
     */
    val isApplication: Boolean? = false

    val numActivities: Int? = 0
    val resourcesConfig: ResourcesConfig? = null

}

class ResourcesConfig {
    var stringCount: Int? = null
    var imageCount: Int? = null
    var layoutCount: Int? = null
}
