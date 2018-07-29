package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.utils.joinPath

import org.junit.Test

class GradlePropertiesBlueprintTest {

    @Test
    fun `properties contains default settings when no overrideProperties is passed`() {
        GradlePropertiesBlueprint("root", null).properties!!
                .assertEquals(mapOf(
                        "org.gradle.jvmargs" to "-Xmx4096m -XX:+HeapDumpOnOutOfMemoryError",
                        "org.gradle.daemon" to "true",
                        "org.gradle.parallel" to "true",
                        "org.gradle.caching" to "true"
                ))

    }

    @Test
    fun `properties contains default values when not overriden`() {
        GradlePropertiesBlueprint("root", mapOf(
                "org.gradle.daemon" to "false",
                "randomKey" to "randomValue"
        )).properties!!
                .assertEquals(mapOf(
                        "org.gradle.jvmargs" to "-Xmx4096m -XX:+HeapDumpOnOutOfMemoryError",
                        "org.gradle.daemon" to "false",
                        "org.gradle.parallel" to "true",
                        "org.gradle.caching" to "true",
                        "randomKey" to "randomValue"
                ))
    }

    @Test
    fun `path is root with gradle properties`() {
        GradlePropertiesBlueprint("root", null).path
                .assertEquals("root".joinPath("gradle.properties"))
    }
}