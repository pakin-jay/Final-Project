package com.example.courtsmash.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.domain.model.ChatMessage
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightChip
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.DeepGold
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.MutedGreenSuccess
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun AiChatbotScreen(
    viewModel: AiChatbotViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val quickQuestions = listOf(
        "Racket & String Tension Guide",
        "Doubles Serving Rules",
        "Footwork & Split Step Drills",
        "Attacking vs Defensive Strategy"
    )

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Surface(
                color = PureWhiteSurface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(LuminousGoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QuestionAnswer,
                            contentDescription = "AI Chat",
                            tint = ChampagneGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Badminton Coach 🏸",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MutedGreenSuccess)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Online • OpenRouter Gemini AI",
                                fontSize = 11.sp,
                                color = WarmGrayText
                            )
                        }
                    }
                }
            }

            // Quick Questions Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickQuestions) { question ->
                    HolyLightChip(
                        text = question,
                        selected = false,
                        onClick = { viewModel.sendMessage(question) }
                    )
                }
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.messages, key = { it.id }) { message ->
                    ChatMessageBubble(message = message)
                }

                if (state.isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        ) {
                            CircularProgressIndicator(
                                color = ChampagneGold,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Coach is typing...",
                                fontSize = 12.sp,
                                color = DeepGold,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Input Dock at Bottom
            Surface(
                color = PureWhiteSurface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.inputText,
                        onValueChange = viewModel::onInputTextChanged,
                        placeholder = {
                            Text(
                                text = "Ask about badminton rules, rackets, drills...",
                                fontSize = 13.sp,
                                color = WarmGrayText
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PureWhiteSurface,
                            unfocusedContainerColor = PureWhiteSurface,
                            focusedBorderColor = ChampagneGold,
                            unfocusedBorderColor = CardBorderTint
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        enabled = state.inputText.isNotBlank() && !state.isLoading,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (state.inputText.isNotBlank() && !state.isLoading) ChampagneGold else LuminousGoldLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(message: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        if (message.isUser) {
            Surface(
                color = ChampagneGold,
                shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(0.82f)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        } else {
            HolyLightCard(
                modifier = Modifier.fillMaxWidth(0.88f),
                cornerRadius = 18.dp,
                containerColor = PureWhiteSurface,
                elevation = 2.dp
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = DeepCharcoalText,
                    lineHeight = 21.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}
