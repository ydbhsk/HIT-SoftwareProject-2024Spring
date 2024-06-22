package com.example.ClassroomManagementSystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.ClassroomManagementSystem.utils.ClassroomDao
import com.example.ClassroomManagementSystem.utils.Reservation
import com.example.ClassroomManagementSystem.utils.ReservationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdminScreen(modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Text(text = "test", modifier = modifier.padding(innerPadding))
                Greeting2(
                    modifier = modifier.padding(innerPadding)
                )
                Column(modifier = modifier) {
                    ReservationList(
                        modifier = modifier
                            .padding(innerPadding)
                            .weight(0.99f),
                        navController = navController
                    )
                    Spacer(modifier = Modifier
                        .padding(innerPadding)
                        .weight(0.01f))
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
fun Greeting2(modifier: Modifier = Modifier) {
    Text(
        text = "预约处理",
        modifier = modifier
    )
}

@Composable
fun ReservationList(modifier: Modifier,navController: NavController){
    val context = LocalContext.current
    var allReservations by remember { mutableStateOf<ArrayList<Reservation>?>(null) }
    var allClassrooms by remember { mutableStateOf<ArrayList<Classroom>?>(null) }
    var roomId2Position by remember { mutableStateOf(HashMap<Int, String>()) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(isLoading) {
        withContext(Dispatchers.IO){// 异步获取数据
            try {
                allReservations = ReservationDao.getAllReservations(context)
                allClassrooms = ClassroomDao.getAllClassrooms(context)
                for (item in allClassrooms!!){
                    roomId2Position.set(item.id, item.position)
                }
            } catch (e: Exception) {
                allReservations = null
                allClassrooms = null
            }
            isLoading = false
        }
    }
    if(isLoading){
        Text(text = "加载中")
    }
    else {
        if(allReservations == null){
            Text(text = "无预约记录")
        }
        else{
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(500.dp)
            ) {
                items(items = allReservations!!) { item ->
                    Row (){
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                            Text(text = item.userId.toString())
                            Text(text = roomId2Position[item.roomId]!!.toString())
                            Text(text = item.date)
                            if (item.result == 0) {
                                Text(text = "待审核")
                            } else if (item.result == 1) {
                                Text(text = "进行中")
                            } else {
                                Text(text = "已退回")
                            }
                        }
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    ReservationDao.updateReservation(
                                        item.date,
                                        item.roomId,
                                        1,
                                        context
                                    )
                                    withContext(Dispatchers.Main) {
                                        navController.navigate("adminScreen")
                                    }
                                }
                            },
                            modifier = Modifier.width(50.dp)
                        ) {
                            Text(text = "通过")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    ReservationDao.updateReservation(
                                        item.date,
                                        item.roomId,
                                        -1,
                                        context
                                    )
                                    withContext(Dispatchers.Main) {
                                        navController.navigate("adminScreen")
                                    }
                                }
                            },
                            modifier = Modifier.width(50.dp)
                        ) {
                            Text(text = "退回")
                        }
                    }
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