package com.example.taylorswitch.ui.Auction.UiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.taylorswitch.R
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.data.WindowType
import com.example.taylorswitch.data.historyRec
import com.example.taylorswitch.data.rememberWindowSize
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

@Composable
fun PostHistoryScreen(bidViewModel: BidViewModel, list: List<historyRec> = emptyList(), navController: NavHostController) {

//    LaunchedEffect(Unit) {
//        bidViewModel.getUserHistoryArray( "userPost", "postRef")
//    }

    val windowSize = rememberWindowSize()
    when (windowSize.width) {
        WindowType.SMALL ->
            if(!list.isEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list) { postRec ->
                        ListPostingPortrait(
                            title = postRec.name,
                            highestBid = postRec.highestBid.toString(),
                            endDate = postRec.endDate,
                            endTime = postRec.endTime,
                            onClickStartSource = {
                                navController.navigate(TaylorSwitchScreen.ViewBid.name + "/${postRec.id.toInt()}")
                                bidViewModel.getAuctionById((postRec.id.toInt()).toString())
                            },
                            live = postRec.live
                        )

                    }
                }
            }

        else ->
            if(!list.isEmpty()){
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list) { postRec ->
                        if(!postRec.imageRef.isEmpty()) {
                            ListPostingLandscape(
                                imageRef = postRec.imageRef.get(0),
                                title = postRec.name,
                                highestBid = postRec.highestBid.toString(),
                                endDate = postRec.endDate,
                                endTime = postRec.endTime,
                                onClickStartSource = {
                                    navController.navigate(TaylorSwitchScreen.ViewBid.name + "/${postRec.id.toInt()}")
                                    bidViewModel.getAuctionById((postRec.id.toInt()).toString())
                                },
                                live = postRec.live
                            )
                        }

                    }
                }
            }


    }

}



@Composable
private fun ListPostingPortrait(
    title: String, highestBid: String, endDate: String, endTime: String,onClickStartSource: () -> Unit, live: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row() {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(text = stringResource(id = R.string.title, title))
                Text(text = stringResource(id = R.string.highest, highestBid.toDoubleOrNull()?:0.00))
                Text(text = stringResource(id = R.string.endDate, endDate))
                Text(text = stringResource(id = R.string.endTime, endTime))
            }
        }

        if(live){
            Button(
                onClick = onClickStartSource, enabled = true, modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {

                Text(text = stringResource(id = R.string.live))
            }
        }else{
            Button(
                onClick = onClickStartSource, enabled = false, modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {

                Text(text = stringResource(id = R.string.end))
            }
        }

    }
    HorizontalDivider(thickness = 1.dp)
}


@Composable
private fun ListPostingLandscape(
    imageRef: String = "", title: String, highestBid: String, endDate: String, endTime: String,onClickStartSource: () -> Unit, live: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row() {
            if(imageRef != ""){
                Image(modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                    contentScale = ContentScale.Crop,
                    painter = rememberImagePainter(data = imageRef),
                    contentDescription = "Thumbnail")
            }else{
                Image(modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "image description"
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(text = stringResource(id = R.string.title, title))
                Text(text = stringResource(id = R.string.highest, highestBid.toDoubleOrNull()?:0.00))
                Text(text = stringResource(id = R.string.endDate, endDate))
                Text(text = stringResource(id = R.string.endTime, endTime))

//                Text(text = stringResource(id = R.string.timeLeft, timeLeft))
            }
        }

        if(live){
            Button(
                onClick = onClickStartSource, enabled = true, modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {

                Text(text = stringResource(id = R.string.live))
            }
        }else{
            Button(
                onClick = onClickStartSource, enabled = false, modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {

                Text(text = stringResource(id = R.string.end))
            }
        }

    }
    HorizontalDivider(thickness = 1.dp)
}

