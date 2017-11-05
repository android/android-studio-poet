package ui.generators.android_modules

import ui.models.AndroidModuleBlueprint
import ui.writers.FileWriter
import utils.joinPath

class AndroidModuleBuildGradleGenerator(val fileWriter: FileWriter) {
    fun generate(blueprint: AndroidModuleBlueprint) {
        val moduleRoot = blueprint.moduleRoot

        // TODO parameters for the package name ?
        val gradleText = """
            apply plugin: 'com.android.application'
            apply plugin: 'kotlin-android'
            apply plugin: 'kotlin-android-extensions'
            android {
                compileSdkVersion 26

                defaultConfig {
                    applicationId "app.test.myapplication"
                    minSdkVersion 19
                    targetSdkVersion 26
                    versionCode 1
                    versionName "1.0"

                    testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

                }

                buildTypes {
                    release {
                        minifyEnabled false
                        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
                    }
                }

            }

            dependencies {
                implementation fileTree(dir: 'libs', include: ['*.jar'])
                implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:1.8"
                implementation 'com.android.support:appcompat-v7:26.1.0'
                implementation 'com.android.support.constraint:constraint-layout:1.0.2'
                testImplementation 'junit:junit:4.12'
                androidTestImplementation 'com.android.support.test:runner:1.0.1'
                androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
            }
            """.trim()

        fileWriter.writeToFile(gradleText, moduleRoot.joinPath("build.gradle"))
    }
}
