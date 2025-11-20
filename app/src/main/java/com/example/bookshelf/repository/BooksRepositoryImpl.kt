package com.example.bookshelf.repository

import com.example.bookshelf.network.BooksApiService
import com.example.bookshelf.model.BookItem
import java.io.IOException

class BooksRepositoryImpl(
    private val booksApiService: BooksApiService
) : BooksRepository {

    override suspend fun getBookData(searchQuery: String): List<BookItem> {

        val bookList = mutableListOf<BookItem>()

        try {
            val volumesResponse = booksApiService.searchVolumes(searchQuery)

            for (item in volumesResponse.items) {

                val volumeInfo = item.volumeInfo

                if (volumeInfo == null) {
                    println("Volumen ${item.id} omitido: volumeInfo es nulo en la respuesta de búsqueda.")
                    continue
                }

                val imageLinks = volumeInfo.imageLinks

                val rawThumbnailUrl = imageLinks?.thumbnail ?: imageLinks?.smallThumbnail

                val thumbnailUrl = rawThumbnailUrl?.replace("http:", "https:")

                if (thumbnailUrl != null) {
                    bookList.add(
                        BookItem(
                            title = volumeInfo.title,
                            thumbnailUrl = thumbnailUrl
                        )
                    )
                } else {

                    println("Volumen ${item.id} omitido: No se encontró URL de imagen válida en la respuesta de búsqueda.")
                }
            }

        } catch (e: Exception) {
            println("-----------------------------------------------------------------")
            println("ERROR (Inesperado) durante la búsqueda de volúmenes:")
            println("Causa: ${e.message}")
            println("Stack Trace Completo:")
            println(e.stackTraceToString())
            println("-----------------------------------------------------------------")
        }

        return bookList
    }
}
