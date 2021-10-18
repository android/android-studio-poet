package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.input.RepositoryConfig
import com.google.androidstudiopoet.testutils.assertContains
import com.google.androidstudiopoet.testutils.assertEquals
import com.google.androidstudiopoet.testutils.assertNull
import com.google.androidstudiopoet.testutils.assertOn
import com.google.androidstudiopoet.utils.joinPath
import org.junit.Test

class ProjectBuildGradleBlueprintTest {

    @Test
    fun `path is root joined with build gradle`() {
        val root = "some/root"

        val blueprint = getBlueprint(root = root, kotlinVersion = "1.2.20")

        assertOn(blueprint) {
            path.assertEquals(root.joinPath("build.gradle"))
        }
    }

    @Test
    fun `kotlinExtStatement is set only if kotlin is enabled`() {
        val kotlinVersion = "1.2.60"

        val blueprint = getBlueprint(
                enableKotlin = true,
                kotlinVersion = kotlinVersion
        )

        val expectedKotlinExtStatement = "ext.kotlin_version = '$kotlinVersion'"

        assertOn(blueprint) {
            kotlinExtStatement!!.assertEquals(expectedKotlinExtStatement)
        }
    }

    @Test
    fun `kotlinExtStatement is set to null if kotlin is disabled`() {
        val kotlinVersion = "1.2.60"

        val blueprint = getBlueprint(
                enableKotlin = false,
                kotlinVersion = kotlinVersion
        )

        assertOn(blueprint) {
            kotlinExtStatement.assertNull()
        }
    }

    @Test
    fun `classpaths should contain only gradle plugin if kotlin is disabled`() {
        val agpVersion = "2.0.0"

        val blueprint = getBlueprint(agpVersion = agpVersion)

        assertOn(blueprint) {
            classpaths.assertEquals(setOf("com.android.tools.build:gradle:$agpVersion"))
        }
    }

    @Test
    fun `classpaths should contain kotlin gradle plugin if kotlin is enabled`() {
        val blueprint = getBlueprint(enableKotlin = true)

        assertOn(blueprint) {
            classpaths.assertContains("org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version")
        }
    }

    @Test
    fun `repositories should contain google and mavenCentral`() {
        val expectedRepositories = setOf(
                Repository.Named("google"),
                Repository.Named("mavenCentral")
        )

        val blueprint = getBlueprint()

        assertOn(blueprint) {
            repositories.assertEquals(expectedRepositories)
        }
    }

    @Test
    fun `repositories should contain additonal repos`() {
        val repoUrl = "http://smth"
        val additionalRepository = listOf(
                RepositoryConfig().apply { url = repoUrl }
        )

        val expectedRepository = Repository.Remote(repoUrl)

        val blueprint = getBlueprint(additionalRepositories = additionalRepository)

        assertOn(blueprint) {
            repositories.contains(expectedRepository)
        }
    }

    private fun getBlueprint(
            root: String = "root",
            enableKotlin: Boolean = false,
            agpVersion: String = "3.0.2",
            kotlinVersion: String = "1.2.20",
            additionalRepositories: List<RepositoryConfig> = listOf(),
            additionalClasspaths: List<String>? = null) =
            ProjectBuildGradleBlueprint(root, enableKotlin, agpVersion, kotlinVersion, additionalRepositories,
                    additionalClasspaths)
}