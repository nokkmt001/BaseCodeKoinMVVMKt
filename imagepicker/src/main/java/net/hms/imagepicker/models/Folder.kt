package net.hms.imagepicker.models

data class Folder (
    var name: String? = null,
    var path: String? = null,
    var cover: Image? = null,
    var images: ArrayList<Image> = ArrayList()
)

