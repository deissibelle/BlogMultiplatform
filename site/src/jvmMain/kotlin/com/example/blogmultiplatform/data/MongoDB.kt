package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.util.Constants.DATABASE_NAME
import com.mongodb.client.model.Filters
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.and
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.serialization.SerializationClassMappingTypeService


@InitApi
fun initMongoDB(
    context: InitApiContext
){
    System.setProperty(
        "org.litote.mongo.mapping.service",
        SerializationClassMappingTypeService::class.qualifiedName!!
    )
    context.data.add(MongoDB(context))
}

class MongoDB(private val context: InitApiContext): MongoRepository {
    private  val  client = KMongo.createClient()
    private  val database = client.getDatabase(DATABASE_NAME)
    private  val userCollection = database.getCollection<User>()
    private val postCollection = database.getCollection<Post>()

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).awaitFirst().wasAcknowledged()
    }
    override suspend fun checkUserExistence(user: User): User? {
        return  try {
            userCollection
                .find(
                and(
                    User :: username eq user.username,
                    User :: password eq user.password
                )

            ).awaitFirst()
        }catch (e:Exception){
            context.logger.error(e.message.toString())
            null
        }


    }
    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(Filters.eq(User::_id.name, id)).awaitFirst()
            documentCount > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }


}