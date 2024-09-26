package com.example.taylorswitch.ui.Auction.UiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.taylorswitch.R
import com.example.taylorswitch.data.BidUiState
import com.example.taylorswitch.data.WindowType
import com.example.taylorswitch.data.rememberWindowSize
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun BidSession(
    auctionId: String?,
    bidUiState: BidUiState,
    bidViewModel: BidViewModel = viewModel()
) {

    var timeRemainingInMillis by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {

        delay(2000L)
        // Calculate the initial time left
        timeRemainingInMillis =
            bidViewModel.calculateTimeLeft(bidViewModel.endDate, bidViewModel.endTime)

        // Continuously update the countdown every second
        while (timeRemainingInMillis > 0) {
            // Update the ViewModel's timeLeft
            bidViewModel.timeLeft = bidViewModel.formatTimeLeft(timeRemainingInMillis)

            // Delay for 1 second before updating
            delay(1000L)

            // Decrease the remaining time by 1 second (1000 milliseconds)
            timeRemainingInMillis -= 1000L
        }

        // When time is up, update the ViewModel to show "Time's up!"
        bidViewModel.timeLeft = "Time's up!"
        bidViewModel.closeListing("$auctionId")
    }

    val windowSize = rememberWindowSize()
    when (windowSize.width) {
        WindowType.SMALL -> BidSessionPortrait(
            auctionId = auctionId,
            bidUiState = bidUiState,
            bidViewModel = bidViewModel
        )

        else -> BidSessionLandscape(
            auctionId = auctionId,
            bidUiState = bidUiState,
            bidViewModel = bidViewModel
        )
    }

}

@Composable
fun BidSessionPortrait(
    bidViewModel: BidViewModel,
    auctionId: String?,
    bidUiState: BidUiState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (bidUiState.imageRef.size > 0) {
                        items(bidUiState.imageRef) { imageUrl ->
                            ImageCardBS(imageUrl, modifier = Modifier.fillMaxHeight())
                        }
                    } else {
                        item {
                            Image(
                                modifier = Modifier
                                    .width(380.dp)
                                    .fillMaxHeight(),

                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "image description",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = bidUiState.title,
                        style = TextStyle(
                            fontSize = 45.sp,
                            lineHeight = 52.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFFFFFFFF),
//                            ${auction?.name}
                        )
                    )
                    Text(
                        text = bidUiState.description,
                        // M3/body/small
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFFFFFFFFF),
                            letterSpacing = 0.4.sp,
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Image(
                        modifier = Modifier
                            .size(55.dp)
                            .padding(10.dp),
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "image description",
                        contentScale = ContentScale.FillBounds
                    )
//                Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = bidUiState.poster,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(400),
                            letterSpacing = 0.5.sp,
                        )
                    )
                }

                Button(
                    onClick = {},
                    enabled = true
                ) {
                    Text(
                        text = "View profile",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(500),
//                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.1.sp,
                        )
                    )
                }

            }

        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bidUiState.startBidAmount.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Starting price",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bidUiState.minCallUp.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Min Call Per Bid",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        text = bidViewModel.timeLeft,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Time left",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = (bidUiState.highestBidder.bidAmount).toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Highest Bid",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                    .height(65.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { bidViewModel.decBidCall() }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "Add Bid Amount"
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .width(80.dp),
                    value = bidViewModel.bidCallAmount,
                    onValueChange = { bidViewModel.updateBidCall(it) },

                    label = {
                        if (bidViewModel.isCallNotValid()) {
                            Text("Error",
                                color = Color(red = 255, green = 0, blue = 0))
                        } else {
                            Text("Bid")
                        }

                    }

                )
                IconButton(onClick = { bidViewModel.incBidCall() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Bid Amount"
                    )
                }

                if(bidUiState.live){
                    Button(
//                    colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C),Color(0xFF2C2C2C),Color(0xFF2C2C2C)),
                        onClick = { bidViewModel.callBid(auctionId = auctionId.toString()) },
                        shape = RoundedCornerShape(size = 8.dp),
                        enabled = true
                    ) {
                        Text(
                            text = "Bid",
                            style = TextStyle(
                                fontSize = 13.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight(800)
                            )
                        )
                    }
                }else{
                    Button(
                        onClick = {
                            bidViewModel.callBid(auctionId = auctionId.toString())
                                  },
                        shape = RoundedCornerShape(size = 8.dp),
                        enabled = false
                    ) {
                        Text(
                            text = "Bid",
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


@Composable
fun BidSessionLandscape(
    bidViewModel: BidViewModel,
    auctionId: String?,
    bidUiState: BidUiState
) {
    val lazyListState = rememberLazyListState()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .height(IntrinsicSize.Min)
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = lazyListState,  // Pass the list state
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    flingBehavior = rememberSnapFlingBehavior(lazyListState)
                ) {
                    if (bidUiState.imageRef.size > 0) {
                        items(bidUiState.imageRef) { imageUrl ->
                            ImageCardBS(imageUrl, modifier = Modifier.fillMaxHeight())
                        }
                    } else {
                        item {
                            Image(
                                modifier = Modifier
                                    .width(380.dp)
                                    .fillMaxHeight(),

                                painter = painterResource(id = R.drawable.image),
                                contentDescription = "image description",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }

//
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = bidUiState.title,
                        style = TextStyle(
                            fontSize = 45.sp,
                            lineHeight = 52.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFFFFFFFF),
//                            ${auction?.name}
                        )
                    )
                    Text(
                        text = bidUiState.description,
                        // M3/body/small
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFFFFFFFFF),
                            letterSpacing = 0.4.sp,
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .size(55.dp)
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
//                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = bidUiState.poster,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontWeight = FontWeight(400),
//                        color = Color(0xFF1D1B20),
                        letterSpacing = 0.5.sp,
                    )
                )



                Button(
                    onClick = {},
                    enabled = true
                ) {
                    Text(
                        text = "View profile",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(500),
//                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.1.sp,
                        )
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bidUiState.startBidAmount.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Starting price",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bidUiState.minCallUp.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(600),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Min Call Per Bid",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        text = bidViewModel.timeLeft,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Time left",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bidUiState.highestBid.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Highest Bid",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
                            fontWeight = FontWeight(400),
//                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                    .height(65.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { bidViewModel.decBidCall() }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "Add Bid Amount"
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .width(80.dp),
                    value = bidViewModel.bidCallAmount,
                    onValueChange = { bidViewModel.updateBidCall(it) },
                    label = {
                        if (bidViewModel.isCallNotValid()) {
                            Text("Error",
                            color = Color(red = 255, green = 0, blue = 0))
                        } else {
                            Text("Bid")
                        }

                    }


                )
                IconButton(onClick = { bidViewModel.incBidCall() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Bid Amount"
                    )
                }




                Button(
//                    colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C),Color(0xFF2C2C2C),Color(0xFF2C2C2C)),
                    onClick = { bidViewModel.callBid(auctionId = auctionId.toString()) },
                    shape = RoundedCornerShape(size = 8.dp),
                    enabled = true
                ) {
                    Text(
//                        modifier = Modifier.width(100.dp),
                        text = auctionId.toString(),
                        style = TextStyle(
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(800),
//                            color = Color(0xFFF5F5F5)
                        )
                    )
                }


            }


        }
    }
}


@Composable
fun ImageCardBS(imageUrl: String, modifier: Modifier) {
    Card(
        modifier = Modifier
            .width(380.dp)
            .fillMaxHeight(), // Keep height same for square images
        shape = RoundedCornerShape(8.dp)
    ) {
        // Coil to load image from URL
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewBidSession() {
//    BidSession()
}