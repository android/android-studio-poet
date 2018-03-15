# Extending AS Poet

AS Poet is designed to be extendable. When you want to add a new feature you should follow up the following four steps, each step shouldn't take you more than 30 minutes to grasp. In this doc let's extend AS Poet with my ```cool feature```

## 1. Add your feature to JSON config
At the highest level AS Poet is a function that accept a JSON configuration file that mimics your project and output an Android Studio buildable project based on the JSON config.

### Currently AS Poet has 2 config formats
1. [Compact format](https://github.com/android/android-studio-poet/blob/master/ConfigCompact.json) - the minimal set of configuration to get started or create projects wihtout fine modules control.

2. [Full format](https://github.com/android/android-studio-poet/blob/master/ConfigFull.json) - full configuration control over created modules, that gives you the ability to control every module.

We recommend adding features to the full format json to keep the compact json simple.

``` json
        "moduleName": "applicationModule",
        "activityCount": 5,
        "myCoolFeature": "myCoolFeatureValue",
        "hasLaunchActivity": true,
```

## 2. Update the model
Great, the next step is to update the JSON model to support the newly added JSON field. The [input](https://github.com/android/android-studio-poet/tree/master/src/main/kotlin/com/google/androidstudiopoet/input) folder holds all the JSON to objects mappings classes.

## 3. Create/update blueprint

Blueprints are data classes that hold all the information to create Android Studio artifacts 

Here the ones you must absolutely know:
* PackageBlueprint
* ModuleBlueprint
* ClassBlueprint

## 4. Create/update generator

Generator is ther layer that takes a blueprint and writes down its representation as a file.
