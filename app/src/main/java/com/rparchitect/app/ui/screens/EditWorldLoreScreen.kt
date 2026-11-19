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
import com.rparchitect.app.data.model.RPWorldLore

private val EditLoreBackground = Color(0xFF101116)
private val EditLoreCard = Color(0xFF1B1D26)
private val EditLoreAccent = Color(0xFF9B8CFF)
private val EditLoreSecondary = Color(0xFF9A9BA8)

@Composable
fun EditWorldLoreScreen(
    worldLore: RPWorldLore,
    onBackClick: () -> Unit,
    onSaveClick: (
        String,
        String,
        String,
        String,
        String
    ) -> Unit
) {
    var title by remember(worldLore.id) {
        mutableStateOf(worldLore.title)
    }

    var history by remember(worldLore.id) {
        mutableStateOf(worldLore.history)
    }

    var culture by remember(worldLore.id) {
        mutableStateOf(worldLore.culture)
    }

    var worldRules by remember(worldLore.id) {
        mutableStateOf(worldLore.worldRules)
    }

    var notes by remember(worldLore.id) {
        mutableStateOf(worldLore.notes)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EditLoreBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ World Lore", color = EditLoreAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Edit World Lore",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Update your world documentation.",
            color = EditLoreSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        EditLoreInputLabel("TITLE")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter lore title") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = editLoreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditLoreInputLabel("HISTORY")

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
            colors = editLoreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditLoreInputLabel("CULTURE")

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
            colors = editLoreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditLoreInputLabel("WORLD RULES")

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
            colors = editLoreFieldColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditLoreInputLabel("ADDITIONAL NOTES")

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
            colors = editLoreFieldColors()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onSaveClick(
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
                containerColor = EditLoreAccent
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "Save Changes",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun EditLoreInputLabel(label: String) {
    Text(
        label,
        color = EditLoreSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun editLoreFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedContainerColor = EditLoreCard,
        unfocusedContainerColor = EditLoreCard,
        focusedBorderColor = EditLoreAccent,
        unfocusedBorderColor = Color(0xFF353744),
        focusedPlaceholderColor = EditLoreSecondary,
        unfocusedPlaceholderColor = EditLoreSecondary,
        cursorColor = EditLoreAccent
    )
