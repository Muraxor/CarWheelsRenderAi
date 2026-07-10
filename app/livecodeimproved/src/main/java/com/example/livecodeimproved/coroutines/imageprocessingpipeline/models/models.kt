package com.example.livecodeimproved.coroutines.imageprocessingpipeline.models

data class Image(
    val id: Int,
    val url: String
)

data class DownloadedImage(
    val id: Int,
    val bytes: ByteArray
)

data class Bitmap(
    val id: Int
)

data class SavedImage(
    val id: Int
)
