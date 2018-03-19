# Extending AS Poet

AS Poet is designed to be extendable. When you want to add a new feature you should follow up the following three steps, each step shouldn't take you more than 30 minutes to grasp. In this doc let's extend AS Poet with my ```cool feature```. All in all, you will need to modify/create three files: one JSON and two Kotlin classes.

## 1. Add your feature to the JSON config
At the highest level, think of AS Poet as of a function that accept a JSON configuration file that mimics your project and outputs an Android Studio buildable project(folders and files) based on the JSON config.

### Currently AS Poet has 2 JSON config formats
1. [Compact format](https://github.com/android/android-studio-poet/blob/master/ConfigCompact.json) - the the minimal set of configurations to get started or create projects without fine module control.

2. [Full format](https://github.com/android/android-studio-poet/blob/master/ConfigFull.json) - full configuration control over created modules, that gives you the ability to control every module separately in terms of dependencies, method count and class count.

We recommend adding features to the full format, as it requires less work and keeps the compact json simple. 

You start by updating the JSON model to support your new JSON fields. The [input](https://github.com/android/android-studio-poet/tree/master/src/main/kotlin/com/google/androidstudiopoet/input) folder holds all the JSON to objects mappings classes.
Root class for the full format is [`com.google.androidstudiopoet.input.GenerationConfig`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/input/GenerationConfig.kt). Please note, that Gson object is created in the class [`com.google.androidstudiopoet.Injector`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/Injector.kt), there you can customize it. Read the code sample below (taken from [`ConfigFull`](https://github.com/android/android-studio-poet/blob/master/ConfigFull.json)):

``` json
    "moduleName": "applicationModule",
    "activityCount": 5,
    "myCoolFeature": "myCoolFeatureValue",
    "hasLaunchActivity": true,
```

If you want to extend compact format, the you need to write/modify a converter from compact format to full, since generator understand only full format models. You can find them in the [`converters`](https://github.com/android/android-studio-poet/tree/master/src/main/kotlin/com/google/androidstudiopoet/converters) package. 

## 2. Create/update blueprint

Blueprints are data classes that hold all the information to create Android Project artifacts, they handles the "business" logic. That architecture allow the following benefits:

1. Dependencies resolution between the projects (one of the complicated features) without the actual code generation
2. Separation of concerns, keeping the blueprints easy to grasp, just by looking

Here the blueprints you must absolutely know:
* [`PackageBlueprint`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/models/PackageBlueprint.kt)
* [`ModuleBlueprint`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/models/ModuleBlueprint.kt)
* [`AndroidModuleBlueprint`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/models/AndroidModuleBlueprint.kt)

Start by looking at the above classes, find the places you want to modify or add a new blueprint. Prefer to add a new  to keep the blue prints super easy.

For example: [`ModuleBuildGradleBlueprint`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/models/ModuleBuildGradleBlueprint.kt).
It is used to describe `build.gradle` file of `java-library` module. 

* As input it takes list of dependencies and different flags (some blueprint takes directly input objects)
* Depending on the input flags it create sets of plugins and libraries that should be applied/included into resulted `build.gradle` file 
* It also responsible for calculating path to the new file

## 3. Create/update generator

Generator is the layer that takes a blueprint and writes down its representation as a file. 

For example [`ModuleBuildGradleGenerator`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/generators/ModuleBuildGradleGenerator.kt). 
* It takes `ModuleBuildGradleBlueprint` described above
* It transforms input into the list of statements from [`GradleLang`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/gradle/GradleLang.kt)
* Each statement can display itself as a part of Gradle script
* Saves result into the file

### Technical details to keep in mind:
* All interactions with the file system should be done via [`FileWriter`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/writers/FileWriter.kt).
* Java code is generated with [JavaPoet](https://github.com/square/javapoet)
* Gradle Scripts are generated with simplified language that you can find in [`GradleLang`](https://github.com/android/android-studio-poet/blob/master/src/main/kotlin/com/google/androidstudiopoet/gradle/GradleLang.kt), feel free to extend it.
* Feel free to add [KotlinPoet](https://github.com/square/kotlinpoet) to this project
* We are currently in the process of choosing "Poet" for XML files, you are welcome to propose one you like via issues on GitHub. 


 

