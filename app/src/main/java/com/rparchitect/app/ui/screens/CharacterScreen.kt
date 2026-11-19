package com.rparchitect.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rparchitect.app.data.model.RPCharacter

private val CharacterBackground = Color(0xFF101116)
private val CharacterCard = Color(0xFF1B1D26)
private val CharacterAccent = Color(0xFF9B8CFF)
private val CharacterSecondary = Color(0xFF9A9BA8)

@Composable
fun CharacterScreen(
    projectName: String,
    characters: List<RPCharacter>,
    onBackClick: () -> Unit,
    onCreateCharacter: () -> Unit,
    onEditCharacter: (RPCharacter) -> Unit,
    onDeleteCharacter: (Long) -> Unit
) {
    var menuCharacter by remember {
        mutableStateOf<RPCharacter?>(null)
    }

    var characterToDelete by remember {
        mutableStateOf<RPCharacter?>(null)
    }

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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "CHARACTER LIBRARY",
                    color = CharacterSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    "${characters.size} characters",
                    color = CharacterSecondary,
                    fontSize = 11.sp
                )
            }

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

        if (characters.isEmpty()) {
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
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = characters,
                    key = { it.id }
                ) { character ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CharacterCard
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(
                                    character.name,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    character.role,
                                    color = CharacterAccent,
                                    fontSize = 11.sp
                                )

                                if (character.age.isNotBlank() ||
                                    character.gender.isNotBlank()
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        listOf(
                                            character.age.takeIf { it.isNotBlank() },
                                            character.gender.takeIf { it.isNotBlank() }
                                        ).filterNotNull().joinToString(" • "),
                                        color = CharacterSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Box {
                                IconButton(
                                    onClick = {
                                        menuCharacter = character
                                    }
                                ) {
                                    Text(
                                        "⋮",
                                        color = Color.White,
                                        fontSize = 24.sp
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuCharacter?.id == character.id,
                                    onDismissRequest = {
                                        menuCharacter = null
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit") },
                                        onClick = {
                                            menuCharacter = null
                                            onEditCharacter(character)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Delete",
                                                color = Color(0xFFE57373)
                                            )
                                        },
                                        onClick = {
                                            menuCharacter = null
                                            characterToDelete = character
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    characterToDelete?.let { character ->
        AlertDialog(
            onDismissRequest = {
                characterToDelete = null
            },
            title = {
                Text("Delete Character?")
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${character.name}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCharacter(character.id)
                        characterToDelete = null
                    }
                ) {
                    Text(
                        "Delete",
                        color = Color(0xFFE57373)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        characterToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
