
package com.rparchitect.app.ui.screens

import androidx.compose.foundation.clickable
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
fun TemplateLibraryScreen(
    templates: List<RPTemplate>,
    onBackClick: () -> Unit,
    onTemplateClick: (RPTemplate) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Template Library")
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
                text = "Choose a template",
                style = MaterialTheme.typography.titleMedium
            )

            templates.forEach { template ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onTemplateClick(template)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = template.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = template.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (template.isCustom) {
                                "Custom Template"
                            } else {
                                "${template.fields.size} fields"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
