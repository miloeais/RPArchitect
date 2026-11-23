
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
import com.rparchitect.app.data.model.RPWorldLore
import com.rparchitect.app.ui.theme.RPArchitectColors
import com.rparchitect.app.ui.components.ExportAllButton

@Composable
fun WorldLoreScreen(
    projectName: String,
    worldLores: List<RPWorldLore>,
    onBackClick: () -> Unit,
    onCreateLore: () -> Unit,
    onEditLore: (RPWorldLore) -> Unit,
    onDeleteLore: (Long) -> Unit,
    onExportLore: (RPWorldLore) -> Unit,
    onExportAllLores: () -> Unit
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
            "World Lore",
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
                    "LORE LIBRARY",
                    color = RPArchitectColors.SecondaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Text(
                    "${worldLores.size} documents",
                    color = RPArchitectColors.SecondaryText,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = onCreateLore,
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
            text = "Export All World Lore",
            enabled = worldLores.isNotEmpty(),
            onClick = onExportAllLores
        )

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
                        color = RPArchitectColors.SecondaryText,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "No world lore yet",
                        color = RPArchitectColors.PrimaryText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Build the history and rules of your world.",
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
                    items = worldLores,
                    key = { it.id }
                ) { lore ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
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
                                    lore.title,
                                    color = RPArchitectColors.PrimaryText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    lore.history
                                        .ifBlank {
                                            "No history description"
                                        }
                                        .take(100),
                                    color = RPArchitectColors.SecondaryText,
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
                                        color = RPArchitectColors.PrimaryText,
                                        fontSize = 24.sp
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuLore?.id == lore.id,
                                    onDismissRequest = {
                                        menuLore = null
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
                                            menuLore = null
                                            onEditLore(lore)
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
                                            menuLore = null
                                            onExportLore(lore)
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
                Text(
                    "Delete World Lore?",
                    color = RPArchitectColors.PrimaryText
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${lore.title}\"?",
                    color = RPArchitectColors.SecondaryText
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
                        color = RPArchitectColors.Error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        loreToDelete = null
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
