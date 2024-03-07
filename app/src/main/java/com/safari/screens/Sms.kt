package com.safari.screens


import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.safari.R
import com.safari.data.ShopItem


@Composable
fun ContactForm(
    modifier: Modifier = Modifier,
    item: ShopItem
) {


    val context = LocalContext.current
    var phone by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }


        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = context.let {
                    ImageRequest.Builder(it)
                        .data(item.itemImage.toUri())
                        .crossfade(true)
                        .build()
                },
                contentDescription = "",
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

                },

                modifier = modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(7)),
                contentScale = ContentScale.Crop
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(text = "phone ") },
                shape = RoundedCornerShape(20),
                modifier = modifier.fillMaxWidth()
                    .padding(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceTint,
                    focusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
                ),
            )
            Spacer(modifier = modifier.height(20.dp))


            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(text = "Message ") },

                placeholder = {
                    item.itemName
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceTint,
                    focusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
                ),
                modifier = modifier.fillMaxWidth()
                    .padding(5.dp),
                shape = RoundedCornerShape(20),
                minLines = 4,
                maxLines = 7

            )
            Spacer(modifier = modifier.height(20.dp))
            OutlinedButton(
                onClick = {
                    sendMessage(phone ,item, message,context)
                },
                enabled = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(

                )
            ) {
                Text(text = "Send",color = MaterialTheme.colorScheme.onPrimary)
            }


        }

    }


fun sendMessage(phone : String,item: ShopItem, text: String, context: Context) {
    val smsManager = SmsManager.getDefault()
    try {
        smsManager.sendTextMessage(
            phone, null,
            "$text  ${item.itemName}", null, null
        )

        Toast.makeText(context, "sent Successfully", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error sending ${e.message.toString()}", Toast.LENGTH_LONG).show()

    }


}

