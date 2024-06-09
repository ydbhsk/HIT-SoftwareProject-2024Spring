package com.example.mytest3

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytest3.ui.theme.MyTest3Theme
import com.example.mytest3.utils.Classroom
import com.example.mytest3.utils.ClassroomDao

@Composable
fun EditScreen(modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
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
                    val delResult = classroomDao.deleteClassroom(item.id)
                    if (delResult < 0){
                        // 删除失败
                        Toast.makeText(context, "教室不存在", Toast.LENGTH_SHORT).show()
                    }
                },
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(text = "删除")
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
    Column(){
        TextField(
            value = roomId,
            onValueChange = { roomId = it },
            label = { Text(text = "教室名称") },
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
                val classroom = Classroom(roomId, position)
                val classroomDao = ClassroomDao(context)
                classroomDao.addClassroom(classroom)
                navController.navigate("editScreen")
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