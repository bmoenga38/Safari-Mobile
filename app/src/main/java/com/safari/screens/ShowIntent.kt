package com.safari.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.safari.R
import com.safari.data.options
import com.safari.navigation.RootGraph
import com.safari.ui.theme.OpenSans
import com.safari.utils.dateFormatter
import com.safari.viewmodels.ShopItemsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowIntent(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    shopItemsViewModel: ShopItemsViewModel
) {
    val context = LocalContext.current

    shopItemsViewModel.uploadTask.value

    var name: String by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf(shopItemsViewModel.intent)
    }
    var price by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()


    var isExpanded by remember {
        mutableStateOf(false)
    }
    var category by remember {
        mutableStateOf("")
    }
    var shoppingPlace by remember {
        mutableStateOf("")
    }

    /*********************************/
    val calendar = Calendar.getInstance()
    val calendarState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    var openDialog by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableLongStateOf(calendar.timeInMillis)
    }
    /*********************************/

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text(text = "From other Sources",
                            modifier = modifier.padding(start = 10.dp),
                            fontFamily = OpenSans,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                },
                navigationIcon = {

                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "back arrow",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = modifier
                            .clickable {
                                navHostController.navigate(RootGraph.Content.route) {
                                    launchSingleTop = true
                                }
                            }
                            .padding(start = 20.dp, end = 20.dp)
                            .size(20.dp)
                            .background(shape = CircleShape, color = Color.Transparent)
                    )


                },
            )
        }
    ) {
        paddingValues ->
        Box(modifier = modifier.fillMaxSize()
            .padding(paddingValues = paddingValues)) {
            Column(
                modifier = modifier
                    .fillMaxSize(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField1(text = name, purpose = "Item name", onValueChanged = { name = it })

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(20),
                    readOnly = true,
                    label = {
                        Text(text = "Url", fontFamily = OpenSans)
                    },
                    textStyle = TextStyle(
                        fontFamily = OpenSans,
                        textAlign = TextAlign.Start

                    ),
                    minLines = 3,
                    maxLines = 6,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )


                TextField1(text = price, purpose = "Price", onValueChanged = { price = it })
                TextField1(
                    text = shoppingPlace,
                    purpose = "Place to shop",
                    onValueChanged = { shoppingPlace = it })

                Spacer(modifier = modifier.height(10.dp))

                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Choose due date: ", fontFamily = OpenSans)
                    Text(text = dateFormatter(selectedDate), fontFamily = OpenSans)
                    Icon(painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = "calendar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = modifier.clickable {
                            openDialog = true
                        })

                }

                HorizontalDivider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                Spacer(modifier = modifier.height(5.dp))


                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {
                        isExpanded = !isExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = category,
                        modifier = modifier.menuAnchor(),
                        readOnly = true,
                        label = { Text(text = "Choose Category") },

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTrailingIconColor = Color.Gray

                        ),
                        textStyle = TextStyle(
                            fontFamily = OpenSans,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        onValueChange = { category = it })
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {
                        options.forEach { selectedOptions ->
                            DropdownMenuItem(
                                text = { Text(text = selectedOptions, fontFamily = OpenSans) },
                                onClick = {
                                    category = selectedOptions
                                    isExpanded = false

                                })
                        }


                    }


                }


                if (openDialog) {
                    DatePickerDialog(
                        onDismissRequest = { openDialog = false },
                        colors = DatePickerDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            todayContentColor = MaterialTheme.colorScheme.onPrimary,
                            todayDateBorderColor = Color.Red,


                            ),
                        properties = DialogProperties(),
                        tonalElevation = 7.dp,
                        dismissButton = {
                            TextButton(
                                onClick = { openDialog = false })
                            {
                                Text(
                                    text = "Cancel",
                                    fontFamily = OpenSans,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(

                                onClick = {
                                    selectedDate = calendarState.selectedDateMillis!!
                                    openDialog = false
                                })
                            {
                                Text(
                                    text = "Confirm",
                                    fontFamily = OpenSans,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }) {
                        DatePicker(
                            state = calendarState,

                            )

                    }
                }

                Spacer(modifier = modifier.height(10.dp))
                /*OutlinedButton(onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }) {
                    Text(
                        text = "Pick Image",
                        fontFamily = OpenSans,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }*/
                Spacer(modifier = modifier.height(3.dp))

            }

            OutlinedButton(
                onClick = {
                   /* val item = ShopItem(
                        itemImage = "KJKDJSKDJKSD",
                        itemName = name,
                        itemDescription = description,
                        dateDue = selectedDate.toString(),
                        dateCreated = calendar.timeInMillis.toString(),
                        price = price.toFloat(),
                        isBought = false,
                        isDelegated = false,
                        category = category,
                        shoppingPlace = shoppingPlace,
                    )*/

                    scope.launch {

                        delay(2000)
                        Toast.makeText(
                            context,
                            "uploaded Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "Upload",
                    fontFamily = OpenSans,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }
        }
    }
}

