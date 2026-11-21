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

private val CreateCharacterBackground = Color(0xFF08090B)
private val CreateCharacterCard = Color(0xFF24262B)
private val CreateCharacterAccent = Color(0xFFFFFFFF)
private val CreateCharacterSecondary = Color(0xFFA0A2A8)

private val characterRoles = listOf(
    "Main Character - npc ai controlled",
    "Main Character - user controlled",
    "Supporting Character - npc ai controlled",
    "Antagonist",
    "Protagonist",
    "Love Interest",
    "Side Character",
    "NPC",
    "Custom"
)

@Composable
fun CreateCharacterScreen(
    onBackClick: () -> Unit,
    onCreateClick: (
        name: String,
        role: String,
        age: String,
        gender: String,
        occupation: String,
        personality: String,
        background: String,
        appearance: String,
        notes: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Main Character - npc ai controlled") }
    var customRole by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var personality by remember { mutableStateOf("") }
    var background by remember { mutableStateOf("") }
    var appearance by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var roleExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreateCharacterBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Characters", color = CreateCharacterAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Create Character",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Build a character for your world.",
            color = CreateCharacterSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        CharacterInputLabel("CHARACTER NAME")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter character name") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("ROLE")

        Box {
            OutlinedButton(
                onClick = { roleExpanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    role,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text("⌄", color = CreateCharacterAccent)
            }

            DropdownMenu(
                expanded = roleExpanded,
                onDismissRequest = { roleExpanded = false }
            ) {
                characterRoles.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            role = item
                            roleExpanded = false
                        }
                    )
                }
            }
        }

        if (role == "Custom") {
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = customRole,
                onValueChange = { customRole = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter custom role") },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("AGE")

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter age") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("GENDER")

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter gender") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("OCCUPATION")

        OutlinedTextField(
            value = occupation,
            onValueChange = { occupation = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter occupation") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("PERSONALITY")

        OutlinedTextField(
            value = personality,
            onValueChange = { personality = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Describe personality...") },
            minLines = 3,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("BACKGROUND")

        OutlinedTextField(
            value = background,
            onValueChange = { background = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Character history and background...") },
            minLines = 4,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("APPEARANCE")

        OutlinedTextField(
            value = appearance,
            onValueChange = { appearance = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Physical appearance...") },
            minLines = 3,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterInputLabel("NOTES")

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Additional notes...") },
            minLines = 3,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                val finalRole = if (role == "Custom") {
                    customRole.trim()
                } else {
                    role
                }

                if (name.isNotBlank() && finalRole.isNotBlank()) {
                    onCreateClick(
                        name.trim(),
                        finalRole,
                        age.trim(),
                        gender.trim(),
                        occupation.trim(),
                        personality.trim(),
                        background.trim(),
                        appearance.trim(),
                        notes.trim()
                    )
                }
            },
            enabled = name.isNotBlank() &&
                (role != "Custom" || customRole.isNotBlank()),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CreateCharacterAccent,
                disabledContainerColor = CreateCharacterCard
            )
        ) {
            Text(
                "CREATE CHARACTER",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun CharacterInputLabel(text: String) {
    Text(
        text,
        color = CreateCharacterSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))
}
