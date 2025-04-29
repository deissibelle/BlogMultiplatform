package com.example.androidapp.data

import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import com.example.shared.Category
import kotlinx.coroutines.flow.Flow

interface CouchbaseSyncRepository {
    fun configureDatabase() // Initialisation de la base Couchbase Lite
    fun readAllPosts(): Flow<RequestState<List<Post>>> // Lire tous les articles
    fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>> // Recherche par titre
    fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>> // Recherche par catégorie
}