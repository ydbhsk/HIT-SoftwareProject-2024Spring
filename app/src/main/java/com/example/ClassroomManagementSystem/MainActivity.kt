package com.example.ClassroomManagementSystem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ClassroomManagementSystem.ui.theme.MyTest3Theme
import com.example.ClassroomManagementSystem.utils.Login
import com.example.ClassroomManagementSystem.utils.UserDao
import com.example.ClassroomManagementSystem.utils.WebConnect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTest3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
                    MainNavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }

    }
}

@Composable
fun MainNavGraph(modifier: Modifier) {
    val navController = rememberNavController() // 创建 NavController

    // 设置 NavHost，管理导航内容
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { // 首页路由
            HomeScreen(modifier,navController)
        }
        composable(
            "userScreen/{userId}",// 用户教室预览页路由
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").toString()
            UserScreen(userId,modifier,navController)
        }

        composable(
            "reserveScreen/{userId}/{roomId}/{position}",// 教室预约页路由
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").toString()
            val roomId = backStackEntry.arguments?.getString("roomId").toString()
            val position = backStackEntry.arguments?.getString("position").toString()
            ReserveScreen(userId,roomId,position, modifier, navController)
        }

        composable(
            "historyScreen/{userId}",// 历史预约页路由
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").toString()
            HistoryScreen(userId,modifier, navController)
        }

        composable("adminScreen"// 管理员教室管理页路由
        ) {backStackEntry ->
            AdminScreen(modifier, navController)
        }

        composable("editScreen"// 管理员教室编辑页路由
        ) { backStackEntry ->
            EditScreen(modifier, navController)
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier,navController: NavController) {
    Column(modifier = modifier){
        Greeting(modifier)
        Login(modifier,navController)
    }
}

@Composable
fun Greeting(modifier: Modifier) {
    Text(
            text = "欢迎使用教室管理系统，请登录",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
    )
}

// 登录界面
@Composable
fun Login(modifier: Modifier, navController: NavController) {
    //用户名
    var user_id by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    val context = LocalContext.current
    var authority by remember{ mutableStateOf(-3) }
        Column() {
            Spacer(modifier = Modifier.weight(0.3f))
            TextField(
                value = user_id,
                onValueChange = { user_id = it },
                label = { Text(text = "学号", fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(10.dp)
            )
            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "密码", fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(10.dp)
            )
            Row(modifier = Modifier.fillMaxWidth().weight(1f)){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .weight(0.5f)
                ) {
                    Button(
                        onClick = {
                            authority = -3
                            CoroutineScope(Dispatchers.IO).launch {
                                authority = UserDao.queryUser(
                                    user_id.toInt(),
                                    password,
                                    context
                                )
                                withContext(Dispatchers.Main) {
                                    when (authority) {
                                        -1 -> {
                                            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                        -2 -> {
                                            Toast.makeText(
                                                context,
                                                "用户不存在",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }

                                        0 -> {
                                            Toast.makeText(context, "成功登入", Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                        1 -> {
                                            Toast.makeText(
                                                context,
                                                "管理员模式",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }

                                        else -> {
                                            Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                    if (authority >= 0) {
                                        val nav = when (authority) {
                                            0 -> "userScreen/${user_id}"
                                            1 -> "adminScreen"
                                            else -> "homeScreen"
                                        }
                                        navController.navigate(nav)
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E3B85)),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = "登录",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .weight(0.5f)
                ) {
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    UserDao.addUser(user_id.toInt(), password, context)
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x9F707070)),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = "注册",
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.8f))
            Box(modifier = Modifier.fillMaxWidth(1f)) {
                Button(
                    onClick = {
                        var isConn = false
                        CoroutineScope(Dispatchers.IO).launch {
                            isConn = WebConnect.testConnect(context)
                            withContext(Dispatchers.Main) {
                                if (isConn) {
                                    Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x9F707070)),
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = "测试连接",
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            MainNavGraph(modifier = Modifier.padding(innerPadding))
        }
    }
}