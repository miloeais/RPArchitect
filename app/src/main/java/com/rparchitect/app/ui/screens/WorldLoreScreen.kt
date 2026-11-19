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
import com.rparchitect.app.data.model.RPWorldLore

private val LoreBackground = Color(0xFF101116)
private val LoreCard = Color(0xFF1B1D26)
private val LoreAccent = Color(0xFF9B8CFF)
private val LoreSecondary = Color(0xFF9A9BA8)

@Composable
fun WorldLoreScreen(
    projectName: String,
    worldLores: List<RPWorldLore>,
    onBackClick: () -> Unit,
    onCreateLore: () -> Unit,
    onEditLore: (RPWorldLore) -> Unit,
    onDeleteLore: (Long) -> Unit
) {
    var menuLore by remember {
        mutableStateOf<RPWorldLore?>(null)
    }

    var loreToDelete by remember {
        mutableStateOf<RPWorldLore?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoreBackground)
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Workspace", color = LoreAccent)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "World Lore",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            projectName,
            color = LoreSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "LORE LIBRARY",
                    color = LoreSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    "${worldLores.size} documents",
                    color = LoreSecondary,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = onCreateLore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoreAccent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ New", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (worldLores.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "✧",
                        color = LoreAccent,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "No world lore yet",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Build the history and rules of your world.",
                        color = LoreSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = worldLores,
                    key = { it.id }
                ) { lore ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LoreCard
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
                                    lore.title,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    lore.history
                                        .ifBlank { "No history description" }
                                        .take(100),
                                    color = LoreSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }

                            Box {
                                IconButton(
                                    onClick = {
                                        menuLore = lore
                                    }
                                ) {
                                    Text(
                                        "⋮",
                                        color = Color.White,
                                        fontSize = 24.sp
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuLore?.id == lore.id,
                                    onDismissRequest = {
                                        menuLore = null
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit") },
                                        onClick = {
                                            menuLore = null
                                            onEditLore(lore)
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
                                            menuLore = null
                                            loreToDelete = lore
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

    loreToDelete?.let { lore ->
        AlertDialog(
            onDismissRequest = {
                loreToDelete = null
            },
            title = {
                Text("Delete World Lore?")
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${lore.title}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteLore(lore.id)
                        loreToDelete = null
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
                        loreToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
