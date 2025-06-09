package com.example.fetchrewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.fetchrewards.ui.theme.FetchRewardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val itemViewModel = ViewModelProvider(this)[ItemViewModel::class.java]
            FetchRewardsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  ItemListScreen(
                      modifier = Modifier.padding(innerPadding),
                      viewModel = itemViewModel
                  )
                }
            }
        }
    }
}
