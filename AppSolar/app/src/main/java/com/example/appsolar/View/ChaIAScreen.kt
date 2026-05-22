package com.example.appsolar.View

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsolar.ViewModel.ChatIAViewModel
import kotlinx.coroutines.delay

@Composable
fun ChatIAScreen(
    modifier: Modifier = Modifier,
    chatIAViewModel: ChatIAViewModel = viewModel()
) {

    var text by remember { mutableStateOf("") }
    val isLoading by chatIAViewModel.isLoading.collectAsState()
    val messages = chatIAViewModel.messages
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {

        listState.scrollToItem(
            messages.lastIndex
        )
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF010B1B))
            .statusBarsPadding()
            .padding(start = 15.dp, end = 15.dp, bottom = 140.dp)
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .border(2.dp, Color.Gray.copy(0.5f), RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "¿Tienes alguna duda?",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier.height(30.dp))
            // CHAT
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp, bottom = 15.dp, top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                items(messages) { message ->

                    var displayedText by remember { mutableStateOf("") }

                    LaunchedEffect(Unit) {

                        if (!message.isUser && !message.animated) {

                            displayedText = ""

                            message.text.forEachIndexed { index, _ ->

                                displayedText =
                                    message.text.substring(0, index + 1)

                                delay(15)
                            }

                            // MARCAR COMO YA ANIMADO
                            message.animated = true

                        } else {

                            displayedText = message.text
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            if (message.isUser)
                                Arrangement.End
                            else
                                Arrangement.Start
                    ) {

                        Box(
                            modifier = Modifier
                                .widthIn(max = 500.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (message.isUser)
                                        Color(0xFF2563EB)
                                    else
                                        Color(0xFF1E293B)
                                )
                                .padding(14.dp)
                        ) {

                            var expanded by remember {
                                mutableStateOf(false)
                            }

                            var hasOverflow by remember {
                                mutableStateOf(false)
                            }

                            Column {

                                Text(
                                    text = displayedText,
                                    color = Color.White,
                                    fontSize = 15.sp,

                                    maxLines =
                                        if (expanded)
                                            Int.MAX_VALUE
                                        else
                                            5,

                                    overflow = TextOverflow.Ellipsis,

                                    onTextLayout = { result ->

                                        if (!expanded) {
                                            hasOverflow = result.hasVisualOverflow
                                        }
                                    }
                                )

                                if (hasOverflow) {

                                    Text(
                                        text =
                                            if (expanded)
                                                "Ver menos"
                                            else
                                                "Ver más",

                                        color = Color(0xFF60A5FA),

                                        modifier = Modifier
                                            .padding(top = 5.dp)
                                            .clickable {
                                                expanded = !expanded
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (isLoading) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, bottom = 10.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF1E293B))
                            .padding(14.dp)
                    ) {

                        Text(
                            "IA escribiendo...",
                            color = Color.White
                        )
                    }
                }
            }

            // INPUT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text("Escribe un mensaje...")
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = {
                        val cleanText = text.trim()
                        if (text.isNotEmpty()) {

                            chatIAViewModel.sendMessage(cleanText)
                            text = ""
                        }
                    }
                ) {

                    Icon(
                        Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }

}