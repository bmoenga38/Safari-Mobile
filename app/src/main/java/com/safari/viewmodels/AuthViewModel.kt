package com.safari.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.safari.data.AuthState
import com.safari.data.AuthenticationState
import com.safari.repositories.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    firebaseAuth: FirebaseAuth
) : ViewModel(){

    private val _loginStatus = MutableStateFlow(AuthState())
    val loginStatus = _loginStatus

    private val _registerStatus = MutableStateFlow(AuthState())
    val registerStatus = _registerStatus

    val firebaseUser = firebaseAuth.currentUser

    fun registerUser(email:String, password : String) = viewModelScope.launch {
        authRepo.RegisterUser(email, password).collect{
           result ->
            when(result){
                is AuthenticationState.Success ->{
                    _registerStatus.value = AuthState(isSuccess = true)
                }

                is AuthenticationState.Error -> {
                    _registerStatus.value = AuthState(isError = result.message)
                }
                is AuthenticationState.Loading -> {
                    _registerStatus.value = AuthState(isLoading = true)
                }
            }
        }
    }

    fun loginUser(email:String, password : String) {
        viewModelScope.launch {
            authRepo.loginUser(email, password).collect{
                result ->
                when(result){
                    is AuthenticationState.Error -> {
                        _loginStatus.value = AuthState(isError = result.message)
                    }
                    is AuthenticationState.Loading -> {
                        _loginStatus.value = AuthState(isLoading = true)
                    }
                    is AuthenticationState.Success -> {
                        _loginStatus.value = AuthState(isSuccess = true)
                    }
                }
            }
        }
    }

    fun logoutUser() = Firebase.auth.signOut()


}