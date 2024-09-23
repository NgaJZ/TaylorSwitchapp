@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taylorswitch.ui.Auction.UiScreen

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taylorswitch.R
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

//@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
//    title:String = "",
//    onTitleChanged:(String) -> Unit,
//    description: String = "",
//    onDescriptionChanged:(String) -> Unit,
//    startBidAmount: String = "",
//    onStartBidAmountChanged: (String) -> Unit,
//    minBidAmount: String = "",
//    onMinBidChanged: (String) -> Unit,

    bidViewModel: BidViewModel = viewModel(),
    onPostButtonClicked: () -> Unit
) {

    val bidUiState by bidViewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()

    val formatter = remember { java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()) }


    var showMenu by remember { mutableStateOf(true) }
    var showDialExample by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
//    val formatter = remember { java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,

        ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {
            Image(
                modifier = Modifier
                    .width(149.dp)
                    .height(194.dp),
                painter = painterResource(id = R.drawable.image),
                contentDescription = "image description",
                contentScale = ContentScale.FillBounds
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .alpha(0.5f)
                .padding(10.dp)
                .width(408.dp)
                .height(60.dp),
            value = bidViewModel.name,
            onValueChange = { bidViewModel.updateListingTitle(it) },
            label = { Text("Listing Title") },
            trailingIcon = {
                IconButton(onClick = { bidViewModel.updateListingTitle("") }) {
                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = "Clear listing title" // Add a valid content description
                    )
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .alpha(0.5f)
                .padding(10.dp)
                .width(408.dp)
                .height(152.dp),
            value = bidViewModel.description,
            onValueChange = { bidViewModel.updateListingDescription(it) },
            label = { Text("Description") },
            trailingIcon = {
                IconButton(
                    onClick = { bidViewModel.updateListingDescription("") }) {
                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = "" // Add a valid content description
                    )
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(10.dp)
                    .height(60.dp)
                    .width(160.dp),
                value = bidViewModel.startAmount,
                onValueChange = { bidViewModel.updateStartBidAmount(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("Start Bid") },
            )
            OutlinedTextField(
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(10.dp)
                    .height(60.dp)
                    .width(160.dp),
                value = bidViewModel.minAmount,
                onValueChange = { bidViewModel.updateMinBidAmount(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text("Min Bid") }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = bidViewModel.endDate,
                onValueChange = {},
                label = { Text("End Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)

            )
            if (showDatePicker) {
                Popup(
                    onDismissRequest = { showDatePicker = false },
                    alignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
//                                .offset(y = 64.dp)
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = !showDatePicker },
                            confirmButton = {
                                TextButton(onClick = {
                                    val selectedDate = datePickerState.selectedDateMillis?.let {
                                        convertMillisToDate(it)
                                    } ?: ""
                                    bidViewModel.updateEndDate(selectedDate)
                                    showDatePicker = !showDatePicker
                                }) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = !showDatePicker }) {
                                    Text("Cancel")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = bidViewModel.endTime,
                onValueChange = {},
                label = { Text("End Time") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = !showTimePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Time"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)

            )
            if (showTimePicker) {
                Popup(
                    onDismissRequest = { showTimePicker = false },
                    alignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
//                                .offset(y = 64.dp)
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {

                        DialWithDialogExample(
                            onDismiss = {
                                showTimePicker = !showTimePicker
                            },
                            onConfirm = { time ->
                                selectedTime = time
                                val cal = Calendar.getInstance()
                                cal.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                                cal.set(Calendar.MINUTE, selectedTime!!.minute)
                                cal.isLenient = false
                                bidViewModel.updateEndTime(formatter.format(cal.time))
                                showTimePicker = !showTimePicker
                            },
                        )
                    }

                }
            }

        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(49.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {
            Button(
                modifier = Modifier
                    .padding(0.dp)
                    .width(100.dp)
                    .height(44.dp),
                onClick =
//                    onPostButtonClicked
                {
                    bidViewModel.resetPosting()
                },
                enabled = true
            ) {
                Text("Cancel")
            }
            Button(
                modifier = Modifier
                    .padding(0.dp)
                    .width(220.dp)
                    .height(44.dp),
                onClick = onPostButtonClicked,
                enabled = true
            ) {
                Text("Post")
            }

        }

    }
}



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun DialWithDialogExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}