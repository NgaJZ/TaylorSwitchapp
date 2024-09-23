package com.example.taylorswitch.ui.Auction.UiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
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
import com.example.taylorswitch.R
import com.example.taylorswitch.data.BidUiState
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel
import kotlinx.coroutines.delay

@Composable
fun BidSession(
    auctionId: String?,
    bidUiState: BidUiState,
    bidViewModel: BidViewModel = viewModel()
){
//    bidViewModel.timeLeft = "Time's up!"
    bidViewModel.getAuctionById("$auctionId")
    val auction by bidViewModel.auction.collectAsStateWithLifecycle()

//    val resources = LocalContext.current.resources
    var timeRemainingInMillis by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        delay(2000L)
        // Calculate the initial time left
        timeRemainingInMillis = bidViewModel.calculateTimeLeft(bidViewModel.endDate, bidViewModel.endTime)

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

//    LaunchedEffect(Unit) {
//        // Calculate the initial time left
//        timeRemainingInMillis = bidViewModel.calculateTimeLeft(bidUiState.endDate, bidUiState.endTime)
//
//        // Continuously update the countdown every second
//        while (timeRemainingInMillis > 0) {
//            // Update the ViewModel's timeLeft
//            bidViewModel.timeLeft = bidViewModel.formatTimeLeft(timeRemainingInMillis)
//
//            // Delay for 1 second before updating
//            delay(1000L)
//
//            // Decrease the remaining time by 1 second (1000 milliseconds)
//            timeRemainingInMillis -= 1000L
//        }
//
//        // When time is up, update the ViewModel to show "Time's up!"
//        bidViewModel.timeLeft = "Time's up!"
////        bidViewModel.closeListing("$auctionId")
//    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(350.dp)
            ) {
                Image(
                    modifier = Modifier
//                        .size(120.dp)
                        .fillMaxSize(),

                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "${auction?.name}",
                        style = TextStyle(
                            fontSize = 45.sp,
                            lineHeight = 52.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF),
//                            ${auction?.name}
                            )
                    )
                    Text(
                        text = "${auction?.description}",
                        // M3/body/small
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFFF),
                            letterSpacing = 0.4.sp,
                        )
                    )
                }
            }
//            Row(
//                modifier = Modifier.fillMaxWidth()
//                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
//                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
//                verticalAlignment = Alignment.Top,
//            ) {
//                Image(
//                    modifier = Modifier
//                        .width(79.dp)
//                        .height(86.dp),
//                    painter = painterResource(id = R.drawable.image),
//                    contentDescription = "image description",
//                    contentScale = ContentScale.FillBounds
//                )
//            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(){
                    Image(
                        modifier = Modifier.size(55.dp)
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
                            color = Color(0xFF1D1B20),
                            letterSpacing = 0.5.sp,
                        )
                    )
                }


                Button(
//                    modifier = Modifier.background(
//
//                        shape = RoundedCornerShape(size = 100.dp)
//                    ),
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
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.1.sp,
                        )
                    )
                }

            }

        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${auction?.basePrice}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF322F35),
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
                            color = Color(0xFF322F35),
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
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF322F35),
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
                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 10.dp),
                        text = bidViewModel.timeLeft,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF322F35),
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
                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Column(modifier = Modifier.width(150.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = bidUiState.highestBidder.bidAmount.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )

                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Highest Bid",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 19.6.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF322F35),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
                    .height(65.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                 IconButton(onClick = {bidViewModel.decBidCall()}) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "Add Bid Amount"
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
//                        .border(width = 1.dp, color = Color(0xFFD8DADC), shape = RoundedCornerShape(size = 10.dp))
//                    .alpha(0.5f)
                    .width(80.dp)
                    ,
                    value = bidViewModel.bidCallAmount,
                    onValueChange = {bidViewModel.updateBidCall(it)},
                    label = {
                        if(bidViewModel.isCallNotValid()){
                            Text("Error")
                        }else{
                            Text("Bid")
                        }

                        }


                )
                IconButton(onClick = { bidViewModel.incBidCall()}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Bid Amount"
                    )
                }

                Button(
//                    colors = ButtonColors(Color(0xFF2C2C2C), Color(0xFF2C2C2C),Color(0xFF2C2C2C),Color(0xFF2C2C2C)),
                    onClick = {bidViewModel.callBid(auctionId = "${auction?.id}")},
                  shape = RoundedCornerShape(size = 8.dp),
                    enabled = true){
                    Text(
//                        modifier = Modifier.width(100.dp),
                        text = "${auction?.id}",
                        style = TextStyle(
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(800),
                            color = Color(0xFFF5F5F5)
                        )
                    )
                }


            }
        }

//Spacer(modifier = Modifier.height(5.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewBidSession(){
//    BidSession()
}