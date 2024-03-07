package com.safari.navigation

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.safari.screens.LoginScreen
import com.safari.screens.RegisterScreen
import com.safari.utils.LOGIN
import com.safari.utils.SIGNUP
import com.safari.viewmodels.AuthViewModel

sealed class AuthGraph(val route: String) {
    data object Login : AuthGraph(LOGIN)
    data object SignUp : AuthGraph(SIGNUP)
}

fun NavGraphBuilder.loginGraph(
    navHostController: NavHostController,
    context: Context,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = AuthGraph.Login.route,
        route = RootGraph.Authentication.route,
    ) {
        composable(route = AuthGraph.Login.route) {
            LoginScreen(
                navController = navHostController,
                authViewModel = authViewModel,
                context = context
            )
        }
        composable(route = AuthGraph.SignUp.route) {
            RegisterScreen(
                navHostController = navHostController,
                authViewModel = authViewModel,
                context = context
            )


        }
    }
}