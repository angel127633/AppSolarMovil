package com.example.appsolar.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldScreen(modifier: Modifier = Modifier) {

    var selected by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomBar() { index ->
                selected = index
            }
        }
    ) {
        Renderizado(selected = selected)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Renderizado(
    modifier: Modifier = Modifier,
    selected: Int
) {
    when (selected) {
        0 -> DashBoardScreen()
        1 -> PredictiveEnergyScreen()
        2 -> ChatIAScreen()
    }
}