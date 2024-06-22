package com.example.ClassroomManagementSystem

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun UserScreen(userId:String,modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Greeting3(
                    userId = userId.toInt(),
                    modifier = modifier.padding(innerPadding)
                )
                Column(modifier = modifier) {
                    RoomList1(
                        userId = userId.toInt(),
                        modifier = Modifier
                            .padding(innerPadding)
                            .weight(0.99f),
                        navController = navController
                    )
                    Spacer(modifier = Modifier
                        .padding(innerPadding)
                        .weight(0.01f))
                    UserNavigater(
                        userId = userId.toInt(),
                        modifier = modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting3(userId: Int, modifier: Modifier) {
    Text(
        text = userId.toString()+" 欢迎使用教室管理系统",
        modifier = modifier
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RoomList1(userId: Int,modifier: Modifier, navController: NavController) {
    val context = LocalContext.current
    var allClassroom by remember { mutableStateOf<ArrayList<Classroom>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(isLoading) {
        withContext(Dispatchers.IO){// 异步获取数据
            try {
                allClassroom = ClassroomDao.getAllClassrooms(context)
            } catch (e: Exception) {
                println(e)
                allClassroom = null
            }
            isLoading = false
        }
    }
    if(isLoading){
        Text(text = "加载中")
    }
    else {
        if (allClassroom == null) {
            Text(text = "无可用教室")
        }
        else{
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(items = allClassroom!!) { item ->
                    Row {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                            Text(text = item.position)
                        }
                        Button(
                            onClick = {
                                navController.navigate("reserveScreen/${userId}/${item.id}/${item.position}")
                            },
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text(text = "预约")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserNavigater(userId: Int,modifier: Modifier = Modifier,navController: NavController) {
    Row(modifier = modifier.fillMaxWidth()){
        Button(onClick = {
            navController.navigate("historyScreen/${userId}")
        }, modifier = modifier.weight(0.4f)) {
            Text(text = "预约查询")
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
//fun GreetingPreview3() {
//    MyTest3Theme {
//        UserScreen(userId = "default123", modifier = Modifier,navController = rememberNavController())
//    }
//}