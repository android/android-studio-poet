package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertOn
import org.junit.Test

class ProjectBuildGradleBlueprintTest {


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

    private fun getBlueprint() = ProjectBuildGradleBlueprint("path")
}