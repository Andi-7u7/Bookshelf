package com.example.bookshelf.repository

import com.example.bookshelf.network.ApiResponseType
import com.example.bookshelf.network.FakeBooksApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before // Necesitas Before para la función setup
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BooksRepositoryImplTest {

    private val testQuery = "Kotlin"
    private lateinit var fakeApiService: FakeBooksApiService
    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setup() {
        fakeApiService = FakeBooksApiService(ApiResponseType.SUCCESS)
        repository = BooksRepositoryImpl(fakeApiService)
    }

    @Test
    fun getBookData_success_returnsFilteredListWithCorrectUrl() = runTest {

        val result = repository.getBookData(testQuery)


        assertEquals(2, result.size)

        assertEquals("Título del Libro 1", result[0].title)
        assertTrue(result[0].thumbnailUrl.startsWith("https://")) // Verifica la corrección
        assertTrue(result[0].thumbnailUrl.endsWith("thumb1.jpg"))

        assertEquals("Título del Libro 2", result[1].title)
        assertTrue(result[1].thumbnailUrl.endsWith("small2.jpg")) // Verifica el fallback
    }

    @Test
    fun getBookData_emptyResponse_returnsEmptyList() = runTest {
        val fakeApiServiceEmpty = FakeBooksApiService(ApiResponseType.EMPTY)
        val repositoryEmpty = BooksRepositoryImpl(fakeApiServiceEmpty)

        val result = repositoryEmpty.getBookData(testQuery)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getBookData_networkError_returnsEmptyListAndLogsError() = runTest {
        val fakeApiServiceError = FakeBooksApiService(ApiResponseType.ERROR)
        val repositoryError = BooksRepositoryImpl(fakeApiServiceError)

        val result = repositoryError.getBookData(testQuery)

        assertTrue(result.isEmpty())
    }
}