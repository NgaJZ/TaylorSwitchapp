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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.taylorswitch.R
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.data.postRec
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

@Composable
fun BidHistoryScreen(bidViewModel: BidViewModel, list: List<postRec> = emptyList(), navController: NavHostController) {


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) { postRec ->
            ListItem(
                id = postRec.id.toString(),
                title = postRec.name,
                highestBid = postRec.highestBid.toString(),
                endDate = postRec.endDate,
                endTime = postRec.endTime,
                onClickStartSource = {
                    navController.navigate(TaylorSwitchScreen.ViewBid.name+"/${postRec.id.toInt()}")
                    bidViewModel.getAuctionById((postRec.id.toInt()).toString())
                }
            )

        }
    }
}

@Composable
fun ListItem(
//    @DrawableRes image: Int,
    id: String = "", title: String, highestBid: String, endDate: String, endTime: String,onClickStartSource: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row() {
            Image(
                painterResource(id = R.drawable.mail), contentDescription = "item",
                Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(text = stringResource(id = R.string.title, id))
                Text(text = stringResource(id = R.string.highest, highestBid))
                Text(text = stringResource(id = R.string.endDate, endDate))
                Text(text = stringResource(id = R.string.endTime, endTime))

//                Text(text = stringResource(id = R.string.timeLeft, timeLeft))
            }
        }

        Button(
            onClick = onClickStartSource, enabled = true, modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {

            Text(text = stringResource(id = R.string.live))
        }
    }
    HorizontalDivider(thickness = 1.dp)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
//        BidHistoryScreen()

}