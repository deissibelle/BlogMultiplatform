@file:Suppress("DEPRECATION")

package com.example.androidapp.data

import android.util.Log
import com.couchbase.lite.*
import com.example.shared.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.URI

object CouchbaseSync : CouchbaseSyncRepository {

    private var database: Database? = null

    override fun configureDatabase() {
        try {
            if (database == null) {
                Log.d("CouchbaseSync", "Initializing database...")
                val config = DatabaseConfiguration()
                database = Database("blog_posts", config)
                Log.d("CouchbaseSync", "Database initialized successfully.")
                configureReplication() // Start replication if needed
            }
        } catch (e: Exception) {
            Log.e("CouchbaseSync", "Database initialization failed: ${e.message}")
        }
    }

    private fun configureReplication() {
        try {
            if (database == null) {
                throw IllegalStateException("Database not initialized!")
            }

            Log.d("CouchbaseSync", "Starting replication...")

            val replicatorConfig = ReplicatorConfiguration(database!!, URLEndpoint(URI("wss://your-capella-endpoint")))
            replicatorConfig.replicatorType = AbstractReplicatorConfiguration.ReplicatorType.PUSH_AND_PULL
            replicatorConfig.authenticator = BasicAuthenticator("your-username", "your-password".toCharArray())

            val replicator = Replicator(replicatorConfig)
            replicator.start()

            Log.d("CouchbaseSync", "Replication started successfully.")
        } catch (e: Exception) {
            Log.e("CouchbaseSync", "Replication failed: ${e.message}")
        }
    }

    override fun readAllPosts(): Flow<RequestState<List<Post>>> {
        configureDatabase()

        return try {
            database ?: throw IllegalStateException("Database has not been initialized!")

            val query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database!!))

            flow {
                val posts = query.execute().mapNotNull { result ->
                    result.getDictionary(0)?.let { doc ->
                        Post(
                            _id = doc.getString("_id") ?: "",
                            author = doc.getString("author") ?: "",
                            date = doc.getLong("date") ?: 0L,
                            title = doc.getString("title") ?: "",
                            subtitle = doc.getString("subtitle") ?: "",
                            thumbnail = doc.getString("thumbnail") ?: "",
                            category = doc.getString("category") ?: "General"
                        )
                    }
                }
                emit(RequestState.Success(posts))
            }
        } catch (e: Exception) {
            flow { emit(RequestState.Error(Exception("Database error: ${e.message}"))) }
        }
    }

    override fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>> {
        configureDatabase()

        return try {
            val queryBuilder = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database!!))
                .where(Expression.property("title").like(Expression.string("%$query%")))

            flow {
                val posts = queryBuilder.execute().mapNotNull { result ->
                    result.getDictionary(0)?.let { doc ->
                        Post(
                            _id = doc.getString("_id") ?: "",
                            author = doc.getString("author") ?: "",
                            date = doc.getLong("date") ?: 0L,
                            title = doc.getString("title") ?: "",
                            subtitle = doc.getString("subtitle") ?: "",
                            thumbnail = doc.getString("thumbnail") ?: "",
                            category = doc.getString("category") ?: "General"
                        )
                    }
                }
                emit(RequestState.Success(posts))
            }
        } catch (e: Exception) {
            flow { emit(RequestState.Error(Exception("Database error: ${e.message}"))) }
        }
    }

    override fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>> {
        configureDatabase()

        return try {
            val queryBuilder = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database!!))
                .where(Expression.property("category").equalTo(Expression.string(category.name)))

            flow {
                val posts = queryBuilder.execute().mapNotNull { result ->
                    result.getDictionary(0)?.let { doc ->
                        Post(
                            _id = doc.getString("_id") ?: "",
                            author = doc.getString("author") ?: "",
                            date = doc.getLong("date") ?: 0L,
                            title = doc.getString("title") ?: "",
                            subtitle = doc.getString("subtitle") ?: "",
                            thumbnail = doc.getString("thumbnail") ?: "",
                            category = doc.getString("category") ?: "General"
                        )
                    }
                }
                emit(RequestState.Success(posts))
            }
        } catch (e: Exception) {
            flow { emit(RequestState.Error(Exception("Database error: ${e.message}"))) }
        }
    }
}