package com.google.androidstudiopoet.models

data class ResourcesToRefer(val strings: List<String>, val images: List<String>, val layouts: List<String>) {
    fun combine(moreResourcesToRefer: ResourcesToRefer) =
            ResourcesToRefer(strings + moreResourcesToRefer.strings,
                    images + moreResourcesToRefer.images,
                    layouts + moreResourcesToRefer.layouts)
}