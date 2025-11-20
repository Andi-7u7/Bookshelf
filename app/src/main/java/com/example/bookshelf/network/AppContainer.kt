package com.example.bookshelf.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.bookshelf.repository.BooksRepository
import com.example.bookshelf.repository.BooksRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object AppContainer {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient) // <-- Usamos el cliente con logging
        .build()

    private val booksApiService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }

    val booksRepository: BooksRepository by lazy {
        BooksRepositoryImpl(booksApiService)
    }
}