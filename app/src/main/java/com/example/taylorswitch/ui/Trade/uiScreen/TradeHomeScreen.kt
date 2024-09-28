package com.example.taylorswitch.ui.Trade.uiScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.taylorswitch.R
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.data.WindowType
import com.example.taylorswitch.data.fireStore.model.Auction
import com.example.taylorswitch.data.fireStore.model.Trade
import com.example.taylorswitch.data.rememberWindowSize
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageScreen(
    viewModel: TradeViewModel = viewModel(),
    navController:NavHostController
){
    viewModel.getTradeList()
    val trades by viewModel.tradeList.collectAsStateWithLifecycle()
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
        items(trades) { trade -> // Adjust the number of items accordingly
            ItemCard(trade, onClickStartSource = {
                navController.navigate(viewModel.tradeList)
                viewModel.getTradeById(trade.id.toString())
            }
            )
        }
    }
}

@Composable
fun ItemCard(trade: Trade, onClickStartSource : () -> Unit){
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
                if(trade.imageRef.isNotEmpty()){
                    Image(modifier = Modifier
                        .width(149.dp)
                        .height(194.dp),
                        contentScale = ContentScale.Crop,
                        painter = rememberImagePainter(data = trade.imageRef[0]),
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
                text = trade.title,
                fontSize = 16.sp,
//                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

//@Preview
//@Composable
//fun GreetingPreview() {
//    HomepageScreen()
//}