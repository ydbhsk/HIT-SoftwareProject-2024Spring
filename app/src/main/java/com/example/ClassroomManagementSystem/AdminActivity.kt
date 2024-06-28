package com.example.ClassroomManagementSystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.uiItem.NavigationItem
import com.example.ClassroomManagementSystem.utils.Classroom
import com.example.ClassroomManagementSystem.utils.ClassroomDao
import com.example.ClassroomManagementSystem.utils.Constant
import com.example.ClassroomManagementSystem.utils.Reservation
import com.example.ClassroomManagementSystem.utils.ReservationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdminScreen(modifier: Modifier,navController: NavController) {
    val navigationItem = listOf(
        NavigationItem(title = "教室编辑", icon =  Icons.Filled.Edit),
        NavigationItem(title = "预约列表", icon =  Icons.Filled.ChecklistRtl),
        NavigationItem(title = "退出登录", icon =  Icons.Filled.Logout)
    )
    var currentNavigationIndex by remember { mutableStateOf(1) }
    var currentPage by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            Row(modifier = Modifier.statusBarsPadding()){
                when(currentNavigationIndex){
                    0 -> currentPage = "教室编辑"
                    1 -> currentPage = "预约列表"
                }
                Text(text = "当前界面：${currentPage}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(0.2f))
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF2196F3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Constant.bottomNavigationHeight.dp)
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
                                contentDescription = null,)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                    )
                }
            }
        }){ padding->
        when(currentNavigationIndex){
            0 -> EditScreen(modifier = modifier.padding(padding),
                navController = navController)
            1 -> ReservationList(modifier = modifier.padding(padding),
                navController = navController)
        }
    }
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
                allReservations = ReservationDao.getUnprocessedReservations(context)
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
        LoadingScreen()
    }
    else {
        if(allReservations == null){
            NullScreen()
        }
        else{
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = Constant.bottomNavigationHeight.dp)
//                    .navigationBarsPadding()
            ) {
                items(items = allReservations!!) { item ->
                    val whichDay = item.date.split("-")[0]
                    val whichClass = item.date.split("-")[1]
                    val result = when (item.result) {
                        -2 ->   "已结束"
                        -1 ->   "已退回"
                        0 ->    "待审核"
                        1 ->    "进行中"
                        else -> { "未知"}
                    }
                    val backcolor = when (item.result) {
                        -2 ->   Color(0xFF999999)
                        -1 ->   Color(0xFFFF6666)
                        0 ->    Color(0xFFFFF06B)
                        1 ->    Color(0xFF74FFC7)
                        else -> { Color(0xFF999999)}
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                        .background(backcolor)
                        .height(100.dp)){
                        Text(text = item.userId.toString()
                                + "\t"
                                + roomId2Position[item.roomId]!!.toString()
                                + "\t"
                                + Constant.weekDays[whichDay.toInt()]
                                + "第" + whichClass + "节课",
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
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(bottom = 8.dp),
                            enabled = item.result == 0
                        ) {
                            Text(text = "通过")
                        }
                        Text(text = result,
                            color = Color(0xFF333333),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 8.dp)
                        )
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if(item.result == 0){
                                        ReservationDao.updateReservation(
                                            item.date,
                                            item.roomId,
                                            -1,
                                            context
                                        )
                                    } else if(item.result == 1){
                                        ReservationDao.updateReservation(
                                            item.date,
                                            item.roomId,
                                            -2,
                                            context
                                        )
                                    }
                                    withContext(Dispatchers.Main) {
                                        navController.navigate("adminScreen")
                                    }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 8.dp)
                        ) {
                            if(item.result == 1){ Text(text = "结束") }
                            else{ Text(text = "退回") }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}