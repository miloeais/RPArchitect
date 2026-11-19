
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rparchitect.app.data.model.RPProject

private val WorkspaceBackground = Color(0xFF101116)
private val WorkspaceCard = Color(0xFF1B1D26)
private val WorkspaceAccent = Color(0xFF9B8CFF)
private val WorkspaceSecondary = Color(0xFF9A9BA8)

@Composable
fun WorkspaceScreen(
    project: RPProject,
    onBackClick: () -> Unit,
    onCharactersClick: () -> Unit,
    onWorldBuildingClick: () -> Unit,
    onLocationsFactionsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WorkspaceBackground)
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Projects", color = WorkspaceAccent)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            project.name,
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(7.dp))

        Surface(
            color = Color(0xFF302B48),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                project.genre,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
                color = WorkspaceAccent,
                fontSize = 11.sp
            )
        }

        if (project.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                project.description,
                color = WorkspaceSecondary,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            "PROJECT WORKSPACE",
            color = WorkspaceSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        WorkspaceCard(
            title = "Characters",
            description = "Create and manage characters",
            symbol = "♙",
            onClick = onCharactersClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        WorkspaceCard(
            title = "World Lore",
            description = "History, culture and world rules",
            symbol = "◈",
            onClick = onWorldBuildingClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        WorkspaceCard(
            title = "Locations & Factions",
            description = "Places, organizations and governments",
            symbol = "⌖",
            onClick = onLocationsFactionsClick
        )
    }
}

@Composable
private fun WorkspaceCard(
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
            containerColor = WorkspaceCard
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
                    color = WorkspaceAccent,
                    fontSize = 25.sp
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    description,
                    color = WorkspaceSecondary,
                    fontSize = 11.sp
                )
            }

            Text(
                "›",
                color = WorkspaceAccent,
                fontSize = 27.sp
            )
        }
    }
}
