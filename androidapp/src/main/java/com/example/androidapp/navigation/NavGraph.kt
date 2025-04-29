package com.example.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.androidapp.navigation.destinations.detailsRoute
import com.example.androidapp.navigation.destinations.homeRoute
import com.example.androidapp.navigation.destinations.categoryRoute


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        homeRoute(
            onCategorySelect = { category ->
                navController.navigate(Screen.Category.passCategory(category))
            },
            onPostClick = { postId ->
                navController.navigate(Screen.Details.passPostId(postId))
            }
        )
        categoryRoute(
            onBackPress = { navController.popBackStack() },
            onPostClick = { postId ->
                navController.navigate(Screen.Details.passPostId(postId))
            }
        )
        detailsRoute(onBackPress = { navController.popBackStack() })
    }
}