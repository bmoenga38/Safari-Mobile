package com.safari.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.safari.R
import com.safari.navigation.BottomNavigation
import com.safari.navigation.ContentDestinations
import com.safari.navigation.HomeGraph
import com.safari.ui.theme.OpenSans
import com.safari.utils.ADDITEMS
import com.safari.viewmodels.AuthViewModel
import com.safari.viewmodels.ShopItemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    shopItemsViewModel: ShopItemsViewModel,
    authViewModel: AuthViewModel
) {

    val route by navHostController.currentBackStackEntryAsState()
    val currentRoute = route?.destination?.route


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (currentRoute!= null){
                        if (currentRoute==ContentDestinations.Account.route ||
                            currentRoute == ContentDestinations.AddItem.route ||
                            currentRoute == ContentDestinations.UpdateItem.route ||
                            currentRoute == ContentDestinations.Sms.route
                        ) {
                            Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                                contentDescription = "back arrow",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = modifier
                                    .clickable {
                                        navHostController.popBackStack()
                                    }
                                    .padding(start = 20.dp, end = 20.dp)
                                    .size(20.dp)
                                    .background(shape = CircleShape,color = Color.Transparent)
                            )
                        }
                    }

                },

                title = {
                    Text(
                        text = when (currentRoute) {
                            ContentDestinations.Home.route -> {
                                stringResource(id = R.string.Home)
                            }

                            ContentDestinations.ShoppingList.route -> {
                                stringResource(id = R.string.list)
                            }

                            ContentDestinations.Account.route -> {
                                stringResource(id = R.string.account)
                            }

                            ContentDestinations.AddItem.route -> {
                                ADDITEMS
                            }
                            ContentDestinations.UpdateItem.route -> {
                                "Update ${shopItemsViewModel.item.itemName}"
                            }
                            ContentDestinations.Sms.route ->{
                                "Send As a Message"
                            }

                            else -> {
                                ""
                            }
                        },
                        modifier = modifier.padding(start = 10.dp),
                        fontFamily = OpenSans,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                actions = {
                    if (currentRoute == ContentDestinations.ShoppingList.route ||
                        currentRoute == ContentDestinations.Home.route
                    ) {
                        Icon(painter = painterResource(id = R.drawable.baseline_person_outline_24),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = modifier
                                .clickable {
                                    navHostController.navigate(ContentDestinations.Account.route) {
                                        launchSingleTop = true
                                    }
                                }
                                .padding(end = 10.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (currentRoute == ContentDestinations.Home.route ||
                currentRoute == ContentDestinations.ShoppingList.route
            ) {
                BottomNavigation(navHostController = navHostController)
            }
        }
    ) {

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeGraph(
                shopItemsViewModel = shopItemsViewModel,
                navHostController = navHostController,
                authViewModel = authViewModel
            )

        }

    }
}