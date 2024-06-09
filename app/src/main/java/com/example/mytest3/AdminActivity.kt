package com.example.mytest3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytest3.ui.theme.MyTest3Theme
import com.example.mytest3.utils.Reservation
import com.example.mytest3.utils.ReservationDao
import com.example.mytest3.utils.Reserve

@Composable
fun AdminScreen(modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Greeting2(
                    name = "",
                    modifier = modifier.padding(innerPadding)
                )
                Column(modifier = modifier) {
                    ReservationList(
                        modifier = modifier.padding(innerPadding).weight(0.99f),
                        navController = navController
                    )
                    Spacer(modifier = Modifier.padding(innerPadding).weight(0.01f))
                    AdminNavigater(
                        modifier = modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "预约处理",
        modifier = modifier
    )
}

@Composable
fun ReservationList(modifier: Modifier,navController: NavController){
    val context = LocalContext.current
    val reservationDao = ReservationDao(context)
    val allReservations = reservationDao.getUnprocessedReservations()
    LazyColumn (modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)){
        items(items = allReservations) { item ->
            Row{
                Column(modifier = Modifier.width(100.dp)){
                    Text(text = item.userId)
                    Text(text = item.roomId)
                    Text(text = item.date)
                }
                Button(onClick = {
                    reservationDao.updateReservation(item.date,item.roomId,1)
                    navController.navigate("adminScreen")
                },
                    modifier = Modifier.fillMaxWidth(0.45f)) {
                    Text(text = "通过")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    reservationDao.updateReservation(item.date,item.roomId,-1)
                    navController.navigate("adminScreen")
                },
                    modifier = Modifier.fillMaxWidth(0.75f)) {
                    Text(text = "退回")
                }
            }
        }
    }
}

@Composable
fun AdminNavigater(modifier: Modifier = Modifier,navController: NavController) {
    Row(modifier = modifier.fillMaxWidth()){
        Button(onClick = {
            navController.navigate("editScreen")
        }, modifier = modifier.weight(0.4f)) {
            Text(text = "编辑教室")
        }
        Spacer(modifier = modifier.weight(0.2f))
        Button(onClick = {
            navController.navigate("homeScreen")
        }, modifier = modifier.weight(0.4f)) {
            Text(text = "退出登录")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    MyTest3Theme {
//        AdminScreen(modifier = Modifier,navController = rememberNavController())
//    }
//}