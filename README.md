# Android Studio Poet

![alt text](https://github.com/borisf/java-generator/blob/master/img/generator.png)  

Optimise your Android Studio builds, by creating generated projects. Inspired by [GradleBuildExperiment](https://github.com/NikitaKozlov/GradleBuildExperiment). A tool to generate real life Java and Kotlin Android projects to analyze and improve build times.

Create a real life complexity Android project that mimics your own and observe the build times. If the build times of the generated project are way smaller than yours, it might be worth inspecting deeply your build configs and optimize.

## Features

* Configurable number of modules
* Configurable number of packages
* Configurable number of classes
* Configurable number of inter module dependencies
* Android resources (images, strings, activities, layouts)
* Configure how to generate UI layer code
* Configurable version of gradle, kotlin, the android gradle plugin
* Experimental Bazel support

## Download
To run, grab the [latest JAR](https://github.com/android/android-studio-poet/releases)
and run `java -jar as-poet.jar`.

## Command line arguments
Android Studio Poet supports the following command line arguments
1. Config - just run  `java -jar as-poet.jar MyConfig.json`
2. Folder with configs - just run  `java -jar as-poet.jar <path to a folder with configs>` Android Studio Poet 
will crawl the folder recursively and execute each config in turn.

## Build & Run
1. Clone the repo
2. `./gradlew aspoet:fatJar`
3. `java -jar aspoet/build/libs/aspoet-all.jar`

## How
* The generated sources will include the basic functionality of method calling both in class itself and between the generated classes, both in Java and Kotlin

* The generated build.gradle files will include other modules as dependencies

* You can control Android resources (images, layouts etc')

* You can control product flavors

* You can have both source and resources inter-module dependencies

## UI layer configurations
* Specify `dataBindingConfig` for data binding
  * It has a property `listenerCount` to indicate the number of data variables.
  * It has a property `kapt` to specify whether we want to use java annotation processor or kapt to process data bindings.
* Specify `composeConfig` for compose. 
  * It has a property `actionCount` to indicate the number of clickable actions.
* If nothing above is specified, the UI layer code will be traditional XML-based layout.
* We cannot expect deterministic ordering when more than one config are specified.

This is not an official Google product.

### License

```
Copyright 2017 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.
```
