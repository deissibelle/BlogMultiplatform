package com.example.androidapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.data.CouchbaseSync
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _allPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts

    private val _searchedPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val searchedPosts: State<RequestState<List<Post>>> = _searchedPosts

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                CouchbaseSync.configureDatabase() // Ensure DB is initialized before queries
                fetchAllPosts()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Database initialization failed: ${e.message}")
                _allPosts.value = RequestState.Error(Exception("Database error: ${e.message}"))
            }
        }
    }

    private suspend fun fetchAllPosts() {
        _allPosts.value = RequestState.Loading
        try {
            CouchbaseSync.readAllPosts().collectLatest {
                _allPosts.value = it
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error fetching posts: ${e.message}")
            _allPosts.value = RequestState.Error(Exception("Failed to load posts"))
        }
    }

    fun searchPostsByTitle(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchedPosts.value = RequestState.Loading
            try {
                CouchbaseSync.searchPostsByTitle(query).collectLatest {
                    _searchedPosts.value = it
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Search error: ${e.message}")
                _searchedPosts.value = RequestState.Error(Exception("Search failed"))
            }
        }
    }

    fun resetSearchedPosts() {
        _searchedPosts.value = RequestState.Idle
    }
}