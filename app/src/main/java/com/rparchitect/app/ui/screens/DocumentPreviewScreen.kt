
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.export.DocumentPreviewField
import com.rparchitect.app.ui.theme.RPArchitectColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentPreviewScreen(
    title: String,
    templateName: String,
    fields: List<DocumentPreviewField>,
    onBackClick: () -> Unit,
    onExportMarkdown: () -> Unit
) {
    Scaffold(
        containerColor = RPArchitectColors.Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Document Preview",
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
        bottomBar = {
            Surface(
                color = RPArchitectColors.Background,
                tonalElevation = 0.dp
            ) {
                Button(
                    onClick = onExportMarkdown,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RPArchitectColors.IconBackground,
                        contentColor = RPArchitectColors.PrimaryText
                    )
                ) {
                    Text("Export Markdown")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = RPArchitectColors.PrimaryText
            )

            Text(
                text = "Template: $templateName",
                style = MaterialTheme.typography.bodyMedium,
                color = RPArchitectColors.SecondaryText
            )

            HorizontalDivider(
                color = RPArchitectColors.Outline
            )

            fields.forEach { field ->

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = field.label,
                        style = MaterialTheme.typography.titleMedium,
                        color = RPArchitectColors.PrimaryText
                    )

                    Text(
                        text = field.value.ifBlank {
                            "Not filled"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (field.value.isBlank()) {
                            RPArchitectColors.SecondaryText
                        } else {
                            RPArchitectColors.PrimaryText
                        }
                    )
                }
            }
        }
    }
}
