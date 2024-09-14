package com.example.taylorswitch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taylorswitch.R


@Composable
fun BidHistoryScreen(){
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {},
                modifier = Modifier
                    .size(50.dp),

                )
            {
                Icon(
                    painter = painterResource(id = R.drawable.mail),
                    contentDescription = "return"
                )
            }
            Text(
                text = stringResource(id = R.string.bid_history),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Image(
                painterResource(id = R.drawable.mail), contentDescription = "item",
                Modifier
                    .size(100.dp)
                    .align(Alignment.CenterVertically)
                    .padding(10.dp))
            Column(){
                Text(text = stringResource(id = R.string.title),
                    modifier = Modifier.padding(top = 5.dp))
                Text(text = stringResource(id = R.string.your_bid),
                    modifier = Modifier.padding(top = 5.dp))
                Text(text = stringResource(id = R.string.highest),
                    modifier = Modifier.padding(top = 5.dp))
            }
            Button(onClick = {},enabled = true, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 100.dp)){
                Text(text = stringResource(id = R.string.live))
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }


}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    TaylorSwiftApplicationTheme {
//        BidHistory()
//    }
//}