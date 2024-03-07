package com.safari.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.safari.data.AuthenticationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepo {
    override suspend fun loginUser(
        email: String,
        password: String
    ): Flow<AuthenticationState<AuthResult>> {
        return flow {
            emit(AuthenticationState.Loading())
            try {
               val result =  firebaseAuth.signInWithEmailAndPassword(email,password)
                   .await()
                emit(AuthenticationState.Success(result))
            }
            catch (e:Exception){
                emit(AuthenticationState.Error(e.message.toString()))
            }
        }
    }

    override suspend fun RegisterUser(
        email: String,
        password: String
    ): Flow<AuthenticationState<AuthResult>> {
       return flow {
           emit(AuthenticationState.Loading())
           try {
               val result = firebaseAuth.createUserWithEmailAndPassword(email,password)
                   .await()
               emit(AuthenticationState.Success(result))
           }
           catch (e : Exception){
               emit(AuthenticationState.Error(e.message.toString()))
           }

       }
    }
}