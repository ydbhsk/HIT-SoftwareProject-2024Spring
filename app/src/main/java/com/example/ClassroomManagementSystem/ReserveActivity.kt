package com.example.ClassroomManagementSystem

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.utils.ReservationDao
import com.example.ClassroomManagementSystem.utils.Reserve
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveScreen(userId:String,roomId:String,position: String, modifier: Modifier,navController: NavController) {
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "${position} 的预约情况",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("userScreen/${userId}")
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },){padding ->
        ReserveList(userId = userId.toInt(),roomId = roomId.toInt(),
            position = position,modifier = modifier.padding(padding),navController = navController)
    }
}
@Composable
fun ReserveList(userId:Int, roomId: Int, position: String,modifier: Modifier,navController: NavController) {
    val context = LocalContext.current
    var reserve by remember { mutableStateOf<Reserve?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val header = listOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
    val rows = mutableListOf<List<Boolean>>()
    for(i in 1..12) {
        val row = mutableListOf<Boolean>()
        for(j in 1..7){
            row.add(false)
        }
        rows.add(row)
    }
    LaunchedEffect(isLoading) {
        withContext(Dispatchers.IO){// 异步获取数据
            try {
                reserve = Reserve(roomId, context)
            } catch (e: Exception) {
                reserve = null
            }
            isLoading = false
        }
    }
    if(isLoading){
        LoadingScreen()
    }
    else {
        if (reserve == null) {
            NullScreen()
        }
        else{
//            LazyColumn(){
//                item{
//                    LazyRow(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        items(header.size) { index ->
//                            Box(
//                                modifier = Modifier
//                                    .background(Color.Gray)
//                                    .padding(8.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(text = header[index], color = Color.White)
//                            }
//                        }
//                    }
//                }
//                items(rows.size){index->
//                    LazyRow {
//
//                    }
//
//                }
//            }





            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.width(55.dp))
                        for (i in 1..7) {
                            Text("$i")
                            Spacer(modifier = Modifier.width(40.dp))
                        }
                    }
                    for (i in 1..12) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                "$i",
                                modifier = Modifier.width(40.dp)
                            )
                            for (j in 1..7) {
                                Button(
                                    onClick = {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try{
                                                Reserve.tryReserve(roomId, j, i, userId, context)
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "预约成功",
                                                        Toast.LENGTH_SHORT).show()
                                                    navController.navigate(
                                                        "reserveScreen/${userId}/${roomId}/${position}")
                                                }
                                            }catch (e: Exception){
                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(context, "预约失败",
                                                        Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp)
                                        .padding(PaddingValues(2.dp)),
                                    shape = CutCornerShape(0.dp),
                                    enabled = !reserve?.isReserved(j, i)!!
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}