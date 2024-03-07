package com.safari.repositories

import com.google.android.gms.tasks.Task
import com.safari.data.DataState
import com.safari.data.ShopItem
import kotlinx.coroutines.flow.Flow

interface ItemToShop {


    suspend fun deleteItem(item: ShopItem) : Flow<DataState<Task<Void>>>
    suspend fun updateItem(item: ShopItem) : Flow<DataState<Task<Void>>>
}