package com.example.ClassroomManagementSystem

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.uiItem.NavigationItem
import com.example.ClassroomManagementSystem.utils.Classroom
import com.example.ClassroomManagementSystem.utils.ClassroomDao
import com.example.ClassroomManagementSystem.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun UserScreen(userId:String,modifier: Modifier,navController: NavController) {
    val navigationItem = listOf(
        NavigationItem(title = "预约历史", icon =  Icons.Filled.History),
        NavigationItem(title = "教室列表", icon =  Icons.Filled.Home),
        NavigationItem(title = "退出登录", icon =  Icons.Filled.Logout)
    )
    var currentNavigationIndex by remember { mutableStateOf(1) }
    var currentPage by remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(modifier = Modifier.statusBarsPadding()){
                when(currentNavigationIndex){
                    0 -> currentPage = "预约历史"
                    1 -> currentPage = "教室列表"
                }
                Text(text = "当前界面：${currentPage}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(0.2f))
                Text(text = "你好，${userId}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1E35C2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Constant.bottomNavigationHeight.dp),
            ) {
                navigationItem.forEachIndexed {index, item ->
                    NavigationBarItem(
                        selected = currentNavigationIndex == index,
                        onClick = {
                            currentNavigationIndex = index
                            if(index == 2){
                                navController.navigate("homeScreen")
                            }
                        },
                        icon = {
                            Icon(imageVector = item.icon,
                                contentDescription = null,
                                tint = Color.White,)
                        },
                        label = {
                            Text(text = item.title,
                                color = Color.White,)
                        },
                        alwaysShowLabel = false,
                    )
                }
            }
        }){padding->
        Box(modifier = Modifier.fillMaxSize()){
            // 渐变图层
            ColorScreen()
            when (currentNavigationIndex) {
                0 -> ReservationList(
                    userId = userId.toInt(),
                    modifier = modifier.padding(padding)
                )

                1 -> RoomList(
                    userId = userId.toInt(),
                    modifier = modifier.padding(padding),
                    navController = navController
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RoomList(userId: Int,modifier: Modifier, navController: NavController) {
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
        LoadingScreen()
    }
    else {
        if (allClassroom == null) {
            NullScreen()
        }
        else{
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
//                    .padding(bottom = Constant.bottomNavigationHeight.dp)
//                    .navigationBarsPadding()
            ) {
                items(items = allClassroom!!) { item ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                        .height(50.dp)) {
                        Text(text = item.position,
                            color = Color(0xFF333333),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(bottom = 8.dp)
                        )
                        Button(
                            onClick = {
                                navController.navigate("reserveScreen/${userId}/${item.id}/${item.position}")
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(bottom = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E3EA0),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "预约")
                        }
                        Text(text = "INFO:${item.id}",
                            color = Color(0xFF333333),
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 8.dp)
                        )
                    }
                    Divider()
                }
            }
            Spacer(modifier = Modifier.height(Constant.bottomNavigationHeight.dp))
        }
    }
}