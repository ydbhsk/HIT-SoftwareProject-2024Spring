package com.example.ClassroomManagementSystem

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.utils.ReservationDao
import com.example.ClassroomManagementSystem.utils.Reserve
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReserveScreen(userId:String,roomId:String,position: String, modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Row{
                    GoBack1(userId = userId.toInt(), modifier = modifier.padding(innerPadding), navController = navController)
                    Greeting5(position = position, modifier = modifier.padding(innerPadding))
                }
                Spacer(modifier = Modifier.weight(0.01f))
                GoReserve(userId = userId.toInt(),roomId = roomId.toInt(),position = position,modifier = modifier.padding(innerPadding),navController = navController)
            }
        }
    }
}

@Composable
fun GoBack1(userId:Int,modifier: Modifier,navController: NavController) {
    Button(onClick = {
        navController.navigate("userScreen/${userId}")
    }) {
        Text(text = "<")
    }
}

@Composable
fun Greeting5(position:String, modifier: Modifier) {
    Text(
        text = position+"的预约情况",
        modifier = modifier
    )
}

@Composable
fun GoReserve(userId:Int, roomId: Int, position: String,modifier: Modifier,navController: NavController) {
    val context = LocalContext.current
    var reserve by remember { mutableStateOf<Reserve?>(null) }
    var isLoading by remember { mutableStateOf(true) }
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
        Text(text = "加载中")
    }
    else {
        if (reserve == null) {
            Text(text = "无可用信息")
        }
        else{
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
                                        .width(50.dp)
                                        .height(50.dp)
                                        .padding(PaddingValues(4.dp)),
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


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview5() {
//    MyTest3Theme {
//        ReserveScreen(userId = "default123",roomId = "201",modifier = Modifier, navController = rememberNavController())
//    }
//}