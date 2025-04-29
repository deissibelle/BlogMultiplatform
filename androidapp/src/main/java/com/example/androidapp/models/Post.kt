package com.example.androidapp.models

import com.example.shared.Category
import com.couchbase.lite.*
import java.util.UUID

data class Post(
    var _id: String = UUID.randomUUID().toString(), // Couchbase Lite uses document IDs
    var author: String = "",
    var date: Long = 0L,
    var title: String = "",
    var subtitle: String = "",
    var thumbnail: String = "",
    var category: String = Category.Programming.name
) {
    fun toDocument(): MutableDocument {
        return MutableDocument(_id).apply {
            setString("author", author)
            setLong("date", date)
            setString("title", title)
            setString("subtitle", subtitle)
            setString("thumbnail", thumbnail)
            setString("category", category)
        }
    }

    companion object {
        fun fromDocument(doc: Document): Post {
            return Post(
                _id = doc.id,
                author = doc.getString("author") ?: "",
                date = doc.getLong("date"),
                title = doc.getString("title") ?: "",
                subtitle = doc.getString("subtitle") ?: "",
                thumbnail = doc.getString("thumbnail") ?: "",
                category = doc.getString("category") ?: Category.Programming.name
            )
        }
    }
}