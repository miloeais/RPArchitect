
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundDark = Color(0xFF101116)
private val CardDark = Color(0xFF1B1D26)
private val AccentColor = Color(0xFF9B8CFF)
private val SecondaryText = Color(0xFF9A9BA8)

@Composable
fun HomeScreen(
    onProjectsClick: () -> Unit,
    onTemplatesClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onRecentProjectsClick: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(20.dp)
    ) {

        Text(
            "RP ARCHITECT",
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp
        )

        Text(
            "ROLEPLAY WORLD BUILDER",
            color = AccentColor,
            fontSize = 9.sp,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            "Welcome to your",
            color = SecondaryText,
            fontSize = 15.sp
        )

        Text(
            "Creative Workspace.",
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Build worlds, develop characters and create stories.",
            color = SecondaryText,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            "WORKSPACE",
            color = SecondaryText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        DashboardCard(
            title = "My Projects",
            description = "Create and manage your roleplay projects",
            symbol = "▦",
            onClick = onProjectsClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard(
            title = "Templates",
            description = "Explore presets and build custom templates",
            symbol = "▤",
            onClick = onTemplatesClick
        )

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(color = CardDark)

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                haptic.performHapticFeedback(
                    HapticFeedbackType.LongPress
                )
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "RP ARCHITECT",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    "Developed by MiloEAIS",
                    color = SecondaryText,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    description: String,
    symbol: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF302B48),
                        RoundedCornerShape(13.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    symbol,
                    color = AccentColor,
                    fontSize = 25.sp
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    description,
                    color = SecondaryText,
                    fontSize = 11.sp
                )
            }

            Text(
                "›",
                color = AccentColor,
                fontSize = 27.sp
            )
        }
    }
}
