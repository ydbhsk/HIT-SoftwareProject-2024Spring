package com.example.mytest3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytest3.ui.theme.MyTest3Theme
import com.example.mytest3.utils.Classroom
import com.example.mytest3.utils.Reservation
import com.example.mytest3.utils.ReservationDao

@Composable
fun HistoryScreen(userId:String, modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Row(modifier = modifier.padding(innerPadding)){
                    GoBack(modifier = modifier.padding(innerPadding), navController = navController)
                    Greeting4(userId = userId, modifier = modifier.padding(innerPadding))
                }
                ReserveList(userId = userId,modifier = modifier.padding(innerPadding))
                Text(text = "END",modifier = modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun Greeting4(userId: String, modifier: Modifier = Modifier) {
    Text(
        text = userId+"的预约记录",
        modifier = modifier
    )
}

@Composable
fun GoBack(modifier: Modifier,navController: NavController) {
    // 返回按钮
    Button(onClick = {
        navController.popBackStack()
    }) {
        Text(text = "<")
    }
}

@Composable
fun ReserveList(userId:String,modifier: Modifier) {
    val context = LocalContext.current
    val reservationDao = ReservationDao(context)
    val allReservation = reservationDao.getReservationsByUserId(userId)
    LazyColumn (modifier = modifier){
        items(items = allReservation) { item ->
            Row(modifier = modifier){
                Text(text = "roomID:"+item.roomId,modifier = modifier)
                Spacer(modifier = modifier.width(20.dp))
                Text(text = "date:"+item.date,modifier = modifier)
                Spacer(modifier = modifier.width(20.dp))
                when(item.result){
                    -2 -> Text(text = "已结束",modifier)
                    -1 -> Text(text = "未通过",modifier)
                    0 -> Text(text = "未处理",modifier)
                    1 -> Text(text = "已通过",modifier)
                }
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview4() {
//    MyTest3Theme {
//        HistoryScreen(userId = "default123", modifier = Modifier, navController = rememberNavController())
//    }
//}