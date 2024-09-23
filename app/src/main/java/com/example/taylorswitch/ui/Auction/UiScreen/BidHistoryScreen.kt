package com.example.taylorswitch.ui.Auction.UiScreen

import androidx.annotation.DrawableRes
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taylorswitch.R
import com.example.taylorswitch.data.BidUiState
import com.example.taylorswitch.data.postRec
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

@Composable
fun BidHistoryScreen(bidViewModel: BidViewModel, list: List<postRec> = emptyList()){
    bidViewModel.getUserPostRefArray("0")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list){ postRec ->
            ListItem(title = postRec.name, currentBid = postRec.currentBid, timeLeft = postRec.timeLeft)

        }
//        items(auction) { auction ->
//            ListItem()
        }
    }

@Composable
fun ListItem(
//    @DrawableRes image: Int,
    title: String, currentBid: String, timeLeft: String){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween){
        Row() {
            Image(
                painterResource(id = R.drawable.mail), contentDescription = "item",
                Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = title)
                Text(text = stringResource(id = R.string.your_bid, currentBid))
                Text(text = stringResource(id = R.string.highest, timeLeft))
//                Text(text = stringResource(id = R.string.title))
//                Text(text = stringResource(id = R.string.your_bid))
//                Text(text = stringResource(id = R.string.highest))
            }
        }
//        Spacer(modifier = Modifier.width(80.dp))
        Button(onClick = {},enabled = true, modifier = Modifier
            .align(Alignment.CenterVertically)
            ){
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