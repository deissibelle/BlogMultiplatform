package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBody
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator

@Api(routeOverride = "addpost")
suspend fun addPost(context: ApiContext) {
    try {
        val postJson = context.req.body?.decodeToString()
        val post = postJson?.let { Json.decodeFromString<Post>(it) }
        val newPost = post?.copy(_id = ObjectIdGenerator().generate().toString())

        val isAdded = newPost?.let {
            context.data.getValue<MongoDB>().addPost(it)
        } ?: false

        context.res.setBody(isAdded)
    } catch (e: Exception) {
        context.res.setBody(e.message ?: "An error occurred")
    }
}
