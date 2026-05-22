package com.example.appsolar.View

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.appsolar.Model.BottomItem

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {

    val listIcons = listOf(
        BottomItem(
            "DashBoard",
            Icons.Default.Dashboard
        ),
        BottomItem(
            "Chat IA",
            Icons.Default.Chat
        ),
    )
    var selected by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = Color.Transparent
    ) {
        listIcons.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selected == index,
                onClick = {
                    selected = index
                    onClick(index)
                },
                icon = {
                    Icon(
                        item.icon, item.text,
                    )
                },
                label = {
                    Text(
                        item.text,
                        fontSize = 16.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    indicatorColor = Color.Green,
                    selectedTextColor = Color.White
                )
            )
        }
    }

}