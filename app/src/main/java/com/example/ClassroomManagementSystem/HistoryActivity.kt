package com.example.ClassroomManagementSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.utils.Classroom
import com.example.ClassroomManagementSystem.utils.Reservation
import com.example.ClassroomManagementSystem.utils.ReservationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList

@Composable
fun HistoryScreen(userId:String, modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Row(modifier = modifier.padding(innerPadding)){
                    GoBack(modifier = modifier.padding(innerPadding), navController = navController)
                    Greeting4(userId = userId.toInt(), modifier = modifier.padding(innerPadding))
                }
                ReserveList(userId = userId.toInt(),modifier = modifier.padding(innerPadding))
                Text(text = "END",modifier = modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun Greeting4(userId: Int, modifier: Modifier = Modifier) {
    Text(
        text = userId.toString()+"的预约记录",
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
fun ReserveList(userId:Int,modifier: Modifier) {
    val context = LocalContext.current
    var allReservation by remember { mutableStateOf<ArrayList<Reservation>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(isLoading){
        withContext(Dispatchers.IO){
            try {
                allReservation = ReservationDao.getReservationsByUserId(userId, context)
            } catch (e: Exception) {
                allReservation = null
            }
            isLoading = false
        }
    }
    if(isLoading){
        Text(text = "加载中")
    }
    else {
        if(allReservation == null){
            Text(text = "无预约记录")
        }
        else {
            LazyColumn(modifier = modifier) {
                items(items = allReservation!!) { item ->
                    Row(modifier = modifier) {
                        Text(text = "roomID:" + item.roomId.toString(), modifier = modifier)
                        Spacer(modifier = modifier.width(20.dp))
                        Text(text = "date:" + item.date, modifier = modifier)
                        Spacer(modifier = modifier.width(20.dp))
                        when (item.result) {
                            -2 -> Text(text = "已结束", modifier)
                            -1 -> Text(text = "未通过", modifier)
                            0 -> Text(text = "未处理", modifier)
                            1 -> Text(text = "进行中", modifier)
                        }
                    }
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