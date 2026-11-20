
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.RPDocument
import com.rparchitect.app.ui.theme.RPArchitectColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(
    projectName: String,
    documents: List<RPDocument>,
    onBackClick: () -> Unit,
    onCreateDocument: () -> Unit,
    onDocumentClick: (RPDocument) -> Unit,
    onDeleteDocument: (String) -> Unit
) {
    Scaffold(
        containerColor = RPArchitectColors.Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Documents",
                        color = RPArchitectColors.PrimaryText
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(
                            "Back",
                            color = RPArchitectColors.PrimaryText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RPArchitectColors.Background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateDocument,
                containerColor = RPArchitectColors.IconBackground,
                contentColor = RPArchitectColors.PrimaryText
            ) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = projectName,
                style = MaterialTheme.typography.titleMedium,
                color = RPArchitectColors.PrimaryText
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (documents.isEmpty()) {
                Text(
                    text = "No documents yet. Create your first document.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RPArchitectColors.SecondaryText
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = documents,
                        key = { it.id }
                    ) { document ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDocumentClick(document)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = RPArchitectColors.Surface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = document.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = RPArchitectColors.PrimaryText
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = document.templateName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = RPArchitectColors.SecondaryText
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Updated: ${
                                        formatDocumentDate(document.updatedAt)
                                    }",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RPArchitectColors.SecondaryText
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = {
                                        onDeleteDocument(document.id)
                                    }
                                ) {
                                    Text(
                                        "Delete",
                                        color = RPArchitectColors.Error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDocumentDate(timestamp: Long): String {
    return SimpleDateFormat(
        "dd MMM yyyy, HH:mm",
        Locale.getDefault()
    ).format(Date(timestamp))
}
