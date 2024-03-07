package com.safari.dependencyInjection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.safari.repositories.AuthRepo
import com.safari.repositories.AuthRepoImpl
import com.safari.repositories.ItemToShop
import com.safari.repositories.ItemToShopImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HiltModule {

    @Provides
    @Singleton
    fun providesLoginRepo(firebaseUser: FirebaseAuth): AuthRepo {
        return AuthRepoImpl(firebaseUser)
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun providesItemRepo(
        firebaseDatabase: FirebaseDatabase,
        firebaseUser: FirebaseAuth,
        firebaseStorage: FirebaseStorage
    ): ItemToShop {
        return ItemToShopImpl(firebaseDatabase = firebaseDatabase, user = firebaseUser)
    }

    @Provides
    @Singleton
    fun providesFirebaseStorage () : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


}