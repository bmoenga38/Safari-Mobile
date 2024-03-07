package com.safari.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.safari.viewmodels.AuthViewModel

@Composable
 fun Logout(modifier : Modifier = Modifier, authViewModel: AuthViewModel){

     Column (modifier = modifier
         .fillMaxSize()
         .background(color = MaterialTheme.colorScheme.primary),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally,
         ){

         OutlinedButton(onClick = {
             authViewModel.logoutUser()
         }) {
             Text(text = "Logout",color = MaterialTheme.colorScheme.onPrimary)
         }

     }

 }