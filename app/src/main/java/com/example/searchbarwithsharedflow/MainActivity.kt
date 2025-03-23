package com.example.searchbarwithsharedflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.searchbarwithsharedflow.ui.theme.SearchBarWithSharedFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchBarWithSharedFlowTheme {

                val viewModel: SearchViewModel = viewModel()
                val searchText = viewModel.searchText.collectAsStateWithLifecycle(initialValue = "")
                //val names = viewModel.filteredNames.collectAsStateWithLifecycle()
                val names = viewModel.filteredNames.collectAsStateWithLifecycle(initialValue = emptyList())
                val isSearching = viewModel.isSearching.collectAsStateWithLifecycle()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding (vertical = 40.dp, horizontal = 16.dp)
                ) {
                    TextField(
                        value = searchText.value,
                        onValueChange = { name -> viewModel.onSearchTextChange(name) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Search") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isSearching.value) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(names.value) { name ->
                                Text(
                                    text = name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


