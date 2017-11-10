package main.generators.android_modules

import main.models.AndroidModuleBlueprint
import main.utils.fold
import main.writers.FileWriter
import main.utils.joinPath

class AndroidModuleBuildGradleGenerator(val fileWriter: FileWriter) {
    fun generate(blueprint: AndroidModuleBlueprint) {
        val moduleRoot = blueprint.moduleRoot

        val androidPlugin = if (blueprint.hasLaunchActivity) "application" else "library"
        val applicationId = if (blueprint.hasLaunchActivity) "applicationId \"${blueprint.packageName}\"" else ""

        val moduleDependencies = blueprint.dependencies.map { "implementation project(':$it')\n" }.fold()

        val gradleText = """
            apply plugin: 'com.android.$androidPlugin'
            apply plugin: 'kotlin-android'
            apply plugin: 'kotlin-android-extensions'
            android {
                compileSdkVersion 26

                defaultConfig {
                    $applicationId
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
                compileOptions {
                    targetCompatibility 1.8
                    sourceCompatibility 1.8
                }

            }

            dependencies {
                implementation fileTree(dir: 'libs', include: ['*.jar'])
                implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:${'$'}kotlin_version"
                implementation 'com.android.support:appcompat-v7:26.1.0'
                implementation 'com.android.support.constraint:constraint-layout:1.0.2'
                testImplementation 'junit:junit:4.12'
                androidTestImplementation 'com.android.support.test:runner:1.0.1'
                androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
                implementation "com.android.support:multidex:1.0.1"
                $moduleDependencies
            }
            """.trim()

        fileWriter.writeToFile(gradleText, moduleRoot.joinPath("build.gradle"))
    }
}
