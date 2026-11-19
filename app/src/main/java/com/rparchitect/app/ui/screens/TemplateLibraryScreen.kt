
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
    val presetTemplates = templates.filter {
        !it.isCustom
    }

    val customTemplates = templates.filter {
        it.isCustom &&
            !(it.name == "Custom Template" && it.fields.isEmpty())
    }

    val builderTemplate = templates.firstOrNull {
        it.isCustom &&
            it.name == "Custom Template" &&
            it.fields.isEmpty()
    }

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
                text = "Preset Templates",
                style = MaterialTheme.typography.titleMedium
            )

            presetTemplates.forEach { template ->
                TemplateCard(
                    template = template,
                    onClick = {
                        onTemplateClick(template)
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "My Custom Templates",
                style = MaterialTheme.typography.titleMedium
            )

            if (customTemplates.isEmpty()) {
                Text(
                    text = "You haven't created any custom templates yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            customTemplates.forEach { template ->
                TemplateCard(
                    template = template,
                    onClick = {
                        onTemplateClick(template)
                    }
                )
            }

            if (builderTemplate != null) {
                Button(
                    onClick = {
                        onTemplateClick(builderTemplate)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Create Custom Template")
                }
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: RPTemplate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
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
                    "${template.fields.size} fields • Custom Template"
                } else {
                    "${template.fields.size} fields"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
