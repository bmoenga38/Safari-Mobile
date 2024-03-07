package com.safari.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.safari.R
import com.safari.data.ShopItem
import com.safari.navigation.ContentDestinations
import com.safari.ui.theme.OpenSans
import com.safari.utils.dateFormatter
import com.safari.utils.numberFormatter
import com.safari.utils.totalAmount
import com.safari.viewmodels.ShopItemsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ToShopPage(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    shopItemsViewModel: ShopItemsViewModel
) {

    val shoppingItems = shopItemsViewModel.getItems.collectAsState()
    val context = LocalContext.current
    val deleteStatus = shopItemsViewModel.deletingStatus.collectAsState()



    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "This is your shopping List",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontFamily = OpenSans,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = modifier.height(20.dp))

        LazyColumn(modifier.height(600.dp)) {
            items(shoppingItems.value) { shopItem ->

                        ShoppingItem(
                            context = context,
                            shopItemsViewModel = shopItemsViewModel,
                            item = shopItem,
                            navHostController = navHostController
                        )

            }
        }
        Spacer(modifier = modifier.height(10.dp))
        if (deleteStatus.value.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = modifier.height(10.dp))


        AssistChip(
            onClick = {},
            label = {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Total Amount : ")
                        }
                        withStyle(
                            SpanStyle(
                                fontFamily = OpenSans,

                                )
                        ) {
                            append(numberFormatter(totalAmount(shopItemsViewModel = shopItemsViewModel)))
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = OpenSans,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.BottomCenter)
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItem(
    modifier: Modifier = Modifier,
    context: Context,
    item: ShopItem,
    navHostController: NavHostController,
    shopItemsViewModel: ShopItemsViewModel
) {

    val scope = rememberCoroutineScope()
    var dismissShow by remember { mutableStateOf(false) }
    var show by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val deleteStatus = shopItemsViewModel.deletingStatus.collectAsState()
    val updatingStatus = shopItemsViewModel.updating.collectAsState()
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = {
            dismissShow = true
            150f },
    )

        if (dismissState.dismissDirection.equals(SwipeToDismissBoxValue.StartToEnd)) {

            LaunchedEffect(key1 = dismissState.dismissDirection){
                scope.launch {
                    shopItemsViewModel.deleteItem(ShopItem())
                }
            }
        }
        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromEndToStart = true,
            enableDismissFromStartToEnd = true,
            modifier = modifier
                .fillMaxWidth()
                .height(130.dp),
            backgroundContent = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                    tint = Color.Red,
                    contentDescription = ""
                )
            }) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(1.dp)
                    .clickable {
                        show = true
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Row(
                    modifier = modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context = context)
                            .crossfade(true)
                            .placeholder(R.drawable.baseline_broken_image_24)
                            .data(item.itemImage.toUri())
                            .build(),
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .fillMaxHeight()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .width(130.dp),
                        contentDescription = item.itemName,
                        loading = {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.surfaceTint,
                                trackColor = MaterialTheme.colorScheme.surface,
                                strokeCap = StrokeCap.Square,
                                modifier = modifier.size(10.dp)
                            )
                        },
                        error = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_broken_image_24),
                                contentDescription = "Broken image",
                                tint = Color.Gray

                            )

                        }
                    )
                    Column(
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Normal
                                    )
                                ) {
                                    append("Item Name : ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = OpenSans
                                    )
                                ) {
                                    append(item.itemName)
                                }
                            },
                            modifier = modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontFamily = OpenSans,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,

                            )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                    )
                                ) {
                                    append("Price : ")
                                }
                                withStyle(
                                    style = SpanStyle(

                                    )
                                ) {
                                    append(item.price.toString())
                                }
                            },
                            modifier = modifier.padding(2.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            fontFamily = OpenSans

                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,

                                        )
                                ) {
                                    append("Date Created : ")
                                }
                                withStyle(
                                    style = SpanStyle(

                                    )
                                ) {
                                    append(dateFormatter(item.dateCreated.toLong()))
                                }

                            },
                            modifier = modifier.padding(2.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = OpenSans

                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,

                                        )
                                ) {
                                    append("Shopping Date : ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                    )
                                ) {
                                    append(dateFormatter(item.dateDue.toLong()))
                                }

                            },
                            modifier = modifier.padding(2.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = OpenSans

                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,

                                        )
                                ) {
                                    append("From : ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                    )
                                ) {
                                    append(item.shoppingPlace)
                                }

                            },
                            modifier = modifier.padding(2.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = OpenSans

                        )

                    }

                    Column(
                        modifier = modifier.fillMaxHeight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                            modifier = modifier
                                .size(30.dp)
                                .clickable {
                                    shopItemsViewModel.deleteItem(item)
                                    scope.launch {
                                        if (deleteStatus.value.isSuccess) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "${item.itemName} has been deleted Successfully",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }


                                    }


                                },
                            tint = Color.Red,
                            contentDescription = "delete"
                        )


                        AssistChip(onClick = {
                            val shopItem = ShopItem(
                                itemImage = item.itemImage,
                                itemName = item.itemName,
                                itemDescription = item.itemDescription,
                                dateDue = item.dateDue,
                                dateCreated = item.dateCreated,
                                price = item.price,
                                isBought = true,
                                isDelegated = false,
                                category = item.category,
                                shoppingPlace = item.shoppingPlace,
                            )
                            shopItemsViewModel.updateItem(item = shopItem)
                            scope.launch {
                                if (updatingStatus.value.isSuccess) {
                                    Toast.makeText(
                                        context,
                                        "${item.itemName} updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                            trailingIcon = {

                                Icon(
                                    painter = painterResource(
                                        id =
                                        if (item.isBought) {
                                            R.drawable.baseline_check_circle_outline_24
                                        } else {
                                            R.drawable.baseline_block_flipped_24
                                        }
                                    ),
                                    contentDescription = "",
                                    tint = if (item.isBought) {
                                        colorResource(id = R.color.Green)
                                    } else {
                                        Color.Red
                                    },
                                )
                            },
                            label = {
                                Text(
                                    text = "Bought",
                                    fontFamily = OpenSans,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            })


                    }

                }

            }
        }




    if (show) {
        ModalBottomSheet(
            onDismissRequest = { show = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = Color.Black.copy(0.8f),
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = item.itemName,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = OpenSans,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    modifier = modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Spacer(modifier = modifier.height(30.dp))
                Text(
                    text = "Item Price : ${item.price} ",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans,
                )
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    text = "Category : ${item.category} ",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans
                )
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    text = "Where to buy : ${item.shoppingPlace} ",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans
                )
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    text = if (item.isBought) {
                        "The item was bought on : ${dateFormatter(item.dateDue.toLong())}"
                    } else {
                        "The item is scheduled to be bought on : ${dateFormatter(item.dateDue.toLong())}"
                    },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans
                )

                Spacer(modifier = modifier.height(20.dp))

                OutlinedButton(
                    onClick = {
                              show = false
                        shopItemsViewModel.item = item
                        navHostController.navigate(ContentDestinations.Sms.route){
                            launchSingleTop = true
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(
                        text = "Delegate Item",
                        fontFamily = OpenSans,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                }
                OutlinedButton(
                    onClick = {
                        show = false
                        scope.launch {
                            delay(2000)
                            shopItemsViewModel.item = item
                            navHostController.navigate(ContentDestinations.UpdateItem.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = modifier
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(
                        text = "Update ${item.itemName}",
                        fontFamily = OpenSans,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                }
                Spacer(modifier = modifier.height(50.dp))
                Text(
                    text = "Happy Shopping",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontStyle = FontStyle.Italic,
                    fontFamily = OpenSans
                )


            }

        }
    }

}
