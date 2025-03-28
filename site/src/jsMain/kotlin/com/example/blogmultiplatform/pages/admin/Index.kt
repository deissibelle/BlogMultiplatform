package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.util.isUserLoggedIn
import com.varabyte.kobweb.core.Page







@Page
@Composable
fun HomePage(){
    isUserLoggedIn {
        HomeScreen()
    }

}

@Composable
fun HomeScreen(){
    println("Admin Home Page")

}