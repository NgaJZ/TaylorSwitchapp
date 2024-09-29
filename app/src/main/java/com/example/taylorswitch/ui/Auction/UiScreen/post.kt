@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.taylorswitch.ui.Auction.UiScreen

import android.icu.text.SimpleDateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.taylorswitch.data.WindowType
import com.example.taylorswitch.data.rememberWindowSize
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
    onPostButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {

//    bidViewModel.resetVM()
    val postUiState by bidViewModel.postUiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
    val formatter = remember { java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()) }


    var showMenu by remember { mutableStateOf(true) }
    var showDialExample by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }

//    var imageUris by remember{
//        mutableStateOf<List<Uri>>(emptyList())
//    }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            bidViewModel.updateImage(it)
        }
    )

    val windowSize = rememberWindowSize()
    when (windowSize.width) {
        WindowType.SMALL ->    Column(
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
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(postUiState.imageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .width(149.dp)
                                .height(194.dp),
                            contentScale = ContentScale.Crop
                            // You can adjust the size as needed
                        )
                    }
                    item{
                        Card(
                            modifier = Modifier
                                .width(149.dp)
                                .height(194.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            IconButton(
                                modifier = Modifier
                                    .width(149.dp)
                                    .height(194.dp),
                                onClick = {
                                    multiplePhotoPicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Image"
                                )
                            }
                        }

                    }
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(10.dp)
                    .width(408.dp)
                    .height(60.dp),
                value = postUiState.title,
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
                value = postUiState.description,
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
                    value = postUiState.startBidInput,
                    onValueChange = { bidViewModel.updateStartBidAmount(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    label = { Text("Start Bid") },
                )
                OutlinedTextField(
                    modifier = Modifier
                        .alpha(0.5f)
                        .padding(10.dp)
                        .height(60.dp)
                        .width(160.dp),
                    value = postUiState.minBidInput,
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
                    value = postUiState.endDate,
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
                    value = postUiState.endTime,
                    onValueChange = {},
                    label = { Text("End Time") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = !showTimePicker }) {
                            Icon(
                                imageVector = Icons.Filled.AccessTime,
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
                    onClick = onCancelButtonClicked,
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


        else ->
            Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

            ) {

            // Image
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(postUiState.imageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .width(149.dp)
                                .height(194.dp),
                            contentScale = ContentScale.Crop
                            // You can adjust the size as needed
                        )
                    }
                    item{
                        IconButton(
                            modifier = Modifier
                                .width(149.dp)
                                .height(194.dp),
                           onClick = {
                                multiplePhotoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Image"
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ){
                Column(
                    modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                ){
                    OutlinedTextField(
                        modifier = Modifier
                            .alpha(0.5f)
                            .padding(10.dp)
                            .width(408.dp)
                            .height(60.dp),
                        value = postUiState.title,
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
                        value = postUiState.description,
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
                }
                Column(
                    modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                ){
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
                            value = postUiState.startBidInput,
                            onValueChange = { bidViewModel.updateStartBidAmount(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            label = { Text("Start Bid") },
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .alpha(0.5f)
                                .padding(10.dp)
                                .height(60.dp)
                                .width(160.dp),
                            value = postUiState.minBidInput,
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
                            value = postUiState.endDate,
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
                                        modifier = Modifier.verticalScroll(rememberScrollState()),
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
                            value = postUiState.endTime,
                            onValueChange = {},
                            label = { Text("End Time") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showTimePicker = !showTimePicker }) {
                                    Icon(
                                        imageVector = Icons.Filled.AccessTime,
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

                                    AdvancedTimePickerExample(
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
                }
            }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                ) {
                    Button(
                        modifier = Modifier
                            .padding(0.dp)
                            .width(100.dp)
                            .height(44.dp),
                        onClick = onCancelButtonClicked,
                        enabled = true
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(50.dp))
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

}



@Composable
fun ImageCard(imageUrl: String) {
    Card(
        modifier = Modifier
            .width(149.dp)
            .height(194.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Coil to load image from URL
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = null,
//            contentScale = ContentScale.Crop,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
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


@OptIn(ExperimentalMaterial3Api::class)
// [START android_compose_components_advanced]
@Composable
fun AdvancedTimePickerExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    /** Determines whether the time picker is dial or input */
    var showDial by remember { mutableStateOf(true) }

    /** The icon used for the icon button that switches from dial to input */
    val toggleIcon = if (showDial) {
        Icons.Filled.EditCalendar
    } else {
        Icons.Filled.AccessTime
    }

    AdvancedTimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) },

    ) {
        if (showDial) {
            TimePicker(
                state = timePickerState,
            )
        } else {
            TimeInput(
                state = timePickerState,
            )
        }
    }
}

@Composable
fun AdvancedTimePickerDialog(
    title: String = "Select Time",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier =
            Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
    }
}