package com.example.searchbarwithsharedflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SearchViewModel: ViewModel() {

    private val _searchText = MutableSharedFlow<String>(replay = 1)
    val searchText = _searchText.asSharedFlow()

    private val _names: MutableStateFlow<List<String>> =
        MutableStateFlow(listOf("Amira", "Ahmed", "Anwar", "Nadine", "Samy", "Khalid"))

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    @OptIn(FlowPreview::class)
    val filteredNames = searchText
        .debounce(300L)
        .onEach { _isSearching.value = true }
        .combine(_names) { query, names ->
            if (query.isBlank()) emptyList()
            else names.filter { it.startsWith(query, ignoreCase = true) }
        }
        .onEach { _isSearching.value = false }
        /*.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )*/
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    fun onSearchTextChange(text: String) {
        viewModelScope.launch {
            _searchText.emit(text)
        }
    }

}
