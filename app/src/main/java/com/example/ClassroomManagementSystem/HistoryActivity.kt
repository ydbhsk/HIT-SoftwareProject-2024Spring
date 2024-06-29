package com.example.ClassroomManagementSystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.utils.Classroom
import com.example.ClassroomManagementSystem.utils.Constant
import com.example.ClassroomManagementSystem.utils.Reservation
import com.example.ClassroomManagementSystem.utils.ReservationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList

@Composable
fun ReservationList(userId:Int,modifier: Modifier) {
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
            LazyColumn(modifier = modifier
                .fillMaxSize()
//                .padding(bottom = Constant.bottomNavigationHeight.dp)
//                .navigationBarsPadding()
            ) {
                items(items = allReservation!!) { item ->
                    val whichDay = item.date.split("-")[0]
                    val whichClass = item.date.split("-")[1]
                    val result = when (item.result) {
                        -2 ->   "已结束"
                        -1 ->   "未通过"
                        0 ->    "未处理"
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
                        .height(50.dp)){
                        Text(text = item.roomId.toString()
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
                        Text(text = result,
                            color = Color(0xFF333333),
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(bottom = 8.dp)
                        )
                        Divider()
                    }
                }
            }
        }
    }
}