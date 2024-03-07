package com.safari.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.safari.data.DataState
import com.safari.data.ShopItem
import com.safari.utils.TOSHOP
import com.safari.utils.USERS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemToShopImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val user: FirebaseAuth
): ItemToShop {




    override suspend fun deleteItem(item: ShopItem): Flow<DataState<Task<Void>>> {
        return flow {
            emit(DataState.Loading())
            try {
                if (user.currentUser?.uid != null){
                    val result = firebaseDatabase.reference.child(USERS)
                        .child(user.currentUser!!.uid)
                        .child(TOSHOP)
                        .child(item.itemName)
                        .removeValue()
                    emit(DataState.Success(result))
                }
            }
            catch (e : Exception){
                emit(DataState.Error(e.message.toString()))
            }
        }
    }

    override suspend fun updateItem(item: ShopItem): Flow<DataState<Task<Void>>> {
        return flow {
            emit(DataState.Loading())
            try {
                if (user.currentUser?.uid != null){
                    val newValue = HashMap<String, Any>()
                    newValue["bought"] = item.isBought
                    newValue["delegated"] = item.isDelegated
                    newValue["category"]= item.category
                    newValue["dateCreated"] = item.dateCreated
                    newValue["dateDue"] = item.dateDue
                    newValue["itemName"] = item.itemName
                    newValue["itemDescription"] = item.itemDescription
                    newValue["price"] = item.price
                    newValue["shoppingPlace"] = item.shoppingPlace

                    val result = firebaseDatabase.reference.child(USERS)
                        .child(user.currentUser!!.uid)
                        .child(TOSHOP)
                        .child(item.itemName)
                        .updateChildren(newValue )
                    emit(DataState.Success(result))
                }
            }
            catch (e : Exception){
                emit(DataState.Error(e.message.toString()))
            }
        }
    }
}