package com.example.mytest3

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytest3.ui.theme.MyTest3Theme
import com.example.mytest3.utils.*

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
        composable("userScreen/{userId}",// 用户教室预览页路由
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").toString()
            UserScreen(userId,modifier,navController)
        }

        composable("reserveScreen/{userId}/{roomId}",// 教室预约页路由
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").toString()
            val roomId = backStackEntry.arguments?.getString("roomId").toString()
            ReserveScreen(userId,roomId,modifier, navController)
        }

        composable("historyScreen/{userId}",// 历史预约页路由
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
            text = "欢迎使用教室管理系统",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
    )
}

// 登录界面
@Composable
fun Login(modifier: Modifier, navController: NavController) {
    //用户名
    var id by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    val context = LocalContext.current
    Column(){
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text(text = "学号") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text(text = "密码") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Button(
            onClick = {
                val authority = Login.tryLog(id, password, context)
                if (authority >= 0) {
                    val nav = Login.chooseActivity(id,authority, context)
                    navController.navigate(nav)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "登录")
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyTest3Theme {
//        MainNavGraph(modifier = Modifier.fillMaxSize())
//    }
//}