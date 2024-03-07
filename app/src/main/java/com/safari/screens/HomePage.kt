package com.safari.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.safari.R
import com.safari.data.ShopItem
import com.safari.data.chips
import com.safari.navigation.ContentDestinations
import com.safari.ui.theme.OpenSans
import com.safari.utils.ADD
import com.safari.utils.CLOTHES
import com.safari.utils.DRINKS
import com.safari.utils.FOOD
import com.safari.viewmodels.ShopItemsViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.abs


@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    shopItemsViewModel: ShopItemsViewModel

) {

val items = shopItemsViewModel.getItems.collectAsState()
val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(230.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "Shopping made easy...",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 10.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontFamily = OpenSans,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Image(painter = painterResource(id = R.drawable.photo3),
                modifier = modifier.fillMaxSize(1f),
                contentScale = ContentScale.Crop,
                contentDescription = "")



        }

        Text(
            text = "Features",
            modifier = modifier.padding(4.dp),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontFamily = OpenSans
        )

        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(3.dp)
        ) {
            items(chips.toList()) { item->

                ChipMaker(icon = item.first, onclick = {

                    when (item.second) {
                        ADD -> {
                            navHostController.navigate(ContentDestinations.AddItem.route){
                                launchSingleTop = true
                            }
                        }

                        FOOD -> {

                        }

                        CLOTHES -> {

                        }

                        DRINKS -> {

                        }
                    }

                }, name = item.second)

            }

        }
        Spacer(modifier = modifier.height(10.dp))

        Text(
            text = "Recent Items",
            modifier = modifier.padding(4.dp),
            fontFamily = OpenSans,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        LazyColumn(
            content = {
                items(items.value) { item ->
                    if (item.isBought){
                        RecentTransactions(context = context , item = item)
                    }
                }
            }, modifier = modifier.height(500.dp)
        )

    }
}

@Composable
fun TextField1(
    modifier: Modifier = Modifier, text: String, purpose: String, onValueChanged: (String) -> Unit
) {

    OutlinedTextField(
        value = text,
        onValueChange = onValueChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20),
        label = {
            Text(text = purpose, fontFamily = OpenSans)
        },
        textStyle = TextStyle(
            fontFamily =  OpenSans,
            textAlign = TextAlign.Start

        ),

        placeholder = {
            Text(text = purpose, fontFamily = OpenSans)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.onPrimary
        )
    )

}

@Composable
fun ChipMaker(modifier: Modifier = Modifier, icon: Int, onclick: () -> Unit, name: String) {
    AssistChip(onClick = { onclick() },
        modifier = modifier.padding(2.dp),

        label = {
            Text(
                text = name,
                fontFamily = OpenSans,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                modifier = modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        })

}

@Composable
fun RecentTransactions(context : Context,modifier: Modifier = Modifier, item: ShopItem) {

    val date1 = item.dateDue.toLong()
    val date2 =Calendar.getInstance().timeInMillis
    val duration = abs(date2 - date1)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        )

        )
    {
        Row(
             modifier = modifier.fillMaxWidth()
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
                    .padding(end = 40.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .width(100.dp),
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
                    .fillMaxWidth(0.6f)
                    .padding(3.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.itemName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans
                )
                Text(
                    text = " Category : ${item.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    softWrap = true,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans
                )

            }

            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f)
                    .padding(end = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.price.toString(),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Text(
                    text = "${TimeUnit.MILLISECONDS.toDays(duration)} days ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontFamily = OpenSans
                )


            }
        }


    }
}


/*@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ShowPrev() {
    val navHostController = rememberNavController()
    HomePage(navHostController = navHostController)
}*/
