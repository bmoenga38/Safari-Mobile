package com.safari

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.safari.navigation.RootGraph
import com.safari.screens.ShowIntent
import com.safari.ui.theme.SafariTheme
import com.safari.viewmodels.ShopItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            SafariTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val shopItemsViewModel : ShopItemsViewModel = hiltViewModel()
                    val intent = intent
                    val mainScreenController = rememberNavController()
                    if (intent.action == Intent.ACTION_SEND){
                        val url: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
                        if (url != null) {
                            shopItemsViewModel.intent = url
                        }
                        ShowIntent(shopItemsViewModel = shopItemsViewModel,
                            navHostController = mainScreenController )

                    }
                    else{
                        RootGraph(context = context,mainScreenController)
                    }



                }
            }
        }
    }


 /*   override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
           intention ->  if (intention.action == Intent.ACTION_SEND){
            val url: String? = intention.getStringExtra(Intent.EXTRA_TEXT)
            setContent {
                val shopItemsViewModel : ShopItemsViewModel = hiltViewModel()
                if (url != null) {
                    shopItemsViewModel.intent = url
                }
                ShowIntent(shopItemsViewModel = shopItemsViewModel )
            }
        }

        }
    }*/
}
