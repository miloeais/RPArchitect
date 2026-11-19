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
import com.rparchitect.app.data.model.RPWorldEntry

private val EntryBackground = Color(0xFF101116)
private val EntryCard = Color(0xFF1B1D26)
private val EntryAccent = Color(0xFF9B8CFF)
private val EntrySecondary = Color(0xFF9A9BA8)

@Composable
fun WorldEntryScreen(
    projectName: String,
    entries: List<RPWorldEntry>,
    onBackClick: () -> Unit,
    onCreateEntry: () -> Unit,
    onEditEntry: (RPWorldEntry) -> Unit,
    onDeleteEntry: (Long) -> Unit
) {
    var menuEntry by remember {
        mutableStateOf<RPWorldEntry?>(null)
    }

    var entryToDelete by remember {
        mutableStateOf<RPWorldEntry?>(null)
    }

    val entryNames = remember(entries) {
        entries.associate { it.id to it.name }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EntryBackground)
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("‹ Workspace", color = EntryAccent)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Locations & Factions",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            projectName,
            color = EntrySecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "WORLD DIRECTORY",
                    color = EntrySecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    "${entries.size} records",
                    color = EntrySecondary,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = onCreateEntry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EntryAccent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ New", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "⌖",
                        color = EntryAccent,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "No records yet",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Create locations and organizations for your world.",
                        color = EntrySecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = entries,
                    key = { it.id }
                ) { entry ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = EntryCard
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
                                    entry.name,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    entry.type,
                                    color = EntryAccent,
                                    fontSize = 11.sp
                                )

                                entry.parentId?.let { parentId ->
                                    entryNames[parentId]?.let { parentName ->
                                        Spacer(modifier = Modifier.height(5.dp))

                                        Text(
                                            "Under: $parentName",
                                            color = EntrySecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (entry.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        entry.description.take(100),
                                        color = EntrySecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }

                            Box {
                                IconButton(
                                    onClick = {
                                        menuEntry = entry
                                    }
                                ) {
                                    Text(
                                        "⋮",
                                        color = Color.White,
                                        fontSize = 24.sp
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuEntry?.id == entry.id,
                                    onDismissRequest = {
                                        menuEntry = null
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit") },
                                        onClick = {
                                            menuEntry = null
                                            onEditEntry(entry)
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
                                            menuEntry = null
                                            entryToDelete = entry
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

    entryToDelete?.let { entry ->
        AlertDialog(
            onDismissRequest = {
                entryToDelete = null
            },
            title = {
                Text("Delete Record?")
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${entry.name}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteEntry(entry.id)
                        entryToDelete = null
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
                        entryToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
