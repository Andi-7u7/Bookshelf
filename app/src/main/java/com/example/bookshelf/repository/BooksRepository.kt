package com.example.bookshelf.repository

import com.example.bookshelf.model.BookItem

interface BooksRepository {
    suspend fun getBookData(searchQuery: String): List<BookItem>
}