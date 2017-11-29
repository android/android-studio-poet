package com.google.androidstudiopoet.models

open class ModuleDependency(val name: String, val methodToCall: MethodToCall)

class AndroidModuleDependency(name: String, methodToCall: MethodToCall, val resourcesToRefer: ResourcesToRefer)
    : ModuleDependency(name, methodToCall)

