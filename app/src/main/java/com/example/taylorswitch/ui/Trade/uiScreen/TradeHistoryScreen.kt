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
import com.example.taylorswitch.data.tradeHistory
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TradeHistoryScreen(tradeViewModel: TradeViewModel, list: List<tradeHistory> = emptyList(), navController: NavHostController){
    LaunchedEffect(Unit) {
        tradeViewModel.getUserHistoryArray("0", "userPost", "postTradeRef")
    }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { tradeHistoryRec ->
                ListItem(
                    imageRef = tradeHistoryRec.imageRef.get(0),
                    title = tradeHistoryRec.title,
                    onClickStartSource = {
                        navController.navigate(route = TaylorSwitchScreen.ReviewTrade.name+"/${tradeHistoryRec.id.toInt()}")
                        tradeViewModel.getTradeById((tradeHistoryRec.id.toInt()).toString())
                    },
                    isOpen = tradeHistoryRec.live,
                    win = tradeViewModel.checkWinOrNot(tradeHistoryRec.trader)
                )
            }
        }
    }


@Composable
fun ListItem(
    title: String, imageRef: String, onClickStartSource: () -> Unit, isOpen: Boolean = false, win: Boolean = false
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Column (
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = stringResource(id = R.string.title, title))
            }
        }
        if (win){
            Button(
                onClick = onClickStartSource,
                enabled = true,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(id = R.string.accepted))
            }
        }else{
            if (isOpen){
                Button(
                    onClick = onClickStartSource,
                    enabled = true,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = stringResource(id = R.string.rejected))
                }
            } else {
                Button(
                    onClick = onClickStartSource
                ) {
                    Text(text = stringResource(id = R.string.rejected))
                }
            }
        }
    }
}
//@Preview
//@Composable
//fun TradeHistoryScreenPreview(){
//    TradeHistoryScreen()
//}