package com.example.bookshelf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.model.BookItem
import com.example.bookshelf.network.AppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookGridScreen() {
    val viewModel: BooksViewModel = viewModel(
        factory = BooksViewModelFactory(AppContainer.booksRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Bookshelf App") }) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is BooksUiState.Loading -> CircularProgressIndicator()
                is BooksUiState.Success -> {
                    BookGrid(books = (uiState as BooksUiState.Success).books)
                }
                is BooksUiState.Error -> {
                    val errorMessage = (uiState as BooksUiState.Error).message
                    ErrorContent(errorMessage) { viewModel.getBooks() }
                }
            }
        }
    }
}

@Composable
fun BookGrid(books: List<BookItem>, modifier: Modifier = Modifier) {
    if (books.isEmpty()) {
        Text("No se encontraron libros.")
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(books) { book ->
            BookItemCard(book = book)
        }
    }
}

@Composable
fun BookItemCard(book: BookItem, modifier: Modifier = Modifier) {
    Card(modifier = modifier.aspectRatio(0.6f)) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(book.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text(message)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}