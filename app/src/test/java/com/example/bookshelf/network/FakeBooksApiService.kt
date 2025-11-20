package com.example.bookshelf.network

import com.example.bookshelf.model.*
import java.io.IOException

enum class ApiResponseType { SUCCESS, EMPTY, ERROR }

class FakeBooksApiService(
    private val responseType: ApiResponseType = ApiResponseType.SUCCESS
) : BooksApiService {

    private val fakeVolumeId = "AZ2kBgAAQBAJ"
    private val fakeThumbnailUrlHttp = "http://books.google.com/content/images/fake.jpg"

    private val fakeVolumeItems = listOf(
        VolumeItem(
            id = fakeVolumeId,
            volumeInfo = VolumeInfo(
                title = "Título del Libro 1",
                imageLinks = ImageLinks(thumbnail = fakeThumbnailUrlHttp, smallThumbnail = "http://books.com/small1.jpg")
            )
        ),
        VolumeItem(
            id = "ID_2",
            volumeInfo = VolumeInfo(
                title = "Título del Libro 2",
                imageLinks = ImageLinks(thumbnail = null, smallThumbnail = "http://books.com/small2.jpg")
            )
        )
    )

    override suspend fun searchVolumes(query: String): VolumesResponse {
        return when (responseType) {
            ApiResponseType.SUCCESS -> VolumesResponse(items = fakeVolumeItems)
            ApiResponseType.EMPTY -> VolumesResponse(items = emptyList())
            ApiResponseType.ERROR -> throw IOException("Simulated Network Error during search.")
        }
    }

    override suspend fun getVolumeInfo(volumeId: String): VolumeInfo {
        if (responseType == ApiResponseType.ERROR) {
            throw IOException("Simulated Network Error during getVolumeInfo.")
        }

        val item = fakeVolumeItems.find { it.id == volumeId }

        return when (volumeId) {
            fakeVolumeId, "ID_2" -> item!!.volumeInfo!!
            else -> throw IllegalArgumentException( "ID de volumen no esperado: $volumeId")
        }
    }
}