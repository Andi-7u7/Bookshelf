package com.example.bookshelf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BooksUiState {
    data class Success(val books: List<com.example.bookshelf.model.BookItem>) : BooksUiState
    object Loading : BooksUiState
    data class Error(val message: String) : BooksUiState
}

class BooksViewModel(
    private val repository: BooksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BooksUiState>(BooksUiState.Loading)
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    init {
        getBooks()
    }

    fun getBooks(query: String = "Kotlin") {
        viewModelScope.launch {
            _uiState.value = BooksUiState.Loading
            try {
                val bookList = repository.getBookData(query)
                _uiState.value = BooksUiState.Success(bookList)
            } catch (e: IOException) {
                _uiState.value = BooksUiState.Error("Fallo de conexión a la red.")
            } catch (e: Exception) {
                _uiState.value = BooksUiState.Error("Error al cargar los datos.")
                e.printStackTrace()
            }
        }
    }
}

class BooksViewModelFactory(private val repository: BooksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BooksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}