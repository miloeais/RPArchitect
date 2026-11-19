package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CharacterBackground = Color(0xFF101116)
private val CharacterCard = Color(0xFF1B1D26)
private val CharacterAccent = Color(0xFF9B8CFF)
private val CharacterSecondary = Color(0xFF9A9BA8)

@Composable
fun CharacterScreen(
    projectName: String,
    onBackClick: () -> Unit,
    onCreateCharacter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CharacterBackground)
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Workspace", color = CharacterAccent)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Characters",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            projectName,
            color = CharacterSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "CHARACTER LIBRARY",
                color = CharacterSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onCreateCharacter,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CharacterAccent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ New", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "♙",
                    color = CharacterAccent,
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "No characters yet",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Create characters for this world.",
                    color = CharacterSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
