package com.google.androidstudiopoet.input

/**
 * Configuration of the build system, right now only Gradle is supported
 */
class BuildSystemConfig {
    /**
     * Build system version
     */
    var version: String? = null

    /**
     * AGP version
     */
    var agpVersion: String? = null

    /**
     * Kotlin version
     */
    var kotlinVersion: String? = null


    /**
     * Default AndroidBuildConfig for all android modules
     */
    var defaultAndroidBuildConfig: AndroidBuildConfig? = null

}
