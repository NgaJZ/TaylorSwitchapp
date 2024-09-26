package com.example.taylorswitch.ui.Trade.uiScreen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTradeItemScreen(
    tradeViewModel: TradeViewModel = viewModel(),
    onPostButtonClicked: () -> Unit
){
    LaunchedEffect(Unit) {
        tradeViewModel.resetUiState()
    }
    val tradeUiState by tradeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    val categories = listOf("Category 1", "Category 2", "Category 3")

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { tradeViewModel.imageUris = it }
    )
    Scaffold(
        modifier = Modifier
            .border(width = 8.dp, color = Color(0xFFCAC4D0))
            .padding(8.dp)
            .width(412.dp)
            .height(826.dp)
            .background(color = Color(0xFFFFFFFF)),
        topBar = {
            TopAppBar(
                title = {Text(text = "Post")},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "navigate back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ){innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){ Row (
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
            verticalAlignment = Alignment.Top
        ){
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tradeViewModel.imageUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .width(149.dp)
                            .height(194.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Card (
                        modifier = Modifier
                            .width(149.dp)
                            .width(194.dp),
                        shape = RoundedCornerShape(8.dp)
                    ){
                        IconButton(
                            modifier = Modifier
                                .width(149.dp)
                                .height(194.dp),
                            onClick = {
                                multiplePhotoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Image"
                            )
                        }
                    }
                }
            }
        }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                OutlinedTextField(
                    value = tradeUiState.title,
                    onValueChange = {tradeViewModel.updateListingTitle(it)},
                    label = {Text("Title")},
                    modifier = Modifier
                        .alpha(0.5f)
                        .padding(0.dp)
                        .width(400.dp)
                        .height(56.dp)
                        .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 4.dp)),
                    trailingIcon = {
                        IconButton(onClick = {tradeViewModel.updateListingTitle("")}){
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "Clear Title"
                            )
                        }
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                OutlinedTextField(
                    value = tradeUiState.description,
                    onValueChange = {tradeViewModel.updateListingDescription(it)},
                    label = {Text("Add Description")},
                    trailingIcon = {
                        IconButton(
                            onClick = { tradeViewModel.updateListingDescription("") }) {
                            Icon(
                                Icons.Outlined.Clear,
                                contentDescription = "" // Add a valid content description
                            )
                        }
                    },
                    modifier = Modifier
                        .alpha(0.5f)
                        .padding(0.dp)
                        .width(408.dp)
                        .height(152.dp)
                        .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 4.dp))
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = tradeUiState.category,
                    onValueChange = { tradeViewModel.updateCategory(it) },
                    label = { Text("Category") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {showCategoryDropdown = !showCategoryDropdown}) {
                            Icon(
                                imageVector = if (showCategoryDropdown)
                                    Icons.Default.KeyboardArrowDown
                                else
                                    Icons.Default.KeyboardArrowUp,
                                contentDescription = "Dropdown arrow"
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = showCategoryDropdown,
                    onDismissRequest = { showCategoryDropdown = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                tradeViewModel.updateCategory(item)
                                showCategoryDropdown = false
                            },
                            text = { Text(text = item) }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onPostButtonClicked
                        run {
                            tradeViewModel.resetPosting()
                        }
                    },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    modifier = Modifier
                        .padding(8.dp)
                        .width(120.dp)
                        .height(44.dp)
                        .background(color = Color(0xFF8E8E93), shape = RoundedCornerShape(size = 100.dp))
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onPostButtonClicked,
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier
                        .padding(8.dp)
                        .width(220.dp)
                        .height(44.dp)
                        .background(color = Color.Black, shape = RoundedCornerShape(size = 100.dp))
                ) {
                    Text("Post")
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PostTradeItemScreenPreview(){
//    PostTradeItemScreen()
//}