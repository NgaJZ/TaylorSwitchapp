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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.taylorswitch.R
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.data.historyRec
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

@Composable
fun BidHistoryScreen(bidViewModel: BidViewModel, list: List<historyRec> = emptyList(), navController: NavHostController) {

    LaunchedEffect(Unit) {
        bidViewModel.getUserHistoryArray("0", "userBid","bidRef")
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) { historyRec ->
            ListItem(
                imageRef = historyRec.imageRef.get(0),
                id = historyRec.id.toString(),
                title = historyRec.name,
                highestBid = historyRec.highestBid.toString(),
                endDate = historyRec.endDate,
                endTime = historyRec.endTime,
                onClickStartSource = {
                    navController.navigate(TaylorSwitchScreen.ViewBid.name+"/${historyRec.id.toInt()}")
                    bidViewModel.getAuctionById((historyRec.id.toInt()).toString())
                },
                live = historyRec.live,
                isHighest = bidViewModel.checkHighestOrNot(user = "test", auctionId = historyRec.id.toString())
            )

        }
    }
}

@Composable
fun ListItem(
//    @DrawableRes image: Int,
    imageRef: String = "",id: String = "", title: String, highestBid: String, endDate: String, endTime: String,onClickStartSource: () -> Unit, live: Boolean = false, isHighest: Boolean = false
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
                Text(text = stringResource(id = R.string.highest, highestBid))
                Text(text = stringResource(id = R.string.endDate, endDate))
                Text(text = stringResource(id = R.string.endTime, endTime))

            }
        }

        if(isHighest){
            Button(
                onClick = onClickStartSource, enabled = true, modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {

                Text(text = stringResource(id = R.string.success))
            }
        }else{
            if(live){
                Button(
                    onClick = onClickStartSource, enabled = true, modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {

                    Text(text = stringResource(id = R.string.fail))
                }
            }else{
                Button(
                    onClick = onClickStartSource, enabled = false, modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {

                    Text(text = stringResource(id = R.string.fail))
                }
            }
        }

    }
    HorizontalDivider(thickness = 1.dp)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
//        BidHistoryScreen()

}