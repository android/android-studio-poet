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
     * Default minSdkVersion value for all android modules
     */
    var minSdkVersion: Int? = null

    /**
     * Default compileSdkVersion value for all android modules
     */
    var compileSdkVersion: Int? = null

    /**
     * Default targetSdkVersion value for all android modules
     */
    var targetSdkVersion: Int? = null
}
