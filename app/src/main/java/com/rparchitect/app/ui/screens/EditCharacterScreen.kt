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
import com.rparchitect.app.data.model.RPCharacter

private val EditCharacterBackground = Color(0xFF08090B)
private val EditCharacterCard = Color(0xFF24262B)
private val EditCharacterAccent = Color(0xFFFFFFFF)
private val EditCharacterSecondary = Color(0xFFA0A2A8)

private val editCharacterRoles = listOf(
    "Main Character - npc AI controlled",
    "Main Character - user controlled",
    "Supporting Character",
    "Antagonist",
    "Protagonist",
    "Love Interest",
    "Side Character",
    "NPC",
    "Custom"
)

@Composable
fun EditCharacterScreen(
    character: RPCharacter,
    onBackClick: () -> Unit,
    onSaveClick: (
        name: String,
        role: String,
        age: String,
        gender: String,
        personality: String,
        background: String,
        appearance: String,
        notes: String
    ) -> Unit
) {
    var name by remember(character.id) {
        mutableStateOf(character.name)
    }

    var role by remember(character.id) {
        mutableStateOf(
            if (character.role in editCharacterRoles) {
                character.role
            } else {
                "Custom"
            }
        )
    }

    var customRole by remember(character.id) {
        mutableStateOf(
            if (character.role !in editCharacterRoles) {
                character.role
            } else {
                ""
            }
        )
    }

    var age by remember(character.id) {
        mutableStateOf(character.age)
    }

    var gender by remember(character.id) {
        mutableStateOf(character.gender)
    }

    var occupation by remember(occupation.id) {
        mutableStateOf(character.occupation)
    }

    var personality by remember(character.id) {
        mutableStateOf(character.personality)
    }

    var background by remember(character.id) {
        mutableStateOf(character.background)
    }

    var appearance by remember(character.id) {
        mutableStateOf(character.appearance)
    }

    var relationship by remember(relationship.id) {
        mutableStateOf(relationship.gender)
    }

    var notes by remember(character.id) {
        mutableStateOf(character.notes)
    }

    var roleExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EditCharacterBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Characters", color = EditCharacterAccent)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Edit Character",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Update character information.",
            color = EditCharacterSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        CharacterEditLabel("CHARACTER NAME")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Enter character name") }
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("ROLE")

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

                Text("⌄", color = EditCharacterAccent)
            }

            DropdownMenu(
                expanded = roleExpanded,
                onDismissRequest = { roleExpanded = false }
            ) {
                editCharacterRoles.forEach { item ->
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

        CharacterEditLabel("AGE")

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Enter age") }
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("GENDER")

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Enter gender") }
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("occupation")

        OutlinedTextField(
            value = occupation,
            onValueChange = { occupation = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Enter occupation") }
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("PERSONALITY")

        OutlinedTextField(
            value = personality,
            onValueChange = { personality = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("BACKGROUND")

        OutlinedTextField(
            value = background,
            onValueChange = { background = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("APPEARANCE")

        OutlinedTextField(
            value = appearance,
            onValueChange = { appearance = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("RELATIONSHIP")

        OutlinedTextField(
            value = relationship,
            onValueChange = { relationship = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(18.dp))

        CharacterEditLabel("NOTES")

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(30.dp))

        val finalRole = if (role == "Custom") {
            customRole.trim()
        } else {
            role
        }

        Button(
            onClick = {
                onSaveClick(
                    name.trim(),
                    finalRole,
                    age.trim(),
                    gender.trim(),
                    occupation.trim(),
                    personality.trim(),
                    background.trim(),
                    appearance.trim(),
                    relationship.trim(),
                    notes.trim()
                )
            },
            enabled = name.isNotBlank() && finalRole.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EditCharacterAccent,
                disabledContainerColor = EditCharacterCard
            )
        ) {
            Text(
                "SAVE CHANGES",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun CharacterEditLabel(text: String) {
    Text(
        text,
        color = EditCharacterSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))
}
