package com.google.androidstudiopoet.generators.android_modules

import com.google.androidstudiopoet.input.AndroidBuildGradleBlueprint
import com.google.androidstudiopoet.testutils.mock
import com.google.androidstudiopoet.writers.FileWriter
import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class AndroidModuleBuildGradleGeneratorTest {
    private val fileWriter: FileWriter = mock()
    private val androidModuleBuildGradleGenerator = AndroidModuleBuildGradleGenerator(fileWriter)

    @Test
    fun `something`() {
        val blueprint = getAndroidBuildGradleBlueprint()
        androidModuleBuildGradleGenerator.generate(blueprint)
        val captor = argumentCaptor<String>()
        verify(fileWriter).writeToFile(captor.capture(), eq("path"))
        val expected = """android {
    compileSdkVersion 0

    defaultConfig {

        minSdkVersion 0
        targetSdkVersion 0
        versionCode 1
        versionName "1.0"
        multiDexEnabled true

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

    }
    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}"""
        verify(fileWriter).writeToFile(expected, "path")
    }

    private fun getAndroidBuildGradleBlueprint(

    ): AndroidBuildGradleBlueprint {
        val blueprint = mock<AndroidBuildGradleBlueprint>()
        whenever(blueprint.path).thenReturn("path")
        return blueprint
    }
}