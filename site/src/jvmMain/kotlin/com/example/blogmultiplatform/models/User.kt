@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.example.blogmultiplatform.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.id.ObjectIdGenerator


@Serializable
actual data class User(
    @SerialName(value = "_id")
    actual val _id: String = ObjectIdGenerator.newObjectId<String>().id.toHexString(),
    actual val username : String="",
    actual val password: String=""

)

@Serializable
actual data class UserWithoutPassword(
    @SerialName(value = "_id")
    actual val _id: String = ObjectIdGenerator.newObjectId<String>().id.toHexString(),
    actual val username : String=""


)
