package com.example.ClassroomManagementSystem

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.utils.Classroom
import com.example.ClassroomManagementSystem.utils.ClassroomDao
import com.example.ClassroomManagementSystem.utils.Constant
import com.example.ClassroomManagementSystem.utils.InputCheck
import com.example.ClassroomManagementSystem.utils.ReservationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditScreen(modifier: Modifier,navController: NavController) {
    var addmode by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = Constant.bottomNavigationHeight.dp)
        .navigationBarsPadding(),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addmode = !addmode
            },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                if(addmode){
                    Icon(Icons.Default.Remove, contentDescription = null)
                } else{
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }){ innerPadding ->
        Box(modifier = Modifier){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween) {
                if(addmode){
                    EditForm(
                        modifier = modifier
                            .padding(innerPadding), navController = navController
                    )
                }
                else{
                    RoomList(
                        modifier = modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
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
        LoadingScreen()
    }
    else {
        if (allClassroom == null) {
            NullScreen()
        }
        else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
            ) {
                items(items = allClassroom!!) { item ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                        .height(50.dp)){
                        Text(text = item.id.toString(),
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
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(bottom = 8.dp)
                        ) {
                            Text(text = "删除")
                        }
                        Text(text = item.position,
                            color = Color(0xFF333333),
                            fontSize = 16.sp,
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
        }
    }
}

@Composable
fun EditForm(modifier: Modifier,navController: NavController) {
    // 编辑表单
    val context = LocalContext.current
    var roomId by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = roomId,
            onValueChange = { roomId = it },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            label = {
                Text(
                    text = "教室编号",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
        TextField(
            value = position,
            onValueChange = { position = it },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Domain,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            label = {
                Text(
                    text = "教室位置",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            Button(
                onClick = {
                    // 提交表单
                    if(InputCheck.checkRoomId(roomId) || InputCheck.checkPosition(position)){
                        Toast.makeText(context, "输入不合法", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF167FD3)),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = "添加教室",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.8f))
    }
}