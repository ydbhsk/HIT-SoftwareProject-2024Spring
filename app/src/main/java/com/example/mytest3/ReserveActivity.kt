package com.example.mytest3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mytest3.ui.theme.MyTest3Theme
import com.example.mytest3.utils.*

@Composable
fun ReserveScreen(userId:String,roomId:String, modifier: Modifier,navController: NavController) {
    MyTest3Theme {
        Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
            Column {
                Row{
                    GoBack1(userId = userId, modifier = modifier.padding(innerPadding), navController = navController)
                    Greeting5(roomId = roomId, modifier = modifier.padding(innerPadding))
                }
                Spacer(modifier = Modifier.weight(0.01f))
                GoReserve(userId = userId,roomId = roomId,modifier = modifier.padding(innerPadding),navController = navController)
            }
        }
    }
}

@Composable
fun GoBack1(userId:String,modifier: Modifier,navController: NavController) {
    Button(onClick = {
        navController.navigate("userScreen/"+userId)
    }) {
        Text(text = "<")
    }
}

@Composable
fun Greeting5(roomId: String, modifier: Modifier) {
    Text(
        text = roomId+"的预约情况",
        modifier = modifier
    )
}

@Composable
fun GoReserve(userId:String,roomId:String,modifier: Modifier,navController: NavController) {
    val context = LocalContext.current
    val reserve = Reserve(roomId,context)
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
                for (i in 1..7){
                    Text("$i")
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }
            for (i in 1..12) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("$i",
                        modifier = Modifier.width(40.dp))
                    for (j in 1..7){
                        Button(
                            onClick = {
                                Reserve.tryReserve(roomId, j,i,userId,context)
                                navController.navigate("reserveScreen/${userId}/${roomId}")
                                      },
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .padding(PaddingValues(4.dp)),
                            shape = CutCornerShape(0.dp),
                            enabled = !reserve.isReserved(j,i)
                        ) {}
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