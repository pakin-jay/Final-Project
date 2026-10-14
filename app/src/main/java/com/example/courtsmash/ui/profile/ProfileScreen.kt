package com.example.courtsmash.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.courtsmash.ui.components.HolyLightCard
import com.example.courtsmash.ui.components.HolyLightPrimaryButton
import com.example.courtsmash.ui.components.HolyLightSecondaryButton
import com.example.courtsmash.ui.theme.CardBorderTint
import com.example.courtsmash.ui.theme.ChampagneGold
import com.example.courtsmash.ui.theme.DeepCharcoalText
import com.example.courtsmash.ui.theme.LuminousGoldLight
import com.example.courtsmash.ui.theme.MutedRedError
import com.example.courtsmash.ui.theme.PureWhiteSurface
import com.example.courtsmash.ui.theme.WarmGrayText
import com.example.courtsmash.ui.theme.WarmIvoryBackground

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    var editName by remember(state.displayName) { mutableStateOf(state.displayName) }
    var editLocation by remember(state.preferredLocation) { mutableStateOf(state.preferredLocation) }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvoryBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Profile & Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepCharcoalText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Info Card
            HolyLightCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(LuminousGoldLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User",
                                    tint = ChampagneGold,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = state.displayName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepCharcoalText
                                )
                                Text(
                                    text = state.preferredLocation,
                                    fontSize = 13.sp,
                                    color = WarmGrayText
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.toggleEditing() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = ChampagneGold
                            )
                        }
                    }

                    if (state.isEditing) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = CardBorderTint)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Display Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChampagneGold,
                                unfocusedBorderColor = CardBorderTint
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editLocation,
                            onValueChange = { editLocation = it },
                            label = { Text("Preferred Location") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ChampagneGold,
                                unfocusedBorderColor = CardBorderTint
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        HolyLightPrimaryButton(
                            text = "Save Profile",
                            onClick = { viewModel.updateProfile(editName, editLocation) },
                            height = 44.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI & API Status Card
            HolyLightCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = LuminousGoldLight.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Status",
                            tint = ChampagneGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "External Services & AI Integration",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Weather API: Open-Meteo REST Service Active\n• AI Assistant: HTTPS Backend Contract Endpoint\n• Local Database: Room Persistent Storage (Solo Prototype)",
                        fontSize = 12.sp,
                        color = WarmGrayText,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App Information
            HolyLightCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = ChampagneGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "About Application",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepCharcoalText
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "CourtSmash - Badminton Court Booking App\nVersion 1.0.0 (Holy Light Theme Edition)\nBuilt with Kotlin, Jetpack Compose, Material 3, Room & Coroutines.",
                        fontSize = 12.sp,
                        color = WarmGrayText,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clear Data Button
            HolyLightSecondaryButton(
                text = "Clear Local Booking History",
                onClick = { showClearDialog = true }
            )
        }

        // Clear Confirmation Dialog
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = {
                    Text(
                        text = "Clear Local Booking Data?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoalText
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to clear all local booking history? This action will wipe all persisted reservations from Room database.",
                        fontSize = 14.sp,
                        color = WarmGrayText
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearBookingHistory()
                            showClearDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MutedRedError)
                    ) {
                        Text(text = "Clear All Data", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text(text = "Cancel", color = DeepCharcoalText)
                    }
                },
                containerColor = PureWhiteSurface,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
