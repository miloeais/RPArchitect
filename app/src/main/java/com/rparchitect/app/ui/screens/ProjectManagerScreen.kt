
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
import com.rparchitect.app.data.model.RPProject
import com.rparchitect.app.ui.components.DeleteProjectDialog
import com.rparchitect.app.ui.theme.RPArchitectColors

@Composable
fun ProjectManagerScreen(
    projects: List<RPProject>,
    onBackClick: () -> Unit,
    onCreateProject: () -> Unit,
    onProjectClick: (RPProject) -> Unit,
    onEditProject: (RPProject) -> Unit,
    onDeleteProject: (Long) -> Unit,
    onExportProject: (RPProject) -> Unit
) {
    var menuProject by remember {
        mutableStateOf<RPProject?>(null)
    }

    var projectToDelete by remember {
        mutableStateOf<RPProject?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RPArchitectColors.Background)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    "‹ Back",
                    color = RPArchitectColors.PrimaryText
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "PROJECT MANAGER",
                color = RPArchitectColors.PrimaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "My Projects",
                    color = RPArchitectColors.PrimaryText,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "${projects.size} projects",
                    color = RPArchitectColors.SecondaryText,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onCreateProject,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RPArchitectColors.IconBackground,
                    contentColor = RPArchitectColors.PrimaryText
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ New")
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        if (projects.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "▦",
                        color = RPArchitectColors.SecondaryText,
                        fontSize = 48.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "No projects yet",
                        color = RPArchitectColors.PrimaryText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Create your first roleplay project.",
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
                    items = projects,
                    key = { it.id }
                ) { project ->
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
                                .padding(
                                    start = 16.dp,
                                    end = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 16.dp)
                            ) {
                                TextButton(
                                    onClick = {
                                        onProjectClick(project)
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Column {
                                        Text(
                                            project.name,
                                            color = RPArchitectColors.PrimaryText,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(
                                            modifier = Modifier.height(5.dp)
                                        )

                                        Text(
                                            project.genre,
                                            color = RPArchitectColors.SecondaryText,
                                            fontSize = 11.sp
                                        )

                                        if (project.description.isNotBlank()) {
                                            Spacer(
                                                modifier = Modifier.height(7.dp)
                                            )

                                            Text(
                                                project.description,
                                                color = RPArchitectColors.SecondaryText,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Box {
                                IconButton(
                                    onClick = {
                                        menuProject = project
                                    }
                                ) {
                                    Text(
                                        "⋮",
                                        color = RPArchitectColors.PrimaryText,
                                        fontSize = 24.sp
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuProject?.id == project.id,
                                    onDismissRequest = {
                                        menuProject = null
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
                                            menuProject = null
                                            onEditProject(project)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                "Export Project",
                                                color = RPArchitectColors.PrimaryText
                                            )
                                        },
                                        onClick = {
                                            menuProject = null
                                            onExportProject(project)
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
                                            menuProject = null
                                            projectToDelete = project
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

    projectToDelete?.let { project ->
        DeleteProjectDialog(
            projectName = project.name,
            onDismiss = {
                projectToDelete = null
            },
            onConfirm = {
                onDeleteProject(project.id)
                projectToDelete = null
            }
        )
    }
}
