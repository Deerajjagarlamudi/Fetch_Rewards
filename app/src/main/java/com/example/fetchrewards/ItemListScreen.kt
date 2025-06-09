package com.example.fetchrewards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun ItemListScreen(modifier: Modifier = Modifier, viewModel: ItemViewModel) {
    val pagedItems = viewModel.pagedItems.collectAsLazyPagingItems()

    LazyColumn(modifier = modifier.fillMaxSize().border(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(12.dp)
    )) {

        if (pagedItems.loadState.refresh is LoadState.Loading) {
            item {
                LoadingIndicator("Loading items...")
            }
        }

        if (pagedItems.loadState.refresh is LoadState.Error) {
            val error = pagedItems.loadState.refresh as LoadState.Error
            item {
                ErrorMessage(error.error.localizedMessage ?: "Something went wrong") {
                    pagedItems.retry()
                }
            }
        }

        items(pagedItems.itemCount) { index ->
            val item = pagedItems[index]
            when (item) {
                is UiItem.Header -> {
                    Text(
                        text = "Fetch Rewards",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(vertical = 16.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
                is UiItem.Content -> {
                    ItemRow(item.item)
                }
                null -> {
                    PlaceholderRow()
                }
            }
        }

        if (pagedItems.loadState.append is LoadState.Loading) {
            item {
                LoadingIndicator("Loading more...")
            }
        }

        if (pagedItems.loadState.append is LoadState.Error) {
            val error = pagedItems.loadState.append as LoadState.Error
            item {
                ErrorMessage(error.error.localizedMessage ?: "Failed to load more items") {
                    pagedItems.retry()
                }
            }
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ID: ${item.id}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f).padding(8.dp)
        )
        Text(
            text = item.name ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f).padding(8.dp)
        )
    }
}

@Composable
fun LoadingIndicator(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = message, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Red.copy(alpha = 0.1f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        ClickableText(
            text = AnnotatedString("Tap to retry"),
            onClick = { onRetry() },
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun PlaceholderRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loading...", style = MaterialTheme.typography.bodySmall)
    }
}