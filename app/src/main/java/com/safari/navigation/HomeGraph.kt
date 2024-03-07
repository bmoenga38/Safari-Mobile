package com.safari.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.safari.R
import com.safari.screens.ChooseImage
import com.safari.screens.ContactForm
import com.safari.screens.HomePage
import com.safari.screens.Logout
import com.safari.screens.ToShopPage
import com.safari.screens.UpdateItem
import com.safari.utils.ACCOUNT
import com.safari.utils.ADDITEMS
import com.safari.utils.HOME
import com.safari.utils.SHOPPINGLIST
import com.safari.utils.SMS
import com.safari.utils.UPDATE
import com.safari.viewmodels.AuthViewModel
import com.safari.viewmodels.ShopItemsViewModel

@Composable
fun HomeGraph(shopItemsViewModel: ShopItemsViewModel, authViewModel: AuthViewModel,navHostController: NavHostController) {


    NavHost(
        navController = navHostController,
        startDestination = ContentDestinations.Home.route,
        route = RootGraph.Content.route
    ) {
        composable(route = ContentDestinations.Home.route) {
            HomePage(navHostController = navHostController, shopItemsViewModel = shopItemsViewModel)
        }
        composable(ContentDestinations.ShoppingList.route) {
            ToShopPage(shopItemsViewModel = shopItemsViewModel, navHostController = navHostController)

        }
        composable(route = ContentDestinations.AddItem.route){
            ChooseImage(shopItemsViewModel = shopItemsViewModel)
        }
        composable(route = ContentDestinations.UpdateItem.route) {
            UpdateItem(item = shopItemsViewModel.item, shopItemsViewModel = shopItemsViewModel)

        }
        composable(route = ContentDestinations.Account.route) {
            Logout(authViewModel = authViewModel )


        }
        composable(route = ContentDestinations.Sms.route) {
            ContactForm(item = shopItemsViewModel.item)


        }
        composable(route = ContentDestinations.Sms.route) {
            ContactForm(item = shopItemsViewModel.item)


        }


    }

}

sealed class ContentDestinations(val route: String, val icon: Int?) {
    data object Home : ContentDestinations(
        route = HOME,
        icon = R.drawable.round_home_24
    )

    data object ShoppingList : ContentDestinations(
        route = SHOPPINGLIST,
        icon = R.drawable.baseline_calendar_view_day_24
    )

    data object Account : ContentDestinations(
        route = ACCOUNT,
        icon = R.drawable.baseline_person_outline_24
    )
    data object AddItem : ContentDestinations(
        route = ADDITEMS,
        icon = null
    )
    data object UpdateItem : ContentDestinations(
        route = UPDATE,
        icon = null
    )
    data object Sms : ContentDestinations(
        route = SMS,
        icon = null
    )

}

@Composable
fun BottomNavigation(modifier: Modifier = Modifier,
                     navHostController: NavHostController) {
    val destinations = listOf(
        ContentDestinations.Home,
        ContentDestinations.ShoppingList,
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentPath = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 4.dp
    ) {

        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentPath == destination.route,
                onClick = {
                    navHostController.navigate(destination.route) {
                        navHostController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(

                        painter = painterResource(id = destination.icon!!),
                        contentDescription = HOME, modifier = modifier.size(24.dp)
                    )

                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor =  Color.Gray,
                ))
        }

    }
}