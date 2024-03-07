package com.safari.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.safari.screens.MainScreen
import com.safari.utils.AUTHENTICATION
import com.safari.utils.CONTENT
import com.safari.utils.ROOT
import com.safari.viewmodels.AuthViewModel
import com.safari.viewmodels.ShopItemsViewModel

@Composable
fun RootGraph(context: Context) {
    val navHostController = rememberNavController()
    val mainScreenController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val shopItemsViewModel : ShopItemsViewModel = hiltViewModel()
    NavHost(
        navController = navHostController,
        startDestination =
        if (authViewModel.firebaseUser != null){
            RootGraph.Content.route
        }
        else{
            RootGraph.Authentication.route
        },
        route = RootGraph.Root.route
    ) {

        loginGraph(navHostController, context, authViewModel = authViewModel)
        composable(route = RootGraph.Content.route) {
            MainScreen(navHostController = mainScreenController,
                authViewModel = authViewModel,
                shopItemsViewModel = shopItemsViewModel)
        }

    }

}

sealed class RootGraph(val route: String) {
    data object Root : RootGraph(ROOT)
    data object Content : RootGraph(CONTENT)
    data object Authentication : RootGraph(AUTHENTICATION)
}

