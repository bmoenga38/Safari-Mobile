package com.safari.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.safari.data.AuthState
import com.safari.data.DataState
import com.safari.data.ShopItem
import com.safari.repositories.ItemToShopImpl
import com.safari.utils.IMAGE
import com.safari.utils.TOSHOP
import com.safari.utils.USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopItemsViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val itemRepo: ItemToShopImpl,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _getItems = MutableStateFlow<List<ShopItem>>(emptyList())
    val getItems = _getItems.asStateFlow()
    private val _getDelegatedItems = MutableStateFlow<List<ShopItem>>(emptyList())

    private val _uploadStatus = mutableStateOf(AuthState())
    val uploadTask = _uploadStatus

    private val _deletingStatus = MutableStateFlow(AuthState())
    val deletingStatus = _deletingStatus

    private val _updatingStatus = MutableStateFlow(AuthState())
    val updating = _updatingStatus

    private val currentUser = firebaseAuth.currentUser?.uid

    lateinit var item : ShopItem
    var intent : String = ""


    init {
        getItems()
    }


    private fun getItems() {
        if (currentUser != null) {
            try {
                viewModelScope.launch {
                    firebaseDatabase.reference.child(USERS)
                        .child(currentUser)
                        .child(TOSHOP)
                        .orderByValue()
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val itemsList = mutableListOf<ShopItem>()
                                for (snap in snapshot.children) {
                                    val shopItem = snap.getValue(ShopItem::class.java)
                                    if (shopItem != null) {
                                        itemsList.add(shopItem)
                                    }
                                    _getItems.value = itemsList

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("database Error", error.message)
                            }
                        })
                }

            } catch (_: Exception) {

            }
        }
    }

    fun addItemToDatabase (item: ShopItem) {
        if (currentUser != null) {
            viewModelScope.launch {


                try {
                    _uploadStatus.value = AuthState(isLoading = true)

                    var imageRef = firebaseStorage.reference.child(IMAGE)
                    imageRef = imageRef.child(System.currentTimeMillis().toString())
                    imageRef.putFile(item.itemImage.toUri()).addOnCompleteListener { task ->
                        try {
                            if (task.isSuccessful) {
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val shopItem = ShopItem(
                                        itemImage = uri.toString(),
                                        itemName = item.itemName,
                                        itemDescription = item.itemDescription,
                                        dateDue = item.dateDue,
                                        dateCreated = item.dateCreated,
                                        price = item.price,
                                        isBought = false,
                                        isDelegated = false,
                                        category = item.category,
                                        shoppingPlace = item.shoppingPlace,
                                    )

                                    firebaseDatabase.reference.child(USERS)
                                        .child(currentUser)
                                        .child(TOSHOP)
                                        .child(item.itemName)
                                        .setValue(shopItem).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                _uploadStatus.value = AuthState(isSuccess = true)
                                            }
                                            else
                                            {
                                                _uploadStatus.value = AuthState(isError = "There was an Error")
                                            }
                                        }

                                }
                            }

                        } catch (e: Exception) {
                            _uploadStatus.value = AuthState(isError = e.message)

                        }
                    }


                } catch (e: Exception) {
                    _uploadStatus.value = AuthState(isError = e.message)
                }

            }

        }
    }

    fun deleteItem (item: ShopItem) = viewModelScope.launch {
        if (currentUser!= null) {
            itemRepo.deleteItem(item).collect{
                result ->
                when(result){
                    is DataState.Error -> {
                        _deletingStatus.value = AuthState(isError = result.message)
                    }
                    is DataState.Loading -> {
                        _deletingStatus.value = AuthState(isLoading = true)
                    }
                    is DataState.Success -> {
                        _deletingStatus.value = AuthState(isSuccess = true)
                    }
                }

            }
        }
        getItems()
    }

    fun updateItem(item: ShopItem) = viewModelScope.launch {
        if (currentUser!= null) {
            itemRepo.updateItem(item).collect{
                result ->
                when(result){
                    is DataState.Error -> {
                        _updatingStatus.value = AuthState(isError = result.message)
                    }
                    is DataState.Loading -> {
                        _updatingStatus.value = AuthState(isLoading = true)

                    }
                    is DataState.Success -> {
                        _updatingStatus.value = AuthState(isSuccess = true)
                    }
                }
            }
        }
    }
    private fun getDelegatedItems() {
        if (currentUser != null) {
            firebaseDatabase.reference.child("users")
                .child(currentUser)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val orderList = mutableListOf<ShopItem>()
                        for (snap in snapshot.children) {
                            val orderItem = snap.getValue(ShopItem::class.java)
                            if (orderItem != null) {
                                if (orderItem.isDelegated) {
                                    orderList.add(orderItem)
                                }

                            }
                            _getDelegatedItems.value = orderList

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("error", error.toString())
                    }

                })
        }
    }
}
