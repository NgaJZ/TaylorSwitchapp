package com.example.taylorswitch.ui.Auction.UiScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taylorswitch.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.taylorswitch.TaylorSwitchBottomBar
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.data.TabBarItem
import com.example.taylorswitch.data.WindowType
import com.example.taylorswitch.data.fireStore.model.Auction
import com.example.taylorswitch.data.rememberWindowSize
import com.example.taylorswitch.ui.Auction.Viewmodel.AuctionViewModel
import com.example.taylorswitch.ui.Auction.Viewmodel.BidViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreen(viewModel: BidViewModel = viewModel(), navController:NavHostController){
    viewModel.getAuctionList()
    val auctions by viewModel.auctionList.collectAsStateWithLifecycle()
    val windowSize = rememberWindowSize()
    val gridSize = when (windowSize.width) {
        WindowType.SMALL -> 2  // 2 columns for small screens (portrait)
        WindowType.MEDIUM -> 3
        else -> 4              // 3 columns for large screens (landscape)
    }

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            items(auctions) { auction -> // Adjust the number of items accordingly
                if(auction.live){
                    ItemCard(auction, onClickStartSource = {
                        navController.navigate(TaylorSwitchScreen.ViewBid.name+"/${auction.id}")
                        viewModel.getAuctionById(auction.id.toString())

                    }  )
                }else{

                }
            }
        }
    }

//}

@Composable
fun ItemCard(auction: Auction, onClickStartSource : () -> Unit){
    Card(
//        colors = CardColors(Color.White,Color.White,Color.White,Color.White),
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
                if(auction.imageRef.size != 0){
                    Image(modifier = Modifier
                        .width(149.dp)
                        .height(194.dp),
                        contentScale = ContentScale.Crop,
                        painter = rememberImagePainter(data = auction.imageRef[0]),
                        contentDescription = "Thumbnail")
                }else{
                    Image(modifier = Modifier
                        .width(149.dp)
                        .height(194.dp),
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "image description"
                    )
                }

            }
            Text(
                text = auction.name,
                fontSize = 16.sp,
//                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = auction.endDate,
                fontSize = 14.sp,
//                color = Color.Gray
            )
        }
    }
}

@Preview (showSystemUi = true)
@Composable
fun PreviewMain(){
//    HomeScreen()
}