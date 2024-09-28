package com.example.taylorswitch.ui.Trade.uiScreen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.taylorswitch.R
import com.example.taylorswitch.data.TradeUiState
import com.example.taylorswitch.data.WindowType
import com.example.taylorswitch.data.rememberWindowSize
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTradeRequest(
    tradeId: String?,
    tradeUiState: TradeUiState,
    tradeViewModel: TradeViewModel
) {
    val windowSize = rememberWindowSize()
    when (windowSize.width){
        WindowType.SMALL -> ReviewTradeRequestPortrait(
            tradeId = tradeId,
            tradeUiState = tradeUiState,
            tradeViewModel = tradeViewModel
        )
        else -> ReviewTradeRequestLandscape(
            tradeId = tradeId,
            tradeUiState = tradeUiState,
            tradeViewModel = tradeViewModel
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTradeRequestPortrait(
    tradeId: String?,
    tradeUiState: TradeUiState,
    tradeViewModel: TradeViewModel
){
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
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(28.dp)),
        topBar = {
            TopAppBar(
                title = { Text("Review Trade", fontWeight = FontWeight.Bold) },
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
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column (){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ){
                    LazyRow (
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        if (tradeUiState.imageRef.size > 0) {
                            items(tradeUiState.imageRef) { imageUrl ->
                                ImageCardTR(imageUrl, modifier = Modifier.fillMaxHeight())
                            }
                        }else{
                            item {
                                Image(
                                    modifier = Modifier
                                        .width(380.dp)
                                        .fillMaxHeight(),
                                    painter = painterResource(R.drawable.image),
                                    contentDescription = "Image Description",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                    Column (
                        modifier = Modifier
                            .matchParentSize()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = tradeUiState.title,
                            style = TextStyle(
                                fontSize = 45.sp,
                                lineHeight = 52.sp,
                                fontWeight = FontWeight(600)
                            )
                        )
                        Text(
                            text = tradeUiState.description,
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight(600),
                                letterSpacing = 0.4.sp
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row() {
                        Image(
                            modifier = Modifier.size(55.dp).padding(10.dp),
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Image Description",
                            contentScale = ContentScale.FillBounds
                        )
                        Text(
                            text = tradeUiState.owner,
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                    Button(
                        onClick = {},
                        enabled = true
                    ) {
                        Text(
                            text = "View Profile",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(500),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.1.sp
                            )
                        )
                    }
                }
            }
            Row (
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
                verticalAlignment = Alignment.Top
            ){
                Text(
                    text = "Trade Item",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(600)
                    )
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (tradeUiState.tradeItem.isNotEmpty()) {
                        items(tradeUiState.tradeItem) { imageUrl ->
                            ImageCardTR(imageUrl, modifier = Modifier.fillMaxHeight())
                        }
                    }else{
                        item {
                            Image(
                                modifier = Modifier
                                    .width(380.dp)
                                    .fillMaxHeight(),
                                painter = painterResource(R.drawable.image),
                                contentDescription = "Image Description",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }
            Column (modifier = Modifier.fillMaxWidth()){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                        .height(65.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if (tradeUiState.isOpen){
                        Button(
                            onClick = { tradeViewModel.callTrade(tradeId = tradeId.toString())},
                            shape = RoundedCornerShape(size = 8.dp),
                            enabled = true
                        ) {
                            Text(
                                text = "Trade",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight(800)
                                )
                            )
                        }
                    }else{
                        Button(
                            onClick = { tradeViewModel.callTrade(tradeId = tradeId.toString())},
                            shape = RoundedCornerShape(size = 8.dp),
                            enabled = false
                        ) {
                            Text(
                                text = "Trade",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight(800)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTradeRequestLandscape(
    tradeId: String?,
    tradeUiState: TradeUiState,
    tradeViewModel: TradeViewModel
){
    val lazyListState = rememberLazyListState()
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
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(28.dp)),
        topBar = {
            TopAppBar(
                title = { Text("Review Trade", fontWeight = FontWeight.Bold) },
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
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Min)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ){
                    LazyRow (
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = lazyListState,
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        flingBehavior = rememberSnapFlingBehavior(lazyListState)
                    ){
                        if (tradeUiState.imageRef.size > 0) {
                            items(tradeUiState.imageRef) { imageUrl ->
                                ImageCardTR(imageUrl, modifier = Modifier.fillMaxHeight())
                            }
                        }else{
                            item {
                                Image(
                                    modifier = Modifier
                                        .width(380.dp)
                                        .fillMaxHeight(),
                                    painter = painterResource(R.drawable.image),
                                    contentDescription = "Image Description",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                    Column (
                        modifier = Modifier
                            .matchParentSize()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = tradeUiState.title,
                            style = TextStyle(
                                fontSize = 45.sp,
                                lineHeight = 52.sp,
                                fontWeight = FontWeight(600)
                            )
                        )
                        Text(
                            text = tradeUiState.description,
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight(600),
                                letterSpacing = 0.4.sp
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            modifier = Modifier.size(55.dp).padding(10.dp),
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Image Description",
                            contentScale = ContentScale.FillBounds
                        )
                        Text(
                            text = tradeUiState.owner,
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(400),
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                    Button(
                        onClick = {},
                        enabled = true
                    ) {
                        Text(
                            text = "View Profile",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight(500),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.1.sp
                            )
                        )
                    }
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Trade Item",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(600)
                    )
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (tradeUiState.tradeItem.isNotEmpty()) {
                        items(tradeUiState.tradeItem) { imageUrl ->
                            ImageCardTR(imageUrl, modifier = Modifier.fillMaxHeight())
                        }
                    }else{
                        item {
                            Image(
                                modifier = Modifier
                                    .width(380.dp)
                                    .fillMaxHeight(),
                                painter = painterResource(R.drawable.image),
                                contentDescription = "Image Description",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }
            Column (modifier = Modifier.fillMaxWidth()){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(49.dp, Alignment.Start),
                    verticalAlignment = Alignment.Top,
                ){
                    if (tradeUiState.isOpen){
                        Button(
                            onClick = { tradeViewModel.rejectTrade(tradeId = tradeId.toString())},
                            shape = RoundedCornerShape(size = 8.dp),
                            enabled = true
                        ) {
                            Text(
                                text = "Reject",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight(800)
                                )
                            )
                        }
                        Button(
                            modifier = Modifier
                                .padding(0.dp)
                                .width(100.dp)
                                .height(44.dp),
                            onClick = { tradeViewModel.acceptTrade(tradeId.toString())},
                            shape = RoundedCornerShape(size = 8.dp),
                            enabled = true
                        ) {
                            Text(
                                text = "Accept",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight(800)
                                )
                            )
                        }
                    }else{
                        Button(
                            onClick = { tradeViewModel.callTrade(tradeId = tradeId.toString())},
                            shape = RoundedCornerShape(size = 8.dp),
                            enabled = false
                        ) {
                            Text(
                                text = "Trade",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight(800)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
//@Composable
//fun ImageCardTR(imageUrl: String, modifier: Modifier) {
//    Card(
//        modifier = Modifier
//            .width(380.dp)
//            .fillMaxHeight(), // Keep height same for square images
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        // Coil to load image from URL
//        Image(
//            painter = rememberImagePainter(data = imageUrl),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}
//@Preview
//@Composable
//fun TradeRequestPreview() {
//    ReviewTradeRequestScreen()
//}