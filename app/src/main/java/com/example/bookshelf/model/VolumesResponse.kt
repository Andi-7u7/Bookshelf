package com.example.bookshelf.model

data class VolumesResponse(
    val items: List<VolumeItem> = emptyList()
)

data class VolumeItem(
    val id: String,
    val volumeInfo: VolumeInfo?
)

data class VolumeInfo(
    val title: String,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?,
    val smallThumbnail: String
)
data class BookItem(
    val title: String,
    val thumbnailUrl: String
)
