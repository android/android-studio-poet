package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.fold

data class ActivityBlueprint(val className: String, val layout: String, val where: String, val packageName: String,
                             val classToReferFromActivity: ClassBlueprint, val listenerClassesForDataBinding: List<ClassBlueprint>) {
    val hasDataBinding = listenerClassesForDataBinding.isNotEmpty()
    val dataBindingClassName = "$packageName.databinding.${layout.toDataBindingShortClassName()}"
}

private fun String.toDataBindingShortClassName(): String = this.split("_").map { it.capitalize() }.fold() + "Binding"
