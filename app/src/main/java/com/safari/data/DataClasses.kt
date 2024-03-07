package com.safari.data

import com.safari.R
import com.safari.utils.ADD
import com.safari.utils.CLOTHES
import com.safari.utils.DRINKS
import com.safari.utils.FOOD

sealed class AuthenticationState<T>(
    val data: T? = null, val message: String? = null
) {

    class Success<T>(data: T) : AuthenticationState<T>(data)
    class Loading<T>(data: T? = null) : AuthenticationState<T>(data)
    class Error<T>(message: String, data: T? = null) :
        AuthenticationState<T>(data, message = message)
}

sealed class DataState<T>(
    val data: T? = null, val message: String? = null
){
    class Success <T>(data: T) : DataState<T> (data)
    class Loading <T>(data: T? = null) : DataState<T>(data)
    class Error <T> (message: String,data: T? = null) : DataState<T>(data, message)
}

data class AuthState(
    val isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    val isError: String? = ""
)

val chips = mapOf(

    Pair(R.drawable.baseline_add_box_24, ADD),
    Pair(R.drawable.baseline_flatware_24, FOOD),
    Pair(R.drawable.baseline_auto_awesome_24, CLOTHES),
    Pair(R.drawable.baseline_local_drink_24, DRINKS )

)
val options = listOf("Food","Clothes","Drinks","Medicine","Others")

data class ShopItem(
    var itemName : String = "",
    var itemDescription : String = "",
    var price : Float = 0f,
    var category : String = "",
    var itemImage : String = "",
    var dateCreated : String = "",
    var dateDue : String = "",
    var shoppingPlace : String = "",
    var isBought : Boolean = false,
    var isDelegated : Boolean = false
)