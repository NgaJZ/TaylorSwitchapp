package com.example.taylorswitch.ui.Trade.uiScreen

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.taylorswitch.R
import com.example.taylorswitch.TaylorSwitchScreen
import com.example.taylorswitch.data.fireStore.model.Trade
import com.example.taylorswitch.data.tradeHistory
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel
import okio.Source

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeListScreen(tradeViewModel: TradeViewModel, navController: NavHostController){
    tradeViewModel.getTradeList()
    val trades by tradeViewModel.tradeList.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        tradeViewModel.getUserHistoryArray("0", "userPost", "postTradeRef")
    }
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trades) { trade ->
                ListPosting(
                    trade, onClickStartSource = {
                        navController.navigate(TaylorSwitchScreen.ReviewTrade.name + "/${trade.id}")
                        tradeViewModel.getTradeById(trade.id.toString())
                    }
                )
            }
        }
    }

@Composable
private fun ListPosting(
    trade: Trade, onClickStartSource: () -> Unit
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(){
            Image(
                painterResource(id = R.drawable.mail),
                contentDescription = "item",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)
            )
            Column (
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Spacer(modifier = Modifier.height(2.dp))

                Text(text = trade.title)
            }
        }
        if (trade.live){
            Button(
                onClick = onClickStartSource,
                enabled = true,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(id = R.string.open))
            }
        }else{
            Button(
                onClick = onClickStartSource,
                enabled = false,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(id = R.string.closed))
            }
        }
    }
    HorizontalDivider(thickness = 1.dp)
}

//@Preview
//@Composable
//fun TradeListScreenPreview(){
//    TradeListScreen()
//}