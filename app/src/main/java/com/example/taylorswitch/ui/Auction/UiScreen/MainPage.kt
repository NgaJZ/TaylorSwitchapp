package com.example.taylorswitch.ui.Auction.UiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taylorswitch.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.ui.Auction.Viewmodel.AuctionViewModel
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

@Composable
fun HomeScreen(viewModel: BidViewModel = viewModel(), navController:NavHostController){
    val auctions by viewModel.auctionList.collectAsStateWithLifecycle()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        items(auctions) { auction -> // Adjust the number of items accordingly
            ItemCard(auction.name, onClickStartSource = {
                navController.navigate(TaylorSwitchScreen.ViewBid.name+"/${auction.id}")
                viewModel.getAuctionById(auction.id.toString())

            }
            )
        }
    }
}

@Composable
fun ItemCard(title:String, onClickStartSource : () -> Unit){
    Card(
        colors = CardColors(Color.White,Color.White,Color.White,Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = onClickStartSource
    ) {
        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Placeholder for shapes
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icons placeholders (use real images or icons here)
                Image(modifier = Modifier.size(240.dp),
                    painter = painterResource(R.drawable.image),
                    contentDescription = "Triangle")
            }
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Updated today",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview (showSystemUi = true)
@Composable
fun PreviewMain(){
//    HomeScreen()
}