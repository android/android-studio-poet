package com.google.androidstudiopoet.models

import com.google.androidstudiopoet.utils.fold

data class ActivityBlueprint(val className: String,
                             val enableCompose: Boolean,
                             val layout: LayoutBlueprint,
                             val where: String,
                             val packageName: String,
                             val classToReferFromActivity: ClassBlueprint,
                             val listenerClassesForDataBinding: List<ClassBlueprint>,
                             private val useButterknife: Boolean) {
    val enableDataBinding = listenerClassesForDataBinding.isNotEmpty()
    val dataBindingClassName = "$packageName.databinding.${layout.name.toDataBindingShortClassName()}"

    val fields: Set<FieldBlueprint> =
            layout.imageViewsBlueprints.mapIndexed { index, it ->
                FieldBlueprint("imageView$index", "android.widget.ImageView", it.getAnnotations()) }.toSet()

    private fun ImageViewBlueprint.getAnnotations(): List<AnnotationBlueprint> {
        if (useButterknife) {
            return listOf(AnnotationBlueprint("butterknife.BindView", mapOf("value" to "R2.id.$id")))
        }
        return listOf()
    }
}



private fun String.toDataBindingShortClassName(): String = this.split("_").map { it.capitalize() }.fold() + "Binding"
