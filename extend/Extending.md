# Extending AS Poet

AS Poet is designed to be extendable. When you want to add a new feature you should follow up the following four steps, each step shouldn't take you more than 30 minutes to grasp. In this doc let's extend AS Poet with my ```cool feature```

## 1. Add your feature to JSON config
At the highest level AS Poet is a function that accept a JSON configuration file that mimics your project and output an Android Studio buildable project based on the JSON config.

### Currently AS Poet has 2 config formats
1. [Compact format](https://github.com/android/android-studio-poet/blob/master/ConfigCompact.json) - the minimal set of configuration to get started or create projects without fine modules control.

2. [Full format](https://github.com/android/android-studio-poet/blob/master/ConfigFull.json) - full configuration control over created modules, that gives you the ability to control every module separately.

We recommend adding features to the full format, it requires less work and keeps the compact json simple. 

So the first step is to update the JSON model to support the new JSON fields. The [input](https://github.com/android/android-studio-poet/tree/master/src/main/kotlin/com/google/androidstudiopoet/input) folder holds all the JSON to objects mappings classes.
Root class for the full format is `com.google.androidstudiopoet.input.GenerationConfig`. Please note, that Gson object is created in the class `com.google.androidstudiopoet.Injector`, there you can customize it.

``` json
        "moduleName": "applicationModule",
        "activityCount": 5,
        "myCoolFeature": "myCoolFeatureValue",
        "hasLaunchActivity": true,
```

If you want to extend compact format, the you need to write/modify a converter from compact format to full, since generator understand only full format models. You can find them in the `converters` package. 

## 2. Create/update blueprint

Blueprints are data classes that hold all the information to create Android Project artifacts, they handles the "business" logic. That allows dependencies resolution without actual code generation.


Here the ones you must absolutely know:
* `PackageBlueprint`
* `ModuleBlueprint`
* `AndroidModuleBlueprint`

## 3. Create/update generator

Generator is the layer that takes a blueprint and writes down its representation as a file.

There are a few details that you should keep in mind:
* All interactions with FS should be done via `com.google.androidstudiopoet.writers.FileWriter`.
* Java code is generated with JavaPoet
* Gradle Scripts are generated with simplified language that you can find in `GradleLang`, feel free to extend it.
* Feel free to add KotlinPoet to this project
* We are currently in the process of choosing "Poet" for XML files, you are welcome to propose one you like via issues on GitHub. 

 

