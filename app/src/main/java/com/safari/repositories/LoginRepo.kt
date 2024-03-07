package com.safari.repositories

import com.google.firebase.auth.AuthResult
import com.safari.data.AuthenticationState
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun loginUser(email : String, password : String):Flow<AuthenticationState<AuthResult>>
    suspend fun RegisterUser(email: String,password: String):Flow<AuthenticationState<AuthResult>>
}