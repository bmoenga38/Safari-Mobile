package com.safari.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.safari.viewmodels.ShopItemsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun dateFormatter(date: Long) : String{

    return SimpleDateFormat("dd-MM-yyyy", Locale.ROOT).format(Date(date))

}

fun numberFormatter(number : Int): String{

    return number
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
}

@Composable
fun totalAmount(shopItemsViewModel: ShopItemsViewModel): Int {
    val list = mutableListOf<Int>()
    val items = shopItemsViewModel.getItems.collectAsState()
    items.value.forEach {
        list.add(it.price.toInt())
    }
    return list.sum()
}

