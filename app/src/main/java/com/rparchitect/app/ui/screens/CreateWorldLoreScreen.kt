package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CreateLoreBackground = Color(0xFF08090B)
private val CreateLoreCard = Color(0xFF24262B)
private val CreateLoreAccent = Color(0xFFFFFFFF)
private val CreateLoreSecondary = Color(0xFF9A9BA8)

@Composable
fun CreateWorldLoreScreen(
    onBackClick: () -> Unit,
    onCreateClick: (
        String,
        String,
        String,
        String,
        String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var history by remember { mutableStateOf("") }
    var culture by remember { mutableStateOf("") }
    var worldRules by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreateLoreBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ World Lore", color = CreateLoreAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Create World Lore",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Document the details of your world.",
            color = CreateLoreSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        LoreInputLabel("TITLE")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter lore title") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = loreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        LoreInputLabel("HISTORY")

        OutlinedTextField(
            value = history,
            onValueChange = { history = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = {
                Text("History, kingdoms, civilizations, major events...")
            },
            shape = RoundedCornerShape(12.dp),
            colors = loreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        LoreInputLabel("CULTURE")

        OutlinedTextField(
            value = culture,
            onValueChange = { culture = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            placeholder = {
                Text("Traditions, beliefs, customs, society...")
            },
            shape = RoundedCornerShape(12.dp),
            colors = loreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        LoreInputLabel("WORLD RULES")

        OutlinedTextField(
            value = worldRules,
            onValueChange = { worldRules = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            placeholder = {
                Text("Laws, magic systems, technology, limitations...")
            },
            shape = RoundedCornerShape(12.dp),
            colors = loreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        LoreInputLabel("ADDITIONAL NOTES")

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            placeholder = {
                Text("Other important information...")
            },
            shape = RoundedCornerShape(12.dp),
            colors = loreFieldColors()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onCreateClick(
                    title.trim(),
                    history.trim(),
                    culture.trim(),
                    worldRules.trim(),
                    notes.trim()
                )
            },
            enabled = title.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CreateLoreAccent
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Create World Lore",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun LoreInputLabel(label: String) {
    Text(
        label,
        color = CreateLoreSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun loreFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = CreateLoreCard,
        unfocusedContainerColor = CreateLoreCard,
        focusedBorderColor = CreateLoreAccent,
        unfocusedBorderColor = Color(0xFF353744),
        focusedPlaceholderColor = CreateLoreSecondary,
        unfocusedPlaceholderColor = CreateLoreSecondary,
        cursorColor = CreateLoreAccent
    )
