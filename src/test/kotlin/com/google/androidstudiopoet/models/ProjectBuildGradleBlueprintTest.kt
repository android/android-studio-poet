package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.testutils.assertContains
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import org.junit.Test

class ProjectBuildGradleBlueprintTest {

    @Test
    fun `classpaths should contain only gradle plugin if kotlin is disabled`() {
        val agpVersion = "2.0.0"

        val blueprint = getBlueprint(agpVersion = agpVersion)

        assertOn(blueprint) {
            blueprint.classpaths.assertEquals(setOf("com.android.tools.build:gradle:$agpVersion"))
        }
    }

    @Test
    fun `classpaths should contain kotlin gradle plugin if kotlin is enabled`() {
        val blueprint = getBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            blueprint.classpaths.assertContains("org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version")
        }
    }

    @Test
    fun `repositories should contain google and jcenter`() {
        val expectedRepositories = setOf(
                Repository.Named("google"),
                Repository.Named("jcenter")
        )

        val blueprint = getBlueprint()

        assertOn(blueprint) {
            blueprint.repositories.assertEquals(expectedRepositories)
        }
    }

    private fun getBlueprint(
            enableKotlin: Boolean = false,
            agpVersion: String = "3.0.2") = ProjectBuildGradleBlueprint("path", enableKotlin, agpVersion, "1.2.20")
}