
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rparchitect.app.data.model.RPCharacter
import com.rparchitect.app.ui.theme.RPArchitectColors
import com.rparchitect.app.ui.components.ExportAllButton

@Composable
fun CharacterScreen(
    projectName: String,
    characters: List<RPCharacter>,
    onBackClick: () -> Unit,
    onCreateCharacter: () -> Unit,
    onEditCharacter: (RPCharacter) -> Unit,
    onDeleteCharacter: (Long) -> Unit,
    onExportCharacter: (RPCharacter) -> Unit,
    onExportAllCharacters: () -> Unit
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
            .background(RPArchitectColors.Background)
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text(
                "‹ Workspace",
                color = RPArchitectColors.PrimaryText
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Characters",
            color = RPArchitectColors.PrimaryText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            projectName,
            color = RPArchitectColors.SecondaryText,
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
                    color = RPArchitectColors.SecondaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    "${characters.size} characters",
                    color = RPArchitectColors.SecondaryText,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = onCreateCharacter,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RPArchitectColors.IconBackground,
                    contentColor = RPArchitectColors.PrimaryText
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ New")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ExportAllButton(
            text = "Export All Characters",
            enabled = characters.isNotEmpty(),
            onClick = onExportAllCharacters
        )

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
                        color = RPArchitectColors.SecondaryText,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "No characters yet",
                        color = RPArchitectColors.PrimaryText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Create characters for this world.",
                        color = RPArchitectColors.SecondaryText,
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
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = RPArchitectColors.Surface
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
                                    color = RPArchitectColors.PrimaryText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    character.role,
                                    color = RPArchitectColors.SecondaryText,
                                    fontSize = 11.sp
                                )

                                if (
                                    character.age.isNotBlank() ||
                                    character.gender.isNotBlank()
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        listOf(
                                            character.age.takeIf {
                                                it.isNotBlank()
                                            },
                                            character.gender.takeIf {
                                                it.isNotBlank()
                                            }
                                        ).filterNotNull().joinToString(" • "),
                                        color = RPArchitectColors.SecondaryText,
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
                                        color = RPArchitectColors.PrimaryText,
                                        fontSize = 24.sp
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuCharacter?.id == character.id,
                                    onDismissRequest = {
                                        menuCharacter = null
                                    },
                                    containerColor = RPArchitectColors.Surface
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Edit",
                                                color = RPArchitectColors.PrimaryText
                                            )
                                        },
                                        onClick = {
                                            menuCharacter = null
                                            onEditCharacter(character)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Export Markdown",
                                                color = RPArchitectColors.PrimaryText
                                            )
                                        },
                                        onClick = {
                                            menuCharacter = null
                                            onExportCharacter(character)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Delete",
                                                color = RPArchitectColors.Error
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
                Text(
                    "Delete Character?",
                    color = RPArchitectColors.PrimaryText
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${character.name}\"?",
                    color = RPArchitectColors.SecondaryText
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
                        color = RPArchitectColors.Error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        characterToDelete = null
                    }
                ) {
                    Text(
                        "Cancel",
                        color = RPArchitectColors.SecondaryText
                    )
                }
            },
            containerColor = RPArchitectColors.Surface
        )
    }
}
