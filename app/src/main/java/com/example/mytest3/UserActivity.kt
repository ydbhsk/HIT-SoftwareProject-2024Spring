package com.example.mytest3

import android.R.string
import android.content.Context
import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytest3.ui.theme.MyTest3Theme
import com.example.mytest3.utils.Classroom
import com.example.mytest3.utils.ClassroomDao


@Composable
fun UserScreen(userId:String,modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Greeting3(
                    userId = userId,
                    modifier = modifier.padding(innerPadding)
                )
                Column(modifier = modifier) {
                    RoomList1(
                        userId = userId,
                        modifier = Modifier.padding(innerPadding).weight(0.99f),
                        navController = navController
                    )
                    Spacer(modifier = Modifier.padding(innerPadding).weight(0.01f))
                    UserNavigater(
                        userId = userId,
                        modifier = modifier.padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting3(userId: String, modifier: Modifier) {
    Text(
        text = userId+" 欢迎使用教室管理系统",
        modifier = modifier
    )
}

@Composable
fun RoomList1(userId: String,modifier: Modifier, navController: NavController) {
    val context = LocalContext.current
    val classroomDao = ClassroomDao(context)
    val allClassroom = classroomDao.getAllClassrooms()
    LazyColumn (modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)){
        items(items = allClassroom) { item ->
            Row{
                Column(modifier = Modifier.fillMaxWidth(0.5f)){
                    Text(text = item.id)
                    Text(text = item.position)
                }
                Button(onClick = {
                    navController.navigate("reserveScreen/${userId}/${item.id}")
                                 },
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(text = "预约")
                }
            }
        }
    }
}

@Composable
fun UserNavigater(userId: String,modifier: Modifier = Modifier,navController: NavController) {
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