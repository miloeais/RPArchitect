
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rparchitect.app.data.model.RPTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateDetailScreen(
    template: RPTemplate,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(template.name)
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = template.description,
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider()

            Text(
                text = "Template Fields",
                style = MaterialTheme.typography.titleMedium
            )

            if (template.fields.isEmpty()) {
                Text(
                    text = "This template has no predefined fields. " +
                        "You can build your own fields in Custom Template Builder.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            template.fields.forEachIndexed { index, field ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = "${index + 1}. ${field.label}",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            text = "Type: ${field.type.name.replace("_", " ")}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = if (field.required) {
                                "Required"
                            } else {
                                "Optional"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = if (field.required) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )

                        if (field.options.isNotEmpty()) {
                            Text(
                                text = "Options: ${field.options.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
