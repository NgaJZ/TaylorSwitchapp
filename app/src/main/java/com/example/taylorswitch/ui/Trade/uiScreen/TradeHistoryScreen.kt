package com.example.taylorswitch.ui.Trade.uiScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.taylorswitch.data.tradeHistory
import com.example.taylorswitch.ui.Trade.ViewModel.TradeViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
                    id = tradeHistoryRec.id.toString(),
                    title = tradeHistoryRec.title,
                    tradeEnd = tradeHistoryRec.tradeEnd,
                    onClickStartSource = {
                        navController.navigate(route = TaylorSwitchScreen.ReviewTrade.name+"/${tradeHistoryRec.id.toInt()}")
                        tradeViewModel.getTradeById((tradeHistoryRec.id.toInt()).toString())
                    },
                    isOpen = tradeHistoryRec.isOpen,
                    win = tradeViewModel.checkWinOrNot(user = "test", tradeId = tradeHistoryRec.id.toString())
                )
            }
        }
    }


@Composable
fun ListItem(
    imageRef: String = "", id: String = "", title:String, tradeEnd: Boolean = false,onClickStartSource: () -> Unit, isOpen: Boolean = false, win: Boolean = false
){
    Row(){
        if (imageRef != ""){
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(data = imageRef),
                contentDescription = "Thumbnail"
            )
        }else{
            Image(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.image),
                contentDescription = "Image Description"
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            Text(text = stringResource(id = R.string.title, title))
        }
    }
    if (win){
        Button(
            onClick = onClickStartSource,
            enabled = true
        ) {
            Text(text = stringResource(id = R.string.accepted))
        }
    }else{
        if (isOpen){
            Button(
                onClick = onClickStartSource,
                enabled = true
            ) {
                Text(text = stringResource(id = R.string.pending))
            }
        }else{
            Button(
                onClick = onClickStartSource,
                enabled = false
            ) {
                Text(text = stringResource(id = R.string.rejected))
            }
        }
    }
    HorizontalDivider(thickness = 1.dp)
}
//@Preview
//@Composable
//fun TradeHistoryScreenPreview(){
//    TradeHistoryScreen()
//}