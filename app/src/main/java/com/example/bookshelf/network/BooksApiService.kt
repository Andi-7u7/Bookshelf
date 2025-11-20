package com.example.bookshelf.network

import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.model.VolumesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes")
    suspend fun searchVolumes(
        @Query("q") query: String
    ): VolumesResponse

    @GET("volumes/{volume_id}")
    suspend fun getVolumeInfo(
        @Path("volume_id") volumeId: String
    ): VolumeInfo
}