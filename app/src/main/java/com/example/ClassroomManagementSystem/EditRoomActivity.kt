package com.example.ClassroomManagementSystem

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.ClassroomManagementSystem.utils.ReservationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditScreen(modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Spacer(modifier = Modifier.height(50.dp))
                Row{
                    GoBack2(modifier = modifier.padding(innerPadding), navController = navController)
                    Greeting6(name = "",modifier = modifier)
                }
                RoomList(modifier = modifier.padding(innerPadding))
                EditForm(modifier = modifier.padding(innerPadding),navController = navController)
            }
        }
    }
}

@Composable
fun GoBack2(modifier: Modifier,navController: NavController) {
    // 返回按钮
    Button(onClick = {
        navController.navigate("adminScreen")
    }) {
        Text(text = "<")
    }
}

@Composable
fun Greeting6(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun RoomList(modifier: Modifier) {
    // 教室列表
    val context = LocalContext.current
    var allClassroom by remember { mutableStateOf<ArrayList<Classroom>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(isLoading) {
        withContext(Dispatchers.IO){// 异步获取数据
            try {
                allClassroom = ClassroomDao.getAllClassrooms(context)
            } catch (e: Exception) {
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
            Text(text = "无教室")
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(items = allClassroom!!) { item ->
                    Row {
                        Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                            Text(text = item.id.toString())
                            Text(text = item.position)
                        }
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try{
                                        ClassroomDao.deleteClassroom(item.id, context)
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception){
                                        withContext(Dispatchers.Main) {
                                            withContext(Dispatchers.Main) {// 删除失败
                                                Toast.makeText(
                                                    context, "删除失败",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text(text = "删除")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditForm(modifier: Modifier,navController: NavController) {
    // 编辑表单
    val context = LocalContext.current
    var roomId by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    Column() {
        TextField(
            value = roomId,
            onValueChange = { roomId = it },
            label = { Text(text = "教室编号") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        TextField(
            value = position,
            onValueChange = {
                position = it
            },
            label = { Text(text = "教室位置") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Button(
            onClick = {
                // 提交表单
                val classroom = Classroom(roomId.toInt(), position)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        ClassroomDao.addClassroom(classroom, context)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "添加教室")
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview6() {
//    MyTest3Theme {
//        EditScreen(modifier = Modifier,navController = rememberNavController())
//    }
//}